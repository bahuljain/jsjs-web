package main

import (
	"bytes"
	"encoding/json"
	"errors"
	"io/ioutil"
	"log"
	"net/http"
	"net/url"
	"os"
	"os/exec"
	"strings"
)

const (
	PORT           string = ":4000"
	CODE_DUMP_FILE string = "/tmp/code.jsjs"
	JSJS_ENVVAR    string = "JSJS"
	JS_OUTPUT_FILE string = "out.js"
)

type requestData struct {
	SourceCode string
}

type responseJSON struct {
	Status       int    `json:"status"`
	SourceCode   string `json:"sourceCode"`
	CompiledCode string `json:"compiledCode"`
	Error        string `json:"error"`
}

func validateOrigin(origin string) bool {
	allowedOrigins := map[string]bool{
		"localhost":     true,
		"jsjs-lang.org": true,
	}
	parsedURL, _ := url.Parse(origin)
	host := strings.Split(parsedURL.Host, ":")[0]
	return allowedOrigins[host]
}

// writes the filename
func writeCodeToFile(code string) {
	f, err := os.Create(CODE_DUMP_FILE)
	if err != nil {
		log.Fatal("unable to create tmp file")
	}
	// adding \n to ensure the file is compiled correctly
	b, err := f.WriteString(code + "\n")
	if err != nil {
		log.Fatal("unable to write code to file")
	}
	log.Printf("wrote %d bytes of code to file", b)
	defer f.Close()
}

// runs: cat /tmp/code.jsjs | ./jsjs.out
func compileCode() ([]byte, error) {
	// Collect the output from the command(s)
	var output bytes.Buffer
	var stderr bytes.Buffer
	var err error

	// set working dir to the compiler location
	_ = os.Chdir(os.Getenv(JSJS_ENVVAR))

	// setup the commands
	c1 := exec.Command("cat", CODE_DUMP_FILE)
	c2 := exec.Command("./jsjs.out")

	// setup pipes
	if c2.Stdin, err = c1.StdoutPipe(); err != nil {
		return nil, err
	}

	c2.Stderr = &stderr
	c2.Stdout = &output

	// run them
	log.Print("compiling...")
	if err := c1.Start(); err != nil {
		return nil, err
	}
	if err := c2.Start(); err != nil {
		return nil, err
	}

	if err := c1.Wait(); err != nil {
		return nil, err
	}
	if err := c2.Wait(); err != nil {
		// compiler gave an exit status of 1
		// return compiler error that is stored in stdout
		return nil, errors.New(string(output.Bytes()))
	}

	if stderr.Len() != 0 {
		// handle other compiler execution errors
		return nil, errors.New(string(stderr.Bytes()))
	}

	// read code from "out.js"
	code, err := ioutil.ReadFile(JS_OUTPUT_FILE)
	if err != nil {
		return nil, err
	}
	return code, nil
}

func LogRequestsWrapper(handler http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		log.Printf("%s %s %s", r.RemoteAddr, r.Method, r.URL)
		handler.ServeHTTP(w, r)
	})
}

// incoming request format: curl -X POST -d '{"sourceCode": "print(10 + 50);" }' <url>
func handleCompileFunc(w http.ResponseWriter, r *http.Request) {
	if r.Method == "GET" {
		http.Error(w, "Method not allowed", http.StatusMethodNotAllowed)
		return
	}
	if r.Method == "OPTIONS" {
		origin := r.Header.Get("Origin")
		if validateOrigin(origin) {
			w.Header().Set("Access-Control-Allow-Origin", origin)
			w.Header().Set("Access-Control-Allow-Methods", "POST")
			w.Header().Set("Access-Control-Allow-Headers",
				"Accept, Content-Type, Content-Length, Accept-Encoding, X-CSRF-Token, Authorization")
		}
	}
	if r.Method == "POST" {
		decoder := json.NewDecoder(r.Body)
		var d requestData
		err := decoder.Decode(&d)
		if err != nil {
			log.Print("Unable to parse JSON. Please make sure the json has `sourceCode` key", err)
			return
		}

		writeCodeToFile(d.SourceCode)
		var j responseJSON
		output, err := compileCode()
		j.SourceCode = d.SourceCode
		if err != nil {
			log.Print("unable to compile. " + err.Error())
			j.Status = 1
			j.Error = err.Error()
		} else {
			log.Print("compiled successfully")
			j.Status = 0
			j.CompiledCode = string(output)
		}
		js, err := json.Marshal(j)
		if err != nil {
			http.Error(w, err.Error(), http.StatusInternalServerError)
			return
		}
		w.Header().Set("Access-Control-Allow-Origin", "*")
		w.Header().Set("Content-Type", "application/json")
		w.Write(js)
	}
}

func main() {
	compilerPath := os.Getenv(JSJS_ENVVAR)

	if compilerPath == "" {
		log.Fatal("`JSJS` env variable is unset. Please set it and try again")
	}

	log.Print("Running server on " + PORT)

    /** ROUTES */
	http.HandleFunc("/compile", handleCompileFunc)
    http.Handle("/", http.FileServer(http.Dir("templates")))
    http.Handle("/assets/", http.StripPrefix("/assets/", http.FileServer(http.Dir("static"))))

	log.Fatal(http.ListenAndServe(PORT, LogRequestsWrapper(http.DefaultServeMux)))
}

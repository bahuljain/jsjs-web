#![img](logo.png)

## Frontend
For the front-end, refer to the [gh-pages](https://github.com/bahuljain/jsjs-web/tree/gh-pages) branch.

## Backend
The compiler service is written in Go

## Build & Run ##

Make sure you have the [JSJS](http://github.com/prakhar1989/JSJS) compiler built for your system. To compile JSJS code, this compiler uses the `JSJS` environment variable to search for the binary that compiles the JSJS code, so do set that before you run this.

```shell
$ go run server.go
```

## Using API ##

- hit the route `/compile` to compile your JSJS code to Javascript.

- request body format:
  ```json
  {
    "sourceCode": "....some JSJS code here...."
  }
  ```

- response format:
  ```json
  {
    "status": 0,
    "sourceCode": "...your original JSJS code...",
    "compiledCode": "...equivalent Javascript code after compilation...",
    "error": "...error logs in case of compilation failure...",
  }
  ```

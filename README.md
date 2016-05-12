# JSJS #

![img](logo.png)

## Frontend
For the front-end, refer to the [gh-pages](https://github.com/bahuljain/jsjs-web/tree/gh-pages) branch.

## Backend
The backend for the website is written in Scala using [Scalatra](scalatra.org)

## Build & Run ##

```sh
$ cd JSJS
$ ./sbt
> jetty:start
> browse
```

If `browse` doesn't launch your browser, manually open [http://localhost:8080/](http://localhost:8080/) in your browser.


## Using API ##

- hit the route `/compiler` to compile your JSJS code to Javascript.

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
    "compilationTime": 12412332
  }
  ```

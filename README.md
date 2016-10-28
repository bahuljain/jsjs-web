#![img](logo.png)

## Frontend

###Dev
```
$ npm install
$ npm run watch
```

### Build
```
$ npm run build
```

## Backend
The compiler service is written in Go

## Build & Run ##

Make sure you have the [JSJS](http://github.com/prakhar1989/JSJS) compiler built for your system. To compile JSJS code, this compiler uses the `JSJS` environment variable to search for the binary that compiles the JSJS code, so do set that before you run this.

```shell
$ export JSJS=/my/compiler/location
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

### Deployment

This app is deployed using a container, and uses [this image](https://hub.docker.com/r/prakhar1989/jsjs/).  Any changes in the app require building and publishing a new image and updating the [Dockerfile](Dockerfile), so that it can be deployed on [now](https://zeit.co/now).

A rough order of steps to achieve the above are -

1. Make changes in the app
2. `docker cp` the files to container's `/opt/` path in filesystem.
3. Compile the Go binary.
4. Build a new image with updated version number using `docker commit`.
5. Finally publish it using `docker push`. Update number in the Dockerfile.


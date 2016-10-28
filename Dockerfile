# base image
FROM prakhar1989/jsjs:version1
MAINTAINER Prakhar Srivastav <prakhar@prakhar.me>

LABEL name="jsjs-web"

EXPOSE 4000

ENV JSJS /opt/jsjs

WORKDIR /opt/jsjs-web
CMD ["./server"]

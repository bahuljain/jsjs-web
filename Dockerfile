# base image
FROM prakhar1989/jsjs:version1
MAINTAINER Prakhar Srivastav <prakhar@prakhar.me>

EXPOSE 4000

WORKDIR /opt/jsjs-web

CMD ["JSJS=/opt/jsjs", "./server"]

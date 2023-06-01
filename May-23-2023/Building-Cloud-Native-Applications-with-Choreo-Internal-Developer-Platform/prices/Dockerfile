FROM golang:1.20.2-alpine

WORKDIR /app

COPY *.go ./
COPY go.mod ./

RUN go build -o /main

EXPOSE 8090

USER 10004

CMD [ "/main" ]

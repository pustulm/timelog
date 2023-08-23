FROM golang:1.21.0-alpine3.17

WORKDIR /app

COPY . .

RUN go build -o main

EXPOSE 8000

CMD ["./main"]
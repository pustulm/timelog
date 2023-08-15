FROM golang:1.20

WORKDIR /app

COPY . .

RUN go build -o main

EXPOSE 8080

CMD ["./main"]

//docker build -t log-time:1.0 .

// docker run -d -p 8080:8080 --name log-time:1.0
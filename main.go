package main

import (
	"fmt"
	"net/http"
	"time"
)

func main() {
	http.HandleFunc("/", getTime)
	fmt.Println("Server is running on port 8080")
	http.ListenAndServe(":8080", nil)
}

func getTime(w http.ResponseWriter, r *http.Request) {
	currentTime := time.Now().Format(time.RFC3339)
	logMessage := fmt.Sprintf("Current time: %s", currentTime)

	fmt.Println(logMessage) // Print the log message to the console

	// Send the log message as the response to the client's browser
	w.Header().Set("Content-Type", "text/plain")
	w.Write([]byte(logMessage))
}

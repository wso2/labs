package main

import (
	"encoding/json"
	"fmt"
	"math/rand"
	"net/http"
)

type GetPrice struct {
	ProductPrice int
}

func getPrices(w http.ResponseWriter, req *http.Request) {

	price := rand.Intn(100)
	m := GetPrice{price}
	b, err := json.Marshal(m)
	if err != nil {
		fmt.Printf("Error: %s", err)
		return
	}
	fmt.Fprintf(w, "%s\n", b)
}

func main() {
	http.HandleFunc("/", getPrices)
	http.ListenAndServe(":8090", nil)
}

package util

import (
	"bufio"
	"log"
	"os"
)

type LineHandler func(string)

func ReadFile(name string, handler LineHandler) {
	file, err := os.Open(name)
	if err != nil {
		log.Fatal(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		handler(scanner.Text())
	}

	if err := scanner.Err(); err != nil {
		log.Fatal(err)
	}
}

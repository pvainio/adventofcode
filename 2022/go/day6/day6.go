package main

import (
	"fmt"
	"os"
)

func main() {
	input, _ := os.ReadFile("../../input/day6.txt")
	fmt.Printf("part1: %d\n", findMarker(input, 4))
	fmt.Printf("part2: %d\n", findMarker(input, 14))
}

func findMarker(input []byte, len int) int {
	for i := range input {
		if allDifferent(input[i : i+len]) {
			return i + len
		}
	}
	return -1
}

func allDifferent(s []byte) bool {
	bytes := map[byte]bool{}
	for _, c1 := range s {
		bytes[c1] = true
	}
	return len(bytes) == len(s)
}

package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day3.txt")
	lines := strings.Split(string(input), "\n")

	sum1 := 0
	for _, line := range lines {
		p1, p2 := []byte(line[:len(line)/2]), []byte(line[len(line)/2:])
		sum1 += priority(commonItem(p1, p2)[0])
	}
	fmt.Printf("part 1: %v\n", sum1)

	sum2 := 0
	var group [][]byte
	for _, line := range lines {
		group = append(group, []byte(line))
		if len(group) == 3 {
			sum2 += priority(commonItem(group...)[0])
			group = nil
		}
	}
	fmt.Printf("part 2: %v\n", sum2)
}

func commonItem(values ...[]byte) []byte {
	res := values[0]
	for _, next := range values[1:] {
		res = commonValues(res, next)
	}
	return res
}

func commonValues(a, b []byte) []byte {
	res := []byte{}
	for _, v := range a {
		if contains(b, v) && !contains(res, v) {
			res = append(res, v)
		}
	}
	return res
}

func contains(items []byte, v byte) bool {
	for _, v1 := range items {
		if v1 == v {
			return true
		}
	}
	return false
}

func priority(c byte) int {
	if c < 'a' {
		return int(c - 'A' + 27)
	} else {
		return int(c - 'a' + 1)
	}
}

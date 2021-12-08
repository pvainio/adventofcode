package main

import (
	"aoc2021/util"
	"fmt"
	"strings"
)

// Advent of Code (AOC) 2021 Day 8 part 1
func main() {

	count := 0

	util.ReadFile("../input/8a.txt", func(line string) {
		parts := strings.Split(line, "|")
		output := strings.Fields(parts[1])

		for _, digit := range output {
			// 1==2, 7==3, 4 == 4, 8 == 7
			switch len(digit) {
			case 2:
				count++
			case 3:
				count++
			case 4:
				count++
			case 7:
				count++
			}
		}
	})

	fmt.Printf("%v\n", count)
}

package main

import (
	"aoc2021/util"
	"fmt"
	"log"
	"strconv"
	"strings"
)

// Advent of Code (AOC) 2021 Day 2 part 2
func main() {

	var pos int
	var depth int
	var aim int

	util.ReadFile("../input/2a.txt", func(line string) {
		fields := strings.Fields(line)
		dir := fields[0]
		amount, err := strconv.Atoi(fields[1])

		if err != nil {
			log.Fatalln(err)
		}

		switch dir {
		case "forward":
			pos += amount
			depth += aim * amount
		case "down":
			aim += amount
		case "up":
			aim -= amount
		}
	})

	fmt.Printf("%d %d %d\n", pos, depth, pos*depth)
}

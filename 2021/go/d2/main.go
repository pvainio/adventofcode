package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
	"strings"
)

type command struct {
	dir    string
	amount int
}

// Advent of Code (AOC) 2021 Day 2
func main() {

	var data []command

	util.ReadFile("../input/2a.txt", func(line string) {
		fields := strings.Fields(line)
		amount, _ := strconv.Atoi(fields[1])
		data = append(data, command{dir: fields[0], amount: amount})
	})

	var pos, depth int

	for _, cmd := range data {
		switch cmd.dir {
		case "forward":
			pos += cmd.amount
		case "down":
			depth += cmd.amount
		case "up":
			depth -= cmd.amount
		}
	}

	fmt.Printf("part 1: %d\n", pos*depth)

	pos, depth = 0, 0
	var aim int

	for _, cmd := range data {
		switch cmd.dir {
		case "forward":
			pos += cmd.amount
			depth += aim * cmd.amount
		case "down":
			aim += cmd.amount
		case "up":
			aim -= cmd.amount
		}
	}

	fmt.Printf("part 1: %d\n", pos*depth)
}

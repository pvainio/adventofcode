package main

import (
	"aoc2021/d5"
	"aoc2021/util"
	"fmt"
)

// Advent of Code (AOC) 2021 Day 5 part 2
func main() {

	coord := make(map[int]map[int]int)

	util.ReadFile("../input/5a.txt", func(line string) {
		l := d5.NewLine(line)
		d5.Plot(coord, l)
	})

	fmt.Printf("%v\n", d5.Count(coord))
}

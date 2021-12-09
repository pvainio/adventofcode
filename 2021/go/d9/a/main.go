package main

import (
	"aoc2021/d9"
	"aoc2021/util"
	"fmt"
)

// Advent of Code (AOC) 2021 Day 9 part 1
func main() {

	var hmap []string

	util.ReadFile("../input/9a_test.txt", func(line string) {
		hmap = append(hmap, line)
	})

	lowPoints := d9.LowPoints(hmap)

	sum := 0
	for _, v := range lowPoints {
		sum += int(hmap[v.Y][v.X]) - '0' + 1
	}

	fmt.Printf("%v\n", sum)
}

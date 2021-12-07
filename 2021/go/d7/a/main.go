package main

import (
	"aoc2021/d7"
	"aoc2021/util"
	"fmt"
	"sort"
	"strconv"
	"strings"
)

// Advent of Code (AOC) 2021 Day 7 part 1
func main() {

	var crab []int
	util.ReadFile("../input/7a.txt", func(line string) {
		parts := strings.Split(line, ",")
		for _, str := range parts {
			i, _ := strconv.Atoi(str)
			crab = append(crab, i)
		}
	})

	pos := median(crab)
	fuel := d7.CalculateMinCost(pos, crab, func(dist int) int { return dist })

	fmt.Printf("%v %v\n", pos, fuel)
}

func median(n []int) int {
	sort.Ints(n)

	mNumber := len(n) / 2
	return n[mNumber]
}

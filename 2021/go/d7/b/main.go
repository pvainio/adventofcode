package main

import (
	"aoc2021/d7"
	"aoc2021/util"
	"fmt"
	"strconv"
	"strings"
)

// Advent of Code (AOC) 2021 Day 7 part 2
func main() {

	var crab []int
	util.ReadFile("../input/7a.txt", func(line string) {
		parts := strings.Split(line, ",")
		for _, str := range parts {
			i, _ := strconv.Atoi(str)
			crab = append(crab, i)
		}
	})

	pos := mean(crab)
	fuel := d7.CalculateMinCost(pos, crab, cost)

	fmt.Printf("%v %v\n", pos, fuel)
}

func cost(dist int) int {
	cost := 0
	for i := 1; i <= dist; i++ {
		cost += i
	}
	return cost
}

func mean(n []int) int {
	sum := 0
	for _, v := range n {
		sum += v
	}
	return sum / len(n)
}

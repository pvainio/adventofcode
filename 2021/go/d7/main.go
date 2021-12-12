package main

import (
	"aoc2021/util"
	"fmt"
	"sort"
	"strconv"
	"strings"
)

// Advent of Code (AOC) 2021 Day 7
func main() {

	var crab []int
	util.ReadFile("../input/7a.txt", func(line string) {
		parts := strings.Split(line, ",")
		for _, str := range parts {
			i, _ := strconv.Atoi(str)
			crab = append(crab, i)
		}
	})

	pos1 := median(crab)
	fuel1 := calculateCost(pos1, crab, func(dist int) int { return dist })

	fmt.Printf("part 1: %v\n", fuel1)

	pos2 := mean(crab)
	fuel2 := calculateCost(pos2, crab, cost)

	fmt.Printf("part 2: %v\n", fuel2)
}

func calculateCost(pos int, crab []int, costForDistance func(int) int) int {
	fuel := 0
	for _, c := range crab {
		dist := pos - c
		if dist < 0 {
			dist = -dist
		}
		fuel += costForDistance(dist)
	}
	return fuel
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

func median(n []int) int {
	sort.Ints(n)
	return n[len(n)/2]
}

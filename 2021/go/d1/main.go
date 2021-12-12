package main

import (
	"aoc2021/util"
	"fmt"
	"math"
	"strconv"
)

// Advent of Code (AOC) 2021 Day 1
func main() {

	var prev1 int = math.MaxInt
	var count1 uint = 0

	util.ReadFile("../input/1a.txt", func(line string) {
		depth, _ := strconv.Atoi(line)
		if depth > prev1 && prev1 != math.MaxInt {
			count1++
		}
		prev1 = depth
	})

	fmt.Printf("part 1: %d\n", count1)

	var prev2 int = math.MaxInt
	var count2 uint = 0
	var slider []int

	util.ReadFile("../input/1a.txt", func(line string) {
		depth, _ := strconv.Atoi(line)
		slider = append(slider, depth)
		if len(slider) < 3 {
			return
		} else if len(slider) > 3 {
			slider = slider[1:]
		}

		sum := 0
		for _, i := range slider {
			sum += i
		}

		if sum > prev2 && prev2 != math.MaxInt {
			count2++
		}
		prev2 = sum

	})

	fmt.Printf("part 2: %d\n", count2)
}

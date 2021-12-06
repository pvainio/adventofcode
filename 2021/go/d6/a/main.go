package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
	"strings"
)

// Advent of Code (AOC) 2021 Day 6 part 1
func main() {

	var fish []int
	util.ReadFile("../input/6a.txt", func(line string) {
		parts := strings.Split(line, ",")
		for _, str := range parts {
			i, _ := strconv.Atoi(str)
			fish = append(fish, i)
		}
	})

	for i := 0; i < 80; i++ {
		fish = spawn(fish)
	}

	fmt.Printf("%v\n", len(fish))
}

func spawn(fish []int) []int {
	var res []int
	for _, timer := range fish {
		if timer == 0 {
			res = append(res, 8, 6)
		} else {
			res = append(res, timer-1)
		}
	}
	return res
}

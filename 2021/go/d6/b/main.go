package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
	"strings"
)

// Advent of Code (AOC) 2021 Day 6 part 2
func main() {

	fish := make(map[int]int)
	util.ReadFile("../input/6a.txt", func(line string) {
		parts := strings.Split(line, ",")
		for _, str := range parts {
			i, _ := strconv.Atoi(str)
			fish[i]++
		}
	})

	for i := 0; i < 256; i++ {
		fish = spawn(fish)
	}

	fmt.Printf("%v\n", count(fish))
}

func count(fish map[int]int) int {
	var res int
	for _, count := range fish {
		res += count
	}
	return res
}

func spawn(fish map[int]int) map[int]int {
	res := make(map[int]int)
	for timer, count := range fish {
		if timer == 0 {
			res[8] += count
			res[6] += count
		} else {
			res[timer-1] += count
		}
	}
	return res
}

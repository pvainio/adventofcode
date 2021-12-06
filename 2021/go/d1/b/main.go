package main

import (
	"aoc2021/util"
	"fmt"
	"log"
	"math"
	"strconv"
)

// Advent of Code (AOC) 2021 Day 1 part 2
func main() {

	var prev int = math.MaxInt
	var count uint = 0
	var slider []int

	util.ReadFile("input/1a.txt", func(line string) {
		depth, err := strconv.Atoi(line)
		if err != nil {
			log.Fatalln(err)
		}

		slider = append(slider, depth)

		if len(slider) < 3 {
			return
		}

		if len(slider) > 3 {
			slider = slider[1:]
		}

		sum := 0
		for _, i := range slider {
			sum += i
		}

		if sum > prev && prev != math.MaxInt {
			count++
		}
		prev = sum
	})

	fmt.Printf("%d\n", count)
}

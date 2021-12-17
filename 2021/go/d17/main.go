package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
	"strings"
)

type area struct {
	x1, x2, y1, y2 int
}

// Advent of Code (AOC) 2021 Day 17
func main() {

	var target area
	util.ReadFile("../input/17a.txt", func(line string) {
		parts := strings.Split(line[len("target area:"):], ",")
		target.x1, target.x2 = parseLimit(parts[0])
		target.y2, target.y1 = parseLimit(parts[1])
	})

	maxHeight := 0
	hits := 0

	for xv := 0; xv <= target.x2; xv++ { // iterate x velocity from 0 to limit
		for yv := target.y2; yv < 1000; yv++ { // iterate y velocity from y limit
			hit, height := shoot(xv, yv, target)
			if hit {
				hits++
				maxHeight = max(maxHeight, height)
			}
		}
	}

	fmt.Printf("part 1: %v\n", maxHeight)
	fmt.Printf("part 2: %v\n", hits)
}

func shoot(xv int, yv int, target area) (bool, int) {
	var x, y, maxHeight int
	for {
		x, y = x+xv, y+yv
		xv, yv = max(0, xv-1), yv-1
		maxHeight = max(maxHeight, y)

		if x >= target.x1 && x <= target.x2 && y <= target.y1 && y >= target.y2 {
			return true, maxHeight
		} else if y < target.y2 || x > target.x2 {
			return false, 0
		}
	}
}

func max(a int, b int) int {
	if a > b {
		return a
	} else {
		return b
	}
}

func parseLimit(part string) (int, int) {
	parts := strings.Split(part[3:], "..")
	start, _ := strconv.Atoi(parts[0])
	end, _ := strconv.Atoi(parts[1])
	return start, end
}

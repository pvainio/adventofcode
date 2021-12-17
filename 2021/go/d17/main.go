package main

import (
	"aoc2021/util"
	"fmt"
	"math"
	"strconv"
	"strings"
)

type xy struct {
	x, y int
}

type area struct {
	start, end xy
}

// Advent of Code (AOC) 2021 Day 17
func main() {

	var target area
	util.ReadFile("../input/17a.txt", func(line string) {
		parts := strings.Split(line[len("target area:"):], ",")
		target.start.x, target.end.x = parseLimit(parts[0])
		target.start.y, target.end.y = parseLimit(parts[1])
	})

	maxHeight := 0
	hits := map[xy]bool{}

xloop:
	for xv := 0; xv <= target.end.x; xv++ { // iterate x velocity from 0 to limit
		for yv := target.end.y; ; yv++ { // iterate y velocity from y limit
			hit, height := shoot(xv, yv, target)
			if hit == HIT {
				hits[xy{xv, yv}] = true // count hits
				if maxHeight < height {
					maxHeight = height // found new max height
				}
			} else if hit == LONG || hit == HIGH_YVEL {
				continue xloop // shooting long or so high y velocity it missis the target
			}
		}
	}

	fmt.Printf("part 1: %v\n", maxHeight)
	fmt.Printf("part 2: %v\n", len(hits))
}

const (
	HIT       int = 0
	LONG      int = 1
	SHORT     int = 2
	HIGH_YVEL int = 3
)

func shoot(xv int, yv int, target area) (int, int) {
	var pos, prev xy
	var maxHeight int
	for step := 1; ; step++ {
		pos.x += xv
		pos.y += yv
		if xv > 0 {
			xv -= 1
		}
		yv -= 1
		if pos.y > maxHeight {
			maxHeight = pos.y
		}

		if pos.x >= target.start.x && pos.x <= target.end.x && pos.y <= target.start.y && pos.y >= target.end.y {
			return HIT, maxHeight
		} else if pos.y < target.end.y && prev.y > target.start.y && prev.y-pos.y > (target.start.y-target.end.y)*2 {
			return HIGH_YVEL, 0 // y velocity so high it missing the target withing one step
		} else if pos.y < target.end.y {
			return SHORT, 0
		} else if pos.x > target.end.x {
			return LONG, 0
		}
		prev = pos
	}
}

func parseLimit(part string) (int, int) {
	parts := strings.Split(part[3:], "..")
	start, _ := strconv.Atoi(parts[0])
	end, _ := strconv.Atoi(parts[1])
	if math.Abs(float64(start)) > math.Abs(float64(end)) {
		start, end = end, start
	}
	return start, end
}

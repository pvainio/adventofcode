package main

import (
	"aoc2021/util"
	"fmt"
	"math"
	"strconv"
	"strings"
)

type Area struct {
	x1, x2, y1, y2 float64
}

type Probe struct {
	x, y, xv, yv, maxHeight float64
}

// Advent of Code (AOC) 2021 Day 17
func main() {

	var target Area
	util.ReadFile("../input/17a.txt", func(line string) {
		parts := strings.Split(line[len("target area:"):], ",")
		target.x1, target.x2 = parseLimit(parts[0])
		target.y2, target.y1 = parseLimit(parts[1])
	})

	hits := 0.
	maxHeight := 0.

	for xv := 0.; xv <= target.x2; xv++ { // iterate x velocity from 0 to limit
		for yv := target.y2; yv < 1000; yv++ { // iterate y velocity from y limit
			probe := shoot(Probe{xv: xv, yv: yv}, target)
			if probe.hit(target) { // count only hits
				hits++
				maxHeight = math.Max(maxHeight, probe.maxHeight)
			}
		}
	}

	fmt.Printf("part 1: %v\n", maxHeight)
	fmt.Printf("part 2: %v\n", hits)
}

func shoot(probe Probe, target Area) Probe {
	for !probe.miss(target) && !probe.hit(target) {
		probe = probe.move() // move until hit or miss
	}
	return probe
}

func (p Probe) move() Probe {
	return Probe{p.x + p.xv, p.y + p.yv, math.Max(0, p.xv-1), p.yv - 1, math.Max(p.maxHeight, p.y)}
}

func (p Probe) hit(target Area) bool {
	return p.x >= target.x1 && p.x <= target.x2 && p.y <= target.y1 && p.y >= target.y2
}

func (p Probe) miss(target Area) bool {
	return p.y < target.y2 || p.x > target.x2
}

func parseLimit(part string) (float64, float64) {
	parts := strings.Split(part[3:], "..")
	start, _ := strconv.ParseFloat(parts[0], 32)
	end, _ := strconv.ParseFloat(parts[1], 32)
	return start, end
}

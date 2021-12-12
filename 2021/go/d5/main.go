package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
	"strings"
)

type Point struct {
	X, Y int
}

type Line struct {
	P1, P2 Point
}

// Advent of Code (AOC) 2021 Day 5
func main() {

	coord1 := make(map[int]map[int]int)
	coord2 := make(map[int]map[int]int)

	util.ReadFile("../input/5a.txt", func(line string) {
		l := NewLine(line)
		if l.P1.X == l.P2.X || l.P1.Y == l.P2.Y {
			Plot(coord1, l)
		}
		Plot(coord2, l)
	})

	fmt.Printf("part 1: %v\n", Count(coord1))
	fmt.Printf("part 2: %v\n", Count(coord2))
}

func NewPoint(pointString string) Point {
	parts := strings.Split(pointString, ",")
	x, _ := strconv.Atoi(parts[0])
	y, _ := strconv.Atoi(parts[1])
	return Point{X: x, Y: y}
}

func NewLine(lineString string) Line {
	parts := strings.Split(lineString, " -> ")
	return Line{P1: NewPoint(parts[0]), P2: NewPoint(parts[1])}
}

func numbersBetween(a int, b int) []int {
	var result []int
	if a > b {
		for i := a; i >= b; i-- {
			result = append(result, i)
		}
	} else {
		for i := a; i <= b; i++ {
			result = append(result, i)
		}
	}
	return result
}

func PlotPoint(coord map[int]map[int]int, x int, y int) {
	row := coord[y]
	if row == nil {
		row = make(map[int]int)
		coord[y] = row
	}
	row[x]++
}

func Plot(coord map[int]map[int]int, line Line) {
	xs := numbersBetween(line.P1.X, line.P2.X)
	ys := numbersBetween(line.P1.Y, line.P2.Y)

	xl := len(xs)
	yl := len(ys)

	len := xl
	if yl > xl {
		len = yl
	}

	for i := 0; i < len; i++ {
		var x, y int
		if i < xl {
			x = xs[i]
		} else {
			x = xs[xl-1]
		}
		if i < yl {
			y = ys[i]
		} else {
			y = ys[yl-1]
		}
		PlotPoint(coord, x, y)
	}
}

func Count(coord map[int]map[int]int) int {
	count := 0
	for _, row := range coord {
		for _, val := range row {
			if val > 1 {
				count++
			}
		}
	}
	return count
}

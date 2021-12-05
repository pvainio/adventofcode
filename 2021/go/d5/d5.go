package d5

import (
	"strconv"
	"strings"
)

type Point struct {
	X, Y int
}

type Line struct {
	P1, P2 Point
}

func NewPoint(pointString string) Point {
	parts := strings.Split(pointString, ",")
	var p Point
	p.X, _ = strconv.Atoi(parts[0])
	p.Y, _ = strconv.Atoi(parts[1])
	return p
}

func NewLine(lineString string) Line {
	parts := strings.Split(lineString, " -> ")
	var line Line
	line.P1 = NewPoint(parts[0])
	line.P2 = NewPoint(parts[1])
	return line
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

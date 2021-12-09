package main

import (
	"aoc2021/d9"
	"aoc2021/util"
	"fmt"
	"sort"
)

type empty struct{}

// Advent of Code (AOC) 2021 Day 9 part 2
func main() {

	var hmap []string

	util.ReadFile("../input/9a.txt", func(line string) {
		hmap = append(hmap, line)
	})

	found := basins(hmap)
	sizes := []int{}
	for _, basin := range found {
		sizes = append(sizes, len(basin))
	}
	sort.Ints(sizes)
	sizes = sizes[len(sizes)-3:]
	multiply := 1
	for _, size := range sizes {
		multiply *= size
	}

	fmt.Printf("%v\n", multiply)
}

func basins(hmap []string) []map[d9.Point]empty {
	lowPoints := d9.LowPoints(hmap)

	var found []map[d9.Point]empty

	for _, p := range lowPoints {
		basin := basin(p, hmap, make(map[d9.Point]empty))
		found = append(found, basin)
	}
	return found
}

func basin(p d9.Point, hmap []string, included map[d9.Point]empty) map[d9.Point]empty {
	height := d9.Height(p.X, p.Y, hmap)
	_, visited := included[p]
	if height >= '9' || visited {
		return make(map[d9.Point]empty)
	}

	included[p] = empty{}

	basin(d9.Point{X: p.X + 1, Y: p.Y}, hmap, included)
	basin(d9.Point{X: p.X - 1, Y: p.Y}, hmap, included)
	basin(d9.Point{X: p.X, Y: p.Y + 1}, hmap, included)
	basin(d9.Point{X: p.X, Y: p.Y - 1}, hmap, included)

	return included
}

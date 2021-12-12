package main

import (
	"aoc2021/util"
	"fmt"
	"sort"
)

type point struct {
	x, y int
}

type empty struct{}

// Advent of Code (AOC) 2021 Day 9
func main() {

	var heightMap []string

	util.ReadFile("../input/9a.txt", func(line string) {
		heightMap = append(heightMap, line)
	})

	lowPoints := lowPoints(heightMap)

	sum := 0
	for _, v := range lowPoints {
		sum += int(heightMap[v.y][v.x]) - '0' + 1
	}

	fmt.Printf("part 1: %v\n", sum)

	found := basins(heightMap)
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

	fmt.Printf("part 2: %v\n", multiply)
}

func basins(hmap []string) []map[point]empty {
	lowPoints := lowPoints(hmap)

	var found []map[point]empty
	for _, p := range lowPoints {
		basin := basin(p, hmap, make(map[point]empty))
		found = append(found, basin)
	}
	return found
}

func basin(p point, hmap []string, included map[point]empty) map[point]empty {
	height := height(p.x, p.y, hmap)
	_, visited := included[p]
	if height >= '9' || visited {
		return make(map[point]empty)
	}

	included[p] = empty{}

	basin(point{x: p.x + 1, y: p.y}, hmap, included)
	basin(point{x: p.x - 1, y: p.y}, hmap, included)
	basin(point{x: p.x, y: p.y + 1}, hmap, included)
	basin(point{x: p.x, y: p.y - 1}, hmap, included)

	return included
}

func lowPoints(hmap []string) []point {
	lowPoints := []point{}
	for y, row := range hmap {
		for x := range row {
			adjacentMin := min(height(x-1, y, hmap), height(x+1, y, hmap), height(x, y-1, hmap), height(x, y+1, hmap))
			if row[x] < adjacentMin {
				lowPoints = append(lowPoints, point{x: x, y: y})
			}
		}
	}
	return lowPoints
}

func min(h ...byte) byte {
	var m byte = 128
	for _, v := range h {
		if v < m {
			m = v
		}
	}
	return m
}

func height(x int, y int, hmap []string) byte {
	if x < 0 || y < 0 || y >= len(hmap) || x >= len(hmap[y]) {
		return '9'
	}
	return hmap[y][x]
}

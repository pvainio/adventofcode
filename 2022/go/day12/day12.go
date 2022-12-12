package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day12.txt")
	height, start, end := readMap(string(input))

	part1 := shortestPath(height, map[xy]int{}, visit{start, 0},
		func(dh int) bool { return dh <= 1 },
		func(p xy) bool { return end == p })

	part2 := shortestPath(height, map[xy]int{}, visit{end, 0},
		func(dh int) bool { return dh >= -1 },
		func(p xy) bool { return height[p] == 'a' })

	fmt.Printf("part1: %v\npart2: %v\n", part1, part2)
}

func shortestPath(height map[xy]int, visits map[xy]int, loc visit, ok func(d int) bool, target func(xy) bool) int {
	min := 9999
	if target(loc.xy) {
		return loc.distance
	} else if old, visited := visits[loc.xy]; visited && old <= loc.distance {
		return min
	}
	visits[loc.xy] = loc.distance
	for _, m := range []xy{{-1, 0}, {1, 0}, {0, -1}, {0, 1}} {
		next := visit{xy{loc.x + m.x, loc.y + m.y}, loc.distance + 1}
		if h, inside := height[next.xy]; inside {
			deltaH := h - height[loc.xy]
			if !ok(deltaH) {
				continue
			}
			d := shortestPath(height, visits, next, ok, target)
			if d < min {
				min = d
			}
		}
	}
	return min
}

func readMap(input string) (height map[xy]int, start, end xy) {
	rows := strings.Split(input, "\n")
	height = map[xy]int{}
	for y, row := range rows {
		for x, c := range row {
			if c == 'S' {
				start = xy{x, y}
				c = 'a'
			} else if c == 'E' {
				end = xy{x, y}
				c = 'z'
			}
			height[xy{x, y}] = int(c)
		}
	}
	return height, start, end
}

type visit struct {
	xy
	distance int
}

type xy struct {
	x, y int
}

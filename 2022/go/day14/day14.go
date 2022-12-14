package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day14.txt")

	fmt.Printf("part1: %v\n", pourSand(input, false))
	fmt.Printf("part2: %v\n", pourSand(input, true))
}

func pourSand(input []byte, useFoor bool) (count int) {
	cave := readRocks(string(input))
	floor := 0
	if useFoor {
		floor = bottom(cave)
	}
	var start = xy{500, 0}
	count = -1
	for count != countSand(cave) {
		count = countSand(cave)
		dropSand(cave, floor, start)
	}
	return count
}

func countSand(cave map[xy]byte) (count int) {
	for _, i := range cave {
		if i == 'o' {
			count++
		}
	}
	return count
}

func dropSand(cave map[xy]byte, floor int, sand xy) {
	if sand.y != floor-1 && sand.y < 1000 { // Try if sand can be moved down
		for _, v := range moves {
			try := xy{sand.x + v.x, sand.y + v.y}
			if _, found := cave[try]; !found {
				dropSand(cave, floor, try) // can be moved, try next
				return
			}
		}
	}

	if _, support := cave[xy{sand.x, sand.y + 1}]; support || sand.y+1 == floor {
		cave[sand] = 'o' // support below, place sand
	}
}

func bottom(cave map[xy]byte) (res int) {
	for p := range cave {
		if p.y > res {
			res = p.y
		}
	}
	return res + 2
}

var moves = []xy{{0, 1}, {-1, 1}, {1, 1}}

func readRocks(input string) map[xy]byte {
	lines := strings.Split(input, "\n")
	rocks := map[xy]byte{}
	for _, line := range lines {
		points := strings.Split(line, " -> ")
		for i := 1; i < len(points); i++ {
			var p1, p2 xy
			fmt.Sscanf(points[i-1], "%d,%d", &p1.x, &p1.y)
			fmt.Sscanf(points[i], "%d,%d", &p2.x, &p2.y)
			draw(rocks, p1, p2)
		}
	}
	return rocks
}

func draw(rocks map[xy]byte, p1, p2 xy) {
	rocks[p1] = '#'
	if p1 == p2 {
		return
	}

	if p1.x == p2.x && p1.y < p2.y {
		draw(rocks, xy{p1.x, p1.y + 1}, p2)
	} else if p1.x == p2.x && p1.y > p2.y {
		draw(rocks, xy{p1.x, p1.y - 1}, p2)
	} else if p1.y == p2.y && p1.x < p2.x {
		draw(rocks, xy{p1.x + 1, p1.y}, p2)
	} else if p1.y == p2.y && p1.x > p2.x {
		draw(rocks, xy{p1.x - 1, p1.y}, p2)
	}
}

type xy struct{ x, y int }

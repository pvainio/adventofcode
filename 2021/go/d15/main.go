package main

import (
	"aoc2021/util"
	"fmt"
)

type point struct {
	x, y int
}

// Advent of Code (AOC) 2021 Day 15
func main() {

	riskMap := readMap("../input/15a.txt")

	part1 := shortestPath(point{x: 0, y: 0}, point{x: len(riskMap[0]), y: len(riskMap)}, riskMap)
	fmt.Printf("part 1: %v\n", part1)

	riskMap = createPart2Map(riskMap)

	part2 := shortestPath(point{x: 0, y: 0}, point{x: len(riskMap[0]), y: len(riskMap)}, riskMap)
	fmt.Printf("part 2: %v\n", part2)
}

func shortestPath(from point, to point, riskMap [][]int) int {
	existingRisk := makeTable(len(riskMap), len(riskMap[0]))
	visit := map[point]int{from: 0} // points to visit mapped to risk
	for len(visit) > 0 {            // loop while we have points to visit
		p, risk := takeOne(visit)
		if existing := existingRisk[p.y][p.x]; existing == 0 || existing > risk {
			existingRisk[p.y][p.x] = risk                         // found new or better solution, update it
			for _, adjacent := range adjacentPoints(p, riskMap) { // loop adjacent points
				adjacentRisk := riskMap[adjacent.y][adjacent.x] + risk
				if r, ok := visit[adjacent]; !ok || adjacentRisk < r {
					// adjacent point not in visit list or lower risk than existing entry, update it
					visit[adjacent] = adjacentRisk
				}
			}
		}
	}
	return existingRisk[to.y-1][to.x-1]
}

func adjacentPoints(p point, riskMap [][]int) []point {
	points := []point{}
	for _, a := range []point{p.adj(-1, 0), p.adj(1, 0), p.adj(0, -1), p.adj(0, 1)} {
		if a.x >= 0 && a.y >= 0 && a.y < len(riskMap) && a.x < len(riskMap[a.y]) {
			points = append(points, a)
		}
	}
	return points
}

func createPart2Map(source [][]int) [][]int {
	target := makeTable(len(source)*5, len(source[0])*5)
	for y := 0; y < 5; y++ {
		for x := 0; x < 5; x++ {
			fillMap(target, x, y, source)
		}
	}
	return target
}

func fillMap(target [][]int, px int, py int, source [][]int) {
	height := len(source)
	width := len(source[0])
	for y, row := range source {
		for x, risk := range row {
			target[py*height+y][px*width+x] = (risk+px+py-1)%9 + 1 // if over 9 rotate to 1
		}
	}
}

func (p *point) adj(h int, v int) point {
	return point{x: p.x + h, y: p.y + v}
}

func takeOne(riskMap map[point]int) (point, int) {
	for k, v := range riskMap {
		delete(riskMap, k)
		return k, v
	}
	return point{}, 0
}

func readMap(file string) [][]int {
	riskMap := [][]int{}
	util.ReadFile(file, func(line string) {
		row := []int{}
		for _, c := range line {
			row = append(row, int(c-'0'))
		}
		riskMap = append(riskMap, row)
	})
	return riskMap
}

func makeTable(height int, width int) [][]int {
	table := make([][]int, height)
	for i := 0; i < height; i++ {
		table[i] = make([]int, width)
	}
	return table
}

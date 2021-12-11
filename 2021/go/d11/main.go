package main

import (
	"aoc2021/util"
	"fmt"
)

// Advent of Code (AOC) 2021 Day 11 part 1 and part 2
func main() {

	var eMap [][]byte

	util.ReadFile("../input/11a.txt", func(line string) {
		row := []byte{}
		for _, c := range line {
			row = append(row, byte(c-'0'))
		}
		eMap = append(eMap, row)
	})

	count := 0
	for x := 0; x < 100; x++ {
		count += step(eMap)
	}

	fmt.Printf("part 1: %v\n", count)

	steps := 100
	for !allFlashes(eMap) {
		step(eMap)
		steps++
	}
	fmt.Printf("part 2: %v\n", steps)
}

func allFlashes(eMap [][]byte) bool {
	for y := 0; y < len(eMap); y++ {
		for x := 0; x < len(eMap[y]); x++ {
			if eMap[y][x] != 0 {
				return false
			}
		}
	}
	return true
}

func step(eMap [][]byte) int {
	// increase all by one
	for y := 0; y < len(eMap); y++ {
		for x := 0; x < len(eMap[y]); x++ {
			eMap[y][x]++
		}
	}

	// count flashes
	count := 0
	for y := 0; y < len(eMap); y++ {
		for x := 0; x < len(eMap[y]); x++ {
			count += flash(eMap, x, y)
		}
	}
	return count
}

func flash(eMap [][]byte, x int, y int) int {
	if eMap[y][x] < 10 { // Flash 10 or above
		return 0
	}

	eMap[y][x] = 0
	count := 1
	for ny := y - 1; ny <= y+1; ny++ {
		for nx := x - 1; nx <= x+1; nx++ {
			if nx < 0 || ny < 0 || ny >= len(eMap) || nx >= len(eMap[ny]) || eMap[ny][nx] == 0 {
				continue // Skip if out of energy map or already flashed this step (==0)
			}
			eMap[ny][nx]++
			count += flash(eMap, nx, ny)
		}
	}
	return count
}

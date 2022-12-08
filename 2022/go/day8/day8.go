package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day8.txt")
	heights := parseMap(string(input))

	fmt.Printf("part1 : %d\n", countVisible(heights))
	fmt.Printf("part2 : %d\n", highestScenicScore(heights))
}

func highestScenicScore(heights [][]byte) (max int) {
	for y, row := range heights {
		for x := range row {
			score := scenicScore(heights, y, x)
			if score > max {
				max = score
			}
		}
	}
	return max
}

func scenicScore(heights [][]byte, y, x int) (score int) {
	up := visibleTrees(heights, y, x, func(x, y int) (int, int) { return x, y - 1 })
	down := visibleTrees(heights, y, x, func(x, y int) (int, int) { return x, y + 1 })
	left := visibleTrees(heights, y, x, func(x, y int) (int, int) { return x - 1, y })
	right := visibleTrees(heights, y, x, func(x, y int) (int, int) { return x + 1, y })
	return up * down * left * right
}

func visibleTrees(heights [][]byte, oy, ox int, move func(x, y int) (int, int)) (count int) {
	for x, y := move(ox, oy); inside(heights, y, x); x, y = move(x, y) {
		count++
		if heights[y][x] >= heights[oy][ox] {
			return count
		}
	}
	return count
}

func inside(heights [][]byte, y, x int) bool {
	return y >= 0 && y < len(heights) && x >= 0 && x < len(heights[y])
}

func countVisible(heights [][]byte) (sum int) {
	for y, row := range heights {
		for x := range row {
			if visible(heights, y, x) {
				sum++
			}
		}
	}
	return sum
}

func visible(heights [][]byte, y, x int) bool {
	if y == 0 || y == len(heights)-1 || x == 0 || x == len(heights[y])-1 {
		return true // edge cases
	}
	height := heights[y][x]
	row := heights[y]

	if max(row[:x]) < height || max(row[x+1:]) < height {
		return true
	}
	col := column(heights, x)
	if max(col[:y]) < height || max(col[y+1:]) < height {
		return true
	}
	return false
}

func column(heights [][]byte, x int) (col []byte) {
	for y := 0; y < len(heights); y++ {
		col = append(col, heights[y][x])
	}
	return col
}

func max(data []byte) (max byte) {
	for _, b := range data {
		if b > max {
			max = b
		}
	}
	return max
}

func parseMap(input string) (heights [][]byte) {
	lines := strings.Split(input, "\n")
	for _, line := range lines {
		var row []byte
		for _, c := range line {
			row = append(row, byte(c-'0'))
		}
		heights = append(heights, row)
	}
	return heights
}

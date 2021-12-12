package main

import (
	"aoc2021/util"
	"fmt"
	"reflect"
	"strconv"
	"strings"
)

type board struct {
	numbers [][]int
	marks   []int
}

// Advent of Code (AOC) 2021 Day 4
func main() {
	var data []string

	util.ReadFile("../input/4a.txt", func(line string) {
		data = append(data, line)
	})

	var numberData = strings.Split(data[0], ",")

	boards := parseBoards(data[1:])
	winner1 := play(numberData, boards)

	fmt.Printf("part 1: %v\n", winner1.score())

	boards = parseBoards(data[1:])
	var winner2 board
	for len(boards) > 0 {
		res := []board{}
		winner2 = play(numberData, boards)
		for _, b := range boards {
			if !reflect.DeepEqual(b, winner2) {
				res = append(res, b)
			}
		}
		boards = res
	}

	fmt.Printf("part 2: %v\n", winner2.score())
}

func (me *board) score() int {
	return me.sumUnmarked() * me.marks[len(me.marks)-1]
}

func (me *board) mark(n int) bool {
	for y, row := range me.numbers {
		for x, v := range row {
			if v == n {
				me.numbers[y][x] = -1
				me.marks = append(me.marks, n)
				return me.bingo()
			}
		}
	}
	return false
}

func (me *board) bingo() bool {
	for i := 0; i < 5; i++ {
		rcount := 0
		ccount := 0
		for j := 0; j < 5; j++ {
			if me.numbers[i][j] == -1 {
				rcount++
			}
			if me.numbers[j][i] == -1 {
				ccount++
			}
			if rcount > 4 || ccount > 4 {
				return true
			}
		}
	}
	return false
}

func (me *board) sumUnmarked() int {
	sum := 0
	for _, row := range me.numbers {
		for _, v := range row {
			if v != -1 {
				sum += v
			}
		}
	}
	return sum
}

func play(numbers []string, boards []board) board {
	for _, n := range numbers {
		for idx, b := range boards {
			ni, _ := strconv.Atoi(n)
			if b.mark(ni) {
				boards[idx] = b
				return b
			}
		}
	}
	return board{}
}

func parseBoards(data []string) []board {
	var boards []board

	var b *board
	for _, line := range data {
		if line == "" {
			boards = append(boards, board{})
			b = &boards[len(boards)-1]
		} else {
			row := strings.Fields(line)
			var rowInts []int
			for _, n := range row {
				ni, _ := strconv.Atoi(n)
				rowInts = append(rowInts, ni)
			}
			b.numbers = append(b.numbers, rowInts)
		}
	}

	return boards
}

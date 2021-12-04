package d4

import (
	"strconv"
	"strings"
)

type Board struct {
	Numbers [][]int
	Marks   []int
}

func (me *Board) Mark(n int) bool {
	for y, row := range me.Numbers {
		for x, v := range row {
			if v == n {
				me.Numbers[y][x] = -1
				me.Marks = append(me.Marks, n)
				return me.Bingo()
			}
		}
	}
	return false
}

func (me *Board) Bingo() bool {
	for i := 0; i < 5; i++ {
		rcount := 0
		ccount := 0
		for j := 0; j < 5; j++ {
			if me.Numbers[i][j] == -1 {
				rcount++
			}
			if me.Numbers[j][i] == -1 {
				ccount++
			}
			if rcount > 4 || ccount > 4 {
				return true
			}
		}
	}
	return false
}

func (me *Board) SumUnmarked() int {
	sum := 0
	for _, row := range me.Numbers {
		for _, v := range row {
			if v != -1 {
				sum += v
			}
		}
	}
	return sum
}

func Play(numbers []string, boards []Board) Board {
	for _, n := range numbers {
		for idx, b := range boards {
			ni, _ := strconv.Atoi(n)
			if b.Mark(ni) {
				boards[idx] = b
				return b
			}
		}
	}
	return Board{}
}

func ParseBoards(data []string) []Board {
	var boards []Board

	for _, line := range data {
		var board *Board
		if line == "" {
			board = &Board{}
			boards = append(boards, *board)
		} else {
			board = &boards[len(boards)-1]
		}
		row := strings.Fields(line)
		var rowInts []int
		for _, n := range row {
			ni, _ := strconv.Atoi(n)
			rowInts = append(rowInts, ni)
		}
		board.Numbers = append(board.Numbers, rowInts)
	}

	return boards
}

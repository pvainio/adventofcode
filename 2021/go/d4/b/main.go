package main

import (
	"aoc2021/d4"
	"aoc2021/util"
	"fmt"
	"reflect"
	"strings"
)

// Advent of Code (AOC) 2021 Day 4 part 2
func main() {

	var data []string

	util.ReadFile("../input/4a.txt", func(line string) {
		data = append(data, line)
	})

	var numberData = strings.Split(data[0], ",")
	var boardData = data[1:]

	boards := d4.ParseBoards(boardData)

	var winner d4.Board
	for len(boards) > 0 {
		res := []d4.Board{}
		winner = d4.Play(numberData, boards)
		for _, b := range boards {
			if !reflect.DeepEqual(b, winner) {
				res = append(res, b)
			}
		}
		boards = res
	}

	fmt.Printf("%v\n", winner.SumUnmarked()*winner.Marks[len(winner.Marks)-1])
}

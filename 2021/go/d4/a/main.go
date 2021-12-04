package main

import (
	"aoc2021/d4"
	"aoc2021/util"
	"fmt"
	"strings"
)

func main() {

	var data []string

	util.ReadFile("../input/4a.txt", func(line string) {
		data = append(data, line)
	})

	var numberData = strings.Split(data[0], ",")
	var boardData = data[1:]

	boards := d4.ParseBoards(boardData)

	winner := d4.Play(numberData, boards)

	fmt.Printf("%v\n", winner.SumUnmarked()*winner.Marks[len(winner.Marks)-1])
}

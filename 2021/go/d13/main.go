package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
	"strings"
)

type Dot struct {
	x, y int
}

type Fold struct {
	axle byte
	pos  int
}

// Advent of Code (AOC) 2021 Day 13
func main() {

	paper := []Dot{}
	folds := []Fold{}

	util.ReadFile("../input/13a.txt", func(line string) {
		if strings.HasPrefix(line, "fold along ") {
			folds = append(folds, newFold(line))
		} else if line != "" {
			paper = append(paper, newDot(line))
		}
	})

	paper = folds[0].foldPaper(paper)
	fmt.Printf("part 1: %v\n", len(paper))

	folds = folds[1:]
	for _, fold := range folds {
		paper = fold.foldPaper(paper)
	}
	fmt.Print("part 2:\n")
	print(paper)
}

func (fold *Fold) foldPaper(paper []Dot) []Dot {
	res := []Dot{}
	for _, d := range paper {
		folded := fold.foldDot(d)
		if !contains(res, folded) {
			res = append(res, folded)
		}
	}
	return res
}

func (fold *Fold) foldDot(dot Dot) Dot {
	if fold.axle == 'x' && dot.x > fold.pos {
		return Dot{x: 2*fold.pos - dot.x, y: dot.y}
	} else if fold.axle == 'y' && dot.y > fold.pos {
		return Dot{x: dot.x, y: 2*fold.pos - dot.y}
	} else {
		return dot
	}
}

func print(paper []Dot) {
	width, height := size(paper)
	for y := 0; y <= height; y++ {
		for x := 0; x <= width; x++ {
			fmt.Print(charAt(paper, x, y))
		}
		fmt.Println()
	}
}

func charAt(paper []Dot, x int, y int) string {
	for _, d := range paper {
		if d.x == x && d.y == y {
			return "█"
		}
	}
	return " "
}

func size(paper []Dot) (int, int) {
	var width, height int
	for _, d := range paper {
		if d.x > width {
			width = d.x
		}
		if d.y > height {
			height = d.y
		}
	}
	return width, height
}

func contains(dots []Dot, dot Dot) bool {
	for _, d := range dots {
		if d == dot {
			return true
		}
	}
	return false
}

func newFold(line string) Fold {
	parts := strings.Split(strings.Replace(line, "fold along ", "", 1), "=")
	axle := parts[0][0]
	pos, _ := strconv.Atoi(parts[1])
	return Fold{axle: axle, pos: pos}
}

func newDot(line string) Dot {
	parts := strings.Split(line, ",")
	x, _ := strconv.Atoi(parts[0])
	y, _ := strconv.Atoi(parts[1])
	return Dot{x: x, y: y}
}

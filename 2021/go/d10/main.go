package main

import (
	"aoc2021/util"
	"fmt"
	"sort"
)

type NavLine struct {
	FirstIllegal rune
	Missing      []rune
}

var braces map[rune]rune = map[rune]rune{
	'(': ')',
	'{': '}',
	'[': ']',
	'<': '>',
}

var scorePart1 map[rune]int = map[rune]int{
	')': 3,
	']': 57,
	'}': 1197,
	'>': 25137,
}

var scoresPart2 map[rune]uint64 = map[rune]uint64{
	')': 1,
	']': 2,
	'}': 3,
	'>': 4,
}

// Advent of Code (AOC) 2021 Day 10 part 1 and part 2
func main() {

	part1Score := 0
	part2Score := []uint64{}

	util.ReadFile("../input/10a.txt", func(line string) {
		nav := ParseLine(line)
		if nav.FirstIllegal > 0 {
			part1Score += scorePart1[nav.FirstIllegal]
		} else {
			part2Score = append(part2Score, scorePart2(nav.Missing))
		}
	})

	fmt.Printf("part 1: %v\n", part1Score)
	sort.Slice(part2Score, func(i, j int) bool { return part2Score[i] < part2Score[j] })
	fmt.Printf("part 2: %v\n", part2Score[len(part2Score)/2])
}

func ParseLine(line string) NavLine {
	stack := []rune{}

	for _, c := range line {
		if closing, ok := braces[c]; ok {
			stack = append([]rune{closing}, stack...)
		} else {
			expected := stack[0]
			stack = stack[1:]
			if expected != c {
				return NavLine{FirstIllegal: c}
			}
		}
	}
	return NavLine{Missing: stack}
}

func scorePart2(missing []rune) uint64 {
	var score uint64
	for _, c := range missing {
		score = score*5 + scoresPart2[c]
	}
	return score
}

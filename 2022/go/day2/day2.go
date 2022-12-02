package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day2.txt")
	lines := strings.Split(string(input), "\n")

	score1 := 0
	for _, line := range lines {
		score1 += game1(line)
	}
	fmt.Printf("part 1: %v\n", score1)

	score2 := 0
	for _, line := range lines {
		score2 += game2(line)
	}
	fmt.Printf("part 2: %v\n", score2)
}

var toWin = []byte{1, 2, 0}  // to win 0 (Rock) you need 1 (Paper) etc
var toLose = []byte{2, 0, 1} // to lose 0 (Rock) you need 2 (Scissors) etc

func game1(line string) int {
	shape1 := line[0] - 'A' // 0=Rock, 1=Paper, 2=Scissors
	shape2 := line[2] - 'X'

	shapeScore := shape2 + 1
	return int(shapeScore) + gameScore(shape1, shape2)
}

func game2(line string) int {
	shape1 := line[0] - 'A'
	shape2 := shape1
	switch line[2] {
	case 'X':
		shape2 = toLose[shape1]
	case 'Z':
		shape2 = toWin[shape1]
	}
	shapeScore := shape2 + 1
	return int(shapeScore) + gameScore(shape1, shape2)
}

func gameScore(shape1 byte, shape2 byte) int {
	if toWin[shape1] == shape2 {
		return 6
	} else if toLose[shape1] == shape2 {
		return 0
	} else {
		return 3
	}
}

package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
)

func main() {

	var count int
	var one []int
	var size int

	util.ReadFile("../input/3a.txt", func(line string) {
		if size == 0 {
			size = len(line)
			one = make([]int, size)
		}
		for pos, char := range line {
			if char == '1' {
				one[pos] += 1
			}
		}
		count++
	})

	var gamma string
	var epsilon string
	for _, ones := range one {
		zeros := count - ones
		if ones > zeros {
			gamma += "1"
			epsilon += "0"
		} else {
			gamma += "0"
			epsilon += "1"
		}
	}

	gammaRate, _ := strconv.ParseInt(gamma, 2, 32)
	epsilonRate, _ := strconv.ParseInt(epsilon, 2, 32)

	pc := gammaRate * epsilonRate

	fmt.Printf("%s %s %d %d %d\n", gamma, epsilon, gammaRate, epsilonRate, pc)
}

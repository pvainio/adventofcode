package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
)

// Advent of Code (AOC) 2021 Day 3
func main() {

	var data []string

	util.ReadFile("../input/3a.txt", func(line string) {
		data = append(data, line)
	})

	bitSize := len(data[0])

	// calculate ones for each bit position
	oneCounts := make([]int, bitSize)
	for _, line := range data {
		for pos, char := range line {
			oneCounts[pos] += int(byte(char) - '0')
		}
	}

	var gamma uint
	var epsilon uint
	for pos, ones := range oneCounts {
		if ones*2 >= len(data) { // half or more of ones, set gamma bit
			gamma = gamma | (1 << (bitSize - pos - 1))
		} else { // otherwise set epsilon bit
			epsilon = epsilon | (1 << (bitSize - pos - 1))
		}
	}

	fmt.Printf("part 1: %d\n", gamma*epsilon)

	o2, _ := filterUntilOne(true, data)
	co2, _ := filterUntilOne(false, data)

	fmt.Printf("part 2: %d\n", o2*co2)
}

func filterUntilOne(acceptMostCommon bool, data []string) (int64, error) {
	pos := 0
	for len(data) > 1 {
		data = filterByBitPos(pos, data, acceptMostCommon)
		pos++
	}
	return strconv.ParseInt(data[0], 2, 32)
}

func filterByBitPos(pos int, data []string, acceptMostCommon bool) []string {
	accept := mostCommonInPos(pos, data)
	if !acceptMostCommon {
		accept = !accept
	}
	result := []string{}
	for _, line := range data {
		if line[pos] == '1' == accept {
			result = append(result, line)
		}
	}
	return result
}

func mostCommonInPos(pos int, data []string) bool {
	ones := 0
	for _, line := range data {
		ones += int(line[pos] - '0')
	}
	return ones*2 >= len(data)
}

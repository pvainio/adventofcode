package main

import (
	"aoc2021/util"
	"fmt"
	"math"
	"strings"
)

// Advent of Code (AOC) 2021 Day 14
func main() {

	var template string
	var pairToCharRules = make(map[string]string)

	util.ReadFile("../input/14a.txt", func(line string) {
		if strings.Contains(line, " -> ") {
			parts := strings.Split(line, " -> ")
			pairToCharRules[parts[0]] = parts[1]
		} else if line != "" {
			template = line
		}
	})

	part1 := solve(template, 10, pairToCharRules)
	fmt.Printf("part 1: %d\n", part1)

	part2 := solve(template, 40, pairToCharRules)
	fmt.Printf("part 2: %d\n", part2)
}

func solve(template string, steps int, pairToCharRules map[string]string) uint64 {
	pairCounts := countPairs(stringAsPairs(template))
	for i := 0; i < steps; i++ {
		pairCounts = doStep(pairCounts, pairToCharRules)
	}

	charCounts := countFirstChars(pairCounts)
	// since the above does not include last char of the template, add it
	charCounts[template[len(template)-1]]++

	// Calculate and return the difference between max and min char counts
	var min uint64 = math.MaxUint64
	var max uint64 = 0
	for _, v := range charCounts {
		if v < min {
			min = v
		}
		if v > max {
			max = v
		}
	}
	return max - min
}

func doStep(pairs map[string]uint64, pairToCharRules map[string]string) map[string]uint64 {
	pairCounts := make(map[string]uint64)
	for pair, count := range pairs {
		// explode pair to two pairs and add those counts to result
		pairToChar := pairToCharRules[pair]
		pair1 := string(pair[0]) + pairToChar
		pair2 := pairToChar + string(pair[1])
		pairCounts[pair1] += count
		pairCounts[pair2] += count
	}
	return pairCounts
}

func countFirstChars(pairs map[string]uint64) map[byte]uint64 {
	charCounts := make(map[byte]uint64)
	for pair, count := range pairs {
		charCounts[pair[0]] += count
	}
	return charCounts
}

func countPairs(pairs []string) map[string]uint64 {
	pairCount := make(map[string]uint64)
	for _, pair := range pairs {
		pairCount[pair]++
	}
	return pairCount
}

func stringAsPairs(str string) []string {
	var prev rune
	var pairs []string
	for _, c := range str {
		if prev != 0 {
			pairs = append(pairs, string([]rune{prev, c}))
		}
		prev = c
	}
	return pairs
}

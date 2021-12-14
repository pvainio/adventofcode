package main

import (
	"aoc2021/util"
	"fmt"
	"math"
	"strings"
)

var existingCounts = make(map[string]map[byte]uint64)
var pairToCharRules = make(map[string]byte)

// Advent of Code (AOC) 2021 Day 14
func main() {

	var template string

	util.ReadFile("../input/14a.txt", func(line string) {
		if strings.Contains(line, " -> ") {
			parts := strings.Split(line, " -> ")
			pairToCharRules[parts[0]] = parts[1][0]
		} else if line != "" {
			template = line
		}
	})

	part1 := solve(template, 10)
	fmt.Printf("part 1: %d\n", part1)

	part2 := solve(template, 40)
	fmt.Printf("part 2: %d\n", part2)
}

func solve(template string, depth int) uint64 {
	result := make(map[byte]uint64)
	pairs := stringAsCharPairs(template)
	for pos, pair := range pairs { // Loop template as pairs NNCB -> NN,NC,CB
		lastPair := pos == len(pairs)-1 // Identify last pair for calculation
		charCountsForPair := counCharsForPair(pair, lastPair, depth)
		result = mergeCountsByChar(result, charCountsForPair)
	}

	// Calculate and return the difference between max and min char counts
	var min uint64 = math.MaxUint64
	var max uint64 = 0
	for _, v := range result {
		if v < min {
			min = v
		}
		if v > max {
			max = v
		}
	}
	return max - min
}

func counCharsForPair(pair []byte, lastPair bool, depth int) map[byte]uint64 {
	if depth == 0 {
		if lastPair {
			return map[byte]uint64{pair[0]: 1, pair[1]: 1} // For last pair count both chars
		} else {
			return map[byte]uint64{pair[0]: 1} // For others only the first char count
		}
	}
	key := fmt.Sprintf("%v%v%v", string(pair), lastPair, depth)
	if res, ok := existingCounts[key]; ok {
		return res // count calculated already, use it
	} else {
		// no existing counts calculation, calculate counts recursively
		pairToChar := pairToCharRules[string(pair)]
		pair1 := []byte{pair[0], pairToChar}
		pair2 := []byte{pairToChar, pair[1]}
		p1Count := counCharsForPair(pair1, false, depth-1)
		p2Count := counCharsForPair(pair2, lastPair, depth-1)
		res := mergeCountsByChar(p1Count, p2Count)
		existingCounts[key] = res // store counts for later use
		return res
	}
}

func mergeCountsByChar(a map[byte]uint64, b map[byte]uint64) map[byte]uint64 {
	res := make(map[byte]uint64)
	addCounts(res, a)
	addCounts(res, b)
	return res
}

func addCounts(res map[byte]uint64, source map[byte]uint64) {
	for k, v := range source {
		if existing, ok := res[k]; ok {
			res[k] = existing + v
		} else {
			res[k] = v
		}
	}
}

func stringAsCharPairs(str string) [][]byte {
	var prev byte
	var result [][]byte
	for _, c := range str {
		if prev != 0 {
			result = append(result, []byte{prev, byte(c)})
		}
		prev = byte(c)
	}
	return result
}

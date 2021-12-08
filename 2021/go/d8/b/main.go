package main

import (
	"aoc2021/util"
	"fmt"
	"sort"
	"strconv"
	"strings"
)

// Advent of Code (AOC) 2021 Day 8 part 2
func main() {

	count := 0

	util.ReadFile("../input/8a.txt", func(line string) {
		parts := strings.Split(line, "|")
		patterns := sortCharsInStrings(strings.Fields(parts[0]))
		output := sortCharsInStrings(strings.Fields(parts[1]))

		// 2: 1
		// 3: 7
		// 4: 4
		// 5: 2, 3, 5
		// 6: 0, 6, 9
		// 7: 8
		//  _  _       _      __   _   _
		//  _| _| |_| |_  |_    | |_| | |
		// |_  _|   |  _| |_|   |  _| |_|

		// 1, 4, 7, 8
		solved, patterns := solveEasy(patterns)
		// 2, 3, 5, 6, 9, 0
		solved = solve362590(solved, patterns)

		res := ""
		for _, digit := range output {
			for pos, pattern := range solved {
				if digit == pattern {
					res += strconv.Itoa(pos)
				}
			}
		}
		i, _ := strconv.Atoi(res)
		count += i
	})

	fmt.Printf("%v\n", count)
}

func solve(n int, solved []string, patterns []string, fn func(pattern string) bool) ([]string, []string) {
	for pos, pattern := range patterns {
		if fn(pattern) {
			solved[n] = pattern
			patterns = append(patterns[:pos], patterns[pos+1:]...)
		}
	}

	return solved, patterns
}

func solve362590(solved []string, patterns []string) []string {
	// 3 == 5 segments, contains 1
	solved, patterns = solve(3, solved, patterns, func(pattern string) bool { return len(pattern) == 5 && containsChars(pattern, solved[1]) })
	// 6 == 6 segments, does not contain 1
	solved, patterns = solve(6, solved, patterns, func(pattern string) bool { return len(pattern) == 6 && !containsChars(pattern, solved[1]) })

	// separate 2 and 5 based on top left segment
	topLeft := solveTopLeftSegment(solved)
	solved, patterns = solve(2, solved, patterns, func(pattern string) bool { return len(pattern) == 5 && strings.Contains(pattern, topLeft) })
	solved, patterns = solve(5, solved, patterns, func(pattern string) bool { return len(pattern) == 5 && !strings.Contains(pattern, topLeft) })

	// separate 0 and 9 based on bottom right segment
	bottomRight := solveBottomRightSegment(solved)
	solved, patterns = solve(9, solved, patterns, func(pattern string) bool { return len(pattern) == 6 && !strings.Contains(pattern, bottomRight) })
	solved, _ = solve(0, solved, patterns, func(pattern string) bool { return len(pattern) == 6 && strings.Contains(pattern, bottomRight) })

	return solved
}

func solveBottomRightSegment(solved []string) string {
	bottomRight := solved[6]
	for _, c := range solved[5] {
		bottomRight = strings.Replace(bottomRight, string(c), "", 1)
	}
	return bottomRight
}

func solveTopLeftSegment(solved []string) string {
	for _, c := range solved[1] {
		if !strings.ContainsRune(solved[6], c) {
			return string(c)
		}
	}
	return ""
}

func containsChars(s1 string, s2 string) bool {
	for _, c := range s2 {
		if !strings.ContainsRune(s1, c) {
			return false
		}
	}
	return true
}

func solveEasy(patterns []string) ([]string, []string) {
	var digits []string = make([]string, 10)
	var resPatterns []string

	for _, pattern := range patterns {
		switch len(pattern) {
		case 2:
			digits[1] = pattern
		case 3:
			digits[7] = pattern
		case 4:
			digits[4] = pattern
		case 7:
			digits[8] = pattern
		default:
			resPatterns = append(resPatterns, pattern)
		}
	}
	return digits, resPatterns
}

func sortCharsInStrings(strs []string) []string {
	for pos, str := range strs {
		strs[pos] = sortString(str)
	}
	return strs
}

func sortString(str string) string {
	parts := strings.Split(str, "")
	sort.Strings(parts)
	return strings.Join(parts, "")
}

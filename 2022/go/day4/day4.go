package main

import (
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day4.txt")
	lines := strings.Split(string(input), "\n")

	sum1 := 0
	sum2 := 0
	for _, line := range lines {
		sections1, sections2 := parsePair(line)
		if sections1.fullyContains(sections2) || sections2.fullyContains(sections1) {
			sum1 += 1
		}

		if sections1.overlap(sections2) || sections2.overlap(sections1) {
			sum2 += 1
		}
	}
	fmt.Printf("part 1: %v\n", sum1)
	fmt.Printf("part 2: %v\n", sum2)
}

type sections struct {
	start, end int
}

func (s1 sections) fullyContains(s2 sections) bool {
	return s1.start <= s2.start && s1.end >= s2.end
}

func (s1 sections) overlap(s2 sections) bool {
	return s1.start <= s2.start && s1.end >= s2.start
}

func parsePair(line string) (sections, sections) {
	ranges := strings.Split(line, ",")
	return parseSection(ranges[0]), parseSection(ranges[1])
}

func parseSection(r string) sections {
	ends := strings.Split(r, "-")
	return sections{start: atoi(ends[0]), end: atoi(ends[1])}
}

func atoi(a string) int {
	i, _ := strconv.Atoi(a)
	return i
}

package main

import (
	"fmt"
	"os"
	"regexp"
	"strings"
)

type move struct {
	count, from, to int
}

type moves []move

func main() {
	input, _ := os.ReadFile("../../input/day5.txt")
	parts := strings.Split(string(input), "\n\n")
	stacks := parseStacks(parts[0])
	moves := parseMoves(parts[1])

	result1 := moves.apply(stacks, crateMover9000)
	fmt.Printf("part1: %s\n", result(result1))

	result2 := moves.apply(stacks, crateMover9001)
	fmt.Printf("part2: %s\n", result(result2))

}

func parseStacks(input string) []string {
	levels := strings.Split(input, "\n")
	stackCount := (len(levels[0]) + 1) / 4
	stacks := make([]string, stackCount)
	for _, level := range levels[0 : len(levels)-1] {
		re := regexp.MustCompile(".(.)..?")
		crates := re.FindAllStringSubmatch(level, -1)
		for i, c := range crates {
			if c[1] != " " {
				stacks[i] = c[1] + stacks[i]
			}
		}
	}
	return stacks
}

func parseMoves(input string) moves {
	lines := strings.Split(string(input), "\n")
	m := make([]move, len(lines))
	for i, line := range lines {
		fmt.Sscanf(line, "move %d from %d to %d", &m[i].count, &m[i].from, &m[i].to)
	}
	return m
}

func (ms moves) apply(stacks []string, crane func(move, []string)) []string {
	result := make([]string, len(stacks))
	copy(result, stacks)
	for _, m := range ms {
		crane(m, result)
	}
	return result
}

func crateMover9000(m move, stacks []string) {
	for i := 0; i < m.count; i++ {
		moveCrates(1, m.from, m.to, stacks)
	}
}

func crateMover9001(m move, stacks []string) {
	moveCrates(m.count, m.from, m.to, stacks)
}

func moveCrates(count, from, to int, stacks []string) {
	from, to = from-1, to-1
	stacks[to] = stacks[to] + stacks[from][len(stacks[from])-count:] // append crates to new stack
	stacks[from] = stacks[from][:len(stacks[from])-count]            // remove from old stack
}

func result(stacks []string) string {
	var res string
	for _, s := range stacks {
		res += s[len(s)-1:]
	}
	return res
}

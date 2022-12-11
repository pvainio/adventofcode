package main

import (
	"fmt"
	"os"
	"sort"
	"strconv"
	"strings"
)

func main() {
	fmt.Printf("part1 : %d\n", monkeyBusiness(20))
	fmt.Printf("part2 : %d\n", monkeyBusiness(10000))
}

func monkeyBusiness(rounds int) int {
	input, _ := os.ReadFile("../../input/day11.txt")
	monkeys := parseMonkeys(string(input))

	for i := 0; i < rounds; i++ {
		doRound(monkeys, rounds <= 20)
	}

	inspects := []int{}
	for _, m := range monkeys {
		inspects = append(inspects, m.inspectCount)
	}
	sort.Sort(sort.Reverse(sort.IntSlice(inspects)))
	return inspects[0] * inspects[1]
}

func doRound(monkeys []*monkey, manageWorry bool) {
	commonMultiplier := 1
	for _, m := range monkeys {
		commonMultiplier *= m.divisible
	}

	for _, m := range monkeys {
		for _, i := range m.items {
			m.inspectCount++
			newLevel := op(i, m.oper)
			if manageWorry {
				newLevel = newLevel / 3
			}
			newLevel = newLevel % commonMultiplier

			if newLevel%m.divisible == 0 {
				monkeys[m.trueTo].items = append(monkeys[m.trueTo].items, newLevel)
			} else {
				monkeys[m.falseTo].items = append(monkeys[m.falseTo].items, newLevel)
			}
		}
		m.items = []int{}
	}
}

func op(val int, op operation) int {
	var v2 int
	if op.val == "old" {
		v2 = val
	} else {
		fmt.Sscanf(op.val, "%d", &v2)
	}

	switch op.op {
	case '*':
		return val * v2
	case '+':
		return val + v2
	}
	panic("error")
}

func parseMonkeys(input string) (monkeys []*monkey) {
	monkey := strings.Split(input, "\n\n")
	for _, m := range monkey {
		monkeys = append(monkeys, parseMonkey(m))
	}
	return monkeys
}

func parseMonkey(input string) *monkey {
	var m monkey
	lines := strings.Split(input, "\n")
	m.items = parseItems(strings.Split(lines[1], ":")[1])
	fmt.Sscanf(lines[2], " Operation: new = old %c %s", &m.oper.op, &m.oper.val)
	fmt.Sscanf(lines[3], " Test: divisible by %d", &m.divisible)
	fmt.Sscanf(lines[4], " If true: throw to monkey %d", &m.trueTo)
	fmt.Sscanf(lines[5], " If false: throw to monkey %d", &m.falseTo)
	return &m
}

func parseItems(input string) (items []int) {
	parts := strings.Split(input, ", ")
	for _, p := range parts {
		i, _ := strconv.Atoi(strings.TrimSpace(p))
		items = append(items, i)
	}
	return items
}

type operation struct {
	op  byte
	val string
}

type monkey struct {
	items           []int
	oper            operation
	divisible       int
	trueTo, falseTo int
	inspectCount    int
}

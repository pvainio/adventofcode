package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day10.txt")
	program := parseProgram(string(input))

	fmt.Printf("part1 : %d\n", part1(program))
	fmt.Printf("part2 :\n")
	part2(program)
}

func part2(program []instruction) {
	for cycle := 1; cycle <= 6*40; cycle++ {
		sprite := getState(cycle, program).x
		pos := (cycle - 1) % 40
		if pos >= sprite-1 && pos <= sprite+1 {
			fmt.Print("#")
		} else {
			fmt.Print(" ")
		}
		if cycle%40 == 0 {
			fmt.Print("\n")
		}
	}
}

func part1(program []instruction) (sum int) {
	for _, cycle := range []int{20, 60, 100, 140, 180, 220} {
		sum += cycle * getState(cycle, program).x
	}
	return sum
}

func getState(atCycle int, program []instruction) state {
	state := state{x: 1}
	cycle := 1
	for _, inst := range program {
		cycles, new := run(inst, state)
		cycle = cycle + cycles
		if cycle > atCycle {
			break
		}
		state = new
	}
	return state
}

func run(i instruction, old state) (int, state) {
	switch i.op {
	case "noop":
		return 1, old
	case "addx":
		return 2, state{x: old.x + i.v}
	}
	panic("error")
}

type state struct {
	x int
}

type instruction struct {
	op string
	v  int
}

func parseProgram(input string) (program []instruction) {
	lines := strings.Split(input, "\n")
	for _, line := range lines {
		var i instruction
		fmt.Sscanf(line, "%s %d", &i.op, &i.v)
		program = append(program, i)
	}
	return program
}

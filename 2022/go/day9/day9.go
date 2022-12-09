package main

import (
	"fmt"
	"math"
	"os"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day9.txt")
	moves := parseMoves(string(input))
	head := tieKnots(10)
	head = runMoves(head, moves)
	fmt.Printf("part1 : %d\n", len(head.getNth(1).visited))
	fmt.Printf("part2 : %d\n", len(head.getNth(9).visited))
}

func tieKnots(count int) *knot {
	knot := newKnot()
	prev := knot
	for i := 1; i < count; i++ {
		prev.tail = newKnot()
		prev = prev.tail
	}
	return knot
}

func runMoves(knot *knot, moves []move) *knot {
	for _, m := range moves {
		for s := byte(0); s < m.steps; s++ {
			knot.move(dirs[m.dir])
		}
	}
	return knot
}

type pos struct {
	x, y int
}

type knot struct {
	pos     pos
	visited map[pos]bool
	tail    *knot
}

type move struct {
	dir, steps byte
}

func (k *knot) getNth(count int) *knot {
	if count == 0 {
		return k
	}
	return k.tail.getNth(count - 1)
}

func (k *knot) move(v pos) {
	k.pos = k.pos.move(v)
	k.visited[k.pos] = true
	if k.tail != nil {
		k.tail.follow(k)
	}
}

func (p pos) move(v pos) pos {
	return pos{p.x + v.x, p.y + v.y}
}

func (tail *knot) follow(head *knot) {
	if tail.pos.touching(head.pos) {
		return
	}
	dx := (head.pos.x-tail.pos.x)/2 + (head.pos.x-tail.pos.x)%2
	dy := (head.pos.y-tail.pos.y)/2 + (head.pos.y-tail.pos.y)%2
	tail.move(pos{dx, dy})
}

func (k1 pos) touching(k2 pos) bool {
	return math.Abs(float64(k1.x)-float64(k2.x)) < 1.5 && math.Abs(float64(k1.y)-float64(k2.y)) < 1.5
}

func parseMoves(input string) (moves []move) {
	lines := strings.Split(input, "\n")
	for _, line := range lines {
		var m move
		fmt.Sscanf(line, "%c %d", &m.dir, &m.steps)
		moves = append(moves, m)
	}
	return moves
}

func newKnot() *knot {
	k := &knot{visited: make(map[pos]bool)}
	k.move(pos{})
	return k
}

var dirs = map[byte]pos{'U': {0, -1}, 'D': {0, 1}, 'L': {-1, 0}, 'R': {1, 0}}

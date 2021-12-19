package main

import (
	"aoc2021/util"
	"fmt"
	"math"
	"strconv"
	"strings"
)

type Number struct {
	left, right, parent *Number
	regular             int
}

// Advent of Code (AOC) 2021 Day 18
func main() {

	numbers := []Number{}

	util.ReadFile("../input/18a.txt", func(line string) {
		numbers = append(numbers, parseNumber(line))
	})

	var add Number
	for _, n := range numbers {
		if add.pair() {
			add = add.add(n)
		} else {
			add = n
		}
	}
	fmt.Printf("part 1: %v\n", add.magnitude())

	max := 0
	for _, n1 := range numbers {
		for _, n2 := range numbers {
			res := n1.add(n2).magnitude()
			if res > max {
				max = res
			}
		}
	}
	fmt.Printf("part 2: %v\n", max)
}

func (n Number) add(o Number) Number {
	return reduce(Number{left: &n, right: &o})
}

func reduce(original Number) Number {
	for { // loop while no movement
		n := parseNumber(original.String())
		n = explode(n)
		if !n.equals(original) {
			original = n
			continue // so exploding while changes
		}
		n = split(n)
		if n.equals(original) {
			return n // no change when splitting, ready
		}
		original = n
	}
}

func explode(n Number) Number {
	// Traverse the number tree with depth to find out first item to explode and prev/next regulars
	stack := []*Number{}
	current := &n

	var prevRegular *Number = nil
	var explode *Number = nil
	var nextRegular *Number = nil

	for len(stack) > 0 || current != nil {
		for current != nil {
			stack = append([]*Number{current}, stack...)
			current = current.left
		}
		current = stack[0]
		stack = stack[1:]
		if explode == nil && !current.pair() && current.depth() <= 4 {
			prevRegular = current
		} else if explode == nil && current.pair() && current.depth() == 4 {
			explode = current
		} else if nextRegular == nil && !current.pair() && explode != nil && current != explode.right {
			nextRegular = current
		}

		current = current.right
	}
	if explode != nil {
		if prevRegular != nil {
			prevRegular.regular += explode.left.regular
		}
		if nextRegular != nil {
			nextRegular.regular += explode.right.regular
		}
		explode.left = nil
		explode.right = nil
		explode.regular = 0
	}
	return n
}

func split(n Number) Number {
	split := findFirst(&n, func(n *Number) bool {
		return n.regular > 9
	})
	if split != nil {
		split.left = &Number{regular: int(math.Floor(float64(split.regular) / 2.0))}
		split.right = &Number{regular: int(math.Ceil(float64(split.regular) / 2.0))}
		split.regular = 0
	}
	return n
}

func findFirst(n *Number, fn func(n *Number) bool) *Number {
	if fn(n) {
		return n
	} else if n.pair() {
		l := findFirst(n.left, fn)
		if l != nil {
			return l
		}
		return findFirst(n.right, fn)
	} else {
		return nil
	}
}

func parseNumber(s string) Number {
	var n *Number
	if strings.HasPrefix(s, "[") {
		s = s[1 : len(s)-1]
		c, pos := 0, 0
		for s[pos] != ',' || c > 0 {
			if s[pos] == '[' {
				c++
			} else if s[pos] == ']' {
				c--
			}
			pos++
		}
		l := parseNumber(s[0:pos])
		r := parseNumber(s[pos+1:])
		n = &Number{left: &l, right: &r}
	} else {
		r, _ := strconv.Atoi(s)
		n = &Number{regular: r}
	}
	setParent(n, nil)
	return *n
}

func setParent(n *Number, parent *Number) {
	n.parent = parent
	if n.pair() {
		setParent(n.left, n)
		setParent(n.right, n)
	}
}

func (n Number) pair() bool {
	return n.left != nil && n.right != nil
}

func (n Number) depth() int {
	if n.parent == nil {
		return 0
	}
	return n.parent.depth() + 1
}

func (n Number) magnitude() int {
	if n.pair() {
		return 3*n.left.magnitude() + 2*n.right.magnitude()
	} else {
		return n.regular
	}
}

func (n Number) equals(o Number) bool {
	return n.String() == o.String()
}

func (n Number) String() string {
	if n.pair() {
		return fmt.Sprintf("[%v,%v]", n.left.String(), n.right.String())
	} else {
		return fmt.Sprintf("%d", n.regular)
	}
}

package main

import (
	"aoc2021/util"
	"fmt"
)

type Range struct {
	start, end int
}

type Cube struct {
	on      bool
	x, y, z Range
}

// Advent of Code (AOC) 2021 Day 22 Reactor Reboot
func main() {
	steps := []Cube{}
	util.ReadFile("../input/22a.txt", func(line string) {
		var x1, x2, y1, y2, z1, z2 int
		var onStr string
		fmt.Sscanf(line, "%s x=%d..%d,y=%d..%d,z=%d..%d", &onStr, &x1, &x2, &y1, &y2, &z1, &z2)
		on := false
		if onStr == "on" {
			on = true
		}
		steps = append(steps, Cube{on, Range{x1, x2}, Range{y1, y2}, Range{z1, z2}})
	})

	p1steps := []Cube{}
	for _, s := range steps {
		if _, ok := s.intersect(Cube{true, Range{-50, 50}, Range{-50, 50}, Range{-50, 50}}); ok {
			p1steps = append(p1steps, s)
		}
	}

	fmt.Printf("part 1: %v\n", reboot(p1steps))

	fmt.Printf("part 2: %v\n", reboot(steps))
}

func reboot(steps []Cube) int64 {
	result := []Cube{}
	for _, step := range steps { // do each step
		newResult := []Cube{}             // for each step, build the result
		for _, existing := range result { // apply each existing result
			newResult = append(newResult, existing.subtract(step)...) // by subtractint the newest step from the result
		}
		newResult = append(newResult, step) // finally add the current step to the result
		result = newResult
	}
	var sum int64 = 0
	for _, c := range result {
		if c.on {
			sum += c.size()
		}
	}
	return sum
}

// subtract cube from another returning the subtract result as new cubes
func (a Cube) subtract(b Cube) []Cube {
	i, ok := a.intersect(b)
	if !ok {
		return []Cube{a}
	}
	xp := a.x.split(b.x)
	yp := a.y.split(b.y)
	zp := a.z.split(b.z)

	res := []Cube{}
	for _, x := range xp {
		for _, y := range yp {
			for _, z := range zp {
				if x != i.x || y != i.y || z != i.z {
					res = append(res, Cube{a.on, x, y, z})
				}
			}

		}
	}
	return res
}

func (a Cube) intersect(b Cube) (Cube, bool) {
	i := Cube{x: a.x.intersect(b.x), y: a.y.intersect(b.y), z: a.z.intersect(b.z)}
	return i, i.x.size() > 0 && i.y.size() > 0 && i.z.size() > 0
}

func (a Range) split(b Range) []Range {
	res := []Range{}
	r1 := Range{a.start, min(b.start-1, a.end)}
	r2 := Range{max(a.start, b.start), min(a.end, b.end)}
	r3 := Range{max(b.end+1, a.start), a.end}
	if r1.size() > 0 {
		res = append(res, r1)
	}
	if r2.size() > 0 {
		res = append(res, r2)
	}
	if r3.size() > 0 {
		res = append(res, r3)
	}
	return res
}

func (a Range) intersect(b Range) Range {
	return Range{max(a.start, b.start), min(a.end, b.end)}
}

func (r Range) size() int {
	return r.end - r.start + 1
}

func (c Cube) size() int64 {
	return int64(c.x.size()) * int64(c.y.size()) * int64(c.z.size())
}

func min(a int, b int) int {
	if a < b {
		return a
	} else {
		return b
	}
}

func max(a int, b int) int {
	if a > b {
		return a
	} else {
		return b
	}
}

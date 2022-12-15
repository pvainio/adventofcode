package main

import (
	"fmt"
	"os"
	"sort"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day15.txt")
	sensors := readInput(string(input))

	spans, count := spansForY(sensors, 2000000), 0
	for _, s := range spans {
		count += s.end - s.start
	}

	fmt.Printf("part1: %v\n", count)

	var x, y int
	for y = 0; y <= 4000000; y++ {
		spans := spansForY(sensors, y)
		if len(spans) > 1 {
			x = spans[0].end + 1
			break
		}
	}
	fmt.Printf("part2: %v\n", x*4000000+y)
}

func spansForY(sensors []sensor, y int) (spans []span) {
	for _, s := range sensors {
		sbDist, srDist := abs(s.loc.x-s.beacon.x)+abs(s.loc.y-s.beacon.y), abs(s.loc.y-y)
		dist := sbDist - srDist
		if dist > 0 {
			spans = append(spans, span{s.loc.x - dist, s.loc.x + dist})
		}
	}
	return merge(spans)
}

func merge(spans []span) (res []span) {
	sort.Sort(byStart(spans))
	res = append(res, spans[0])
	for _, next := range spans[1:] {
		prev := res[len(res)-1]
		if prev.end >= next.start-1 {
			res[len(res)-1] = span{prev.start, max(prev.end, next.end)}
		} else {
			res = append(res, next)
		}
	}
	return res
}

func readInput(input string) (res []sensor) {
	lines := strings.Split(input, "\n")
	for _, l := range lines {
		var s sensor
		fmt.Sscanf(l, "Sensor at x=%d, y=%d: closest beacon is at x=%d, y=%d",
			&s.loc.x, &s.loc.y, &s.beacon.x, &s.beacon.y)
		res = append(res, s)
	}
	return res
}

type span struct{ start, end int }
type sensor struct{ loc, beacon xy }
type xy struct{ x, y int }
type byStart []span

func (a byStart) Len() int           { return len(a) }
func (a byStart) Swap(i, j int)      { a[i], a[j] = a[j], a[i] }
func (a byStart) Less(i, j int) bool { return a[i].start < a[j].start }

func max(a, b int) int {
	if a > b {
		return a
	}
	return b
}

func abs(v int) int {
	if v < 0 {
		return -v
	}
	return v
}

package main

import (
	"encoding/json"
	"fmt"
	"os"
	"reflect"
	"sort"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day13.txt")
	pairs := readPairs(string(input))

	packets := [][]any{}
	sum := 0
	for i, pair := range pairs {
		if less(pair[0], pair[1]) {
			sum += i + 1
		}
		packets = append(packets, pair[0], pair[1])
	}
	fmt.Printf("part1: %v\n", sum)

	packets = append(packets, dividers...)
	sort.Sort(ByPacket(packets))
	var key int
	for i, p := range packets {
		if reflect.DeepEqual(p, dividers[0]) {
			key = i + 1
		} else if reflect.DeepEqual(p, dividers[1]) {
			key = key * (i + 1)
		}
	}
	fmt.Printf("part2: %v\n", key)
}

func less(left []any, right []any) (less bool) {
	return compare(left, right, 0) <= 0
}

func compare(left []any, right []any, i int) (res int) {
	if i >= len(left) && i >= len(right) {
		return 0 // end of both lists
	} else if i >= len(left) {
		return -1 // left ended first
	} else if i >= len(right) {
		return 1 // right shorter
	}

	// number comparison
	li, ri := left[i], right[i]
	ln, isln := li.(float64)
	rn, isrn := ri.(float64)
	if isln && isrn && ln == rn {
		return compare(left, right, i+1) // same, continue to next item
	} else if isln && isrn {
		return int(ln - rn) // different numbers
	}

	// list comparison
	llist, isllist := li.([]any)
	rlist, isrlist := ri.([]any)
	if !isllist {
		llist = []any{ln} // mixed, convert to list
	}
	if !isrlist {
		rlist = []any{rn} // mixed, convert to list
	}
	res = compare(llist, rlist, 0)
	if res == 0 { // tie, continue to next item
		return compare(left, right, i+1)
	}
	return res
}

var dividers = [][]any{{[]any{float64(2)}}, {[]any{float64(6)}}}

type ByPacket [][]any

func (p ByPacket) Len() int           { return len(p) }
func (p ByPacket) Swap(i, j int)      { p[i], p[j] = p[j], p[i] }
func (p ByPacket) Less(i, j int) bool { return less(p[i], p[j]) }

func readPairs(input string) (res [][2][]any) {
	pairs := strings.Split(input, "\n\n")
	for _, pair := range pairs {
		part := strings.Split(pair, "\n")
		var left, right []any
		json.Unmarshal([]byte(part[0]), &left)
		json.Unmarshal([]byte(part[1]), &right)
		pair := [2][]any{left, right}
		res = append(res, pair)
	}
	return res
}

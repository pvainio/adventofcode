package main

import (
	"aoc2021/util"
	"fmt"
	"log"
	"math"
	"strconv"
)

func main() {

	var prev int = math.MaxInt
	var count uint = 0

	util.ReadFile("input/1a.txt", func(line string) {
		depth, err := strconv.Atoi(line)
		if err != nil {
			log.Fatalln(err)
		}

		if depth > prev && prev != math.MaxInt {
			count++
		}
		prev = depth
	})

	fmt.Printf("%d\n", count)
}

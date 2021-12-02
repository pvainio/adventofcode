package main

import (
	"aoc2021/util"
	"fmt"
	"log"
	"strconv"
	"strings"
)

func main() {

	var pos int
	var depth int

	util.ReadFile("../input/2a.txt", func(line string) {
		fields := strings.Fields(line)
		dir := fields[0]
		amount, err := strconv.Atoi(fields[1])

		if err != nil {
			log.Fatalln(err)
		}

		switch dir {
		case "forward":
			pos += amount
		case "down":
			depth += amount
		case "up":
			depth -= amount
		}
	})

	fmt.Printf("%d %d %d\n", pos, depth, pos*depth)
}

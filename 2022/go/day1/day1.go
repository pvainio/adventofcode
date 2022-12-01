package main

import (
	"bufio"
	"fmt"
	"os"
	"sort"
	"strconv"
)

func main() {
	elfs := readElfs("../../input/day1.txt")

	sort.Sort(sort.Reverse(sort.IntSlice(elfs)))

	fmt.Printf("part 1: %v\n", elfs[0])

	sum := 0
	for _, c := range elfs[0:3] {
		sum += c
	}

	fmt.Printf("part 2: %v\n", sum)
}

func readElfs(name string) []int {
	elfs := []int{0}
	currentElf := &elfs[0]

	fileByLine(name, func(line string) {
		if line == "" {
			elfs = append(elfs, 0)
			currentElf = &elfs[len(elfs)-1]
		} else {
			cals, _ := strconv.Atoi(line)
			*currentElf += cals
		}
	})
	return elfs
}

func fileByLine(name string, handler func(line string)) {
	file, err := os.Open(name)
	if err != nil {
		panic(err)
	}
	defer file.Close()

	scanner := bufio.NewScanner(file)
	for scanner.Scan() {
		handler(scanner.Text())
	}
}

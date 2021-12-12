package main

import (
	"aoc2021/util"
	"fmt"
	"strings"
	"unicode"
)

type linkMap map[string][]string

// Advent of Code (AOC) 2021 Day 12
func main() {

	links := make(linkMap)

	util.ReadFile("../input/12a.txt", func(line string) {
		link := strings.Split(line, "-")
		addLink(links, link[0], link[1])
		addLink(links, link[1], link[0])
	})

	paths := findPaths(links, []string{"start"}, make(map[string]byte), 1)

	fmt.Printf("part 1: %v\n", len(paths))

	paths = findPaths(links, []string{"start"}, make(map[string]byte), 2)

	fmt.Printf("part 2: %v\n", len(paths))
}

func findPaths(links linkMap, currentPath []string, visited map[string]byte, maxSingleSmallVisits byte) [][]string {
	currentCave := currentPath[len(currentPath)-1]

	if currentCave == "end" {
		return [][]string{currentPath}
	}

	visited[currentCave]++

	paths := [][]string{}
	for _, nextCave := range links[currentCave] {
		if !alreadyMaxVisits(visited, nextCave, maxSingleSmallVisits) {
			nextPaths := findPaths(links, append(currentPath, nextCave), cloneMap(visited), maxSingleSmallVisits)
			paths = append(paths, nextPaths...)
		}
	}
	return paths
}

func isSmallCave(cave string) bool {
	return !unicode.IsUpper([]rune(cave)[0])
}

func cloneMap(m map[string]byte) map[string]byte {
	r := make(map[string]byte)
	for k, v := range m {
		r[k] = v
	}
	return r
}

func alreadyMaxVisits(visited map[string]byte, cave string, maxSingleSmallVisits byte) bool {
	if !isSmallCave(cave) || visited[cave] == 0 {
		return false
	} else if cave == "start" || cave == "end" || maxSingleSmallVisits < 2 {
		return true
	}

	for k, v := range visited {
		if isSmallCave(k) && v >= maxSingleSmallVisits {
			return true
		}
	}

	return false
}

func addLink(links linkMap, a string, b string) {
	links[a] = append(links[a], b)
}

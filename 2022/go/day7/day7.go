package main

import (
	"fmt"
	"os"
	"strings"
)

func main() {
	input, _ := os.ReadFile("../../input/day7.txt")
	root := parseTerminal(string(input))
	var part1 int
	root.forEach(func(n *node) {
		if n.fileSize == 0 && n.size() <= 100000 {
			part1 += n.size()
		}
	})
	fmt.Printf("part1: %v\n", part1)

	needed := 30000000 - (70000000 - root.size())

	smallest := 70000000
	root.forEach(func(n *node) {
		if n.size() >= needed && n.size() < smallest {
			smallest = n.size()
		}
	})
	fmt.Printf("part2: %v\n", smallest)
}

type node struct {
	fileSize int
	parent   *node
	nodes    map[string]*node
}

func parseTerminal(input string) *node {
	root := newNode(0, nil)
	cwd := root
	lines := strings.Split(input, "\n")
	for _, line := range lines {
		cwd = cwd.apply(line)
	}
	return root
}

func (n *node) apply(line string) *node {
	name, size := "", 0
	if strings.HasPrefix(line, "$ cd ") {
		return n.cd(line[5:])
	} else if _, err := fmt.Sscanf(line, "%d %s", &size, &name); err == nil {
		return n.add(name, size)
	}
	return n
}

func (n *node) cd(name string) *node {
	if name == "/" && n.parent == nil {
		return n
	} else if name == "/" {
		return n.cd("/")
	} else if name == ".." {
		return n.parent
	}
	if _, ok := n.nodes[name]; !ok {
		n.nodes[name] = newNode(0, n)
	}
	return n.nodes[name]
}

func (n *node) add(name string, size int) *node {
	n.nodes[name] = newNode(size, n)
	return n
}

func (n *node) size() int {
	if n.fileSize > 0 {
		return n.fileSize
	}
	var sum int
	for _, c := range n.nodes {
		sum += c.size()
	}
	return sum
}

func newNode(size int, parent *node) *node {
	return &node{fileSize: size, parent: parent, nodes: make(map[string]*node)}
}

func (n *node) forEach(f func(*node)) {
	for _, c := range n.nodes {
		f(c)
		c.forEach(f)
	}
}

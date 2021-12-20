package main

import (
	"aoc2021/util"
	"fmt"
	"math"
)

type Pixel struct {
	x, y int
}

type Image struct {
	background     bool
	x1, y1, x2, y2 int
	pixels         map[Pixel]int
}

// Advent of Code (AOC) 2021 Day 20
func main() {

	var algorithm string
	var image Image
	util.ReadFile("../input/20a.txt", func(line string) {
		if algorithm == "" {
			algorithm = line
		} else {
			image.addPixels(line)
		}
	})

	image = enhanceImage(image, algorithm)
	image = enhanceImage(image, algorithm)
	fmt.Printf("part 1: %v\n", len(image.pixels))

	for i := 0; i < 48; i++ {
		image = enhanceImage(image, algorithm)
	}
	fmt.Printf("part 2: %v\n", len(image.pixels))
}

func enhanceImage(input Image, algorithm string) Image {
	output := input.pixels
	increase := 1
	background := false
	for extend := 1; increase > 0 && !background; extend++ {
		prevCount := len(output)
		output = enhanceWithExtendedSize(extend, input, algorithm)
		increase = len(output) - prevCount
		borderSize := 2*(input.x2-input.x1+extend*2) + 2*(input.y2-input.y1+extend*2)
		background = borderSize == increase
	}
	return NewImage(background, output)
}

func enhanceWithExtendedSize(extend int, input Image, algorithm string) map[Pixel]int {
	output := map[Pixel]int{}
	for y := input.y1 - extend; y <= input.y2+extend; y++ {
		for x := input.x1 - extend; x <= input.x2+extend; x++ {
			enhance(x, y, input, algorithm, output)
		}
	}
	return output
}

func enhance(ox int, oy int, input Image, algorithm string, output map[Pixel]int) {
	bits := 0
	for y := oy - 1; y <= oy+1; y++ {
		for x := ox - 1; x <= ox+1; x++ {
			bits = bits << 1 // build index from surrounding pixels
			bits += bit(x, y, input)
		}
	}
	setPixel(Pixel{ox, oy}, algorithm[bits], output)
}

func bit(x int, y int, i Image) int {
	if x < i.x1 || x > i.x2 || y < i.y1 || y > i.y2 {
		if i.background { // Handle the chaning background color
			return 1
		} else {
			return 0
		}
	}
	return i.pixels[Pixel{x, y}]
}

func setPixel(p Pixel, ch byte, output map[Pixel]int) {
	if ch == '#' {
		output[p] = 1
	}
}

func (i *Image) addPixels(pixels string) {
	if i.pixels == nil {
		i.pixels = map[Pixel]int{}
		i.y2 = -1
	}
	y := i.y2 + 1
	for x, c := range pixels {
		setPixel(Pixel{x, y}, byte(c), i.pixels)
	}
	i.calculateSize()
}

func NewImage(background bool, pixels map[Pixel]int) Image {
	i := Image{background: background, pixels: pixels}
	i.calculateSize()
	return i
}

func (i *Image) calculateSize() {
	for p := range i.pixels {
		i.x1, i.y1, i.x2, i.y2 = min(p.x, i.x1), min(p.y, i.y1), max(p.x, i.x2), max(p.y, i.y2)
	}
}

func min(a int, b int) int {
	return int(math.Min(float64(a), float64(b)))
}
func max(a int, b int) int {
	return int(math.Max(float64(a), float64(b)))
}

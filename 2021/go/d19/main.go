package main

import (
	"aoc2021/util"
	"fmt"
	"math"
	"strings"
)

type Vector struct {
	x, y, z int
}

type Scanner struct {
	location Vector
	beacons  []Vector
}

// Advent of Code (AOC) 2021 Day 19
func main() {
	scanners := []Scanner{}
	util.ReadFile("../input/19a.txt", func(line string) {
		if strings.HasPrefix(line, "--- scanner") {
			scanners = append(scanners, Scanner{})
		} else if len(line) > 0 {
			scanner := &scanners[len(scanners)-1]
			beacon := Vector{}
			fmt.Sscanf(line, "%d,%d,%d", &beacon.x, &beacon.y, &beacon.z)
			scanner.beacons = append(scanner.beacons, beacon)
		}
	})

	aligned := align(scanners)
	distinct := map[Vector]bool{}
	for _, s := range aligned {
		for _, b := range s.beacons {
			distinct[b] = true
		}
	}

	fmt.Printf("part 1: %v\n", len(distinct))

	max := 0
	for _, s1 := range aligned {
		for _, s2 := range aligned {
			dist := s1.location.distance(s2.location)
			if dist > max {
				max = dist
			}
		}
	}

	fmt.Printf("part 2: %v\n", max)

}

func align(scanners []Scanner) []Scanner {
	aligned := []Scanner{scanners[0]}
	scanners = scanners[1:]

nextTry:
	for len(scanners) > 0 { // Loop until all scanners aligned
		for _, s := range aligned { // Use aligned scanners as source
			for pos, test := range scanners { // Test agains original list
				for _, orientation := range orientations(test) { // test all orientations
					if align, ok := calculateAlignVector(s, orientation); ok {
						oa := orientation.move(align) // Move Scanner to origin
						scanners = append(scanners[:pos], scanners[pos+1:]...)
						aligned = append(aligned, oa)
						continue nextTry
					}
				}
			}
		}
	}
	return aligned
}

// Calculate vector to align S2 with S1 and null if not possible
func calculateAlignVector(s1 Scanner, s2 Scanner) (Vector, bool) {
	// Count number of distances for each axis
	xDists := map[int]int{}
	yDists := map[int]int{}
	zDists := map[int]int{}

	for _, b1 := range s1.beacons {
		for _, b2 := range s2.beacons {
			if b1 != b2 {
				v := b1.vector(b2)
				xDists[v.x] += 1
				yDists[v.y] += 1
				zDists[v.z] += 1
			}
		}
	}

	// Get max max number of distances for each axis
	xMaxDist, xMaxCount := findMax(xDists)
	yMaxDist, yMaxCount := findMax(yDists)
	zMaxDist, zMaxCount := findMax(zDists)

	if xMaxCount >= 12 && yMaxCount >= 12 && zMaxCount >= 12 { // 12 or more same distances for each axis
		return Vector{x: xMaxDist, y: yMaxDist, z: zMaxDist}, true
	} else {
		return Vector{}, false
	}
}

func findMax(m map[int]int) (int, int) {
	maxKey, maxValue := 0, 0
	for k, v := range m {
		if v > maxValue {
			maxValue = v
			maxKey = k
		}
	}
	return maxKey, maxValue
}

func orientations(scanner Scanner) []Scanner {
	return rotations(rotations(rotations([]Scanner{scanner}, Z), Y), X)
}

func rotations(scanners []Scanner, matrix [][]int) []Scanner {
	res := []Scanner{}
	for _, s := range scanners {
		rotate := s
		for x := 0; x < 4; x++ {
			rotate = rotate.rotate(matrix)
			res = append(res, rotate)
		}
	}
	return res
}

func (s Scanner) rotate(m [][]int) Scanner {
	res := Scanner{}
	for _, b := range s.beacons {
		res.beacons = append(res.beacons, b.rotate(m))
	}
	return res
}

func (s Scanner) move(v Vector) Scanner {
	res := Scanner{location: v}
	for _, b := range s.beacons {
		res.beacons = append(res.beacons, b.move(v))
	}
	return res
}

func (b Vector) rotate(rm [][]int) Vector {
	rx := rm[0][0]*b.x + rm[0][1]*b.y + rm[0][2]*b.z
	ry := rm[1][0]*b.x + rm[1][1]*b.y + rm[1][2]*b.z
	rz := rm[2][0]*b.x + rm[2][1]*b.y + rm[2][2]*b.z
	return Vector{rx, ry, rz}
}

func (b Vector) move(v Vector) Vector {
	return Vector{b.x + v.x, b.y + v.y, b.z + v.z}
}

func (b1 Vector) vector(b2 Vector) Vector {
	return Vector{x: b1.x - b2.x, y: b1.y - b2.y, z: b1.z - b2.z}
}

func (b1 Vector) distance(b2 Vector) int {
	return int(math.Abs(float64(b1.x-b2.x)) + math.Abs(float64(b1.y-b2.y)) + math.Abs(float64(b1.z-b2.z)))
}

// rotation matrixes
var X = [][]int{{1, 0, 0}, {0, 0, -1}, {0, 1, 0}}
var Y = [][]int{{0, 0, 1}, {0, 1, 0}, {-1, 0, 0}}
var Z = [][]int{{0, -1, 0}, {1, 0, 0}, {0, 0, 1}}

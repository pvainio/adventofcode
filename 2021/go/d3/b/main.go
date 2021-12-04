package main

import (
	"aoc2021/util"
	"fmt"
	"strconv"
)

func mostCommon(pos int, data []string) byte {
	ones, zeros := 0, 0
	for _, line := range data {
		if line[pos] == '1' {
			ones++
		} else {
			zeros++
		}
	}
	if ones >= zeros {
		return '1'
	} else {
		return '0'
	}
}

func filter(pos int, data []string, acceptMostCommon bool) []string {
	mc := mostCommon(pos, data)
	result := []string{}
	accept := mc
	if !acceptMostCommon {
		if mc == '1' {
			accept = '0'
		} else {
			accept = '1'
		}
	}
	for _, line := range data {
		if line[pos] == accept {
			result = append(result, line)
		}
	}
	return result
}

func filterUntilOne(acceptMostCommon bool, data []string) string {
	pos := 0
	for len(data) > 1 {
		data = filter(pos, data, acceptMostCommon)
		pos++
	}
	return data[0]
}

func main() {

	var data []string

	util.ReadFile("../input/3a.txt", func(line string) {
		data = append(data, line)
	})

	o2 := filterUntilOne(true, data)
	fmt.Printf("Oxygen %s\n", o2)

	co2 := filterUntilOne(false, data)
	fmt.Printf("CO2 %s\n", co2)

	o2Rating, _ := strconv.ParseInt(o2, 2, 32)
	co2Rating, _ := strconv.ParseInt(co2, 2, 32)

	fmt.Printf("%d %d %d\n", o2Rating, co2Rating, o2Rating*co2Rating)
}

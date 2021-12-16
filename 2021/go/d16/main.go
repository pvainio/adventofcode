package main

import (
	"aoc2021/util"
	"bytes"
	"fmt"
	"math"
	"strconv"
	"strings"
)

type packet interface {
	version() int
	value() int
}

type packetHeader struct {
	version, typeId int
}

type literalPacket struct {
	header  packetHeader
	literal int
}

type operatorPacket struct {
	header  packetHeader
	packets []packet
}

// Advent of Code (AOC) 2021 Day 16
func main() {

	var data string
	util.ReadFile("../input/16a.txt", func(line string) {
		data = asBits(line)
	})

	in := bytes.NewBufferString(data)

	p := parsePacket(in)

	fmt.Printf("part 1: %v\n", p.version())
	fmt.Printf("part 2: %v\n", p.value())
}

func parsePacket(in *bytes.Buffer) packet {
	header := packetHeader{version: getBits(in, 3), typeId: getBits(in, 3)}
	switch header.typeId {
	case 4:
		return parseLiteral(header, in)
	default:
		return parseOperator(header, in)
	}
}

func parseLiteral(header packetHeader, in *bytes.Buffer) packet {
	var value int = 0
	groupPrefix := 0
	for ok := true; ok; ok = groupPrefix == 1 {
		groupPrefix = getBits(in, 1)
		value = (value << 4) + getBits(in, 4)
	}
	return literalPacket{header: header, literal: value}
}

func parseOperator(header packetHeader, in *bytes.Buffer) packet {
	lengthType := getBits(in, 1)
	packets := []packet{}
	if lengthType == 0 {
		length := getBits(in, 15)
		startLen := in.Len()
		for startLen-in.Len() < length {
			packets = append(packets, parsePacket(in))
		}
	} else {
		number := getBits(in, 11)
		for i := 0; i < number; i++ {
			packets = append(packets, parsePacket(in))
		}
	}
	return operatorPacket{header: header, packets: packets}
}

func (p literalPacket) version() int {
	return p.header.version
}

func (p operatorPacket) version() int {
	subVersion := 0
	for _, p := range p.packets {
		subVersion += p.version()
	}
	return p.header.version + subVersion
}

func (p literalPacket) value() int {
	return p.literal
}

func (p operatorPacket) value() int {
	switch p.header.typeId {
	case 0:
		return p.reduce(0, func(a int, b int) int { return a + b })
	case 1:
		return p.reduce(1, func(a int, b int) int { return a * b })
	case 2:
		return p.reduce(math.MaxInt, func(a int, b int) int { return min(a, b) })
	case 3:
		return p.reduce(math.MinInt, func(a int, b int) int { return max(a, b) })
	case 5:
		return p.cmpSub(func(a int, b int) bool { return a > b })
	case 6:
		return p.cmpSub(func(a int, b int) bool { return a < b })
	case 7:
		return p.cmpSub(func(a int, b int) bool { return a == b })
	default:
		return -1
	}
}

func (p operatorPacket) cmpSub(fn func(a int, b int) bool) int {
	if fn(p.packets[0].value(), p.packets[1].value()) {
		return 1
	} else {
		return 0
	}
}

func (p operatorPacket) reduce(value int, fn func(a int, b int) int) int {
	for _, p := range p.packets {
		value = fn(value, p.value())
	}
	return value
}

func min(a int, b int) int {
	if a < b {
		return a
	} else {
		return b
	}
}

func max(a int, b int) int {
	if a > b {
		return a
	} else {
		return b
	}
}

func getBits(in *bytes.Buffer, len int) int {
	bits := string(in.Next(len))
	val, _ := strconv.ParseInt(bits, 2, 32)
	return int(val)
}

func asBits(hex string) string {
	var result string
	for _, c := range hex {
		result += hexToBits(c)
	}
	return result
}

func hexToBits(hex rune) string {
	v, _ := strconv.ParseInt(string(hex), 16, 8)
	return strings.ReplaceAll(fmt.Sprintf("%4s", strconv.FormatInt(v, 2)), " ", "0")
}

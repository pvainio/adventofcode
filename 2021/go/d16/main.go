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
	head() packetHeader
	version() int
	content() int64
}

type packetHeader struct {
	version, typeId int
}

type literalPacket struct {
	header packetHeader
	value  int
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
	fmt.Printf("part 2: %v\n", p.content())
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
	return literalPacket{header: header, value: value}
}

func parseOperator(header packetHeader, in *bytes.Buffer) packet {
	lengthType := getBits(in, 1)
	if lengthType == 0 {
		return parsePacketsLength(header, in)
	} else {
		return parsePacketsNumber(header, in)
	}
}

func parsePacketsLength(header packetHeader, in *bytes.Buffer) packet {
	length := getBits(in, 15)
	startLen := in.Len()
	packets := []packet{}
	for startLen-in.Len() < length {
		packets = append(packets, parsePacket(in))
	}
	return operatorPacket{header: header, packets: packets}
}

func parsePacketsNumber(header packetHeader, in *bytes.Buffer) packet {
	number := getBits(in, 11)
	packets := []packet{}
	for i := 0; i < number; i++ {
		packets = append(packets, parsePacket(in))
	}
	return operatorPacket{header: header, packets: packets}
}

func (p literalPacket) head() packetHeader {
	return p.header
}

func (p operatorPacket) head() packetHeader {
	return p.header
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

func (p literalPacket) content() int64 {
	return int64(p.value)
}

func (p operatorPacket) content() int64 {
	switch p.header.typeId {
	case 0:
		return reduce(p.packets, 0, func(a int64, b int64) int64 { return a + b })
	case 1:
		return reduce(p.packets, 1, func(a int64, b int64) int64 { return a * b })
	case 2:
		return reduce(p.packets, math.MaxInt64, func(a int64, b int64) int64 { return min(a, b) })
	case 3:
		return reduce(p.packets, math.MinInt64, func(a int64, b int64) int64 { return max(a, b) })
	case 5:
		return compare(p.packets, func(a int64, b int64) bool { return a > b })
	case 6:
		return compare(p.packets, func(a int64, b int64) bool { return a < b })
	case 7:
		return compare(p.packets, func(a int64, b int64) bool { return a == b })
	default:
		return -1
	}
}

func compare(packets []packet, fn func(a int64, b int64) bool) int64 {
	if fn(packets[0].content(), packets[1].content()) {
		return 1
	} else {
		return 0
	}
}

func reduce(packets []packet, value int64, fn func(a int64, b int64) int64) int64 {
	for _, p := range packets {
		value = fn(value, p.content())
	}
	return value
}

func min(a int64, b int64) int64 {
	if a < b {
		return a
	} else {
		return b
	}
}

func max(a int64, b int64) int64 {
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

package aoc2021;

import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HexFormat;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 16
 */
public class D16 {

    interface Packet {
        int version();
        long value();
    }

    record LiteralPacket(int version, int type, long value) implements Packet{}

    record OperatorPacket(int version, int type, List<Packet> packets) implements Packet {
        public long value() {
            return switch (type) { // calculate packet value by type
                case 0 -> packets.stream().mapToLong(p -> p.value()).sum();
                case 1 -> packets.stream().mapToLong(p -> p.value()).reduce(1, (a,b) -> a*b); 
                case 2 -> packets.stream().mapToLong(p -> p.value()).min().getAsLong();
                case 3 -> packets.stream().mapToLong(p -> p.value()).max().getAsLong();
                case 5 -> packets.get(0).value() > packets.get(1).value() ? 1l : 0l;
                case 6 -> packets.get(0).value() < packets.get(1).value() ? 1l : 0l;
                case 7 -> packets.get(0).value() == packets.get(1).value() ? 1l : 0l;
                default -> throw new IllegalStateException("invalid packet type " + type);
            };
        }

        public int version() {
            return version + packets.stream().mapToInt(p -> p.version()).sum();
        }
    }

    public static void main(String... args) throws IOException {
        String data = asBits(Files.lines(Path.of("../input/16a.txt")).findFirst().get());

        Packet p = parsePacket(CharBuffer.wrap(data));

        System.out.printf("part 1: %d\n", p.version());
        System.out.printf("part 2: %d\n", p.value());
    }

    static Packet parsePacket(CharBuffer in) {
        int version = getBits(in, 3);
        int type = getBits(in, 3);
        return switch(type) {
            case 4 -> parseLiteralPacket(version, type, in);
            default -> parseOperatorPacket(version, type, in);
        };
    }

    static Packet parseOperatorPacket(int version, int type, CharBuffer in) {
        int lengthType = getBits(in, 1);
        if (lengthType == 0) { // lengthType 0 mean sub packets by bit length
            int len = getBits(in, 15); // next 15 bits is the number of bits to read
            int start = in.position();
            var packets = Stream.iterate(0, i -> in.position() - start < len, (a) -> 0) // iterate unti len consumed
                .map(i -> parsePacket(in))
                .toList();
            return new OperatorPacket(version, type, packets);
        } else { // lengthType 1 -> sub packets by numbers
            int number = getBits(in, 11); // next 11 bit is the number of packets to read
            var packets = IntStream.range(0, number).mapToObj(i -> parsePacket(in)).toList();
            return new OperatorPacket(version, type, packets);
        }
    }

    static Packet parseLiteralPacket(int version, int type, CharBuffer in) {
        long value = 0;
        int groupPrefix;
        do {
            groupPrefix = getBits(in, 1);
            value = (value << 4) + getBits(in, 4); // add 4 bits to literal
        } while (groupPrefix > 0); // group is prefixed with 1 until last group is not
        return new LiteralPacket(version, type, value);
    }

    static int getBits(CharBuffer in, int len) {
        String bits = in.subSequence(0, len).toString();
        in.position(in.position()+len);
        return Integer.parseInt(bits, 2);
    }

    static String asBits(String hexString) {
        return hexString.chars()
            .map(c -> HexFormat.fromHexDigit(c))
            .mapToObj(h -> String.format("%4s", Integer.toString(h, 2)).replace(' ', '0'))
            .collect(Collectors.joining(""));
    }
}
package aoc2021;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Advent of Code (AOC) 2021 Day 20
 */
public class D20 {

    record Pixel(int x, int y) {}
    record Image(boolean background, Set<Pixel> pixels, int x1, int y1, int x2, int y2) {
        static Image create(boolean background, Set<Pixel> pixels) {
            int minY = pixels.stream().mapToInt(p -> p.y).min().getAsInt();
            int minX = pixels.stream().mapToInt(p -> p.x).min().getAsInt();
            int maxY = pixels.stream().mapToInt(p -> p.y).max().getAsInt();
            int maxX = pixels.stream().mapToInt(p -> p.x).max().getAsInt();
            return new Image(background, pixels, minX, minY, maxX, maxY);
        }
    }

    public static void main(String... args) throws IOException 
    {
        List<String> data = Files.readAllLines(Path.of("../input/20a.txt"));
        String algorithm = data.get(0);
        Image image = parseImage(data.stream().skip(2).toList());

        image = enhanceImage(image, algorithm);
        image = enhanceImage(image, algorithm);
        System.out.printf("part 1: %d\n", image.pixels.size());

        for (int i = 0; i < 48; i++) {
            image = enhanceImage(image, algorithm);         
        }
        System.out.printf("part 2: %d\n", image.pixels.size());
    }

    static Image enhanceImage(Image input, String algorithm) {
        Set<Pixel> output = input.pixels;
        int increase = 1;
        boolean background = false;
        for (int extend = 1; increase > 0 && !background; extend++) { // enhance in loop each time extending border 1 pixel further
            int prevCount = output.size();
            output = enchangePixelslWithExtendedSize(extend, input, algorithm);
            increase = output.size()-prevCount;
            int borderSize = 2*(input.x2-input.x1 + extend*2) + 2*(input.y2-input.y1 + extend*2);
            background = borderSize == increase;  // all the pixels in border are set -> background changed color to 1
        }
        return Image.create(background, output);
    }

    static Set<Pixel> enchangePixelslWithExtendedSize(int extend, Image image, String algorithm) {
        Set<Pixel> output = new HashSet<>();
        for (int y = image.y1 - extend; y <= image.y2 + extend; y++) {
            for (int x = image.x1 - extend; x <= image.x2 + extend; x++) {
                enchangePixel(new Pixel(x, y), image, algorithm).forEach(output::add);
            }
        }
        return output;
    }

    static Stream<Pixel> enchangePixel(Pixel p, Image source, String algorithm) {
        int bits = 0;
        for (int y = p.y-1; y <= p.y+1; y++) {
            for (int x = p.x-1; x <= p.x+1; x++) {
                bits = bits << 1;
                bits += bit(x, y, source) ? 1 : 0;
            }
        }
        return algorithm.charAt(bits) == '.' ? Stream.empty() : Stream.of(p);
    }

    static boolean bit(int x, int y, Image image) {
        if (x < image.x1 || x > image.x2 || y < image.y1 || y > image.y2) {
            return image.background;
        } else {
            return image.pixels.contains(new Pixel(x, y));
        }
    }

    static Image parseImage(List<String> data) {
        Set<Pixel> pixels = new HashSet<>();
        for (int y = 0; y < data.size(); y++) {
            String line = data.get(y);
            for (int x=0; x < line.length(); x++) {
                if (line.charAt(x) != '.') {
                    pixels.add(new Pixel(x,y));
                }
            }
        }
        return Image.create(false, pixels);
    }
}
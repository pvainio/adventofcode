import { asLines } from './util.js';

const galaxy = asLines('../input/day11.txt').flatMap((row, y) =>
  Array.from(row).map((char, x) => char === '#' ? [BigInt(x), BigInt(y)] : []).filter(x => x.length > 0)
).filter(x => x.length > 0);

const width = galaxy.reduce((max, [x, y]) => max > x ? max : Number(x), 0) + 1;
const height = galaxy.reduce((max, [x, y]) => max > y ? max : Number(y), 0) + 1;

const emptyCols =[...Array(width).keys()].map(x => BigInt(x)).filter((col) => !galaxy.find(([x, y]) => x === col));
const emptyRows =[...Array(height).keys()].map(y => BigInt(y)).filter((row) => !galaxy.find(([x, y]) => y === row));

const expand = (x, y, emptyCols, emptyRows, factor) => {
  x += BigInt(emptyCols.filter((col) => col < x).length) * (factor - 1n);
  y += BigInt(emptyRows.filter((row) => row < y).length) * (factor - 1n);
  return [x, y];
};

const expanded = galaxy.map(([x, y]) => expand(x, y, emptyCols, emptyRows, 2n));

const genPairs = (arr) => {
  return arr.map((a, i) => arr.slice(i + 1).map(b => [a, b])).flat();
};

const pairs = genPairs(expanded)

const abs = (x) => x < 0 ? -x : x;
const shortestPath = (a, b) => abs(a[0] - b[0]) + abs(a[1] - b[1]);

const sum1 = pairs.map(([a, b]) => shortestPath(a, b)).reduce((a, b) => a + b, 0n);

console.log(`part1: ${sum1}`);

const expanded2 = galaxy.map(([x, y]) => expand(x, y, emptyCols, emptyRows, 1000000n));
const pairs2 = genPairs(expanded2);
const sum2 = pairs2.map(([a, b]) => shortestPath(a, b)).reduce((a, b) => a + b, 0n);

console.log(`part2: ${sum2}`);

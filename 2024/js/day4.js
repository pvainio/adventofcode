
import { asLines } from './util.js';

const day4 = asLines('../input/4.txt').map((l) => l.split(""));

const moves = [[-1, 0], [1, 0], [0, -1], [0, 1], [-1, -1], [-1, 1], [1, -1], [1, 1]];

const count = (word, row, col, data, dir) => {
  if (row < 0 || row >= data.length || col < 0 || col >= data[row].length) return 0; // outside
  if (data[row][col] !== word.at(0)) return 0; // no mach
  if (word.length === 1) return 1; // final matching character
  const dirs = dir ? [dir] : moves; // continue with direction or test all directtions
  return dirs.map(([x, y]) => count(word.slice(1), row + x, col + y, data, [x, y])).reduce((a, b) => a + b);
}

const countHits = (fn) => {
  return day4.map((_, row) => day4[row].map((_, col) => fn(row, col)) // for each row and col find hits
    .reduce( (a, b) => a + b)).reduce((a, b) => a + b); // sum all of them 
}

const sum = countHits((row, col) => count('XMAS', row, col, day4));
console.log(`day4a: ${sum}`);

const moves2 = [[-1, -1], [-1, 1], [1, -1], [1, 1]];

const startCount = (row, col, data) => {
  if (row < 0 || row >= data.length || col < 0 || col >= data[row].length) return 0; // outside
  if (data[row][col] !== 'A') return 0; // not a starting point
  // count MAS diagonally from starting point 'A'
  const found = moves2.map((m) => count("MAS", row + m[0], col + m[1], data, [-m[0], -m[1]]))
    .reduce((a, b) => a + b);
  return found > 1 ? 1 : 0; // convert 2 "MAX" found as 1 hit
}

const sum2 = countHits((row, col) => startCount(row, col, day4));
console.log(`day4b: ${sum2}`);

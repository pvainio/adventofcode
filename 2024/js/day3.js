
import { asLines } from './util.js';

const day3 = asLines('../input/3.txt').join();

const matches = day3.matchAll(/mul\((\d{1,3})\,(\d{1,3})\)/g);
const muls = Array.from(matches, (m) => m[1] * m[2]);
const res = muls.reduce((a, b) => a + b)
console.log(`day3a ${res}`);

const matches2 = day3.matchAll(/(?:mul\((\d{1,3})\,(\d{1,3})\))|(?:do\(\))|(?:don\'t\(\))/g);
let act = true;
let sum = 0;
for (const match of matches2) {
  if (match[0] == 'do()') act = true;
  else if (match[0] == 'don\'t()') act = false;
  else if (act) sum += match[1] * match[2];
}
console.log(`day3b: ${sum}`);

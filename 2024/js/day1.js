
import { asLines } from './util.js';

const day1a = asLines('../input/1.txt').map((l) => l.split(/\s+/));
const leftList = day1a.map((l) => Number(l[0])).sort();
const rightList = day1a.map((l) => Number(l[1])).sort();

const diffs = leftList.map((v, i) => Math.abs(v-rightList[i]));
const sum = diffs.reduce((a,b) => a+b);

console.log(`day1 part a: ${sum}`);

const similarityScore = leftList.map((v) => rightList.filter((v2) => v2 == v).length * v);
const sum2 = similarityScore.reduce((a,b) => a+b);

console.log(`day1 part a: ${sum2}`);

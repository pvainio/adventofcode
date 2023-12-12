import { asLines } from './util.js';

const records = asLines('../input/day12_test.txt')

const isValid = (line) => {
  const [springs, damagedGroups] = line.split(' ');
  const damaged = springs.split('.').filter(s => s.length > 0);
  const damagedLengths = damagedGroups.split(',').map(s => parseInt(s));
  if (damaged.length !== damagedLengths.length) {
    return false;
  }
  const mismatch = damaged.filter(group => group.length !== damagedLengths.shift());
  return mismatch.length === 0;
}

const replaceCharAt = (str, index, replacement) => {
  return str.substring(0, index) + replacement + str.substring(index + 1);
}

const solve = (line) => {
  const next = line.indexOf('?');
  if (next === -1) {
    return isValid(line) ? 1 : 0;
  }

  const opt1 = replaceCharAt(line, next, '.');
  const opt2 = replaceCharAt(line, next, '#');

  return solve(opt1) + solve(opt2);
}

const sum = records.map(r => solve(r)).reduce((a, b) => a + b, 0);

console.log(`part 1: ${sum}`);

const unfold = (line, times) => {
  const [left, right] = line.split(' ');
  const left5 = Array(times).fill(left).join('?')
  const right5 = Array(times).fill(right).join(',')
  return `${left5} ${right5}`;
}

// This does not work, takes too long
const unfolded = records.map(r => unfold(r, 5));
const sum2 = unfolded.map(r => solve(r));

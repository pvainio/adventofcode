
import { asLines } from './util.js';

const lineToCalibrationValueA = (line) => {
  const first = line.match(/^[^\d]*(\d)/)[1];
  const last = line.match(/(\d)[^\d]*$/)[1];
  return parseInt(first + last);
};

const day1a = asLines('../input/day1_a.txt')
  .map(lineToCalibrationValueA)
  .reduce((a,b) => a + b, 0);

console.log(`day1 part a: ${day1a}`);

const lineToCalibrationValueB = (line) => {
  return parseInt(findDigits(line).join(''));
}

const numbers = ['one', 'two', 'three', 'four', 'five', 'six', 'seven', 'eight', 'nine'];
const number = (str) => {
  if (str.charAt(0) > '0' && str.charAt(0) <= '9') {
    return parseInt(str.charAt(0));
  }
  const i = numbers.findIndex((n) => str.startsWith(n));
  return i < 0 ? null : i + 1;
};

const findDigits = (line) => {
  let first, last;
  for (let i = 0; i < line.length; i++) {
    const n = number(line.substring(i));
    if (n) {
      first = first || n;
      last = n;
    }
  }
  return [first, last];
};

const day1b = asLines('../input/day1_a.txt')
  .map(lineToCalibrationValueB)
  .reduce((a,b) => a + b, 0);

console.log(`day1 part b: ${day1b}`);

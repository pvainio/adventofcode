
import { asLines } from './util.js';

const input = asLines('../input/day4.txt');

const parseCard = (line) => {
  const [_, cardContent] = line.split(': ');
  const [winStr, haveStr] = cardContent.split(' | ');
  const [win, have] = [winStr.split(/[ ]+/), haveStr.split(/[ ]+/)];
  return {win, have};
};

const sum1 = () => input.map((line) => {
  const {win, have} = parseCard(line);
  const points = have.filter((n) => win.includes(n)) // filter numbers that are in both
    .reduce((a,b) => a * 2, 0.5); // multiply with 2 each number
  return points >= 1 ? points : 0;
}).reduce((a,b) => a + b, 0);

console.log(`part 1: ${sum1()}`);

const sum2 = () => {
  const counts = Array(input.length).fill(1); // initially have 1 of each card
  input.forEach((line, index) => {
    const {win, have} = parseCard(line);
    const matches = have.filter((n) => win.includes(n)).length;
    for (let i = 1; i <= matches; i++) { // for each match, add the count to the next i cards
      counts[index+i] += counts[index]; // add times we already have this card
    }
  });
  return counts.reduce((a,b) => a + b, 0); // sum all counts
}

console.log(`part 2: ${sum2()}`);

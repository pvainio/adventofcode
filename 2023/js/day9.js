import { asLines } from './util.js';

const history = asLines('../input/day9.txt')
  .map(line => line.split(' ')
  .map(n => parseInt(n)));

const difference = (history) => {
  const res = [];
  for (let i = 0; i < history.length - 1; i++) {
    const d = history[i+1] - history[i];
    res.push(d);
  }
  return res;
};

const differences = (history) => {
  const res = [ history ];
  while(history.find(n => n != 0)) {
    const d = difference(history);
    res.push(difference(history));
    history = d;
  }
  return res;
};

const part1Extrapolator = (prevValue, prevLine) => prevValue + prevLine[prevLine.length - 1];
const part2Extrapolator = (prevValue, prevLine) => prevLine[0] - prevValue;

const extrapolate = (extrapolator) => (history) => {
  const d = differences(history);
  // from last differences to first, extrapolate backwards
  return d.toReversed().slice(1).reduce((acc, cur) => extrapolator(acc, cur), 0);
};

const sum = history.map(h => extrapolate(part1Extrapolator)(h))
  .reduce((a, b) => a + b, 0);

console.log(`part1: ${sum}`);

const sum2 = history.map(h => extrapolate(part2Extrapolator)(h))
  .reduce((a, b) => a + b, 0);

console.log(`part2: ${sum2}`);

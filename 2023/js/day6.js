import { asLines } from './util.js';

const input = asLines('../input/day6.txt');

const toDocument = (input) => input.map((line) => {
  const [name, numbersStr] = line.split(/:[ ]*/);
  const numbers = numbersStr.split(/[ ]+/).map((n) => parseInt(n));
  return {name, numbers};
}).reduce((a, b) => {
  a[b.name] = b.numbers;
  return a;
}, {});

const waysToWin = (input) => {
  const doc = toDocument(input);
  const races = doc.Time.map((time, index) => [time, doc.Distance[index]]);
  const ways = races.map(([recordTime, recordDistance]) => {
    // speed * (recordTime - holdTime) = distance, speed = holdTime
    // holdTime * (recordTime - holdTime) = distance
    // holdTime * (recordTime - holdTime) > recordDistance
    // holdTime * recordTime - holdTime^2 > recordDistance
    // holdTime^2 - recordTime * holdTime + recordDistance < 0
    // solve quadratic equation with quadratic formula
    const p1 = Math.ceil((recordTime + Math.sqrt(recordTime**2-4*recordDistance))/2)-1;
    const p2 = Math.floor((recordTime - Math.sqrt(recordTime**2-4*recordDistance))/2)+1;
    return p1 - p2 + 1;
  });
  return ways.reduce((a, b) => a * b, 1);
}

console.log(`part 1: ${waysToWin(input)}`);

const input2 = input.map(line => line.replaceAll(' ', ''));

console.log(`part 2: ${waysToWin(input2)}`);

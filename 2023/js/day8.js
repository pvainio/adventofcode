import { readFileSync } from 'fs';

const [instructions, network] = readFileSync('../input/day8.txt').toString().split('\n\n');
const nodes = network.split('\n').map(line => line.replaceAll(/[\(\)]/g,'').split(' = '));
const nodeMap = nodes.reduce((acc, cur) => {
  acc[cur[0]] = cur[1].split(', ');
  return acc;
}, {});


const solveSteps = (endCondition) => (position) => {
  let step;
  for (step = 0; !endCondition(position); step++) {
    const instruction = instructions.charAt(step % instructions.length);
    const options = nodeMap[position];
    position = options[instruction === 'R' ? 1 : 0];
  }
  return step;
}

const part1Solver = solveSteps(position => position === 'ZZZ');
const part1 = part1Solver('AAA');

console.log(`part1: ${part1}`);

const greatestCommonDivisor = (a, b) => {
  return b ? greatestCommonDivisor(b, a % b) : a;
}
const leastCommonMultiple = (a, b) => {
  return a * b / greatestCommonDivisor(a, b);
}

let startPositions = Object.keys(nodeMap).filter(key => key.endsWith('A'));
const part2Solver = solveSteps(position => position.endsWith('Z'));
const stepsForEacStart = startPositions.map(position => part2Solver(position));
const lcm = stepsForEacStart.reduce((a, b) => leastCommonMultiple(a, b));

console.log(`part2: ${lcm}`);

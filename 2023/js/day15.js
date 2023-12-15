import { readFileSync } from 'fs';

const initializationSequence = readFileSync('../input/day15.txt').toString().split(',');

const hash = (str) => str.split('').reduce((a, b) => (a + b.charCodeAt(0)) * 17 % 256, 0);

const sum1 = initializationSequence.map(step => hash(step)).reduce((a, b) => a + b, 0);

console.log(`part1: ${sum1}`);

const boxes = Array(256).fill(0).map((a) => []);

const minus = (box, label) => box = box.filter(l => l.label !== label);
const equal = (box, label, focalLength) => {
  if (box.find(l => l.label === label)) {
    return box.map(l => l.label === label ? { label, focalLength } : l);
  } else {
    return [...box, { label, focalLength }];
  }
};

const apply = (step, boxes) => {
  const [_, label, instruction, focalLength] = step.match(/([a-z]+)([-=])([0-9]*)/);
  const box = boxes[hash(label)];
  const newBox = instruction === '-' ? minus(box, label) : equal(box, label, focalLength);
  boxes[hash(label)] = newBox;
};

initializationSequence.forEach((step) => apply(step, boxes));

const lensPower = (box, position, focalLength) => (box + 1) * (position + 1) * focalLength;
const boxPower = (box, i) => box.map((l, j) => lensPower(i, j, l.focalLength)).reduce((a, b) => a + b, 0);

const sum2 = boxes.map((box, i) => boxPower(box, i)).reduce((a, b) => a + b, 0);

console.log(`part2: ${sum2}`);
import { readFileSync } from 'fs';

const [flowsData, partsData] = readFileSync('../input/day19.txt').toString().split('\n\n')
  .map(data => data.split('\n'));

const parseFlow = (line) => {
  const [_, name, rulesData] = line.match(/^([a-z]+){(.*)}$/);
  const rules = rulesData.split(',').map(rule => rule.split(':'));
  return [name, rules];
}

const parsePart = (line) => JSON.parse(line.replaceAll(/([a-z]+)=/g, '"$1":'));

const flows = flowsData.map(parseFlow);
const parts = partsData.map(parsePart);
parts.forEach(part => part.state = 'in');

const matchRule = (cond, part) => {
  const [_, left, operator, right] = cond.match(/([a-z]+)([<>=]+)([0-9]+)/);
  return eval(`${part[left]} ${operator} ${right}`);
}

const runFlow = ([_, rules], part) => {
  return rules.flatMap(([cond, target]) => {
    if (target === undefined) return [cond];
    if (matchRule(cond, part)) return [target];
    return [];
  });
};

while (parts.find(part => (part.state !== 'A' && part.state !== 'R'))) {
  for (const part of parts) {
    if (part.state === 'A' || part.state === 'R') continue;
    const flow = flows.find(([name]) => name === part.state);
    const dest = runFlow(flow, part)[0];
    part.state = dest;
  }
}

const sum1 = parts.filter(part => part.state === 'A')
  .map(part => Object.entries(part)
    .reduce((a, [rating, value]) => a + (rating != 'state' ? value : 0), 0)) // sum of all ratings
  .reduce((a, b) => a + b, 0); // sum of all parts

console.log(`part1 ${sum1}`);

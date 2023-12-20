import { asLines } from './util.js';

const parseModule = (line) => {
  const [[module], outputs] = line.split(' -> ').map(str => str.split(', '));
  const [_, type, name] = module.match(/([\%\&]?)([a-z]+)/);
  return { type, name, outputs };
};

const modules = asLines('../input/day20.txt').map(parseModule)
  .reduce((map, mod) => map.set(mod.name, mod), new Map());

const ON = 1, OFF = 0, LOW = 0, HIGH = 1;

[...modules.values()].filter(mod => mod.type === '&').forEach(mod => { 
  const inputs = [...modules.values()].filter(modi => modi.outputs.includes(mod.name)).map(modi => modi.name);
  mod.inputs = inputs.reduce((map, input) => map.set(input, LOW), new Map());
});

const send = (mod, signal, counter, queue) => {
  queue.push(...mod.outputs.map(output => [output, signal, mod.name]));
  counter[signal] += mod.outputs.length;
};

let count = 0;
let firstHigh = {};
let secondHigh = {};

const ping = (counter) => {
  const queue = [ ['broadcaster', LOW, 'button'] ];
  counter[0]++;
  while (queue.length > 0) {
    const [dest, signal, source] = queue.shift();

    // part2, identify cycles for zr inputs, that control rx
    const zrInputs = modules.get('zr').inputs; // zr controls rx
    if ([...zrInputs.entries()].find(([key, value]) => dest === key && value === HIGH)) {
      if (!firstHigh[source]) {
        firstHigh[source] = count;
      } else if (count > firstHigh[source] && !secondHigh[source]) {
        secondHigh[source] = count;
      }
    }

    const mod = modules.get(dest);
    if (mod === undefined) {
      continue;
    }
    if (mod.name === 'broadcaster') {
      send(mod, signal, counter, queue);
    } else if (mod.type === '%' && signal === LOW) {
      mod.state = mod.state ? OFF : ON;
      send(mod, mod.state, counter, queue);
    } else if (mod.type === '&') {
      mod.inputs.set(source, signal);
      const allHigh = [...mod.inputs.values()].find(input => input === LOW) === undefined;
      const pulse = allHigh ? LOW : HIGH;
      send(mod, pulse, counter, queue);
    }
  }
};

let counter = [0, 0];
for (let i = 0; i < 1000; i++) {
  ping(counter);
}

console.log(`part1: ${counter[0]*counter[1]}`);

while(true) {
  count++;
  ping(counter);
  if (Object.keys(secondHigh).length === 4) {
    break; // we have found cycles for all zr inputs that control rx
  }
}

// calculate the cycle lengths for the zr inputs
const cycles = Object.entries(secondHigh).map(([key, value]) => value - firstHigh[key]);

// calculate the count when cycles match and will trigger rx
const greatestCommonDivisor = (a, b) => b ? greatestCommonDivisor(b, a % b) : a;
const leastCommonMultiple = (a, b) => a * b / greatestCommonDivisor(a, b);
const lcm = cycles.reduce((a, b) => leastCommonMultiple(a, b));

console.log(`part2: ${lcm}`);
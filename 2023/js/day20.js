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

const ping = (counter) => {
  const queue = [ ['broadcaster', LOW, 'button'] ];
  counter[0]++;
  while (queue.length > 0) {
    const [dest, signal, source] = queue.shift();
    if (dest === 'rx' && signal === LOW) {
      return true;
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

// kind of quessed this brute force approach does not work
let count = 0;
while(true) {
  count++;
  if (ping(counter)) {
    break;
  }
}

console.log(`part2: ${count}`);
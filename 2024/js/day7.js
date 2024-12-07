import { asLines } from './util.js';

const equations = asLines('../input/7.txt').map(l => l.split(":").map(s => s.trim().split(" ").map(Number)));

const solve = (eq, operators) => {
  if (eq.length == 1) return eq;
  const a = eq[0];  // operand a for the next operation
  const b = eq[1];  // operand b for the next operation
  const res = operators.map(op => op(a, b)); // results of different ops
  // continue calculating equation forfard and build list of possible results
  return res.flatMap(r => solve([r, ...eq.slice(2)], operators))
}

const solvable = (eq, operators) => {
  const expected = eq[0][0];
  return solve(eq[1], operators).includes(expected);
}

const operators = [ (a, b) => a + b, (a, b) => a * b];
const sumOfSolvableEqs = (eqs, ops) => eqs.filter(e => solvable(e, ops))
  .map(e => e[0][0])
  .reduce((a, b) => a + b);

console.log(`a: ${sumOfSolvableEqs(equations, operators)}`);

const operators2 = [ (a, b) => a + b, (a, b) => a * b, (a, b) => Number('' + a + b)];
console.log(`b: ${sumOfSolvableEqs(equations, operators2)}`);

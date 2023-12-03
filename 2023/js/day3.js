
import { asLines } from './util.js';

const schematic = asLines('../input/day3.txt');

const sum1 = () => schematic.flatMap((row, y) => {
  const foundNumbers = Array.from(row.matchAll(/[\d]+/g)); // find numbers on this row
  return foundNumbers.flatMap((match) => {
    const { 0: number, index: x } = match; // number and x position of the number
    return adjacentToSymbol(schematic, x, x + number.length - 1, y) 
      ? [parseInt(number)] // if adjacent to symbol, return the number
      : []; // else return empty and flatMap will remove it
  });
}).reduce((a,b) => a + b, 0);

// check if there is a symbol adjacent to then given x1-x2 range on given row
const adjacentToSymbol = (schematic, x1, x2, row) => {
  for (let y = row - 1; y <= row + 1; y++) {
    for (let x = x1 - 1; x <= x2 + 1; x++) {
      if (!schematic[y] || !schematic[y][x]) continue;
      const c = schematic[y][x];
      if (c != '.' && !(c >= '0' && c <= '9')) return true;
    }
  }
  return false;
}

console.log(`part 1: ${sum1()}`);

const sum2 = () => schematic.flatMap((row, y) => {
  return row.split('').flatMap((c, x) => c === '*' ? [x] : []) // find all start x positions
    .map((x) => findAdjacentNumbers(schematic, x, y)) // find all adjacent numbers for stars (x,y)
    .filter(n => n.length === 2) // filter stars with 2 adjacent numbers
    .map(n => n.reduce((a,b) => a*b, 1)) // multiply with each other
  }).reduce((a,b) => a + b, 0); // sum all

const findAdjacentNumbers = (schematic, x, y) => {
  const numbers = [];
  for (let dy = y-1; dy <= y+1; dy++) {
    const row = schematic[dy];
    const foundNumbers = Array.from(row.matchAll(/[\d]+/g));
    const adjacent = foundNumbers.flatMap((match) => {
      const { 0: number, index: x1 } = match; // number and x position of the number
      const x2 = x1 + number.length - 1; // end position of the number
      return x2 >= x - 1 && x1 <= x + 1 ? [parseInt(number)] : [];
    });
    numbers.push(...adjacent);
  }
  return numbers;
}
  
console.log(`part 2: ${sum2()}`);

import { asLines } from './util.js';

const input = asLines('../input/day7.txt');
const hands = input.map(line => line.split(' '))

const countCards = (hand) => {
  const counts = Array.from(hand).reduce((acc, cur) => {
    acc[cur] ? acc[cur]++ : acc[cur] = 1;
    return acc;
  }, {});
  return counts;
}

const cardScore = 'AKQJT98765432'.split('');
const jokerScore = 'AKQT98765432J'.split('');
const cardScorer = (cardIndex) => (card) => cardIndex.length - cardIndex.indexOf(card);

const handScorer = (useJokers) => (hand) => {
  const cards = countCards(hand);
  let jokers = 0;
  if (useJokers) {
    jokers = cards['J'] ?? 0;
    delete cards['J'];
  }
  const kindCounts = Object.keys(cards).length;
  const maxKind = Math.max(...Object.values(cards)) + jokers;
  if (kindCounts <= 1) {
    return 6; // five of a kind
  } else if (kindCounts === 2 && maxKind === 4) {
    return 5; // four of a kind
  } else if (kindCounts === 2) {
    return 4; // full house
  } else if (maxKind === 3) {
    return 3; // three of a kind
  } else if (kindCounts === 3 && maxKind === 2) {
    return 2; // two pair
  } else if (maxKind === 2) {
    return 1; // one pair
  } 
  return 0; // high card
}

const handSorter = (a, b, handScorer, cardScorer) => {
  if (handScorer(a) !== handScorer(b)) {
    return handScorer(a) - handScorer(b);
  }
  // same hand, sort by card
  for (let i = 0; i < a.length; i++) {
    if (cardScorer(a[i]) !== cardScorer(b[i])) {
      return cardScorer(a[i]) - cardScorer(b[i]);
    }
  }
  return 0;
}

const winnings = (handScorer, cardScorer) => {
  const sorted = hands.sort((a, b) => handSorter(a[0], b[0], handScorer, cardScorer));
  return sorted.map((hand, index) => hand[1] * (index+1)).reduce((a, b) => a + b, 0);
}

const part1HandScorer = handScorer(false);
const part1CardScorer = cardScorer(cardScore);

console.log(`part 1: ${winnings(part1HandScorer, part1CardScorer)}`);

const part2HandScorer = handScorer(true);
const part2CardScorer = cardScorer(jokerScore);

console.log(`part 2: ${winnings(part2HandScorer, part2CardScorer)}`);

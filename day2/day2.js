const fs = require("node:fs");
const readline = require("node:readline");

async function parseInput(filename) {
  let nums = [];
  let f = fs.createReadStream(filename);
  const rl = readline.createInterface({
    input: f,
    crlfDelay: Infinity,
  });

  for await (let line of rl) {
    let lineArr = line.toString().split(" ");
    let row = lineArr.map((num) => parseInt(num));
    nums.push(row);
  }

  return nums;
}

function isSafe(row, depth) {
  let offendingIndices = new Set();

  for (let i = 1; i < row.length; i++) {
    let prev = row[i - 1];
    let curr = row[i];

    if (Math.abs(prev - curr) < 1 || Math.abs(prev - curr) > 3) {
      offendingIndices.add(i);
      offendingIndices.add(i - 1);
    }

    if (i < row.length - 1) {
      let next = row[i + 1];

      if ((prev < curr && curr < next) || (next < curr && curr < prev)) {
        continue;
      } else {
        offendingIndices.add(i);
        offendingIndices.add(i - 1);
        offendingIndices.add(i + 1);
      }
    }
  }

  if (depth < 1) {
    for (let idx of Array.from(offendingIndices)) {
      let withRemoval = [];

      for (let i = 0; i < row.length; i++) {
        if (i !== idx) {
          withRemoval.push(row[i]);
        }
      }

      if (isSafe(withRemoval, depth + 1)) {
        return 1;
      }
    }
  }

  return offendingIndices.size === 0 ? 1 : 0;
}

async function main() {
  let input = await parseInput("day2/input.txt");
  console.log(input);

  let sum = input.map((row) => isSafe(row, 0)).reduce((a, b) => a + b, 0);

  console.log(sum);
}

main();

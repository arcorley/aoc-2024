def part1():
  lines = []
  with open('day4/input.txt', 'r') as input:
    content = input.read()
    lines = content.splitlines()

  total = 0
  for j in range(0, len(lines)):
    line = lines[j]
    for i in range(0, len(line)):
      if line[i] == 'X' or line[i] == 'S':
        # Check diagonal right
        if i + 3 < len(line) and j + 3 < len(lines):
          w = line[i] + lines[j+1][i+1] + lines[j+2][i+2] + lines[j+3][i+3]
          if w == 'XMAS' or w == 'SAMX':
            total += 1
        
        # Check diagonal left
        if (i - 3) >= 0 and j + 3 < len(lines):
          w = line[i] + lines[j+1][i-1] + lines[j+2][i-2] + lines[j+3][i-3]
          if w == 'XMAS' or w == 'SAMX':
            total += 1
        
        # Check if we can check forward
        if i + 3 < len(line):
          w = line[i : i+4]
          if w == 'XMAS' or w == 'SAMX':
            total += 1
        
        # Check if we can check down
        if j + 3 < len(lines):
          w = line[i] + lines[j+1][i] + lines[j+2][i] + lines[j+3][i]
          if w == 'XMAS' or w == 'SAMX':
            total += 1
        
  print(total)

def part2():
  lines = []
  with open('day4/input.txt', 'r') as input:
    content = input.read()
    lines = content.splitlines()

  total = 0
  for j in range(0, len(lines)):
    line = lines[j]
    for i in range(0, len(line)):
      if line[i] == 'M' or line[i] == 'S':
        if i + 2 < len(line) and j + 2 < len(lines):
          diag1 = line[i] + lines[j + 1][i + 1] + lines[j + 2][i + 2]
          diag2 = line[i + 2] + lines[j + 1][i + 1] + lines[j + 2][i]

          if (diag1 == 'SAM' or diag1 == 'MAS') and (diag2 == 'SAM' or diag2 == 'MAS'):
            total += 1

  print(total)

part1()
part2()
package main

import (
	"fmt"
	"os"
	"regexp"
	"strings"
)

func part1(input string) int {
	regex := regexp.MustCompile("mul\\([0-9]{1,3},[0-9]{1,3}\\)")
	results := regex.FindAll([]byte(input), -1)
	total := 0

	for _, result := range results {
		asStr := string(result)
		fmt.Println(asStr)
		// get the content inside the parens
		noPrefix, _ := strings.CutPrefix(asStr, "mul(")
		onlyArgs, _ := strings.CutSuffix(noPrefix, ")")
		// split by comma
		args := strings.Split(onlyArgs, ",")
		if len(args) < 2 {
			panic("Received invalid number of args")
		}

		argsAsInt := []int{}
		for _, arg := range args {
			asInt := 0
			fmt.Sscanf(arg, "%d", &asInt)
			argsAsInt = append(argsAsInt, asInt)
		}

		cumulative := argsAsInt[0] * argsAsInt[1]

		if len(argsAsInt) > 2 {
			for i := 2; i < len(argsAsInt); i++ {
				cumulative *= argsAsInt[i]
			}
		}

		total += cumulative
	}

	return total
}

func part2(input string) int {
	segments := []string{}

	idx := 0
	on := true

	for {
		if idx >= len(input) {
			break
		}

		if on {
			// Find next "don't()"
			s := strings.Index(input[idx:], "don't()")
			fmt.Println("Idx of next don't(): ", s)
			if s == -1 {
				segments = append(segments, input[idx:])
				break
			}
			segments = append(segments, input[idx:idx+s])
			idx += s
			on = false
		} else {
			// Find next "do()"
			s := strings.Index(input[idx:], "do()")
			fmt.Println("Idx of next do(): ", s)
			if s == -1 {
				break
			}
			idx += s
			on = true
		}
	}

	cumulative := 0

	for _, segment := range segments {
		cumulative += part1(segment)
	}

	return cumulative
}

func main() {
	fmt.Println("Hello, World!")

	// Read input file into a string
	input, err := os.ReadFile("input.txt")
	if err != nil {
		fmt.Println("Error reading file")
		return
	}

	fmt.Printf("Part 1: %d\n", part1(string(input)))
	fmt.Printf("Part 2: %d\n", part2(string(input)))
}

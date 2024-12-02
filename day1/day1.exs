defmodule Day1 do
  def run(inputFile) do
    {first_col, second_col} = read_input(inputFile)

    first_sorted = Enum.sort(first_col)
    second_sorted = Enum.sort(second_col)

    Stream.with_index(first_sorted)
    |> Enum.reduce(0, fn {first, index}, acc ->
      second = Enum.at(second_sorted, index)
      diff = abs(first - second)
      acc + diff
    end)
  end

  def run_part2(inputFile) do
    {first_col, second_col} = read_input(inputFile)

    second_frequencies = Enum.frequencies(second_col)

    Enum.reduce(first_col, 0, fn first, acc ->
      acc + first * Map.get(second_frequencies, first, 0)
    end)
  end

  def read_input(file) do
    {_first_column, _second_column} =
      File.stream!(file)
      |> Enum.reduce({[], []}, fn line, {first_acc, second_acc} ->
        # Ensure no trailing or leading whitespace
        [first, second] =
          String.trim(line)
          # Split on any whitespace
          |> String.split(~r/\s+/)
          # Convert to integers
          |> Enum.map(&String.to_integer/1)

        # Add numbers to the accumulators
        {[first | first_acc], [second | second_acc]}
      end)
  end
end

IO.puts("Part 1 answer: #{Day1.run("day1/input.txt")}")
IO.puts("Part 2 answer: #{Day1.run_part2("day1/input.txt")}")

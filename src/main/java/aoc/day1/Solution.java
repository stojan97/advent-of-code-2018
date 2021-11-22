package aoc.day1;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<Integer> numbers = FileReader
      .readLines("day1")
      .stream()
      .map(Integer::parseInt)
      .collect(Collectors.toList());

    System.out.println("Part 1: " + solveFirst(numbers));
    System.out.println("Part 2: " + solveSecond(numbers));
  }

  private static int solveSecond(List<Integer> numbers) {

    int i = 0, sum = numbers.get(i);
    Set<Integer> seen = new HashSet<>();

    seen.add(0);

    while (!seen.contains(sum)) {
      seen.add(sum);
      i = (i + 1) % numbers.size();
      sum += numbers.get(i);
    }

    return sum;
  }

  private static int solveFirst(List<Integer> numbers) {

    return numbers
      .stream()
      .mapToInt(Integer::intValue)
      .sum();
  }
}

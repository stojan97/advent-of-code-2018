package aoc.day2;

import java.util.Arrays;
import java.util.List;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day2");

    System.out.println("Part 1: " + solveFirst(lines));
    System.out.println("Part 2: " + solveSecond(lines));
  }

  private static int solveFirst(List<String> input) {

    int two = 0;
    int three = 0;

    for (String s : input) {

      int[] count = new int[26];

      for (char c : s.toCharArray()) {
        count[c - 97]++;
      }

      boolean currentTwo = Arrays
        .stream(count)
        .anyMatch(c -> c == 2);

      boolean currentThree = Arrays
        .stream(count)
        .anyMatch(c -> c == 3);

      two += currentTwo ? 1 : 0;
      three += currentThree ? 1 : 0;
    }

    return two * three;
  }

  private static String solveSecond(List<String> input) {

    for (int i = 0; i < input.size(); i++) {
      for (int j = i + 1; j < input.size(); j++) {
        String result = buildEqualChars(input.get(i), input.get(j));
        if (input.get(0).length() == result.length() + 1) {
          return result;
        }
      }
    }

    return "";
  }

  private static String buildEqualChars(String s, String s1) {

    String res = "";

    for (int i = 0; i < s.length(); i++) {
      if (s.charAt(i) == s1.charAt(i)) {
        res += s.charAt(i);
      }
    }

    return res;
  }
}

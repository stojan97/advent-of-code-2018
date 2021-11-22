package aoc.day5;

import java.util.Stack;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    String str = FileReader.readLines("day5").get(0);
    int sol = solve(str, '_');
    System.out.println("First part " + sol);
    System.out.println("Second part " + solve2(str));
  }

  static int solve(String s, char skip) {

    Stack<Character> stack = new Stack<>();

    for (Character c : s.toCharArray()) {
      if (Character.toLowerCase(c) == skip) {
        continue;
      }
      if (stack.size() > 0 && isOpposite(c, stack.peek())) {
        stack.pop();
      } else {
        stack.push(c);
      }
    }

    return stack.size();
  }

  static int solve2(String s) {

    int min = Integer.MAX_VALUE;
    for (char c = 'a'; c <= 'z'; c++) {
      min = Math.min(solve(s, c), min);
    }
    return min;
  }

  static boolean isOpposite(char a, char b) {

    if (Character.toLowerCase(a) != Character.toLowerCase(b)) {
      return false;
    }
    if (Character.isLowerCase(a) && Character.isUpperCase(b)) {
      return true;
    }
    if (Character.isUpperCase(a) && Character.isLowerCase(b)) {
      return true;
    }
    return false;
  }

}

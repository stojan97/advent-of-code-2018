package aoc.day14;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.FileReader;

public class SolutionPart1 {

  static List<Integer> a = new ArrayList<>();

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day14");

    int limit = Integer.parseInt(lines.get(0));

    a.add(3);
    a.add(7);
    int first = 0, second = 1;
    while (a.size() < limit + 10) {
      int frecipe = a.get(first), srecipe = a.get(second);
      int sum = frecipe + srecipe;
      if (sum / 10 != 0) {
        a.add(sum / 10);
      }
      a.add(sum % 10);

      first = (first + frecipe + 1) % a.size();
      second = (second + srecipe + 1) % a.size();
    }
    System.out.print("Part 1: ");

    for (int i = limit; i < limit + 10; i++) {
      System.out.print(a.get(i));
    }
    System.out.println();
  }
}

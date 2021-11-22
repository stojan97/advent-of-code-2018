package aoc.day14;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.FileReader;

public class SolutionPart2 {

  static List<Integer> a = new ArrayList<>();

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day14");

    String limit = lines.get(0);
    int digits = lines.get(0).length();

    a.add(3);
    a.add(7);
    int first = 0, second = 1;
    int index = 0;

    while (index < digits) {
      int frecipe = a.get(first), srecipe = a.get(second);
      int sum = frecipe + srecipe;
      int div10 = sum / 10;
      int mod10 = sum % 10;

      if (div10 != 0) {
        a.add(div10);
        index = div10 == Character.getNumericValue(limit.charAt(index)) ? index + 1 :
                div10 == Character.getNumericValue(limit.charAt(0)) ? 1 : 0;
        if (index >= digits) {
          break;
        }
      }
      a.add(mod10);
      index = mod10 == Character.getNumericValue(limit.charAt(index)) ? index + 1 :
              mod10 == Character.getNumericValue(limit.charAt(0)) ? 1 : 0;

      first = (first + frecipe + 1) % a.size();
      second = (second + srecipe + 1) % a.size();
    }

    System.out.println("Part 2: " + (a.size() - digits));
  }
}

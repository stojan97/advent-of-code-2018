package aoc.day12;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.utils.FileReader;

public class Solution {

  static int firstIndex = 250;
  static String init;
  static char[] a;
  static Map<String, Character> notes = new HashMap<>();
  static long countPart1, countPart2;

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day12");

    for (String line : lines) {
      if (line.isEmpty()) {
        continue;
      }
      String[] s = line.split(" ");

      if (s[0].equals("initial")) {
        init = s[2];
      } else {
        notes.put(s[0], s[2].charAt(0));
      }
    }

    solve();
    System.out.println("Part 1: " + countPart1);
    System.out.println("Part 2: " + countPart2);
  }

  static long count(long k) {

    long count = 0L;
    for (int i = 0; i < a.length; i++) {
      if (a[i] == '#') {
        long offset = i - firstIndex;
        long add = 50000000000L - (k - 1);
        long include = offset + add;
        count += (k == 20L) ? offset : include;
      }
    }
    return count;
  }

  static void solve() {

    String negative = "", positive = "";

    for (int i = 0; i < 250; i++) {
      negative += ".";
    }

    for (int i = 0; i < 250; i++) {
      positive += ".";
    }

    String merged = negative + init + positive;
    a = merged.toCharArray();

    long k = 1;
    for (; k <= 162L; k++) {
      char[] b = a.clone();
      for (int i = 2; i < b.length - 2; i++) {
        String s = new String(b, i - 2, 5);
        Character comp = notes.get(s);
        a[i] = (comp != null) ? comp : '.';
      }
      if (k == 20L) {
        countPart1 = count(k);
      }
    }

    countPart2 = count(k);
  }
}

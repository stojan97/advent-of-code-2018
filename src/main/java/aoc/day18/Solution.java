package aoc.day18;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.utils.FileReader;

public class Solution {

  static int di[] = { -1, -1, -1, 0, 0, 1, 1, 1 };
  static int dj[] = { -1, 0, 1, -1, 1, -1, 0, 1 };
  static Map<Character, Character> conversions = new HashMap<>();
  static Map<Integer, Integer> resultMap = new HashMap<>();
  static char a[][], b[][];
  static int after10Minutes = 0;

  public static void main(String[] args) throws IOException {

    List<String> lines = FileReader.readLines("day18");

    a = new char[lines.size()][];

    for (int i = 0; i < lines.size(); i++) {
      a[i] = lines.get(i).toCharArray();
    }
    conversions.put('.', '|');
    conversions.put('|', '#');
    conversions.put('#', '.');
    solve();
    System.out.println("Part 1: " + after10Minutes);
    System.out.println("Part 2: " + resultMap.get(1000000000 % 28));
  }

  static boolean inRange(int i, int j) {

    return i >= 0 && i < a.length && j >= 0 && j < a[i].length;
  }

  static int countAdjacent(int i, int j, char type) {

    int count = 0;
    for (int k = 0; k < 8; k++) {
      int pi = i + di[k];
      int pj = j + dj[k];

      if (inRange(pi, pj) && a[pi][pj] == type) {
        count++;
      }
    }

    return count;
  }

  static void changeMap() {

    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[i].length; j++) {
        char next = conversions.get(a[i][j]);
        if ((a[i][j] == '.' || a[i][j] == '|') && countAdjacent(i, j, next) >= 3) {
          b[i][j] = next;
        }
        if (a[i][j] == '#' && (countAdjacent(i, j, '#') < 1 || countAdjacent(i, j, '|') < 1)) {
          b[i][j] = next;
        }
      }
    }
  }

  static int count() {

    int trees = 0, lumberyards = 0;
    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[i].length; j++) {
        if (a[i][j] == '|') {
          trees++;
        }
        if (a[i][j] == '#') {
          lumberyards++;
        }
      }
    }

    return trees * lumberyards;
  }

  static void solve() {

    int r = count();

    for (int t = 1; t <= 513; t++) {
      b = new char[a.length][];
      for (int i = 0; i < a.length; i++) {
        b[i] = a[i].clone();
      }

      changeMap();

      for (int i = 0; i < b.length; i++) {
        a[i] = b[i].clone();
      }

      int res = count();

      if (t >= 486) {
        resultMap.put(t % 28, res);
      }
      if (t == 10) {
        after10Minutes = res;
      }
    }
  }

}

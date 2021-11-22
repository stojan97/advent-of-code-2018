package aoc.day11;

import java.util.List;

import aoc.utils.FileReader;

public class SolutionPart1 {

  static int[][] a = new int[301][301];
  static int x = -1, y = -1;

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day11");

    int s = Integer.parseInt(lines.get(0));
    solve(s);
    System.out.println("Part 1: " + x + "," + y);
  }

  static void solve(int s) {

    int max = Integer.MIN_VALUE;

    for (int i = 1; i <= 300; i++) {
      for (int j = 1; j <= 300; j++) {
        int rackId = j + 10;
        int tmp = rackId * i + s;
        int mul = ((tmp * rackId) / 100) % 10;
        int res = mul - 5;
        a[i][j] = res;
      }
    }

    for (int i = 1; i <= 298; i++) {
      for (int j = 1; j <= 298; j++) {
        int sum = 0;
        for (int k = i; k <= i + 2; k++) {
          for (int u = j; u <= j + 2; u++) {
            sum += a[k][u];
          }
        }
        if (sum > max) {
          max = sum;
          x = j;
          y = i;
        }
      }
    }
  }
}

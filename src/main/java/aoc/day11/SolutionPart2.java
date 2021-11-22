package aoc.day11;

import java.util.List;

import aoc.utils.FileReader;

public class SolutionPart2 {

  static int a[][] = new int[301][301];
  static int x = -1, y = -1;
  static int sq = 0;

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day11");

    int s = Integer.parseInt(lines.get(0));
    solve(s);
    System.out.println("Second part: " + x + "," + y + "," + sq);
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
        if (res > max) {
          max = res;
          x = j;
          y = i;
        }
      }
    }

    for (int i = 1; i <= 299; i++) {
      for (int j = 1; j <= 299; j++) {
        for (int ti = 1, tj = 1; ti <= 300 - i && tj <= 300 - j; ti++, tj++) {
          int sum = 0;
          if (ti != tj) {
            continue;
          }
          for (int k = i; k <= i + ti; k++) {
            for (int u = j; u <= j + tj; u++) {
              sum += a[k][u];
            }
          }
          if (sum > max) {
            max = sum;
            x = j;
            y = i;
            sq = ti + 1;
          }
        }
      }
    }
  }

}

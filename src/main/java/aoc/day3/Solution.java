package aoc.day3;

import java.util.List;

import aoc.utils.FileReader;

public class Solution {

  static int a[][] = new int[1000][1000];
  static int count = 0;
  static boolean claims[];

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day3");
    claims = new boolean[lines.size() + 1];

    for (String line : lines) {
      String[] arr = line.replaceAll("[^0-9]+", ",").split(",");

      int claim = Integer.parseInt(arr[1]);
      int i = Integer.parseInt(arr[2]);
      int j = Integer.parseInt(arr[3]);
      int endj = Integer.parseInt(arr[4]);
      int endi = Integer.parseInt(arr[5]);

      fillAndCount(claim, i, j, endi, endj);
    }

    System.out.println("Part 1:" + count);
    System.out.println("Part 2: " + getIntactClaim());
  }

  static void fillAndCount(int claim, int x, int y, int ei, int ej) {

    for (int i = y; i < y + ei; i++) {
      for (int j = x; j < x + ej; j++) {
        if (a[i][j] == 0) {
          a[i][j] = claim;
        } else {
          claims[claim] = true;
          if (a[i][j] != -1) {
            claims[a[i][j]] = true;
            count++;
            a[i][j] = -1;
          }
        }
      }
    }
  }

  static int getIntactClaim() {

    for (int i = 1; i < claims.length; i++) {
      if (!claims[i]) {
        return i;
      }
    }
    return -1;
  }
}

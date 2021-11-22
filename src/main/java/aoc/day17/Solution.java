package aoc.day17;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

import aoc.utils.FileReader;

public class Solution {

  static int di[] = { 0, 0 };
  static int dj[] = { -1, 1 };
  static char a[][] = new char[2000][2000];
  static int mini = Integer.MAX_VALUE, maxi = Integer.MIN_VALUE;
  static int score = 0;
  static HashSet<Pair> visited = new HashSet<>();

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day17");

    for (int i = 0; i < 2000; i++) {
      for (int j = 0; j < 2000; j++) {
        a[i][j] = '.';
      }
    }

    for (String line : lines) {
      String[] arr = line.split("\\W");
      String type = arr[0];
      int start = Integer.parseInt(arr[4]);
      int end = Integer.parseInt(arr[6]);
      int corr = Integer.parseInt(arr[1]);

      if (type.equals("y")) {
        mini = Math.min(mini, corr);
        maxi = Math.max(maxi, corr);
      } else {
        mini = Math.min(mini, start);
        maxi = Math.max(maxi, start);
        mini = Math.min(mini, end);
        maxi = Math.max(maxi, end);
      }

      for (int i = start; i <= end; i++) {
        if (type.equals("x")) {
          a[i][corr] = '#';
        } else {
          a[corr][i] = '#';
        }
      }
    }
    bfs(mini, 500);
    int res = findDryWater(mini, 500);

    System.out.println("Part 1: " + score);
    System.out.println("Part 2: " + (score - res));
  }

  static Queue<Pair> q = new LinkedList<>();

  static void incrementScore(int i, int j) {

    if (a[i][j] == '.') {
      score++;
    }
  }

  static boolean dfsSide(int i, int j, int dir) {

    incrementScore(i, j);

    if (a[i][j] == '.') {
      a[i][j] = 'S';
    }

    if (a[i][j] == 'R' || a[i + 1][j] == 'R') {
      return false;
    }

    if (a[i + 1][j] == '.') {
      a[i][j] = 'R';
      q.add(new Pair(i + 1, j));
      incrementScore(i + 1, j);
      a[i + 1][j] = 'W';
      return false;
    }

    if (a[i][j + dir] == '.' || a[i][j + dir] == 'U' || a[i][j + dir] == 'S' || a[i][j + dir] == 'R'
        || a[i][j + dir] == 'W') {
      return dfsSide(i, j + dir, dir);
    }

    return true;
  }

  static void bfs(int i, int j) {

    q.add(new Pair(i, j));
    incrementScore(i, j);
    a[i][j] = 'W';

    while (!q.isEmpty()) {
      Pair curr = q.poll();

      if (curr.i + 1 > maxi) {
        continue;
      }

      if (a[curr.i + 1][curr.j] == '.') {
        q.add(new Pair(curr.i + 1, curr.j));
        incrementScore(curr.i + 1, curr.j);
        a[curr.i + 1][curr.j] = 'W';
        continue;
      }

      if (a[curr.i + 1][curr.j] == '#' || a[curr.i + 1][curr.j] == 'U' || a[curr.i + 1][curr.j] == 'S') {
        boolean first = true, second = true;

        if (a[curr.i + 1][curr.j] == 'S') {
          curr.i++;
        }

        if (a[curr.i][curr.j + 1] == '.' || a[curr.i][curr.j + 1] == 'U' || a[curr.i][curr.j + 1] == 'S'
            || a[curr.i][curr.j + 1] == 'W') {
          first = dfsSide(curr.i, curr.j + 1, 1);
        }

        if (a[curr.i][curr.j - 1] == '.' || a[curr.i][curr.j - 1] == 'U' || a[curr.i][curr.j - 1] == 'S'
            || a[curr.i][curr.j - 1] == 'W') {
          second = dfsSide(curr.i, curr.j - 1, -1);
        }

        a[curr.i][curr.j] = 'U';
        if (first && second) {
          q.add(new Pair(curr.i - 1, curr.j));
          incrementScore(curr.i - 1, curr.j);
          a[curr.i - 1][curr.j] = 'U';
        }

        continue;
      }

    }

  }

  static boolean isBetween(int i, int j) {

    return a[i][j + 1] != 'S' && a[i][j + 1] != 'U' && a[i][j - 1] != 'S' && a[i][j - 1] != 'U';
  }

  static int findDryWater(int i, int j) {

    Queue<Pair> res = new LinkedList<>();
    int sum = 1;
    Pair start = new Pair(i, j);
    visited.add(start);
    res.add(start);

    while (!res.isEmpty()) {
      Pair curr = res.poll();

      if (((a[curr.i][curr.j] == 'W' && isBetween(curr.i, curr.j)) || a[curr.i][curr.j] == 'R')
          && a[curr.i + 1][curr.j] != '.' && a[curr.i + 1][curr.j] != '#'
          && !visited.contains(new Pair(curr.i + 1, curr.j))) {
        Pair pp = new Pair(curr.i + 1, curr.j);
        visited.add(pp);
        res.add(pp);
        sum++;
        continue;
      }

      for (int k = 0; k < 2; k++) {
        int pi = curr.i + di[k];
        int pj = curr.j + dj[k];
        Pair pp = new Pair(pi, pj);

        if (a[pi][pj] != '#' && a[pi][pj] != '.' && !visited.contains(pp)) {
          visited.add(pp);
          res.add(pp);
          sum++;
        }
      }
    }

    return sum;
  }

  static class Pair {

    int i, j;

    Pair(int i, int j) {

      this.i = i;
      this.j = j;
    }

    @Override
    public boolean equals(Object o) {

      Pair p = (Pair) o;
      return this.i == p.i && this.j == p.j;
    }

    @Override
    public int hashCode() {

      return Objects.hash(i, j);
    }

  }

}

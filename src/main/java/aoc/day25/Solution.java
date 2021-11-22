package aoc.day25;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.FileReader;

public class Solution {

  static class Point {

    int x, y, z, r, id = -1;

    Point(int x, int y, int z, int r) {

      this.x = x;
      this.y = y;
      this.z = z;
      this.r = r;
    }

    public int distTo(Point to) {

      return Math.abs(this.x - to.x) + Math.abs(this.y - to.y) + Math.abs(this.z - to.z) + Math.abs(this.r - to.r);
    }
  }

  static List<List<Integer>> graph = new ArrayList<>();
  static List<Point> points = new ArrayList<>();
  static boolean visited[];
  static int constellations = 0;

  static void dfs(int curr) {

    visited[curr] = true;

    for (int i : graph.get(curr)) {
      if (!visited[i]) {
        dfs(i);
      }
    }
  }

  public static void main(String[] args) {

    List<String> input = FileReader.readLines("day25");

    for (String line : input) {
      String[] split = line.split(",");
      Point point = new Point(Integer.parseInt(split[0]), Integer.parseInt(split[1]), Integer.parseInt(split[2]),
        Integer.parseInt(split[3]));
      points.add(point);
      graph.add(new ArrayList<>());
    }

    visited = new boolean[points.size()];

    for (int i = 0; i < points.size(); i++) {
      Point curr = points.get(i);
      for (int j = i + 1; j < points.size(); j++) {
        Point to = points.get(j);
        if (curr.distTo(to) <= 3) {
          graph.get(i).add(j);
          graph.get(j).add(i);
        }
      }
    }

    for (int i = 0; i < points.size(); i++) {
      if (!visited[i]) {
        dfs(i);
        constellations++;
      }
    }

    System.out.println("Part 1: " + constellations);
  }
}

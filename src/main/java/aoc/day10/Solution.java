package aoc.day10;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  static Set<Point> points = new HashSet<>();
  static List<Point> pointsList = new ArrayList<Point>();
  static List<Velocity> velocities = new ArrayList<>();
  static long s = 0L;
  static int minx = Integer.MAX_VALUE, maxx = Integer.MIN_VALUE, miny = Integer.MAX_VALUE, maxy = Integer.MIN_VALUE;

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day10");

    for (String s : lines) {
      String[] numbers = s.split("[^-?\\d+| $]");
      int x = Integer.parseInt(numbers[10].trim());
      int y = Integer.parseInt(numbers[11].trim());
      int velx = Integer.parseInt(numbers[22].trim());
      int vely = Integer.parseInt(numbers[23].trim());
      Point p = new Point(x, y);
      pointsList.add(p);
      velocities.add(new Velocity(velx, vely));
    }
    solve();

    System.out.println("First part ");
    System.out.println();

    for (int i = minx; i <= maxx; i++) {
      for (int j = miny; j <= maxy; j++) {
        System.out.print(points.contains(new Point(i, j)) ? "#" : ".");
      }
      System.out.println();
    }

    System.out.println();
    System.out.println("Second part " + s);
  }

  static void solve() {

    while (!checkPoints()) {

      for (int i = 0; i < pointsList.size(); i++) {
        Velocity v = velocities.get(i);
        Point p = pointsList.get(i);
        p.x += v.velx;
        p.y += v.vely;
        pointsList.set(i, p);
      }
      s++;
    }

    for (Point p : pointsList) {
      minx = Math.min(p.x, minx);
      miny = Math.min(p.y, miny);
      maxx = Math.max(p.x, maxx);
      maxy = Math.max(p.y, maxy);
      points.add(p);
    }
  }

  static boolean checkPoints() {

    Map<Integer, Integer> countx = new HashMap<>();
    Map<Integer, Integer> county = new HashMap<>();
    for (Point p : pointsList) {
      Integer px = countx.get(p.x);
      int ix = (px == null) ? 0 : px;
      countx.put(p.x, ix + 1);
      Integer py = county.get(p.y);
      int iy = (py == null) ? 0 : py;
      county.put(p.y, iy + 1);
    }

    int maxx = Collections
      .max(countx.entrySet(), Comparator.comparing(Map.Entry<Integer, Integer>::getValue))
      .getValue();

    int maxy = Collections
      .max(county.entrySet(), Comparator.comparing(Map.Entry<Integer, Integer>::getValue))
      .getValue();

    return maxx >= 50 || maxy >= 50;
  }

  static class Point {

    int x, y;

    Point(int x, int y) {

      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object obj) {

      Point p = (Point) obj;
      return this.x == p.x && this.y == p.y;
    }

    @Override
    public int hashCode() {

      return Objects.hash(x, y);
    }
  }

  static class Velocity {

    int velx, vely;

    Velocity(int velx, int vely) {

      this.velx = velx;
      this.vely = vely;
    }
  }
}

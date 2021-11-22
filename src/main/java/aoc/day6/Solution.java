package aoc.day6;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import aoc.utils.FileReader;

public class Solution {

  static int maxRow = Integer.MIN_VALUE, minRow = Integer.MAX_VALUE;
  static int maxCol = Integer.MIN_VALUE, minCol = Integer.MAX_VALUE;
  static int maxDistToAllPointsCounter = 0;
  static Map<Point, Result> points = new HashMap<>();

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day6");

    for (String line : lines) {
      String[] div = line.trim().split(", ");
      int row = Integer.parseInt(div[0]);
      int col = Integer.parseInt(div[1]);
      maxRow = Math.max(row, maxRow);
      minRow = Math.min(row, minRow);
      maxCol = Math.max(col, maxCol);
      minCol = Math.min(col, minCol);
      points.put(new Point(row, col), new Result(true));
    }

    System.out.println("First part: " + getResult());
    System.out.println("Second part: " + maxDistToAllPointsCounter);
  }

  static int distance(Point a, Point b) {

    return Math.abs(a.row - b.row) + Math.abs(a.col - b.col);
  }

  static int getLargestArea() {

    int maxArea = Integer.MIN_VALUE;

    for (Map.Entry<Point, Result> entry : points.entrySet()) {
      if (entry.getValue().isFinite) {
        maxArea = Math.max(maxArea, entry.getValue().area);
      }
    }

    return maxArea;
  }

  static int getResult() {

    for (int i = minRow; i <= maxRow; i++) {
      for (int j = minCol; j <= maxCol; j++) {

        boolean isCoor = points.containsKey(new Point(i, j));
        Point closestPoint = null;
        boolean moreThanOnePoint = false;
        int minDistance = Integer.MAX_VALUE;
        int distToAllPoints = 0;

        for (Map.Entry<Point, Result> entry : points.entrySet()) {
          int dist = distance(entry.getKey(), new Point(i, j));
          distToAllPoints += dist;

          if (dist < minDistance) {
            minDistance = dist;
            closestPoint = entry.getKey();
            moreThanOnePoint = false;
          } else if (dist == minDistance) {
            moreThanOnePoint = true;
          }
        }

        if (distToAllPoints < 10000) {
          maxDistToAllPointsCounter++;
        }

        if (isCoor || closestPoint == null || moreThanOnePoint) {
          continue;
        }

        Result res = points.get(closestPoint);
        if (i == minRow || i == maxRow || j == minCol || j == maxCol) {
          res.isFinite = false;
        }
        res.area++;
        points.put(closestPoint, res);
      }
    }

    return getLargestArea();
  }

  static class Result {

    int area = 1;
    boolean isFinite;

    Result(boolean isFinite) {

      this.isFinite = isFinite;
    }
  }

  static class Point {

    int row, col;

    Point(int row, int col) {

      this.row = row;
      this.col = col;
    }

    @Override
    public boolean equals(Object obj) {

      Point p = (Point) obj;
      return this.row == p.row && this.col == p.col;
    }

    @Override
    public int hashCode() {

      return Objects.hash(row, col);
    }
  }

}

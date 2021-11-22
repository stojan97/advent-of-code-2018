package aoc.day23;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> input = FileReader.readLines("day23");
    List<NanoBot> nanoBots = new ArrayList<>();

    for (String row : input) {
      String[] split = row.split("[^-?\\d+| $]");
      int[] c = { Integer.parseInt(split[5]), Integer.parseInt(split[6]), Integer.parseInt(split[7]) };
      int r = Integer.parseInt(split[11]);
      nanoBots.add(new NanoBot(new Point(c[0], c[1], c[2]), r));
    }

    int solvePartOne = solvePartOne(nanoBots);
    int distanceToStart = solvePartTwo(nanoBots);

    System.out.println("Part 1: " + solvePartOne);
    System.out.println("Part 2: " + distanceToStart);
  }

  private static int solvePartOne(List<NanoBot> nanoBots) {

    NanoBot largestSignalNanoBot = nanoBots
      .stream()
      .max(Comparator.comparing(nanoBot -> nanoBot.r))
      .get();

    return (int) nanoBots
      .stream()
      .filter(largestSignalNanoBot::covers)
      .count();
  }

  private static int solvePartTwo(List<NanoBot> nanoBots) {

    int minDistanceToStart = Integer.MAX_VALUE;
    int maxNanoBotCoverage = Integer.MIN_VALUE;

    int minCoordinate = nanoBots
      .stream()
      .mapToInt(NanoBot::getMinCoordinate)
      .min()
      .orElseThrow(NoSuchElementException::new);

    int maxCoordinate = nanoBots
      .stream()
      .mapToInt(NanoBot::getMaxCoordinate)
      .max()
      .orElseThrow(NoSuchElementException::new);

    minCoordinate += minCoordinate % 2 != 0 ? -1 : 0;
    maxCoordinate += maxCoordinate % 2 != 0 ? 1 : 0;

    Range initRangeX = new Range(minCoordinate, maxCoordinate);
    Range initRangeY = new Range(minCoordinate, maxCoordinate);
    Range initRangeZ = new Range(minCoordinate, maxCoordinate);
    Octant root = new Octant(initRangeX, initRangeY, initRangeZ, nanoBots);
    PriorityQueue<Octant> priorityQueue = new PriorityQueue<>();
    priorityQueue.add(root);

    while (!priorityQueue.isEmpty()) {
      Octant topOctant = priorityQueue.poll();

      if (topOctant.getNanoBotsSize() <= maxNanoBotCoverage) {
        break;
      }

      if (topOctant.checkIfSingleCell()) {
        minDistanceToStart = topOctant.getDistanceToStart();
        maxNanoBotCoverage = topOctant.getNanoBotsSize();
        continue;
      }

      for (Range currentRangeX : topOctant.rangeX.divideRange()) {
        for (Range currentRangeY : topOctant.rangeY.divideRange()) {
          for (Range currentRangeZ : topOctant.rangeZ.divideRange()) {
            Point centerPoint = Point.createCenterPointFromRanges(currentRangeX, currentRangeY, currentRangeZ);
            int octantRadius = currentRangeX.distanceToCenter();
            List<NanoBot> currentNanobots = getTouchingNanobots(centerPoint, octantRadius, topOctant.nanoBots);
            if (currentNanobots.size() == 0) {
              continue;
            }
            Octant currentOctant = new Octant(currentRangeX, currentRangeY, currentRangeZ, currentNanobots);
            priorityQueue.add(currentOctant);
          }
        }
      }
    }

    return minDistanceToStart;
  }

  private static List<NanoBot> getTouchingNanobots(Point centerPoint, int octantRadius, List<NanoBot> nanoBots) {

    return nanoBots
      .stream()
      .filter(nanoBot -> nanoBot.isTouchingOctant(centerPoint, octantRadius))
      .collect(Collectors.toList());
  }

  private static class Octant implements Comparable<Octant> {

    Range rangeX;
    Range rangeY;
    Range rangeZ;
    Point center;
    List<NanoBot> nanoBots;

    public Octant(Range rangeX, Range rangeY, Range rangeZ, List<NanoBot> nanoBots) {

      this.rangeX = rangeX;
      this.rangeY = rangeY;
      this.rangeZ = rangeZ;
      this.center = Point.createCenterPointFromRanges(rangeX, rangeY, rangeZ);
      this.nanoBots = nanoBots;
    }

    @Override
    public int compareTo(Octant otherOctant) {

      int compareNanoBotsSize = Integer.compare(otherOctant.getNanoBotsSize(), getNanoBotsSize());
      if (compareNanoBotsSize != 0) {
        return compareNanoBotsSize;
      }

      return Integer.compare(getDistanceToStart(), otherOctant.getDistanceToStart());
    }

    public int getDistanceToStart() {

      return Math.abs(center.x) + Math.abs(center.y) + Math.abs(center.z);
    }

    public boolean checkIfSingleCell() {

      return rangeX.distanceToCenter() == 0;
    }

    public int getNanoBotsSize() {

      return nanoBots.size();
    }
  }

  private static class Range {

    int start;
    int end;
    int center;

    public Range(int start, int end) {

      this.start = start;
      this.end = end;
      this.center = getCenter(start, end);
    }

    public List<Range> divideRange() {

      List<Range> ranges = new ArrayList<>();
      ranges.add(new Range(start, center));
      ranges.add(new Range(center, end - start == 1 ? center : end));
      return ranges;
    }

    public int distanceToCenter() {

      return center - start;
    }

    private int getCenter(int start, int end) {

      return start + (end - start) / 2;
    }
  }

  private static class NanoBot {

    Point point;
    int r;

    public NanoBot(Point point, int r) {

      this.point = point;
      this.r = r;
    }

    public boolean covers(NanoBot other) {

      return point.distanceTo(other.point) <= r;
    }

    public int getMinCoordinate() {

      return Math.min(Math.min(point.x, point.y), point.z) - r;
    }

    public int getMaxCoordinate() {

      return Math.max(Math.max(point.x, point.y), point.z) + r;
    }

    public boolean isTouchingOctant(Point octantCenter, int octantRadius) {

      return point.distanceTo(octantCenter) - 3 * octantRadius <= r;
    }
  }

  private static class Point {

    int x;
    int y;
    int z;

    public Point(int x, int y, int z) {

      this.x = x;
      this.y = y;
      this.z = z;
    }

    public static Point createCenterPointFromRanges(Range rangeX, Range rangeY, Range rangeZ) {

      return new Point(rangeX.center, rangeY.center, rangeZ.center);
    }

    public int distanceTo(Point other) {

      return Math.abs(x - other.x) + Math.abs(y - other.y) + Math.abs(z - other.z);
    }
  }
}

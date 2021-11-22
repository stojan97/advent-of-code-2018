package aoc.day22;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  private static final int[] DX = { 0, -1, 1, 0 };
  private static final int[] DY = { -1, 0, 0, 1 };

  private static int DEPTH;
  private static int TARGET_X;
  private static int TARGET_Y;

  public static void main(String[] args) {

    List<String> input = FileReader.readLines("day22");
    DEPTH = Integer.parseInt(input.get(0).split(": ")[1]);
    String coordinates = input.get(1).split(": ")[1];
    TARGET_X = Integer.parseInt(coordinates.split(",")[0]);
    TARGET_Y = Integer.parseInt(coordinates.split(",")[1]);

    int limit = Math.max(TARGET_X, TARGET_Y) * 2;

    int[][] erosionLevel = new int[limit][limit];
    prepareErosionLevels(erosionLevel);
    long start = System.currentTimeMillis();
    int partOne = solvePartOne(erosionLevel);
    int partTwo = solvePartTwo(erosionLevel);
    long end = System.currentTimeMillis();
    System.out.println((end - start) / 1000.0);
    // executes 4 secs on avg.
    System.out.println("Part 1: " + partOne);
    System.out.println("Part 2: " + partTwo);
  }

  private static void prepareErosionLevels(int[][] erosionLevel) {

    for (int y = 0; y < erosionLevel.length; y++) {
      for (int x = 0; x < erosionLevel.length; x++) {
        erosionLevel[y][x] = getErosionLevel(x, y, erosionLevel);
      }
    }
  }

  private static int getErosionLevel(int x, int y, int[][] erosionLevel) {

    int geologicIndex = getGeologicIndex(x, y);
    if (geologicIndex == -1) {
      geologicIndex = erosionLevel[y][x - 1] * erosionLevel[y - 1][x];
    }
    return (geologicIndex + DEPTH) % 20183;
  }

  private static int getGeologicIndex(int x, int y) {

    if (x == 0 && y == 0) {
      return 0;
    }

    if (x == TARGET_X && y == TARGET_Y) {
      return 0;
    }

    if (x != 0 && y == 0) {
      return x * 16807;
    }

    if (x == 0) {
      return y * 48271;
    }

    return -1;
  }

  private static int solvePartOne(int[][] erosionLevel) {

    int riskLevel = 0;

    for (int y = 0; y <= TARGET_Y; y++) {
      for (int x = 0; x <= TARGET_X; x++) {
        riskLevel += erosionLevel[y][x] % 3;
      }
    }

    return riskLevel;
  }

  private static int solvePartTwo(int[][] erosionLevel) {

    PriorityQueue<Cell> pq = new PriorityQueue<>(Comparator.comparing(cell -> cell.distance));
    pq.add(new Cell(0, 0, Tool.TORCH, 0, erosionLevel[0][0] % 3));
    Set<Cell> visited = new HashSet<>();

    while (!pq.isEmpty()) {

      Cell top = pq.poll();
      int currentDistance = top.distance;
      if (top.x == TARGET_X && top.y == TARGET_Y) {
        return currentDistance;
      }

      if (visited.contains(top)) {
        continue;
      }
      Set<Tool> currentPossibleTools = getPossibleToolsForRegion(top.x, top.y, top.regionType);
      visited.add(top);

      for (int k = 0; k < 4; k++) {
        int currentX = top.x + DX[k];
        int currentY = top.y + DY[k];
        if (currentX < 0 || currentY < 0) {
          continue;
        }
        int regionType = erosionLevel[currentY][currentX] % 3;
        Set<Tool> nextCellPossibleTools = getPossibleToolsForRegion(currentX, currentY, regionType);
        Set<Tool> intersection = new HashSet<>(currentPossibleTools);
        intersection.retainAll(nextCellPossibleTools);

        for (Tool nextTool : intersection) {
          int switchMinutes = top.currentTool != nextTool ? 7 : 0;
          pq.add(new Cell(currentX, currentY, nextTool, currentDistance + 1 + switchMinutes, regionType));
        }
      }
    }

    return -1;
  }

  private static Set<Tool> getPossibleToolsForRegion(int x, int y, int regionType) {

    if (x == TARGET_X && y == TARGET_Y) {
      return Set.of(Tool.TORCH);
    }
    if (regionType == 0) {
      return Set.of(Tool.TORCH, Tool.CLIMBING_GEAR);
    }
    if (regionType == 1) {
      return Set.of(Tool.CLIMBING_GEAR, Tool.NEITHER);
    }
    if (regionType == 2) {
      return Set.of(Tool.TORCH, Tool.NEITHER);
    }
    throw new IllegalStateException();
  }

  private static class Cell {

    int x;
    int y;
    Tool currentTool;
    int distance;
    int regionType;

    public Cell(int x, int y, Tool currentTool, int distance, int regionType) {

      this.x = x;
      this.y = y;
      this.currentTool = currentTool;
      this.distance = distance;
      this.regionType = regionType;
    }

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Cell cell = (Cell) o;
      return x == cell.x && y == cell.y && currentTool == cell.currentTool;
    }

    @Override
    public int hashCode() {

      return Objects.hash(x, y, currentTool);
    }
  }

  private enum Tool {

    TORCH,
    CLIMBING_GEAR,
    NEITHER
  }

}

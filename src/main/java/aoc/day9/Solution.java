package aoc.day9;

import java.util.Arrays;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    String input = FileReader.readLines("day9").get(0);
    String[] s = input.split(" ");
    int totalPlayers = Integer.parseInt(s[0]);
    int lastMarblePart1 = Integer.parseInt(s[6]);
    int lastMarblePart2 = Integer.parseInt(s[6]) * 100;
    long[] part = solveParts(totalPlayers, lastMarblePart1, lastMarblePart2);

    System.out.println("Part 1: " + part[0]);
    System.out.println("Part 2: " + part[1]);
  }

  private static long[] solveParts(int totalPlayers, int lastMarblePart1, int lastMarblePart2) {

    long[][] playerScore = new long[2][totalPlayers + 1];
    int[] marbleNext = new int[lastMarblePart2 + 1];
    int[] marblePrevious = new int[lastMarblePart2 + 1];
    marblePrevious[0] = 1;
    marbleNext[0] = 1;
    marblePrevious[1] = 0;
    marbleNext[1] = 0;
    int currentMarble = 1;
    int currentPlayer = 2;

    for (
      int newMarble = 2;
      newMarble <= lastMarblePart2;
      newMarble++, currentPlayer = currentPlayer + 1 > totalPlayers ? 1 : currentPlayer + 1) {

      if (newMarble % 23 == 0) {
        for (int i = 1; i <= 7; i++) {
          currentMarble = marblePrevious[currentMarble];
        }
        if (newMarble <= lastMarblePart1) {
          playerScore[0][currentPlayer] += newMarble + currentMarble;
        }
        playerScore[1][currentPlayer] += newMarble + currentMarble;

        int nextToRemovingMarble = marbleNext[currentMarble];
        removeMarble(currentMarble, marbleNext, marblePrevious);
        currentMarble = nextToRemovingMarble;
        continue;
      }
      placeMarble(currentMarble, newMarble, marbleNext, marblePrevious);
      currentMarble = newMarble;
    }

    long part1 = Arrays.stream(playerScore[0]).max().orElse(0);
    long part2 = Arrays.stream(playerScore[1]).max().orElse(0);

    return new long[] { part1, part2 };
  }

  private static void removeMarble(int currentMarble, int[] marbleNext, int[] marblePrevious) {

    int left = marblePrevious[currentMarble];
    int right = marbleNext[currentMarble];
    marbleNext[left] = right;
    marblePrevious[right] = left;
  }

  private static void placeMarble(int currentMarble, int newMarble, int[] marbleNext, int[] marblePrevious) {

    int left = marbleNext[currentMarble];
    int right = marbleNext[left];
    marbleNext[newMarble] = right;
    marblePrevious[newMarble] = left;

    marbleNext[left] = newMarble;
    marblePrevious[right] = newMarble;
  }
}

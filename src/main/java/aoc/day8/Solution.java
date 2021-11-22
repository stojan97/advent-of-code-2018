package aoc.day8;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.FileReader;

public class Solution {
  static List<Integer> input = new ArrayList<>();
  static int totalMetadataEntries = 0, globalIndex = 0;

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day8");

    String[] inputElements = lines.get(0).split(" ");

    for (String in : inputElements) {
      input.add(Integer.parseInt(in));
    }

    int valRoot = dfs();
    System.out.println("First part " + totalMetadataEntries);
    System.out.println("Second part: " + valRoot);
  }

  static int dfs() {

    int metadataEntries = input.get(globalIndex + 1);
    int childNodes = input.get(globalIndex);
    globalIndex += 2;
    int tempNodes[] = new int[childNodes];
    int currentSum = 0;

    for (int i = 0; i < childNodes; i++) {
      tempNodes[i] = dfs();
    }

    for (int i = 0; i < metadataEntries; i++, globalIndex++) {
      int currMetadata = input.get(globalIndex);
      totalMetadataEntries += currMetadata;
      if (childNodes == 0) {
        currentSum += currMetadata;
      } else {
        if (currMetadata <= childNodes) {
          currentSum += tempNodes[currMetadata - 1];
        }
      }

    }

    return currentSum;
  }
}

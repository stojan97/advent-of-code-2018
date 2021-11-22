package aoc.day7;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import aoc.utils.FileReader;

public class SolutionPart1 {

  static List<List<Integer>> graph = new ArrayList<List<Integer>>(26);
  static boolean[] visited = new boolean[26];
  static int[] counts = new int[26];
  static PriorityQueue<Integer> pq = new PriorityQueue<>();

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day7");

    for (int i = 0; i < 26; i++) {
      graph.add(new ArrayList<>());
    }

    for (String line : lines) {
      String[] div = line.split(" ");
      int first = div[1].charAt(0) - 65;
      int second = div[7].charAt(0) - 65;
      visited[first] = true;
      graph.get(first).add(second);
      counts[second]++;
    }

    System.out.print("First Part ");
    solve();
    System.out.println();
  }

  static void solve() {

    for (int i = 0; i < 26; i++) {
      if (visited[i] && counts[i] == 0) {
        pq.add(i);
      }
      visited[i] = false;
    }

    while (!pq.isEmpty()) {

      int curr = pq.poll();
      System.out.print((char) (curr + 65));

      for (Integer i : graph.get(curr)) {
        counts[i]--;
        if (counts[i] == 0) {
          pq.add(i);
        }

      }
    }

  }

}

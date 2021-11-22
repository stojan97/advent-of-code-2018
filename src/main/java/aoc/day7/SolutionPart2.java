package aoc.day7;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import aoc.utils.FileReader;

public class SolutionPart2 {

  static List<List<Integer>> graph = new ArrayList<List<Integer>>(26);
  static boolean visited[] = new boolean[26];
  static int counts[] = new int[26];
  static Letter workers[] = new Letter[5];
  static PriorityQueue<Integer> pq = new PriorityQueue<>();
  static int time = -1;
  static List<Integer> result = new ArrayList<>();

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

    solve();
    System.out.println("Second Part " + time);
  }

  static void solve() {

    for (int i = 0; i < 26; i++) {
      if (visited[i] && counts[i] == 0) {
        pq.add(i);
      }
      visited[i] = false;
    }

    for (int i = 0; i < workers.length; i++) {
      workers[i] = new Letter(0, -1);
    }

    while (!pq.isEmpty() || avaliableWorkers()) {
      time++;
      for (int i = 0; i < workers.length; i++) {
        if (workers[i].rem == 0) {
          putElements(workers[i].id);
          Integer pulled = pq.poll();
          if (pulled == null) {
            workers[i].id = -1;
            continue;
          }

          workers[i].rem = pulled + 60;
          workers[i].id = pulled;
          checkNonWorking(i);
          continue;
        }
        workers[i].rem--;
        checkNonWorking(i);
      }
    }
  }

  static void putElements(int curr) {

    if (curr == -1) {
      return;
    }

    for (Integer i : graph.get(curr)) {
      counts[i]--;
      if (counts[i] == 0) {
        pq.add(i);
      }

    }
  }

  static boolean avaliableWorkers() {

    for (int i = 0; i < workers.length; i++) {
      if (workers[i].id != -1) {
        return true;
      }
    }

    return false;
  }

  static void fillNonWorking() {

    for (int i = 0; i < workers.length; i++) {
      if (workers[i].id == -1) {
        Integer pulled = pq.poll();
        if (pulled == null) {
          workers[i].id = -1;
          continue;
        }
        workers[i].rem = pulled + 60;
        workers[i].id = pulled;
      }
    }

  }

  static void checkNonWorking(int curr) {

    if (curr == workers.length - 1 && !pq.isEmpty()) {
      fillNonWorking();
    }
  }

  static class Letter {

    int rem, id;

    Letter(int rem, int id) {

      this.rem = rem;
      this.id = id;
    }
  }
}

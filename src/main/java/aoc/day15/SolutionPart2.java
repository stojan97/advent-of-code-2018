package aoc.day15;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.TreeSet;

import aoc.utils.FileReader;

public class SolutionPart2 {

  static int[] di = { -1, 0, 0, 1 };
  static int[] dj = { 0, -1, 1, 0 };
  static int ELVES_ATTACK_POWER = 4;
  static int round = 1, goblins = 0, elves = 0;
  static int initGoblins = 0, initElves = 0;
  static char[][] a, b;
  static TreeSet<Unit> units = new TreeSet<>(Comparator.comparingInt((Unit u) -> u.i).thenComparingInt(u -> u.j));
  static List<Unit> uList = new ArrayList<>();
  static List<Unit> initUnitList = new ArrayList<>();
  static Map<Unit, Integer> unitMap = new HashMap<>(), initUnitMap = new HashMap<>();

  public static void main(String[] args) throws IOException, CloneNotSupportedException {

    b = FileReader.readLinesAs2dMap("day15");

    int idx = 0;
    for (int i = 0; i < b.length; i++) {
      for (int j = 0; j < b[i].length; j++) {
        if (b[i][j] == 'G' || b[i][j] == 'E') {
          Unit unit = new Unit(i, j);
          unit.type = b[i][j];
          initUnitMap.put(unit, idx);
          initUnitList.add(unit);
          idx++;
          if (b[i][j] == 'G') {
            initGoblins++;
          } else {
            initElves++;
          }
        }
      }
    }

    reset();
    startGame();
    int sum = 0;
    for (Unit unit : uList) {
      if (unit.hp > 0) {
        sum += unit.hp;
      }
    }
    System.out.println("Part 2: " + (round - 1) * sum);
  }

  static void startGame() throws CloneNotSupportedException {

    while (goblins > 0 && elves == initElves) {

      boolean isTerminated = false;
      addAll();
      for (Unit u : units) {
        if (goblins <= 0) {
          isTerminated = true;
          break;
        }
        makeMove(u);
      }
      units.clear();
      if (isTerminated) {
        break;
      }
      round++;
    }

    if (initElves != elves) {
      round = 1;
      ELVES_ATTACK_POWER++;
      reset();
      startGame();
    }
  }

  static void reset() throws CloneNotSupportedException {

    a = new char[b.length][];
    for (int i = 0; i < b.length; i++) {
      a[i] = b[i].clone();
    }
    uList = cloneList(initUnitList);
    unitMap = new HashMap<>(initUnitMap);
    elves = initElves;
    goblins = initGoblins;
  }

  static Unit findSmallestTarget(Unit u) {

    Unit smallest = null;
    int smallestHp = Integer.MAX_VALUE;
    for (int k = 0; k < 4; k++) {
      int pi = u.i + di[k];
      int pj = u.j + dj[k];
      if (inRange(pi, pj) && isOpposite(pi, pj, u.type) && a[pi][pj] != '.') {
        int tempidx = unitMap.get(new Unit(pi, pj));
        Unit target = uList.get(tempidx);
        if (target.hp < smallestHp) {
          smallest = target;
          smallestHp = target.hp;
        }
      }
    }

    return smallest;
  }

  static Pair bfs(Unit u, char type) {

    Queue<Unit> queue = new LinkedList<>();
    Map<Unit, Integer> dist = new HashMap<>();
    queue.add(u);
    dist.put(u, 1);
    boolean finished = false;
    Unit targetUnit = null;

    while (!queue.isEmpty()) {
      Unit curr = queue.poll();
      int currDist = dist.get(curr);

      for (int k = 0; k < 4; k++) {
        int pi = curr.i + di[k];
        int pj = curr.j + dj[k];
        Unit neigh = new Unit(pi, pj);
        if (inRange(pi, pj) && isOpposite(pi, pj, type) && !dist.containsKey(neigh)) {
          if (!finished) {
            if (a[pi][pj] != '.') {
              finished = true;
              targetUnit = new Unit(pi, pj);
            }
            dist.put(neigh, currDist + 1);
            queue.add(neigh);
          }
        }
      }
    }

    if (targetUnit == null) {
      return null;
    }

    Pair shortest = new Pair(null, Integer.MAX_VALUE);
    for (int k = 0; k < 4; k++) {
      int pi = targetUnit.i + di[k];
      int pj = targetUnit.j + dj[k];
      Unit neigh = new Unit(pi, pj);
      Integer d = dist.get(neigh);
      if (inRange(pi, pj) && a[pi][pj] == '.' && d != null) {
        if (d < shortest.dist) {
          shortest = new Pair(neigh, d);
        }
      }
    }
    shortest.start = u;
    return shortest;
  }

  static void attackUnit(Unit target) {

    target.hp = (target.type == 'G') ? target.hp - ELVES_ATTACK_POWER : target.hp - 3;
    if (target.hp <= 0) {
      a[target.i][target.j] = '.';
      unitMap.remove(target);
      if (target.type == 'G') {
        goblins--;
      } else {
        elves--;
      }
    }
  }

  static void makeMove(Unit u) {

    if (u.hp <= 0) {
      return;
    }
    Unit target = findSmallestTarget(u);
    if (target != null) {
      attackUnit(target);
      return;
    }

    Pair min = new Pair(null, Integer.MAX_VALUE);

    for (int k = 0; k < 4; k++) {
      int pi = u.i + di[k];
      int pj = u.j + dj[k];
      if (inRange(pi, pj) && a[pi][pj] == '.') {
        Unit curr = new Unit(pi, pj);
        Pair shortest = bfs(curr, u.type);
        if (shortest != null && shortest.dist < min.dist) {
          min = shortest;
        } else if (shortest != null && shortest.dist == min.dist) {
          if (shortest.unit.equals(min.unit)) {
            continue;
          }
          if ((shortest.unit.i < min.unit.i) || (shortest.unit.i == min.unit.i && shortest.unit.j < min.unit.j)) {
            min = shortest;
          }
        }
      }
    }

    if (min.unit == null) {
      return;
    }
    a[u.i][u.j] = '.';
    a[min.start.i][min.start.j] = u.type;
    int index = unitMap.get(u);
    unitMap.remove(u);
    u.i = min.start.i;
    u.j = min.start.j;

    target = findSmallestTarget(u);
    if (target != null) {
      attackUnit(target);
    }
    uList.set(index, u);
    unitMap.put(u, index);
  }

  static void addAll() {

    for (Unit u : uList) {
      if (u.hp > 0) {
        units.add(u);
      }
    }
  }

  static boolean inRange(int i, int j) {

    return i >= 0 && i < a.length && j >= 0 && j < a[i].length;
  }

  static boolean isOpposite(int i, int j, char type) {

    return a[i][j] != '#' && type != a[i][j];
  }

  static List<Unit> cloneList(List<Unit> list) throws CloneNotSupportedException {

    List<Unit> clone = new ArrayList<Unit>(list.size());
    for (Unit item : list) {
      clone.add((Unit) item.clone());
    }
    return clone;
  }

  static class Unit implements Cloneable {

    int hp = 200;
    char type;
    int i, j;

    Unit(int i, int j) {

      this.i = i;
      this.j = j;
    }

    @Override
    public boolean equals(Object o) {

      Unit u = (Unit) o;
      return this.i == u.i && this.j == u.j;
    }

    @Override
    public int hashCode() {

      return Objects.hash(i, j);
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {

      Unit clone;
      clone = (Unit) super.clone();
      return clone;
    }
  }

  static class Pair {

    Unit unit, start;
    int dist;

    Pair(Unit unit, int dist) {

      this.unit = unit;
      this.dist = dist;
    }
  }
}

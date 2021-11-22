package aoc.day13;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  static char[][] a;
  static int part2y = -1, part2x = -1;
  static int part1y = -1, part1x = -1;
  static boolean isPart1 = true;

  static Set<Cart> carts = new HashSet<>();
  static List<Cart> cartsList = new ArrayList<>();

  public static void main(String[] args) throws IOException {

    a = FileReader.readLinesAs2dMap("day13");

    makeSet();
    solve();
    System.out.println("Part 1: " + part1x + "," + part1y);
    System.out.println("Part 2: " + part2x + "," + part2y);
  }

  static void makeSet() {

    for (int i = 0; i < a.length; i++) {
      for (int j = 0; j < a[i].length; j++) {
        if (a[i][j] == '^' || a[i][j] == 'v' || a[i][j] == '>' || a[i][j] == '<') {
          Cart curr = new Cart(i, j);
          curr.dir = a[i][j];
          cartsList.add(curr);
          carts.add(curr);
        }
      }
    }
  }

  static void solve() {

    while (count() != 1) {
      for (int i = 0; i < cartsList.size(); i++) {
        calcNextPos(i);
      }
    }
  }

  static void modifyPair(Cart cart, int index) {

    for (int i = 0; i < cartsList.size(); i++) {
      if (index != i && cartsList.get(i).equals(cart)) {
        cartsList.set(i, cart);
        return;
      }
    }
  }

  static int count() {

    int cc = 0, x, y;
    for (Cart c : cartsList) {
      if (!c.isCrash) {
        cc++;
        part2y = c.i;
        part2x = c.j;
      } else {
        if (isPart1) {
          part1y = c.i;
          part1x = c.j;
        }
      }
    }

    if (cc == cartsList.size() - 2) {
      isPart1 = false;
    }
    return cc;
  }

  static void calcNextPos(int index) {

    Cart c = cartsList.get(index);
    if (c.isCrash) {
      return;
    }
    carts.remove(c);

    if (c.dir == '>') {
      c.j++;

      if (a[c.i][c.j] == '+') {
        switch (c.cross) {
          case 0:
            c.dir = '^';
            break;
          case 1:
            c.dir = '>';
            break;
          case 2:
            c.dir = 'v';
            break;
        }
        c.cross = (c.cross + 1) % 3;

      } else if (a[c.i][c.j] == '/') {
        c.dir = '^';
      } else if (a[c.i][c.j] == '\\') {
        c.dir = 'v';
      } else {
        c.dir = '>';
      }

    } else if (c.dir == '<') {
      c.j--;

      if (a[c.i][c.j] == '+') {
        switch (c.cross) {
          case 0:
            c.dir = 'v';
            break;
          case 1:
            c.dir = '<';
            break;
          case 2:
            c.dir = '^';
            break;
        }
        c.cross = (c.cross + 1) % 3;
      } else if (a[c.i][c.j] == '/') {
        c.dir = 'v';
      } else if (a[c.i][c.j] == '\\') {
        c.dir = '^';
      } else {
        c.dir = '<';
      }

    } else if (c.dir == '^') {
      c.i--;

      if (a[c.i][c.j] == '+') {
        switch (c.cross) {
          case 0:
            c.dir = '<';
            break;
          case 1:
            c.dir = '^';
            break;
          case 2:
            c.dir = '>';
            break;
        }
        c.cross = (c.cross + 1) % 3;
      } else if (a[c.i][c.j] == '/') {
        c.dir = '>';
      } else if (a[c.i][c.j] == '\\') {
        c.dir = '<';
      } else {
        c.dir = '^';
      }
    } else if (c.dir == 'v') {
      c.i++;

      if (a[c.i][c.j] == '+') {
        switch (c.cross) {
          case 0:
            c.dir = '>';
            break;
          case 1:
            c.dir = 'v';
            break;
          case 2:
            c.dir = '<';
            break;
        }
        c.cross = (c.cross + 1) % 3;

      } else if (a[c.i][c.j] == '/') {
        c.dir = '<';
      } else if (a[c.i][c.j] == '\\') {
        c.dir = '>';
      } else {
        c.dir = 'v';
      }
    }

    int currsize = carts.size();
    carts.add(c);

    if (carts.size() == currsize) {
      c.isCrash = true;
      modifyPair(c, index);
      carts.remove(c);
    }
  }

  static class Cart {

    int i, j, cross = 0;
    char dir;
    boolean isCrash = false;

    Cart(int i, int j) {

      this.i = i;
      this.j = j;
    }

    @Override
    public boolean equals(Object o) {

      Cart c = (Cart) o;
      return this.i == c.i && this.j == c.j;
    }

    @Override
    public int hashCode() {

      return Objects.hash(i, j);
    }
  }

}

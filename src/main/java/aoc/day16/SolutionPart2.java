package aoc.day16;

import java.util.List;

import aoc.utils.FileReader;

public class SolutionPart2 {

  private static final int reg[] = { 0, 0, 0, 0 };

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day16");

    int countEmtpyLines = 0;

    for (int i = 0; i < lines.size(); i++) {
      if (countEmtpyLines >= 3) {
        String[] s = lines.get(i).split(" ");
        executeInstruction(Integer.parseInt(s[0]), Integer.parseInt(s[1]),
          Integer.parseInt(s[2]), Integer.parseInt(s[3]));
      } else {
        if (lines.get(i).isEmpty()) {
          countEmtpyLines++;
          continue;
        } else {
          countEmtpyLines = 0;
        }
      }
    }

    System.out.println("Part 2: " + reg[0]);

  }

  static void executeInstruction(int opcode, int a, int b, int c) {

    if (opcode == 0) {
      reg[c] = reg[a] | reg[b];
    } else if (opcode == 1) {
      reg[c] = a;
    } else if (opcode == 2) {
      reg[c] = reg[a] * reg[b];
    } else if (opcode == 3) {
      reg[c] = (reg[a] == b) ? 1 : 0;
    } else if (opcode == 4) {
      reg[c] = reg[a] & reg[b];
    } else if (opcode == 5) {
      reg[c] = reg[a] | b;
    } else if (opcode == 6) {
      reg[c] = reg[a] & b;
    } else if (opcode == 7) {
      reg[c] = (reg[a] > b) ? 1 : 0;
    } else if (opcode == 8) {
      reg[c] = reg[a] + reg[b];
    } else if (opcode == 9) {
      reg[c] = reg[a] * b;
    } else if (opcode == 10) {
      reg[c] = reg[a] + b;
    } else if (opcode == 11) {
      reg[c] = (reg[a] == reg[b]) ? 1 : 0;
    } else if (opcode == 12) {
      reg[c] = (a > reg[b]) ? 1 : 0;
    } else if (opcode == 13) {
      reg[c] = (a == reg[b]) ? 1 : 0;
    } else if (opcode == 14) {
      reg[c] = reg[a];
    } else if (opcode == 15) {
      reg[c] = (reg[a] > reg[b]) ? 1 : 0;
    }
  }
}

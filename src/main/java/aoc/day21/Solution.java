package aoc.day21;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import aoc.utils.FileReader;

public class Solution {

  static int ip = 0, boundRegister = -1, p1 = 0;
  static List<Instruction> instructions = new ArrayList<>();
  static HashSet<Long> registers = new HashSet<>();
  static long part1, part2;
  static long[] reg = { 0, 0, 0, 0, 0, 0 };

  public static void main(String[] args) {

    List<String> input = FileReader.readLines("day21");

    int idx = 0;
    for (String line : input) {
      String[] split = line.split(" ");
      if (idx == 0) {
        boundRegister = Integer.parseInt(split[1]);
        idx++;
        continue;
      }
      instructions.add(new Instruction(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2]),
        Integer.parseInt(split[3])));
      idx++;
    }
    // wait 19-20 seconds.
    long start = System.currentTimeMillis();
    executeProgram();
    long end = System.currentTimeMillis();
    System.out.println("Seconds executed: " + ((end - start) / 1000.0));
    System.out.println("Part 1: " + part1);
    System.out.println("Part 2: " + part2);
  }

  static void executeProgram() {

    while (ip >= 0 && ip < instructions.size()) {
      Instruction currentInstruction = instructions.get(ip);
      reg[boundRegister] = ip;
      // loop till the process starts repeating numbers.
      executeInstruction(currentInstruction);
      if (ip == 28) {
        int checkingRegister = currentInstruction.a == 0 ? currentInstruction.b : currentInstruction.a;

        if (registers.contains(reg[checkingRegister])) {
          break;
        }
        if (p1 == 0) {
          part1 = reg[checkingRegister];
        }
        registers.add(reg[checkingRegister]);
        part2 = reg[checkingRegister];
        p1++;
      }

      ip = (int) reg[boundRegister];
      ip++;
    }
  }

  static void executeInstruction(Instruction instruction) {

    String opcode = instruction.opcode;
    int a = instruction.a, b = instruction.b, c = instruction.c;

    switch (opcode) {
      case "borr":
        reg[c] = reg[a] | reg[b];
        break;
      case "seti":
        reg[c] = a;
        break;
      case "mulr":
        reg[c] = reg[a] * reg[b];
        break;
      case "eqri":
        reg[c] = (reg[a] == b) ? 1 : 0;
        break;
      case "banr":
        reg[c] = reg[a] & reg[b];
        break;
      case "bori":
        reg[c] = reg[a] | b;
        break;
      case "bani":
        reg[c] = reg[a] & b;
        break;
      case "gtri":
        reg[c] = (reg[a] > b) ? 1 : 0;
        break;
      case "addr":
        reg[c] = reg[a] + reg[b];
        break;
      case "muli":
        reg[c] = reg[a] * b;
        break;
      case "addi":
        reg[c] = reg[a] + b;
        break;
      case "eqrr":
        reg[c] = (reg[a] == reg[b]) ? 1 : 0;
        break;
      case "gtir":
        reg[c] = (a > reg[b]) ? 1 : 0;
        break;
      case "eqir":
        reg[c] = (a == reg[b]) ? 1 : 0;
        break;
      case "setr":
        reg[c] = reg[a];
        break;
      case "gtrr":
        reg[c] = (reg[a] > reg[b]) ? 1 : 0;
        break;
    }
  }

  static class Instruction {

    String opcode;
    int a, b, c;

    Instruction(String opcode, int a, int b, int c) {

      this.opcode = opcode;
      this.a = a;
      this.b = b;
      this.c = c;
    }
  }
}

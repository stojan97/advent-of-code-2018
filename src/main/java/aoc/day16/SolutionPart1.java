package aoc.day16;

import java.util.ArrayList;
import java.util.List;

import aoc.utils.FileReader;

public class SolutionPart1 {

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day16");

    int countEmtpyLines = 0;
    int res = 0;

    for (int i = 0; i < lines.size(); ) {
      if (lines.get(i).isEmpty()) {
        countEmtpyLines++;
        i++;
        if (countEmtpyLines >= 3) {
          break;
        }
        continue;
      }

      countEmtpyLines = 0;
      String before[] = lines.get(i).split(" ");
      String ins[] = lines.get(i + 1).split(" ");
      String after[] = lines.get(i + 2).split(" ");
      List<Integer> beforeList = new ArrayList<>(), afterList = new ArrayList<>(), insList = new ArrayList<>();
      beforeList.add(Integer.parseInt(before[1].split("\\D")[1]));
      for (String in : ins) {
        insList.add(Integer.parseInt(in));
      }
      afterList.add(Integer.parseInt(after[2].split("\\D")[1]));

      for (int j = 2; j < before.length; j++) {
        String z[] = before[j].split("\\D");
        beforeList.add(Integer.parseInt(z[0]));
        String u[] = after[j + 1].split("\\D");
        afterList.add(Integer.parseInt(u[0]));
      }

      int c = calcOpCodes(beforeList, insList, afterList);
      if (c >= 3) {
        res++;
      }
      i += 3;
    }

    System.out.println("Part 1: " + res);
  }

  static int calcOpCodes(List<Integer> before, List<Integer> ins, List<Integer> after) {

    int count = 0;
    int valueA = ins.get(1);
    int valueB = ins.get(2);
    int valueC = ins.get(3);
    int beforeRegisterA = before.get(valueA);
    int beforeRegisterB = before.get(valueB);
    int afterRegisterC = after.get(valueC);

    if (beforeRegisterA + beforeRegisterB == afterRegisterC) {
      count++;
    }
    if (beforeRegisterA + valueB == afterRegisterC) {
      count++;
    }
    if (beforeRegisterA * beforeRegisterB == afterRegisterC) {
      count++;
    }
    if (beforeRegisterA * valueB == afterRegisterC) {
      count++;
    }
    if ((beforeRegisterA & beforeRegisterB) == afterRegisterC) {
      count++;
    }
    if ((beforeRegisterA & valueB) == afterRegisterC) {
      count++;
    }
    if ((beforeRegisterA | valueB) == afterRegisterC) {
      count++;
    }
    if ((beforeRegisterA | beforeRegisterB) == afterRegisterC) {
      count++;
    }
    if (beforeRegisterA == afterRegisterC) {
      count++;
    }
    if (valueA == afterRegisterC) {
      count++;
    }
    if ((valueA > beforeRegisterB && afterRegisterC == 1) || (valueA <= beforeRegisterB && afterRegisterC == 0)) {
      count++;
    }
    if ((beforeRegisterA > valueB && afterRegisterC == 1) || (beforeRegisterA <= valueB && afterRegisterC == 0)) {
      count++;
    }
    if ((beforeRegisterA > beforeRegisterB && afterRegisterC == 1)
        || (beforeRegisterA <= beforeRegisterB && afterRegisterC == 0)) {
      count++;
    }
    if ((valueA == beforeRegisterB && afterRegisterC == 1) || (valueA != beforeRegisterB && afterRegisterC == 0)) {
      count++;
    }
    if ((beforeRegisterA == valueB && afterRegisterC == 1) || (beforeRegisterA != valueB && afterRegisterC == 0)) {
      count++;
    }
    if ((beforeRegisterA == beforeRegisterB && afterRegisterC == 1)
        || (beforeRegisterA != beforeRegisterB && afterRegisterC == 0)) {
      count++;
    }

    return count;
  }
}

package aoc.day4;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aoc.utils.FileReader;

public class Solution {

  static List<Record> records = new ArrayList<>();
  static Map<Integer, Guard> guards = new HashMap<>();
  static int globalMaxSleepingMinute = Integer.MIN_VALUE, globalMaxSleepingMinuteCounter = 0;
  static int globalGuardId = -1;

  public static void main(String[] args) {

    List<String> lines = FileReader.readLines("day4");

    for (String line : lines) {
      String[] div = line.trim().split("(\\s|\\[|-|]|:+)");

      int year = Integer.parseInt(div[1]);
      int month = Integer.parseInt(div[2]);
      int day = Integer.parseInt(div[3]);
      int hour = Integer.parseInt(div[4]);
      int minute = Integer.parseInt(div[5]);
      String type = div[7];
      int id = -1;
      if (type.equals("Guard")) {
        id = Integer.parseInt(div[8].substring(1));
      }

      LocalDateTime dateTime = LocalDateTime.of(year, month, day, hour, minute);

      Record record = new Record(dateTime, id, type);
      records.add(record);
    }

    records.sort(Comparator.comparing(r -> r.dateTime));

    int guardId = solveAndGetGuard();

    System.out.println("First part: " + guardId * guards.get(guardId).maxSleepingMinute);
    System.out.println("Second part: " + globalGuardId * globalMaxSleepingMinute);
  }

  static int solveAndGetGuard() {

    int maxId = -1, maxSleepingMinutes = Integer.MIN_VALUE, currentId = -1, currentMinutes = 0;

    for (Record record : records) {
      if (record.id != -1) {
        currentId = record.id;
        if (!guards.containsKey(currentId)) {
          guards.put(currentId, new Guard());
        }
        continue;
      }

      if (record.type.equals("falls")) {
        currentMinutes = record.dateTime.getMinute();
        continue;
      }

      if (record.type.equals("wakes")) {
        Guard g = guards.get(currentId);
        int wakeUpTime = record.dateTime.getMinute();
        for (int i = currentMinutes; i < wakeUpTime; i++) {
          g.sleepingMinutes[i]++;
          if (g.sleepingMinutes[i] > g.maxSleepingMinuteCounter) {
            g.maxSleepingMinute = i;
            g.maxSleepingMinuteCounter = g.sleepingMinutes[i];

            if (g.maxSleepingMinuteCounter > globalMaxSleepingMinuteCounter) {
              globalMaxSleepingMinuteCounter = g.maxSleepingMinuteCounter;
              globalMaxSleepingMinute = i;
              globalGuardId = currentId;
            }
          }

        }
        int diff = wakeUpTime - currentMinutes;
        g.totalSleepingMinutes += diff;
        guards.put(currentId, g);

        if (g.totalSleepingMinutes > maxSleepingMinutes) {
          maxId = currentId;
          maxSleepingMinutes = g.totalSleepingMinutes;
        }
      }

    }

    return maxId;
  }

  static class Record {

    LocalDateTime dateTime;
    int id;
    String type;

    Record(LocalDateTime dateTime, int id, String type) {

      this.dateTime = dateTime;
      this.id = id;
      this.type = type;
    }
  }

  static class Guard {

    int totalSleepingMinutes = 0;
    int[] sleepingMinutes = new int[60];
    int maxSleepingMinute = Integer.MIN_VALUE;
    int maxSleepingMinuteCounter = 0;
  }
}

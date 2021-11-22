package aoc.day24;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    List<String> input = FileReader.readLines("day24");
    List<List<Group>> groups = parseGroups(input);
    long partOne = simulateImmuneSystem(groups, 0);
    long partTwo = getUnitsForImmuneSystemWin(groups);

    System.out.println("Part 1: " + partOne);
    System.out.println("Part 2: " + partTwo);
  }

  private static long getUnitsForImmuneSystemWin(List<List<Group>> groups) {

    long left = 1;
    long right = 100_000_000;

    while (left <= right) {
      long mid = left + (right - left) / 2;
      long result = simulateImmuneSystem(groups, mid);
      if (left == right) {
        return result;
      }
      if (result == 0) {
        left = mid + 1;
      } else {
        right = mid;
      }
    }

    return -1;
  }

  private static long simulateImmuneSystem(List<List<Group>> input, long boostImmuneSystem) {

    List<List<Group>> groups = new ArrayList<>();

    for (List<Group> groupList : input) {
      List<Group> gList = new ArrayList<>();
      for (Group currentGroup : groupList) {
        gList.add(new Group(currentGroup));
      }
      groups.add(gList);
    }

    List<Group> immuneSystemGroups = groups.get(0);
    immuneSystemGroups.forEach(group -> group.boostAttack(boostImmuneSystem));
    List<Group> infectionGroups = groups.get(1);

    while (!immuneSystemGroups.isEmpty() && !infectionGroups.isEmpty()) {

      targetSelectionPhase(immuneSystemGroups, infectionGroups);
      targetSelectionPhase(infectionGroups, immuneSystemGroups);

      Long beforeAttackPhaseImmuneSystemUnits = getTotalUnits(immuneSystemGroups);
      Long beforeAttackPhaseInfectionUnits = getTotalUnits(infectionGroups);

      attackingPhase(immuneSystemGroups, infectionGroups);

      Long afterAttackPhaseImmuneSystemUnits = getTotalUnits(immuneSystemGroups);
      Long afterAttackPhaseInfectionUnits = getTotalUnits(infectionGroups);

      if (beforeAttackPhaseImmuneSystemUnits.equals(afterAttackPhaseImmuneSystemUnits) &&
          beforeAttackPhaseInfectionUnits.equals(afterAttackPhaseInfectionUnits)) {
        // no one wins
        return 0;
      }

      immuneSystemGroups = immuneSystemGroups
        .stream()
        .filter(Group::hasUnits)
        .collect(Collectors.toList());

      infectionGroups = infectionGroups
        .stream()
        .filter(Group::hasUnits)
        .collect(Collectors.toList());
    }

    List<Group> finalGroups = new ArrayList<>(immuneSystemGroups);
    if (boostImmuneSystem == 0) {
      finalGroups.addAll(infectionGroups);
    }

    return getTotalUnits(finalGroups);
  }

  private static Long getTotalUnits(List<Group> groups) {

    return groups
      .stream()
      .map(group -> group.units)
      .reduce(0L, Long::sum);
  }

  private static void targetSelectionPhase(List<Group> attackingGroups, List<Group> defendingGroups) {

    attackingGroups.sort(Solution::targetSelectionAttackingComp);

    for (Group attackingGroup : attackingGroups) {
      Group defendingGroup = defendingGroups
        .stream()
        .filter(dg -> !dg.isTaken && attackingGroup.getMostDamage(dg) != 0)
        .max(targetSelectionDefendingComp(attackingGroup))
        .orElse(null);

      attackingGroup.setDefendingGroup(defendingGroup);
    }
  }

  private static void attackingPhase(List<Group> immuneSystemGroups, List<Group> infectionGroups) {

    List<Group> combineGroups = new ArrayList<>(immuneSystemGroups);
    combineGroups.addAll(infectionGroups);
    combineGroups.sort(Comparator.comparing(group -> -group.initiative));
    combineGroups
      .stream()
      .filter(group -> group.defendingGroup != null)
      .forEach(Group::attack);
  }

  private static Comparator<Group> targetSelectionDefendingComp(Group attackingGroup) {

    return (g1, g2) -> {
      long mostDamageG1 = attackingGroup.getMostDamage(g1);
      long mostDamageG2 = attackingGroup.getMostDamage(g2);
      if (mostDamageG1 != mostDamageG2) {
        return Long.compare(mostDamageG1, mostDamageG2);
      }
      if (g1.getEffectivePower() != g2.getEffectivePower()) {
        return Long.compare(g1.getEffectivePower(), g2.getEffectivePower());
      }

      return Long.compare(g1.initiative, g2.initiative);
    };
  }

  private static int targetSelectionAttackingComp(Group g1, Group g2) {

    long g1EffectivePower = g1.getEffectivePower();
    long g2EffectivePower = g2.getEffectivePower();
    if (g1EffectivePower != g2EffectivePower) {
      return Long.compare(g2EffectivePower, g1EffectivePower);
    }
    return Long.compare(g2.initiative, g1.initiative);
  }

  private static List<List<Group>> parseGroups(List<String> input) {

    List<List<Group>> groups = new ArrayList<>();

    for (String s : input) {
      if (s.trim().isEmpty()) {
        continue;
      }
      if (s.contains("Immune System") || s.contains("Infection")) {
        groups.add(new ArrayList<>());
        continue;
      }
      String[] split = s.split("[^0-9]+");
      long units = Long.parseLong(split[0]);
      long hitPolongs = Long.parseLong((split[1]));
      long attackDamage = Long.parseLong((split[2]));
      long initiative = Long.parseLong((split[3]));

      Pattern pattern = Pattern.compile("\\w+(?=\\s+damage)");
      Matcher matcher = pattern.matcher(s);
      String attackType = "";
      if (matcher.find()) {
        attackType = matcher.group();
      }

      pattern = Pattern.compile("\\(([^)]+)\\)");
      matcher = pattern.matcher(s);
      String additionalString = "";
      if (matcher.find()) {
        additionalString = matcher.group();
      }
      Set<String> weaknesses = new HashSet<>();
      Set<String> immunities = new HashSet<>();

      if (!additionalString.isEmpty()) {
        String[] split1 = additionalString.split("\\(|\\)");
        String additionalInfo = split1[1];
        String[] variants = additionalInfo.split("; ");
        for (String variant : variants) {
          String replacement = "";
          if (variant.contains("weak")) {
            replacement = "weak to ";
          } else {
            replacement = "immune to ";
          }
          variant = variant.replaceAll(replacement, "");
          String[] info = variant.split(", ");
          if (replacement.equals("weak to ")) {
            Collections.addAll(weaknesses, info);
          } else {
            Collections.addAll(immunities, info);
          }
        }
      }

      Group currentGroup = new Group(units, hitPolongs, immunities, weaknesses, attackType, attackDamage, initiative);
      groups.get(groups.size() - 1).add(currentGroup);
    }

    return groups;
  }

  private static class Group {

    long units;
    long hitPoints;
    Set<String> immunities;
    Set<String> weaknesses;
    String attackType;
    long attackDamage;
    long initiative;
    boolean isTaken;
    Group defendingGroup;

    public Group(
      long units,
      long hitPoints,
      Set<String> immunities,
      Set<String> weaknesses,
      String attackType,
      long attackDamage,
      long initiative) {

      this.units = units;
      this.hitPoints = hitPoints;
      this.immunities = immunities;
      this.weaknesses = weaknesses;
      this.attackType = attackType;
      this.attackDamage = attackDamage;
      this.initiative = initiative;
    }

    public Group(Group other) {

      this.units = other.units;
      this.hitPoints = other.hitPoints;
      this.immunities = other.immunities;
      this.weaknesses = other.weaknesses;
      this.attackType = other.attackType;
      this.attackDamage = other.attackDamage;
      this.initiative = other.initiative;
    }

    public boolean hasUnits() {

      return units > 0;
    }

    public long getEffectivePower() {

      return units * attackDamage;
    }

    public void setDefendingGroup(Group defendingGroup) {

      if (defendingGroup == null) {
        return;
      }
      defendingGroup.isTaken = true;
      this.defendingGroup = defendingGroup;
    }

    public void attack() {

      defendingGroup.isTaken = false;
      long mostDamage = getMostDamage(defendingGroup);
      long killingUnits = mostDamage / defendingGroup.hitPoints;
      defendingGroup.units = Math.max(0, defendingGroup.units - killingUnits);
      defendingGroup = null;
    }

    public long getMostDamage(Group other) {

      long effectivePower = getEffectivePower();
      if (other.immunities.contains(attackType)) {
        return 0;
      }
      if (other.weaknesses.contains(attackType)) {
        effectivePower *= 2;
      }
      return effectivePower;
    }

    public void boostAttack(long boost) {

      this.attackDamage += boost;
    }
  }

}

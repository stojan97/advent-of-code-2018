package aoc.day20;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import aoc.utils.FileReader;

public class Solution {

  public static void main(String[] args) {

    String input = FileReader.readLinesAsString("day20");

    int[] solutionPart = solveBothParts(input);

    System.out.println("Part 1: " + solutionPart[0]);
    System.out.println("Part 2: " + solutionPart[1]);
  }

  public static int[] solveBothParts(String inputRegex) {
    // prepare map
    Map<Room, Room> seenRooms = new HashMap<>();
    Room startRoom = new Room(0, 0);
    seenRooms.put(startRoom, startRoom);
    constructMap(startRoom, inputRegex, seenRooms, 0);

    // solve both parts
    Set<Room> visited = new HashSet<>();
    Queue<Room> queue = new LinkedList<>();
    queue.add(startRoom);
    visited.add(startRoom);
    int result = Integer.MIN_VALUE;
    int roomsWithAtLeastThousandDist = 0;

    while (!queue.isEmpty()) {
      Room top = queue.poll();
      int currentDistance = top.distance;

      result = Math.max(result, currentDistance);
      roomsWithAtLeastThousandDist += currentDistance >= 1000 ? 1 : 0;

      putToQueue(visited, queue, top.left, currentDistance);
      putToQueue(visited, queue, top.right, currentDistance);
      putToQueue(visited, queue, top.up, currentDistance);
      putToQueue(visited, queue, top.down, currentDistance);
    }

    return new int[] { result, roomsWithAtLeastThousandDist };
  }

  private static void putToQueue(
    Set<Room> visited,
    Queue<Room> queue,
    Room next,
    int currentDistance) {

    if (next != null && !visited.contains(next)) {
      visited.add(next);
      next.distance = currentDistance + 1;
      queue.add(next);
    }
  }

  private static void constructMap(Room currentRoom, String inputRegex, Map<Room, Room> seenRooms, int index) {

    if (index == inputRegex.length() || inputRegex.charAt(index) == '$') {
      return;
    }

    if (inputRegex.charAt(index) == '^') {
      constructMap(currentRoom, inputRegex, seenRooms, index + 1);
      return;
    }

    if (inputRegex.charAt(index) == '(') {
      int closingPosition = getClosingIndex(inputRegex, index);
      String branch = inputRegex.substring(index + 1, closingPosition);
      String currentBranch = "";
      int openingBrackets = 0;
      for (int branchIndex = 0; branchIndex < branch.length(); branchIndex++) {
        char currentChar = branch.charAt(branchIndex);
        if (currentChar == '|' && openingBrackets == 0) {
          constructMap(currentRoom, currentBranch, seenRooms, 0);
          currentBranch = "";
        } else {
          openingBrackets += currentChar == '(' ? 1 : 0;
          openingBrackets += currentChar == ')' ? -1 : 0;
          currentBranch += currentChar;
        }
      }
      constructMap(currentRoom, currentBranch, seenRooms, 0);
      constructMap(currentRoom, inputRegex, seenRooms, closingPosition + 1);
      return;
    }

    Room next = null;

    if (inputRegex.charAt(index) == 'W') {
      if (currentRoom.left == null) {
        Room newRoom = new Room(currentRoom.x - 1, currentRoom.y);
        next = getRoom(seenRooms, newRoom);
        currentRoom.left = next;
      } else {
        next = currentRoom.left;
      }
    } else if (inputRegex.charAt(index) == 'E') {
      if (currentRoom.right == null) {
        Room newRoom = new Room(currentRoom.x + 1, currentRoom.y);
        next = getRoom(seenRooms, newRoom);
        currentRoom.right = next;
      } else {
        next = currentRoom.right;
      }
    } else if (inputRegex.charAt(index) == 'N') {
      if (currentRoom.up == null) {
        Room newRoom = new Room(currentRoom.x, currentRoom.y + 1);
        next = getRoom(seenRooms, newRoom);
        currentRoom.up = next;
      } else {
        next = currentRoom.up;
      }
    } else if (inputRegex.charAt(index) == 'S') {
      if (currentRoom.down == null) {
        Room newRoom = new Room(currentRoom.x, currentRoom.y - 1);
        next = getRoom(seenRooms, newRoom);
        currentRoom.down = next;
      } else {
        next = currentRoom.down;
      }
    }

    constructMap(next, inputRegex, seenRooms, index + 1);
  }

  private static int getClosingIndex(String inputRegex, int index) {

    int tmpIndex = index + 1;

    int openingBrackets = 0;

    while (true) {
      if (inputRegex.charAt(tmpIndex) == ')') {
        if (openingBrackets != 0) {
          openingBrackets--;
        } else {
          return tmpIndex;
        }
      }
      if (inputRegex.charAt(tmpIndex) == '(') {
        openingBrackets++;
      }

      tmpIndex++;
    }
  }

  private static Room getRoom(Map<Room, Room> seenRooms, Room newRoom) {

    if (seenRooms.containsKey(newRoom)) {
      return seenRooms.get(newRoom);
    }
    seenRooms.put(newRoom, newRoom);
    return newRoom;
  }

  private static class Room {

    int x;
    int y;
    Room left, right, up, down;
    int distance = 0;

    public Room(int x, int y) {

      this.x = x;
      this.y = y;
    }

    @Override
    public boolean equals(Object o) {

      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Room room = (Room) o;
      return x == room.x && y == room.y;
    }

    @Override
    public int hashCode() {

      return Objects.hash(x, y);
    }
  }

}

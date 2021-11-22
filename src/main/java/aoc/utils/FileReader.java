package aoc.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public final class FileReader {

  private FileReader() {

  }

  public static List<String> readLines(String day) {

    String path = String.format("aoc/%s/input", day);

    ClassLoader classLoader = FileReader.class.getClassLoader();
    File file = new File(Objects.requireNonNull(classLoader.getResource(path)).getFile());

    try {
      return Files.readAllLines(file.toPath());
    } catch (IOException e) {
      String msg = String.format("There was issue when reading the input file: %s", path);
      throw new RuntimeException(msg);
    }
  }

  public static char[][] readLinesAs2dMap(String day) {

    List<String> lines = readLines(day);
    return Converter.to2dMap(lines);
  }

  public static String readLinesAsString(String day) {
    List<String> lines = readLines(day);
    return String.join("", lines);
  }

}

package ocp.study.part09;

import java.util.stream.IntStream;

public class IsParallel {

  public static void main(String[] args) {

    System.out.println("Normal...");

    IntStream range = IntStream.rangeClosed(1, 10);
    System.out.println(range.isParallel());         // false
    range.forEach(System.out::println);

    System.out.println("Parallel...");

    IntStream range2 = IntStream.rangeClosed(1, 10);
    IntStream range2Parallel = range2.parallel();
    System.out.println(range2Parallel.isParallel()); // true
    range2Parallel.forEach(System.out::println);

  }

}

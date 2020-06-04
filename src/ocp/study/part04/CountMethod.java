package ocp.study.part04;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class CountMethod {

    public static void main(String[] args) {
        List<String> list = Arrays.asList("AA", "AB", "CC");
        Predicate<String> predicate = s -> s.startsWith("A");
        long l = list.stream().filter(predicate).count(); // returns a long
        System.out.println("Number of Matching Element:" + l);
    }
}

package ocp.study.part09;

import java.util.Arrays;
import java.util.List;

public class ParallelStreamExample {
    public static void main(String[] args) {
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd", "", "jkl");
        // get count of empty string
        long count = strings.parallelStream().filter(string -> string.isEmpty()).count();
        System.out.println(count + " empty Strings");
    }
}

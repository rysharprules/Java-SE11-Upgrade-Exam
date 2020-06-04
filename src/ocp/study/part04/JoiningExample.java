package ocp.study.part04;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JoiningExample {
    public static void main(String[] args) {
        Stream<String> s = Stream.of("a", "b", "c");

        String names = s
                .collect(Collectors.joining());

        System.out.println(names);


        List<String> list = Arrays.asList("Ram", "Shyam", "Shiv", "Mahesh");
        String result = list.stream().collect(Collectors.joining());
        System.out.println(result);
        result = list.stream().collect(Collectors.joining(","));
        System.out.println(result);
        result = list.stream().collect(Collectors.joining("-", "[", "]"));
        System.out.println(result);
    }
}

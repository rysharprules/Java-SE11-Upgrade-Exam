package ocp.study.part08.functional;

import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AveragingDoubleExample {
    public static void main(String[] args) {
        Stream<String> s = Stream.of("1", "2", "3");
        Stream<String> s1 = Stream.of("1", "2", "3");

        double o = s.collect(Collectors.averagingDouble(n -> Double.parseDouble(n)));
        double op = s1.collect(Collectors.averagingDouble(Double::parseDouble));

        System.out.println(o);
        System.out.println(op);
    }
}

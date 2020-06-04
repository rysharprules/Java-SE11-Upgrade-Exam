package ocp.study.part08.functional;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class StreamMathDemo {

    private static void objStreams() {
        List<String> strList = Arrays.asList("Abcs", "One", "fv", "-", "seven", "Tn");

        String result = strList.stream().max(Comparator.comparing(s -> s.length())).get();
        System.out.println("Result: " + result);

        String result2 = strList.stream().min(Comparator.comparing(s -> s.length())).get();
        System.out.println("Result: " + result2);

        long count = strList.stream().filter(s -> s.contains("n")).count();
        System.out.println("Result: " + count); // Num of elems passed to it
    }

    private static void primitiveStreams() {
        List<Integer> list = Arrays.asList(20, 2, 72, 991, 100, -11);

        IntStream is = list.stream().mapToInt(t -> t);
        System.out.println("Max = " + is.max());

        IntStream is2 = list.stream().mapToInt(t -> t);
        System.out.println("Min = " + is2.min());

        IntStream is3 = list.stream().mapToInt(t -> t);
        System.out.println("Sum = " + is3.sum());

        IntStream is4 = list.stream().mapToInt(t -> t);
        System.out.println("Avg = " + is4.average().getAsDouble());
    }

    public static void main(String[] args) {
        objStreams();
        primitiveStreams();
    }
}

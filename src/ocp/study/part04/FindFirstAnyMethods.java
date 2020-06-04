package ocp.study.part04;

import java.util.Arrays;
import java.util.List;

public class FindFirstAnyMethods {

    public static void main(String[] afdfw) {
        List<Integer> list = Arrays.asList(1, 10, 3, 7, 5);
        int a = list.stream().peek(num -> System.out.println("will filter " + num))
                .filter(x -> x > 5) // filter records that are > 5
                .findFirst() // find the first
                .get(); // retrieve
        System.out.println(a);

        int b = list.stream().peek(num -> System.out.println("will filter " + num))
                .filter(x -> x > 5) // filter records that are > 5
                .findAny() // find any
                .get(); // retrieve
        System.out.println(b);
    }
}

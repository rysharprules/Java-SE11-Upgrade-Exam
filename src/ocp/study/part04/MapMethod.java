package ocp.study.part04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MapMethod {

    public static void main(String args[]) {
        List<String> cities = Arrays.asList("London", "HongKong", "Paris", "NewYork");
        System.out.println("Original list : " + cities);
        System.out.println("list transformed using Java 8 :" + transform(cities));
        System.out.println("list transformed using loop before Java 8 : " + beforeJava8(cities));

        // You can even on the fly tranform Collection in Java using Map function
        // let's transform a List of integers to square each element
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9);
        List<Integer> squares = numbers.stream().map(i -> i * i).collect(Collectors.toList());
        System.out.println("original list of numbers : " + numbers);
        System.out.println("transformed list of integers using Map in Java 8 : " + squares);
    }

    public static List<String> beforeJava8(List<String> listOfString) {
        List<String> coll = new ArrayList<>();
        for (String str : listOfString) {
            coll.add(str.toUpperCase());
        }
        return coll;
    }

    public static List<String> transform(List<String> listOfString) {
        return listOfString.stream() // Convert list to Stream
                .map(String::toUpperCase) // Convert each element to upper case
                .collect(toList()); // Collect results into a new list
    }

}

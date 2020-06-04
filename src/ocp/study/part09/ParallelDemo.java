package ocp.study.part09;

import ocp.study.Person;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ParallelDemo {

    static void parallelAvgDemo(List<Person> persons) {
        double average = persons.parallelStream()
                .filter(p -> p.getGender() == Person.Sex.MALE)
                .mapToInt(p -> p.getAge())
                .average().getAsDouble();
        System.out.println("average=" + average);
    }

    static void parallelReduce(List<Person> persons) {
        // This is called a concurrent reduction.
        ConcurrentMap<Person.Sex, List<Person>> byGender =
                persons.parallelStream()
                        .collect(Collectors.groupingByConcurrent(Person::getGender));
        System.out.println("Parallel Reduction by Gender " + byGender);
    }

    static void ordering() {
        Integer[] intArray = {1, 2, 3, 4, 5, 6, 7, 8};
        List<Integer> listOfIntegers =
                new ArrayList<>(Arrays.asList(intArray));

        System.out.println("listOfIntegers:");
        listOfIntegers
                .stream()
                .forEach(e -> System.out.print(e + " "));
        System.out.println("");

        System.out.println("listOfIntegers sorted in reverse order:");
        Comparator<Integer> normal = Integer::compare;
        Comparator<Integer> reversed = normal.reversed();
        // Collections.sort(listOfIntegers, reversed);
        listOfIntegers
                .stream()
                .sorted(reversed)
                .forEach(e -> System.out.print(e + " "));
        System.out.println("");

        System.out.println("Parallel stream");
        listOfIntegers
                .parallelStream()
                .forEach(e -> System.out.print(e + " "));
        System.out.println("");

        System.out.println("Another parallel stream:");
        listOfIntegers
                .parallelStream()
                .forEach(e -> System.out.print(e + " "));
        System.out.println("");

        System.out.println("With forEachOrdered:");
        listOfIntegers
                .parallelStream()
                .forEachOrdered(e -> System.out.print(e + " "));
        System.out.println("");

        // Stateful operation
        System.out.println("Stateful operation:");
        List<Integer> list = new ArrayList<>();
        list.add(11);
        list.add(12);
        list.add(13);
        list.add(14);
        List<Integer> serialStorage = list;// Arrays.asList(1, 2, 3, 4, 5);
        serialStorage
                .stream()
                // Don't do this! It uses a stateful lambda expression.
                .map(e -> {
                    serialStorage.add(e);
                    return e;
                })    // Throws java.util.ConcurrentModificationException
                .forEachOrdered(e -> System.out.print(e + " "));
    }

    public static void main(String[] args) {
        List<Person> persons = new ArrayList<>();
        persons.add(new Person("Jane", 25, false));
        persons.add(new Person("Alice", 28, false));
        persons.add(new Person("Bob", 42, true));
        persons.add(new Person("Tina", 19, false));

        parallelAvgDemo(persons);
        parallelReduce(persons);

        ordering();
    }
}

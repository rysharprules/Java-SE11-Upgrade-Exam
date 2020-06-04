package ocp.study.part04;

import ocp.study.Employee;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SortedDemo {

    private static void sorted() {

        Stream<Employee> emps = Stream.of(new Employee("John"), new Employee("Alice"), new Employee("Bob"), new Employee("Janett"));

        // Employee has to implement Comparable, else Exception in thread "main" java.lang.ClassCastException: com.jcp.study.Employee cannot be cast to java.lang.Comparable
        List<Employee> sorted = emps.sorted().collect(Collectors.toList());
        System.out.println(sorted);
    }

    private static void sortedWithComp() {
        Stream<Employee> emps = Stream.of(new Employee("John"), new Employee("Alice"), new Employee("Bob"), new Employee("Janett"));

        Stream<Employee> employeeStream = emps;
        employeeStream.sorted(Comparator.<Employee>reverseOrder()).forEach(System.out::println);
        // List<Employee> sorted = employeeStream.collect(Collectors.toList());
        // System.out.println(sorted);

        Stream<Employee> emps2 = Stream.of(new Employee("John"), new Employee("Alice"), new Employee("Bob"), new Employee("Janett"));
        List<Employee> empl = emps2.sorted(Comparator.comparing(s -> s.getName().length())).collect(Collectors.toList());
        System.out.println(empl);

        Stream<Employee> emps3 = Stream.of(new Employee("Jorn"), new Employee("Alice"), new Employee("Bob"), new Employee("Jone"));
        Comparator<Employee> c1 = Comparator.comparing(e -> e.name.length());
        Comparator<Employee> c2 = (e1, e2) -> e1.name.compareTo(e2.name);

        List<Employee> sl = emps3
                .sorted(c1.thenComparing(c2))
                .collect(Collectors.toList());
        System.out.println(sl);
    }

    public static void main(String[] args) {
        sorted();
        sortedWithComp();
    }

}

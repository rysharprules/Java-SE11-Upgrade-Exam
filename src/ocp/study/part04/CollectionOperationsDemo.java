package ocp.study.part04;

import ocp.study.Employee;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public class CollectionOperationsDemo {

    private static void findFirst() {
        List<Employee> employees = Arrays.asList(new Employee("Jack"), new Employee("Jill"), new Employee("Jiane"));
        Stream<Employee> emps = employees.stream();

        Optional<Employee> result = emps.filter(s -> s.getName().contains("i")).findFirst();

        System.out.println("findFirst Result = " + result.get());
    }

    private static void findAny() {
        List<Employee> employees = Arrays.asList(new Employee("Jack"), new Employee("Jill"), new Employee("Jiane"));
        Stream<Employee> emps = employees.stream();

        Optional<Employee> result = emps.filter(s -> s.getName().contains("i")).findAny();

        System.out.println("findAny Result = " + result.get());
    }

    private static void allMatch() {
        List<Employee> employees = Arrays.asList(new Employee("Jack"), new Employee("Jill"), new Employee("Jiane"));
        Stream<Employee> emps = employees.stream();

        boolean result = emps.allMatch(s -> s.getName().startsWith("J"));

        System.out.println("allMatch Result = " + result);
    }

    private static void anyMatch() {
        List<Employee> employees = Arrays.asList(new Employee("Jack"), new Employee("Jill"), new Employee("Jiane"));
        Stream<Employee> emps = employees.stream();

        boolean result = emps.anyMatch(s -> s.getName().contains("i"));

        System.out.println("anyMatch Result = " + result);
    }

    private static void noneMatch() {
        List<Employee> employees = Arrays.asList(new Employee("Jack"), new Employee("Jill"), new Employee("Jiane"));
        Stream<Employee> emps = employees.stream();

        boolean result = emps.noneMatch(s -> s.getName().startsWith("K"));

        System.out.println("noneMatch Result = " + result);
    }

    public static void main(String[] args) {
        findFirst();
        findAny();
        anyMatch();
        allMatch();
        noneMatch();
    }
}

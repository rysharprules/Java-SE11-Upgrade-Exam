package ocp.study.part08.functional;

import ocp.study.Employee;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CollectionDemo {

    private static void mapDemo() {
        Stream<Employee> emps = Stream.of(new Employee("John"), new Employee("Alice"));
        Stream<String> names = emps.map(Employee::getName);
        List<String> staff = names.collect(Collectors.toList());
        System.out.println(staff);

        Stream<Employee> emps2 = Stream.of(new Employee("John"), new Employee("Alice"));
        Stream<String> names2 = emps2.map(Employee::getName);
        Stream<Integer> lengths = names2.map(s -> s.length());
        System.out.println("Lens=" + lengths.collect(Collectors.toList()));
    }

    private static void intStreamDemo() {
        Stream<Employee> emps = Stream.of(new Employee("John"), new Employee("Alice"), new Employee("Bob"), new Employee("Janett"));
        Stream<String> names = emps.map(Employee::getName);
        IntStream istream = names.mapToInt(s -> s.length());
        System.out.println("Max: " + istream.max().getAsInt());

        // stream has already been operated upon or closed
        // System.out.println("Total: " + istream.count());
    }

    public static void main(String[] args) {
        mapDemo();
        intStreamDemo();
    }
}

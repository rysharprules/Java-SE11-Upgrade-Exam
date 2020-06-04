package ocp.study.part04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamsDemo {

  private static void intermediateOperations() {
    System.out.println("\n*** Intermediate OPERATIONS ***");
    List<String> list = Arrays.asList("Java", "isa", "isa", "so", "great");
    list.stream().filter(s -> s.contains("a")).map(String::toLowerCase).forEach(System.out::println);

    // Note distinct is a INTERMEDIATE OP
    System.out.println("Number of distinct elements : " + list.stream().distinct().count());

    // NOTE Nothing gets printed for this line....unless u apply foreach to it.
    List<String> list3 = Arrays.asList("Bob", "Alice", "Jane");
    System.out.println("Only Intermediate op");
    Stream<String> strStream = list3.stream().peek(s -> System.out.print("Peek " + s + " -> "));

    System.out.println("\nIntermediate op with foreach");
    strStream.forEach(System.out::println);

    System.out.println("\nTerminal with collect");
    Stream<String> words = Stream.of("lower", "case", "text");
    List<String> list2 = words
        .peek(s -> System.out.println(s))
        .map(s -> s.toUpperCase())
        .collect(Collectors.toList());
    System.out.println(list2);

    List<Employee> emps = new ArrayList<>();
    Collection<Employee> empss = emps;
    emps.add(new Employee("John", 120000.0));
    emps.add(new Employee("Bob", 112000.0));
    emps.add(new Employee("Jack", 36000.0));
    emps.add(new Employee("Alice", 150000.0));
    Predicate<Employee> p = emp -> emp.salary > 100000.0;
    Consumer<Employee> c = emp -> System.out.println(emp);
    emps.stream().filter(p).forEach(c);

    // INTERMEDIATE OPS are delayed till TERMINAL OPS are satisfied.
    List<String> names = Arrays.asList("John", "Bob", "Jack", "Alice");
    Stream<String> stream = names.stream()
        .filter(s -> {
          System.out.println("filtering " + s);
          return s.length() == 4;
        })
        .map(s -> {
          System.out.println("uppercasing " + s);
          return s.toUpperCase();
        });
    System.out.println("\nStream was filtered and mapped...");
    String name = stream.findFirst().get();
    System.out.println(name);
    System.out.println("Note above the operation ends as soon as the terminal operation finishes finding first elem");
  }

  private static void terminalOperations() {
    System.out.println("\n*** TERMINAL OPERATIONS ***");
    List<String> list = Arrays.asList("Java", "isa", "isa", "so", "great");

    // COLLECT
    List<String> result = list.stream()
        .filter(s -> s.endsWith("a")).collect(Collectors.toList());
    System.out.println(result);

    // MIN- MAX
    List<Integer> list2 = Arrays.asList(20, 2, 72, 991, 100, -11);
    Optional<Integer> min = list2.stream().min(Integer::compareTo);
    Optional<Integer> max = list2.stream().max(Integer::compareTo);
    System.out.println("Min = " + min.get() + " Max = " + max.get());

    Optional<String> result2 = list.stream()
        .filter(item -> item.contains("t"))
        .findAny();
    System.out.println(result2.get());
  }

  static class Employee {
    public String name;
    public double salary;

    public Employee(String n, double s) {
      name = n;
      salary = s;
    }

    public String toString() {
      return name + " : " + salary;
    }
  }

  public static void main(String[] args) {
    intermediateOperations();

    terminalOperations();
  }

}

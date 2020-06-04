package ocp.study.part04;

import ocp.study.Department;
import ocp.study.Employee;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CollectorsDemo {

    private static void joining() {
        Stream<Employee> emps = Stream.of(new Employee("John"), new Employee("Alice"), new Employee("Bob"), new Employee("Janett"));

        String joined = emps
                .map(Object::toString)
                .collect(Collectors.joining("; ", "e<", ">"));
        System.out.println(joined);
    }

    private static void groupBy() {
        System.out.println("\ngroupBy by Dept");
        Stream<Employee> emps = Stream.of(new Employee("John", "Sales"), new Employee("Bob", "Engineering"),
                new Employee("Alice", "Engineering"), new Employee("Janett", "Sales"));
        // Group employees by department
        Map<Department, List<Employee>> byDept
                = emps.collect(Collectors.groupingBy(Employee::getDepartment));
        for (Department d : byDept.keySet()) {
            System.out.println(d + " -> " + byDept.get(d));
        }

        // Group by Dept and then by name - Note the two groupingBy
        System.out.println("\ngroupBy by Dept and then Name");
        Stream<Employee> emps2 = Stream.of(new Employee("John", "Sales"), new Employee("Bob", "Engineering"),
                new Employee("Alice", "Engineering"), new Employee("Janett", "Sales"));
        Map<Department, Map<String, List<Employee>>> byDeptNames =
                emps2.collect(Collectors.groupingBy(Employee::getDepartment, Collectors.groupingBy(Employee::getName)));
        for (Department d : byDeptNames.keySet()) {
            System.out.println(d + " -> " + byDeptNames.get(d));
        }
    }

    private static void partitionBy() {
        System.out.println("\npartitionBy");
        Stream<Employee> emps = Stream.of(new Employee("John", "Sales"), new Employee("Bob", "Engineering"),
                new Employee("Alice", "Engineering"), new Employee("Janett", "Sales"));

        Map<Boolean, List<Employee>> deptCat =
                emps.collect(Collectors.partitioningBy(e -> e.getDepartment().getDept().startsWith("E")));
        for (Boolean d : deptCat.keySet()) {
            System.out.println(d + " -> " + deptCat.get(d));
        }
    }

    public static void main(String[] args) {
        joining();
        groupBy();
        partitionBy();
    }
}

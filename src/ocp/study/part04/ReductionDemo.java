package ocp.study.part04;

import ocp.study.Person;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

public class ReductionDemo {

    static void reduce(List<Person> roster) {
        Integer totalAge = roster
                .stream()
                .mapToInt(Person::getAge)
                .sum();
        System.out.println("TotalAge: " + totalAge);

        Integer totalAgeReduce = roster
                .stream()
                .map(Person::getAge)
                .reduce(
                        0,
                        (a, b) -> a + b);
        System.out.println("TotalAge: " + totalAgeReduce);
    }

    static void collect(List<Person> roster) {
        Averager averageCollect = roster.stream()
                .map(Person::getAge)
                .collect(Averager::new, Averager::accept, Averager::combine);
        System.out.println("Average age of all members: " + averageCollect.average());
        // see https://docs.oracle.com/javase/tutorial/collections/streams/reduction.html
    }

    static void collectYoungOnes(List<Person> persons) {
        List<Person> list = persons.stream().filter(p -> p.getAge() <= 30).collect(Collectors.toList());
        System.out.println("Young ones: " + list);
    }

    static void collectGroups(List<Person> persons) {
        Map<Person.Sex, List<Person>> list = persons.stream()
                .collect(
                        Collectors.groupingBy(Person::getGender));
        System.out.println("By Gender: " + list);

        Map<Person.Sex, Integer> totalAgeByGender =
                persons
                        .stream()
                        .collect(
                                Collectors.groupingBy(
                                        Person::getGender,
                                        Collectors.reducing(
                                                0,
                                                Person::getAge,
                                                Integer::sum)));
        System.out.println("TotalAgeByGender: " + totalAgeByGender);

        Map<Person.Sex, Double> averageAgeByGender = persons
                .stream()
                .collect(
                        Collectors.groupingBy(
                                Person::getGender,
                                Collectors.averagingInt(Person::getAge)));
        System.out.println("AverageAgeByGender: " + averageAgeByGender);
    }

    static class Averager implements IntConsumer {
        private int total = 0;
        private int count = 0;

        public double average() {
            return count > 0 ? ((double) total) / count : 0;
        }

        public void accept(int i) {
            total += i;
            count++;
        }

        public void combine(Averager other) {
            total += other.total;
            count += other.count;
        }
    }

    public static void main(String[] args) {
        List<Person> persons = Arrays.asList(new Person("Jane", 25, false), new Person("Alice", 28, false),
                new Person("Bob", 42, true), new Person("Tina", 19, false));

        reduce(persons);
        collect(persons);
        collectYoungOnes(persons);
        collectGroups(persons);
    }
}

package ocp.study.part04;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class MinMaxMethods {

    public static void main(String[] args) {
        Person obama = new Person("Barack Obama", 53);
        Person bush2 = new Person("George Bush", 68);
        Person clinton = new Person("Bill Clinton", 68);
        Person bush1 = new Person("George HW Bush", 90);

        Person[] personArray = new Person[]{obama, bush2, clinton, bush1};
        List<Person> personList = Arrays.asList(personArray);

        // Find Oldest Person
        final Comparator<Person> comp = (p1, p2) -> Integer.compare(p1.getAge(), p2.getAge());
        Person oldest = personList.stream().max(comp).get();
        System.out.println("Oldest: " + oldest);

        // Find Youngest Person
        // -This time instead create the Comparator as the argument to the min()
        // method
        Person youngest = personList.stream().min((p1, p2) -> Integer.compare(p1.getAge(), p2.getAge())).get();
        System.out.println("Youngest: " + youngest);
    }
}

class Person {
    String name;
    int age;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public int getAge() {
        return age;
    }

    public String toString() {
        return name + " " + age;
    }
}

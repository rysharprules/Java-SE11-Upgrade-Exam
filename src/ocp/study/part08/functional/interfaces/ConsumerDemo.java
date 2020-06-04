package ocp.study.part08.functional.interfaces;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ConsumerDemo {

    static class Name {
        String name;

        Name(String nm) {
            name = nm;
        }

        void setName(String nm) {
            name = nm;
        }

        public String toString() {
            return name;
        }
    }

    public static void processList(List<Name> names, Consumer<Name> consumer) {
        // One way
        for (Name n : names) {
            consumer.accept(n);
        }
        // or using method references
        // names.forEach(consumer::accept);
    }

    public static void main(String[] args) {
        List<Name> list = Arrays.asList(new Name("a"), new Name("b"), new Name("c"), new Name("d"));
        System.out.println(list);
        Consumer<Name> capitalize = s -> s.setName(s.name.toUpperCase());

        processList(list, capitalize);
        System.out.println(list);
    }
}
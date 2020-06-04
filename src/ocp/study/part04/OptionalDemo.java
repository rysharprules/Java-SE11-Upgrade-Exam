package ocp.study.part04;

import java.util.Optional;

public class OptionalDemo {

    public static void main(String[] args) {
        Optional<String> opt1 = Optional.of("Hello");
        String s = opt1.get();

        System.out.println(opt1.isPresent() + " " + s);

        Optional<String> opt2 = Optional.empty();
        System.out.println("Value or default: " + opt2.orElse("Default"));

        // With Supplier
        System.out.println("Value or default: " + opt2.orElseGet(() -> "Some default"));

        // Print only if opt1 has a value, Takes in a Consumer
        opt1.ifPresent(System.out::println);

        // Throws NPE
        Optional<String> optNull = Optional.of(null);
    }
}

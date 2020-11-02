// package ocp.study.part04;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OptionalStreams {

    private static Supplier<Stream<Optional<String>>> supplier = () -> {
        return Stream.of(
            Optional.of("Java"),
            Optional.empty(), 
            Optional.of("is"),
            Optional.empty(), 
            Optional.of("good"));
    };

    public static void main(String[] args) {
        System.out.println(removeEmpties_java8());
        System.out.println(removeEmpties_java9());

        optionalMap();
        optionalFlatMap();
    }

    /*
     * In Java 8.0 you could remove empty optionals as follows:
     */
    private static List<String> removeEmpties_java8() {
        return supplier.get().flatMap(o -> o.isPresent() ? Stream.of(o.get()) : Stream.empty())
                .collect(Collectors.toList());
    }

    /*
     * In Java 9.0 and later you can remove empty optionals with shorter code using
     * flatMap():
     */
    private static List<String> removeEmpties_java9() {
        return supplier.get().flatMap(Optional::stream).collect(Collectors.toList());
    }

    /*
     * If the function returns the exact type we need:
     */
    private static void optionalMap() {
        Optional<String> s = Optional.of("Java");
        Optional<String> s1 = s.map(String::toUpperCase); // function returns String
        s1.ifPresent(System.out::println);
    }

    /*
     * If we have a function that returns an Optional then using map() would lead to
     * a nested structure of optionals, as the map() does an additional wrapping.
     * Use flatMap() to keep a flat structure:
     */
    private static void optionalFlatMap() {
        Optional<String> s = Optional.of("Java");
        Optional<String> s1 = s.flatMap(val -> Optional.of(val.toUpperCase())); // function returns Optional<String>
        s1.ifPresent(System.out::println);
    }
}

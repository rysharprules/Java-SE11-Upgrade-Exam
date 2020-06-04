package ocp.study.part08;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Supplier;

public class MethodReferences2 {
    public static void main(String args[]) {

        // reference to static method
        // Syntax:
        // Class::staticMethodName

        IntFunction<String> f1 = (i) -> String.valueOf(i);
        System.out.println(f1.apply(100));

        IntFunction<String> f11 = String::valueOf;
        System.out.println(f11.apply(100));

        // Reference to a constructor
        // Syntax:
        // ClassName::new

        Function<char[], String> f2 = (arr) -> new String(arr);
        System.out.println(f2.apply(new char[]{'H', 'i'}));

        Function<char[], String> f21 = String::new;
        System.out.println(f21.apply(new char[]{'H', 'i'}));

        // Reference to an instance method of an arbitrary object of a particular type
        // Syntax:
        // Class::instanceMethodName

        BiFunction<String, String, Boolean> f31 = (s1, s2) -> s1.equalsIgnoreCase(s2);
        System.out.println(f31.apply("Hello", "HELLO"));

        BiFunction<String, String, Boolean> f32 = String::equalsIgnoreCase;
        System.out.println(f32.apply("Hello", "HELLO"));

        // Reference to an instance method of a particular object
        // Syntax:
        // object::instanceMethodName

        Integer i = new Integer(1);
        Supplier<String> f41 = () -> i.toString();
        System.out.println(f41.get());

        Integer i1 = new Integer(1);
        Supplier<String> f42 = i1::toString;
        System.out.println(f42.get());
    }
}

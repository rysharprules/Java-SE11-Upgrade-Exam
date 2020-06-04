package ocp.study.part08.functional.interfaces;

import java.util.function.BiConsumer;

/*
 java.util.function
Interface BiConsumer<T,U>

Type Parameters:
T - the type of the first argument to the operation
U - the type of the second argument to the operation
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface BiConsumer<T,U>
Represents an operation that accepts two input arguments and returns no result. This is the two-arity specialization of Consumer. Unlike most other functional interfaces, BiConsumer is expected to operate via side-effects.
This is a functional interface whose functional method is accept(Object, Object).
 */

public class BiConsumerExample {

    public static void main(String[] args) {
        BiConsumer<String, String> biConsumer = (x, y) -> {
            System.out.println(x);
            System.out.println(y);
        };

        biConsumer.accept("java2s.com", "tutorials");

    }
}

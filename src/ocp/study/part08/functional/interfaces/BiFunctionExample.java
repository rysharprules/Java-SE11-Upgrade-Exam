package ocp.study.part08.functional.interfaces;

import java.util.function.BiFunction;

/*
 java.util.function
Interface BiFunction<T,U,R>

Type Parameters:
T - the type of the first argument to the function
U - the type of the second argument to the function
R - the type of the result of the function
All Known Subinterfaces:
BinaryOperator<T>
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface BiFunction<T,U,R>
Represents a function that accepts two arguments and produces a result. This is the two-arity specialization of Function.
This is a functional interface whose functional method is apply(Object, Object).
 */
public class BiFunctionExample {
    public static void main(String[] args) {
        BiFunction<String, String, String> bi = (x, y) -> {
            return x + y;
        };

        System.out.println(bi.apply("java2s.com", "tutorial"));
    }
}

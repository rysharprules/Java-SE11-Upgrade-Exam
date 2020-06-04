package ocp.study.part08.functional.interfaces;

import java.util.function.DoubleToLongFunction;

/*
 java.util.function
Interface DoubleToLongFunction

Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface DoubleToLongFunction
Represents a function that accepts a double-valued argument and produces a long-valued result. This is the double-to-long primitive specialization for Function.
This is a functional interface whose functional method is applyAsLong(double).
 */
public class DoubleToLongFunctionExample {
    public static void main(String[] args) {
        DoubleToLongFunction dl = (x) -> {
            return Long.MAX_VALUE - (long) x;
        };
        System.out.println(dl.applyAsLong(3.14));
    }
}

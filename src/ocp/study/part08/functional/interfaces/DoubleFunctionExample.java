package ocp.study.part08.functional.interfaces;

import java.util.function.DoubleFunction;

/*
 java.util.function
Interface DoubleFunction<R>

Type Parameters:
R - the type of the result of the function
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface DoubleFunction<R>
Represents a function that accepts a double-valued argument and produces a result. This is the double-consuming primitive specialization for Function.
This is a functional interface whose functional method is apply(double).
 */
public class DoubleFunctionExample {
    public static void main(String[] args) {
        DoubleFunction<String> df = (d) -> d + " is now a string";

        System.out.println(df.apply(0.5));

    }
}

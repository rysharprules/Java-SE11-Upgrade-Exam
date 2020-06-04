package ocp.study.part08.functional.interfaces;

import java.util.function.ToDoubleFunction;

/*
 java.util.function
Interface ToDoubleFunction<T>

Type Parameters:
T - the type of the input to the function
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface ToDoubleFunction<T>
Represents a function that produces a double-valued result. This is the double-producing primitive specialization for Function.
This is a functional interface whose functional method is applyAsDouble(Object).
 */
public class ToDoubleFunctionExample {
    public static void main(String[] args) {
        ToDoubleFunction<Integer> i = (x) -> Math.sin(x);

        System.out.println(i.applyAsDouble(Integer.MAX_VALUE));
    }
}

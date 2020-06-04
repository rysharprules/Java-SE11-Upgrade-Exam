package ocp.study.part08.functional.interfaces;

import java.util.function.DoubleUnaryOperator;

/*
 java.util.function
Interface DoubleUnaryOperator

Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface DoubleUnaryOperator
Represents an operation on a single double-valued operand that produces a double-valued result. This is the primitive type specialization of UnaryOperator for double.
This is a functional interface whose functional method is applyAsDouble(double).
 */
public class DoubleUnaryOperatorExample {
    public static void main(String[] args) {
        DoubleUnaryOperator dl = (x) -> x * x;
        System.out.println(dl.applyAsDouble(3.14));
    }
}

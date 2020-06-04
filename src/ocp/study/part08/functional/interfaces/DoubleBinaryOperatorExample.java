package ocp.study.part08.functional.interfaces;

import java.util.function.DoubleBinaryOperator;

/*
 java.util.function
Interface DoubleBinaryOperator

Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface DoubleBinaryOperator
Represents an operation upon two double-valued operands and producing a double-valued result. This is the primitive type specialization of BinaryOperator for double.
This is a functional interface whose functional method is applyAsDouble(double, double).
 */
public class DoubleBinaryOperatorExample {
    public static void main(String[] args) {
        DoubleBinaryOperator d = (x, y) -> x * y;
        System.out.println(d.applyAsDouble(0.23, 0.45));
    }
}

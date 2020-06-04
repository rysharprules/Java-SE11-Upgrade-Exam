package ocp.study.part08.functional.interfaces;

import java.util.function.BinaryOperator;

/*
 java.util.function
Interface BinaryOperator<T>

Type Parameters:
T - the type of the operands and result of the operator
All Superinterfaces:
BiFunction<T,T,T>
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface BinaryOperator<T>
extends BiFunction<T,T,T>
Represents an operation upon two operands of the same type, producing a result of the same type as the operands. This is a specialization of BiFunction for the case where the operands and the result are all of the same type.
This is a functional interface whose functional method is BiFunction.apply(Object, Object).
 */
public class BinaryOperatorExample {
    public static void main(String[] args) {
        BinaryOperator<Integer> adder = (n1, n2) -> n1 + n2;

        System.out.println(adder.apply(3, 4));
    }
}

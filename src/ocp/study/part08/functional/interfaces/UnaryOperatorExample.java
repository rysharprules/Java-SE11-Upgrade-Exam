package ocp.study.part08.functional.interfaces;

import java.util.function.UnaryOperator;

/*
 java.util.function
Interface UnaryOperator<T>

Type Parameters:
T - the type of the operand and result of the operator
All Superinterfaces:
Function<T,T>
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface UnaryOperator<T>
extends Function<T,T>
Represents an operation on a single operand that produces a result of the same type as its operand. This is a specialization of Function for the case where the operand and result are of the same type.
This is a functional interface whose functional method is Function.apply(Object).
 */

public class UnaryOperatorExample {

    //UnaryOperator represents an operation on a single operand that produces a result of the same type as its operand.
    //This is a specialization of Function for the case where the operand and result are of the same type.
    public static void main(String[] args) {
        UnaryOperator<String> i = String::toUpperCase;
        UnaryOperator<Integer> j = (x) -> x * x;
        System.out.println(i.apply("example"));
        System.out.println(j.apply(5));
    }

}

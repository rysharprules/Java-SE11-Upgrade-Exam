package ocp.study.part08.functional.interfaces;

import java.util.function.BiPredicate;

/*
 java.util.function
Interface BiPredicate<T,U>

Type Parameters:
T - the type of the first argument to the predicate
U - the type of the second argument the predicate
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface BiPredicate<T,U>
Represents a predicate (boolean-valued function) of two arguments. This is the two-arity specialization of Predicate.
This is a functional interface whose functional method is test(Object, Object).
 */
public class BiPredicateExample {
    public static void main(String[] args) {
        BiPredicate<Integer, Integer> bi = (x, y) -> x > y;
        System.out.println(bi.test(2, 3));
    }
}

package ocp.study.part08.functional.interfaces;

import java.util.function.Supplier;

/*
 java.util.function
Interface Supplier<T>

Type Parameters:
T - the type of results supplied by this supplier
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface Supplier<T>
Represents a supplier of results.
There is no requirement that a new or distinct result be returned each time the supplier is invoked.

This is a functional interface whose functional method is get().
 */

public class SupplierExample {

    public static void main(String[] args) {
        Supplier<String> i = () -> "Ryan";
        System.out.println(i.get());

        Supplier<Integer> o = () -> 123;
        System.out.println(o.get());

        Supplier<Object> p = Object::new;
        System.out.println(p.get());
    }

}

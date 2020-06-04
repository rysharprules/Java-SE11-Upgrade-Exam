package ocp.study.part08.functional.interfaces;

import java.util.function.Function;

/*
 java.util.function
Interface Function<T,R>

Type Parameters:
T - the type of the input to the function
R - the type of the result of the function
All Known Subinterfaces:
UnaryOperator<T>
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface Function<T,R>
Represents a function that accepts one argument and produces a result.
This is a functional interface whose functional method is apply(Object).
 */

public class FunctionExample {


    public static void main(String[] sargsd) {
        String ryan = "Ryan is ";
        FunctionExample funEx = new FunctionExample();
        System.out.println(funEx.modifyString(ryan, s -> s.concat("Awesome!")));
        System.out.println(funEx.modifyString(ryan, s -> s.concat("Awesome!")));
        System.out.println(funEx.modify("Awesome!", ryan::concat));
        System.out.println(funEx.returnInteger(123, i -> i * 4));
        System.out.println(funEx.returnInteger(321, i -> i * 4)); // infer T as Integer
        System.out.println(funEx.returnInteger(ryan, String::length)); // infer T as String
        System.out.println(funEx.modify(123.3, Math::rint));

        Function<String, String> func = t -> t += "Awessssomee";
        System.out.println(func.apply(ryan));
        Function<String, String> func2 = String::toUpperCase;
        System.out.println(func2.apply(func.apply(ryan)));
    }

    // use generics to make method more flexible (T=Type, R=result)
    public <T, R> R modify(T original, Function<T, R> func) {
        return func.apply(original);
    }

    // only for Strings
    public String modifyString(String original, Function<String, String> func) {
        return func.apply(original);
    }

    // only T is generic, R is always int
    public <T> int returnInteger(T original, Function<T, Integer> func) {
        return func.apply(original);
    }
}

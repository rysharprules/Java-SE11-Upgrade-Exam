package ocp.study.part08.functional.interfaces;

import java.util.function.Consumer;

/*
 java.util.function
Interface Consumer<T>

Type Parameters:
T - the type of the input to the operation
All Known Subinterfaces:
Stream.Builder<T>
Functional Interface:
This is a functional interface and can therefore be used as the assignment target for a lambda expression or method reference.

@FunctionalInterface
public interface Consumer<T>
Represents an operation that accepts a single input argument and returns no result. Unlike most other functional interfaces, Consumer is expected to operate via side-effects.
This is a functional interface whose functional method is accept(Object).
 */
public class ConsumerExample<T> {
    private T ryan;

    ConsumerExample(T ryan) {
        this.ryan = ryan;
    }

    public static void main(String args[]) {
        Consumer<String> c = x -> System.out.println(x.toLowerCase());
        c.accept("RYAN");
        c.accept("RYAN IS COOL");

        ConsumerExample<String> consEx = new ConsumerExample<>("RyRy");
        consEx.consumerFun(c);

        consEx.consumerFun(t -> { // t is an Object
            if (t instanceof String) {
                System.out.println("Stringy");
            }
        });

        Consumer<String> c2 = c.andThen(x -> System.out.println(x.concat(" IS DENCH")));
        c2.accept("MR BIG");
        c2.andThen(c).accept("SNOW");

        Consumer<String> c3 = (x) -> System.out.println(x.toLowerCase());
        c3.andThen(c3).accept("AGAIN");

        ConsumerExample<Integer> consEx2 = new ConsumerExample<>(123);
        consEx2.consumerInteger(45, x -> System.out.println(x * consEx2.ryan));
        consEx2.consumerInteger(consEx2.ryan, x -> System.out.println(x * 5));
        consEx2.consumerInteger(consEx2.ryan, x -> System.out.println(consEx2.ryan = 666));
    }

    public <Integer> void consumerInteger(Integer intt, Consumer<Integer> cons) {
        cons.accept(intt);
    }

    public <T> void consumerFun(Consumer<T> cons) {
        cons.accept((T) ryan);
    }
}

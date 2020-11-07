// package ocp.study.part00;

import java.util.stream.Stream;

public class IterateExample {

  public static void main(String... args) {
    // iterate​(T seed, UnaryOperator<T> f)
    Stream.iterate(new SavingsAccount(100), // seed
        a -> new SavingsAccount(a.getBalance() + 100)) // f
        .limit(5)
        .forEach(System.out::print); // [100][200][300][400][500]

    System.out.println();

    // iterate​(T seed, Predicate<? super T> hasNext, UnaryOperator<T> next)
    // useful when you don't know how many elements you'll need to limit to
    // this has some similarities with the for-loop
    Stream.iterate(new SavingsAccount(100), // seed
        a -> a.getBalance() <= 500, // hasNext
        a -> new SavingsAccount(a.getBalance() + 100)) // next
        .forEach(System.out::print); // [100][200][300][400][500]
  }

}

- [3.1 - Create and use methods in interfaces](#3-1)
- [3.2 - Define and write functional interfaces](#3-2)
- [Quiz](#q)

## <a name="3-1"></a>3.1 Create and use methods in interfaces

An interface is an abstract data type, similar to a class that defines a
list of public abstract methods that any class implementing the interface must provide. An
interface may also include constant public static final variables, default methods, and
static methods. The following is an example of an interface and a class that implements it:

````
public interface Fly {
    public int getWingSpan() throws Exception;
    public static final int MAX_SPEED = 100;
    public default void land() {
        System.out.println("Animal is landing");
    }
}
public static double calculateSpeed(float distance, double time) {
    return distance/time;
}

public class Eagle implements Fly {
    public int getWingSpan() {
        return 15;
    }
    public void land() {
        System.out.println("Eagle is diving fast");
    }
}
````

In this example, the first method of the interface, `getWingSpan()`, declares an exception
in the interface. Due to the rules of method overriding, this does not require the exception
to be declared in the overridden method in the `Eagle` class. The second declaration,
`MAX_SPEED`, is a constant static variable available anywhere within our application.
The next method, `land()`, is a default method that has been optionally overridden in
the `Eagle` class. Finally, the method `calculateSpeed()` is a static member and, like
`MAX_SPEED`, it is available without an instance of the interface.

An interface may extend another interface, and in doing so it inherits all of the abstract
methods. The following is an example of an interface that extends another interface:

````
public interface Walk {
    boolean isQuadruped();
    abstract double getMaxSpeed();
}
public interface Run extends Walk {
    public abstract boolean canHuntWhileRunning();
    abstract double getMaxSpeed();
}
public class Lion implements Run {
    public boolean isQuadruped() {
        return true;
    }
    public boolean canHuntWhileRunning() {
        return true;
    }
    public double getMaxSpeed() {
        return 100;
    }
}
````

In this example, the interface `Run` extends `Walk` and inherits all of the abstract methods
of the parent interface. Notice that modifiers used in the methods `isQuadruped()`,
`getMaxSpeed()`, and `canHuntWhileRunning()` are different between the class and
interface definitions, such as public and abstract. The compiler automatically adds
public to all interface methods and abstract to all non‐static and non‐default
methods, if the developer does not provide them. By contrast, the class implementing the
interface must provide the proper modifiers. For example, the code would not compile if
`getMaxSpeed()` was not marked public in the `Lion` class.

Since the `Lion` class implements `Run`, and `Run` extends `Walk`, the `Lion` class must provide
concrete implementations of all inherited abstract methods. As shown in this example
with `getMaxSpeed()`, interface method definitions may be duplicated in a child interface
without issue.

Remember that an interface cannot extend a class, nor can a class extend an interface.
For these reasons, none of the following definitions using our previous Walk interface and
`Lion` class will compile:

````
public interface Sleep extends Lion {} // DOES NOT COMPILE
public class Tiger extends Walk {} // DOES NOT COMPILE
````

In the first definition, the interface `Sleep` cannot extend `Lion`, since `Lion` is a class.
Likewise, the class `Tiger` cannot extend the interface Walk.

Interfaces also serve to provide limited support for multiple inheritance within the
Java language, as a class may implement multiple interfaces, such as in the following
example:

````
public interface Swim {
}
public interface Hop {
}
public class Frog implements Swim, Hop {
}
````

In this example, the `Frog` class implements both the `Swim` and `Hop` interfaces.
An instance of `Frog` may be passed to any method that accepts `Swim`, `Hop`, `Frog`, or
`java.lang.Object` as an input parameter. As shown in this example, you can also
construct interfaces that have neither methods nor class members, traditionally referred
to as marker interfaces. The `java.io.Serializable`
interface, which contains no methods, is an example of a marker interface.

There are numerous rules associated with implementing interfaces that you should know
quite well at this point. For example, interfaces cannot extend classes, nor can classes
extend interfaces. Interfaces may also not be marked final or instantiated directly. There
are additional rules for default methods, such as Java failing to compile if a class or
interface inherits two default methods with the same signature and doesn’t provide its
own implementation.

### Private methods in interfaces

Java SE 9 introduces the ability to implement `private` methods in interfaces:

````
public interface ExampleInterface {
    private void exampleMethod(int x) {
        System.out.println(x);
    }
}
````

This is the latest step in the evolution of Java interfaces. To fully understand why this is beneficial,
we'll need to re-examine the original purpose of interfaces and how they were written in Java SE 7 and earlier.

#### Java SE 7 Interfaces

Interfaces are Java's solution to safely facilitate multiple inheritance. Interfaces originally only contained `static`
variables and `abstract` methods. An example is the `Accessible` interface below. This interface is meant to
be implemented in classes for financial products where people access money through deposits and withdrawls.

````
public interface Accessible {
    public static final double OVERDRAFT_FEE = 25;

    public abstract double verifyDeposit(double amount, int pin);
    public abstract double verifyWithdraw(double amount, int pin);
}
````

`abstract` methods must be implemented later. If one class implements an interface, you'll write your implementation
logic inside that class. If many classes implement the same interface, you'll write your implementation logic many times.

What if most classes implement the exact same logic? Must you duplicate the same code in many places? Isn't code duplication bad?

#### Example: Implementing `abstract` methods

Notice the logic found in these methods:

````
public class BasicChecking implements Accessible {
    ...
    public double verifyDeposit(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
    }
    public double verifyWithdraw(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
        // Verify account balance won't go negative
    }
}
````

#### Example: Duplicated logic

The same logic is largely duplicated by other classes that implement `Accessible`.

````
public class RestrictedChecking implements Accessible {
    ...
    public double verifyDeposit(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
    }
    public double verifyWithdraw(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
        // Verify account balance won't go negative
        // Verify the withdrawl is under 20
    }
}
````

#### Java SE 8 Interfaces

In Java SE 8, you're allowed to implement special types of methods within interfaces: `static` methods and `default` methods.

`default` methods help minimize code duplication. They provide a single location to write and edit. They can
be overridden later if necessary. They're overridden with per-class precision.

Previously duplicated logic can be written once in `Accessible`:

````
public interface Accessible {
    ...
    public default double verifyDeposit(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
    }
    public default double verifyWithdraw(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
        // Verify account balance won't go negative
    }
}
````

You can override a `default` method and call the interface's implementation with `super`:

````
public class RestrictedChecking implements Accessible {
    ...
    public double verifyWithdraw(double amount, int pin) {
        // Call the interface's implementation
        Accessible.super.verifyWithdraw(amount, pin);

        // Verify the withdrawl is under 20
    }
}
````

#### What about problems in multiple inheritance?

![Figure 3.1](img/figure3-1.png)

#### Inheritance rules of `default` methods

1. A superclass method takes priority over an interface `default` method
![Figure 3.2](img/figure3-2.png)<br />
    - The superclass method may be concrete or abstract
    - Only consider the interface `default` if no method exists from the superclass
1. A subtype interface's `default` method takes priority over a super-type interface `default` method<br />
![Figure 3.3](img/figure3-3.png)
1. If there is a conflict, treat the `default` method as abstract
    - The concrete class must provide its own implementation. This may include a call to a specific interface's implementation

#### Interfaces don't replace abstract classes

- An interface doesn't let you store the state of an instance
- An `abstract` class may contain instance fields
- To avoid complications caused by multiple inheritance of state, a class cannot extend multiple `abstract` classes

#### What if `default` methods duplicate logic?

See `Verify the PIN` and `Verify amount is greater than 0` below.

````
public interface Accessible {
    ...
    public default double verifyDeposit(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
    }
    public default double verifyWithdraw(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
        // Verify account balance won't go negative
    }
}
````

One strategy is to put duplicated logic within its own default method:

````
public interface Accessible {
    ...
    public default double verifyDeposit(double amount, int pin) {
        verifyTransaction(amount, pin);
    }
    public default double verifyWithdraw(double amount, int pin) {
        verifyTransaction(amount, pin);
        // Verify account balance won't go negative
    }
    public default boolean verifyTransaction(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
    }
}
````

The problem with this approach is `default` methods must be `public`. They can be called from almost anywhere.
The returned values may not mean anything outside the context of the methods. It's dangerous if the method returns
information you don't want exposed. 

They can also be overridden at any time. The result of the calling method may not be predictable.

#### Introducing `private` methods in interface

- A better strategy is to make the method `private`
- `private` interface methods are more secure
    - They can't be called from elsewhere
    - They limit the risk of exposing sensitive information
- `private` interface methods lead to more predictable programs
    - They can't be overridden
    - They can't be called from a class which implements the interface
- `private` interface methods lead to more maintainable code
    - Common logic can be stored and edited in one place
- `private` interface methods don't lead to complications

````
public interface Accessible {
    ...
    public default double verifyDeposit(double amount, int pin) {
        verifyTransaction(amount, pin);
    }
    public default double verifyWithdraw(double amount, int pin) {
        verifyTransaction(amount, pin);
        // Verify account balance won't go negative
    }
    private boolean verifyTransaction(double amount, int pin) {
        // Verify the PIN
        // Verify amount is greater than 0
    }
}
````

#### Types of methods in interfaces

| Access modifier and method type | Supported? |
| --- | --- |
| `public abstract` | Yes |
| `private abstract` | Compiler error |
| `public default` | Yes |
| `private default` | Compiler error |
| `public static` | Yes |
| `private static` | Yes |
| `private` | Yes |

## <a name="3-2"></a>3.2 Define and write functional interfaces

Let’s take a look at an example of a functional interface and a class that implements it:

```
@FunctionalInterface
public interface Sprint {
    public void sprint(Animal animal);
}
public class Tiger implements Sprint {
    public void sprint(Animal animal) {
        System.out.println("Animal is sprinting fast! "+animal.toString());
    }
}
```

In this example, the Sprint class is a functional interface, because it contains exactly
one abstract method, and the Tiger class is a valid class that implements the interface.

Consider the following three interfaces. Assuming Sprint is our previously defined
functional interface, which ones would also be functional interfaces?

```
public interface Run extends Sprint {}
public interface SprintFaster extends Sprint {
    public void sprint(Animal animal);
}
public interface Skip extends Sprint {
    public default int getHopCount(Kangaroo kangaroo) {return 10;}
    public static void skip(int speed) {}
}
```

The answer? All three are valid functional interfaces! The first interface, `Run`, defines no
new methods, but since it extends `Sprint`, which defines a single abstract method, it is also
a functional interface. The second interface, `SprintFaster`, extends `Sprint` and defines
an abstract method, but this is an override of the parent `sprint()` method; therefore, the
resulting interface has only one abstract method, and it is considered a functional interface.
The third interface, `Skip`, extends `Sprint` and defines a static method and a default
method, each with an implementation. Since neither of these methods is abstract, the resulting
interface has only one abstract method and is a functional interface.

Now that you’ve seen some variations of valid functional interfaces, let’s look at some
invalid ones using our previous `Sprint` functional interface definition:

```
public interface Walk {}
public interface Dance extends Sprint {
    public void dance(Animal animal);
}
public interface Crawl {
    public void crawl();
    public int getCount();
}
```
           
Although all three of these interfaces will compile, none of them are considered functional interfaces. The Walk interface neither extends any functional interface classes
nor defines any methods, so it is not a functional interface. The `dance` method extends
 `Sprint`, which already includes a single abstract method, bringing the total to two abstract
 methods; therefore, `dance` is not a functional interface. Finally, the `crawl` method defines
 two abstract methods; therefore it cannot be a functional interface.
 
 In these examples, applying the `@FunctionalInterface` annotation to any of these
 interfaces would result in a compiler error, as would attempting to use them implicitly as
 functional interfaces in a lambda expression.

### Applying the `@FunctionalInterface` Annotation

While it is a good practice to mark a functional interface with the `@FunctionalInterface`
annotation for clarity, it is not required with functional programming. The Java compiler
implicitly assumes that any interface that contains exactly one abstract method is
a functional interface. Conversely, if a class marked with the `@FunctionalInterface`
annotation contains more than one abstract method, or no abstract methods at all, then
the compiler will detect this error and not compile.

One problem with not always marking your functional interfaces with this annotation is
that another developer may treat any interface you create that has only one method as
a functional interface. If you later modify the interface to have other abstract methods,
suddenly their code will break since it will no longer be a functional interface.

Therefore, it is recommend that you explicitly mark the interface with the
`@FunctionalInterface` annotation so that other developers know which interfaces they
can safely apply lambdas to without the possibility that they may stop being functional
interfaces down the road.

The exam writers aren’t likely to use this annotation, as they expect you to be able to
determine whether an interface is a functional interface on your own.

## <a name="q"></a>Quiz

1. What is true about code duplication?
    - Duplication makes your code longer. This is good because it makes colleagues believe you're really smart and capable of handling complex code
    - Duplication is good because it builds redundancy into the system
    - If you need to make an edit, you''ll have to search for all the occassions where the code is duplicated. This is tedious and inefficient (A)
    - Duplication is an elegant substitute for version control
1. If a `private` method is written in an interface, where can that method be called from?
    - From any other method within the interface (A)
    - From any class which implements the interface
    - From any class which shares the same package
    - From the main method in a separate test class
1. Given the code fragment:
    ````
    public interface i1 {
        private default void m1(){
            System.out.println("i");
        }
        abstract void m2();
        public default void m3(){}
        static default void m4(){}
        private static void m5(){}
    }
    ````
   What are the valid methods in the interface i1? (Choose three):
   - m3 (A)
   - m4
   - m1
   - m2 (A)
   - m5 (A)
1. The private methods in interfaces feature helps you:
   A) Improve the readability of the code
   B) Improve the security of the business logic implemented
   C) Avoid inheritance complications
   Select the correct answer:
   - only option B
   - all the listed features (A)
   - only options A and C
   - only option C
1. Given the following, what is the result?
    ````
    class C1 {
        public void m() { System.out.println
    ("C"); }
   }
   interface I1 {
        default void m(){ System.out.println
   ("I");}
   }
   public class App extends C1
   implements I1{
        public static void main(String[] args){
            I1 obj = new App();
            obj.m();
        }
   }
   ````
   - I
   - an error at I1.java
   - an error at App.java
   - C (A)
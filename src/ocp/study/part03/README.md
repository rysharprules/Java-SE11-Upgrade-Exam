- [3.1 - Create and use methods in interfaces](#3-1)
- [3.2 - Define and write functional interfaces](#3-2)

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
`getMaxSpeed()` was not marked public in the Lion class.

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

In the first definition, the interface `Sleep` cannot extend `Lion`, since Lion is a class.
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

In this example, the Frog class implements both the Swim and Hop interfaces.
An instance of Frog may be passed to any method that accepts Swim, Hop, Frog, or
java.lang.Object as an input parameter. As shown in this example, you can also
construct interfaces that have neither methods nor class members, traditionally referred
to as marker interfaces. The java.io.Serializable
interface, which contains no methods, is an example of a marker interface.

There are numerous rules associated with implementing interfaces that you should know
quite well at this point. For example, interfaces cannot extend classes, nor can classes
extend interfaces. Interfaces may also not be marked final or instantiated directly. There
are additional rules for default methods, such as Java failing to compile if a class or
interface inherits two default methods with the same signature and doesn’t provide its
own implementation.

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

The answer? All three are valid functional interfaces! The first interface, Run, defines no
new methods, but since it extends Sprint, which defines a single abstract method, it is also
a functional interface. The second interface, SprintFaster, extends Sprint and defines
an abstract method, but this is an override of the parent sprint() method; therefore, the
resulting interface has only one abstract method, and it is considered a functional interface.
The third interface, Skip, extends Sprint and defines a static method and a default
method, each with an implementation. Since neither of these methods is abstract, the resulting
interface has only one abstract method and is a functional interface.

Now that you’ve seen some variations of valid functional interfaces, let’s look at some
invalid ones using our previous Sprint functional interface definition:

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
nor defines any methods, so it is not a functional interface. The Dance method extends
 Sprint, which already includes a single abstract method, bringing the total to two abstract
 methods; therefore, Dance is not a functional interface. Finally, the Crawl method defines
 two abstract methods; therefore it cannot be a functional interface.
 
 In these examples, applying the `@FunctionalInterface` annotation to any of these
 interfaces would result in a compiler error, as would attempting to use them implicitly as
 functional interfaces in a lambda expression.

### Applying the @FunctionalInterface Annotation

While it is a good practice to mark a functional interface with the @FunctionalInterface
annotation for clarity, it is not required with functional programming. The Java compiler
implicitly assumes that any interface that contains exactly one abstract method is
a functional interface. Conversely, if a class marked with the @FunctionalInterface
annotation contains more than one abstract method, or no abstract methods at all, then
the compiler will detect this error and not compile.

One problem with not always marking your functional interfaces with this annotation is
that another developer may treat any interface you create that has only one method as
a functional interface. If you later modify the interface to have other abstract methods,
suddenly their code will break since it will no longer be a functional interface.

Therefore, it is recommend that you explicitly mark the interface with the
@FunctionalInterface annotation so that other developers know which interfaces they
can safely apply lambdas to without the possibility that they may stop being functional
interfaces down the road.

The exam writers aren’t likely to use this annotation, as they expect you to be able to
determine whether an interface is a functional interface on your own.


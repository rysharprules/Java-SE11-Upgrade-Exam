- [10.1 - Use `try-with-resources` construct](#101---use-try-with-resources-construct)
  - [`try-with-resources` Basics](#try-with-resources-basics)
  - [`AutoCloseable`](#autocloseable)
    - [Suppressed exceptions](#suppressed-exceptions)
    - [`AutoCloseable` vs. `Closeable`](#autocloseable-vs-closeable)
- [10.2 - Develop code that handles multiple `Exception` types in a single catch block](#102---develop-code-that-handles-multiple-exception-types-in-a-single-catch-block)
  - [Multi-`catch` Is effectively `final`](#multi-catch-is-effectively-final)
- [Quiz](#quiz)
- [Quiz answers](#quiz-answers)

# 10.1 - Use `try-with-resources` construct

The `try-with-resources` statement is a try statement that declares one or more resources.

A resource is as an object that must be closed after the program is finished with it. The `try-with-resources` statement ensures that each resource is closed at the end of the statement. 

Multi-`catch` allows you to write code without duplication. Another problem arises with duplication in `finally` blocks. It is important to close resources when you are finished with them.

Imagine that you want to write a simple method to read the first line of one file and write it to another file. Prior to Java 7, your code would look like the following. Pay attention to the `try`-`catch` statements.

````
10: public void oldApproach(Path path1, Path path2) throws IOException {
11:     BufferedReader in = null;
12:     BufferedWriter out = null;
13:     try {
14:         in = Files.newBufferedReader(path1);
15:         out = Files.newBufferedWriter(path2);
16:         out.write(in.readLine());
17:     } finally {
18:         if (in != null) in.close();
19:         if (out != null) out.close();
20:     }
21: }
````

That’s twelve lines of code to do something quite simple, and we don’t even deal with catching the exception. Switching to the `try-with-resources` syntax introduced in Java 7, it can be rewritten as follows:

````
30: public void newApproach(Path path1, Path path2) throws IOException {
31:     try (BufferedReader in = Files.newBufferedReader(path1);
32:             BufferedWriter out = Files.newBufferedWriter(path2)) {
33:         out.write(in.readLine());
34:     }
35: }
````

The new version has half as many lines! There is no longer code just to close resources. The new `try-with-resources` statement automatically closes all resources opened in the `try` clause. This feature is also known as automatic resource management, because Java automatically takes care of the closing.

## `try-with-resources` Basics

You might have noticed that there is no `finally` block in the `try-with-resources` code. A `try` statement must have one or more `catch` blocks or a `finally` block. This is still true. The `finally` clause exists implicitly. You just don’t have to type it.

The example below shows what a `try-with-resources` statement looks like. Notice that one or more resources can be opened in the `try` clause. Also, notice that parentheses are used to list those resources and semicolons are used to separate the declarations. This works just like declaring multiple indexes in a `for` loop.

The syntax of a basic `try-with-resources`

````
try (BufferedReader r = Files.newBufferedReader(path1); // Any resources that should automatically be closed within try
BufferedWriter w = Files.newBufferedWriter(path2)) {
// protected code
} // Resources are closed at this point
````

The example below shows that a `try-with-resources` statement is still allowed to have `catch` and/or `finally` blocks. They are run in addition to the implicit one. The implicit finally block runs before any programmer-coded ones.

````
try (BufferedReader r = Files.newBufferedReader(path1); // Any resources that should automatically be closed
        BufferedWriter w = Files.newBufferedWriter(path2)) {
    //protected code
} catch (IOException e) { // Optional clauses; resources still closed automatically
    // exeption handler
} finally { // Optional clauses; resources still closed automatically
    // finally block
}
````

To make sure that you’ve wrapped your head around the differences, make sure you can fill in Table 10.1 and Table 10.2 with whichever combinations of catch and `finally` blocks are legal configurations.

TABLE 10.1 - Legal vs. illegal configurations with a traditional `try` statement

|  | 0 finally blocks | 1 finally block | 2 or more finally blocks |
| --- | --- | --- | --- |
| 0 catch blocks | Not legal | Legal | Not legal |
| 1 or more catch blocks | Legal | Legal | Not legal |

TABLE 10.2 - Legal vs. illegal configurations with a `try-with-resources` statement

|  | 0 finally blocks | 1 finally block | 2 or more finally blocks |
| --- | --- | --- | --- |
| 0 catch blocks | Not legal | Legal | Not legal |
| 1 or more catch blocks | Legal | Legal | Not legal |

The resources created in the `try` clause are only in scope within the `try` block. This is another way to remember that the implicit finally runs before any `catch`/`finally` blocks that you code yourself. The implicit close has run already, and the resource is no longer available. Do you see why lines 6 and 8 don’t compile in this example?

````
3: try (Scanner s = new Scanner(System.in)) {
4:  s.nextLine();
5: } catch(Exception e) {
6:  s.nextInt(); // DOES NOT COMPILE
7: } finally{
8:  s.nextInt(); // DOES NOT COMPILE
9: }
````

The problem is that `Scanner` has gone out of scope at the end of the `try` clause. Lines 6 and 8 do not have access to it. This is actually a nice feature. You can’t accidentally use an object that has been closed. In a traditional `try` statement, the variable has to be declared before the `try` statement so that both the `try` and `finally` blocks can access it, which has the unpleasant side effect of making the variable in scope for the rest of the method, just inviting you to call it by accident.

### Improved `try-with-resources` statement

In Java 7 or Java 8 the `AutoCloseable` resources must be declared and initialized inside the try block of `try-with-resources` statement:

````
File file = new File("hello.txt");
try (BufferedReader br = new BufferedReader(new FileReader(file));) {
    String s = br.readLine();
    System.out.println(s);
} 
````
					
In Java 9 you are not required to declare new local variable and initialize it inside the `try` block. If you have a resource which is already declared and opened outside a `try-with-resources` statement, then you only can write the variable name pointing to resource inside `try` block and will be eligible for automatic resource management:

````
File file = new File("hello.txt");
BufferedReader br = new BufferedReader(new FileReader(file)); // must be final or effectively final
try (br) {
    String s = br.readLine();
    System.out.println(s);
}
````
					
The restriction is that the local variable which you refer inside `try` block must be _effectively final_ or explicitly declared with the `final` keyword.

Recall, an effectively `final` variable is a variable whose value is never changed after it is initialized.

In Java 10 you can use the `var` reserved type name for resource variable declaration:

````
File file = new File("hello.txt");
try (var br = new BufferedReader(new FileReader(file));) {
    String s = br.readLine();
    System.out.println(s);
}					
````

or:

````
File file = new File("hello.txt");
var br = new BufferedReader(new FileReader(file));
try (br) {
    String s = br.readLine();
    System.out.println(s);
}
````

## `AutoCloseable`

You can’t just put any random class in a `try-with-resources` statement. Java commits to closing automatically any resources opened in the `try` clause. Here we tell Java to try to close the `Turkey` class when we are finished with it:

````
public class Turkey {
    public static void main(String[] args) {
        try (Turkey t = new Turkey()) { // DOES NOT COMPILE
            System.out.println(t);
        }
    }
}
````

Java doesn’t allow this. It has no idea how to close a `Turkey`. Java informs us of this fact with a compiler error:

`The resource type Turkey does not implement java.lang.AutoCloseable`

In order for a class to be created in the `try` clause, Java requires it to implement an interface called `AutoCloseable`. Any object that implements `java.lang.AutoCloseable` can be used as a resource:

````
package java.lang;

public interface AutoCloseable {
    void close() throws Exception;
}
````

`TurkeyCage` does implement this interface:

````
1: public class TurkeyCage implements AutoCloseable {
2:      public void close() {
3:          System.out.println("Close gate");
4:      }
5:      public static void main(String[] args) {
6:          try (TurkeyCage t = new TurkeyCage()) {
7:              System.out.println("put turkeys in");
8:          }
9:      }
10: }
````

That’s much better. Line 1 declares that the class implements the `AutoCloseable` interface. This interface requires a `close()` method to be implemented, which is done on lines 2–4. Now, line 6 is allowed. Java does know how to close a `TurkeyCage` object. All Java has to do is to call the `close()` method.

The `AutoCloseable` interface has only one method to implement:

`public void close() throws Exception;`

Wait—`TurkeyCage` didn’t throw an `Exception`. That’s OK because an overriding method is allowed to declare more specific exceptions than the parent or even none at all. By declaring `Exception`, the `AutoCloseable` interface is saying that implementers may throw any exceptions they choose.

The following shows what happens when an exception is thrown. Do you see any problems with it?

````
public class StuckTurkeyCage implements AutoCloseable {
    public void close() throws Exception {
        throw new Exception("Cage door does not close");
    }
    public static void main(String[] args) {
        try (StuckTurkeyCage t = new StuckTurkeyCage()) { // DOES NOT COMPILE
            System.out.println("put turkeys in");
        }
    }
}
````

The `try-with-resources` statement throws a checked exception. And you know that checked exceptions need to be handled or declared. Tricky isn’t it? This is something that you need to watch for on the exam. If the `main()` method declared an `Exception`, this code would compile.

Java strongly recommends that `close()` not actually throw `Exception`. It is better to throw a more specific exception. Java also recommends to make the `close()` method idempotent. Idempotent means that the method can called be multiple times without any side effects or undesirable behavior on subsequent runs. For example, it shouldn’t throw an exception the second time or change state or the like. Both these negative practices are allowed. They are merely discouraged.

To better understand this, see which implementation you think is best:

````
class ExampleOne implements AutoCloseable {
    public void close() throws IllegalStateException {
        throw new IllegalStateException("Cage door does not close");
    }
}
class ExampleTwo implements AutoCloseable {
    public void close() throws Exception {
        throw new Exception("Cage door does not close");
    }
}
class ExampleThree implements AutoCloseable {
    static int COUNT = 0;
    public void close() {
        COUNT++;
    }
}
````

`ExampleOne` is the best implementation. `ExampleTwo` throws `Exception` rather than a more specific subclass, which is not recommended. `ExampleThree` has a side effect. It changes the state of a variable. Side effects are not recommended.

<img src="../img/note.png" alt="Note" width="20"/> _Note: the `close()` methods of resources are called in the OPPOSITE order of their creation. 

A `try-with-resources` statement CAN have `catch` and `finally` blocks just like an ordinary `try` statement. In a `try-with-resources` statement, any `catch` or `finally` block is run AFTER the resources declared have been closed._

Consider the following example:

````
public class AutoCloseableResource implements AutoCloseable {

    @Override
    public void close() {
        System.out.println("in close()");
        throw new RuntimeException("Exception in close()");
    }

    public void work() throws Exception {
        System.out.println("in work()");
        throw new Exception("Exception in work()");
    }
}
````

````				
public class AutoCloseableTest {

    public static void main(String[] args) {
        try (AutoCloseableResource resource = new AutoCloseableResource()) {
            resource.work();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
````
					
The `AutoCloseableResource` class implements `java.lang.AutoCloseable` and can thus be used as part of a `try-with-resources` statement, as illustrated in the `AutoCloseableTest.main(...)` method. Intentionally, we added some console output, and we throw exceptions both in the `work()` and `close()` methods of the class. Running the program yields the following output:

````
in work()
in close()
java.lang.Exception: Exception in work()
	at AutoCloseableResource.work(AutoCloseableResource.java:21)
	at AutoCloseableTest.main(AutoCloseableTest.java:15)
	Suppressed: java.lang.RuntimeException: Exception in close()
		at AutoCloseableResource.close(AutoCloseableResource.java:16)
		at AutoCloseableTest.main(AutoCloseableTest.java:16)
````

The output clearly proves that `close()` was indeed called BEFORE entering the `catch` block that should handle the exception.

### Suppressed exceptions

If both the (explicit) `try` block and the (implicit) resource handling code throw an exception, then the `try` block exception is the one which will be thrown. The resource handling exception will be made available via the `Throwable.getSupressed()` method of the thrown exception. The `Throwable[] Throwable.getSupressed()` was to the `Throwable` class in Java SE 7 specifically for this purpose. If there were no suppressed exceptions then this will return an empty array.

The Java 7 extensions to `java.lang.Throwable` are as follows:

- `public final void addSuppressed(Throwable exception)` - appends a suppressed exception to another one, so as to avoid exception masking.
- `public final Throwable[] getSuppressed()` - gets the suppressed exceptions that were added to an exception.

These extensions were introduced especially for supporting the `try-with-resources` statement and fixing exception-masking problems.

An exception can be thrown from the block of code associated with the `try-with-resources` statement. See the example `copyFile` below:

````
public static void copyFile(String src, String dest) throws IOException  {
    try (BufferedReader in = new BufferedReader(new FileReader(src));
         BufferedWriter out = new BufferedWriter(new FileWriter(dest))) {
        String line;
        while((line = in.readLine()) != null) {
            out.write(line);
            out.write('\n');
        }
    } // No need to close resources in a "finally"
}
````

An exception can be thrown from the `try` block, and up to two exceptions can be thrown from the `try-with-resources` statement when it tries to close the `BufferedReader` and `BufferedWriter` objects. If an exception is thrown from the `try` block and one or more exceptions are thrown from the `try-with-resources` statement, then those exceptions thrown from the `try-with-resources` statement are SUPPRESSED, and the exception thrown by the `try` block is the one that is thrown by the `copyFile` method. You can retrieve these suppressed exceptions by calling the `Throwable.getSuppressed` method from the exception thrown by the `try` block.

### `AutoCloseable` vs. `Closeable`

The `AutoCloseable` interface was introduced in Java 7. Before that, another interface existed called `Closeable`:

````
package java.io;

import java.io.IOException;

public interface Closeable extends AutoCloseable {
    public void close() throws IOException;
}
````

It was similar to what the language designers wanted, with the following exceptions:

- `Closeable` restricts the type of exception thrown to `IOException`.
- `Closeable` requires implementations to be idempotent.

The `java.io.Closeable` interface extends the `java.lang.AutoCloseable` interface. The `close()` method of the `Closeable` interface throws exceptions of type `IOException` while the `close()` method of the `AutoCloseable` interface throws exceptions of type `Exception`. Consequently, subclasses of the `AutoCloseable` interface can override this behavior of the `close()` method to throw specialized exceptions, such as `IOException`, or no exception at all.

The language designers emphasize backward compatibility. Since changing the existing interface was undesirable, they made a new one called `AutoCloseable`. This new interface is less strict than `Closeable`. Since `Closeable` meets the requirements for `AutoCloseable`, it started implementing `AutoCloseable` when the latter was introduced.

# 10.2 - Develop code that handles multiple `Exception` types in a single catch block

When something goes wrong in a program, it is common to log the error and convert it to a different exception type. In this example, we print the stack trace rather than write to a log. Next, we throw a runtime exception:

````
2: public static void main(String[] args) {
3:      try {
4:          Path path = Paths.get("dolphinsBorn.txt");
5:          String text = new String(Files.readAllBytes(path));
6:          LocalDate date = LocalDate.parse(text);
7:          System.out.println(date);
8:      } catch (DateTimeParseException e) {
9:          e.printStackTrace();
10:         throw new RuntimeException(e);
11:     } catch (IOException e) {
12:         e.printStackTrace();
13:         throw new RuntimeException(e);
14:     } }
````

Lines 4 and 5 read a text file into a `String`. It throws an `IOException` if the operation fails. Line 6 converts that `String` to a `LocalDate`. This throws a `DateTimeParseException` on failure. The two catch blocks on lines 8–14 print a stack trace and then wrap the exception in a `RuntimeException`.

This works. However, duplicating code is bad. Think about what happens if we decide that we want to change the code to write to a log file instead of printing the stack trace. We have to be sure to change the code in two places. Before Java 7, there were two approaches to deal with this problem. One was to catch `Exception` instead of the specific types:

````
public static void main(String[] args) {
    try {
        Path path = Paths.get("dolphinsBorn.txt");
        String text = new String(Files.readAllBytes(path));
        LocalDate date = LocalDate.parse(text);
        System.out.println(date);
    } catch (Exception e) { // BAD approach
        e.printStackTrace();
        throw new RuntimeException(e);
} }
````

The duplicate code is gone. However, this isn’t a good approach because it catches other exceptions too. For example, suppose that we had incorrect code that threw a `NullPointerException`. The catch block would catch it, which was never the intent. The other approach is to extract the duplicate code into a helper method:

````
public static void main(String[] args) {
    try {
        Path path = Paths.get("dolphinsBorn.txt");
        String text = new String(Files.readAllBytes(path));
        LocalDate date = LocalDate.parse(text);
        System.out.println(date);
    } catch (DateTimeParseException e) {
        handleException(e);
    } catch (IOException e) {
        handleException(e);
    }
}
private static void handleException(Exception e) {
    e.printStackTrace();
    throw new RuntimeException(e);
}
````

The duplicate code is mostly gone now. We still have a little duplication in that the code calls `handleException()` in two places. The code also is longer and a bit harder to read. 

The Java language designers recognized that this situation is an undesirable tradeoff. In Java 7, they introduced the ability to catch multiple exceptions in the same `catch` block, also known as multi-`catch`. Now we have an elegant solution to the problem:

````
public static void main(String[] args) {
    try {
        Path path = Paths.get("dolphinsBorn.txt");
        String text = new String(Files.readAllBytes(path));
        LocalDate date = LocalDate.parse(text);
        System.out.println(date);
    } catch (DateTimeParseException | IOException e) {
        e.printStackTrace();
        throw new RuntimeException(e);
} }
````

This is much better. There’s no duplicate code, the common logic is all in one place, and the logic is exactly where we would expect to find it.

The code example below shows the syntax of multi-`catch`. It’s like a regular `catch` clause, except two or more exception types are specified separated by a pipe. The pipe is also used as the “or” operator, making it easy to remember that you can use either/or of the exception types. Notice how there is only one variable name in the `catch` clause. Java is saying that the variable named `e` can be of type `Exception1` or `Exception2`.

````
try {
    //protected code
} catch(Exception1 | Exception2 e) { // Catch either of these exceptions with pipe
    //exception handler
}
````

The exam might try to trick you with invalid syntax. Remember that the exceptions can be listed in any order within the `catch` clause. However, the variable name must appear only once and at the end. Do you see why these are valid or invalid?

````
catch(Exception1 e | Exception2 e | Exception3 e) // DOES NOT COMPILE
catch(Exception1 e1 | Exception2 e2 | Exception3 e3) // DOES NOT COMPILE
catch(Exception1 | Exception2 | Exception3 e)
````

The first line is incorrect because the variable name appears three times. Just because it happens to be the same variable name doesn’t make it OK. The second line is incorrect because the variable name again appears three times. Using different variable names doesn’t make it any better. The third line does compile. It shows the correct syntax for specifying three exceptions.

Java intends multi-`catch` to be used for exceptions that aren’t related, and it prevents you from specifying redundant types in a multi-`catch`. Do you see what is wrong here?

````
try {
    throw new IOException();
} catch (FileNotFoundException | IOException e) { } // DOES NOT COMPILE
````

`FileNotFoundException` is a subclass of `IOException`. Specifying it in the multi-`catch` is redundant, and the compiler gives a message such as this:

The exception `FileNotFoundException` is already caught by the alternative `IOException`

Since we can omit that exception type without changing the behavior of the program, Java does not allow declaring it. The correct code is as follows:

````
try {
    throw new IOException();
} catch (IOException e) { }
````

To review multi-`catch`, see how many errors you can find in this try statement.

````
11: public void doesNotCompile() { // METHOD DOES NOT COMPILE
12:     try {
13:         mightThrow();
14:     } catch (FileNotFoundException | IllegalStateException e) {
15:     } catch (InputMismatchException e | MissingResourceException e) {
16:     } catch (SQLException | ArrayIndexOutOfBoundsException e) {
17:     } catch (FileNotFoundException | IllegalArgumentException e) {
18:     } catch (Exception e) {
19:     } catch (IOException e) {
20:     }
21: }
22: private void mightThrow() throws DateTimeParseException, IOException { }
````

This code is just swimming with errors. In fact, some errors hide others, so you might not see them all in the compiler. Once you start fixing some errors, you’ll see the others. Here’s what’s wrong:

- Line 15 has an extra variable name. Remember that there can be only one exception variable per `catch` block.
- Line 18 and 19 are reversed. The more general superclasses must be caught after their subclasses. While this doesn’t have anything to do with multi-`catch`, you’ll see “regular” `catch` block problems mixed in with multi-`catch`.
- Line 17 cannot catch `FileNotFoundException` because that exception was already caught on line 15. You can’t list the same exception type more than once in the same `try` statement, just like with “regular” `catch` blocks.
- Line 16 cannot catch `SQLException` because nothing in the `try` statement can potentially `throw` one. Again, just like “regular” `catch` blocks, any runtime exception may be caught. However, only checked exceptions that have the potential to be thrown are allowed to be caught.

Don’t worry—you won’t see this many problems in the same example on the exam!

## Multi-`catch` Is effectively `final`

This `try` statement is legal. It is a bad idea to reassign the variable in a `catch` block, but it is allowed:

````
try {
    // do some work
} catch(RuntimeException e) {
    e = new RuntimeException();
}
````

When adding multi-`catch`, this pattern is no longer allowed:

````
try {
    throw new IOException();
} catch(IOException | RuntimeException e) {
    e = new RuntimeException(); // DOES NOT COMPILE
}
````

With multi-`catch`, we no longer have a specific type of exception. Java uses the common `Exception` superclass for the variable internally. However, the intent isn’t really to have any old random exception in there. It wouldn’t make sense to shove an `IllegalStateException` in `e`. That would just make the code more complicated. Imagine that you wanted to rethrow the exception and it could be any old type. To avoid these problems and complexity, Java forbids reassigning the exception variable in a multi-`catch` situation.

This is scarcely a hardship given that it is bad practice to reassign the variable to begin with! Since Java is big on backward compatibility, this bad practice is still permitted when catching a single exception type.

# Quiz

1. <a name="q1"></a>Which of the following fills in the blank to make the code compile? (Choose all that apply)
    ````
   public static void main(String[] args) {
       try {
            throw new IOException();
       } catch (__________________________ ) { }
   }
   ````
   - A. `FileNotFoundException | IOException e`
   - B. `FileNotFoundException e | IOException e`
   - C. `FileNotFoundException | RuntimeException e`
   - D. `FileNotFoundException e | RuntimeException e`
   - E. `IOException | RuntimeException e`
   - F. `IOException e | RuntimeException e`
<br />[Jump to answer](#qa1)
2. <a name="q2"></a>Which of the following are true statements? (Choose all that apply.)
    - A. A traditional `try` statement without a `catch` block requires a `finally` block.
    - B. A traditional `try` statement without a `finally` block requires a `catch` block.
    - C. A traditional `try` statement with only one statement can omit the `{}`.
    - D. A try-with-resources statement without a `catch` block requires a `finally` block.
    - E. A try-with-resources statement without a `finally` block requires a `catch` block.
    - F. A try-with-resources statement with only one statement can omit the `{}`.
<br />[Jump to answer](#qa2)
3. <a name="q3"></a>Which of the following can legally fill in the blank? (Choose all that apply):
    ````
   public class AhChoo {
       static class SneezeException extends Exception { }
       static class SniffleException extends SneezeException { }
       public static void main(String[] args) throws SneezeException {
           try {
                throw new SneezeException();
           } catch (SneezeException | RuntimeException e) {
                _________________
                throw e;
   } } }
   ````
   - A. `// leave line blank`
   - B. `e = new Exception();`
   - C. `e = new RuntimeException();`
   - D. `e = new SneezeException();`
   - E. `e = new SniffleException();`
   - F. None of the above; the code does not compile.
<br />[Jump to answer](#qa3)
4. <a name="q4"></a>Fill in the blank: A class that implements _________________ may be in a 
try-with-resource statement. (Choose all that apply.)
    - A. `AutoCloseable`
    - B. `Closeable`
    - C. `Exception`
    - D. `RuntimeException`
    - E. `Serializable`
<br />[Jump to answer](#qa4)
5. <a name="q5"></a>Which of the following cannot fill in the blank? (Choose all that apply):
    ````
   public void read() throws SQLException {
       try {
           readFromDatabase();
       } catch (_________________ e) {
           throw e;
       }
   }
   private void readFromDatabase() throws SQLException { }
   ````
   - A. `Exception`
   - B. `RuntimeException`
   - C. `SQLException`
   - D. `SQLException | IOException`
   - E. `SQLException | RuntimeException`
<br />[Jump to answer](#qa5)
6. <a name="q6"></a>What is the output of the following code?
    ````
   import java.io.*;
   public class AutocloseableFlow {
       static class Door implements AutoCloseable {
           public void close() {
               System.out.print("D");
           } }
       static class Window implements Closeable {
           public void close() {
               System.out.print("W");
               throw new RuntimeException();
           } }
       public static void main(String[] args) {
           try (Door d = new Door(); Window w = new Window()) {
               System.out.print("T");
           } catch (Exception e) {
               System.out.print("E");
           } finally {
               System.out.print("F");
           } } }
   ````
   - A. `TWF`
   - B. `TWDF`
   - C. `TWDEF`
   - D. `TWF` followed by an exception
   - E. `TWDF` followed by an exception
   - F. `TWEF` followed by an exception
   - G. The code does not compile.
<br />[Jump to answer](#qa6)

# Quiz answers

1. <a name="qa1"></a>[Jump to question](#q1) - **E.** Options B, D, and F are incorrect because only one variable name is allowed in a multi-catch block. Option A is incorrect because `FileNotFoundException` is a subclass of `IOException`. A multi-catch statement does not allow redundancy, and just catching `IOException` would have been equivalent. Option C is incorrect because the `IOException` that is thrown is not handled.
2. <a name="qa2"></a>[Jump to question](#q2) - **A, B.** A try-with-resources statement does not require a catch or finally block. A traditional try statement requires at least one of the two.
3. <a name="qa3"></a>[Jump to question](#q3) - **A.** Since a multi-catch is used, the variable in the `catch` block is effectively final and cannot be reassigned.
4. 4. <a name="qa4"></a>[Jump to question](#q4) - **A, B.** `Closeable` was the original interface for IO classes. `AutoCloseable` was added in Java 7 along with try-with-resources. `Closeable` extends `AutoCloseable` for backward compatibility.
5. <a name="qa5"></a>[Jump to question](#q5) - **D.** Choice A is allowed because Java 7 and later “translates” `Exception` in a `catch` block to the correct one. Choices C and E are allowed because they actually `catch` a `SQLException`. Choice D is not allowed because there is no `IOException` declared. Choice B is allowed because a method does not have to handle an exception if it declares it.
6. <a name="qa6"></a>[Jump to question](#q6) - **C.** After opening both resources in the try-with-resources statement, `T` is printed. Then the try-with-resource completes and closes the resources in reverse order 
from which they were opened. After `W` is printed, an exception is thrown. However, the remaining resource is still closed and `D` is printed. The exception thrown is then caught and `E` is printed. Last, the `finally` block is run, printing `F`. Therefore the answer is `TWDEF`.
- [9.1 - Develop code that uses parallel streams](#91---develop-code-that-uses-parallel-streams)
  - [`parallel()`](#parallel)
  - [`parallelStream()`](#parallelstream)
  - [`isParallel`](#isparallel)
  - [Processing tasks in parallel](#processing-tasks-in-parallel)
    - [Understanding performance improvements](#understanding-performance-improvements)
    - [Ordered/Unordered streams](#orderedunordered-streams)
    - [Understanding independent operations](#understanding-independent-operations)
    - [Avoiding stateful operations](#avoiding-stateful-operations)
      - [Using concurrent collections with parallel streams](#using-concurrent-collections-with-parallel-streams)
- [9.2 - Implement decomposition and reduction with streams](#92---implement-decomposition-and-reduction-with-streams)
  - [Combining results with `reduce()`](#combining-results-with-reduce)
    - [Reduction operations](#reduction-operations)
      - [Requirements for `reduce()` arguments](#requirements-for-reduce-arguments)
  - [Combing results with `collect()`](#combing-results-with-collect)
    - [Using the one-argument `collect()` method](#using-the-one-argument-collect-method)
    - [Requirements for parallel reduction with `collect()`](#requirements-for-parallel-reduction-with-collect)
- [Quiz](#quiz)
- [Quiz answers](#quiz-answers)

# 9.1 - Develop code that uses parallel streams

Streams can be sequential or parallel. Operations on a sequential stream are processed in serial using one thread. Operations on a parallel stream are processed in parallel using multiple threads. You do not need to take additional steps to process streams because they are sequential or parallel. All you need to do is call the appropriate method that produces sequential or parallel stream. Everything else is taken care of by the Streams API

The Streams API was designed to make creating parallel streams quite easy. For the exam, you should be familiar with the two ways of creating a parallel stream.

## `parallel()`

The first way to create a parallel stream is from an existing stream. You just call `parallel()` on an existing stream to convert it to one that supports multi-threaded processing, as shown in the following code:

````
Stream<Integer> stream = Arrays.asList(1,2,3,4,5,6).stream();
Stream<Integer> parallelStream = stream.parallel();
````

Be aware that `parallel()` is an intermediate operation that operates on the original stream.

Conversely, use the `sequential()` method on a stream to convert a parallel stream into a sequential stream.

## `parallelStream()`

The second way to create a parallel stream is from a Java collection class. The `Collection` interface includes a method `parallelStream()` that can be called on any collection and returns a parallel stream. The following is a revised code snippet that creates the parallel stream directly from the `List` object:

````
Stream<Integer> parallelStream2 = Arrays.asList(1,2,3,4,5,6).parallelStream();
````

## `isParallel`

The Stream interface includes a method `isParallel()` that can be used to test if the instance of a stream supports parallel processing. Some operations on streams preserve the parallel attribute, while others do
not. For example, the `Stream.concat(Stream s1, Stream s2)` is parallel if either `s1` or `s2` is parallel. On the other hand, `flatMap()` creates a new stream that is not parallel by default, regardless of whether the underlying elements were parallel.

## Processing tasks in parallel

As you may have noticed, creating the parallel stream is the easy part. The interesting part comes in using it. Let’s take a look at a serial example:

````
Arrays.asList(1,2,3,4,5,6)
.stream()
.forEach(s -> System.out.print(s+" "));
````

What do you think this code will output when executed as part of a `main()` method?

Let’s take a look:

`1 2 3 4 5 6`

As you might expect, the results are ordered and predictable because we are using a serial stream. What happens if we use a parallel stream, though?

````
Arrays.asList(1,2,3,4,5,6)
.parallelStream()
.forEach(s -> System.out.print(s+" "));
````

With a parallel stream, the `forEach()` operation is applied across multiple elements of the stream concurrently. The following are each sample outputs of this code snippet:

````
4 1 6 5 2 3
5 2 1 3 6 4
1 2 6 4 5 3
````

As you can see, the results are no longer ordered or predictable.

### Understanding performance improvements

Let’s look at another example to see how much using a parallel stream may improve performance in your applications. Let’s say that you have a task that requires processing 4,000 records, with each record taking a modest 10 milliseconds to complete. The following is a sample implementation that uses `Thread.sleep()` to simulate processing the data:

````
import java.util.*;
public class WhaleDataCalculator {
    public int processRecord(int input) {
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // Handle interrupted exception
        }
        return input+1;
    }
    public void processAllData(List<Integer> data) {
        data.stream().map(a -> processRecord(a)).count();
    }
    public static void main(String[] args) {
        WhaleDataCalculator calculator = new WhaleDataCalculator();
        // Define the data
        List<Integer> data = new ArrayList<Integer>();
        for(int i=0; i<4000; i++) data.add(i);
        // Process the data
        long start = System.currentTimeMillis();
        calculator.processAllData(data);
        double time = (System.currentTimeMillis()—start)/1000.0;
        // Report results
        System.out.println("\nTasks completed in: "+time+" seconds");
    }
}
````

Given that there are 4,000 records, and each record takes 10 milliseconds to process, by using a serial `stream()`, the results will take approximately 40 seconds to complete this task. Each task is completed one at a time:

`Tasks completed in: 40.044 seconds`

If we use a parallel stream, though, the results can be processed concurrently:

````
public void processAllData(List<Integer> data) {
    data.parallelStream().map(a -> processRecord(a)).count();
}
````

Depending on the number of CPUs available in your environment, the following is a possible output of the code using a parallel stream:

`Tasks completed in: 10.542 seconds`

You see that using a parallel stream can have a four-fold improvement in the results. Even better, the results scale with the number of processors. Scaling is the property that, as we add more resources such as CPUs, the results gradually improve.

Does that mean that all of your streams should be parallel? Not exactly. Parallel streams tend to achieve the most improvement when the number of elements in the stream is significantly large. For small streams, the improvement is often limited, as there are some overhead costs to allocating and setting up the parallel processing.

### Ordered/Unordered streams

Besides being parallel or sequential, a stream also can be ordered or unordered.

Streams may or may not have a defined encounter order. Whether or not a stream has an encounter order depends on the source and the intermediate operations. Certain stream sources (such as `List` or arrays) are intrinsically ordered, whereas others (such as `HashSet`) are not. Some intermediate operations, such as `sorted()`, may impose an encounter order on an otherwise unordered stream, and others may render an ordered stream unordered, such as `BaseStream.unordered()`. Further, some terminal operations may ignore encounter order, such as `forEach()`.

If a stream is ordered, most operations are constrained to operate on the elements in their encounter order; if the source of a stream is a `List` containing `[1, 2, 3]`, then the result of executing `map(x -> x*2)` must be `[2, 4, 6]`. However, if the source has no defined encounter order, then any permutation of the values `[2, 4, 6]` would be a valid result.

For sequential streams, the presence or absence of an encounter order does not affect performance, only determinism. If a stream is ordered, repeated execution of identical stream pipelines on an identical source will produce an identical result; if it is not ordered, repeated execution might produce different results.

For parallel streams, relaxing the ordering constraint can sometimes enable more efficient execution. Certain aggregate operations, such as filtering duplicates (`distinct()`) or grouped reductions (`Collectors.groupingBy()`) can be implemented more efficiently if ordering of elements is not relevant. Similarly, operations that are intrinsically tied to encounter order, such as `limit()`, may require buffering to ensure proper ordering, undermining the benefit of parallelism. In cases where the stream has an encounter order, but the user does not particularly care about that encounter order, explicitly de-ordering the stream with `unordered()` may improve parallel performance for some stateful or terminal operations. However, most stream pipelines, such as the "sum of weight of blocks" example above, still parallelize efficiently even under ordering constraints.

### Understanding independent operations

Parallel streams can improve performance because they rely on the property that many stream operations can be executed independently. By independent operations, we mean that the results of an operation on one element of a stream do not require or impact the results of another element of the stream. For example, in the previous example, each call to `processRecord()` can be executed separately, without impacting any other invocation of the method.

As another example, consider the following lambda expression supplied to the `map()` method, which maps the stream contents to uppercase strings:

````
Arrays.asList("jackal","kangaroo","lemur")
    .parallelStream()
    .map(s -> s.toUpperCase())
    .forEach(System.out::println);
````

In this example, mapping `jackal` to `JACKAL` can be done independently of mapping `kangaroo` to `KANGAROO`. In other words, multiple elements of the stream can be processed at the same time and the results will not change.

Many common streams including `map()`, `forEach()`, and `filter()` can be processed independently, although order is never guaranteed. Consider the following modified version of our previous stream code:

````
Arrays.asList("jackal","kangaroo","lemur")
    .parallelStream()
    .map(s -> {System.out.println(s); return s.toUpperCase();})
    .forEach(System.out::println);
````

This example includes an embedded print statement in the lambda passed to the `map()` method. While the return values of the `map()` operation are the same, the order in which they are processed can result in very different output. We might even print terminal results before the intermediate operations have finished, as shown in the following generated output:

````
kangaroo
KANGAROO
lemur
jackal
JACKAL
LEMUR
````

When using streams, you should avoid any lambda expressions that can produce side effects.

### Avoiding stateful operations

Side effects can also appear in parallel streams if your lambda expressions are stateful. A *stateful lambda expression* is one whose result depends on any state that might change during the execution of a pipeline. On the other hand, a stateless lambda expression is one whose result does not depend on any state that might change during the execution of a pipeline.

Let’s take a look at an example to see why stateful lambda expressions should be avoided in parallel streams:

````
List<Integer> data = Collections.synchronizedList(new ArrayList<>());
Arrays.asList(1,2,3,4,5,6).parallelStream()
    .map(i -> {data.add(i); return i;}) // AVOID STATEFUL LAMBDA EXPRESSIONS!
    .forEachOrdered(i -> System.out.print(i+" "));
System.out.println();
for(Integer e: data) {
    System.out.print(e+" ");
}
````

The following is a sample generation of this code snippet using a parallel stream:

````
1 2 3 4 5 6
2 4 3 5 6 1
````

The `forEachOrdered()` method displays the numbers in the stream sequentially, whereas the order of the elements in the data list is completely random. You can see that a stateful lambda expression, which modifies the data list in parallel, produces unpredictable results at runtime. Note that this would not have been noticeable with a serial stream, where the results would have been the following:

````
1 2 3 4 5 6
1 2 3 4 5 6
````

It is strongly recommended that you avoid stateful operations when using parallel streams, so as to remove any potential data side effects. In fact, they should generally be avoided in serial streams wherever possible, since they prevent your streams from taking advantage of parallelization.

#### Using concurrent collections with parallel streams

We applied the parallel stream to a synchronized list in the previous example. Anytime you are working with a collection with a parallel stream, it is recommended that you use a concurrent collection. For example, if we had used a regular `ArrayList` rather than a synchronized one, we could have seen output such as the following:

````
1 2 3 4 5 6
null 2 4 5 6 1
````

For an `ArrayList` object, the JVM internally manages a primitive array of the same type. As the size of the dynamic `ArrayList` grows, a new, larger primitive array is periodically required. If two threads both trigger the array to be resized at the same time, a result can be lost, producing the unexpected value shown here. The unexpected result of two tasks executing at the same time is a race condition.

# 9.2 - Implement decomposition and reduction with streams

## Combining results with `reduce()`

The `reduce(T identity, BinaryOperator<T> accumulator)` method of the `Stream` interface performs a reduction operation on the stream to reduce the stream to a single value. It takes an initial value (the identity) and an accumulator that is a `BinaryOperator<T>` as arguments. The first time, the accumulator receives the initial value and the first element of the stream as arguments, and returns a value. The second time, the accumulator receives the value returned from its previous call and the second element from the stream. This process continues until all elements of the stream have been passed to the accumulator.

The returned value from the last call of the accumulator is returned from the `reduce(...)` method. The following snippet of code performs the summation of all integers in the stream:

````
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
Integer sum = numbers.stream().reduce(0, (n1, n2) -> n1 + n2); // must be 36
System.out.print("Sum of all integers in the stream : " + sum);
````
					
The `Integer` class contains a `static` `sum(...)` method to perform sum of two integers. You can rewrite the code using a method reference, like so:

`Integer sum = numbers.stream().reduce(0, Integer::sum);`

Recall that first parameter to the `reduce()` method is called the **identity**, the second parameter is called the **accumulator**, and the third parameter is called the **combiner**.

We can concatenate a string using the `reduce()` method to produce `wolf`, as shown in the following example:

````
System.out.println(Arrays.asList('w', 'o', 'l', 'f')
    .stream()
    .reduce("",(c,s1) -> c + s1,
        (s2,s3) -> s2 + s3));
````

On parallel streams, the `reduce()` method works by applying the reduction to pairs of elements within the stream to create intermediate values and then combining those intermediate values to produce a fi nal result. Whereas with a serial stream, `wolf` was built one character at a time, in a parallel stream, the intermediate strings `wo` and `lf` could have been created and then combined.

With parallel streams, though, we now have to be concerned about order. What if the elements of a string are combined in the wrong order to produce `wlfo` or `flwo`? The Streams API prevents this problem, while still allowing streams to be processed in parallel, as long as the arguments to the `reduce()` operation adhere to certain principles.

### Reduction operations

A reduction operation (also called a **fold**) takes a sequence of input elements and combines them into a single summary result by repeated application of a combining operation, such as finding the sum or maximum of a set of numbers, or accumulating elements into a list. The streams classes have multiple forms of general reduction operations, called `reduce()` and `collect()`, as well as multiple specialized reduction forms such as `sum()`, `max()`, or `count()`.

Of course, such operations can be readily implemented as simple sequential loops, as in:

````
int sum = 0;
for (int x : numbers) {
    sum += x;
}
````
					
However, there are good reasons to prefer a reduce operation over a mutative accumulation such as the above. Not only is a reduction "more abstract" -- it operates on the stream as a whole rather than individual elements -- but a properly constructed reduce operation is inherently parallelizable, so long as the function(s) used to process the elements are associative and stateless. For example, given a stream of numbers for which we want to find the sum, we can write:

`int sum = numbers.stream().reduce(0, (x, y) -> x + y);`
					
or:

`int sum = numbers.stream().reduce(0, Integer::sum);`
					
These reduction operations can run safely in parallel with almost no modification:

`int sum = numbers.parallelStream().reduce(0, Integer::sum);`
					
Reduction parallellizes well because the implementation can operate on subsets of the data in parallel, and then combine the intermediate results to get the final correct answer. (Even if the language had a "parallel for-each" construct, the mutative accumulation approach would still require the developer to provide thread-safe updates to the shared accumulating variable sum, and the required synchronization would then likely eliminate any performance gain from parallelism.) Using `reduce()` instead removes all of the burden of parallelizing the reduction operation, and the library can provide an efficient parallel implementation with no additional synchronization required.

In its more general form, a reduce operation on elements of type `<T>` yielding a result of type `<U>` requires three parameters:

````
 <U> U reduce(U identity,
     BiFunction<U, ? super T, U> accumulator,
     BinaryOperator<U> combiner);
````					
					
- The identity element is both an initial seed value for the reduction and a default result if there are no input elements.
- The accumulator function takes a partial result and the next element, and produces a new partial result.
- The combiner function combines two partial results to produce a new partial result. (The combiner is necessary in parallel reductions, where the input is partitioned, a partial accumulation computed for each partition, and then the partial results are combined to produce a final result.)

The code to sum all the integers in a stream can be rewritten as follows:

````
List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8);
Integer sum = numbers.stream().reduce(0, 
    (res, i) -> res + i,                  // adds one element's value to partial result
    (part1, part2) -> part1 + part2);     // combine two partial results
System.out.print("Sum of all integers in the stream : " + sum);
````

#### Requirements for `reduce()` arguments

- The identity must be defined such that for all elements in the stream `u`, `combiner.apply(identity, u)` is equal to `u`.
- The accumulator operator `op` must be associative and stateless such that `(a op b) op c` is equal to `a op (b op c)`.
- The combiner operator must also be associative and stateless and compatible with the identity, such that for all `u` and `t` `combiner.apply(u, accumulator.apply(identity,t))` is equal to `accumulator.apply(u, t)`.

If you follow these principles when building your `reduce()` arguments, then the operations can be performed using a parallel stream and the results will be ordered as they would be with a serial stream. Note that these principles still apply to the identity and accumulator when using the one- or two-argument version of `reduce()` on parallel streams.

Let’s take a look at an example using a non-associative accumulator. In particular, subtracting numbers is not an associative operation; therefore the following code can output different values depending on whether you use a serial or parallel stream:

````
System.out.println(Arrays.asList(1,2,3,4,5,6)
    .parallelStream()
    .reduce(0,(a,b) -> (a-b))); // NOT AN ASSOCIATIVE ACCUMULATOR
````

It may output `-21`, `3`, or some other value, as the accumulator function violates the associativity property.

As a more detailed explanation: If you run a serial Stream, you'll get the expected result of:

`0 - 1 - 2 - 3 - 4 - 5 - 6 = -21`

On the other hand, for parallel `Streams`, the work is split to multiple threads. For example, if reduce is executed in parallel on 6 threads, and then the intermediate results are combined, you can get a different result, e.g.:

````
(1 - 2) - 3 = -4
1 - (2 - 3) =  2
````

Therefore subtraction is not associative.

You can see other problems if we use an identity parameter that is not truly an identity value. For example, what do you expect the following code to output?

````
System.out.println(Arrays.asList("w","o","l","f")
    .parallelStream()
    .reduce("X",String::concat));
````

In fact, it can output `XwXoXlXf`. As part of the parallel process, the identity is applied to multiple elements in the stream, resulting in very unexpected data.

`a+b` doesn't cause the same problem, since addition is an associative operator (i.e. `(a+b)+c == a+(b+c)`).

The problem with the identity example is that when reduce is executed in parallel on multiple threads, `X` is appended to the starts of each intermediate result.

What exactly would be a proper identity to use then?

If you change the identity value to `""`:

````
System.out.println(Arrays.asList("w","o","l","f"))
    .parallelStream()
    .reduce("", String::concat));
````

You'll get `wolf` instead of `XwXoXlXf`.

## Combing results with `collect()`

Like `reduce()`, the Streams API includes a three-argument version of `collect()` that takes accumulator and combiner operators, along with a supplier operator instead of an identity. Also like `reduce()`, the accumulator and combiner operations must be associative and stateless, with the combiner operation compatible with the accumulator operator, as previously discussed. In this manner, the three-argument version of `collect()` can be performed as a parallel reduction, as shown in the following example:

````
Stream<String> stream = Stream.of("w", "o", "l", "f").parallel();
SortedSet<String> set = stream.collect(ConcurrentSkipListSet::new, Set::add,
    Set::addAll);
System.out.println(set); // [f, l, o, w]
````

Elements in a `ConcurrentSkipListSet` are sorted according to their natural ordering.

You should use a concurrent collection to combine the results, ensuring that the results of concurrent threads do not cause a `ConcurrentModificationException`.

### Using the one-argument `collect()` method

The one-argument version of `collect()` takes a collector argument, as shown in the following example:

````
Stream<String> stream = Stream.of("w", "o", "l", "f").parallel();
Set<String> set = stream.collect(Collectors.toSet());
System.out.println(set); // [f, w, l, o]
````

Performing parallel reductions with a collector requires additional considerations. For example, if the collection into which you are inserting is an ordered data set, such as a `List`, then the elements in the resulting collection must be in the same order, regardless of whether you use a serial or parallel stream. This may reduce performance, though, as some operations are unable to be completed in parallel.

The following rules ensure that a parallel reduction will be performed efficiently in Java using a collector.

### Requirements for parallel reduction with `collect()`

- The stream is parallel.
- The parameter of the collect operation has the `Collector.Characteristics.CONCURRENT` characteristic.
- Either the stream is unordered, or the collector has the characteristic `Collector.Characteristics.UNORDERED`.

Any class that implements the `Collector` interface includes a `characteristics()` method that returns a set of available attributes for the collector. While `Collectors.toSet()` does have the `UNORDERED` characteristic, it does not have the `CONCURRENT` characteristic; therefore the previous collector example will not be performed as a concurrent reduction.

The `Collectors` class includes two sets of methods for retrieving collectors that are both `UNORDERED` and `CONCURRENT`, `Collectors.toConcurrentMap()` and `Collectors.groupingByConcurrent()`, and therefore it is capable of performing parallel reductions efficiently. Like their non-concurrent counterparts, there are overloaded versions that take additional arguments.

````
Stream<String> ohMy = Stream.of("lions", "tigers", "bears").parallel();
ConcurrentMap<Integer, String> map = ohMy
    .collect(Collectors.toConcurrentMap(String::length, k -> k,
        (s1, s2) -> s1 + "," + s2));
System.out.println(map); // {5=lions,bears, 6=tigers}
System.out.println(map.getClass()); // java.util.concurrent.ConcurrentHashMap
````

We use a `ConcurrentMap` reference, although the actual class returned is likely `ConcurrentHashMap`. The particular class is not guaranteed; it will just be a class that implements the interface `ConcurrentMap`.

Finally, we can rewrite our `groupingBy()` example to use a parallel stream and parallel reduction:

````
Stream<String> ohMy = Stream.of("lions", "tigers", "bears").parallel();
ConcurrentMap<Integer, List<String>> map = ohMy.collect(
    Collectors.groupingByConcurrent(String::length));
System.out.println(map); // {5=[lions, bears], 6=[tigers]}
````

As before, the returned object can be assigned a `ConcurrentMap` reference.

# Quiz

1. <a name="q1"></a>Given an instance of a Stream, `s`, and a Collection, `c`, which are valid ways of creating a parallel stream? (Choose all that apply):
    - A. `new ParallelStream(s)`
    - B. `c.parallel()`
    - C. `s.parallelStream()`
    - D. `c.parallelStream()`
    - E. `new ParallelStream(c)`
    - F. `s.parallel()`
<br />[Jump to answer](#qa1)
2. <a name="q2"></a>What statements about the following code are true? (Choose all that apply):
    ````
   System.out.println(Arrays.asList("duck","chicken","flamingo","pelican")
        .parallelStream().parallel() // q1
        .reduce(0,
           (c1, c2) -> c1.length() + c2.length(), // q2
           (s1, s2) -> s1 + s2)); // q3
   ````
   - A. It compiles and runs without issue, outputting the total length of all strings in the stream.
   - B. The code will not compile because of line q1.
   - C. The code will not compile because of line q2.
   - D. The code will not compile because of line q3.
   - E. It compiles but throws an exception at runtime.
<br />[Jump to answer](#qa2)
3. <a name="q3"></a>What statements about the following code snippet are true? (Choose all that apply):
    ````
   4: Stream<String> cats = Stream.of("leopard","lynx","ocelot","puma").parallel();
   5: Stream<String> bears = Stream.of("panda","grizzly","polar").parallel();
   6: ConcurrentMap<Boolean, List<String>> data = Stream.of(cats,bears)
   7:   .flatMap(s -> s)
   8:   .collect(Collectors.groupingByConcurrent(s -> !s.startsWith("p")));
   9: System.out.println(data.get(false).size()+" "+data.get(true).size());
   ````
   - A. It outputs 3 4.
   - B. It outputs 4 3.
   - C. The code will not compile because of line 6.
   - D. The code will not compile because of line 7.
   - E. The code will not compile because of line 8.
   - F. It compiles but throws an exception at runtime.
   - G. The `collect()` operation is always executed in a single-threaded fashion.
<br />[Jump to answer](#qa3)
4. <a name="q4"></a>Which of the following properties of concurrency are true? (Choose all that apply):
    - A. By itself, concurrency does not guarantee which task will be completed first.
    - B. Concurrency always improves the performance of an application.
    - C. Computers with a single processor do not benefit from concurrency.
    - D. Applications with many resource-heavy tasks tend to benefit more from concurrency than ones with CPU-intensive tasks.
    - E. Concurrent tasks do not share the same memory.
<br />[Jump to answer](#qa4)

# Quiz answers

1. <a name="qa1"></a>[Jump to question](#q1) - **D, F.** There is no such class as ParallelStream, so A and E are incorrect. The method defined in the Stream class to create a parallel stream from an existing stream is `parallel();` therefore F is correct and C is incorrect. The method defined in the Collection class to create a parallel stream from a collection is `parallelStream();` therefore D is correct and B is incorrect.
2. <a name="qa2"></a>[Jump to question](#q2) - **C.** The code does not compile, so A and E are incorrect. The problem here is that `c1` is a `String` but `c2` is an `int`, so the code fails to combine on line q2, since calling `length()` on an `int` is not allowed, and C is correct. The rest of the lines compile without issue. Note that calling `parallel()` on an already parallel is allowed, and it may in fact return the same object.
3. <a name="qa3"></a>[Jump to question](#q3) - **A, G.** The code compiles and runs without issue, so C, D, E, and F are incorrect. The `collect()` operation groups the animals into those that do and do not start with the letter p. Note that there are four animals that do not start with the letter p and three animals that do. The negation operator `!` before the `startsWith()` method means that results are reversed, so the output is 3 4 and A is correct, making B incorrect. Finally, the stream created by `flatMap()` is a new stream that is not parallel by default, even though its elements are parallel streams. Therefore, the performance will be single-threaded and G is correct.
4. <a name="qa4"></a>[Jump to question](#q4) - **A, D.** By itself, concurrency does not guarantee which task will be completed first, so A is correct. Furthermore, applications with numerous resource requests will often be stuck waiting for a resource, which allows other tasks to run. Therefore, they tend to benefit more from concurrency than CPU-intensive tasks, so D is also correct. B is incorrect because concurrency may in fact make an application slower if it is truly single-threaded in nature. Keep in mind that there is a cost associated with allocating additional memory and CPU time to manage the concurrent process. C is incorrect because single-processor CPUs have been benefiting from concurrency for decades. Finally, E is incorrect; concurrent tasks share memory. 
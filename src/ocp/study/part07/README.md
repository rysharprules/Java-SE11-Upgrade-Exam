- [7.1 - Use local-variable type inference](#71---use-local-variable-type-inference)
  - [Reserved Type Name `var`](#reserved-type-name-var)
    - [Example of Benefits](#example-of-benefits)
    - [Legal type inferrence usage](#legal-type-inferrence-usage)
    - [Illegal type inferrence usage](#illegal-type-inferrence-usage)
      - [Why not?](#why-not)
    - [The debate](#the-debate)
- [7.2 - Create and use lambda expressions with local-variable type inferred parameters](#72---create-and-use-lambda-expressions-with-local-variable-type-inferred-parameters)
  - [Benefits](#benefits)
  - [These won't compile](#these-wont-compile)

# 7.1 - Use local-variable type inference

Previously, all local variable declarations required an explicit type on the left-hand side.

`ByteArrayOutputStream outputStream = new ByteArrayOutputStream();`

Now, the explicit type can be replaced by the reserved type name `var`. 

Local variable type inference is a feature in Java 10 that allows the developer to skip the type declaration associated with local variables (those defined inside method definitions, initialization blocks, `for`-loops, and other blocks like `if`-`else`), and the type is inferred by the JDK. It will, then, be the job of the compiler to figure out the datatype of the variable.

The compiler infers the variable type from the initializer on the right-hand side.

`var outputStream = new ByteArrayOutputStream();`

## Reserved Type Name `var`

Keywords cannot be used for variables names:

`int else = 10; // Not valid`

`var` is NOT a keyword. Instead, it is a **reserved type name**. This means that existing code that uses var as a variable, method, or package name WILL NOT be affected.

`int var = 20; // Valid, but not recommended. Old code like this won't break under Java 11`

`var` for type inference is only used when we know we're looking for type information

`var x = 30;`

Existing code that uses `var` as a class or interface name WILL be affected.

Despite the introduction of `var`, Java is still a statically typed language, and there should be enough information to infer the type of local variable. If not, the compiler will throw an error.

### Example of Benefits

Code to read a line of text from a socket using try-with-resources:

````
try (InputStream is = socket.getInputStream();
     InputStreamReader isr = new InputStreamReader(is, charsetName);
     BufferedReader buf = new BufferedReader(isr)) {
        return buf.readLine();
}
````

With local-variable type inference:

````
try (var is = socket.getInputStream();
     var isr = new InputStreamReader(is, charsetName);
     var buf = new BufferedReader(isr)) {
        return buf.readLine();
}
````

Variable names align (more readable) and class names aren't repeated (more concise).

### Legal type inferrence usage

- Local variables with an initial value: `var itemDescription ="Shirt"; // inferred as String` including within static/instance initialization blocks, as a local variable in a method, and as a value from another method (`var x = getId();`)
- As a resource variable in the try-with-resource block: `try (var input = new FileInputStream("file.txt")) { ...`
- Enhanced for-loop indexes: `for (var item : itemArray) // inferred as Item object`
- Traditional for-loop index variables `for (var i=0; i<10; i++) // inferred as int`
- Some non-denotable types: 
    - [Intersection types](https://itnext.io/java-generics-intersection-types-23b2fbdddfbb)
    - Anonymous class types
    
### Illegal type inferrence usage

- Declarations without an initial value/initialization: `var price;`
- Initialization with a null value: `var price = null;` (can be initialized to null with **explicit cast**: `var x = (Integer) null;  // compiles OK`)
- Compound declarations: `var price = 9.95, tax = 0.05;`
- Array initializers: `var prices = {9.95, 5, 3.50};`
- Class or instance fields: `static var i = 0;`, `public var price;`
- Parameters for methods: `public void setPrice(var price) {...}`
- Not allowed as lambda expression type (still needs an explicit target type): `var p = (String[] s) -> s.length > 0;`
- Not allowed with method reference (still needs an explicit target type): `var unaryOp = String::toLowerCase;`
- Rassign to a different (incompatible) type:
    ````
    public static void main(String[] args) {
        var x = 0;  // inferred to be of type int
        x = "Java"; // ERROR - String cannot be converted/assigned to int
    }
    ````
- Method return type:
    ````
    public var getPrice() {
        return price
    }
    ````

#### Why not?

Consider this bad example method:

````
public var getSomething(var something) {
    return something;
}
````

How should this compile? `something` could be anything. **Type inference** is an algorithm, not magic. Type inference refers to the automatic detection of the datatype of a variable, done at **compile time**. A goal of this feature is to let developers more-quickly read and understand code. Both humans and the compiler require context for understanding. 

Prevent "action at a distance" issues, i.e.

- Don't allow code elsewhere to conflict on what type to infer
    - This prevents binary incompatibilities
- Type must be inferred where the variable is declared

### The debate

Arguments against this feature:

- "Useful information will be hidden"
- "Readability will be impaired"
- "Bad developers will misuse and overuse the feature to write terrible code"

Argument for this feature:

- "Redundant information is removed"
- "Code is more concise and readable"
- "Bad developers will write terrible code no matter what"

Like all features, it must be used with judgement. Follow these guidelines to more effectively realize the benefits:

1. Choose a variable name that provides useful information
    - Consider names that express the variable's role or nature, e.g. rather than `var result` we can use `var customers`:
    ````
   try (var customers = dbconn.executeQuery(query)) {
        return result.map(...).filter(...).findAny();
   }
   ````
   - Consider names that convey the variable's meaning or type, e.g. rather than `var x`, a better name is `var custList`:
   ````
   var custList = dbconn.executeQuery(query);
   ````
1. Minimize the scope of local variables
    - Because the lines of code are farther away it makes the code harder to read and the situation harder to access:
    ````
    var items = new HashSet<Item>(...);
    // ... 100 lines of code ...
   // ... 100 lines of code ...
   // ... 100 lines of code ...
   items.add(MUST_BE_PROCESSED_LAST);
   for(var item : items) ...
    ````   
1. Consider `var` when the initializer provides sufficient information
    - Both sides of these statements offer type information:
    ````
   ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
   BufferedReader reader = Files.newBufferedReader(...);
   List<String> list = List.of("a", "b", "c");
   ````
   - Constructor or factory method calls are enough context:
   ````
   var outputStream = new ByteArrayOutputStream();
   var reader = Files.newBufferedReader(...);
   var list.of("a", "b", "c");
   ````
1. Consider `var` to split chained or nested expressions
    - These examples take a collection fo `String` and finds which occurs most
    - As a single expression, it's hard to decipher the stream and optional mix:
    ````
   return strings.stream()
        .collect(groupingBy(s -> s, counting()))
        .entrySet()
        .stream()
        .max(Map.Entry.comparingByValue())
        .map(Map.Entry::getKey);
   ````
   - Splitting it may yield a good balance of readability and concision:
   ````
      var freqMap = strings.stream()
           .collect(groupingBy(s -> s, counting()));
      var maxEntryOpt = freqMap.entrySet()
           .stream()
           .max(Map.Entry.comparingByValue());
      return maxEntryOpt.map(Map.Entry::getKey);
      ````
1. Don't worry too much about "Programming to the Interface" with local variables
    - A common Java idiom is to construct an instance with a concrete intializer type, but assign 
    it to a variable of an interface type:
    `List<String> list = new ArrayList<String>();`
    - This isn't possible with local variable type inference
    `var list = new ArrayList<String>():`
1. Take care using `var` with diamond or generic methods
    - Consider this statement:
    `PriorityQueue<Item> itemQueue = new PriorityQueue<Item>();`
    - The diamond feature lets you omit explicit type information:
    `PriorityQueue<Item> itemQueue = new PriorityQueue<>();`
    - `var` also lets you omit explicit type information:
    `var itemQueue = new PriorityQueue<Item>();`
    - Using both strips the statement of context:
    `var itemQueue = new PriorityQueue<>(); // inferred as PriorityQueue<Object>, which may not be helpful`
    - Arguments can provide context so type is inferred properly:
    ````
    Comparator<String> comp = ...;
    var itemQueue = new PriorityQueue<>(comp); // inferred as PriorityQueue<String>
    ````
1. Take care using `var` with literals
    - There is no issue `boolean`, `char`, `long`, and `String` literals:
    ````
    // before
    boolean ready = true;
   char ch = '\ufffd';
   long sum = 0L;
   String label = "wombat";
   
   // after
   var ready = true;
   var ch = '\ufffd';
   var sum = 0L;
   var label = "wombat";
   ````
   - Whole numbers require care, as they may be inferred as integer types:
   ````
   // before
   byte flags = 0;
   short mask = 0x7fff;
   long base = 17;
   
   // after
   var flags = 0;
   var mask - 0x7fff;
   var base = 17;
   ````
   - Floating point numbers require a little care, if you previously mixed types:
   ````
   // before
   float f1 = 1.0f;
   float f2 = 2.0;
   double d3 = 3.0;
   double d4 = 4.0f;
   
   // after
   float f1 = 1.0f;
   float f2 = 2.0; // inferred as double
   double d3 = 3.0; 
   double d4 = 4.0f; // inferred as float
   ````
   
# 7.2 - Create and use lambda expressions with local-variable type inferred parameters

Lambda expressions could be explicitly typed:

`(Item x, int y) -> x.process(y)`

Lambda expressions could be implicitly typed:

`(x, y) -> x.process(y)`

From Java 11 you can use the `var` reserved type name as lambda expression parameter type:

`(var x, var y) -> x.process(y)`

For example:

`UnaryOperator<String> lc = (var s) -> s.toLowerCase();`

The type of the parameter declared with the `var` above will be inferred to the type `String`, because the type declaration of the variable has its generic type set to `UnaryOperator<String>`, which means that the parameter type and return type of the `UnaryOperator` is `String`.

## Benefits

- Uniform syntax for local-variables type inferences:
  ````
  var x = new BigClassNamesMakeReadingHard();
  (var x, var y) -> x.process(y)
  ````
- Uniform syntax for local-variables type inferences:
  `(@Nonnull BigClassNamesMakeReadingHard x, final int y) -> x.process(y)`
- `var` provides the more readable implicitly typed alternative:
  `(@Nonnull var x, final var y) -> x.process(y)`
  
## These won't compile

You could never mix implicitly and explicitly typed lambda parameters:

`(Item x, y) -> x.process(y)`

You cannot mix `var` and non-`var` in implicitly typed lambda expressions:

`(var x, y) -> x.process(y)`

You cannot mix `var` and non-`var` in explicitly typed lambda expressions:

`(var x, int y) -> x.process(y)`

You cannot omit the parenthesis for single explicitly typed or `var` parameters:

`Item x -> x.toString()`
`var x -> x.toString()`
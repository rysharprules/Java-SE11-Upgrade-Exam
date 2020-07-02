- [5.1 - Use Path interface to operate on file and directory paths](#5-1)
- [5.2 - Use Files class to check, delete, copy or move a file or directory](#5-2)
- [5.3 - Use Stream API with Files](#5-3)
- [Quiz](#q)
- [Quiz Answers](#qa)

## <a name="5-1"></a>5.1 - Use Path interface to operate on file and directory paths

The `java.nio.file.Path` interface, or `Path` interface for short, is the primary entry point
for working with the NIO.2 API. A `Path` object represents a hierarchical path on the
storage system to a file or directory. In this manner, `Path` is a direct replacement for the
legacy `java.io.File` class, and conceptually it contains many of the same properties. For
example, both `File` and `Path` objects may refer to a file or a directory. Both also may refer
to an absolute path or relative path within the file system.

Unlike the `File` class, the `Path` interface contains support for symbolic links. A symbolic
link is a special file within an operating system that serves as a reference or pointer to another
file or directory. In general, symbolic links are transparent to the user, as the operating system
takes care of resolving the reference to the actual file. The NIO.2 API includes full support
for creating, detecting, and navigating symbolic links within the file system.

### Creating Instances with Factory and Helper Classes

The NIO.2 API makes good use of the factory pattern. A factory class can be implemented using static
methods to create instances of another class. For example, you can create an instance of a
`Path` interface using a static method available in the `Paths` factory class. Note the `s` at the
end of the `Paths` class to distinguish it from the `Path` interface.

NIO.2 also includes helper classes such as `java.nio.file.Files`, whose primary purpose
is to operate on instances of `Path` objects. Helper or utility classes are similar to factory classes
in that they are often composed primarily of static methods that operate on a particular
class. They differ in that helper classes are focused on manipulating or creating new objects
from existing instances, whereas factory classes are focused primarily on object creation.

You should become comfortable with this paradigm, if you are not already, as most
of your interactions with the NIO.2 API will require accessing at least two classes: an
interface and a factory or helper class. As a guideline for this section, we present the NIO.2
class and interface relationships in Figure 5.1.

![Figure 5.1](img/figure5-1.png)

### Creating `Paths`

Since `Path` is an interface, you need a factory class to create instances of one. The NIO.2
API provides a number of classes and methods that you can use to create `Path` objects,
which we will review in this section.

### Using the `Paths` Class

The simplest and most straightforward way to obtain a `Path` object is using the
`java.nio.files.Paths` factory class, or `Paths` for short. To obtain a reference to a file or
directory, you would call the static method `Paths.get(String)` method, as shown in
the following examples:

````
Path path1 = Paths.get("pandas/cuddly.png");
Path path2 = Paths.get("c:\\zooinfo\\November\\employees.txt");
Path path3 = Paths.get("/home/zoodirector");
````

The first example creates a `Path` reference to a relative file in the current working directory.
The second example creates a `Path` reference to an absolute file in a Windows-based
system. The third example creates a `Path` reference to an absolute directory in a Linux or
Mac-based system.

You can also create a `Path` using the `Paths` class using a vararg of type `String`, such as
`Paths.get(String,String...)`. This allows you to create a `Path` from a list of `String` values
in which the operating system-dependent `path.separator` is automatically inserted between
elements. System.getProperty("path.separator") can be used to get the operating system-dependent 
file separator from the JVM. That said, most JVM implementations support both forward and backward 
slashes regardless of the file system, allowing the same code to run on multiple operating systems 
without having to rewrite the slashes.

````
Path path1 = Paths.get("pandas","cuddly.png");
Path path2 = Paths.get("c:","zooinfo","November","employees.txt");
Path path3 = Paths.get("/","home","zoodirector");
````

These examples are a rewrite of our previous set of `Path` examples, using the parameter
list of `String` values instead of a single `String` value. The advantage of using this
overloaded method is that it is more robust when manually constructing path values, as it
inserts the proper path separator for you.

Another way to construct a `Path` using the `Paths` class is with a `URI` value. A uniform
resource identifier (URI) is a string of characters that identify a resource. It begins with
a schema that indicates the resource type, followed by a path value. Examples of schema
values include `file://`, `http://`, `https://`, and `ftp://`. The `java.net.URI` class is used to
create and manage `URI` values.

````
Path path1 = Paths.get(new URI("file://pandas/cuddly.png")); // THROWS EXCEPTION AT RUNTIME
Path path2 = Paths.get(new URI("file:///c:/zoo-info/November/employees.txt"));
Path path3 = Paths.get(new URI("file:///home/zoodirectory"));
````

These examples show how the `Paths.get(URI)` method can be used to obtain a reference
to a URI-based resource. Notice that these are actually rewrites of our earlier examples, as
we can use `URI` values for both local and network paths. The first example actually throws
an exception at runtime, as URIs must reference absolute paths at runtime. The `URI` class
does have an `isAbsolute()` method, although this is related to whether or not the URI has
a schema, not the file location.

We now present two additional methods that use other types of non-local file system
schemas. For the exam, you do not need to know the syntax of these schemas, but you
should be aware they exist.

````
Path path4 = Paths.get(new URI("http://www.wiley.com"));
Path path5 = Paths.get(new URI("ftp://username:password@ftp.the-ftp-server.com"));
````

Note that the constructor `new URI(String)` does throw a checked `URISyntaxException`,
which would have to be caught in any application where the previous code snippets are used.
Finally, the `Path` interface also contains a reciprocal method `toUri()` for converting a
`Path` instance back to a `URI` instance, as shown in the following sample code:

````
Path path4 = Paths.get(new URI("http://www.wiley.com"));
URI uri4 = path4.toUri();
````

#### Be Wary of `Path` vs. `Paths` on the Exam

Java is fond of using one name for the data class and the plural form
of that name for the factory or helper class. When you see questions with `Path` or `Paths`
on the exam, be sure that the class reference and usage are correct. For example, the
following usage is incorrect and will not compile:

````
Paths path1 = Paths.get("/alligator/swim.txt"); // DOES NOT COMPILE
Path path2 = Path.get("/crocodile/food.csv"); // DOES NOT COMPILE
````

In the first example, the `Path` object is being assigned to a `Paths` instance, which is
incompatible. In the second example, there is attempt to access a method that does not
exist, `Path.get("String")`.

The key to remember is that the singular form `Path` represents the instance with which
you want to work, whereas the plural form `Paths` is the factory class containing methods
for creating `Path` instances.

### Providing Optional Arguments

Throughout this section, we introduce numerous methods for interacting with files and
directories in NIO.2. Many of the methods in the NIO.2 API that interact with real files
and directories take additional options flags in the form of a vararg.
For the exam, you do not need to memorize which of the dozens of NIO.2 methods take
which optional arguments, but you should be able to recognize what they do when you
see them on the exam. Table 5.1 lists the values that you should know for the exam. Note
that these descriptions apply to both files and directories. If you are not familiar with the
operations to which these attributes apply, don’t worry; we’ll explain them later in this chapter.

TABLE 5.1 - Common optional arguments in NIO.2

| Enum Value | Usage | Description |
| --- | --- | --- |
| `NOFOLLOW_LINKS` | Test file existing, Read file data, Copy file, Move file | If provided, symbolic links when encountered will not be traversed. Useful for performing operations on symbolic links themselves rather than their target. |
| `FOLLOW_LINKS` | Traverse a directory tree | If provided, symbolic links when encountered will be traversed. |
| `COPY_ATTRIBUTES` | Copy file | If provided, all metadata about a file will be copied with it. |
| `REPLACE_EXISTING` | Copy file, Move file | If provided and the target file exists, it will be replaced; otherwise, if it is not provided, an exception will be thrown if the file already exists. |
| `ATOMIC_MOVE` | Move file | The operation is performed in an atomic manner within the file system, ensuring that any process using the file sees only a complete record. Method using it may throw an exception if the feature is unsupported by the file system. |

For simplicity as well as better readability, we purposely omit the `enum` class names to
which the values belong throughout the text, although we do include them in any practice
questions. For example, the copy methods take a list of `CopyOption` interface values,
of which `StandardCopyOption` is an enum that implements the interface and includes
`StandardCopyOption.COPY_ATTRIBUTES` as an option. As we said, for simplicity, we omit
these details as the exam won’t require you to understand this relationship.

While the name of many of these options gives a basic description of their function, the
`ATOMIC_MOVE` option may be completely new to you. An atomic operation is any operation
that is performed as a single indivisible unit of execution, which appears to the rest of the
system as occurring instantaneously. Furthermore, an atomic move is one in which any
process monitoring the file system never sees an incomplete or partially written file. If the file
system does not support this feature, an `AtomicMoveNotSupportedException` will be thrown.

For the remainder of the chapter, we leave out these `enum` values in the method definitions
so that you can focus on the core functionality. For the exam, you should understand
their effect if you see them provided to a method.

### Using `Path` Objects

The `Path` interface includes numerous methods for using `Path` objects. You have already
seen two of them, `toFile()` and `toUri()`, used to convert `Path` objects to other types of
resources.

Many of the methods in the `Path` interface transform the path value in some way and
return a new `Path` object, allowing the methods to be chained:

`Paths.get("/zoo/../home").getParent().normalize().toAbsolutePath();`

#### Viewing the `Path` with `toString()`, `getNameCount()`, and `getName()`

The `Path` interface contains three methods to retrieve basic information about the
path representative. The first method, `toString()`, returns a `String` representation of
the entire path. In fact, it is the only method in the `Path` interface to return a `String`.
Most of the other methods that we will discuss in this section return a new `Path`
object.

The second and third methods, `getNameCount()` and `getName(int)`, are often
used in conjunction to retrieve the number of elements in the path and a reference to
each element, respectively. For greater compatibility with other NIO.2 methods, the
`getName(int)` method returns the component of the `Path` as a new `Path` object rather
than a `String`.

The following sample code uses these methods to retrieve path data:

````
Path path = Paths.get("/land/hippo/harry.happy");
System.out.println("The Path Name is: "+path);
for(int i=0; i<path.getNameCount(); i++) {
    System.out.println(" Element "+i+" is: "+path.getName(i));
}
````

Printing an object automatically invokes the object’s `toString()` method. The output of
this code snippet is the following:

````
The Path Name is: /land/hippo/harry.happy
Element 0 is: land
Element 1 is: hippo
Element 2 is: harry.happy
````

Notice that the root element `/` is not included in the list of names. If the `Path` object
represents the root element itself, then the number of names in the `Path` object returned by
`getNameCount()` will be 0.

What if we ran the preceding code using the relative path `land/hippo/harry.happy`?
The output would be as follows:

````
The Path Name is: land/hippo/harry.happy
Element 0 is: land
Element 1 is: hippo
Element 2 is: harry.happy
````

Notice that the individual names are the same. For the exam, you should be aware that
the `getName(int)` method is zero-indexed, with the file system root excluded from the path
components.

#### Accessing `Path` Components with `getFileName()`, `getParent()`, and `getRoot()`

The `Path` interface contains numerous methods for retrieving specific sub-elements of a `Path`
object, returned as `Path` objects themselves. The first method, `getFileName()`, returns a `Path`
instance representing the filename, which is the farthest element from the root. Like most
methods in the `Path` interface, `getFileName()` returns a new `Path` instance rather than a `String`.

The next method, `getParent()`, returns a `Path` instance representing the parent path or
`null` if there is no such parent. If the instance of the `Path` object is relative, this method will
stop at the top-level element defined in the `Path` object. In other words, it will not traverse
outside the working directory to the file system root.

The last method, `getRoot()`, returns the root element for the `Path` object or `null` if the
`Path` object is relative.

We present a sample application that traverses absolute and relative `Path` objects to show
how each handles the root differently:

````
import java.nio.file.*;
public class PathFilePathTest {
    public static void printPathInformation(Path path) {
        System.out.println("Filename is: "+path.getFileName());
        System.out.println("Root is: "+path.getRoot());
        Path currentParent = path;
        while((currentParent = currentParent.getParent()) != null) {
            System.out.println(" Current parent is: "+currentParent);
        }
    }
    public static void main(String[] args) {
        printPathInformation(Paths.get("/zoo/armadillo/shells.txt"));
        System.out.println();
        printPathInformation(Paths.get("armadillo/shells.txt"));
    }
}
````

The while loop in the `printPathInformation()` method continues until `getParent()`
returns `null`. This sample application produces the following output:

````
Filename is: shells.txt
Root is: /
Current parent is: /zoo/armadillo
Current parent is: /zoo
Current parent is: /
Filename is: shells.txt
Root is: null
Current parent is: armadillo
````

Reviewing the sample output, you can see the difference in the behavior of `getRoot()` on
absolute and relative paths. Also, notice that traversing the second path stopped at the top
of the relative directory. As you can see in the example, it does not traverse relative directories
outside the working directory.

#### Checking `Path` Type with `isAbsolute()` and `toAbsolutePath()`

The `Path` interface contains two methods for assisting with relative and absolute
paths. The first method, `isAbsolute()`, returns true if the path the object references
is absolute and false if the path the object references is relative.

The second method, `toAbsolutePath()`, converts a relative `Path` object to an absolute
`Path` object by joining it to the current working directory. If the `Path` object is already
absolute, then the method just returns an equivalent copy of it.

The following code snippet shows usage of both of these methods:

````
Path path1 = Paths.get("C:\\birds\\egret.txt");
System.out.println("Path1 is Absolute? "+path1.isAbsolute());
System.out.println("Absolute Path1: "+path1.toAbsolutePath());

Path path2 = Paths.get("birds/condor.txt");
System.out.println("Path2 is Absolute? "+path2.isAbsolute());
System.out.println("Absolute Path2 "+path2.toAbsolutePath());
````

The output for the code snippet is shown in the following sample code. Since the precise
output is file system dependent, we’ll treat the first example as being run on a Windows-based
system, whereas the second example is run in a Linux or Mac-based system with the
current working directory of /home.

````
Path1 is Absolute? true
Absolute Path1: C:\birds\egret.txt

Path2 is Absolute? false
Absolute Path2 /home/birds/condor.txt
````

Keep in mind that if the `Path` object already represents an absolute path, then the output
is a new `Path` object with the same value.

As discussed earlier in this chapter, absolute and relative path types are actually file
system dependent. In fact, you might be surprised by the output of the following lines of
code on various operating systems:

````
System.out.println(Paths.get("/stripes/zebra.exe").isAbsolute());
System.out.println(Paths.get("c:/goats/Food.java").isAbsolute());
````

Although the first line outputs true on a Linux or Mac-based system, it outputs false
on a Windows-based system since it is missing a drive letter prefix. In the same manner, the
second path outputs true on Windows but false on a Linux or Mac-based system, as it is
missing the root forward slash, `/`.

#### Creating a New `Path` with `subpath()`

The method `subpath(int,int)` returns a relative subpath of the `Path` object, referenced
by an inclusive start index and an exclusive end index. It is useful for constructing a new
relative path from a particular parent path element to another parent path element, as
shown in the following example:

````
Path path = Paths.get("/mammal/carnivore/raccoon.image");
System.out.println("Path is: "+path);

System.out.println("Subpath from 0 to 3 is: "+path.subpath(0,3));
System.out.println("Subpath from 1 to 3 is: "+path.subpath(1,3));
System.out.println("Subpath from 1 to 2 is: "+path.subpath(1,2));
````

You might notice that the `subpath()` and `getName(int)` methods are similar in that
they both return a `Path` object that represents a component of an existing `Path`. The
difference is that the `subpath()` method may include multiple path components, whereas
the `getName(int)` method only includes one.

The output of this code snippet is the following:

````
Path is: /mammal/carnivore/raccoon.image
Subpath from 0 to 3 is: mammal/carnivore/raccoon.image
Subpath from 1 to 3 is: carnivore/raccoon.image
Subpath from 1 to 2 is: carnivore
````

This code demonstrates that the `subpath(int,int)` method does not include the root
of the file. Notice that the 0-indexed element is mammal in this example and not the root
directory; therefore, the maximum index that can be used is 3.

The following two examples both throw `java.lang.IllegalArgumentException` at runtime:

````
System.out.println("Subpath from 0 to 4 is: "+path.subpath(0,4)); // THROWS EXCEPTION AT RUNTIME
System.out.println("Subpath from 1 to 1 is: "+path.subpath(1,1)); // THROWS EXCEPTION AT RUNTIME
````

The first example throws an exception at runtime, since the maximum index value
allowed is 3. The second example throws an exception since the start and end indexes are
the same, leading to an empty path value.

#### Using `Path` Symbols

Many file systems support paths that contain relative path information in the form of path
symbols. For example, you might want a path that refers to the parent directory, regardless
of what the current directory is. In this scenario, the double period value `..` can be used to
reference the parent directory. In addition, the single period value `.` can be used to reference
the current directory within a path.

Table 5.2 lists symbols that are helpful to know while working with file paths.

TABLE 5.2 File system symbols

| Symbol | Description |
| --- | --- |
| `.` | A reference to the current directory |
| `..` | A reference to the parent of the current directory |

For example, the path value `../bear.txt` refers to a file named `bear.txt` in the parent
of the current directory. Likewise, the path value `./penguin.txt` refers to a file named
`penguin.txt` in the current directory. These symbols can also be combined for greater
effect. For example, `../../lion.data` refers to a file `lion.data` that is two directories up
from the current working directory.

#### Deriving a `Path` with `relativize()`

The `Path` interface provides a method `relativize(Path)` for constructing the relative
path from one `Path` object to another. Consider the following relative and absolute path
examples using the `relativize()` method.

````
Path path1 = Paths.get("fish.txt");
Path path2 = Paths.get("birds.txt");
System.out.println(path1.relativize(path2));
System.out.println(path2.relativize(path1));
````

The code snippet produces the following output when executed:

````
..\birds.txt
..\fish.txt
````

If both path values are relative, then the `relativize()` method computes the paths as if
they are in the same current working directory. Notice that `..\` is included at the start of
the first set of examples. Since our path value points to a file, we need to move to the parent
directory that contains the file.

Alternatively, if both path values are absolute, then the method computes the relative
path from one absolute location to another, regardless of the current working directory.
The following example demonstrates this property:

````
Path path3 = Paths.get("E:\\habitat");
Path path4 = Paths.get("E:\\sanctuary\\raven");
System.out.println(path3.relativize(path4));
System.out.println(path4.relativize(path3));
````

This code snippet produces the following output when executed:

````
..\sanctuary\raven
..\..\habitat
````

In this set of examples, the two path values are absolute, and the `relativize()` method
constructs the relative path between the two absolute path values within the file system.
Note that the file system is not accessed to perform this comparison. For example, the root
path element `E:` may not exist in the file system, yet the code would execute without issue
since Java is referencing the path elements and not the actual file values.

##### Compatible `Path` Types for `relativize()`

The `relativize()` method requires that both paths be absolute or both relative, and
it will throw an `IllegalArgumentException` if a relative path value is mixed with an
absolute path value. For example, the following would throw an exception at runtime:

````
Path path1 = Paths.get("/primate/chimpanzee");
Path path2 = Paths.get("bananas.txt");
Path1.relativize(path3); // THROWS EXCEPTION AT RUNTIME
````

On Windows-based systems, it also requires that if absolute paths are used, then both
paths must have the same root directory or drive letter. For example, the following would
also throw an `IllegalArgumentException` at runtime in a Windows-based system since
they use different roots:

````
Path path3 = Paths.get("c:\\primate\\chimpanzee");
Path path4 = Paths.get("d:\\storage\\bananas.txt");
path3.relativize(path4); // THROWS EXCEPTION AT RUNTIME
````

#### Joining `Path` Objects with `resolve()`

The `Path` interface includes a `resolve(Path)` method for creating a new `Path` by joining an
existing path to the current path. To put it another way, the object on which the `resolve()`
method is invoked becomes the basis of the new `Path` object, with the input argument being
appended onto the `Path`. Let’s see what happens if we apply `resolve()` to an absolute path
and a relative path:

````
final Path path1 = Paths.get("/cats/../panther");
final Path path2 = Paths.get("food");
System.out.println(path1.resolve(path2));
````

The code snippet generates the following output:

`/cats/../panther/food`

For the exam, you should be aware that, like the `relativize()` method, the `resolve()`
method does not clean up path symbols, such as the parent directory `..` symbol. For that,
you’ll need to use the `normalize()` method.

In this example, the input argument to the `resolve()` method was a relative path, but
what if had been an absolute path?

````
final Path path1 = Paths.get("/turkey/food");
final Path path2 = Paths.get("/tiger/cage");
System.out.println(path1.resolve(path2));
````

Since the input parameter path2 is an absolute path, the output would be the following:

`/tiger/cage`

For the exam, you should be cognizant of mixing absolute and relative paths with the
`resolve()` method. If an absolute path is provided as input to the method, such as `path1.
resolve(path2)`, then `path1` would be ignored and a copy of `path2` would be returned.

#### Cleaning Up a `Path` with `normalize()`

As you saw with the `relativize()` method, file systems can construct relative paths using
`..` and `.` values. There are times, however, when relative paths are combined such that there
are redundancies in the path value. Luckily, Java provides us with the `normalize(Path)`
method to eliminate the redundancies in the path.

For example, let’s take the output of one of our previous examples that resulted in
the path value `..\user\home` and try to reconstitute the original absolute path using the
`resolve()` method:

````
Path path3 = Paths.get("E:\\data");
Path path4 = Paths.get("E:\\user\\home");
Path relativePath = path3.relativize(path4);
System.out.println(path3.resolve(relativePath));
````

The result of this sample code would be the following output:

`E:\data\..\user\home`

You can see that this path value contains a redundancy. Worse yet, it does not match our
original value, `E:\user\home`. We can resolve this redundancy by applying the `normalize()`
method as shown here:

`System.out.println(path3.resolve(relativePath).normalize());`

This modified last line of code nicely produces our original path value:

`E:\user\home`

Like `relativize()`, the `normalize()` method does not check that the file actually exists.
As you shall see with our final `Path` method, `toRealPath()`, Java provides a way to verify
that the file does exactly exist.

#### Checking for file Existence with `toRealPath()`

The `toRealPath(Path)` method takes a `Path` object that may or may not point to an
existing file within the file system, and it returns a reference to a real path within the file
system. It is similar to the `toAbsolutePath()` method in that it can convert a relative path
to an absolute path, except that it also verifies that the file referenced by the path actually
exists, and thus it throws a checked `IOException` at runtime if the file cannot be located. It
is also the only `Path` method to support the `NOFOLLOW_LINKS` option.

The `toRealPath()` method performs additional steps, such as removing redundant path
elements. In other words, it implicitly calls `normalize()` on the resulting absolute path.

Let’s say that we have a file system in which we have a symbolic link from `food.source`
to `food.txt`, as described in the following relationship:

`/zebra/food.source → /horse/food.txt`

Assuming that our current working directory is `/horse/schedule`, then consider the
following code:

````
try {
    System.out.println(Paths.get("/zebra/food.source").toRealPath());
    System.out.println(Paths.get(".././food.txt").toRealPath());
} catch (IOException e) {
    // Handle file I/O exception...
}
````

Notice that we have to catch `IOException`, since unlike the `toAbsolutePath()` method,
the `toRealPath()` method interacts with the file system to check if the path is valid. Given the
symbolic link and current working directory as described, then the output would be the following:

````
/horse/food.txt
/horse/food.txt
````

In these examples, the absolute and relative paths both resolve to the same absolute file,
as the symbolic link points to a real file within the file system.

Finally, we can also use the `toRealPath()` method to gain access to the current working
directory, such as shown here:

`System.out.println(Paths.get(".").toRealPath());`

## <a name="5-2"></a> 5.2 Use Files class to check, delete, copy or move a file or directory

### Interacting with `Files`

Great! We now have access to a `Path` object, and we can find out a ton of information
about it, but what can we do with the file it references? For starters, many of the same
operations available in `java.io.File` are available to `java.nio.file.Path` via a helper
class called `java.nio.file.Files`, or `Files` for short. Unlike the methods in the `Path` and
`Paths` class, most of the options within the `Files` class will throw an exception if the file to
which the `Path` refers does not exist.

The `Files` class contains numerous static methods for interacting with files, with most
taking one or two `Path` objects as arguments. Some of these methods are capable of throwing
the checked `IOException` at runtime, often when the file being referenced does not exist
within the file system, as you saw with the `Path` method `toRealPath()`.

#### Testing a `Path` with `exists()`

The `Files.exists(Path)` method takes a `Path` object and returns true if, and only if, it
references a file that exists in the file system.

Let’s take a look at some sample code:

````
Files.exists(Paths.get("/ostrich/feathers.png"));
Files.exists(Paths.get("/ostrich"));
````

The first example checks whether a file exists, while the second example checks whether a
directory exists. You can see that this method does not throw an exception if the file does not
exist, as doing so would prevent this method from ever returning false at runtime.

#### Testing Uniqueness with `isSameFile()`

The `Files.isSameFile(Path,Path)` method is useful for determining if two `Path` objects
relate to the same file within the file system. It takes two `Path` objects as input and follows
symbolic links. Despite the name, the method also determines if two `Path` objects refer to
the same directory.

The `isSameFile()` method first checks if the `Path` objects are equal in terms of `equal()`,
and if so, it automatically returns true without checking to see if either file exists. If the
`Path` object `equals()` comparison returns false, then it locates each file to which the
path refers in the file system and determines if they are the same, throwing a checked
`IOException` if either file does not exist.

_Note: This `isSameFile()` method does not compare the contents of the file. For
example, two files may have identical content and attributes, but if they
are in different locations, then this method will return false._

Let’s assume that all of the files in the following examples exist within the file system and
that cobra is a symbolic link to the snake file. What would be the output of the following code
snippet?

````
try {
    System.out.println(Files.isSameFile(Paths.get("/user/home/cobra"),
        Paths.get("/user/home/snake")));
    System.out.println(Files.isSameFile(Paths.get("/user/tree/../monkey"),
        Paths.get("/user/monkey")));
    System.out.println(Files.isSameFile(Paths.get("/leaves/./giraffe.exe"),
        Paths.get("/leaves/giraffe.exe")));
    System.out.println(Files.isSameFile(Paths.get("/flamingo/tail.data"),
        Paths.get("/cardinal/tail.data")));
} catch (IOException e) {
    // Handle file I/O exception...
}
````

Since `cobra` is a symbolic link to the `snake` file, the first example outputs true. In
the second example, the symbol `..` cancels out the tree path of the path, resulting in
the method also outputting true. In the third example, the symbol `.` leaves the path
unmodified, so the result is true as well. The final example returns false, assuming that
neither file is a symbolic link to the other. Even if the files have the same name and the same
contents, if they are at different locations, they are considered different files within the file
system.

#### Making Directories with `createDirectory()` and `createDirectories()`

To create directories in the legacy `java.io API`, we called `mkdir()` or `mkdirs()` on a `File`
object. In the NIO.2 API, we can use the `Files.createDirectory(Path)` method to create
a directory. There is also a plural form of the method called `createDirectories()`, which
like `mkdirs()` creates the target directory along with any nonexistent parent directories
leading up to the target directory in the path.

The directory-creation methods can throw the checked `IOException`, such as when
the directory cannot be created or already exists. For example, the first method,
`createDirectory()`, will throw an exception if the parent directory in which the new
directory resides does not exist.

Both of these methods also accept an optional list of `FileAttribute<?>` values to set on
the newly created directory or directories. We will discuss file attributes in the next section.
We now present a code snippet that shows how to create directories using NIO.2:

````
try {
    Files.createDirectory(Paths.get("/bison/field"));
    Files.createDirectories(Paths.get("/bison/field/pasture/green"));
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The first example creates a new directory, field, in the directory `/bison`, assuming
`/bison` exists; or else an exception is thrown. Contrast this with the second example that
creates the directory `green` along with any of the following parent directories if they do not
already exist, such as `/bison`, `/bison/field`, or `/bison/pasture`.

#### Duplicating File Contents with `copy()`

Unlike the legacy `java.io.File` class, the NIO.2 Files class provides a set of overloaded
`copy()` methods for copying files and directories within the file system. The primary one
that you should know about for the exam is `Files.copy(Path,Path)`, which copies a
file or directory from one location to another. The `copy()` method throws the checked
`IOException`, such as when the file or directory does not exist or cannot be read.

Directory copies are shallow rather than deep, meaning that files and subdirectories
within the directory are not copied. To copy the contents of a directory, you would need to
create a function to traverse the directory and copy each file and subdirectory individually:

````
try {
    Files.copy(Paths.get("/panda"), Paths.get("/panda-save"));
    Files.copy(Paths.get("/panda/bamboo.txt"), Paths.get("/panda-save/bamboo.txt"));
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The first example performs a shallow copy of the `panda` directory, creating a new
`panda-save` directory, but it does not copy any of the contents of the original directory.
The second example copies the `bamboo.txt` file from the directory panda to the directory
`panda-save`.

By default, copying files and directories will traverse symbolic links, although it
will not overwrite a file or directory if it already exists, nor will it copy file attributes.
These behaviors can be altered by providing the additional options `NOFOLLOW_LINKS`,
`REPLACE_EXISTING`, and `COPY_ATTRIBUTES`, respectively.

##### Copying Files with java.io and NIO.2

The NIO.2 Files API class contains two overloaded `copy()` methods for copying files
using java.io streams. The first `copy()` method takes a source
`java.io.InputStream` along with a target `Path` object. It reads the contents from the
stream and writes the output to a file represented by a `Path` object.

The second `copy()` method takes a source `Path` object and target `java.io.OutputStream`.
It reads the contents of the file and writes the output to the stream.

The following are examples of each `copy()` method:

````
try (InputStream is = new FileInputStream("source-data.txt");
    OutputStream out = new FileOutputStream("output-data.txt")) {
        // Copy stream data to file
        Files.copy(is, Paths.get("c:\\mammals\\wolf.txt"));
        // Copy file data to stream
        Files.copy(Paths.get("c:\\fish\\clown.xsl"), out);
} catch (IOException e) {
    // Handle file I/O exception...
}
````

In this example, the `InputStream` and `OutputStream` parameters could refer to any valid
stream, including website connections, in-memory stream resources, and so forth.

Like the first `copy()` method, the `copy(InputStream,Path)` method also supports
optional vararg options, since the data is being written to a file represented by a `Path`
object. The second method, `copy(Path,OutputStream)`, does not support optional vararg
values, though, since the data is being written to a stream that may not represent a file
system resource.

#### Changing a File Location with `move()`

The `Files.move(Path,Path)` method moves or renames a file or directory within the file
system. Like the `copy()` method, the `move()` method also throws the checked `IOException`
in the event that the file or directory could not be found or moved.
The following is some sample code that uses the `move()` method:

````
try {
    Files.move(Paths.get("c:\\zoo"), Paths.get("c:\\zoo-new"));
    Files.move(Paths.get("c:\\user\\addresses.txt"), Paths.get("c:\\zoo-new\\addresses.txt"));
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The first example renames the `zoo` directory to `zoo-new` directory, keeping all of the
original contents from the source directory. The second example moves the `addresses.txt`
file from the directory user to the directory `zoo-new`, and it renames it to `addresses2.txt`.

By default, the `move()` method will follow links, throw an exception if the file
already exists, and not perform an atomic move. These behaviors can be changed by
providing the optional values `NOFOLLOW_LINKS`, `REPLACE_EXISTING`, or `ATOMIC_MOVE`,
respectively, to the method. If the file system does not support atomic moves, an
`AtomicMoveNotSupportedException` will be thrown at runtime.

_Note: The `Files.move()` method can be applied to non-empty directories only if
they are on the same underlying drive. While moving an empty directory
across a drive is supported, moving a non-empty directory across a drive
will throw an NIO.2 `DirectoryNotEmptyException`._

#### Removing a File with `delete()` and `deleteIfExists()`

The `Files.delete(Path)` method deletes a file or empty directory within the file system.
The `delete()` method throws the checked `IOException` under a variety of circumstances.
For example, if the path represents a non-empty directory, the operation will throw the
runtime `DirectoryNotEmptyException`. If the target of the path is a symbol link, then the
symbolic link will be deleted, not the target of the link.

The `deleteIfExists(Path)` method is identical to the `delete(Path)` method, except
that it will not throw an exception if the file or directory does not exist, but instead it will
return a boolean value of false. It will still throw an exception if the file or directory does
exist but fails, such as in the case of the directory not being empty.

We now provide sample code that performs `delete()` operations:

````
try {
    Files.delete(Paths.get("/vulture/feathers.txt"));
    Files.deleteIfExists(Paths.get("/pigeon"));
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The first example deletes the `features.txt` file in the vulture directory, and it throws
a `NoSuchFileException` if the file or directory does not exist. The second example deletes
the `pigeon` directory assuming it is empty. If the `pigeon` directory does not exist, then the
second line will not throw an exception.

#### Reading and Writing File Data with `newBufferedReader()` and `newBufferedWriter()`

The NIO.2 API includes methods for reading and writing file contents using java.io streams.

The first method, `Files.newBufferedReader(Path,Charset)`, reads the file specified
at the `Path` location using a `java.io.BufferedReader` object. It also requires a `Charset`
value to determine what character encoding to use to read the file (characters can be encoded in 
bytes in a variety of ways). It may also be useful to know that `Charset.defaultCharset()` can be used to get
the default Charset for the JVM.

````
Path path = Paths.get("/animals/gopher.txt");
try (BufferedReader reader = Files.newBufferedReader(path,
    Charset.forName("US-ASCII"))) {
    // Read from the stream
    String currentLine = null;
    while((currentLine = reader.readLine()) != null)
        System.out.println(currentLine);
} catch (IOException e) {
    // Handle file I/O exception...
}
````

This example reads the contents of the files using a `BufferedReader` and outputs the
contents to the user. As you shall see in the next section, there is a much simpler way to
accomplish this, which uses functional programming streams.

The second method, `Files.newBufferedWriter(Path,Charset)`, writes to a file specified
at the `Path` location using a `BufferedWriter`. Like the reader method, it also takes a
`Charset` value:

````
Path path = Paths.get("/animals/gorilla.txt");
List<String> data = new ArrayList();
try (BufferedWriter writer = Files.newBufferedWriter(path,
            Charset.forName("UTF-16"))) {
    writer.write("Hello World");
} catch (IOException e) {
    // Handle file I/O exception...
}
````

This code snippet creates a new file with the specified contents, overwriting the file if it
already exists. The `newBufferedWriter()` method also supports taking additional enum
values in an optional vararg, such as appending to an existing file instead of overwriting it,
although you do not need to memorize this list for the exam.

Since both of these methods create resources, we use the try-with-resource syntax.
Also, note that both of these methods use buffered streams rather than low-level
file streams. The buffered stream classes are much more performant in practice, so much so that 
the NIO.2 API includes methods that specifically return these stream classes, in part to encourage 
you always to use buffered streams in your application.

#### Reading Files with `readAllLines()`

The `Files.readAllLines()` method reads all of the lines of a text file and returns the
results as an ordered `List` of `String` values. The NIO.2 API includes an overloaded version
that takes an optional `Charset` value. The following sample code reads the lines of the file
and outputs them to the user:

````
Path path = Paths.get("/fish/sharks.log");
try {
    final List<String> lines = Files.readAllLines(path);
    for(String line: lines) {
        System.out.println(line);
    }
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The code snippet reads all of the lines of the file and then iterates over them. As you
might expect, the method may throw an `IOException` if the file cannot be read.

Be aware that the entire file is read when `readAllLines()` is called, with the resulting
`String` array storing all of the contents of the file in memory at once. Therefore, if the file
is significantly large, you may encounter an `OutOfMemoryError` trying to load all of it into
memory.

### Discovering Basic File Attributes

#### Reading Common Attributes with `isDirectory()`, `isRegularFile()`, and `isSymbolicLink()`

The `Files` class includes three methods for determining if a path refers to a directory, a regular
file, or a symbolic link. The methods to accomplish this are named `Files.isDirectory(Path)`,
`Files.isRegularFile(Path)`, and `Files.isSymbolicLink(Path)`, respectively.

Java defines a regular file as one that contains content, as opposed to a symbolic link,
directory, resource, or other non-regular file that may be present in some operating systems.
If the symbolic link points to a real file or directory, Java will perform the check on the
target of the symbolic link. In other words, it is possible for `isRegularFile()` to return
true for a symbolic link, as long as the link resolves to a regular file.

````
Files.isDirectory(Paths.get("/canine/coyote/fur.jpg"));
Files.isRegularFile(Paths.get("/canine/types.txt"));
Files.isSymbolicLink(Paths.get("/canine/coyote"));
````

The first example returns true if `fur.jpg` is a directory or a symbolic link to a directory
and false otherwise. Note that directories can have extensions in many file systems, so it
is possible for `fur.jpg` to be the name of a directory. The second example returns true if
`types.txt` points to a regular file or alternatively a symbolic link that points to a regular
file. The third example returns true if `/canine/coyote` is a symbolic link, regardless of
whether the file or directory it points to exists.

We illuminate these concepts in Table 5.3. For this table, assume that the file system
with the directory `/canine/coyote` and file `/canine/types.txt` exists. Furthermore,
assume that `/coyotes` is a symbolic link within the file system that points to another path
within the file system.

TABLE 5.3 `isDirectory()`, `isRegularFile()`, `isSymbolicLink()` examples

| | isDirectory() | isRegularFile() | isSymbolicLink() |
|---|---|---|---|
|/canine/coyote | true |false |false|
|/canine/types.txt |false |true |false|
|/coyotes | true if the target is a directory|true if the target is a regular file|true|

You see that the value of `isDirectory()` and `isRegular()` in Table 5.3 cannot be determined
on the symbolic link `/coyotes` without knowledge of what the symbolic link points to.

#### Checking File Visibility with `isHidden()`

The Files class includes the `Files.isHidden(Path)` method to determine whether a
file or directory is hidden within the file system. In Linux- or Mac-based systems, this
is often denoted by file or directory entries that begin with a period character (`.`), while
in Windows-based systems this requires the hidden attribute to be set. The `isHidden()`
method throws the checked `IOException`, as there may be an I/O error reading the underlying
file information. We present illustrative usage of this method in the following sample
code:

````
try {
    System.out.println(Files.isHidden(Paths.get("/walrus.txt")));
} catch (IOException e) {
    // Handle file I/O exception...
}
````

If the `walrus.txt` file is available and hidden within the file system, this method will
return true.

#### Testing File Accessibility with `isReadable()` and `isExecutable()`

The `Files` class includes two methods for reading file accessibility: `Files.isReadable(Path)` 
and `Files.isExecutable(Path)`. This is important in file systems where the filename can be viewed 
within a directory, but the user may not have permission to read the contents of the file or execute 
it.

````
System.out.println(Files.isReadable(Paths.get("/seal/baby.png")));
System.out.println(Files.isExecutable(Paths.get("/seal/baby.png")));
````

The first example returns true if the `baby.png` file exists and its contents are readable,
based on the permission rules of the underlying file system. The second example returns
true if the `baby.png` file is marked executable within the file system. Note that the file
extension does not necessary determine whether a file is executable. For example, an image
file that ends in .png could be marked executable within a Linux-based system.

Like the `isDirectory()`, `isRegularFile()`, and `isSymbolicLink()` methods, the
`isReadable()` and `isExecutable()` methods do not throw exceptions if the file does not
exist but instead return false.

#### Reading File Length with `size()`

The `Files.size(Path)` method is used to determine the size of the file in bytes. The size
returned by this method represents the conceptual size of the data, and this may differ from
the actual size on the persistence storage device due to file system compression and organization.
The `size()` method throws the checked `IOException` if the file does not exist or if
the process is unable to read the file information.

````
try {
    System.out.println(Files.size(Paths.get("/zoo/c/animals.txt")));
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The example outputs the number of bytes in the file, expressed as a long value. As you
may have already realized, we’re repeating a lot of the methods defined in `java.io.File`. 
Since the NIO.2 API was defined as a replacement for the java.io API, it includes many of the same 
methods in one form or another.

#### Managing File Modifications with `getLastModifiedTime()` and `setLastModifiedTime()`

Most operating systems support tracking a last-modified date/time value with each file.
Some applications use this to determine when the file should be read again. For example,
there might be a program that performs an operation anytime the file data changes. In the
majority of circumstances, it is a lot faster to check a single file metadata attribute than to
reload the entire contents of the file, especially if the file is large.

The `Files` class provides the method `Files.getLastModifiedTime(Path)`, which returns
a `FileTime` object to accomplish this. The `FileTime` class is a simple container class that
stores the date/time information about when a file was accessed, modified, or created. For
convenience, it has a `toMillis()` method that returns the epoch time.

The `Files` class also provides a mechanism for updating the last-modified date/time of
a file using the `Files.setLastModifiedTime(Path,FileTime)` method. The `FileTime` class
also has a static `fromMillis()` method that converts from the epoch time to a `FileTime`
object.

Both of these methods have the ability to throw a checked `IOException` when the file is
accessed or modified.

````
try {
    final Path path = Paths.get("/rabbit/food.jpg");
    System.out.println(Files.getLastModifiedTime(path).toMillis());
    Files.setLastModifiedTime(path,
        FileTime.fromMillis(System.currentTimeMillis()));
    System.out.println(Files.getLastModifiedTime(path).toMillis());
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The first part of the code reads and outputs the last-modified time value of the `food.jpeg`
file. The next line sets a last-modified date/time using the current time value. Finally, we
repeat our earlier line and output the newly set last-modified date/time value.

#### Managing Ownership with `getOwner()` and `setOwner()`

Many file systems also support the notion of user-owned files and directories. In this
manner, the `Files.getOwner(Path)` method returns an instance of `UserPrincipal` that
represents the owner of the file within the file system.

As you may have already guessed, there is also a method to set the owner, called `Files
.setOwner(Path,UserPrincipal)`. Note that the operating system may intervene when you
try to modify the owner of a file and block the operation. For example, a process running
under one user may not be allowed to take ownership of a file owned by another user. Both
the `getOwner()` and `setOwner()` methods can throw the checked exception `IOException` in
case of any issues accessing or modifying the file.

In order to set a file owner to an arbitrary user, the NIO.2 API provides a
`UserPrincipalLookupService` helper class for finding a `UserPrincipal` record for a
particular user within a file system. In order to use the helper class, you first need to obtain
an instance of a `FileSystem` object, either by using the `FileSystems.getDefault()` method
or by calling `getFileSystem()` on the `Path` object with which you are working, as shown in
the following two examples:

````
UserPrincipal owner = FileSystems.getDefault().getUserPrincipalLookupService()
    .lookupPrincipalByName("jane");
Path path = ...
UserPrincipal owner = path.getFileSystem().getUserPrincipalLookupService()
    .lookupPrincipalByName("jane");
````

We now present examples of the `getOwner()` and `setOwner()` methods, including an
example of how to use the `UserPrincipalLookupService`:

````
try {
    // Read owner of file
    Path path = Paths.get("/chicken/feathers.txt");
    System.out.println(Files.getOwner(path).getName());
    // Change owner of file
    UserPrincipal owner = path.getFileSystem()
        .getUserPrincipalLookupService().lookupPrincipalByName("jane");
    Files.setOwner(path, owner);
    // Output the updated owner information
    System.out.println(Files.getOwner(path).getName());
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The first set of lines reads the owner of the file and outputs the name of the user. The
second set of lines retrieves a user named `jane` within the related file system and uses it to
set a new owner for the file. Finally, we read the file owner name again to verify that is has
been updated.

### Improving Access with Views

Up until now, we have been accessing individual file attributes with single method calls.
While this is functionally correct, there are often costs associated with accessing the
file that make it far more efficient to retrieve all file metadata attributes in a single call.
Furthermore, some attributes are file system specific and cannot be easily generalized for all
file systems.

The NIO.2 API addresses both of these concerns by allowing you to construct views
for various file systems in a single method call. A view is a group of related attributes for a
particular file system type. A file may support multiple views, allowing you to retrieve and
update various sets of information about the file.

If you need to read multiple attributes of a file or directory at a time, the performance
advantage of using a view may be substantial. Although more attributes are read than in
a single method call, there are fewer round-trips between Java and the operating system,
whereas reading the same attributes with the previously described single method calls would
require many such trips. In practice, the number of trips between Java and the operating
system is more important in determining performance than the number of attributes read.

That’s not to say that the single method calls we just finished discussing do not have
their applications. If you only need to read exactly one file attribute, then there is little or
no performance difference. They also tend to be more convenient to use given their concise
nature.

#### Understanding Views

To request a view, you need to provide both a path to the file or a directory whose information
you want to read, as well as a class object, which tells the NIO.2 API method which
type of view you would like returned.

The Files API includes two sets of methods of analogous classes for accessing view
information. The first method, `Files.readAttributes()`, returns a read-only view of the
file attributes. The second method, `Files.getFileAttributeView()`, returns the underlying
attribute view, and it provides a direct resource for modifying file information.

Both of these methods can throw a checked `IOException`, such as when the view class
type is unsupported. For example, trying to read Windows-based attributes within a Linux
file system may throw an `UnsupportedOperationException`.

Table 5.4 lists the commonly used attributes and view classes; note that the first
row is required knowledge for the exam. The DOS and POSIX classes are useful for
reading and modifying operating system–specific properties. They also both inherit
from their respective attribute and view classes. For example, `PosixFileAttributes`
inherits from `BasicFileAttributes`, just as `DosFileAttributeView` inherits from
BasicFileAttributeView, meaning that all of the operations available on the parent class
are available in the respective subclasses.

TABLE 5.4 The attributes and view classes

|Attributes Class |View Class |Description|
|---|---|---|
|BasicFileAttributes |BasicFileAttributeView |Basic set of attributes supported by all file systems|
|DosFileAttributes |DosFileAttributeView |Attributes supported by DOS/Windows-based systems|
|PosixFileAttributes |PosixFileAttributeView |Attributes supported by POSIX systems, such as UNIX, Linux, Mac, and so on|

For the exam, you should be familiar with the `BasicFileAttributes` and
`BasicFileAttributeView` classes and their common methods, such as `creationTime()`,
`lastModifiedTime()`, and so forth. You do not need to memorize the methods available to
the `DosFile` and `PosixFile` classes for the exam, although you should be aware that they
exist in case you come across them.

#### Reading Attributes

The NIO.2 API provides a `Files.readAttributes(Path,Class<A>)` method, which returns
read-only versions of a file view. The second parameter uses generics such that the return
type of the method will be an instance of the provided class.

##### `BasicFileAttributes`

All attributes classes extend from `BasicFileAttributes`; therefore it contains attributes
common to all supported file systems. It includes many of the file attributes that you previously
saw as single-line method calls in the `Files` class, such as `Files.isDirectory()`,
`Files.getLastModifiedTime()`, and so on.

````
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class BasicFileAttributesSample {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/turtles/sea.txt");
        BasicFileAttributes data = Files.readAttributes(path,
            BasicFileAttributes.class);
        System.out.println("Is path a directory? "+data.isDirectory());
        System.out.println("Is path a regular file? "+data.isRegularFile());
        System.out.println("Is path a symbolic link? "+data.isSymbolicLink());
        System.out.println("Path not a file, directory, nor symbolic link? "+
            data.isOther());
        System.out.println("Size (in bytes): "+data.size());
        System.out.println("Creation date/time: "+data.creationTime());
        System.out.println("Last modified date/time: "+data.lastModifiedTime());
        System.out.println("Last accessed date/time: "+data.lastAccessTime());
        System.out.println("Unique file identifier (if available): "+
            data.fileKey());
    }
}
````

The majority of these attributes should be familiar to you, as they were covered in the
previous section of this chapter. The only ones that are new are `isOther()`, `lastAccessTime()`,
`creationTime()`, and `fileKey()`. The `isOther()` method is used to check for paths that are not
files, directories, or symbolic links, such as paths that refer to resources or devices in some file
systems. The `lastAccessTime()` and `creationTime()` methods return other date/time information
about the file. The `fileKey()` method returns a file system value that represents a unique
identifier for the file within the file system or `null` if it is not supported by the file system.

#### Modifying Attributes

While the `Files.readAttributes()` method is useful for reading file data, it does not
provide a direct mechanism for modifying file attributes. The NIO.2 API provides the
`Files.getFileAttributeView(Path,Class<V>)` method, which returns a view object that
we can use to update the file system–dependent attributes. We can also use the view object
to read the associated file system attributes by calling `readAttributes()` on the view object.

##### `BasicFileAttributeView`

`BasicFileAttributeView` is used to modify a file’s set of date/time values. In general, we
cannot modify the other basic attributes directly, since this would change the property of
the file system object. For example, we cannot set a property to change a directory into
a file, since this leaves the files in the future in an ambiguous state. Likewise, we cannot
change the size of the object without modifying its contents.

This sample application reads a file’s basic attributes and increments the file’s last-modified 
date/time values by 10,000 milliseconds, or 10 seconds:

````
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.*;

public class BasicFileAttributeViewSample {
    public static void main(String[] args) throws IOException {
        Path path = Paths.get("/turtles/sea.txt");
        BasicFileAttributeView view =
            Files.getFileAttributeView(path,BasicFileAttributeView.class);
        BasicFileAttributes data = view.readAttributes();
        FileTime lastModifiedTime = FileTime.fromMillis(
            data.lastModifiedTime().toMillis()+10_000);
        view.setTimes(lastModifiedTime,null,null);
    }
}
````

Notice that although we called `Files.getFileAttributeView()`, we were still able to
retrieve a `BasicFileAttributes` object by calling `readAttributes(`) on the resulting view.
Since there is only one update method, `setTimes(FileTime lastModifiedTime, FileTime
lastAccessTime, FileTime createTime)` in the `BasicFileAttributeView` class, and it
takes three arguments, we need to pass three values to the method.

The NIO.2 API allows us to pass `null` for any date/time value that we do not wish to
modify. For example, the following line of code would change only the last-modified
date/time, leaving the other file date/time values unaffected:

`view.setTimes(lastModifiedTime,null,null);`

## <a name="5-3"></a> 5.3 - Use Stream API with Files

### Conceptualizing Directory Walking

Before delving into the new NIO.2 stream methods, let’s review some basic concepts about
file systems. A directory is organized in a hierarchical manner, can contain files and other
directories, which can in turn contain other files and directories. Every record in a file system
has exactly one parent, with the exception of the root directory, which sits atop everything.

This is commonly visualized as a tree with a single root node and many branches and
leaves, as shown in Figure 5.2.

![Figure 5.2](img/figure5-2.png)

A common task in a file system is to iterate over the descendants of a particular file path,
either recording information about them or, more commonly, filtering them for a specific
set of files. For example, you may want to search a folder and print a list of all of the `.java`
files. Furthermore, file systems store file records in a hierarchical manner. Generally speaking,
if you want to search for a file, you have to start with a parent directory, read its child
elements, then read their children, and so on.

Walking or traversing a directory is the process by which you start with a parent
directory and iterate over all of its descendants until some condition is met or there are no
more elements over which to iterate. The starting path is usually a relevant directory to the
application; after all, it would be time consuming to search the entire file system if your
application uses only a single directory!

#### Selecting a Search Strategy

There are two common strategies associated with walking a directory tree: a _depth-first_
search and a _breadth-first_ search. A depth-first search traverses the structure from the root
to an arbitrary leaf and then navigates back up toward the root, traversing fully down any
paths it skipped along the way. The search depth is the distance from the root to current
node. For performance reasons, some processes have a maximum search depth that is used
to limit how many levels deep the search goes before stopping.

Alternatively, a breadth-first search starts at the root and processes all elements of each
particular depth, or distance from the root, before proceeding to the next depth level. The results
are ordered by depth, with all nodes at depth 1 read before all nodes at depth 2, and so on.

For the exam, you don’t have to understand the details of each search strategy that Java
employs; you just need to be aware that the Streams API uses depth-first searching with a
default maximum depth of `Integer.MAX_VALUE`.

### Walking a Directory

Java 8 includes a new Streams API for performing complex operations in a single line of code using 
functional programming and lambda expressions. The first newly added NIO.2 stream-based method that 
we will cover is one used to traverse a directory. The `Files.walk(path)` method returns a `Stream<Path>` 
object that traverses the directory in a depth-first, lazy manner. By lazy, we mean the set of 
elements is built and read while the directory is being traversed. For example, until a specific 
subdirectory is reached, its child elements are not loaded. This performance enhancement allows 
the process to be run on directories with a large number of descendants in a reasonable manner.

_Note: Keep in mind that when you create a `Stream<Path>` object using `Files.walk()`, the contents of 
the directory have not yet been traversed._

The following is an example of using a stream to walk a directory structure:

````
Path path = Paths.get("/bigcats");
try {
    Files.walk(path)
        .filter(p -> p.toString().endsWith(".java"))
        .forEach(System.out::println);
} catch (IOException e) {
    // Handle file I/O exception...
}
````

This example iterates over a directory and outputs all of the files that end with a java
extension. You can see that the method also throws a somewhat expected `IOException`, as
there could be a problem reading the underlying file system. Sample output for this method
would be similar to the following:

````
/bigcats/version1/backup/Lion.java
/bigcats/version1/Lion.java
/bigcats/version1/Tiger.java
/bigcats/Lion.java
````

By default, the method iterates up to `Integer.MAX_VALUE` directories deep, although
there is an overloaded version of `walk(Path,int)` that takes a maximum directory depth
integer value as the second parameter. A value of 0 indicates the current path record itself.
In the previous example, you would need to specify a value of at least 1 to print any child
record. In practice, you may want to set a limit to prevent your application from searching
too deeply on a large directory structure and taking too much time.

You see that the `Stream<Path>` object returned by the `walk()` method visits every
descendant path, with the filter being applied as each path is encountered.

#### Avoiding Circular `Paths`

Unlike our earlier NIO.2 methods, the `walk()` method will not traverse symbolic links by
default. Following symbolic links could result in a directory tree that includes other, seemingly
unrelated directories in the search. For example, a symbolic link to the root directory
in a subdirectory means that every file in the system may be traversed.

Worse yet, symbolic links could lead to a cycle. A cycle is an infinite circular dependency
in which an entry in a directory is an ancestor of the directory, resulting in an infinite loop.

If you have a situation where you need to change the default behavior and traverse
symbolic links, NIO.2 offers the `FOLLOW_LINKS` option as a vararg to the `walk()` method.
It is recommended to specify an appropriate depth limit when this option is used. Also, be
aware that when this option is used, the `walk()` method will track the paths it has visited,
throwing a `FileSystemLoopException` if a cycle is detected.

### Searching a Directory

In the previous example, we applied a filter to the `Stream<Path>` object to filter the
results, although the NIO.2 API provides a more direct method. The
`Files.find(Path,int,BiPredicate)` method behaves in a similar manner as the
`Files.walk()` method, except that it requires the depth value to be explicitly set along
with a `BiPredicate` to filter the data. Like `walk()`, `find()` also supports the `FOLLOW_LINK`
vararg option.

A `BiPredicate` is an interface that takes two generic objects and returns a boolean value of the 
form `(T, U) -> boolean`. In this case, the two object types are `Path` and `BasicFileAttributes`. 
In this manner, the NIO.2 automatically loads the `BasicFileAttributes` object for you, allowing you 
to write complex lambda expressions that have direct access to this object.

````
Path path = Paths.get("/bigcats");
long dateFilter = 1420070400000l;
try {
    Stream<Path> stream = Files.find(path, 10,
        (p,a) -> p.toString().endsWith(".java")
            && a.lastModifiedTime().toMillis()>dateFilter);
    stream.forEach(System.out::println);
} catch (Exception e) {
    // Handle file I/O exception...
}
````

This example is similar to our previous `Files.walk()` example in that it will search
a directory for files that end with the `.java` extension. It is more advanced, though, in
that it applies a last-modified-time filter using the `BasicFileAttributes` object. Finally,
it sets the directory depth limit for search to 10, as opposed to relying on the default
`Integer.MAX_VALUE` value that the `Files.walk()` method uses.

#### Listing Directory Contents

Although you could use the `Files.walk()` method with a maximum depth limit of 1 to perform this 
same task, the NIO.2 API includes a new stream method, `Files.list(Path)`, that does this for you.

````
/zoo:
try {
    Path path = Paths.get("ducks");
    Files.list(path)
        .filter(p -> !Files.isDirectory(p))
        .map(p -> p.toAbsolutePath())
        .forEach(System.out::println);
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The code snippet iterates over a directory, outputting the full path of the files that it
contains. Depending on the contents of the file system, the output might look something
like the following:

````
/zoo/ducks/food.txt
/zoo/ducks/food-backup.txt
/zoo/ducks/weight.txt
````

Contrast this method with the `Files.walk()` method, which traverses all subdirectories.
For the exam, you should be aware that `Files.list()` searches one level deep and is analogous
to `java.io.File.listFiles()`, except that it relies on streams.

### Printing `File` Contents

Earlier in the chapter, we presented `Files.readAllLines()` and commented that using it to
read a very large file could result in an `OutOfMemoryError` problem. Luckily, the NIO.2 API in
Java 8 includes a `Files.lines(Path)` method that returns a `Stream<String>` object and
does not suffer from this same issue. The contents of the file are read and processed lazily,
which means that only a small portion of the file is stored in memory at any given time.
We now present `Files.lines()`, which is equivalent to the previous `Files.readAllLines()`
sample code:

````
Path path = Paths.get("/fish/sharks.log");
try {
    Files.lines(path).forEach(System.out::println);
} catch (IOException e) {
    // Handle file I/O exception...
}
````

The first thing you may notice is that this example is a lot shorter, accomplishing in a
single line what took multiple lines earlier. It is also more performant on large files, since it
does not require the entire file to be read and stored in memory.

Taking things one step further, we can leverage other stream methods for a more powerful
example:

````
Path path = Paths.get("/fish/sharks.log");
try {
    System.out.println(Files.lines(path)
        .filter(s -> s.startsWith("WARN "))
        .map(s -> s.substring(5))
        .collect(Collectors.toList()));
} catch (IOException e) {
    // Handle file I/O exception...
}
````

This sample code now searches for lines in the file that start with `WARN`, outputting
everything after it to a single list that is printed to the user. You can see that lambda
expressions coupled with NIO.2 allow us to perform very complex file operations concisely.
Assuming that the input file `sharks.log` is as follows,

````
INFO Server starting
DEBUG Processes available = 10
WARN No database could be detected
DEBUG Processes available reset to 0
WARN Performing manual recovery
INFO Server successfully started
````

then the sample output would be the following:

`[No database could be detected, Performing manual recovery]`

##### `Files.readAllLines()` vs. `Files.lines()`

For the exam, you should be familiar with both `readAllLines()` and `lines()` and with
which one returns a `List` and which one returns a Stream. This is even more difficult
since the `forEach()` method can be called on both Stream and Collection objects. For
example, both of the following lines compile and run without issue:

````
Files.readAllLines(Paths.get("birds.txt")).forEach(System.out::println);
Files.lines(Paths.get("birds.txt")).forEach(System.out::println);
````

The first code snippet reads the entire file into memory and then performs a print
operation on the resulting object. The second code snippet reads the lines lazily and
prints them as they are being read. The advantage of the second code snippet is that it
does not require the entire file to be stored in memory as it is being read.

You should also be aware of when they are mixing incompatible types on the exam. For
example, can you determine which of the following two lines compiles?

````
Files.readAllLines(path).filter(s -> s.length()>2).forEach(System.out::println);
Files.lines(path).filter(s -> s.length()>2).forEach(System.out::println);
````

The first line does not compile because the `filter()` operation cannot be applied to a
Collection without first converting it to a Stream using the `stream()` method.

## <a name="q"></a>Quiz

1. <a name="q1"></a>What is the output of the following code?
    ````
   Path path = Path.get("/user/.././root","../kodiacbear.txt");
   path.normalize().relativize("/lion");
   System.out.println(path);
   ````
    - A. `/user/.././root/../kodiacbear.txt`
    - B. `/user/./root/kodiacbear.txt/lion`
    - C. `/kodiacbear.txt`
    - D. `kodiacbear.txt`
    - E. `../lion`
    - F. The code does not compile.
<br />[Jump to answer](#qa1)
2. <a name="q2"></a>For which values of path inserted on the blank line would it be possible for the 
following code to output Success? (Choose all that apply.)
    ````
   Path path = ____________________;
   if(Files.isDirectory(path))
   System.out.println(Files.deleteIfExists(path) ? "Success": "Try Again");
   ````
   - A. path refers to a regular file in the file system.
   - B. path refers to a symbolic link in the file system.
   - C. path refers to an empty directory in the file system.
   - D. path refers to a directory with content in the file system.
   - E. path does not refer to a record that exists within the file system.
   - F. The code does not compile.
<br />[Jump to answer](#qa2)
3. <a name="q3"></a>What is the result of executing the following code? (Choose all that apply.)
    ````
   1: Path path = Paths.get("sloth.schedule");
   2: BasicFileAttributes attributes = Files.readAttributes(path, BasicFileAttributes.class);
   3: if(attributes.size()>0 && attributes.creationTime().toMillis()>0) {
   4:   attributes.setTimes(null,null,null);
   5: }
   ````
   - A. It compiles and runs without issue.
   - B. The code will not compile because of line 2.
   - C. The code will not compile because of line 3.
   - D. The code will not compile because of line 4.
   - E. The code compiles but throws an exception at runtime.
<br />[Jump to answer](#qa3)
4. <a name="q4"></a>If the current working directory is /user/home, then what is the output of the 
following code?
    ````
   Path path = Paths.get("/zoo/animals/bear/koala/food.txt");
   System.out.println(path.subpath(1,3).getName(1).toAbsolutePath());
   ````
   - A. `animals/bear`
   - B. `koala`
   - C. `/user/home/bear`
   - D. `/user/home/koala/koala`
   - E. `/user/home/food.txt`
   - F. `/user/home/koala/food.txt`
   - G. The code does not compile.
<br />[Jump to answer](#qa4)
5. <a name="q5"></a>Assume `/kang` exists as a symbolic link to the directory `/mammal/kangaroo` within 
the file system. Which of the following statements are correct about this code snippet? (Choose all
that apply.)
    ````
   Path path = Paths.get("/kang");
   if(Files.isDirectory(path) && Files.isSymbolicLink(path))
        Files.createDirectory(path.resolve("joey"));
   ````
   - A. A new directory will always be created.
   - B. A new directory will be created only if `/mammal/kangaroo` exists.
   - C. If the code creates a directory, it will be reachable at `/kang/joey`.
   - D. If the code creates a directory, it will be reachable at `/mammal/kangaroo/joey`.
   - E. The code does not compile.
   - F. The code will compile but always throws an exception at runtime.
<br />[Jump to answer](#qa5)
6. <a name="q6"></a>Given that /animals is a directory that exists and it is empty, what is the result 
of the following code?
    ````
   Path path = Paths.get("/animals");
   boolean myBoolean = Files.walk(path)
       .filter((p,a) -> a.isDirectory() && !path.equals(p)) // w1
       .findFirst().isPresent(); // w2
   System.out.println(myBoolean ? "No Sub-directory": "Has Sub-directory");
   ````
   - A. It prints N`o Sub-directory`.
   - B. It prints `Has Sub-directory`.
   - C. The code will not compile because of line w1.
   - D. The code will not compile because of line w2.
   - E. The output cannot be determined.
   - F. It produces an infinite loop at runtime.
<br />[Jump to answer](#qa6)
7. <a name="q7"></a>If the current working directory is `/zoo`, and the path `/zoo/turkey` does not exist, 
then what is the result of executing the following code? (Choose all that apply.)
    ````
   Path path = Paths.get("turkey");
   if(Files.isSameFile(path,Paths.get("/zoo/turkey"))) // x1
        Files.createDirectory(path.resolve("info")); // x2
   ````
   - A. The code compiles and runs without issue, but it does not create any directories.
   - B. The directory `/zoo/turkey` is created.
   - C. The directory `/zoo/turkey/info` is created.
   - D. The code will not compile because of line x1.
   - E. The code will not compile because of line x2.
   - F. It compiles but throws an exception at runtime.
<br />[Jump to answer](#qa7)  
8. <a name="q8"></a>What is the output of the following code?
    ````
   Path path1 = Paths.get("/pets/../cat.txt");
   Path path2 = Paths.get("./dog.txt");
   System.out.println(path1.resolve(path2));
   System.out.println(path2.resolve(path1));
   ````
   - A. 
   ````
   /pets/../cat.txt/./dog.txt
   /pets/../cat.txt
   ````
   - B. 
   ````
   /pets/../cat.txt/./dog.txt
   ./dog.txt/pets/../cat.txt
   ````
   - C. 
   ````
   /cats.txt
   /dog.txt
   ````
   - D. 
   ````
   /cats.txt/dog.txt
   /cat.txt
   ````
   - E. It compiles but throws an exception at runtime.
<br />[Jump to answer](#qa8)
9. <a name="q9"></a>What are some advantages of using Files.lines() over Files.readAllLines()? (Choose 
all that apply.)
    - A. It is often faster.
    - B. It can be run on large files with very little memory available.
    - C. It can be chained with stream methods directly.
    - D. It does not modify the contents of the file.
    - E. It ensures the file is not read-locked by the file system.
    - F. There are no differences, because one method is a pointer to the other.
<br />[Jump to answer](#qa9)
10. <a name="q10"></a>What is correct about the following code snippet? (Choose all that apply.)
    ````
    Files.move(Paths.get("monkey.txt"), Paths.get("/animals"),
        StandardCopyOption.ATOMIC_MOVE,
        LinkOption.NOFOLLOW_LINKS);
    ````
    - A. If `/animals` exists, it will be overwritten at runtime.
    - B. If `monkey.txt` is a symbolic link, the file it points to will be moved at runtime.
    - C. If another process is monitoring the file system, it will not see an incomplete file at
    runtime.
    - D. The code will always throw an exception, since no filename is specified in the target
    - folder path.
    - E. The metadata of the `monkey.txt` will be moved along with the file.
<br />[Jump to answer](#qa10)
11. <a name="q11"></a>For the `copy()` method shown here, assume that the source exists as regular 
file and that the target does not. What is the result of the following code?
    ````
    Path path1 = Paths.get("./goat.txt").normalize(); // k1
    Path path2 = Paths.get("mule.png");
    Files.copy(path1,path2,StandardCopyOption.COPY_ATTRIBUTES); //k2
    System.out.println(Files.isSameFile(path1, path2)); //k3
    ````
    - A. It will output false.
    - B. It will output true.
    - C. It does not compile because of line k1.
    - D. It does not compile because of line k2.
    - E. It does not compile because of line k3.
    - F. It compiles but throws an exception at runtime.
<br />[Jump to answer](#qa11)    
12. <a name="q12"></a>Which of the following methods cannot be used to obtain a `Path` instance? (Choose 
all that apply.)
    - A. `new Path("jaguar.txt")`
    - B. `FileSystems.getDefault().getPath("puma.txt")`
    - C. `Paths.get(new URI("cheetah.txt"))`
    - D. `Paths.get("cats","lynx.txt")`
    - E. `new java.io.File("tiger.txt").toPath()`
    - F. `new FileSystem().getPath("leopard")`
    - G. `Paths.getPath("ocelot.txt")`
<br />[Jump to answer](#qa12)
13. <a name="q13"></a>Assume `/monkeys` exists as a regular directory containing multiple files, symbolic 
links, and subdirectories. What is true about the following code? (Choose all that apply.)
    ````
    Path path = Paths.get("/monkeys");
    Files.find(path, 0, (p,a) -> a.isSymbolicLink()).map(p -> p.toString()) // y1
        .collect(Collectors.toList()) // y2
        .stream() // y3
        .filter(x -> x.toString().endsWith(".txt")) // y4
        .forEach(System.out::println);
    ````
    - A. It will print all symbolic links in the directory tree ending in `.txt`.
    - B. It will print nothing.
    - C. It does not compile because of line y1.
    - D. It does not compile because of line y2.
    - E. It does not compile because of line y3.
    - F. It does not compile because of line y4.
    - G. It compiles but throws an exception at runtime.
<br />[Jump to answer](#qa13)
14. <a name="q14"></a>Which NIO.2 method is most similar to the legacy `java.io.File.listFiles()` method?
    - A. `Path.listFiles()`
    - B. `Files.walk()`
    - C. `Files.find()`
    - D. `Files.files()`
    - E. `Files.list()`
    - F. `Files.lines()`
<br />[Jump to answer](#qa14)   
15. <a name="q15"></a>Assuming `/squid/food-schedule.csv` exists as a regular non-empty file that a program 
has access to read, what is correct about the following code snippet? (Choose all that apply.)
    ````
    Path path = Paths.get("/squid/food-schedule.csv");
    Files.lines(path) // r1
        .flatMap(p -> Stream.of(p.split(","))) // r2
        .map(s -> s.toUpperCase()) // r3
        .forEach(System.out::println);
    ````
    - A. It compiles but may throw an exception at runtime.
    - B. The code will not compile because of line r1.
    - C. The code will not compile because of line r2.
    - D. The code will not compile because of line r3.
    - E. It may not print anything at runtime.
    - F. If it prints anything, it will not include commas.
<br />[Jump to answer](#qa15)
16. <a name="q16"></a>Assuming the current directory is `/animals/cute`, which are possible results of 
executing the following code? (Choose all that apply.)
    ````
    Files.walk(Paths.get("..").toRealPath().getParent()) // u1
        .map(p -> p.toAbsolutePath().toString()) // u2
        .filter(s -> s.endsWith(".java")) // u3
        .collect(Collectors.toList())
        .forEach(System.out::println);
    ````
    - A. It compiles but may throw an exception at runtime.
    - B. The code will not compile because of line u1.
    - C. The code will not compile because of line u2.
    - D. The code will not compile because of line u3.
    - E. It prints all `.java` files in the `/animals` directory tree.
    - F. It prints all `.java` files in the `/animals/cute` directory tree.
    - G. It prints all `.java` files in the root directory tree.
<br />[Jump to answer](#qa16)
17. <a name="q17"></a>Assuming the directories and files referenced here all exist and are accessible 
within the file system, what is the result of the following code?
    ````
    Path path1 = Paths.get("/lizard/./").resolve(Paths.get("walking.txt"));
    Path path2 = new File("/lizard/././actions/../walking.txt").toPath();
    
    System.out.print(Files.isSameFile(path1,path2));
    System.out.print(" "+path1.equals(path2));
    System.out.print(" "+path1.normalize().equals(path2.normalize()));
    ````
    - A. `true true true`
    - B. `false false false`
    - C. `false true false`
    - D. `true false true`
    - E. `true false false`
    - F. The code does not compile.
<br />[Jump to answer](#qa17)
18. <a name="q18"></a>Assuming the current directory is `/seals/harp/food`, what is the result of 
executing the following code?
    ````
    final Path path = Paths.get(".").normalize(); // h1
    int count = 0;
    for(int i=0; i<path.getNameCount(); ++i) {
        count++;
    }
    System.out.println(count);
    ````
    - A. 0
    - B. 1
    - C. 2
    - D. 3
    - E. 4
    - F. The code throws a runtime exception because of line h1.
<br />[Jump to answer](#qa18)

## <a name="qa"></a>Quiz Answers 4?

1. <a name="qa1"></a>[Jump to question](#q1) - **F.** The code snippet will not compile due to a bug 
on the first and second lines. The first line should use `Paths.get()`, because there is no method 
`Path.get()`. The second line passes a `String` to `relativize()` instead of a `Path` object. If both 
lines were corrected to use `Paths.get()`, then the correct answer would be A. Remember that the `normalize()`
method, like most methods in the `Path` interface, does not modify the `Path` object,
but instead it returns a new `Path` object. If it was corrected to reassign the new value to the
existing path variable, then E would be correct.
2. <a name="qa2"></a>[Jump to question](#q2) - **B, C.** The code snippet compiles without issue, so 
F is incorrect. If the value refers to a regular file, `isDirectory()` will return false and the 
statement will be skipped, so A is incorrect. Likewise, if the directory does not exist, the method 
also returns false, so E is also incorrect. A symbolic link can point to a real directory, and by 
default `isDirectory()` follows links, so B is possible. In this case, the symbolic link, not the 
directory, would be deleted. C is also possible and is the simple case of deleting an empty directory. 
D would allow the code to reach the execution block of the `if`/then statement, but the method
`deleteIfExists()` would throw a `DirectoryNotEmptyException` if it had contents.
3. <a name="qa3"></a>[Jump to question](#q3) - **D.** The `setTimes()` method is available only on 
`BasicFileAttributeView`, not the readonly `BasicFileAttributes` class, so line 4 will not compile and 
D is correct. You need to retrieve an instance of the view class to update the data. The rest of the 
lines compile without issue and only D is correct.
4. <a name="qa4"></a>[Jump to question](#q4) - **C.** First off, the code compiles without issue, so 
G is incorrect. Let’s take this one step at a time. First, the `subpath()` method is applied to the 
absolute path, which returns the relative path `animals/bear`. Next, the `getName()` method is applied 
to the relative path, and since this is indexed from zero, it returns the relative path `bear`. Finally, 
the `toAbsolutePath()` method is applied to the relative path `bear`, resulting in the current directory 
being incorporated into the path. The final output is the absolute path `/user/home/bear`, so C is correct.
5. <a name="qa5"></a>[Jump to question](#q5) - **B, C, D.** The first clause of the `if`/then statement 
will be true only if the target of the symbolic link, `/mammal/kangaroo`, exists, since by default 
`isDirectory()` follows symbolic links, so B is correct. Option A is incorrect because `/mammal/kangaroo` 
may not exist or `/mammal/kangaroo/joey` may already exist. If `/mammal/kangaroo` does exist, then the
directory will be created at `/mammal/kangaroo/joey`, and because the symbolic link would
be accessible as `/kang/joey`, C and D are both correct. E is incorrect, because the code
compiles without issue. F is incorrect because the code may throw an exception at runtime,
such as when the file system is unavailable or locked for usage; thus it is not guaranteed to
throw an exception at runtime.
6. <a name="qa6"></a>[Jump to question](#q6) - **C.** The code does not compile since the stream output 
by `Files.walk()` is `Stream<Path>`, therefore we need a `Predictate`, not a `BiPredicate`, on line w1, and 
the answer is C. If the `Files.find()` method had been used instead, and the lambda had been passed as 
an argument to the method instead of on `filter()`, the output would be B, `Has Sub-directory`,
since the directory is given to be empty. For fun, we reversed the expected output of the
ternary operation to make sure that you understood the process.
7. <a name="qa7"></a>[Jump to question](#q7) - **F.** The code compiles without issue, so D and E are 
incorrect. The method `Files.isSameFile()` first checks to see if the `Path` values are the same in 
terms of `equals()`. Since the first path is relative and the second path is absolute, this comparison 
will return false, forcing `isSameFile()` to check for the existence of both paths in the file system. 
Since we know `/zoo/turkey` does not exist, a `NoSuchFileException` is thrown and F is the correct
answer. A, B, and C are incorrect since an exception is thrown at runtime.
8. <a name="qa8"></a>[Jump to question](#q8) - **A.** The code compiles and runs without issue, so E 
is incorrect. For this question, you have to remember two things. First, the `resolve()` method does 
not normalize any path symbols, so C and D are not correct. Second, calling `resolve()` with an absolute 
path as a parameter returns the absolute path, so A is correct and B is incorrect.
9. <a name="qa9"></a>[Jump to question](#q9) - **B, C.** The methods are not the same, because `Files.lines()` 
returns a `Stream<Path>` and `Files.readAllLines()` returns a `List<String>`, so F is incorrect. A is incorrect, 
because performance is not often the reason to prefer one to the other. `Files.lines()` reads the
file in a lazy manner, while `Files.readAllLines()` reads the entire file into memory all at
once; therefore `Files.lines()` works better on large files with limited memory available,
and B is correct. Although a List can be converted to a stream with the `stream()` method,
this requires an extra step; therefore C is correct since the resulting object can be chained
directly to a stream. Finally, D and E are incorrect because they are not relevant to these
methods.
10. <a name="qa10"></a>[Jump to question](#q10) - **C, E.** The `REPLACE_EXISTING` flag was not provided, 
so if the target exists, it will throw an exception at runtime and A is incorrect. Next, the `NOFOLLOW_LINKS` 
option means that if the source is a symbolic link, the link itself and not the target will be copied at runtime,
so B is also incorrect. The option `ATOMIC_MOVE` means that any process monitoring the file
system will not see an incomplete file during the move, so C is correct. D is incorrect, since
you could rename a file not to have an extension. Note that in this example, if `monkey.txt`
is a file, then the resulting `/animals` would be a file, not a directory. Likewise, if the source
is a directory, the result would also be a directory. E is correct, because moving always preserves
the metadata even if the `COPY_ATTRIBUTES` flag is not set.
11. <a name="qa11"></a>[Jump to question](#q11) - **A.** The code compiles and runs without issue, so 
C, D, E, and F are incorrect. Even though the file is copied with attributes preserved, the file is 
considered a separate file, so the output is false and A is correct and B is incorrect. Remember, 
`isSameFile()` returns true only if the files pointed to in the file system are the same, without regard 
to the file contents.
12. <a name="qa12"></a>[Jump to question](#q12) - **A, F.** For this question, you need to rule out the 
answers that can be used to obtain a `Path` instance. D and G both use the `Paths.get()` method, one with 
optional vararg values. C uses an overloaded version of `Paths.get()` that takes a `URI`. B is a longer 
form for getting a `Path` using a specific file system, in this case the default file system. Finally, 
E uses a method added to `java.io.File` to make it easily compatible with `Path`. The remaining
choices A and F are the correct ones, because they call constructors on `Path` and `FileSystem`,
respectively, instead of using the underlying factory methods. The rest are invalid since
they do not use the factory methods to gain access to instances.
13. <a name="qa13"></a>[Jump to question](#q13) - **B.** The code compiles and runs without issue, 
so C, D, E, F, and G are incorrect. Note that the sample code creates a stream, collects it as a list, 
and then converts it back to a stream before outputting the filenames. The key here is that the depth 
parameter specified as the second argument to `find()` is `0`, meaning the only record that will be searched 
is the toplevel directory. Since we know that the top directory is regular and not a symbolic link, no
other paths will be visited and nothing will be printed. For these reasons, B is the correct
answer and A is incorrect.
14. <a name="qa14"></a>[Jump to question](#q14) - **E.** First off, recall that the `java.io.File.listFiles()` 
method retrieves the members of the current directory without traversing any subdirectories. The methods 
`Path.listFiles()` and `Files.files()` do not exist, so A and D are incorrect. `Files.walk()` and
`Files.find()` recursively traverse a directory tree rather than list the contents of the
current directory; therefore, they are not a close match, and B and C are incorrect. Note
that you could use these methods to perform the same operation if you set the depth limit
to 1 and used a lambda with `Files.find()` that always returns true, but the question
was about which method is most similar to `java.io.File.listFiles()`. In that regard,
`Files.list()` is the closest match since it always reads only a single directory, and E is
correct. Note that instead of an array, a stream of `Path` values is returned. Finally, F is
incorrect because it reads the contents of a file, not a directory.
15. <a name="qa15"></a>[Jump to question](#q15) - **F.** The code compiles without issue, so B, C, and 
D are incorrect. The code snippet breaks a file into lines and then further separates the lines by commas 
using the `flatMap()` method. The result is printed with one entry on a single line, but all original 
line breaks and commas from the file are removed; therefore F is correct. Since we are told that the 
file is non-empty and regular, and the program has access to read it, A and E are incorrect.
16. <a name="qa16"></a>[Jump to question](#q16) - **A, G.** The code compiles without issue, so B, C, 
and D are incorrect. The first line actually resolves to the root path since `..` and `getParent()` 
are conceptually equivalent. Therefore, G is correct and E and F are incorrect. A is also correct since 
it may encounter a file that it does not have access to read, which is common when trying to read an 
entire file system.
17. <a name="qa17"></a>[Jump to question](#q17) - **D.** The code compiles and runs without issue, 
so F is incorrect. The one thing to notice about these paths is that they represent the same path 
within the file system. Therefore, `isSameFile()` would return true and B and C are incorrect. The 
second output is false, because `Path.equals()` does not resolve the path within the file system, so A 
is incorrect. Finally, the normalized paths are `equals()`, since all extra symbols have been removed;
therefore D is correct and E is incorrect.
18. <a name="qa18"></a>[Jump to question](#q18) - **B.** The `normalize()` method does not convert a 
relative path into an absolute path; therefore, the path value after the first line is just the current 
directory symbol. The `for()` loop iterates the name values, but since there is only one entry, the 
loop terminates after a single iteration. Therefore, B is correct and the rest of the answers are incorrect.
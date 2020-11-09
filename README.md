# Java-SE11-Upgrade-Exam

Examples and notes to aid revision for the Oracle Certified Professional (OCP) Upgrade Exam for Java SE 8 to 11 (1Z0-817).

- Duration:	180 Minutes
- Number of Questions: 80
- Passing Score: 61%

<a href="https://education.oracle.com/upgrade-java-se-7-to-java-se-8-ocp-programmer/pexam_1Z0-810" ><img src="https://raw.githubusercontent.com/rysharprules/Java-SE8-Upgrade-Exam/master/ocp_logo.gif" /></a>

_This documentation is an aggregation of multiple sources and is intended as a study guide for my personal use and such, wording and examples may jump between perspectives or may appear incomplete. I make no personal gain from this other than learning._

## Exam Topics

### 1. [Understanding Modules](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part01)
- 1.1 - Describe the Modular JDK
- 1.2 - Declare modules and enable access between modules
- 1.3 - Describe how a modular project is compiled and run

### 2. [Services in a Modular Application](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part02)
- 2.1 - Describe the components of Services including directives
- 2.2 - Design a service type, load the services using ServiceLoader, check for dependencies of the services including consumer module and provider module

### 3. [Java Interfaces](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part03)
- 3.1 - Create and use methods in interfaces
- 3.2 - Define and write functional interfaces

### 4. [Lambda Operations on Streams](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part04)
- 4.1 - Extract stream data using map, peek and flatMap methods
- 4.2 - Search stream data using search findFirst, findAny, anyMatch, allMatch and noneMatch methods
- 4.3 - Use the Optional class
- 4.4 - Perform calculations using count, max, min, average and sum stream operations
- 4.5 - Sort a collection using lambda expressions
- 4.6 - Use Collectors with streams, including the groupingBy and partitioningBy operation

### 5. [Java File I/O (NIO.2)](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part05)
- 5.1 - Use Path interface to operate on file and directory paths
- 5.2 - Use Files class to check, delete, copy or move a file or directory
- 5.3 - Use Stream API with Files

### 6. [Migration to a Modular Application](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part06)
- 6.1 - Migrate the application developed using a Java version prior to SE 9 to SE 11 including top-down and bottom-up migration, splitting a Java SE 8 application into modules for migration
- 6.2 - Use jdeps to determine dependencies and identify way to address the cyclic dependencies

### 7. [Local-Variable Type Inference](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part07)
- 7.1 - Use local-variable type inference
- 7.2 - Create and use lambda expressions with local-variable type inferred parameters

### 8. [Lambda Expressions](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part08)
- 8.1 - Create and use lambda expressions
- 8.2 - Use lambda expressions and method references
- 8.3 - Use built-in functional interfaces including Predicate, Consumer, Function, and Supplier
- 8.4 - Use primitive and binary variations of base interfaces of java.util.function package

### 9. [Parallel Streams](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part09)
- 9.1 - Develop code that uses parallel streams
- 9.2 - Implement decomposition and reduction with streams

### 10 [Language Enhancements](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part10)
- 10.1 - Use try-with-resources construct
- 10.2 - Develop code that handles multiple Exception types in a single catch block

## [Extra Reading](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/tree/master/src/ocp/study/part00)

- [Command Line Cheat Sheet](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/commandCheatsheet.md)
- 0.1 - Multi-Release JAR Files
- 0.2 - Enhancements to the Stream API
- 0.3 - JShell
- 0.4 - Convenience Methods for Collections
- 0.5 - Convenience Methods for Arrays
- 0.6 - Enhanced Deprecation

## Bibliography and Resources

- [Oracle JLS - Chapter 7. Packages and Modules](https://docs.oracle.com/javase/specs/jls/se11/html/jls-7.html#jls-7.1)
- [Project Jigsaw](https://openjdk.java.net/projects/jigsaw/) 
- [`ServiceLoader` JavaDoc](https://docs.oracle.com/javase/9/docs/api/java/util/ServiceLoader.html)
- [Study Guide from java.boot.by](http://java.boot.by/ocpjd11-upgrade-guide/index.html)
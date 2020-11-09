# Command Cheat Sheet

Cheat sheet limited to what is required for the exam.

## javac

| Option                                                            | Description                                       |
|-------------------------------------------------------------------|---------------------------------------------------|
| -cp <classpath><br />  -classpath <classpath><br />  --class-path <classpath> | Location of classes needed to compile the program |
| -d <dir>                                                          | Directory to place generated class files          |
| -p <path><br /> --module-path <path>                                    | Module path                                       |
<br>

### Examples

````
javac packagea/ClassA.java packageb/ClassB.java
````

````
javac -d classes packagea/ClassA.java packageb/ClassB.java
````

````
javac -p mods
   -d feeding
   feeding/zoo/animal/feeding/Task.java
   feeding/*.java
````

## java

| Option                                                            | Description                                   |
|-------------------------------------------------------------------|-----------------------------------------------|
| -cp <classpath>  <br />-classpath <classpath>  <br />--class-path <classpath> | Location of classes needed to run the program |
| -p <path> <br />--module-path <path>                                    | Module path                                   |
| -m <name> <br />--module <name>                                         | Module name (module[/mainclass] format)                                  |
| -d <br />--describe-module                                              | Describes the module                          |
<br>

### Examples

#### Running nonmodular applications

````
java packageb.ClassB
````

````
java -cp classes packageb.ClassB
````

````
java -classpath classes packageb.ClassB
````

````
java --class-path classes packageb.ClassB
````

#### Running modular applications

````
java --module-path feeding
   --module zoo.animal.feeding/zoo.animal.feeding.Task
````

````
java -p feeding
   -m zoo.animal.feeding/zoo.animal.feeding.Task
````

#### Describe modular application

````
java -p mods
   -d zoo.animal.feeding
````

````
java -p mods
   --describe-module zoo.animal.feeding
````

Output:
````
zoo.animal.feeding file:///absolutePath/mods/zoo.animal.feeding.jar
exports zoo.animal.feeding
requires java.base mandated
````

#### List modules

````
java --list-modules 
````

Output:
````
java.base@11.0.2
java.compiler@11.0.2
java.datatransfer@11.0.2
````

````
java -p mods --list-modules
````

````
zoo.animal.care file:///absolutePath/mods/zoo.animal.care.jar
zoo.animal.feeding file:///absolutePath/mods/zoo.animal.feeding.jar
zoo.animal.talks file:///absolutePath/mods/zoo.animal.talks.jar
zoo.staff file:///absolutePath/mods/zoo.staff.jar
````

#### Module resolution

````
java --show-module-resolution
   -p feeding
   -m zoo.animal.feeding/zoo.animal.feeding.Task
````

Output:
````
root zoo.animal.feeding file:///absolutePath/feeding/
java.base binds java.desktop jrt:/java.desktop
java.base binds jdk.jartool jrt:/jdk.jartool
...
jdk.security.auth requires java.naming jrt:/java.naming
jdk.security.auth requires java.security.jgss jrt:/java.security.jgss
...
All fed!
````

## jar

| Option                          | Description                                             |
|---------------------------------|---------------------------------------------------------|
| -c <br />--create                     | Creates a new JAR file                                  |
| -v <br />--verbose                    | Prints details when working with JAR files              |
| -f <fileName> <br />--file <fileName> | JAR file name                                           |
| -C <directory>                  | Directory containing files to be used to create the JAR |
<br>

### Examples

````
jar -cvf myNewFile.jar .
````

````
jar --create --verbose --file myNewFile.jar .
````

````
jar -cvf myNewFile.jar -C dir .
````

#### Describe modular application

````
jar -f mods/zoo.animal.feeding.jar -d
````

````
jar --file mods/zoo.animal.feeding.jar --describe-module
````

Output:
````
zoo.animal.feeding file:///absolutePath/mods/zoo.animal.feeding.jar
exports zoo.animal.feeding
requires java.base mandated
````

## jdeps

`jdeps [options] path ...`

| Option                                           | Description                                              |
|--------------------------------------------------|----------------------------------------------------------|
| -dotoutput <dir> <br>--dot-output <dir>              | Specifies the destination directory for DOT file output  |
| -s -summary                                      | Prints a dependency summary only                         |
| -jdkinternals  <br>--jdk-internals                   | Finds class-level dependencies in the JDK internal APIs  |
| --module-path <module-path>                      | Specifies the module path. Note: -p is used for pkg name |
| -m <moduel name>                                 | Specifies the root module for analysis                   |
| -cp <path> <br>-classpath <path> <br>--class-path <path> | Files on the classpath                                   |
| --list-deps                                      | Lists the module dependencies                            |
<br>

### Examples

````
jdeps mods/zoo.animal.feeding.jar
````

Output:
````
[file:///absolutePath/mods/zoo.animal.feeding.jar]
   requires mandated java.base (@11.0.2)
zoo.animal.feeding -> java.base
   zoo.animal.feeding         -> java.io
      java.base
   zoo.animal.feeding         -> java.lang
      java.base
````

#### Summary

````
jdeps -s mods/zoo.animal.feeding.jar
````

````
jdeps -summary mods/zoo.animal.feeding.jar
````
 
Output:
````
zoo.animal.feeding -> java.base
````

````
jdeps -s
   --module-path mods
   mods/zoo.animal.care.jar
````

````
jdeps -summary
   --module-path mods
   mods/zoo.animal.care.jar
````

Output:
````
zoo.animal.care -> java.base
zoo.animal.care -> zoo.animal.feeding
````

## jmod

`jmod (create|extract|list|describe|hash) [options] jmod-file`

| Operation | Description                                             |
|-----------|---------------------------------------------------------|
| create    | Creates a JMOD file.                                    |
| extract   | Extracts all files from the JMOD. Works like unzipping. |
| describe  | Prints the module details such as requires.             |
| list      | Lists all files in the JMOD file.                       |
| hash      | Shows a long string that goes with the file             |
<br>

## Comparison

| Description             | Syntax                                                                                                                                                                |
|-------------------------|-----------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| Compile nonmodular code | **javac -cp** *classpath* -d *directory classesToCompile* <br/>**javac --class-path** *classpath* -d *directory classesToCompile* <br>**javac -classpath** *classpath* -d *directory classesToCompile* |
| Run nonmodular code     | **java -cp** *classpath package.className* <br>**java -classpath** *classpath package.className* <br>**java --class-path** *classpath package.className*                                      |
| Compile a module        | **javac -p** *moduleFolderName* -d *directory classesToCompileIncludingModuleInfo* <br>**javac --module-path** *moduleFolderName* -d *directory classesToCompileIncludingModuleInfo*      |
| Run a module            | **java -p** *moduleFolderName* **-m** *moduleName/package.className* <br>**java --module-path** *moduleFolderName* **--module** *moduleName/package.className*                                    |
| Describe a module       | **java -p** *moduleFolderName* **-d** *moduleName* <br>**java --module-path** *moduleFolderName* **--describe-module** *moduleName* <br>**jar** --file *jarName* **--describe-module** <br>**jar** -f *jarName* **-d**        |
| List available modules  | **java --module-path** *moduleFolderName* **--list-modules** <br>**java -p** *moduleFolderName* **--list-modules** <br>**java --list-modules**                      |
| View dependencies       | **jdeps -summary --module-path** *moduleFolderName jarName*<br>**jdeps -s --module-path** *moduleFolderName jarName*                                                                |
| Show module resolution  | **java --show-module-resolution -p** *moduleFolderName* **-m** *moduleName* <br>**java --show-module-resolution --module-path** *moduleFolderName* **--module** *moduleName*                      |
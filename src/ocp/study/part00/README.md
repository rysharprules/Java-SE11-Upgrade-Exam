# Extra Material

Material which does not map to the exam topics but may be useful nonetheless. 

- [0.1 - Custom Runtime Images](#0-1)
- [0.2 - Multi-Release JAR Files](#0-2)
- [Quiz](#q)

## <a name="0-1"></a>Custom Runtime Images

### What is a Custom Runtime Image?

You can create a special distribution of the java runtime containing only the runtime modules. 
Application modules and only those platform modules required by your application. You can do this in 
Java SE 9 with custom runtime images. A custom runtime image is a self contained image that bundles the
application modules with the JVM and everything else it needs to execute your application.

### Link Time

In Java SE 9, an optional link time is introduced between the compilation and runtime phase. A link time
requries a linking tool that will assemble and optimize a set of modules and their transitive dependencies to create
a runtime image.

### Using `jlink` to Create a Custom Runtime Image

`jlink` is a new tool in Java SE 9 that can be used to create a custom platform specific runtime image,
assemble a set of modules form their dependencies (using a set of dependencies from `module-info.class`),
and performing optimization.

A basic invocation of `jlink`:

`jlink [options] --module-path modulepath --add-modules mods --output path`

You will have to specify the following three parameters:

1. `modulepath`: The module path where the platform and application modules added to be added to the image are located. Modules can be modular jar files, jmods or 
exploded directories.
1. `mods`: The list of the modules to be added to the adds these modules and their transitive dependencies into your image.
1. `path`: The output directory where the generated runtime image will be stored

#### Example: Using `jlink` to Create a Runtime Image

The following command creates a new runtime image:

````
/Hello$ jlink
    --module-path dist/Hello.jar:/usr/java/jdk-9/mods
    --add-modules com.greeting
    --output myimage
````
1. `--module-path`: This constructs a module path where the Hello World application is present and the `$JAVA_HOME/jmods` directory contains the platform modules
1. `--add-modules`: This indicates that `com.greeting` which is the module that needs to be added in the runtime image
1. `--output`: This directory is where the runtime image will be generated

Note: In Windows, the path separator is ; instead of :

![Figure 0.1](img/figure0-1.png)

Note the sizes of the JDK 9 compared to the custom runtime image performed with `du -sh` command (979M in `jdk-9$` and 45M in `myimage$`).

The generated image, `myimage`, has the following directory layout:

````
myimage/
    conf
    include
    legal
    lib
    bin
        java
````

A custom runtime image is fully self contained. It bundles the application modules with the JVM and everything else it needs to execute your application.

You can check the runtime custom image with the `java -version` command.

You can also execute the `java --list-modules` command to list the modules that are in a custom runtime image.

You can use the `java` command, which is in `myimage`, to launch your application:

`$ myimage/bin/ java --module com.greeting`

or

`$ myimage/bin/ java -m com.greeting`

You do not need to set the module path. The custom runtime image is in its own module path.

The `jlink` command has a `-launcher` option that creates a platform specific executable in the `bin` directory.

````
/Hello$ jlink
    --module-path dist/Hello.jar:/usr/java/jdk-9/mods
    --add-modules com.greeting
    --launcher com.greeting=Hello
    --output myimage
````

You can use this executable to run your application:

`$ myimage/bin Hello`

#### `jlink` resolves transitive dependencies

The `jlink` tool will resolve all dependencies transitively for the modules specified using the `--add-modules` option, and includes all the resolved dependent modules into the runtime image.

### Advantages of a Custom Runtime Image

- Ease of use - Can be shipped to you application users who don't have to download and install JRE separately to run the application
- Reduced footprint - Consists of only those modules that your application uses and is therefore much smaller than a full JDK. It can be used on resource constrained devices and applications on the cloud
- Performance - Runs faster because of link time optimization

#### JIMAGE format

The runtime image is stored in a special format called JIMAGE, which is optimized for space and speed. It is a much faster way to search and load classes from JAR and JMOD files.
JDK 9 ships with the `jimage` tool to let you explore the contents of a JIMAGE file.

### Optimizing a custom runtime image

#### Using plug-ins with the `jlink` tool

To use a plug-in, you need to use the command line option for it. Run the `jlink` tool with the `--list-plugins` options to print the list of all available plug-ins with their descriptions and command line options.

`$ jlink --list-plugins`

The `compress` plug-in optimizes the custom runtime image by reducing the Java SE 9 runtime image (level 1 is constant string sharing and level 2 is ZIP).

The `strip-debug` plug-in removes all the debugging information from the Java code further reducing the size of the image.

````
/Hello$ jlink
    --module-path dist/Hello.jar:/usr/java/jdk-9/mods
    --add-modules com.greeting
    --output myimage
    -strip-debug --compress=2
````

## <a name="0-2"></a>Multi-Release JAR Files

### Packaging an Application for Different JDKs

- Maintaining and releasing an application targeting different JDKs is difficult
- Prior to Java SE 9, when packaging the application code for different JDKs, you needed to create a separate application JAR for each

![Figure 0.2](img/figure0-2.png)

- JDK 9 offers a new way of packaging application code so that it can support multiple JDK versions in a single JAR
- This type of JAR is called a multi-release JAR

![Figure 0.3](img/figure0-3.png)

### What is a Multi-Release JAR file?

A multi-release JAR (MRJAR) is a single unit of distribution, compatible with multiple major Java platform versions. For example:

- You can have an MRJAR that will work on Java SE 8 and Java SE 9
- The MRJAR will contain the class files compiled in Java SE 8, plus additional classes compiled in Java SE 9.
    - The classes compiled in Java SE 9 may take advantage of APIs only offered in Java SE 9 & 10

### Structure of a multi-release JAR file

A JAR has a root directory, which contains:

- A directory structure representing packages and classes
- A `META-INF` directory that is used to store metadata about the JAR (this contains a `META-INF/MANIFEST.MF` file containing its attributes)

![Figure 0.4](img/figure0-4.png)

An MRJAR extends the `META-INF` directory to store classes that are for a particular JDK version.

- These classes that are specific to particular JDK version are contained in a versions subdirectory in the `META-INF` directory.
- The versions directory may contain many subdirectories - each of them named as a JDK major version

Entries in a multi-release JAR file look like the following:

![Figure 0.5](img/figure0-5.png)

If ann MRJAR is used in an environment that does not support MRJAR then it is treated as a regular JAR. So, if the MRJAR is ued with:

- Pre JDK 9:
    - The contents in the root directory will be used and everything in `META-INF/versions/9` will be ignored
- JDK 9+:
    - There are additional classes which will be used instead of those with the same name

#### Search process in an MRJAR

If more than one version of a class exists in an MRJAR:

- The JDK will use the first one it finds
- The search begins in the directory tree whose name matches the JDK major version number
- The search continues with successively lower-numbered directories until finally reaching the root directory
    - i.e., if the class has more than one version, for JDK 9 the search starts at `META-INF/versions/9`, whilst for JDK 8, it starts at teh root, `META-INF`  

### Creating a multi-release JAR file

The `jar` tool has been enhanced in JDK 9 to support creating MRJARs. In JDK 9, the `jar` tool accepts a new option called `--release`:

`$ jar <options> --release N <other options>`

Here, `N` is a JDK major version, such as 9 for JDK 9

![Figure 0.6](img/figure0-6.png)

You can list the entries in `foo.jar` by using the `--list` option:

````
$ jar --list --file foor.jar

META-INF/
META-INF/MANIFEST.MF
com/
com/foo/
com/foo/ListUtil.class
...
META-INF/versions/9/com/foo/ListUtil.class
...
````

You can run `foo.jar` on JDK 7, 8 or 9 with:

`$ java -jar foo.jar`

But different classes will be used in each run depending on the JDK version.

### Creating a modular multi-release JAR file

Here is the same project converted to a modular project by adding `module-info.java`:

![Figure 0.7](img/figure0-7.png)

Use the `--create` option from the `jar` tool as seen previously to create the modular MRJAR. If you ran `--list` on this, you'll see the `module-info.class` included in the directory/file list. 

Running can be done the same way as above. But now, because the project is modular we can also run it on the module path:

`java -p modular_foo.jar -m mymod`

## <a name="q"></a>Quiz

1. In Java SE 9, which phase provides an opportunity to perform optimization
    - Compile time
    - Link time (A)
    - Run time
1. Identify the set of tasks `jlink` can perform on a set of modules to create a custom runtime image
    - Assemble modules
    - Optimize modules
    - Both the above (A)
1. What are the advantages of a custom runtime image? (Choose two):
    - It consists of only those modules that your application uses (A)
    - It runs faster because of link time optimization (A)
    - It can be used to run an application in the cloud
    - It is heavier than the full JDK
1. Which tool is used to create a runtime image?
    - jar
    - jshell
    - jlink (A)
    - javap
1. Which statement is true about custom runtime images?
    - Customized runtime images contain onlyt the application's module
    - Customized runtime images can be created by using JDK 8
    - Customized runtime images are bundled with the application modules and platform modules of the JVM, and everything else it needs to execute the application (A)
    - Customized runtime images contain all the modules of JDK 9
1. In what format are runtime images stored?
    - jimage (A)
    - jlink
    - javaimage
    - jmod
1. Which option in the `jlink` tool can be used to resolve all dependencies transitively for the modules specified, as well as to include all the resolved dependent modules into the runtime image?
    - module
    - add-modules (A)
    - list-modules
    - module-path
1. If a class `Test.class` contains SE 9 specific code and is in the root directory of an MRJAR, and another
`Test.class` file contains SE 8 specific code and is in `META-INF/versions/9`, what will happen if a JDK 8 runtime is used?
    - `Test.class` in the root directory will be used and will fail
    - `Test.class` containing SE 8 specific code will be used
    - `Test.class` in the root directory will be used (A)
1. In a modular MRJAR file, where should the `module-info.class` be stored?
    - In the root directory for the module, under the `META-INF/versions/<version number>` for the appropriate version of the modular application
    - In both locations A & B
    - In the root directory for the module, under the root directory of the JAR (A)
1. If a `Test.class` exists in the root directory of an MRJAR and under `META-INF/versions/10`, which version of the class will be used with a JDK 9 runtime? (Assuming that `Test.class` is required to run the code in the JAR)
    - Neither, the code will fail if no `Test.class` file in a `META-INF/versions/9` directory
    - `Test.class` in the `META-INF/versions/10` directory
    - `Test.class` in the root directory (A)
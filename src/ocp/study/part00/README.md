# Extra Material

Material which does not map to the exam topics but may be useful nonetheless. 

- [0.1 - Custom Runtime Images](#0-1)
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
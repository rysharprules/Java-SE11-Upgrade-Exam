- [2.1 - Describe the components of Services including directives](#21---describe-the-components-of-services-including-directives)
  - [Service Module](#service-module)
  - [Service Provider Module](#service-provider-module)
  - [Service Client Application](#service-client-application)
- [2.2 - Design a service type, load the services using ServiceLoader, check for dependencies of the services including consumer module and provider module](#22---design-a-service-type-load-the-services-using-serviceloader-check-for-dependencies-of-the-services-including-consumer-module-and-provider-module)
  - [Designing services](#designing-services)
  - [Developing service providers](#developing-service-providers)
  - [Deploying service providers as modules](#deploying-service-providers-as-modules)
    - [The `provider()` method](#the-provider-method)
  - [Service module dependency](#service-module-dependency)
  - [Provider module dependency](#provider-module-dependency)
  - [Client module dependency](#client-module-dependency)
  - [Service Based Design](#service-based-design)
    - [Module Dependencies](#module-dependencies)
    - [Service Relationships](#service-relationships)
    - [Expressing Service Relationships](#expressing-service-relationships)
    - [Service Loader](#service-loader)
    - [Choosing the provider class](#choosing-the-provider-class)
    - [Designing a Service Type](#designing-a-service-type)
  - [<a name="q"></a>Quiz](#quiz)
  - [<a name="qa"></a>Quiz Answers](#quiz-answers)

# 2.1 - Describe the components of Services including directives

Java since version 6 supported service-provider loading facility via the `java.util.ServiceLoader` class. Using Service Loader you can have a service provider interface (SPI) simply called Service, and multiple implementations of the SPI simply called Service Providers. These Service Providers in Java 8 and earlier are located in the classpath and loaded at run time.

Since Java 9 you can develop Services and Service Providers as modules. A service module declares one or more interfaces whose implementations will be provided at run time by some provider modules. A provider module declares what implementations of service interfaces it `provides`.The module that discovers and loads service providers must contain a `uses` directive in its declaration.

## Service Module

We have a [GreeterIntf interface](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_1/service/p1/GreeterIntf.java) with one method, `greet()`, which resides in package `p1`. This is exported by `modS` defined in the service [module-info.java](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_1/service/module-info.java).

We can compile this with: `javac service/module-info.java service/p1/GreeterIntf.java` and package as a JAR with: `jar --create --file service.jar -C service .`

## Service Provider Module

We have a [GreeterImpl class](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_1/provider/p2/GreeterImpl.java) which implements the `GreeterIntf` interface. 

The [module-info.java](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_1/provider/module-info.java) class defines `modP` which `requires` `modS` (the service module) so it can use the `GreeterIntf` interface. 

A service provider will use `provides ... with ...` directive to declare what service interface it intends to use (by using `provides` keyword) and what implementation of the interface it wants to expose (by using `with` keyword):

`provides p1.GreeterIntf with p2.GreeterImpl`

<img src="../img/note.png" alt="Note" width="20"/> _Note: We don't have to specify the service implementation in a file under the resource directory `META-INF/services` as of Java 9._

We can compile this with: `javac -p service.jar provider/module-info.java provider/p2/GreeterImpl.java` and package as a provider JAR with: `jar --create --file provider.jar -C provider .`

## Service Client Application

In order for a service to be used, its providers need to be discovered and loaded. The `ServiceLoader` class does the work of discovering and loading the service providers. The module that discovers and loads service providers must contain a `uses <service interface name>` directive in its declaration. In our example, [module-info.java](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_1/service-client/module-info.java) we see `modC` declares it `uses p1.GreeterIntf`.

If a module uses the `ServiceLoader<GreeterIntf>` class to load the instances of service providers for a service interface named `p1.GreeterIntf`, the module declaration must contain the `uses p1.GreeterIntf` declaration.

[Client](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_1/service-client/app/Client.java) class uses the `ServiceLoader<GreeterIntf>` class to load the instances of service providers for a service interface named `p1.GreeterIntf`, then calls the `greet()` method for the first it finds (we have only one here).

````
ServiceLoader<GreeterIntf> services = ServiceLoader.load(GreeterIntf.class);
services.findFirst().ifPresent(s -> s.greet());
````

We can compile this with: `javac -p service.jar service-client/module-info.java service-client/app/Client.java` and run the client code: 

````
java -p service.jar;provider.jar;service-client -m modC/app.Client
Greeting from GreeterImpl !
````

# 2.2 - Design a service type, load the services using ServiceLoader, check for dependencies of the services including consumer module and provider module

## Designing services

A service is a single type, usually an interface or abstract class. A concrete class can be used, but this is not recommended. The type may have any accessibility. The methods of a service are highly domain-specific, so this API specification cannot give concrete advice about their form or function. However, there are two general guidelines:

1. A service should declare as many methods as needed to allow service providers to communicate their domain-specific properties and other quality-of-implementation factors. An application which obtains a service loader for the service may then invoke these methods on each instance of a service provider, in order to choose the best provider for the application.
1. A service should express whether its service providers are intended to be direct implementations of the service or to be an indirection mechanism such as a "proxy" or a "factory". Service providers tend to be indirection mechanisms when domain-specific objects are relatively expensive to instantiate; in this case, the service should be designed so that service providers are abstractions which create the "real" implementation on demand. For example, the `CodecFactory` service expresses through its name that its service providers are factories for codecs, rather than codecs themselves, because it may be expensive or complicated to produce certain codecs.

## Developing service providers

A service provider is a single type, usually a concrete class. An interface or abstract class is permitted because it may declare a `static` `provider()` method, discussed later. The type must be public and must not be an inner class.

A service provider and its supporting code may be developed in a module, which is then deployed on the application module path or in a modular image. Alternatively, a service provider and its supporting code may be packaged as a JAR file and deployed on the application class path. The advantage of developing a service provider in a module is that the provider can be fully encapsulated to hide all details of its implementation.

An application that obtains a service loader for a given service is indifferent to whether providers of the service are deployed in modules or packaged as JAR files. The application instantiates service providers via the service loader's iterator, or via `Provider` objects in the service loader's stream, without knowledge of the service providers' locations.

We can update the `app.Client` class (from previous section) as follows (see [greeting_2/service-client/app/Client.java](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_2/service-client/app/Client.java) class for full implementation):

````
ServiceLoader.load(GreeterIntf.class)
        .stream()                
        .filter((Provider p) -> p.type().getSimpleName().startsWith("Greeter"))
        .map(Provider::get)
        .findFirst()
        .ifPresent(s -> s.greet());
````

An instance of the `ServiceLoader.Provider` interface represents a service provider. Its `type()` method returns the `Class` object of the service implementation. The `get()` method instantiates and returns the service provider. When you use the `stream()` method, each element in the stream is of the `ServiceLoader.Provider` type. You can filter the stream based on the class name or type of the provider, which will not instantiate the provider. You can use the `type()` method in your filters. When you find the desired provider, call the `get()` method to instantiate the provider. This way, you instantiate a provider when you know you need it, not when you are iterating through all providers.

## Deploying service providers as modules

A service provider that is developed in a module must be specified in a `provides` directive in the module declaration. The `provides` directive specifies both the service and the service provider; this helps to locate the provider when another module, with a `uses` directive for the service, obtains a service loader for the service. It is strongly recommended that the module does not export the package containing the service provider (see note below). There is no support for a module specifying, in a `provides` directive, a service provider in another module.

<img src="../img/note.png" alt="Note" width="20"/> _Note: This a bad example of module definition:_

````
module modP {
    requires modS;
    provides p1.GreeterIntf with p2.GreeterImpl;
    exports p2; // BAD !!! 
}
````

_We should not export implementation of the service._

A service provider that is developed in a module has no control over when it is instantiated, since that occurs at the behest of the application, but it does have control over how it is instantiated:

### The `provider()` method

- If the service provider declares a `provider()` method, then the service loader invokes that method to obtain an instance of the service provider. A provider method is a `public static` method named "provider" with no formal parameters and a return type that is assignable to the service's interface or class.

    In this case, the service provider itself NEED NOT be assignable to the service's interface or class.

- If the service provider does not declare a `provider()` method, then the service provider is instantiated directly, via its provider constructor. A provider constructor is a public constructor with no formal parameters.

    In this case, the service provider MUST be assignable to the service's interface or class.

A service provider that is deployed as an automatic module on the application module path must have a provider constructor. There is no support for a `provider()` method in this case.

Let's create new service provider ([MyProvider.java](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_2/provider/p3/MyProvider.java)):

````
package p3;

import p1.GreeterIntf;

public class MyProvider {
    public static GreeterIntf provider() {
        return new GreeterIntf() {
            @Override
            public void greet() {
                System.out.println("Greeting from MyProvider !");
            }
        };
    }
}
````

[module-info.java](https://github.com/rysharprules/Java-SE11-Upgrade-Exam/blob/master/src/ocp/study/part02/greeting_2/provider/module-info.java):

````
module modPP {
    requires modS;
    provides p1.GreeterIntf with p3.MyProvider;
}
````

The output will be:

`Greeting from MyProvider !`

Two points here: 

1. The `provider()` method was used to instantiate service implementation
2. The service provider type (`MyProvider`) is not assignable to service interface (`GreeterIntf`).

## Service module dependency

Run the command:

````
jar --describe-module --file=service.jar

modS jar:file:///C:.../service.jar/!module-info.class
exports p1
requires java.base mandated	
````

As you can see is depends on `java.base` module in our case (it always implicitly added).

## Provider module dependency

Run the command:

````
jar --describe-module --file=provider.jar

modP jar:file:///C:.../provider.jar/!module-info.class
requires java.base mandated
requires modS
provides p1.GreeterIntf with p2.GreeterImpl
contains p2
````

As you can see this depends on the `java.base` and `modS` service module.

## Client module dependency

Run the command:

````
jar --create --file service-client.jar -C service-client .

jar --describe-module --file=service-client.jar

modC jar:file:///C:.../service-client.jar/!module-info.class
requires java.base mandated
requires modS
uses p1.GreeterIntf
contains app
````

As you can see this depends on the `java.base` and `modS` service module. The client module (`modC`) does not depend on provider module (`modP`) and is not aware of it at compile time.

## Service Based Design

### Module Dependencies

![Figure 2.1](img/figure2-1.png)

If we wanted to add another game on top of `basketball` and `soccer`, e.g. softball, baseball etc, 
that new module would depend on the `gameapi` and the `competition` module would depend on it.

### Service Relationships

Game can be an interface (e.g. basketball, soccer etc). The competition module then can use the game. 
Then the soccer class can implement the game interface.

![Figure 2.2](img/figure2-2.png)

### Expressing Service Relationships

Consumer module:
````
module competition {
    uses gameapi.Game
}
````

Provider module:
````
module soccer {
    provides gameapi.Game
}
````

### Service Loader

We can now make use of the [ServiceLoader](https://docs.oracle.com/javase/9/docs/api/java/util/ServiceLoader.html) 
class in the `competition` module.

````
ServiceLoader<Game> game = ServiceLoader.load(Game.class);
ArrayList<Game> providers = new ArrayList<>();
for (Game currGame : game) {
    providers.add(currGame);
}
return providers;
````

The `load` method above: Creates a new service loader for the given service type, using the current 
thread's context class loader.

### Choosing the provider class

We have multiple potential providers; soccer, basketball etc. We could add a type so that the consumer
gets the correct service:

````
ServiceLoader<Game> game = ServiceLoader.load(Game.class);
ArrayList<Game> providers = new ArrayList<>();
for (Game currGame : game) {
    if(currGame.getType().equals("soccer")) return currGame;
}
throw new RuntimeException("No suitable service provider");
````

We can move the Game interface into the competition module. This removes any chance of cyclic dependency.

![Figure 2.3](img/figure2-3.png)

### Designing a Service Type

gameapi module:
````
public interface Game {
    String getType();
    Team getHomeTeam();
    Team getAwayTeam();
    void playGame();
    ...
}
````
````
public interface GameProvider {
    Game getGame (Team homeTeam, Team awayTeam, LocalDateTime dateTime);
    Team getTeam (String teamName, Player[] players);
    Player getPlayer(String playerName);
    String getType();
}
````

We can now have a class `SoccerProvider` which implements `GameProvider` in the soccer module. We can then
do the same for other game types, e.g. `BasketBallProvider` which implements `GameProvider`.

![Figure 2.4](img/figure2-4.png)

## <a name="q"></a>Quiz

1. <a name="q1"></a>What needs to be implemented in a provider module?:
    - A. All interfaces in the consumer module
    - B. All interfaces declared with "provides ... with"
    - C. All interfaces declared with "uses" in the `module-info` file of the consumer module
<br />[Jump to answer](#qa1)
2. <a name="q2"></a>Which of the following are true? (Choose two):
    - A. The consumer module does not need to declare a dependency on the provider module(s)
    - B. The consumer module must declare a dependency on the provider module(s)
    - C. The provider module does not need to declare a dependency on the consumer module(s)
    - D. The provider module must declare a dependency on the consumer module(s)
<br />[Jump to answer](#qa2)
3. <a name="q3"></a>Which of the following is true?:
    - A. The consumer module must declare a dependency on the provider module
    - B. The provider module must declare a dependency on the consumer module
    - C. Neither consumer or provider modules need declare a dependency on each other
<br />[Jump to answer](#qa3)
4. <a name="q4"></a>How many service provider implementations can be made to a consumer?
    - A. Zero or any number in any module in the module path
    - B. Zero or one in the module path
    - C. One only in the module path
    - D. Zero or one in any module in the module path
<br />[Jump to answer](#qa4)

## <a name="qa"></a>Quiz Answers

1. <a name="qa1"></a>[Jump to question](#q1) - **C.** A service provider must provide (via `provides`) 
an implementation for consumer modules to use (via `uses`). 
2. <a name="qa2"></a>[Jump to question](#q2) - **A, C.** 
3. <a name="qa3"></a>[Jump to question](#q3) - **C.**
4. <a name="qa4"></a>[Jump to question](#q4) - **A.**
- [2.1 - Describe the components of Services including directives](#2-1)
- [2.2 - Design a service type, load the services using ServiceLoader, check for dependencies of the services including consumer module and provider module](#2-2)
- [Quiz](#q)
- [Quiz Answers](#qa)

## <a name="2-1"></a>2.1 - Describe the components of Services including directives
## <a name="2-2"></a>2.2 - Design a service type, load the services using ServiceLoader, check for dependencies of the services including consumer module and provider module

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
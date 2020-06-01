package ocp.study.part03;

// Single Abstract Method interfaces (SAM Interfaces)
@FunctionalInterface
public interface FunctionalInterfaceExample {

  void doWork();

  default void doSomeOtherWork() {
    System.out.println("Doing some other work in interface impl...");
  }

  static void doSomeStaticWork() {
    System.out.println("Doing static work");
  }
}

package ocp.study.part03;

public class FunctionalInterfaceDemo {

  @FunctionalInterface
  public interface MyDefInterface {

    default void defaultDoIt() {
      System.out.println("default");
    }

    static void staticDoIt() {
      System.out.println("static");
    }

    void doItNow();  // SAM (Single Abstract Method)
  }


  public static void main(String[] args) {
    MyDefInterface myDefInterface = () -> System.out.println("just do it");

    myDefInterface.defaultDoIt();
    myDefInterface.doItNow();
    MyDefInterface.staticDoIt();
  }

}

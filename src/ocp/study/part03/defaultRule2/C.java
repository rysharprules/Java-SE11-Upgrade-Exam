package ocp.study.part03.defaultRule2;

public interface C extends B {
  default void m() {
    System.out.println("Interface C");
  }
}

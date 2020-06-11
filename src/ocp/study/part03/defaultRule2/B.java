package ocp.study.part03.defaultRule2;

public interface B {
  default void m() {
    System.out.println("Interface B");
  }
}

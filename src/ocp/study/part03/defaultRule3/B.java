package ocp.study.part03.defaultRule3;

public interface B {
  default void m() {
    System.out.println("Interface B");
  }
}

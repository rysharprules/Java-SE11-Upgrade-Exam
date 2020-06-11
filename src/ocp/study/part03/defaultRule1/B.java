package ocp.study.part03.defaultRule1;

public interface B {
  default void m() {
    System.out.println("Interface B");
  }
}

package ocp.study.part03.defaultRule3;

import ocp.study.part03.defaultRule2.B;

public interface C {
  default void m() {
    System.out.println("Interface C");
  }
}

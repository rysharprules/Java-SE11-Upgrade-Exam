package ocp.study.part03.defaultRule3;

import ocp.study.part03.defaultRule2.C;

public class D implements B, C {

  @Override
  public void m() {
    B.super.m();
  }
}

package ocp.study.part00;

public class SavingsAccount {

  private int balance;

  SavingsAccount(int initial) {
    this.balance = initial;
  }

  public int getBalance() {
    return balance;
  }

  public void chargeFee() {
    balance += 10;
  }

  public String toString() {
    return "[" + balance + "]";
  }
}

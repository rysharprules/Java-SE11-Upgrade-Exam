package ocp.study.part00;

import java.util.ArrayList;
import java.util.List;

public class TakeWhileExample {

  public static void main(String... args) {
    List<SavingsAccount> accountList = new ArrayList<>();
    accountList.add(new SavingsAccount(100));
    accountList.add(new SavingsAccount(200));
    accountList.add(new SavingsAccount(300));
    accountList.add(new SavingsAccount(400));
    accountList.add(new SavingsAccount(500));

    var example = new TakeWhileExample();
    example.takeWhileExample(accountList);
  }

  // Must be done on ordered list as takeWhile is a short circuit operation
  // takeWhile is an intermediate operation
  private void takeWhileExample(List<SavingsAccount> accountList) {
    accountList.stream()
        .takeWhile(a -> a.getBalance() < 250) // 100, 200
        .peek(System.out::print) // [100][200]
        .forEach(SavingsAccount::chargeFee);
    System.out.println();
    System.out.println(accountList); // [[110], [210], [300], [400], [500]]
  }

}

package ocp.study.part00;

import java.util.ArrayList;
import java.util.List;

public class DropWhileExample {

  public static void main(String... args) {
    List<SavingsAccount> accountList = new ArrayList<>();
    accountList.add(new SavingsAccount(100));
    accountList.add(new SavingsAccount(200));
    accountList.add(new SavingsAccount(300));
    accountList.add(new SavingsAccount(400));
    accountList.add(new SavingsAccount(500));

    var example = new DropWhileExample();
    example.dropWhileExample(accountList);
  }

  // Must be done on ordered list as dropWhile is a short circuit operation
  // dropWhile is an intermediate operation
  private void dropWhileExample(List<SavingsAccount> accountList) {
    accountList.stream()
        .dropWhile(a -> a.getBalance() < 250)
        .peek(System.out::print) // [300][400][500]
        .forEach(SavingsAccount::chargeFee);
    System.out.println();
    System.out.println(accountList); // [[100], [200], [310], [410], [510]]
  }

}

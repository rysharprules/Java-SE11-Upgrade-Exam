package ocp.study;

public class Employee implements Comparable {

  public final String name;
  private final Department dept;

  public Employee(String n) {
    name = n;
    dept = new Department("Engineering");
  }

  public Employee(String n, String d) {
    name = n;
    dept = new Department(d);
  }

  public String getName() {
    return name;
  }

  public Department getDepartment() {
    return dept;
  }

  @Override
  public String toString() {
    return name + ":" + dept.getDept();
  }

  @Override
  public int compareTo(Object o) {
    return name.compareTo(((Employee) o).getName());
  }
}

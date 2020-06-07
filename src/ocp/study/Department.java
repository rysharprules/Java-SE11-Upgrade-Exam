package ocp.study;

public class Department {

  String dept;

  public String getDept() {
    return dept;
  }

  public void setDept(String dept) {
    this.dept = dept;
  }

  public Department(String dept) {
    this.dept = dept;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null || obj.getClass() != Department.class) {
      return false;
    }
    return dept.endsWith(((Department) obj).dept);
  }

  @Override
  public int hashCode() {
    return dept.hashCode();
  }

  @Override
  public String toString() {
    return dept + " Department";
  }
}

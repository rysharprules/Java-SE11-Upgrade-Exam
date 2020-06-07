package ocp.study;

import java.time.LocalDate;

public class Person {

  String name;
  int age;
  LocalDate birthday;
  Sex gender;
  String emailAddress;

  public Person(String name, int age) {
    this.age = age;
    this.name = name;
  }

  public Person(String name, int age, boolean isMale) {
    this.age = age;
    this.name = name;
    this.gender = isMale ? Sex.MALE : Sex.FEMALE;
  }

  public static int compareByAge(Person a, Person b) {
    return a.getAge().compareTo(b.getAge());
  }

  @Override
  public String toString() {
    return name + " : " + age;
  }

  public String getName() {
    return name;
  }

  public Sex getGender() {
    return gender;
  }

  public Integer getAge() {
    return age;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public int compareByName(Person b) {
    return getName().compareTo(b.getName());
  }

  public enum Sex {
    MALE, FEMALE
  }
}
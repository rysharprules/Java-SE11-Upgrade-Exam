package ocp.study;

import java.time.LocalDate;

public class Person {

  public enum Sex {
    MALE, FEMALE
  }

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
    //return LocalDate.now().minus(birthday.getLong(ChronoField.EPOCH_DAY), ChronoUnit.DAYS).getLong(ChronoField.EPOCH_DAY);
    return age;
  }

  public LocalDate getBirthday() {
    return birthday;
  }

  public int compareByName(Person b) {
    return getName().compareTo(b.getName());
  }

  public static int compareByAge(Person a, Person b) {
    return a.getAge().compareTo(b.getAge());
    //return a.birthday.compareTo(b.birthday);
  }
}
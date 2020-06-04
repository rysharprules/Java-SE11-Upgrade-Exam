package ocp.study.part08;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import ocp.study.Person;

public class MethodReferences {

  static void staticMethod(Person[] persons) {
    Arrays.sort(persons, Person::compareByAge);
    System.out.println(Arrays.toString(persons));
  }

  static void instanceMethod(Person[] persons) {
    class ComparisonProvider {
      public int compareByName(Person a, Person b) {
        return a.getName().compareTo(b.getName());
      }

      public int compareByAge(Person a, Person b) {
        return a.getBirthday().compareTo(b.getBirthday());
      }
    }

    ComparisonProvider cp = new ComparisonProvider();
    Arrays.sort(persons, cp::compareByName);
    System.out.println(Arrays.toString(persons));
  }


  static void typeMethod(Person[] persons) {
    // See the syntax of Person::compareByName
    // public int compareByName(Person b) {
    //      return getName().compareTo(b.getName());
    // }
    Arrays.sort(persons, Person::compareByName);
    System.out.println(Arrays.toString(persons));
  }

  static void constructorReference(List<Person> persons) {
    Set<Person> rosterSetLambda = transferElements(persons, () -> {
      return new HashSet<>();
    });

    // or use constructor reference.
    Set<Person> rosterSet = transferElements(persons, HashSet::new);
    System.out.println("Set:" + rosterSet);
  }

  public static <T, S extends Collection<T>, D extends Collection<T>> D transferElements(
      S sourceCollection, Supplier<D> collectionFactory) {
    D result = collectionFactory.get();
    for (T t : sourceCollection) {
      result.add(t);
    }
    return result;
  }

  public static void main(String[] args) {
    List<Person> persons = Arrays.asList(new Person("Jane", 25), new Person("Alice", 28),
        new Person("Bob", 42), new Person("Mina", 19));

    Person[] pArray = persons.toArray(new Person[persons.size()]);

    staticMethod(pArray);
    instanceMethod(pArray);
    typeMethod(pArray);
    constructorReference(persons);
  }
}

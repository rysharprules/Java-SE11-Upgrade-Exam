package ocp.study.part08.functional.interfaces;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class PredicateDemo {

    private static void processList(List<String> list, Predicate<String> predicate) {
        for (String s : list) {
            System.out.println(s + " Test pass? " + predicate.test(s));
        }
    }

    public static void main(String[] args) {
        List<String> strList = Arrays.asList("TRUE", "true", null, "", "false", "TrUe");
        Predicate<String> boolTest = s -> new Boolean(s).booleanValue();

        processList(strList, boolTest);
    }
}

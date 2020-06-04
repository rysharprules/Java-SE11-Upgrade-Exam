package ocp.study.part08.functional.interfaces;

import java.util.function.Supplier;

public class SupplierDemo {

    public static void main(String[] args) {
        Supplier<String> supplier = () -> new String("Hello");

        System.out.println(supplier.get());

        Supplier<String> sup = String::new;
        System.out.println("Note this reference: " + sup);
        System.out.println("Empty String: " + sup.get() + ".");
    }
}

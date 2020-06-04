package ocp.study.part03;

public interface MyInterface {

    default void someDefault() {
        // You can call an abstract method from within a default one.
        test();
    }

    void test();
}

package ocp.study.part03;

public class DefaultAndStaticMethods implements TestInterface {

    public static void main(String... strings) {
        DefaultAndStaticMethods dasm = new DefaultAndStaticMethods();
        dasm.method1();
    }

    @Override
    public void method1() {
        log(String.valueOf(TestInterface.isNull(null)));
    }

    public void log(String str) {
        System.out.println("childlogging::" + str);
    }

}

interface TestInterface {

    void method1();

    default void log(String str) {
        System.out.println("logging::" + str);
    }

    static boolean isNull(String str) {
        System.out.println("Interface Null Check");
        return str == null ? true : false;
    }
}

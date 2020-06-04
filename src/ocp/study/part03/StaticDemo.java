package ocp.study.part03;

public class StaticDemo {

    interface One {
        void doIt();

        default void doSomething() {
            System.out.println("I - One: Do something");
            // Default can invoke static methods.
            sayHello();
        }

        static void sayHello() {
            System.out.println("I - One: SayHello");
            // Cant call default methods from static context
            // doSomething();
        }
    }

    interface Two extends One {
        // Redefined it now
        default void doSomething() {
            System.out.println("I - Two: Do something");
        }

        // doSomething2 is now abstract..you cant get the default impl now
        void doSomething2();

        static void sayHello() {
            System.out.println("I - Two: SayHello");
        }
    }

    static class OneTest implements One {

        @Override
        public void doIt() {
            System.out.println("C - OneTest: Do it");
        }

    //    @Override
    //    public void doSomething() {
    //      System.out.println("C - OneTest: Do something");
    //    }
    }

    static class TwoTest implements Two {

        @Override
        public void doIt() {
            System.out.println("C - TwoTest: Do it");
        }

        @Override
        public void doSomething2() {
            System.out.println("C - TwoTest: Do something");
        }
    }

    public static void main(String[] args) {
        One one = new OneTest();
        one.doIt();
        one.doSomething();

        // Can use only Interface One to call sayHello()
        One.sayHello();

        Two two = new TwoTest();
        two.doIt();
        two.doSomething();
        Two.sayHello();
    }
}

package p3;

import p1.GreeterIntf;

public class MyProvider {
    public static GreeterIntf provider() {
        return new GreeterIntf() {
            @Override
            public void greet() {
                System.out.println("Greeting from MyProvider !");
            }
        };
    }
}
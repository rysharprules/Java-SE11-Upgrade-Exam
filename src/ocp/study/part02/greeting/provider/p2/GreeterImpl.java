package p2;

import p1.GreeterIntf;

public class GreeterImpl implements GreeterIntf {

    @Override
    public void greet() {
        System.out.println("Greeting from GreeterImpl !");
    }    
}
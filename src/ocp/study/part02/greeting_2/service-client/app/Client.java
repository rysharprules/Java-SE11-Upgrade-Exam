package app;

import java.util.ServiceLoader;
import java.util.ServiceLoader.Provider;
import p1.GreeterIntf;

public class Client {
    public static void main(String[] args) {
        ServiceLoader.load(GreeterIntf.class)
                .stream()                
                .filter((Provider p) -> p.type().getSimpleName().startsWith("Greeter"))
                .map(Provider::get)
                .findFirst()
                .ifPresent(s -> s.greet());
    }
}
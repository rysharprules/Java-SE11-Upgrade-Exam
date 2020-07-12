package app;

import java.util.ServiceLoader;
import p1.GreeterIntf;

public class Client {
    public static void main(String[] args) {
        ServiceLoader<GreeterIntf> services = ServiceLoader.load(GreeterIntf.class);
        services.findFirst().ifPresent(s -> s.greet());
    }
}

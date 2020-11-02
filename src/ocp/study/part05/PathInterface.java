import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;

public class PathInterface {

    public static void main(String... unused) {

        // Java 6.0 and earlier
        File file = new File("README.md");

        // Since Java 7.0
        Path path = Paths.get("C:\\projects\\Java-SE11-Upgrade-Exam\\README.md");
        Path pathFromFile = new File("README.md").toPath();
        Path pathFromUri = Paths.get(URI.create("file:///C:/projects/Java-SE11-Upgrade-Exam/README.md"));
        File fileFromPath = path.toFile();
        URI uriFromFile = file.toURI();
        URI uriFromPath = path.toUri();

        // Java 11.0 added static .of(...) methods
        Path pathJava11 = Path.of("C:", "projects", "Java-SE11-Upgrade-Exam", "README.md");
    
        // Path methods
        System.out.println("toString(): " + path);
        System.out.println("getFileName(): " + path.getFileName());
        System.out.println("getName(): " + path.getName(0));
        System.out.println("getNameCount(): " + path.getNameCount());
        System.out.println("subpath(): " + path.subpath(0, 2));
        System.out.println("getRoot(): " + path.getRoot());
        System.out.println("resolveSibling(): " + path.resolveSibling("foobar"));
        System.out.println("resolve(): " + path.resolve("foobar"));
        System.out.println("resolve(): " + path.resolve("C:\\projects\\Java-SE14-Upgrade-Exam"));
        Path pathWithRedundancies = Paths.get("C:\\projects\\Java-SE11-Upgrade-Exam\\..\\..\\foo");
        System.out.println("normalize(): " + pathWithRedundancies.normalize());
        Path p1 = Paths.get("home");
        Path p2 = Paths.get("home/rysharp/foo");
        Path p1_p2 = p1.relativize(p2);
        System.out.println("relativize(): " + p1_p2);
        Path p2_p1 = p2.relativize(p1);
        System.out.println("relativize(): " + p2_p1);
    }

}

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URI;

public class PathInterface {

    private static final String README_FILE = "C:\\projects\\Java-SE11-Upgrade-Exam\\README.md";

    public static void main(String... unused) throws Exception {

        // Java 6.0 and earlier
        File file = new File("README.md");

        // Since Java 7.0
        Path path = Paths.get(README_FILE);
        Path pathFromFile = new File("README.md").toPath();
        Path pathFromUri = Paths.get(URI.create("file:///C:/projects/Java-SE11-Upgrade-Exam/README.md"));
        // A file system is the factory for several types of objects including getPath
        Path pathFromFileSystems = FileSystems.getDefault().getPath(README_FILE);
        seperator();
        System.out.println("FileSystem");
        seperator();
        System.out.println(path.getFileSystem()); // sun.nio.fs.WindowsFileSystem@7ee8290b
        System.out.println(path.getFileSystem().getRootDirectories()); // [C:\, E:\, G:\]
        File fileFromPath = path.toFile();
        URI uriFromFile = file.toURI();
        URI uriFromPath = path.toUri();

        // Java 11.0 added static .of(...) methods
        Path pathJava11 = Path.of("C:", "projects", "Java-SE11-Upgrade-Exam", "README.md");
    
        // Path methods
        seperator();
        System.out.println("Path methods");
        seperator();
        System.out.println("toString(): " + path);
        
        // getFileName(): The file name is the farthest element from the root in the directory hierarchy 
        // (last element of the sequence of name elements)
        System.out.println("getFileName(): " + path.getFileName());
        
        // getName(int index): returns a name element of this path as a Path object. The index parameter is 
        // the index of the name element to return. The element that is closest to the root in the directory 
        // hierarchy has index 0. The element that is farthest from the root has index count-1.
        System.out.println("getName(): " + path.getName(0));
        
        // getNameCount(): returns the number of name elements in the path.
        System.out.println("getNameCount(): " + path.getNameCount());
        
        // subpath(int beginIndex, int endIndex): returns a relative Path that is a subsequence of the 
        // name elements of this path. The beginIndex and endIndex parameters specify the subsequence of name 
        // elements. The name that is closest to the root in the directory hierarchy has index 0. The name 
        // that is farthest from the root has index count-1. The returned Path object has the name elements 
        // that begin at beginIndex and extend to the element at index endIndex-1.
        System.out.println("subpath(): " + path.subpath(0, 2));
        
        // getRoot(): returns the root component of this path as a Path object, or null if this path 
        // does not have a root component (e.g. for relative paths). For UNIX platform the root will be "/". 
        // For Windows, something like "C:\".
        System.out.println("getRoot(): " + path.getRoot());

        // getParent(): return the parent path of current path object, or null if this path does not have a parent.
        System.out.println("getParent(): " + path.getParent());
        
        // resolveSibling(Path other) and resolveSibling(String other): resolve the given path against 
        // this path's parent path. This is useful where a file name needs to be replaced with another file name.
        System.out.println("resolveSibling(): " + path.resolveSibling("foobar"));
        
        // Path.resolve(Path other) and Path.resolve(String other): combine paths
        // If the other parameter is an absolute path then this method trivially returns
        // other. If other is an empty path then this method trivially returns this path.
        System.out.println("resolve(): " + path.resolve("foobar"));
        System.out.println("resolve(): " + path.resolve("C:\\projects\\Java-SE14-Upgrade-Exam"));

        // isAbsolute(): boolean to confirm is absolute
        System.out.println("isAbsolute(): " + path.isAbsolute()); // true
        System.out.println("isAbsolute(): " + Path.of("foo/bar").isAbsolute()); // false

        // toAbsolutePath(): returns a Path object representing the absolute path of
        // this path.
        System.out.println("toAbsolutePath(): " + Path.of("foo/bar").toAbsolutePath());

        // toRealPath(): returns the REAL path of existing file. Note, this throws IOException if not exists
        // By default, symbolic links are resolved to their final target
        // If the option NOFOLLOW_LINKS is present then this method does not resolve
        // symbolic links
        System.out.println("toRealPath(): " + path.toRealPath(LinkOption.NOFOLLOW_LINKS));
        try {
            System.out.println("toRealPath(): " + Path.of("foo/bar").toRealPath());
        } catch(IOException e) {
            System.out.println("toRealPath(): " + e);
        }

        // normalize(): returns a path that is this path with redundant name elements eliminated.
        Path pathWithRedundancies = Paths.get("C:\\projects\\Java-SE11-Upgrade-Exam\\..\\..\\foo");
        System.out.println("normalize(): " + pathWithRedundancies.normalize());
        
        // compareTo​(): compares two abstract paths lexicographically.
        // returns zero if the argument is equal to this path, a value less than zero if
        // this path is lexicographically less than the argument, or a value greater
        // than zero if this path is lexicographically greater than the argument
        int compare = path.compareTo(pathWithRedundancies);
        System.out.println("compareTo(): " + compare);

        // endsWith​(): Tests if this path ends with a Path/String
        System.out.println("endsWith(): " + path.endsWith(".md")); // false
        System.out.println("endsWith(): " + path.endsWith("README.md")); // true
        System.out.println("endsWith(): " + path.endsWith(Path.of("README.md"))); // true

        // startsWith​(): Tests if this path starts with a Path/String
        System.out.println("startsWith(): " + path.startsWith("C")); // false
        System.out.println("startsWith(): " + path.startsWith("C:/")); // true
        System.out.println("startsWith(): " + path.startsWith(Path.of("C:/"))); // true

        // equals() checks the path String for equality - it does not adjust for dir structure
        boolean equals = path.equals(pathJava11);
        System.out.println("equals(): " + equals); // true
        Path samePath = Path.of("C:", "projects", "Java-SE11-Upgrade-Exam", "random", "..", "README.md");
        System.out.println("equals(): " + samePath.equals(path)); // false
        System.out.println("equals(): " + samePath.normalize().equals(path)); // true

        // relativize(): constructs a path originating from the original path and ending at the location 
        // specified by the passed-in path. The new path is relative to the original path.
        Path p1 = Paths.get("home");
        Path p2 = Paths.get("home/rysharp/foo");
        Path p1_p2 = p1.relativize(p2);
        System.out.println("relativize(): " + p1_p2);
        Path p2_p1 = p2.relativize(p1);
        System.out.println("relativize(): " + p2_p1);
        // A relative path CANNOT be constructed if only one of the paths includes a root element.
        try {
            path.relativize(p1);
        } catch(IllegalArgumentException e) {
            System.out.println("relativize(): " + e);
        }
        try {
            p1.relativize(path);
        } catch (IllegalArgumentException e) {
            System.out.println("relativize(): " + e);
        }
        // If both paths include a root element, the capability to construct a relative path is system dependent.
        System.out.println("relativize(): " + path.relativize(pathWithRedundancies));
        System.out.println("relativize(): " + pathWithRedundancies.relativize(path));

        
    }

    private static void seperator() {
        System.out.println("############################################################################");
    }


}

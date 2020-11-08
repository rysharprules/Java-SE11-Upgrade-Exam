import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class FilesClass {

    private static final String FILE1 = "C:\\projects\\Java-SE11-Upgrade-Exam\\src\\ocp\\study\\part05\\file1.txt";
    private static final String FILE2 = "C:\\projects\\Java-SE11-Upgrade-Exam\\src\\ocp\\study\\part05\\file2.txt";
    private static final String PART_05 = "C:\\projects\\Java-SE11-Upgrade-Exam\\src\\ocp\\study\\part05\\";

    public static void main(String... unused) throws IOException {
        // move()
        // allowed CopyOptions: StandardCopyOption.ATOMIC_MOVE,
        // StandardCopyOption.REPLACE_EXISTING
        // not allowed: StandardCopyOption.COPY_ATTRIBUTES
        var move = Files.move(Path.of(FILE1), Path.of(FILE2)); // if exists: Exception in thread "main"
                                                               // java.nio.file.FileAlreadyExistsException
        System.out.println("move(): " + move);
        move = Files.move(Path.of(FILE2), Path.of(FILE1), REPLACE_EXISTING, ATOMIC_MOVE);
        System.out.println("move(): " + move);
        try {
            move = Files.move(Path.of(FILE1), Path.of(FILE2), COPY_ATTRIBUTES);
        } catch (UnsupportedOperationException e) {
            System.out.println("move(): " + e);
        }

        // copy()
        // allowed CopyOptions: LinkOption.NOFOLLOW_LINKS,
        // StandardCopyOption.COPY_ATTRIBUTES, StandardCopyOption.REPLACE_EXISTING
        // not allowed: StandardCopyOption.ATOMIC_MOVE
        var copy = Files.copy(Path.of(FILE1), Path.of(FILE2), COPY_ATTRIBUTES);
        System.out.println("copy(): " + copy);
        try {
            copy = Files.copy(Path.of(FILE1), Path.of(FILE2), ATOMIC_MOVE);
        } catch (UnsupportedOperationException e) {
            System.out.println("copy(): " + e);
        }

        // delete()
        Files.delete(copy);
        try {
            Files.delete(copy);
        } catch (NoSuchFileException e) {
            System.out.println("delete(): " + e);
        }

        // deleteIfExists(): returns true if the file was deleted by this method; false
        // if the file could not be deleted because it did not exist
        System.out.println("deleteIfExists(): " + Files.deleteIfExists(copy));

        // readAllBytes(), readAllLines(): if you have a small file and you would like
        // to read its entire contents in one pass
        Path file = Path.of(FILE1);
        byte[] fileArray;
        fileArray = Files.readAllBytes(file); // byte[]
        System.out.println("readAllBytes(): " + new String(fileArray, StandardCharsets.UTF_8));
        System.out.println("readAllLines(): " + Files.readAllLines(file)); // List<String>

        // newBufferedReader(): opens a file for reading, returning a BufferedReader
        // that can be used to read text from a file in an efficient manner.
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(file, charset)) {
            String line = null;
            System.out.print("newBufferedReader(): ");
            while ((line = reader.readLine()) != null) {
                System.out.print(line);
            }
            System.out.println();
        } catch (NoSuchFileException x) {
            System.err.format("No such file: %s", x.getFile());
        } catch (IOException x) {
            System.err.println(x);
        }

        // newBufferedWriter(Path, Charset, OpenOption...): write to a file using a
        // BufferedWriter:
        String s = "awesome file contents";
        try (BufferedWriter writer = Files.newBufferedWriter(file, charset)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }

        // newInputStream(Path, OpenOption...): returns an unbuffered input stream for
        // reading bytes from the file.
        try (InputStream in = Files.newInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = null;
            System.out.print("newInputStream(): ");
            while ((line = reader.readLine()) != null) {
                System.out.print(line);
            }
            System.out.println();
        } catch (NoSuchFileException x) {
            System.err.println("No such file exists: " + x.getFile());
        } catch (IOException x) {
            System.err.println(x);
        }

        // Java 11 added following new methods to java.nio.file.Files class to directly
        // read String from a file and to
        // directly write String to a file:
        System.out.println("writeString(): " + Files.writeString(file, " cool", charset, StandardOpenOption.APPEND));
        System.out.println("readString(): " + Files.readString(file));
        System.out.println("readString(): " + Files.readString(file, charset));

        // newDirectoryStream() can use glob syntax to specify pattern-matching
        // behavior.
        System.out.print("newDirectoryStream(): ");
        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Path.of(PART_05), "*.java");) {
            for (Path entry : directoryStream) {
                System.out.print(entry.getFileName() + ", ");
            }
            System.out.println();
        }

        var tempPath = Files.createTempFile(Path.of(PART_05), "ry", "an");
        System.out.println("createTempFile(): " + tempPath);
        System.out.println("exists(): " + Files.exists(tempPath)); // true
        System.out.println("getAttribute(): " + Files.getAttribute(tempPath, "basic:creationTime"));
        System.out.println("getAttribute(): " + Files.getAttribute(tempPath, "dos:readonly"));
        System.out.println("getOwner(): " + Files.getOwner(tempPath));
        System.out.println("isDirectory(): " + Files.isDirectory(tempPath));
        System.out.println("isExecutable(): " + Files.isExecutable(tempPath));
        System.out.println("isHidden(): " + Files.isHidden(tempPath));
        System.out.println("isRegularFile(): " + Files.isRegularFile(tempPath));
        System.out.println("isReadable(): " + Files.isReadable(tempPath));
        System.out.println("isWritable(): " + Files.isWritable(tempPath));
        System.out.println("isSymbolicLink(): " + Files.isSymbolicLink(tempPath));
        System.out.println("isSameFile(): " + Files.isSameFile(tempPath, file));
        Files.deleteIfExists(tempPath);

        try (FileReader f = new FileReader(FILE1); BufferedReader b = new BufferedReader(f);) {
            Stream<String> i = b.lines();
            i.filter(line -> line.contains("awesome")).findFirst()
                    .ifPresent(line -> System.out.println("lines(): " + line));
        }

        System.out.print("list(): ");
        Files.list(Path.of(PART_05)).forEach(f -> System.out.print(f + " "));
        System.out.println();

        System.out.print("walk(): ");
        try (Stream<Path> walk = Files.walk(Paths.get(PART_05))) {
            List<String> result = walk.filter(Files::isRegularFile).map(x -> x.toString()).collect(Collectors.toList());
            result.forEach(w -> System.out.print(w + " "));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();

        System.out.print("find(): ");
        Path testPath = Paths.get(PART_05);
        try (Stream<Path> stream = Files.find(testPath, 100, (path, basicFileAttributes) -> 
            !Files.isDirectory(path) && path.getFileName().toString().contains("file1")
        );) {
            stream.forEach(System.out::print);
        }
        System.out.println();

        // createTempFile, BasicFileAttributeView (Files.getFileAttributeView) & BasicFileAttributes (BasicFileAttributeView.readAttributes())
        Path tempFile = Files.createTempFile("test-file", ".txt");
        BasicFileAttributeView fileAttributeView = Files.getFileAttributeView(tempFile, BasicFileAttributeView.class);
        System.out.println("getFileAttributeView(): " + fileAttributeView);
        BasicFileAttributes basicFileAttributes = fileAttributeView.readAttributes();
        System.out.println("BasicFileAttributes.creationTime(): " + basicFileAttributes.creationTime());
        FileTime from = FileTime.from(400, TimeUnit.HOURS);
        fileAttributeView.setTimes(from, from, from);
        System.out.println("getLastModifiedTime(): " + Files.getLastModifiedTime(tempFile));

        // createTempDirectory & createDirectory
        System.out.println("Files.createTempDirectory");
        Path tempPath2 = Files.createTempDirectory("test");
        Path dirToCreate = tempPath2.resolve("test1");
        System.out.println("dir to create: " + dirToCreate);
        System.out.println("dir exists: " + Files.exists(dirToCreate));
        Path directory = Files.createDirectory(dirToCreate);
        System.out.println("directory created: " + directory);
        System.out.println("dir created exits: " + Files.exists(directory));

        // create directories
        System.out.println("Files.createDirectories");
        Path tempPath3 = Files.createTempDirectory("test");
        Path dirToCreate2 = tempPath3.resolve("test2").resolve("test3");
        System.out.println("dir to create: " + dirToCreate2);
        System.out.println("dir exits: " + Files.exists(dirToCreate2));
        Path directories = Files.createDirectories(dirToCreate2);
        System.out.println("directories created: " + directories);
        System.out.println("dir created exits: " + Files.exists(directories));
    }
}

import java.nio.file.Files;
import java.nio.file.Path;

import static java.nio.file.StandardCopyOption.*;

import java.io.IOException;

public class FilesClass {

    private static final String FILE1 = "C:\\projects\\Java-SE11-Upgrade-Exam\\src\\ocp\\study\\part05\\file1.txt";
    private static final String FILE2 = "C:\\projects\\Java-SE11-Upgrade-Exam\\src\\ocp\\study\\part05\\file2.txt";

    public static void main(String... unused) throws IOException {
        var move = Files.move(Path.of(FILE1), Path.of(FILE2), REPLACE_EXISTING);
        System.out.println(move);
        move = Files.move(Path.of(FILE2), Path.of(FILE1), REPLACE_EXISTING);
        System.out.println(move);
    }
}

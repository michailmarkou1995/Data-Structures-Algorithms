package org.family;

import org.family.famillytree.Generation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for simple App.
 */
public class FamilyTest {
    //    @Rule
//    public TemporaryFolder folder = new TemporaryFolder();
    @TempDir
    File anotherTempDir;

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    @Test
    void readcsv() {

        //with if null return -1
        Assertions.assertEquals(-1, Family.readcsv(null, 0, null, null, null));
        Generation gen = new Generation();
//        Assertions.assertThrows(FileNotFoundException.class, () -> {
//            //new Family().run();
//            Family.readcsv(null, 0, null, null, gen);
//            //throw new FileNotFoundException();
//        });

    }

    @Test
    void read_file_path_to_disk() {
//        File file = Mockito.mock(File.class);
//        try {
//            Mockito.verify(file).createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Mockito.when(file.exists()).thenReturn(true);
        //File file1 = new File("C:\\Users\\backt\\Downloads\\Compressed\\family_duck.csv");
        //System.out.println(file1.toString());
        //Assertions.assertThrows(IOException.class, () -> Family.read_file_path_to_disk(file1.toString()));
        /*  ac:\\users\\myuser\\downloads\\exercise\\family.csv */
        Assertions.assertThrows(InvalidPathException.class, () -> Family.read_file_path_to_disk("ac:\\"));
        Assertions.assertThrows(IOException.class, () -> Family.read_file_path_to_disk(""));
        Assertions.assertThrows(NullPointerException.class, () -> Family.read_file_path_to_disk(null));
    }

    @Test
    void read_file_input_menu(@TempDir Path tempDir) throws IOException {
//        Path numbers = tempDir.resolve("numbers.txt");
//
//        List<String> lines = Arrays.asList("1", "2", "3");
//        Files.write(numbers, lines);
//
//        assertAll(
//                () -> assertTrue(Files.exists(numbers), "File should exist"),
//                () -> assertLinesMatch(lines, Files.readAllLines(numbers)));
//        File file = Mockito.mock(File.class);
//        Path filePath = tempDir.resolve(file.toString());
//        Mockito.verify(file);
//        System.out.println(Mockito.verify(file));
//        File file1 = new File("C:\\Users\\backt\\Downloads\\Compressed\\family_duck1.csv");
//        Assertions.assertSame(file1.toString(), Family.read_file_input_menu(file1.toString()));
        //File fileDenied = new File("C:\\users\\backt\\downloads\\");
        File fileTemp = new File("family_duck.csv");
        Path pathFileTemp = tempDir.resolve(String.valueOf(fileTemp));
        //Path pathFileDenied= tempDir.resolve(String.valueOf(fileDenied));
        Path pathFileNull = null;

        List<String> lines = Arrays.asList("Grandpa Duck", "husband", "Grandma Duck");
        Files.write(pathFileTemp, lines);


        Assertions.assertThrows(NullPointerException.class, () -> Family.read_file_input_menu(pathFileNull.toString()));
        //Assertions.assertThrows(IOException.class, () -> Family.read_file_input_menu(String.valueOf(pathFileNull)));
        //Assertions.assertThrows(IOException.class, () -> Family.read_file_input_menu(String.valueOf((Object) null)));
        Assertions.assertThrows(NullPointerException.class, () -> Files.exists(null));  // Assertions.assertEquals(null, Files.exists(null));
        //Assertions.assertThrows(AccessDeniedException.class, () -> Files.write(pathFileDenied, lines));
        //Assertions.assertThrows(NoSuchFileException.class, () -> Files.write(Path.of("C:\\users\\back\\downloads"), lines));

        assertAll(
                () -> assertTrue(Files.exists(pathFileTemp), "File should exist"),
                () -> assertFalse(Files.isDirectory(pathFileTemp)),
                //() -> assertFalse(Files.exists(pathFileDenied)),
                () -> assertLinesMatch(lines, Files.readAllLines(pathFileTemp)));
        //() -> Assertions.assertSame(pathFileTemp.toString(), Family.read_file_input_menu(pathFileTemp.toString())));
    }

    @Test
    void check_and_find_Files() {
    }

    @Test
    void main() {
    }

    @Test
    void run() {
    }

    @Test
    void testReadcsv() {
    }

    @Test
    void testCheck_and_find_Files() {
    }

    @Test
    void testMain() {
    }

    @Test
    void testRun() {
    }
}

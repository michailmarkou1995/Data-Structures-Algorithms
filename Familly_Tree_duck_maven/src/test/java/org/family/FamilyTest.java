package org.family;

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
 * Unit test for Family Tree Traverse App.
 *
 * @author Michail Markou
 */
public class FamilyTest {

    @TempDir
    File anotherTempDir;

    @Test
    public void shouldAnswerWithTrue() {
        assertTrue(true);
    }

    /**
     *
     */
    @Test
    void testRead_file_path_to_disk() {
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

    /**
     * <p>When user "DOES NOT" Provide CLI args</p>
     *
     * @param tempDir Local dir Created by JUnit 5
     * @throws IOException
     */
    @Test
    void testRead_file_input_menu(@TempDir Path tempDir) throws IOException {

    }

    /**
     * <p>When user "DOES" Provide CLI args</p>
     *
     * @param tempDir Local dir Created by JUnit 5
     * @throws IOException
     */
    @Test
    void testRead_file_input_menu_args(@TempDir Path tempDir) throws IOException {
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

    /**
     * <p>Tests for any mismatch '"file" and Dir' extension of given input csvFile String toPath</p>
     *
     * @throws IOException if no Files.write then no actual file is created this ensures it has by not throwing,
     *                     throws == File error on "declaration" not in creation (definition)
     */
    @Test
    void testCheck_and_find_Files() throws IOException {
        assertTrue(this.anotherTempDir.isDirectory(), "Should be a directory ");

        // File creation
        File fileTemp = new File("family_duck.csv");
        File fileTempWrong = new File("family_duck.css");

        // Declare Files to Paths
        Path pathFileTemp = anotherTempDir.toPath().resolve(String.valueOf(fileTemp));
        Path pathFileTempWrong = anotherTempDir.toPath().resolve(String.valueOf(fileTempWrong));

        // Create actual File by write
        List<String> lines = Arrays.asList("Grandpa Duck", "husband", "Grandma Duck");  // if no write file No Exists empty dir
        Files.write(pathFileTemp, lines);
        Files.write(pathFileTempWrong, lines);

        // File Temp is OK
        assertAll(
                () -> assertTrue(Files.exists(pathFileTemp), "File should exist"),
                () -> assertTrue(Files.exists(pathFileTempWrong), "File should exist"),
                () -> assertLinesMatch(lines, Files.readAllLines(pathFileTemp)),
                () -> assertLinesMatch(lines, Files.readAllLines(pathFileTempWrong)));  // toPath() if its File not Path obj

        // correct does not throw
        Assertions.assertDoesNotThrow(() -> Family.check_and_find_Files(pathFileTemp, ".csv", "file"));
        // invalid extension throws or isDirectory
        Assertions.assertThrows(IOException.class, () -> Family.check_and_find_Files(pathFileTempWrong, ".csv", "file"));
        Assertions.assertThrows(IOException.class, () -> Family.check_and_find_Files(this.anotherTempDir.toPath(), ".csv", "file"));
    }


    @Test
    void testInput_checking_scenario_Loop() throws ArrayIndexOutOfBoundsException {

    }

    @Test
    void testReadcsv() {
    }

    @Test
    void testMain() {
    }

    @Test
    void testRun() {
    }
}

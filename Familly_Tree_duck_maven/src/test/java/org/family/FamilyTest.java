package org.family;

import org.family.famillytree.Generation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

        //File fileDenied = new File("C:\\users\\backt\\downloads\\");
        File fileTemp = new File("family_duck.csv");
        Path pathFileTemp = tempDir.resolve(String.valueOf(fileTemp));
        //Path pathFileDenied= tempDir.resolve(String.valueOf(fileDenied));
        Path pathFileNull = null;

        List<String> lines = Arrays.asList("Grandpa Duck", "husband", "Grandma Duck");
        Files.write(pathFileTemp, lines);

        assertAll(
                () -> assertFalse(Files.isDirectory(pathFileTemp)),
                () -> assertTrue(Files.exists(pathFileTemp), "File should exist"), // fileTemp.toPath()
                () -> assertLinesMatch(lines, Files.readAllLines(pathFileTemp)));

        Assertions.assertThrows(NullPointerException.class, () -> Family.read_file_input_menu(pathFileNull.toString()));
        Assertions.assertDoesNotThrow(() -> Family.read_file_input_menu(pathFileTemp.toString()));
        //Assertions.assertThrows(NullPointerException.class, () -> Files.exists(null));  // Assertions.assertEquals(null, Files.exists(null));
        //Assertions.assertThrows(IOException.class, () -> Family.read_file_input_menu(String.valueOf(pathFileNull)));
        //Assertions.assertThrows(IOException.class, () -> Family.read_file_input_menu(String.valueOf((Object) null)));
        //Assertions.assertThrows(AccessDeniedException.class, () -> Files.write(pathFileDenied, lines));
        //Assertions.assertThrows(NoSuchFileException.class, () -> Files.write(Path.of("C:\\users\\back\\downloads"), lines));
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

    /**
     * <h2>Game Of Thrones Testing</h2>
     */
    //@Test
    void testMenu_options_Game_Of_Thrones() {
        assertTrue(this.anotherTempDir.isDirectory(), "Should be a directory ");

        // File creation
        File fileTempDuck = new File("family_duck.csv");
        File fileTempGOT = new File("family_got.csv");

        // Declare Files to Paths
        Path pathFileTempDuck = anotherTempDir.toPath().resolve(String.valueOf(fileTempDuck));
        Path pathFileTempGot = anotherTempDir.toPath().resolve(String.valueOf(fileTempGOT));

        // Takes Path (e.g. pathFileTempGot) final Existed created file makes new file ready to write data
        File fileGot = new File(String.valueOf(pathFileTempGot));

        // Create actual File by write
        List<String[]> dataLinesDuck = new ArrayList<>(); // if no write file No Exists empty dir
        List<String[]> dataLinesGOT = new ArrayList<>(); // if no write file No Exists empty dir

        // Data Files of Game Of Thrones Testing below
        {
            // Data
            dataLinesGOT.add(new String[]{"Steffon Baratheon", "man"});
            dataLinesGOT.add(new String[]{"Steffon Baratheon", "father", "Robert Baratheon"});
            dataLinesGOT.add(new String[]{"Steffon Baratheon", "father", "Stannis Baratheon"});
            dataLinesGOT.add(new String[]{"Steffon Baratheon", "father", "Renly Baratheon"});
            dataLinesGOT.add(new String[]{"Steffon Baratheon", "husband", "Cassana Estermont"});

            dataLinesGOT.add(new String[]{"Cassana Estermont", "woman"});
            dataLinesGOT.add(new String[]{"Cassana Estermont", "mother", "Robert Baratheon"});
            dataLinesGOT.add(new String[]{"Cassana Estermont", "mother", "Stannis Baratheon"});
            dataLinesGOT.add(new String[]{"Cassana Estermont", "mother", "Renly Baratheon"});
            dataLinesGOT.add(new String[]{"Cassana Estermont", "wife", "Steffon Baratheon"});

            dataLinesGOT.add(new String[]{"Robert Baratheon", "man"});
            dataLinesGOT.add(new String[]{"Robert Baratheon", "father", "Gendry"});
            dataLinesGOT.add(new String[]{"Robert Baratheon", "father", "Joffrey Baratheon"});
            dataLinesGOT.add(new String[]{"Robert Baratheon", "father", "Myrchella Baratheon"});
            dataLinesGOT.add(new String[]{"Robert Baratheon", "father", "Tommen Baratheon"});
            dataLinesGOT.add(new String[]{"Robert Baratheon", "husband", "Cersei Lannister"});

            dataLinesGOT.add(new String[]{"Cersei Lannister", "woman"});
            dataLinesGOT.add(new String[]{"Cersei Lannister", "mother", "Joffrey Baratheon"});
            dataLinesGOT.add(new String[]{"Cersei Lannister", "mother", "Myrchella Baratheon"});
            dataLinesGOT.add(new String[]{"Cersei Lannister", "mother", "Tommen Baratheon"});
            dataLinesGOT.add(new String[]{"Cersei Lannister", "wife", "Robert Baratheon"});

            dataLinesGOT.add(new String[]{"Stannis Baratheon", "man"});
            dataLinesGOT.add(new String[]{"Stannis Baratheon", "father", "Shireen Baratheon"});
            dataLinesGOT.add(new String[]{"Stannis Baratheon", "husband", "Selyse Baratheon"});

            dataLinesGOT.add(new String[]{"Selyse Baratheon", "woman"});
            dataLinesGOT.add(new String[]{"Selyse Baratheon", "mother", "Shireen Baratheon"});
            dataLinesGOT.add(new String[]{"Selyse Baratheon", "wife", "Stannis Baratheon"});

            dataLinesGOT.add(new String[]{"Renly Baratheon", "man"});
            dataLinesGOT.add(new String[]{"Renly Baratheon", "husband", "Margaery Tyrell"});

            dataLinesGOT.add(new String[]{"Margaery Tyrell", "woman"});
            dataLinesGOT.add(new String[]{"Margaery Tyrell", "wife", "Renly Baratheon"});

            dataLinesGOT.add(new String[]{"Gendry", "man"});

            dataLinesGOT.add(new String[]{"Joffrey Baratheon", "man"});
            dataLinesGOT.add(new String[]{"Tommen Baratheon", "man"});
            dataLinesGOT.add(new String[]{"Myrchella Baratheon", "woman"});
            dataLinesGOT.add(new String[]{"Shireen Baratheon", "woman"});
        }

        // Convert Data structure to Csv and write Data in a file
        {
            try (PrintWriter pw = new PrintWriter(fileGot)) {
                dataLinesGOT.stream()
                        .map(this::convertToCSV)
                        .forEach(pw::println);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assertTrue(fileTempGOT.exists());
        }

        // Testing phase output below adjust values if needed
        {
            Family.family_MAIN_lastname = "Baratheon";

            System.out.println("******** SIMPLE PRINTING READ CSV ********");
            Family.menu_options(fileGot.toString(), null, 1, null, null, "test");
            System.out.println("***************************");
            System.out.println("******** Advanced Treemap Sorting full output ********");
            Family.menu_options(fileGot.toString(), null, 5, null, null, "test");
            System.out.println("***************************");

            System.out.print("\n######### " + "steffon uknownPerson " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "steffon", "uknownPerson", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "steffon steffon " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "steffon", "steffon", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "steffon cassana " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "steffon", "cassana", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "cersei robert " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "cersei", "robert", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "shireen steffon " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "shireen", "steffon", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "shireen robert " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "shireen", "robert", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "cersei shireen " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "cersei", "shireen", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "tommen joffrey " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "tommen", "joffrey", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "Renly Myrchella " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "Renly", "Myrchella", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "Renly Margaery " + "######### **BELOW**");
            Family.menu_options(fileGot.toString(), null, 3, "Renly", "Margaery", "test");
            clearDataStatic();
            System.out.println("################################");
        }
    }

    /**
     * <h2>Duck Looney Tunes Testing</h2>
     */
    @Test
    void testMenu_options_Duck_Looney_Tunes() {
        // Run Both DUCK + GAME OF THRONES TEST in SAME OUTPUT WINDOW CONSOLE !!!
        testMenu_options_Game_Of_Thrones();

        assertTrue(this.anotherTempDir.isDirectory(), "Should be a directory ");

        // File creation
        File fileTempDuck = new File("family_duck.csv");

        // Declare Files to Paths
        Path pathFileTempDuck = anotherTempDir.toPath().resolve(String.valueOf(fileTempDuck));

        // Takes Path (e.g. pathFileTempDuck) final Existed created file makes new file ready to write data
        File fileDuck = new File(String.valueOf(pathFileTempDuck));

        // Create actual File by write
        List<String[]> dataLinesDuck = new ArrayList<>(); // if no write file No Exists empty dir

        // Data Files of Duck Looney Tunes Testing below
        {
            // Data
            dataLinesDuck.add(new String[]{"Grandpa Duck", "male"});
            dataLinesDuck.add(new String[]{"Grandpa Duck", "father", "Louis Von Drake Duck"});
            dataLinesDuck.add(new String[]{"Grandpa Duck", "father", "Fanny Goose Duck"});
            dataLinesDuck.add(new String[]{"Grandpa Duck", "father", "Quackmore Duck"});
            dataLinesDuck.add(new String[]{"Grandpa Duck", "father", "Hortense Duck McDuck"});
            dataLinesDuck.add(new String[]{"Grandpa Duck", "father", "Jira Gander McDuck"});
            dataLinesDuck.add(new String[]{"Grandpa Duck", "father", "Scrooge McDuck"});
            dataLinesDuck.add(new String[]{"Grandpa Duck", "husband", "Grandma Duck"});

            dataLinesDuck.add(new String[]{"Grandma Duck", "female"});
            dataLinesDuck.add(new String[]{"Grandma Duck", "mother", "Louis Von Drake Duck"});
            dataLinesDuck.add(new String[]{"Grandma Duck", "mother", "Fanny Goose Duck"});
            dataLinesDuck.add(new String[]{"Grandma Duck", "mother", "Quackmore Duck"});
            dataLinesDuck.add(new String[]{"Grandma Duck", "mother", "Hortense Duck McDuck"});
            dataLinesDuck.add(new String[]{"Grandma Duck", "mother", "Jira Gander McDuck"});
            dataLinesDuck.add(new String[]{"Grandma Duck", "mother", "Scrooge McDuck"});
            dataLinesDuck.add(new String[]{"Grandma Duck", "wife", "Grandpa Duck"});

            dataLinesDuck.add(new String[]{"Louis Von Drake Duck", "female"});
            dataLinesDuck.add(new String[]{"Louis Von Drake Duck", "wife", "Ludwig Von Drake"});

            dataLinesDuck.add(new String[]{"Ludwig Von Drake", "male"});
            dataLinesDuck.add(new String[]{"Ludwig Von Drake", "husband", "Louis Von Drake Duck"});

            dataLinesDuck.add(new String[]{"Fanny Goose Duck", "female"});
            dataLinesDuck.add(new String[]{"Fanny Goose Duck", "mother", "Gus Goose"});
            dataLinesDuck.add(new String[]{"Fanny Goose Duck", "wife", "Poopy Goose"});

            dataLinesDuck.add(new String[]{"Poopy Goose", "male"});
            dataLinesDuck.add(new String[]{"Poopy Goose", "father", "Gus Goose"});
            dataLinesDuck.add(new String[]{"Poopy Goose", "husband", "Fanny Goose Duck"});

            dataLinesDuck.add(new String[]{"Jira Gander McDuck", "female"});
            dataLinesDuck.add(new String[]{"Jira Gander McDuck", "mother", "Gladstone Gander"});
            dataLinesDuck.add(new String[]{"Jira Gander McDuck", "wife", "Julious Gander"});

            dataLinesDuck.add(new String[]{"Julious Gander", "male"});
            dataLinesDuck.add(new String[]{"Julious Gander", "father", "Gladstone Gander"});
            dataLinesDuck.add(new String[]{"Julious Gander", "husband", "Jira Gander McDuck"});

            dataLinesDuck.add(new String[]{"Scrooge McDuck", "male"});
            dataLinesDuck.add(new String[]{"Gladstone Gander", "male"});
            dataLinesDuck.add(new String[]{"Gus Goose", "male"});

            dataLinesDuck.add(new String[]{"Quackmore Duck", "male"});
            dataLinesDuck.add(new String[]{"Quackmore Duck", "father", "Donald Fauntleroy Duck"});
            dataLinesDuck.add(new String[]{"Quackmore Duck", "father", "Dumbella Duck Duck"});
            dataLinesDuck.add(new String[]{"Quackmore Duck", "father", "Dux Duck"});
            dataLinesDuck.add(new String[]{"Quackmore Duck", "father", "Daisy Duck"});
            dataLinesDuck.add(new String[]{"Quackmore Duck", "father", "Helena spouch Duck"});
            dataLinesDuck.add(new String[]{"Quackmore Duck", "husband", "Hortense Duck McDuck"});

            dataLinesDuck.add(new String[]{"Hortense Duck McDuck", "female"});
            dataLinesDuck.add(new String[]{"Hortense Duck McDuck", "mother", "Donald Fauntleroy Duck"});
            dataLinesDuck.add(new String[]{"Hortense Duck McDuck", "mother", "Dumbella Duck Duck"});
            dataLinesDuck.add(new String[]{"Hortense Duck McDuck", "mother", "Dux Duck"});
            dataLinesDuck.add(new String[]{"Hortense Duck McDuck", "mother", "Daisy Duck"});
            dataLinesDuck.add(new String[]{"Hortense Duck McDuck", "mother", "Helena spouch Duck"});
            dataLinesDuck.add(new String[]{"Hortense Duck McDuck", "wife", "Quackmore Duck"});

            dataLinesDuck.add(new String[]{"Donald Fauntleroy Duck", "male"});

            dataLinesDuck.add(new String[]{"Dumbella Duck Duck", "female"});
            dataLinesDuck.add(new String[]{"Dumbella Duck Duck", "mother", "Huey Duck"});
            dataLinesDuck.add(new String[]{"Dumbella Duck Duck", "mother", "Dewey Duck"});
            dataLinesDuck.add(new String[]{"Dumbella Duck Duck", "mother", "Louie Duck"});
            dataLinesDuck.add(new String[]{"Dumbella Duck Duck", "wife", "Dux Duck"});

            dataLinesDuck.add(new String[]{"Dux Duck", "male"});
            dataLinesDuck.add(new String[]{"Dux Duck", "father", "Huey Duck"});
            dataLinesDuck.add(new String[]{"Dux Duck", "father", "Dewey Duck"});
            dataLinesDuck.add(new String[]{"Dux Duck", "father", "Louie Duck"});
            dataLinesDuck.add(new String[]{"Dux Duck", "husband", "Dumbella Duck Duck"});

            dataLinesDuck.add(new String[]{"Daisy Duck", "female"});

            dataLinesDuck.add(new String[]{"Helena spouch Duck", "female"});
            dataLinesDuck.add(new String[]{"Helena spouch Duck", "mother", "April"});
            dataLinesDuck.add(new String[]{"Helena spouch Duck", "mother", "May"});
            dataLinesDuck.add(new String[]{"Helena spouch Duck", "mother", "June"});
            dataLinesDuck.add(new String[]{"Helena spouch Duck", "wife", "pepe"});

            dataLinesDuck.add(new String[]{"pepe", "male"});
            dataLinesDuck.add(new String[]{"pepe", "father", "April"});
            dataLinesDuck.add(new String[]{"pepe", "father", "May"});
            dataLinesDuck.add(new String[]{"pepe", "father", "June"});
            dataLinesDuck.add(new String[]{"pepe", "husband", "Helena spouch Duck"});
        }
        
        // Convert Data structure to Csv and write Data in a file
        {
            try (PrintWriter pw = new PrintWriter(fileDuck)) {
                dataLinesDuck.stream()
                        .map(this::convertToCSV)
                        .forEach(pw::println);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            assertTrue(fileTempDuck.exists());
        }

        // Testing phase output below adjust values if needed
        {
            Family.family_MAIN_lastname = "Duck";

            System.out.println("******** SIMPLE PRINTING READ CSV ********");
            Family.menu_options(fileDuck.toString(), null, 1, null, null, "test");
            System.out.println("***************************");
            System.out.println("******** Advanced Treemap Sorting full output ********");
            Family.menu_options(fileDuck.toString(), null, 5, null, null, "test");
            System.out.println("***************************");

            System.out.print("\n######### " + "grandpa uknownPerson " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "grandpa", "uknownPerson", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "grandpa grandma " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "grandpa", "grandma", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "ludwig louis " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "ludwig", "louis", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "pepe fanny " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "pepe", "fanny", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "poopy fanny " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "poopy", "fanny", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "gus gladstone " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "gus", "gladstone", "test");
            clearDataStatic();
            System.out.println("################################");
//
            System.out.print("\n######### " + "grandpa donald " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "grandpa", "donald", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "grandma may " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "grandma", "may", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "fanny daisy " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "fanny", "daisy", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "dumbella dux " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "dumbella", "dux", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "jira julious " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "jira", "julious", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "pepe grandma " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "pepe", "grandma", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "April Dewey " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "April", "Dewey", "test");
            clearDataStatic();
            System.out.println("################################");

            System.out.print("\n######### " + "April June " + "######### **BELOW**");
            Family.menu_options(fileDuck.toString(), null, 3, "April", "June", "test");
            clearDataStatic();
            System.out.println("################################");
        }
    }

    /**
     * <p>formatting a single line of data, represented as an array of Strings</p>
     *
     * @param data String Array of DataLines passed to volatile Memory Data structure
     * @return Csv Ready formatted data
     */
    private String convertToCSV(String[] data) {
        return Stream.of(data)
                .map(this::escapeSpecialCharacters)
                .collect(Collectors.joining(","));
    }

    /**
     * <p>Handles special Character of file for optimal control of data</p>
     * <p>commas, quotes, new lines</p>
     * <p>e.g. "\rSteffon\"Baratheon" Converts it to -> " "Steffon"" Baratheon"" so they are parsed correctly</p>
     * <p><a href="https://docs.oracle.com/javase/8/docs/api/java/util/regex/Pattern.html"></a></p>
     *
     * @param data DataLine of String array from .add method
     * @return Clean Data Format for MS Excel or Google Sheets
     */
    private String escapeSpecialCharacters(String data) {
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            data = data.replace("\"", "\"\"");
            escapedData = "\"" + data + "\"";
        }
        return escapedData;
    }

    /**
     * <p>Clear Carry Data Per multiple run Instances Of Program "at once"</p>
     */
    private void clearDataStatic() {
        Family.they_are = null;//""
        Family.they_have = "";
        Family.all_list.clear();
        Family.who_is_husband_of_wife_List.clear();
        Family.who_is_father_mother_List.clear();
        Family.all_names.clear();
        Family.name_sorted_List.clear();
        Family.childs.clear();
        Family.records1.clear();
        Generation.getObj_wFatherMother.clear();
        Generation.getGetObj_wHusWife().clear();
        Family.exist_in_list = false;
        Family.isSiblings2 = false;
        Family.isSiblings1 = false;
        Family.isBlood_mix2 = false;
        Family.isBlood_mix1 = false;
        Family.isIsSiblings2Far = false;
        Family.isIsSiblings1Far = false;
        Family.isTop_root_parent1 = false;
        Family.isTop_root_parent2 = false;
        Family.name_N1p1_not_set = false;
        Family.name_N1p2_not_set = false;
        Family.name_N2p1_not_set = false;
        Family.name_N2p2_not_set = false;
        Family.take_once = false;
        Family.not_children_of_parent = false;
        Family.incest = false;
        Family.add_they_have = false;
        Family.blood_hus = false;
        Family.blood_wife = false;
        Family.N2 = null;//""
        Family.N1 = null;//""
        Family.parent1 = "a";
        Family.parent2 = "b";
        Family.new_N1_p1 = null;//""
        Family.new_N1_p2 = null;//""
        Family.new_N2_p1 = null;//""
        Family.new_N2_p2 = null;//""
        Family.N1_initials = null;//""
        Family.N2_initials = null;//""
        Family.N1_initials_child = null;
        Family.N2_initials_child = null;
        Family.husband = "";
        Family.wife = "";
        Family.root_of_root_p1 = null;
        Family.root_of_root_p2 = null;
    }

}

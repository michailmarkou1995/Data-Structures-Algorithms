package org.family;

import guru.nidi.graphviz.attribute.Style;
import guru.nidi.graphviz.engine.Format;
import guru.nidi.graphviz.engine.Graphviz;
import guru.nidi.graphviz.model.MutableGraph;
import guru.nidi.graphviz.parse.Parser;
import org.apache.commons.lang3.StringUtils;
import org.family.famillytree.Generation;
import org.family.famillytree.Parent;
import org.family.famillytree.Person;
import org.family.famillytree.Wife;
import org.family.famillytree.toons.Toons;
import picocli.CommandLine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <h1>Family Tree Traverse App.</h1>
 * <p>
 * add to "System Var" "winKey+S" a System variable path to jdk-16.0.2\bin
 * OR C:\Program Files (x86)\Common Files\Oracle\Java\javapath must have JRE 16.0.2 support
 * java --version
 * java -jar filename.jar must have 16.0.2 JDK (class file version 60)
 * </p>
 * <p>use GraphViz binaries installed with this command .\dot.exe -Tsvg .\toGraphviz.dot</p>
 *
 * @author Michail Markou
 */
@CommandLine.Command(name = "FamilyTreeApp", mixinStandardHelpOptions = true,
        version = "FamilyTree Profile Information V1.0",
        description = """
                Displays the FamilyTree profile information. ##########################USAGE#############################
                e.g. java -jar familytreeduck.jar -p family_duck.csv -l duck -o 3
                e.g. java -jar familytreeduck.jar -p family_duck.csv -l duck -o 3 fanny dumbella <- both names must or none
                e.g. java -jar familytreeduck.jar <- Run it without Arguments
                ##########################OPTIONS#############################
                --option=1 to read the family members lists
                --option=2 to create an alphabetically minimal sorted family members lists
                --option=5 to create a Full output sorted Family memberss
                --option=3 to use the relationship apps
                --option=4 to generate the .dot file + Generate SVG GraphViz Images
                ##########################COMMANDS#############################""")
public class Family implements Runnable {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern file_path_drive = Pattern.compile("\\\\");
    //private static Generation generation = new Generation();
    static List<String> all_names = new ArrayList<>();  // all names of csv
    static List<Generation> name_sorted_List = new ArrayList<>();  // Creating an Arraylist that stores objects <Person>
    static List<List<String>> records1 = new ArrayList<>();
    static HashMap<String, String> childs = new HashMap<>();
    static HashMap<String, Generation> who_is_father_mother_List = new HashMap<>();
    static HashMap<String, Generation> who_is_husband_of_wife_List = new HashMap<>();
    static Map<String, Generation> all_list = new HashMap<>();
    static AtomicInteger timesCsv = new AtomicInteger();
    static String N1, N2, parent1 = "a", parent2 = "b", family_MAIN_lastname, new_N1_p1, new_N1_p2,
            new_N2_p1, new_N2_p2, N1_initials, N2_initials, N1_initials_child, N2_initials_child,
            they_are, they_have = "", husband, wife, root_of_root_p1, root_of_root_p2, n_original;
    static StringBuilder file_path_canonical, getFile_path_canonical;
    static boolean isTop_root_parent1 = false, isTop_root_parent2 = false, name_N1p1_not_set = false,
            name_N1p2_not_set = false, name_N2p1_not_set = false, name_N2p2_not_set = false, isBlood_mix1 = false,
            isBlood_mix2 = false, take_once = false, exist_in_list = false, not_children_of_parent = false,
            isSiblings1 = false, isSiblings2 = false, isIsSiblings1Far = false, isIsSiblings2Far = false,
            incest = false, add_they_have = false, blood_hus = false, blood_wife = false, n1_full_given = false,
            n2_full_given = false;

    /**
     * <p>
     * picoCLI mvn repo
     * PicoCLI command Arguments non-positional non-required
     *
     * @see <a href="https://picocli.info/"</a>
     * </p>
     */
    @CommandLine.Option(names = {
            "-p",
            "--path"
    },
            required = false, description = "Path of csv file \"Absolute\" or \"Relative\"")
    private String filepath;
    @CommandLine.Option(names = {
            "-l",
            "--lastname"
    },
            required = false, description = "Last Name of the Family Incest Tree")
    private String lastName;
    @CommandLine.Option(names = {
            "-o",
            "--option"
    },
            required = false, description = "Option of Main Menu switch case loop")
    private String optionsMenu;
    @CommandLine.Option(names = {
            "-f1",
            "--firstname1"
    },
            required = false, description = "First Name of the user 1")
    private String firstName1;
    @CommandLine.Option(names = {
            "-f2",
            "--firstname2"
    },
            required = false, description = "First Name of the user 2")
    private String firstName2;

    public Family() {
    }

    /**
     * <h2>[Wrapper Function]</h2>
     * <p>
     * This function reads the csv file and stores its content on an Arraylist, printing the result
     * This function acts as component for every other function of this program
     * This function does or does not accept null When CLI args or Not are given
     * </p>
     *
     * @param path                Relative or absolute of System Path of existing File "ONLY" .csv accepted with max 1-3 Columns
     *                            [Person1, male] || [Person1, husband/wife, Person2] || [Person1, father/mother, Person2]
     * @param input               User Input of Main Menu Options 1-5
     *                            1: Read csv and display it
     *                            2: Create Person1 Dictionary sort of Name + Gender (simple sorting)
     *                            3: Find relations between 2 Persons
     *                            4: Create .dot File + Generate SVG files of GraphViz
     *                            5: Advanced Full sort of Person1 has Which Wife Which Son/'s if any
     * @param FName1_originalCase If not null user Selected Option 3 to Find Relationship between 2 Names (non-case-sensitive)
     * @param FName2_originalCase If not null user Selected Option 3 to Find Relationship between 2 Names (non-case-sensitive)
     * @param generation          Actually does not do anything for the moment
     */
    public static void readcsv(String path, int input, String FName1_originalCase, String FName2_originalCase,
                               Generation generation) {
        assert path != null : "Path is null";
        assert !path.isBlank() || !path.isEmpty() : "Path is Blank";
        String line;
        final String csvSplitBy = ",";
        String FName1 = "", FName2 = "";
        if (FName1_originalCase == null && FName2_originalCase == null) {
        } else {
            FName1 = Objects.requireNonNull(FName1_originalCase).toLowerCase(Locale.ROOT);
            FName2 = FName2_originalCase.toLowerCase(Locale.ROOT);
        }

        N1 = FName1;
        N2 = FName2;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            //this loop parses every line of the csv file and stores it on the Arraylist mylist
            while ((line = br.readLine()) != null) {
                String[] allStrings = line.split(csvSplitBy);
                input_checking_scenario_Loop(input, FName1_originalCase, FName2_originalCase, allStrings, generation);
            }
            // reduce amount of Data if duplicates e.g. just 25 names of duck tree
            all_names = all_names.stream()
                    .distinct()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * <h2>[Wrapper Function]</h2>
     * <p>
     * this function sorts the Arraylist alphabetically (dictionary sort) and then exports a sorted csv file
     * User gives Path where to be saved "Absolute" or "Relative"
     * it checks if same file also exists for Message Prompt if wanna replace or not
     * </p>
     */
    private static void sortcsv(String mode) {
        assert mode != null;
        if (!mode.equals("test")) {

              // calls the Class Comparable implementation
//            Collections.sort(name_sorted_List);
//            for (Generation s : name_sorted_List) { System.out.println(s); }

            //compares series of pairs of elements and based on result of compare
            // it sees if elements from that pair should be swapped or not
            name_sorted_List.sort((o1, o2) -> {
                // compare two instance of `Score` and return `int` as result.
                return ~o2.getName().compareTo(o1.getName());
                // use ~ to reverse order
            });
            //loop that prints the sorted result
            for (Generation generation : name_sorted_List) {
                System.out.println(generation);
            }
            // Create csv
            FileWriter writer;
            String csvFile;
            try {
                Scanner file_path = new Scanner(System.in);
                System.out.println("\n### Where you wanna save the export sorted List? \"Local\" or \"Absolute\" path ###");
                System.out.print("Path: ");
                csvFile = file_path.nextLine();
                //creating object writer with a path and charset format
                writer = new FileWriter(write_file_path_to_disk(csvFile), StandardCharsets.UTF_8);  // "resources/Duck_generated.csv"

                //this loop uses Filewriter "write" function to print the result on the newly created csv file
                for (Generation s : name_sorted_List) {
                    writer.write(s.toString());
                    writer.write(" \n");
                }

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>
     * [Wrapper Function]
     * When (no) Initial CLI args "ARE NOT" given acts as a Wrapper function to find all kind of Tree Relationships
     * between Name1, Name2 of given User Inputs
     * </p>
     *
     * @param csvFile an .csv only Constrained File loaded absolute or relative from Non-Volatile Memory Storage Device
     *                with max 1-3 Columns
     *                [Person1, male] || [Person1, husband/wife, Person2] || [Person1, father/mother, Person2]
     * @param input   a user input [3] of Options Menu on Main Program (run() thread) e.g. 3 find Name1 and Name2
     */
    private static void searchNameRelations(String csvFile, int input) {
        System.out.println("\n***Find Relationships***");
        System.out.print("Enter 1st First Names character (as unique ID) \nName: (e.g. grandpa or cersei) ");
        try (Scanner name1 = new Scanner(System.in)) {
            String Personname1 = name1.nextLine();
            Scanner name2 = new Scanner(System.in);
            System.out.print("Enter 2nd First Names character (as unique ID)\nName: (e.g. dumbella or robert) ");
            String Personname2 = name2.nextLine();
            readcsv(csvFile, input, Personname1, Personname2, null);
            case_scenario_checking_relations(input);
        } catch (Exception e) {
            System.out.println("There was a Input Problem");
            e.printStackTrace();
            System.exit(1);
        }
    }


    /**
     * <h2>[Wrapper Function]</h2>
     * <p>
     * When Initial CLI args "ARE" given acts as a Wrapper function to find all kind of Tree Relationships
     * between Name1, Name2 of given User Inputs
     * </p>
     *
     * @param csvFile   an .csv only Constrained File loaded absolute or relative from Non-Volatile Memory Storage Device
     *                  with max 1-3 Columns
     *                  [Person1, male] || [Person1, husband/wife, Person2] || [Person1, father/mother, Person2]
     * @param argName1  a user input [3] of argName1 to compare with argsName2 for Tree relations
     * @param argsName2 a user input [3] of argsName2 to compare with argName1 for Tree relations
     */
    private static void searchNameRelations(String csvFile, String argName1, String argsName2) {
        System.out.println("\n***Find Relationships***");
        try {
            readcsv(csvFile, 3, argName1, argsName2, null);
            case_scenario_checking_relations(3);
        } catch (Exception e) {
            System.out.println("There was a Input Problem");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * <p>
     * Creates .dot file after Reading CSV from "component" function readcsv(**extra_fields)
     * <strong>It does not ask for Output Directory File Path "ONLY" saves it Relative to this familytreeapp.jar OR
     * familytreeapp.exe if it was installed from Packer</strong>
     * </p>
     *
     * @param csvFile    an .csv only Constrained File loaded absolute or relative from Non-Volatile Memory Storage Device
     *                   with max 1-3 Columns
     *                   [Person1, male] || [Person1, husband/wife, Person2] || [Person1, father/mother, Person2]
     * @param input      User Option [4]
     * @param generation Actually does not do anything for the moment
     * @param mode       if its test don't save
     */
    private static void createDot(String csvFile, int input, Generation generation, String mode) {
        readcsv(csvFile, input, null, null, generation);

        assert mode != null;
        if (!mode.equals("test")) {
            FileWriter writerF;
//        StringBuilder csvFileExtension;

            try {
//            Scanner file_path = new Scanner(System.in);
//            System.out.println("\n### Where you wanna save the export sorted List? \"Local\" or \"Absolute\" path ###");
//            System.out.print("Path: ");
//            csvFileExtension = new StringBuilder(file_path.nextLine());
                /* Gets File Path from readcsv and saves it in the same place */
                writerF = new FileWriter(file_path_canonical
                        .append("\\toGraphViz_")
                        .append(family_MAIN_lastname)
                        .append(".dot").toString(), StandardCharsets.UTF_8);
                file_path_canonical = getFile_path_canonical;

                writerF.write("digraph " + family_MAIN_lastname + " {\n");
                writerF.write("rankdir=LR;\n");
                writerF.write("size=\"8,5\"\n");
                writerF.write("node [shape = rectangle] [color=black];\n");

                who_is_father_mother_List.forEach((key, value) -> {
                    System.out.println("\"" + value.getName() + "\" -> \"" + value.getChild() + "\" [label=\""
                            + value.getRelated() + "\"];");
                    try {
                        writerF.write("\"" + value.getName() + "\" -> \"" + value.getChild() + "\" [label=\""
                                + value.getRelated() + "\"];\n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                writerF.write("}");
                writerF.close();
            } catch (IOException e) { //| URISyntaxException
                e.printStackTrace();
            }
        }
    }

    //after readcsv - input_checking_scenario_Loop - comes this (2)
    private static void case_scenario_checking_relations(int input) {

        // Check if two Inputs have same parents
        for (Generation g : Generation.getObj_wFatherMother) {
            String names_children_equals_full = g.getChild().toLowerCase();
            String names_father_equals_full = g.getName().toLowerCase();

            // if Name 1 or Name 2 is Full or partial given
            if (N1.toLowerCase().contains(names_children_equals_full)
                    || N2.toLowerCase().contains(names_children_equals_full)
                    || names_children_equals_full.contains(N1.toLowerCase())
                    || names_children_equals_full.contains(N2.toLowerCase())
                    || N1.toLowerCase().contains(names_father_equals_full)
                    || N2.toLowerCase().contains(names_father_equals_full)
                    || names_father_equals_full.contains(N1.toLowerCase())
                    || names_father_equals_full.contains(N2.toLowerCase())) {
                if (N1.equals(names_children_equals_full)) {  // N1 is lowercased anyway later
                    isSiblings1 = true;
                    parent1 = names_father_equals_full;
                    n1_full_given = true;
                } else if (N1.equals(names_father_equals_full)) {
                    parent1 = names_father_equals_full;
                    n1_full_given = true;
                } else if (names_children_equals_full.contains(N1)) {  // if full or partial keep lowecase the first Name
                    N1 = g.getChild();
                    N1 = Arrays.stream(N1.split(" ")).toArray()[0].toString().toLowerCase();
                    isSiblings1 = true;
                    parent1 = names_father_equals_full;
                    n1_full_given = true;
                } else if (names_father_equals_full.contains(N1)) {  // if full or partial keep lowecase the first Name
                    N1 = g.getName();
                    N1 = Arrays.stream(N1.split(" ")).toArray()[0].toString().toLowerCase();
                    //isSiblings1 = false;
                    parent1 = names_father_equals_full;
                    n1_full_given = true;
                } /* break name per child or per parent and set apropriate flags above and below */

                if (N2.equals(names_children_equals_full)) {   // N2 is lowercased anyway later
                    isSiblings2 = true;
                    parent2 = names_father_equals_full;
                    n2_full_given = true;
                } else if (N2.equals(names_father_equals_full)) {
                    parent2 = names_father_equals_full;
                    n2_full_given = true;
                } else if (names_children_equals_full.contains(N2)) {
                    N2 = g.getChild();
                    N2 = Arrays.stream(N2.split(" ")).toArray()[0].toString().toLowerCase();  // if full or partial keep lowecase the first Name
                    isSiblings2 = true;
                    parent2 = names_father_equals_full;
                    n2_full_given = true;
                } else if (names_father_equals_full.contains(N2)) {
                    N2 = g.getName();
                    N2 = Arrays.stream(N2.split(" ")).toArray()[0].toString().toLowerCase();  // if full or partial keep lowecase the first Name
                    //isSiblings2 = false;
                    parent2 = names_father_equals_full;
                    n2_full_given = true;
                }

                if (!parent1.equals(parent2) && (isSiblings1 && isSiblings2)) {
                    who_is_husband_of_wife_List.forEach((key, value) -> {
                        if (value.getName().equals(parent1) && value.getHusband().equals(parent2)
                                || value.getName().equals(parent2) && value.getHusband().equals(parent1)) {
                            isIsSiblings1Far = true;
                            isIsSiblings2Far = true;
                        }
                    });
                }
            }

            String[] names_children_equals = g.getChild().toLowerCase().split(" ");
            String names_father_equals = g.getName();
            for (String names_children_equal : names_children_equals) {
                if (N1.equals(names_children_equal)) {
                    isSiblings1 = true;
                    parent1 = names_father_equals;
                }
                if (N2.equals(names_children_equal)) {
                    isSiblings2 = true;
                    parent2 = names_father_equals;
                }

                // makes in memory Data structure file with relations of wife or husband e.g. this has this as wife
                // so its diff parents and they are far siblings(not the parent included as root)
                if (!parent1.equals(parent2) && (isSiblings1 && isSiblings2)) {
                    who_is_husband_of_wife_List.forEach((key, value) -> {
                        if (value.getName().equals(parent1) && value.getHusband().equals(parent2)
                                || value.getName().equals(parent2) && value.getHusband().equals(parent1)) {
                            isIsSiblings1Far = true;
                            isIsSiblings2Far = true;
                        }
                    });
                }
            }
        }

        var temp_exist_1 = false;
        var temp_exist_2 = false;
        for (String all_name : all_names) {
            if (N1.equals(all_name))
                temp_exist_1 = true;
            if (N2.equals(all_name))
                temp_exist_2 = true;

            if (temp_exist_1 && temp_exist_2)
                exist_in_list = true;
        }

        // finds from 2 Inputs relation between another Recursion of Tree backwards (including root)
        find_shared_root(N1, N2);

        if (input == 3) {
            if (add_they_have) {
                AtomicInteger count = new AtomicInteger();
                if (!not_children_of_parent) {
                    childs.forEach((key, value) -> {
                        if (childs.size() == 1) {
                            they_have += " and this is their child [" + value + "]";
                        } else {
                            count.getAndIncrement();
                            if (count.get() == 1)
                                they_have += "and have these children's [" + value;
                            else if (count.get() != 1 && count.get() != childs.size())
                                they_have += "," + value;
                            else they_have += "," + value + "]";
                        }
                    });
                }
            }
        }
        temp_exist_1 = false;
        temp_exist_2 = false;
        for (String all_name : all_names) {
            if (N1.equals(all_name))
                temp_exist_1 = true;
            if (N2.equals(all_name))
                temp_exist_2 = true;

            if (temp_exist_1 && temp_exist_2)
                exist_in_list = true;
        }

        /*Actual Printing to Screen AKA output Stream forwarding messages of the results*/
        // gets hairy
        if (!incest) {
            if (!(isIsSiblings1Far && isIsSiblings2Far)) {
                if (isSiblings1 && isSiblings2 && N1_initials != null && N2_initials != null) {
                    if (N1.contains(family_MAIN_lastname) && N2.contains(family_MAIN_lastname))
                        System.out.println("\nFar Siblings in arms");
                    else if (!N1.contains(family_MAIN_lastname) && !N2.contains(family_MAIN_lastname))
                        System.out.println("\nFar Bastards in Arms");
                    else if (!N1.contains(family_MAIN_lastname) && N2.contains(family_MAIN_lastname)
                            || N1.contains(family_MAIN_lastname) && !N2.contains(family_MAIN_lastname)
                            || !N1.contains(family_MAIN_lastname) && !N2.contains(family_MAIN_lastname)
                            && N1_initials.contains(family_MAIN_lastname) || N2_initials.contains(family_MAIN_lastname))
                        System.out.println("\n Far not related child's with \"in\" Blood \"" + family_MAIN_lastname + "\" Parent");
                    else
                        System.out.println("\nFar Siblings in arms");
                } else if (N1_initials != null && N1_initials.contains(family_MAIN_lastname)
                        && N2_initials != null && N2_initials.contains(family_MAIN_lastname)
                        && !N1.contains(family_MAIN_lastname)
                        || !N2.contains(family_MAIN_lastname) && they_are == null && exist_in_list)
                    if (N1_initials != null && N2_initials != null && N1_initials.contains(family_MAIN_lastname) && N2_initials.contains(family_MAIN_lastname))
                        System.out.println("\nFar Bastard child");
                    else
                        System.out.println("\n" + they_are + " (not related)");//System.out.println("\nFar Bastard child");
                else if (!they_have.isEmpty())
                    System.out.println("\n" + they_are + " (not related) " + they_have);
                else if ((N1_initials != null && N1_initials.contains(family_MAIN_lastname))
                        && (N2_initials != null && N2_initials.contains(family_MAIN_lastname)))
                    System.out.println("\nFar Child");
//                else if (N1.toLowerCase().contains(family_MAIN_lastname.toLowerCase()) && N2.toLowerCase().contains(family_MAIN_lastname.toLowerCase()))
//                    System.out.println("\nSiblings in arms");
                else System.out.println("\n" + they_are + " (not related)");
            } else System.out.println("\nSiblings in arms");
        } else if (N1.contains(family_MAIN_lastname) && N2.contains(family_MAIN_lastname) && they_have == null) {
            System.out.println("\n" + they_are + " (related)");
        } else if (N1.toLowerCase().contains(family_MAIN_lastname.toLowerCase()) && N2.toLowerCase().contains(family_MAIN_lastname.toLowerCase())
                && they_have.isEmpty()) {
            System.out.println("\n" + they_are + " (related)");
        } else if (N1.contains(family_MAIN_lastname) && N2.contains(family_MAIN_lastname) && they_have != null) {
            System.out.println("\n" + they_are + " (related) " + they_have);
        } else if (root_of_root_p1 != null && root_of_root_p2 != null
                && root_of_root_p1.contains(family_MAIN_lastname) && root_of_root_p2.contains(family_MAIN_lastname) && they_have != null) {
            System.out.println("\n" + they_are + " (not related) " + they_have);
        } else if (root_of_root_p1 != null && root_of_root_p2 != null
                && root_of_root_p1.contains(family_MAIN_lastname) && root_of_root_p2.contains(family_MAIN_lastname)) {
            System.out.println("\n" + they_are + " (not related)");
        } else if (they_have != null && they_are != null && n_original != null) {
            System.out.println("\n" + they_are + " (not related) " + they_have);
        } else if (they_have != null && they_are != null) {
            System.out.println("\n" + they_are + " (related) " + they_have);
        } else if (isBlood_mix1 || isBlood_mix2) {
            System.out.println("\nFar siblings in Arm");
        } else
            System.out.println("\n" + they_are + " (related) " + they_have);
    }

    /**
     * <h2>Recursion to Traverse Tree</h2>
     * <pre>
     * TODO: Algorithm improvement
     * works 100% but it can be improved as direct full name capture and compare below in algorithm and in print above
     * state to become way more efficient rather getting different names capture result will be less
     * verbose algorithm + print statements
     * Notes to remember root of root foreign name do not append husbands lastname rather check
     * if they are together + flag it as foreign marriage like blood mix and not incest relationships
     * </pre>
     *
     * <p>This Traverse back the Tree Relationships, finds and holds Names of
     * Parents + child's and if they are Incest or not
     * </p>
     *
     * @param FName1 Name1 of user Input to compare Non-Case-sensitive
     * @param FName2 Name2 of user Input to compare Non-Case-sensitive
     */
    // gives 2 Names Finds each of those their parent and check if they are incest or not by raise flag till reaches
    // root of root state which means the top has no more top e.g. a Name has No Parent
    private static void find_shared_root(String FName1, String FName2) {
        if (isTop_root_parent1 && isTop_root_parent2) {
            if (N1_initials != null && N1_initials.toLowerCase().contains(N1))
                N1 = N1_initials;  // input 1 from keyboard
            if (N1_initials_child != null && N1_initials_child.toLowerCase().contains(N1))
                N1 = N1_initials_child;  // input 1 from keyboard
            if (N2_initials != null && N2_initials.toLowerCase().contains(N2))
                N2 = N2_initials;  // input 2 from keyboard
            if (N2_initials_child != null && N2_initials_child.toLowerCase().contains(N2))
                N2 = N2_initials_child;  // input 1 from keyboard
            if (root_of_root_p1 != null && root_of_root_p2 != null) {
                if (!root_of_root_p1.contains(family_MAIN_lastname)) {
                    n_original = root_of_root_p1;
                    root_of_root_p1 += " " + family_MAIN_lastname;
                    if (root_of_root_p1.contains(N1))
                        N1 = root_of_root_p1;
                    else if (root_of_root_p2.contains(N2))
                        N2 = root_of_root_p1;
                } else {
                    for (var name : all_names) {
                        if (name.toLowerCase().contains(N1) || name.contains(N1))
                            N1 = name;
                        if (name.toLowerCase().contains(N2) || name.contains(N2))
                            N2 = name;
                    }
                }
                if (!root_of_root_p2.contains(family_MAIN_lastname)) {
                    n_original = root_of_root_p2;
                    root_of_root_p2 += " " + family_MAIN_lastname;
                    if (root_of_root_p2.contains(N1))
                        N1 = root_of_root_p2;
                    else if (root_of_root_p2.contains(N2))
                        N2 = root_of_root_p2;
                } else {
                    for (var name : all_names) {
                        if (name.toLowerCase().contains(N1) || name.contains(N1))
                            N1 = name;
                        if (name.toLowerCase().contains(N2) || name.contains(N2))
                            N2 = name;
                    }
                }
            }

            return;
        }
        boolean yes_new_parentS1 = false, yes_new_parentS2 = false;
        name_N1p1_not_set = false;
        name_N1p2_not_set = false;
        name_N2p1_not_set = false;
        name_N2p2_not_set = false;
        String[] clear_n1, clear_n2;
        for (Generation g : Generation.getObj_wFatherMother) {
            String whole_name = g.getName();
            String[] ch1_arr = g.getChild().toLowerCase().split(" ");
            String[] p_arr = g.getName().toLowerCase().split(" ");
            String ch1 = ch1_arr[0];

            /* if full name is given instead of part then keep the first part as usual*/
            if (n1_full_given) {
                //System.out.println("test1");
                //isN1_full_given_falseIt=true;
                if (FName1 != null) {
//                    for (String name : all_names) { if (name.contains(FName1)) FName1=name; }
                    FName1 = Arrays.stream(FName1.split(" ")).toArray()[0].toString();
                }
                n1_full_given = false;
            }
            if (n2_full_given) {
                //System.out.println("test2");
                //isN2_full_given_falseIt=true;
                if (FName2 != null) {
//                    for (String name : all_names) { if (name.contains(FName2)) FName2=name;}
                    FName2 = Arrays.stream(FName2.split(" ")).toArray()[0].toString();

                }
                //FName2= Arrays.toString(Arrays.stream(FName2.split(" ")).filter().collect(Collectors.toList()));
                n1_full_given = false;
            }

            if (ch1.equals(FName1)) {  //&& !isN1_full_given_falseIt
                name_N1p1_not_set = !name_N1p1_not_set;  // false
                name_N1p2_not_set = !name_N1p2_not_set;  // false // switch prepare
                if (name_N1p1_not_set) {
                    root_of_root_p1 = whole_name;
                    new_N1_p1 = g.getName();  // gets full name of N1p1 (Name 1 Parent 1)
                    if (N1_initials == null) {
                        N1_initials = g.getName();  // gets Parent Name and holds it
                        N1_initials_child = g.getChild();  // gets child of Parent and holds it
                    }
                    name_N1p1_not_set = true;
                    name_N1p2_not_set = false;
                }
                if (name_N1p2_not_set) {
                    root_of_root_p2 = whole_name;
                    new_N1_p2 = g.getName();  // gets full name of N1p2 (Name 1 Parent 2)
                    name_N1p2_not_set = true;
                }
                yes_new_parentS1 = true;
                name_N1p2_not_set = false;  // false so next recursion stack loop will have false -> true and no more exec
            }
            if (ch1.equals(FName2)) {  // && !isN2_full_given_falseIt
                name_N2p1_not_set = !name_N2p1_not_set;  // false
                name_N2p2_not_set = !name_N2p2_not_set;  // false
                if (name_N2p1_not_set) {
                    new_N2_p1 = g.getName();  // gets full name of N2p1 (Name 2 Parent 1)
                    if (N2_initials == null) {
                        N2_initials = g.getName();
                        N2_initials_child = g.getChild();
                    }
                    name_N2p2_not_set = false;
                }
                if (name_N2p2_not_set) {
                    new_N2_p2 = g.getName();  // gets full name of N2p2 (Name 2 Parent 2)
                    N2_initials = g.getChild();
                    name_N2p2_not_set = true;
                }
                yes_new_parentS2 = true;
                name_N2p2_not_set = false;
            }

            if (!take_once && FName1 != null && FName1.equals(p_arr[0])) {
                take_once = true;
                N1_initials = g.getName();
                N1_initials_child = g.getChild();
            }
            if (!take_once && FName2 != null && FName2.equals(p_arr[0])) {
                take_once = true;
                N2_initials = g.getName();
                N2_initials_child = g.getChild();
            }
        }

        // reached the end of Tree reverse
        if (!yes_new_parentS1 && !yes_new_parentS2) {
            isTop_root_parent1 = true;
            isTop_root_parent2 = true;
        } else {
            //  keeps one of 2 Parents with the "Incest" name either both are Incest or Blood mix so its keeps the Incest one #1
            if (!(new_N1_p1 == null) && !(new_N1_p2 == null)) {
                if (new_N1_p1.contains(family_MAIN_lastname) && new_N1_p2.contains(family_MAIN_lastname)) {
                    clear_n1 = new_N1_p1.toLowerCase().split(" ");
                    new_N1_p1 = clear_n1[0];
                } else if (new_N1_p1.contains(family_MAIN_lastname) || new_N1_p2.contains(family_MAIN_lastname)) {
                    isBlood_mix1 = true;
                    if (new_N1_p1.contains(family_MAIN_lastname)) {
                        clear_n1 = new_N1_p1.toLowerCase().split(" ");
                        new_N1_p1 = clear_n1[0];
                    } else if (new_N1_p2.contains(family_MAIN_lastname)) {
                        clear_n1 = new_N1_p2.toLowerCase().split(" ");
                        new_N1_p2 = clear_n1[0];
                    }
                }
            }

            //  keeps one of 2 Parents with the "Incest" name either both are Incest or Blood mix so its keeps the Incest one #2
            if (!(new_N2_p1 == null) && !(new_N2_p2 == null)) {
                who_is_husband_of_wife_List.entrySet().forEach(entry -> {
                    if (entry.getValue().getName().contains(new_N2_p1) && entry.getValue().getHusband().contains(new_N2_p2)
                            || entry.getValue().getName().contains(new_N2_p2) && entry.getValue().getHusband().contains(new_N2_p1)) {
                        /* but not of root of root only locally its seems like it, it works 100% but must make it better */
                        //System.out.println("Together");
                        root_of_root_p1 = new_N2_p1;
                        root_of_root_p2 = new_N2_p2;
                    }
                });
                if (new_N2_p1.contains(family_MAIN_lastname) && new_N2_p2.contains(family_MAIN_lastname)) {
                    if (root_of_root_p1 == null) {
                        who_is_husband_of_wife_List.entrySet().forEach(entry -> {
                            if (entry.getValue().getName().contains(new_N2_p1))
                                root_of_root_p1 = entry.getValue().getName();
                            else if (entry.getValue().getHusband().contains(new_N2_p1))
                                root_of_root_p1 = entry.getValue().getHusband();
                        });
                    }
                    if (root_of_root_p2 == null) {
                        who_is_husband_of_wife_List.entrySet().forEach(entry -> {
                            if (entry.getValue().getName().contains(new_N2_p2))
                                root_of_root_p2 = entry.getValue().getName();
                            else if (entry.getValue().getHusband().contains(new_N2_p2))
                                root_of_root_p2 = entry.getValue().getHusband();
                        });
                    }
                    clear_n2 = new_N2_p1.toLowerCase().split(" ");
                    new_N2_p1 = clear_n2[0];
                } else {
                    isBlood_mix2 = true;
                    if (new_N2_p1.contains(family_MAIN_lastname)) {
                        clear_n2 = new_N2_p1.toLowerCase().split(" ");
                        new_N2_p1 = clear_n2[0];
                    } else if (new_N2_p2.contains(family_MAIN_lastname)) {
                        clear_n2 = new_N2_p2.toLowerCase().split(" ");
                        new_N2_p2 = clear_n2[0];
                    }
                }
            }
        }
        // finds from 2 Inputs relation between another Recursion of Tree backwards (including root)
        find_shared_root(new_N1_p1, new_N2_p1);
    }

    /**
     * <h2>Main Logic of Tree</h2>
     * <p>
     * Based on Users [1-5] Option Input, before sending to output Stream here finds
     * Relation Loop Function that includes all scenarios following by a Recursion OR
     * parses all input stream from csv and outputs it OR
     * Creates Sorted List's
     * </p>
     *
     * @param input      User Input of Main Menu Options 1-5
     *                   1: Read csv and display it
     *                   2: Create Person1 Dictionary sort of Name + Gender (simple sorting)
     *                   3: Find relations between 2 Persons
     *                   4: Create .dot File + Generate SVG files of GraphViz
     *                   5: Advanced Full sort of Person1 has Which Wife Which Son/'s if any
     * @param FName1     Name1 of User Input for Tree Map Relation (non-case-sensitive)
     * @param FName2     Name2 of User Input for Tree Map Relation (non-case-sensitive)
     * @param allStrings Actual Bytes from readcsv() e.g. The Fields of Columns Data themselves
     * @param generation Actually does not do anything for the moment
     * @throws ArrayIndexOutOfBoundsException if more than 3 columns in CSV are available
     */
    static void input_checking_scenario_Loop(int input, String FName1, String FName2, String[] allStrings,
                                             Generation generation) throws ArrayIndexOutOfBoundsException {

        // Make CSV input for Only Name Sections for Cols NOT EQUAL to 1 which will be "relations" part
        // Make Only name Sections First capitalize
        for (int i = 0; i < allStrings.length; i++) {
            String capFirstMove;
            String[] capFirst;
            StringBuilder save_new_Cap = new StringBuilder();
            if (i != 1) {
                allStrings[i] = allStrings[i].replace("\uFEFF", "");
                capFirstMove = allStrings[i];
                capFirst = capFirstMove.split(" ");
                for (int j = 0; j < Objects.requireNonNull(capFirst).length; j++) {
                    String makeCap = capFirst[j];

                    // csv first Start Of File has a special character (\uFEFF AKA== "BOM" char utf-8)
                    // so we just from charAt(0) to 1 only and only then!
                    capFirst[j] = makeCap.toUpperCase().charAt(0) + makeCap.substring(1);

                    if (j == 0)
                        save_new_Cap.append(capFirst[j]);
                    else
                        save_new_Cap.append(" ").append(capFirst[j]);
                    if (j == capFirst.length - 1)
                        allStrings[i] = save_new_Cap.toString();
                }
            }
        }

        // Capitalized copy of String array
        String[] allStringsLC = allStrings.clone();

        // csv Max 3 cols
        if (allStrings.length > 3)
            throw new ArrayIndexOutOfBoundsException("csv file MUST not exceed 3 columns [1-3]");

        for (int i = 0; i < allStrings.length; i++) {
            allStringsLC[i] = allStrings[i].toLowerCase(Locale.ROOT); //str.substring(0,1).toUpperCase()
        }

        // menu 1 print List of CSV after read
        if (input == 1)
            System.out.println(Arrays.toString(allStrings));

        // sort csv used
        if (input != 3 && input != 4)
            if (allStrings.length == 2) {
                name_sorted_List.add(new Person(allStrings[0], allStrings[1]));
            }

        // Search and Fine Relations part
        if (input == 3) {

            //  Csv name list
            all_names.add(allStrings[0]);

            if (allStringsLC[0].contains(FName1.toLowerCase())) {
                records1.add(Arrays.asList(allStrings));
            }
            // only if they have 3 csv columns
            if (allStrings.length == 3) {

                //  Csv name list
                all_names.add(allStrings[2]);

                // Arrange as Who has wife or husband name list
                if ((allStrings[1].equals("wife")))
                    who_is_husband_of_wife_List.put(String.valueOf(new Random().nextInt() & Integer.MAX_VALUE),
                            new Wife(allStrings[0], allStrings[1], allStrings[2]));  // zero out the sign bit 0x7fffffff

                // check father or mother both have in common for Create ".dot" File .. DB simulation with Hashmap uses PK
                // as new Random and Values the object to access itself
                if ((allStrings[1].equals("mother")) || (allStrings[1].equals("father"))) {
                    who_is_father_mother_List.put(String.valueOf(Math.abs(new Random().nextInt())),
                            new Parent(allStrings[0], allStrings[1], allStrings[2]));
                }
                // Check to see if one of the two names give input IS NOT a Child to the other input Flag while adding the (as)child
                if ((allStringsLC[0].contains(FName1.toLowerCase()) && (allStrings[1].contains("mother") || allStrings[1].contains("father"))
                        || allStringsLC[0].contains(FName2.toLowerCase()) && (allStrings[1].contains("mother") || allStrings[1].contains("father")))) {
                    if ((allStringsLC[0].contains(FName1.toLowerCase()) && (allStrings[1].contains("mother") || allStrings[1].contains("father"))
                            == allStringsLC[2].contains(FName2.toLowerCase()))
                            || (allStringsLC[0].contains(FName2.toLowerCase()) && (allStrings[1].contains("mother") || allStrings[1].contains("father"))
                            == allStringsLC[2].contains(FName1.toLowerCase()))) {
                        not_children_of_parent = true;
                    }
                    childs.put(allStrings[2], allStrings[2]);
                }

                // Check for relation and incest from 2 Inputs given from user
                // e.g. if 2 of the inputs have 3 cols mean they are something incest or not
                if (allStringsLC[0].contains(FName1.toLowerCase()) && allStringsLC[2].contains(FName2.toLowerCase())
                        || allStringsLC[2].contains(FName1.toLowerCase()) && allStringsLC[0].contains(FName2.toLowerCase())) {
                    if (allStrings[1].equals("husband")) {
                        husband = allStrings[1];
                        String[] hus_name = allStrings[0].split(" ");
                        for (String s : hus_name) {
                            if (s.equals(family_MAIN_lastname)) {
                                blood_hus = true;
                                break;
                            }
                        }
                    }
                    if (allStrings[1].equals("wife")) {
                        wife = allStrings[1];
                        String[] hus_wife = allStrings[0].split(" ");
                        for (String s : hus_wife) {
                            if (s.equals(family_MAIN_lastname)) {
                                blood_wife = true;
                                break;
                            }
                        }
                    }
                    if (blood_hus == blood_wife) incest = true;

                    they_are = allStrings[0] + " is " + allStrings[1] + " of " + allStrings[2];

                    add_they_have = true;
                }
            }
        }

        if (input == 4) {
            //myList.clear();
            if (allStrings.length == 3) {
                if ((allStrings[1].equals("mother")) || (allStrings[1].equals("father"))) {
                    who_is_father_mother_List.put(String.valueOf(new Random().nextInt() & Integer.MAX_VALUE),
                            new Parent(allStrings[0], allStrings[1], allStrings[2]));  //new AtomicInteger(1).incrementAndGet()
                }
            }
        }
        if (input == 5) {
            if ((allStrings[1].equals("husband"))) {
                if (all_list.size() != 0) {
                    if (all_list.containsKey(allStrings[0]))
                        all_list.get(allStrings[0]).setWife(allStrings[2]);
                    else
                        all_list.put(allStrings[0], new Generation(allStrings[0], allStrings[1], allStrings[2], null, 0, 0));
                } else
                    all_list.put(allStrings[0], new Generation(allStrings[0], allStrings[1], allStrings[2], null, 0, 0));
            }
            if ((allStrings[1].equals("father"))) {
                if (all_list.size() != 0) {
                    if (all_list.containsKey(allStrings[0])) {
                        all_list.get(allStrings[0]).setChildConcat(allStrings[2]);
                        //all_list.get(allStrings[0]).setChild_list1_obj(allStrings[2]);
                    } else
                        all_list.put(allStrings[0], new Generation(allStrings[0], allStrings[1], null, allStrings[2], 0, 0));
                } else {
                    all_list.put(allStrings[0], new Generation(allStrings[0], allStrings[1], null, allStrings[2], 0, 0));
                }
            }
        }
    }

    /**
     * <p>
     * Creates a full output based on Option 5 of Husband + Wife + Child/Children's AND
     * sorts it in Natural Name Flow with a Map to Treemap Key sorted
     * </p>
     *
     * @param mode modified output based if its run normally or if its testing
     */
    private static void advancedSortData(String mode) {
        TreeMap<String, Generation> sorted_all_list = new TreeMap<>(all_list);
        //List<Generation> arr_all_list = new ArrayList<>(all_list.keySet());

        sorted_all_list.forEach((key, value) -> {
            System.out.println(value);
        });

        assert mode != null;
        if (!mode.equals("test")) {

            // Create csv
            FileWriter writer;
            String csvFile;
            try {
                Scanner file_path = new Scanner(System.in);
                System.out.println("\n### Where you wanna save the export sorted List? \"Local\" or \"Absolute\" path ###");
                System.out.print("Path: ");
                csvFile = file_path.nextLine();
                //creating object writer with a path and charset format
                writer = new FileWriter(write_file_path_to_disk(csvFile), StandardCharsets.UTF_8);

                //this loop uses Filewriter "write" function to print the result on the newly created csv file
                sorted_all_list.forEach((key, value) -> {
                    try {
                        writer.write(value.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer.write(" \n");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * <p>Checks if Menu Options is not and Invalid Input character/Integer</p>
     *
     * @param strNum Input of Menu Options if its Numeric
     * @return A boolean approval of continuing or not with flow of program.
     */
    private static boolean isNumeric(String strNum) {

        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    /**
     * <p>read from disk else callee throws exception to caller</p>
     *
     * @param csvFile Relative or absolute of System Path of existing File "ONLY" .csv accepted with max 1-3 Columns
     *                [Person1, male] || [Person1, husband/wife, Person2] || [Person1, father/mother, Person2]
     * @throws IOException throws if there is an IO error e.g. null, file not found, directory detected
     */
    static void read_file_path_to_disk(String csvFile) throws IOException {
        if (csvFile == null) throw new NullPointerException("Null File is not allowed");
        File file = new File(csvFile);
        Path path = Paths.get(String.valueOf(file));
        String test = file.getParent();
        if (!file.isDirectory()) {
            List<String> result = check_and_find_Files(path, ".csv", "file");

            file = file.getCanonicalFile();  // getParentFile()
            file_path_canonical = new StringBuilder(file.getParent());
            getFile_path_canonical = new StringBuilder(file_path_canonical);
        }
        if (file.isDirectory())
            throw new IOException("Directory detected");
        if (file.exists()) {
            //csvFile=file.toString();
            //csvFile=csvFile.replaceAll(file_path_drive.toString(), Matcher.quoteReplacement("\\\\"));
            //String CapFirst= csvFile.substring(0,1).toUpperCase(Locale.ROOT);
            //csvFile=CapFirst + csvFile.substring(1);
        } else
            throw new IOException("File could not found");
    }

    /**
     * <p>[Before] saves to disk else callee throws to caller exception</p>
     * <p>Checks for Already if the file Exists and Makes "the user dilemma"</p>
     * <p>it is not used on .dot creation though this function saves relative to .jar .exe and replaces always the file</p>
     *
     * @param csvFile File name to be written-[Before] in Non-Volatile Memory Storage Device any extension accepted (e.g. .dot, .csv)
     * @return to function itself (feed) as parameter callback [returns the csvFile path to caller] -line 801
     * @throws IOException throws if file already exists or is directory
     */
    private static String write_file_path_to_disk(String csvFile) throws IOException {
        String answer;
        File file = new File(csvFile);
        if (!file.isDirectory()) {
            file = file.getCanonicalFile();  // getParentFile()
        }
        if (file.isDirectory())
            throw new IOException("Directory detected");
        if (file.exists()) {
            System.out.print("\nFile Already exists replace it? (y/N): [N] ");
            Scanner getAnswer = new Scanner(System.in);
            answer = getAnswer.nextLine().toLowerCase();
            if (answer.equals("yes") || answer.equals("y")) {
                System.out.println("File Modified...");
                return csvFile;
            } else if (answer.equals("no") || answer.equals("n")) {
                System.out.println("Operation Aborted...");
                System.exit(0);
            } else {
                System.out.println("Operation Aborted...");
                System.exit(0);
            }
        }
        if (!file.exists()) {
            csvFile = file.toString();
            csvFile = csvFile.replaceAll(file_path_drive.toString(), Matcher.quoteReplacement("\\\\"));
            System.out.println("File is Written...");
            return csvFile;
        } else
            throw new IOException("File could not be saved");
    }

    /**
     * <p>Ensures Correct file extension exists ".csv" only accepted</p>
     *
     * @param path          Relative or absolute of System Path of existing File "ONLY" .csv accepted with max 1-3 Columns
     *                      [Person1, male] || [Person1, husband/wife, Person2] || [Person1, father/mother, Person2]
     * @param fileExtension Extension permitted from user to load
     * @param category_type Category of what is about to be loaded e.g. file vs dir find
     * @return Never actually used anywhere just exits the func
     * @throws IOException when file is not .csv
     */
    static List<String> check_and_find_Files(Path path, String fileExtension, String category_type)
            throws IOException {

        if (category_type.equals("dir")) {
            if (!Files.isDirectory(path)) {
                throw new IllegalArgumentException("Path must be a directory!");
            }

            List<String> result;

            try (Stream<Path> walk = Files.walk(path)) {
                result = walk
                        .filter(p -> !Files.isDirectory(p))
                        // this is a path, not string,
                        // this only test if path end with a certain path
                        //.filter(p -> p.endsWith(fileExtension))
                        // convert path to string first
                        .map(p -> p.toString().toLowerCase())
                        .filter(f -> f.endsWith(fileExtension))
                        .collect(Collectors.toList());
            }

            return result;
        } else if (!Files.isDirectory(path) && Files.exists(path)) {
            List<String> result;

            try (Stream<Path> walk = Files.walk(path)) {
                result = walk
                        .filter(p -> !Files.isDirectory(p))
                        .map(p -> p.toString().toLowerCase())
                        .filter(f -> f.endsWith(fileExtension))
                        .collect(Collectors.toList());
//                System.out.println(path);
                // remove if to become find file if check only extension then include it for error pop up
                if (result.isEmpty())
                    throw new IOException("Only csv extensions");
                return result;
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Path must be a file!");
            }
        } else
            throw new IOException("File Error: Must not be a directory");
        //return null;
    }

    /**
     * <p>Wrapper class of random Generation Toons Class and Inspirational quotes</p>
     */
    private static void ascii_art_generator() {

        int min = 0;
        int max = 20; // max from 1-20 while % max length is always 1-max of enum actually
        Names spoken = callThem(ThreadLocalRandom.current().nextInt(min, max + 1));

        int width = 180;
        int height = 30;

        //BufferedImage image = ImageIO.read(new File("/Users/username/Desktop/logo.jpg"));
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = image.getGraphics();
        g.setFont(new Font("SansSerif", Font.BOLD, 24));

        Graphics2D graphics = (Graphics2D) g;
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics.drawString(String.valueOf(spoken), 12, 24);

        //save this image
        //ImageIO.write(image, "png", new File("/users/username/ascii-art.png"));

        for (int y = 0; y < height; y++) {
            StringBuilder sb = new StringBuilder();
            for (int x = 0; x < width; x++) {

                sb.append(image.getRGB(x, y) == -16777216 ? " " : "*");  // 0 starts from

            }

            if (sb.toString().trim().isEmpty()) {
                continue;
            }

            System.out.println(sb);
        }
        System.out.println();

        switch (new Random().nextInt(((max + 1) - min) + min) % 7) {
            case 1 -> Toons.BugsBunny();
            case 2 -> Toons.DonaldDuck();
            case 3 -> Toons.GrandpaDuck();
            case 4 -> Toons.MarvinTheMartian();
            case 5 -> Toons.SylvesterTheCat();
            case 6 -> Toons.YosemiteSam();
            default -> Toons.DonaldDuck();
        }

    }

    /**
     * <p>You called them now they are here !</p>
     * <p>Generates Ascii Drawn (enumerator class) 2D Graphics Inspirational quotes following the Toons themselves</p>
     *
     * @param random Random Number based on inclusive 0 to exclusive Max number of Inspirational quotes available
     * @return returns the Inspirational quote
     */
    private static Names callThem(int random) {
        return Names.values()[random % Names.values().length]; // cycle itself till max limit
    }

    /**
     * <p>graphviz-java
     * <strong>Plugin for In-Program creation SVG/PNG files Images</strong>
     *
     * @see <a href="https://github.com/nidi3/graphviz-java"</a>
     * </p>
     */
    private static void graphvizGenerate() {
        String path_temp_original = getFile_path_canonical.toString();
        String reverse_slashes = file_path_canonical.toString().replaceAll("\\\\", "/");
        file_path_canonical = new StringBuilder(reverse_slashes);
        String path_temp_reverse = file_path_canonical.toString();

        // accepts / and \\
        File dot = new File(String.valueOf(path_temp_reverse + "/toGraphViz_" + family_MAIN_lastname + ".dot"));

        try (BufferedReader dotBuffer = new BufferedReader(new FileReader(dot))) {
            // compress Output Error's from this "Javascript Engine"
            PrintStream out = System.out;
            System.setOut(new PrintStream(OutputStream.nullOutputStream()));
            /*starts*/

            MutableGraph g = new Parser().read(dot);
            Graphviz.fromGraph(g).width(700).render(Format.SVG)
                    .toFile(new File(path_temp_original + "\\" + family_MAIN_lastname + "_tree_simple.svg"));

            g.graphAttrs()
                    .add(guru.nidi.graphviz.attribute.Color
                            .WHITE.gradient(guru.nidi.graphviz.attribute.Color.rgb("888888")).background().angle(90))
                    .nodeAttrs().add(guru.nidi.graphviz.attribute.Color.WHITE.fill())
                    .nodes().forEach(node ->
                    node.add(
                            guru.nidi.graphviz.attribute.Color.named(node.name().toString()),
                            Style.lineWidth(4), Style.FILLED));
            Graphviz.fromGraph(g).width(700).render(Format.SVG)
                    .toFile(new File(path_temp_original + "\\" + family_MAIN_lastname + "_tree_simple_gradient.svg"));

            System.setOut(out); // beyond re-enables Console Output
            /*end compressing*/
        } catch (IOException e) { // IOException| FileNotFound e
            e.printStackTrace();
        }
    }

    /**
     * @deprecated function
     * <p>Not used anywhere</p>
     */
    private static int getRandomNumberInRange(int min, int max) {
        // min inclusive max exclusive

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
        //return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    /**
     * <h2>Wrapper Function</h2>
     * <p>Ascii Generator from Another Class File + enumerator of Text</p>
     */
    private static void intro_menu() {
        // randomized
        ascii_art_generator();
        //Creating the text menu
        System.out.println("###Non-Case-Sensitive###\n");
    }

    /**
     * <p>User has NOT Passed CLI args for File to Load from Disk Path location Relative or Absolute</p>
     *
     * @return returns if available the existing full path of File
     */
    static String read_file_input_menu() {
        System.out.println("Enter File Path to csv \"Absolute\" or \"Local\" Path \ne.g. \"c:\\users\\myuser\\downloads" +
                "\\exercise\\family.csv\" OR \"path\\local\\family.csv\"");
        System.out.print("Path: ");
        String csvFile = "C:\\example\\file\\to\\my\\family.csv";
        try {
            Scanner file_path = new Scanner(System.in);
            csvFile = file_path.nextLine();
            read_file_path_to_disk(csvFile);  // throws to caller exception catch by callee
        } catch (IOException | InvalidPathException e) {
            if (e instanceof IOException) {
                System.out.print("File Path Input Error: " + getFile_path_canonical + "\\" + csvFile + "\n");
                e.printStackTrace();
                System.exit(1);
            } else {
                System.out.println("Invalid Path Input Error: " + getFile_path_canonical + "\\" + csvFile + "\n");
                e.printStackTrace();
                System.exit(1);
            }
        }
        return csvFile;
    }

    /**
     * <p>User has Passed CLI args for File to Load from Disk Path location Relative or Absolute</p>
     *
     * @param csvFile an .csv only Constrained File loaded absolute or relative from Non-Volatile Memory Storage Device
     *                with max 1-3 Columns
     *                [Person1, male] || [Person1, husband/wife, Person2] || [Person1, father/mother, Person2]
     * @return returns if available the existing full path of File
     */
    static String read_file_input_menu(String csvFile) {
        try {
            read_file_path_to_disk(csvFile);  // throws to caller exception catch by callee
        } catch (IOException | InvalidPathException e) {
            if (e instanceof IOException) {
                System.out.print("File Path Input Error: " + getFile_path_canonical + "\\" + csvFile + "\n");
                e.printStackTrace();
                System.exit(1);
            } else {
                System.out.println("Invalid Path Input Error: " + getFile_path_canonical + "\\" + csvFile + "\n");
                e.printStackTrace();
                System.exit(1);
            }
        }
        return csvFile;
    }

    /**
     * <p>
     * Family Last Name
     * </p>
     * <p>User has NOT passed any CLI args but are given from normal Program Flow when asked</p>
     */
    private static void family_last_name_input() {
        try {
            Scanner fam_lst = new Scanner(System.in);
            System.out.print("\nWhat is Main Family Lastname?: ");
            family_MAIN_lastname = fam_lst.nextLine().toLowerCase(Locale.ROOT);
            if (isNumeric(family_MAIN_lastname) || StringUtils.isNumericSpace(family_MAIN_lastname))
                throw new IOException();
            String capFirst = family_MAIN_lastname;
            family_MAIN_lastname = capFirst.toUpperCase().charAt(0) + capFirst.substring(1, capFirst.length());
        } catch (IOException e) {
            System.out.println("Input Error ... Program Exits ...");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * <p>
     * Family Last Name
     * </p>
     * <p>User has passed CLI args</p>
     *
     * @param FamilyLastName Family Main LastName of Tree
     */
    private static void family_last_name_input(String FamilyLastName) {
        try {
            family_MAIN_lastname = FamilyLastName.toLowerCase(Locale.ROOT);
            if (isNumeric(family_MAIN_lastname) || StringUtils.isNumericSpace(family_MAIN_lastname))
                throw new IOException();
            String capFirst = family_MAIN_lastname;
            family_MAIN_lastname = capFirst.toUpperCase().charAt(0) + capFirst.substring(1);
        } catch (IOException e) {
            System.out.println("Input Error ... Program Exits ...");
            e.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * <h2>Menu Options</h2>
     * <p>For both (CLI args passed OR NOT passed) Check States</p>
     *
     * @param csvFile     an .csv only Constrained File loaded absolute or relative from Non-Volatile Memory Storage Device
     *                    with max 1-3 Columns
     *                    [Person1, male] || [Person1, husband/wife, Person2] || [Person1, father/mother, Person2]
     * @param generation  Actually does not do anything for the moment
     * @param inputOption User Option [1-5] if "null" Then user has Passed CLI args
     *                    if [2 MUST] Names have provided goes to final else
     *                    else asks for Name1 (argsName1) and name2 (argsName2) respectively
     * @param argsName1   argsName1 to compare argsName2 if "null" Then user has Passed CLI args
     *                    user Must provide Both Names otherwise program kicks in from latest input normal flow
     * @param argsName2   argsName2 to compare argsName1 if "null" Then user has Passed CLI args
     *                    user Must provide Both Names otherwise program kicks in from latest input normal flow
     * @param mode        modified output based if its run normally or if its testing
     */
    static void menu_options(String csvFile, Generation generation, Integer inputOption
            , String argsName1, String argsName2, String mode) {
        if (mode == null)
            mode = "userflow";
        if (mode.isEmpty() || mode.isBlank())
            mode = "userflow";
        if (!(argsName1 != null && argsName2 != null)) {
            if (inputOption == null) {
                System.out.println("***This is the \"" + family_MAIN_lastname + "\" family tree app***");
                System.out.println("***Main Menu***");
                try (Scanner menu = new Scanner(System.in)) {
                    System.out.print("""
                            Press 1 to read the family members list\s
                            Press 2 to create an alphabetically minimal sorted family members list\s
                            Press 5 to create a Full output sorted Family members\s
                            Press 3 to use the relationship app\s
                            Press 4 to generate the .dot file + Generate SVG GraphViz Image\s
                            Enter: """);

                    int input = menu.nextInt();
                    switch (input) {
                        case 1 -> readcsv(csvFile, input, null, null, null);
                        case 2 -> {
                            // It is important to run readcsv() first, for sortcsv() to work
                            // select Save path
                            readcsv(csvFile, input, null, null, null);
                            System.out.println("\n");
                            sortcsv(mode);
                        }
                        case 3 -> searchNameRelations(csvFile, input);  // recursion
                        case 4 -> {
                            // re-uses File save dot and svg from readcsv() Path Taken
                            name_sorted_List.clear();
                            createDot(csvFile, input, generation, mode);
                            graphvizGenerate();
                        }
                        case 5 -> {
                            readcsv(csvFile, input, null, null, null);
                            advancedSortData(mode);
                        }
                        default -> System.out.println("Please try again!");
                    }
                } catch (Exception e) {
                    System.out.println("Input Error ... Program Exits ...");
                    e.printStackTrace();
                    System.exit(1);
                }
            } else {
                System.out.println("***This is the \"" + family_MAIN_lastname + "\" family tree app***");
                try (Scanner menu = new Scanner(System.in)) {
                    int input = inputOption;
                    switch (input) {
                        case 1 -> readcsv(csvFile, input, null, null, null);
                        case 2 -> {
                            // It is important to run readcsv() first, for sortcsv() to work
                            // select Save path
                            readcsv(csvFile, input, null, null, null);
                            System.out.println("\n");
                            sortcsv(mode);
                        }
                        case 3 -> searchNameRelations(csvFile, input);  // recursion
                        case 4 -> {
                            // re-uses File save dot and svg from readcsv() Path Taken
                            name_sorted_List.clear();
                            createDot(csvFile, input, generation, mode);
                            graphvizGenerate();
                        }
                        case 5 -> {
                            readcsv(csvFile, input, null, null, null);
                            advancedSortData(mode);
                        }
                        default -> System.out.println("Please try again!");
                    }
                } catch (Exception e) {
                    System.out.println("Input Error ... Program Exits ...");
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        } else {
            searchNameRelations(csvFile, argsName1, argsName2);  // recursion
        }
    }

    /**
     * <p>This implements Runnable, so parsing, error handling, and help messages can be done with this line:</p>
     *
     * @param args Old Method CLI get Args no longer Used in this App
     * @deprecated Params args As of Release v0.5, replaced by {@link CommandLine}
     */
    public static void main(String[] args) {
        int exitCode = new CommandLine(new Family()).execute(args);
        System.exit(exitCode);
    }

    /**
     * <p>Thread Run Caused By PicoCLI Options</p>
     */
    @Override
    public void run() { // your business logic goes here...
//        if (mobileNumber != null) {
//            System.out.println("User Mobile Number is: " + mobileNumber);
//        }
//        if (country != null && !country.isEmpty()) {
//            System.out.println("(Positional parameter) User's country is: " + country);
//        }

        Generation generation = new Generation();
        String csvFile;

        // ascii art
        intro_menu();

        // param #0 (Args)
        try {
            if (filepath == null || filepath.isEmpty()) {
                csvFile = read_file_input_menu();
            } else
                csvFile = read_file_input_menu(filepath);
        } catch (ArrayIndexOutOfBoundsException e) {
            csvFile = read_file_input_menu();
        }

        // for local Resources in compile uncomment this
//        String csvFile = Objects.requireNonNull(Family.class.getClassLoader().getResource("family.csv")).getPath();

        // param #1
        try {
            if (lastName != null && !lastName.isEmpty())
                family_last_name_input(lastName);
            else
                family_last_name_input();
        } catch (ArrayIndexOutOfBoundsException e) {
            family_last_name_input();
        }

//        family_MAIN_lastname = "Duck";

        // param #2
        try {
            // if not array out of bound or empty do below else catch
            if (optionsMenu != null) {
                if (firstName1 != null && firstName2 != null) {
                    if (!optionsMenu.isEmpty() && ((firstName1.isEmpty() || firstName2.isEmpty()) || (firstName1.isBlank() || firstName2.isBlank()))) {
                        menu_options(csvFile, generation, Integer.parseInt(optionsMenu), null, null, "");
                    } else if (!optionsMenu.isEmpty() && !((firstName1.isEmpty() && firstName2.isEmpty()) || (firstName1.isBlank() && firstName2.isBlank()))) {
                        menu_options(csvFile, generation, Integer.parseInt(optionsMenu), firstName1, firstName2, "");
                    } else {
                        if (!optionsMenu.isEmpty()) {
                            menu_options(csvFile, generation, Integer.parseInt(optionsMenu), null, null, "");
                        }
                    }
                } else
                    menu_options(csvFile, generation, Integer.parseInt(optionsMenu), null, null, "");
            } else
                throw new ArrayIndexOutOfBoundsException();
        } catch (ArrayIndexOutOfBoundsException ignored) {
            assert optionsMenu == null;
            menu_options(csvFile, generation, null, null, null, "");
        }
        //Arrays.sort(); // see sort implementation
    }

    /**
     * <p>list of Random Ascii Inspiration Messages in Beginning of the App</p>
     * <p>
     * uses Integer to Get String with Names.values()[];
     * </p>
     */
    private enum Names {
        JAVA(1),
        FAMILY(2),
        HOPE(3),
        SUMMER(4),
        TOONS(5),
        LOVE(6),
        AWAY(7);

        private final int names;

        Names(int i) {
            this.names = i;
        }

        public int getNamesValue() {
            return names;
        }
    }
}

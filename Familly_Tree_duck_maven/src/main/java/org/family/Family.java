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

import javax.naming.Name;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
    add to "System Var" "winKey+S" a System variable path to jdk-16.0.2\bin
    OR C:\Program Files (x86)\Common Files\Oracle\Java\javapath must have JRE 16.0.2 support
    java --version
    java -jar filename.jar must have 16.0.2 JDK (class file version 60)
*/

/*
    .\dot.exe -Tsvg .\toGraphviz.dot
*/

/*
 1) Graph convert
   1.1) give Manios
   1.10) new classes for string overide Interface? assign from new class style sto not null output toString ovveride
   1.11) new sorting
 2) push github
 4) make web viz out of this like python */

public class Family {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    private static final Pattern file_path_drive = Pattern.compile("\\\\");
    //private static Generation generation = new Generation();
    static List<String> all_names = new ArrayList<>();  // all names of csv
    static List<Generation> name_sorted_List = new ArrayList<>();  // Creating an Arraylist that stores objects <Person>
    static List<List<String>> records1 = new ArrayList<>();
    static HashMap<String, String> childs = new HashMap<>();
    static HashMap<String, Generation> who_is_father_mother_List = new HashMap<>();
    static HashMap<String, Generation> who_is_husband_of_wife_List = new HashMap<>();
    static HashMap<String, Generation> all_list = new HashMap<>();
    static AtomicInteger timesCsv = new AtomicInteger();
    static AtomicInteger idK = new AtomicInteger();
    static String N1, N2, parent1 = "a", parent2 = "b", family_MAIN_lastname, new_N1_p1, new_N1_p2,
            new_N2_p1, new_N2_p2, N1_initials, N2_initials, N1_initials_child, N2_initials_child,
            they_are, they_have = "", husband, wife;
    static StringBuilder file_path_canonical, getFile_path_canonical;
    static boolean isTop_root_parent1 = false, isTop_root_parent2 = false, name_N1p1_not_set = false,
            name_N1p2_not_set = false, name_N2p1_not_set = false, name_N2p2_not_set = false, isBlood_mix1 = false,
            isBlood_mix2 = false, take_once = false, exist_in_list = false, not_children_of_parent = false,
            isSiblings1 = false, isSiblings2 = false, isIsSiblings1Far = false, isIsSiblings2Far = false,
            incest = false, add_they_have = false, blood_hus = false, blood_wife = false;

    //This function reads the csv file and stores its content on an Arraylist, printing the result
    public static void readcsv(String path, int input, String FName1_originalCase, String FName2_originalCase,
                               Generation generation) {
        String line;
        final String csvSplitBy = ",";
        String FName1 = "", FName2 = "";
        if (FName1_originalCase == null && FName2_originalCase == null) {
        } else {
            assert FName1_originalCase != null;
            assert FName2_originalCase != null;
            FName1 = FName1_originalCase.toLowerCase(Locale.ROOT);
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this function sorts the Arraylist alphabetically and then exports a sorted csv file
    private static void sortcsv() {
        //compares series of pairs of elements and based on result of compare
        // it sees if elements from that pair should be swapped or not
        name_sorted_List.sort((o1, o2) -> {
            // compare two instance of `Score` and return `int` as result.
            return ~o2.getName().compareTo(o1.getName()) - 1;
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

    private static void searchNameRelations(String csvFile, int input) {
        System.out.println("\n***Find Relationships***");
        System.out.print("Enter 1st First Names character (as unique ID) \nName: (e.g. grandpa) ");
        try (Scanner name1 = new Scanner(System.in)) {
            String Personname1 = name1.nextLine();
            Scanner name2 = new Scanner(System.in);
            System.out.print("Enter 2nd First Names character (as unique ID)\nName: (e.g. dumbella) ");
            String Personname2 = name2.nextLine();
            readcsv(csvFile, input, Personname1, Personname2, null);
            case_scenario_checking(input);
        } catch (Exception e) {
            System.out.println("There was a Input Problem");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void createDot(String csvFile, int input, Generation generation) {
        readcsv(csvFile, input, null, null, generation);
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

    //after readcsv - input_checking_scenario_Loop - comes this (2)
    private static void case_scenario_checking(int input) {

        // Check if two Inputs have same parents
        for (Generation g : Generation.getObj_wFatherMother) {
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
                    System.out.println("\nFar Bastard child");
                else if (!they_have.isEmpty())
                    System.out.println("\n" + they_are + " (not related) " + they_have);
                else if ((N1_initials != null && N1_initials.contains(family_MAIN_lastname))
                        && (N2_initials != null && N2_initials.contains(family_MAIN_lastname)))
                    System.out.println("\nFar Child");
                else System.out.println("\n" + they_are + " (not related)");
            } else System.out.println("\nSiblings in arms");
        } else if (isBlood_mix1 || isBlood_mix2) {
            System.out.println("\nFar siblings in Arm");
        } else
            System.out.println("\n" + they_are + " (related) " + they_have);
    }

    // finds from 2 Inputs relation between another Recursion of Tree backwards (including root)
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

            return;
        }
        boolean yes_new_parentS1 = false, yes_new_parentS2 = false;
        name_N1p1_not_set = false;
        name_N1p2_not_set = false;
        name_N2p1_not_set = false;
        name_N2p2_not_set = false;
        String[] clear_n1, clear_n2;
        for (Generation g : Generation.getObj_wFatherMother) {
            String[] ch1_arr = g.getChild().toLowerCase().split(" ");
            String[] p_arr = g.getName().toLowerCase().split(" ");
            String ch1 = ch1_arr[0];
            if (ch1.equals(FName1)) {
                name_N1p1_not_set = !name_N1p1_not_set;  // false
                name_N1p2_not_set = !name_N1p2_not_set;  // false // switch prepare
                if (name_N1p1_not_set) {
                    new_N1_p1 = g.getName();  // gets full name of N1p1 (Name 1 Parent 1)
                    if (N1_initials == null) {
                        N1_initials = g.getName();  // gets Parent Name and holds it
                        N1_initials_child = g.getChild();  // gets child of Parent and holds it
                    }
                    name_N1p1_not_set = true;
                    name_N1p2_not_set = false;
                }
                if (name_N1p2_not_set) {
                    new_N1_p2 = g.getName();  // gets full name of N1p2 (Name 1 Parent 2)
                    name_N1p2_not_set = true;
                }
                yes_new_parentS1 = true;
                name_N1p2_not_set = false;  // false so next recursion stack loop will have false -> true and no more exec
            }
            if (ch1.equals(FName2)) {
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
                if (new_N2_p1.contains(family_MAIN_lastname) && new_N2_p2.contains(family_MAIN_lastname)) {
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

        find_shared_root(new_N1_p1, new_N2_p1);
    }

    private static void input_checking_scenario_Loop(int input, String FName1, String FName2, String[] allStrings,
                                                     Generation generation) {

        // Make CSV input for Only Name Sections for Cols NOT EQUAL to 1 which will be "relations" part
        // Make Only name Sections First capitalize
        for (int i = 0; i < allStrings.length; i++) {
            timesCsv.incrementAndGet();
            String capFirstMove;
            String[] capFirst;
            StringBuilder save_new_Cap = new StringBuilder();
            if (i != 1) {
                capFirstMove = allStrings[i];
                capFirst = capFirstMove.split(" ");
                for (int j = 0; j < Objects.requireNonNull(capFirst).length; j++) {
                    String makeCap = capFirst[j];

                    // csv first Start Of File has a special character (\uFEFF AKA== "BOM" char utf-8)
                    // so we just from charAt(0) to 1 only and only then!
                    final String s = makeCap.toUpperCase().charAt(0) + makeCap.substring(1);
                    if (timesCsv.get() == 1) {
                        if (j != 0)
                            capFirst[j] = s;
                        else
                            capFirst[j] = makeCap.toUpperCase().charAt(1) + makeCap.substring(2);
                    } else
                        capFirst[j] = s;

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

        for (int i = 0; i < allStrings.length; i++) {
            allStringsLC[i] = allStrings[i].toLowerCase(Locale.ROOT); //str.substring(0,1).toUpperCase()
        }

        // menu 1 print List of CSV after read
        if (input == 1)
            System.out.println(Arrays.toString(allStrings));

        // sort csv used
        if (input != 3 && input != 4)
            if (allStrings.length == 2) {
//                if (allStrings[1].equals("male") || allStrings[1].equals("female")
//                        || allStrings[1].equals("father") || allStrings[1].equals("mother")
//                        || allStrings[1].equals("husband") || allStrings[1].equals("wife"))
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
                    //myList1.add(new Generation(allStrings[0], allStrings[1], allStrings[2]));
                    who_is_father_mother_List.put(String.valueOf(new Random().nextInt() & Integer.MAX_VALUE),
                            new Parent(allStrings[0], allStrings[1], allStrings[2]));  //new AtomicInteger(1).incrementAndGet()
                    //all_list.put(allStrings[0], generation.se);
                }
                if ((allStrings[1].equals("husband"))) {
                    all_list.put(String.valueOf(idK.get()), generation.setHusbandObj(allStrings[0]));
                    //all_list.put(allStrings[0], new Generation(allStrings[0], allStrings[1], allStrings[2], 0));
                }
                if ((allStrings[1].equals("wife"))) {
                    all_list.put(String.valueOf(idK.get()), generation.setWifeObj(allStrings[0]));
                    //all_list.put(allStrings[0], new Generation(allStrings[0], allStrings[1], allStrings[2], 0, 0));
                }
                if ((allStrings[1].equals("father"))) {
                    /* ######### LIST OF CHILDS ara hasmap treemap? mesa me List? arrayList <> */
                    all_list.put(String.valueOf(idK.get()), generation.setChildObj(allStrings[2]));
                    idK.incrementAndGet();
                    //all_list.put(allStrings[0], new Generation(allStrings[0], allStrings[1], allStrings[2], 0, 0));
                }
                if ((allStrings[1].equals("male")) || (allStrings[1].equals("female"))) {
                    //all_list.put(allStrings[0], new Generation(allStrings[0], allStrings[1], allStrings[2]));
                }
            }
        }
    }

    private static void advancedSortData() {
        /*
        combines case 1,3? then spits 5 run here again?
        */
    }

    private static boolean isNumeric(String strNum) {

        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    // read from disk else callee throws exception to caller
    private static void read_file_path_to_disk(String csvFile) throws IOException {
        File file = new File(csvFile);
        if (!file.isDirectory())
            file = file.getCanonicalFile();  // getParentFile()
        file_path_canonical = new StringBuilder(file.getParent());
        getFile_path_canonical = new StringBuilder(file_path_canonical);
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

    // save to disk else callee throws to caller exception
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

    // You called them now they are here !
    private static Names callThem(int random) {
        return Names.values()[random % Names.values().length]; // cycle itself till max limit
    }

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
            assert dot != null;

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

    private static int getRandomNumberInRange(int min, int max) {
        // min inclusive max exclusive

        if (min >= max) {
            throw new IllegalArgumentException("max must be greater than min");
        }

        Random r = new Random();
        return r.nextInt((max - min) + 1) + min;
        //return (int)(Math.random() * ((max - min) + 1)) + min;
    }

    private static void intro_menu(){
        // randomized
        ascii_art_generator();
        //Creating the text menu
        System.out.println("###Non-Case-Sensitive###\n");
    }

    private static String read_file_input_menu(){
        System.out.println("Enter File Path to csv \"Absolute\" or \"Local\" Path \ne.g. \"c:\\users\\myuser\\downloads" +
                "\\exercise\\family.csv\" OR \"path\\local\\family.csv\"");
        System.out.print("Path: ");
        String csvFile = "C:\\example\\file\\to\\my\\family.csv";
        try {
            Scanner file_path = new Scanner(System.in);
            csvFile = file_path.nextLine();
            read_file_path_to_disk(csvFile);  // throws to caller exception catch by callee
        } catch (IOException e) {
            System.out.print("File Path Input Error: " + getFile_path_canonical + "\\" + csvFile + "\n");
            e.printStackTrace();
            System.exit(1);
        }
        return csvFile;
    }

    private static String read_file_input_menu(String csvFile){
        try {
            read_file_path_to_disk(csvFile);  // throws to caller exception catch by callee
        } catch (IOException e) {
            System.out.print("File Path Input Error: " + getFile_path_canonical + "\\" + csvFile + "\n");
            e.printStackTrace();
            System.exit(1);
        }
        return csvFile;
    }

    private static void family_last_name_input(){
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

    private static void family_last_name_input(String FamilyLastName){
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

    private static void menu_options(String csvFile, Generation generation, Integer inputOption){
        if (inputOption==null) {
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
                        sortcsv();
                    }
                    case 3 -> searchNameRelations(csvFile, input);  // recursion
                    case 4 -> {
                        // re-uses File save dot and svg from readcsv() Path Taken
                        name_sorted_List.clear();
                        createDot(csvFile, input, generation);
                        graphvizGenerate();
                    }
                    case 5 -> {
                        advancedSortData();
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
                        sortcsv();
                    }
                    case 3 -> searchNameRelations(csvFile, input);  // recursion
                    case 4 -> {
                        // re-uses File save dot and svg from readcsv() Path Taken
                        name_sorted_List.clear();
                        createDot(csvFile, input, generation);
                        graphvizGenerate();
                    }
                    case 5 -> {
                        advancedSortData();
                    }
                    default -> System.out.println("Please try again!");
                }
            } catch (Exception e) {
                System.out.println("Input Error ... Program Exits ...");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static void main(String[] args) {
        Generation generation = new Generation();
        String csvFile;

//        for (String str: args) { System.out.println(str); }

        // ascii art
        intro_menu();

        // param #1 (Args)
        try {
            if (!args[0].equals("family.csv"))
                csvFile = read_file_input_menu();
            else
                csvFile = read_file_input_menu(args[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            csvFile = read_file_input_menu();
        }

          // for local Resources in compile uncomment this
//        String csvFile = Objects.requireNonNull(Family.class.getClassLoader().getResource("family.csv")).getPath();

        // param #2
        try {
            if (!args[1].isEmpty())
                family_last_name_input(args[1]);
            else
                family_last_name_input();
        } catch (ArrayIndexOutOfBoundsException e) {
            family_last_name_input();
        }

//        family_MAIN_lastname = "Duck";

        // param #3
        try {
            if (!args[2].isEmpty())
                menu_options(csvFile, generation, Integer.parseInt(args[2]));
            else
                menu_options(csvFile, generation, null);
        } catch (ArrayIndexOutOfBoundsException e) {
            menu_options(csvFile, generation, null);
        }
    }

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

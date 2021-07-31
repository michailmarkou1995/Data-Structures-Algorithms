package org.family;

import org.family.famillytree.Generation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/*
.\dot.exe -Tsvg .\toGraphviz.dotbak
 1) TO-DO make menu 3, 4 with 3? uses enter a name and see local tree like a short story father-grandfather + children
   1.1) give Manios
   1.2) Make Loop Menu
   1.3) Read csv search
   1.4) remove 1.3 and make graph search convert before search
   1.5) Fix null Pointers try Catch + Make First Capital or lowercase all no matter the input User
   1.6) name output with Capital First Letter
   1.7) fix hashmaps? reduce? no second input check2 there
   1.8) when they are far related == not related?! + no match name == not related or doesnt exist message
   1.9) DO GRANDPA RELATED WITH FAR CHILDER AS FAR CHILDREN !!! + no DUCK in CODE HARDCODE search in CONTROL + F
 2) push github
 3) make resources Local Path instead of Read from File
 4) make web viz out of this like python */

public class Family {

    private static final Pattern pattern = Pattern.compile("-?\\d+(\\.\\d+)?");
    public static String N1, N2, parent1 = "a", parent2 = "b", family_MAIN_lastname;
    public static boolean blood_hus = false, blood_wife = false;
    //Creating an Arraylist that stores objects <Person>
    static List<Generation> name_sorted_List = new ArrayList<Generation>();
    static List<Generation> myList1 = new ArrayList<Generation>();
    static HashMap<String, Generation> who_is_father_mother_List = new HashMap<String, Generation>();
    static HashMap<String, Generation> who_is_husband_wife_List = new HashMap<String, Generation>();
    static List<List<String>> records1 = new ArrayList<>();
    static HashMap<String, String> childs = new HashMap<String, String>();
    //StringBuffer
    static String they_are, they_have = "", husband, wife;
    static boolean not_children_of_parent = false, isSiblings1 = false, isSiblings2 = false,
            isIsSiblings1Far = false, isIsSiblings2Far = false,
            incest = false, add_they_have = false, isParent_of_parent = false;
    static Generation gen = new Generation();
    static AtomicInteger timesCsv = new AtomicInteger();
    static boolean isTop_root_parent1=false, isTop_root_parent2=false, name_N1p1_not_set=false, name_N1p2_not_set=false
            , name_N2p1_not_set=false, name_N2p2_not_set=false, isBlood_mix1=false, isBlood_mix2=false, take_once=false
            ,exist_in_list=false;
    static String new_N1_p1, new_N1_p2, new_N2_p1, new_N2_p2, N1_initials, N2_initials, N1_initials_child, N2_initials_child;
    public static List<String> all_names = new ArrayList<>();

    //This function reads the csv file and stores its content on an Arraylist, printing the result
    public static void readcsv(String path, int input, String FName1_originalCase, String FName2_originalCase) {
        String line = "";
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
                input_checking_scenario_Loop(input, FName1_originalCase, FName2_originalCase, allStrings);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //this function sorts the Arraylist alphabetically and then exports a sorted csv file
    private static void sortcsv() {
        name_sorted_List.sort(new Comparator<Generation>() {

            //compares series of pairs of elements and based on result of compare it sees if elements from that pair should be swapped or not
            public int compare(Generation o1, Generation o2) {
                // compare two instance of `Score` and return `int` as result.
                return ~o2.getName().compareTo(o1.getName());
                // use ~ to reverse order
            }
        });
        //loop that prints the sorted result
        for (Generation generation : name_sorted_List) {
            System.out.println(generation);
        }
        // Create csv
        FileWriter writer;
        try {
            //creating object writer with a path and charset format
            writer = new FileWriter("resources/Duck_generated.csv", StandardCharsets.UTF_8);

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
        System.out.print("Enter 1st First Names character (as unique ID) \nName: ");
        try (Scanner name1 = new Scanner(System.in)) {
            String Personname1 = name1.nextLine();
            Scanner name2 = new Scanner(System.in);
            System.out.print("Enter 2nd First Names character (as unique ID)\nName: ");
            String Personname2 = name2.nextLine();
            readcsv(csvFile, input, Personname1, Personname2);
            case_scenario_checking(input);
        } catch (Exception e) {
            System.out.println("There was a Input Problem");
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void createDot(String csvFile, int input) {
        readcsv(csvFile, input, null, null);
        FileWriter writerF;
        try {
            writerF = new FileWriter("resources/toGraphViz.dot", StandardCharsets.UTF_8);

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
        for (Generation g : Generation.getObj_wFM) {
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
/*
                if (isParent_of_parent){
recursive func find parent of parent apo domi same as .dot?
                }
*/
                // makes in memory Data structure file with relations of wife or husband e.g. this has this as wife
                // so its diff parents and they are far siblings(not the parent included as root)
                if (!parent1.equals(parent2) && (isSiblings1 && isSiblings2)) {
                    who_is_husband_wife_List.forEach((key, value) -> {
                        if (value.getName().equals(parent1) && value.getHusband().equals(parent2)
                                || value.getName().equals(parent2) && value.getHusband().equals(parent1)) {
                            isIsSiblings1Far = true;
                            isIsSiblings2Far = true;
                        }
                    });
                }
//                if ((isSiblings1==false && isSiblings2==false) && !isIsSiblings1Far && !isIsSiblings2Far){
//                    System.out.println("test Far child");
//                }
            }
        }
      /*not in lopp here remove it! below*/ // if ((isSiblings1==false || isSiblings2==false) && !isIsSiblings1Far && !isIsSiblings2Far){
            //System.out.println("test Far child");
            find_shared_root(N1, N2);
//            who_is_father_mother_List.entrySet().forEach(entry -> {
//                System.out.println(entry.getValue());
//            });//dinis may kai grandpa ine far child ara grandpa check if has father(no) then continue check MAY's father which is pepe
            //that means blood mix far child ara check mother || check && mother aparaitita Helena poia ine i mitera tis Helena? hortense -> grandma
            //check both parents always at same time with 2 recursion loops? pws implement to oti o father den exi father allo?
      /* above*/ // }

        // if they are something (2 inputs) meaning 3 cols how many children have List?
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
        var temp_exist_1=false;
        var temp_exist_2=false;
        for (int i = 0; i < all_names.size(); i++){
            if (N1.equals(all_names.get(i)))
                temp_exist_1=true;
            if (N2.equals(all_names.get(i)))
                temp_exist_2=true;

            if (temp_exist_1 && temp_exist_2)
                exist_in_list=true;
        }
        //dumbella quackmore, pepe dumbella ... pari me dumbella
//pepe quackmore is wrong not with grandpa AND donald with grandpa
        //donald grandma ludwig fanny they_are != or == null
        //far sibligns working? root me root? root me child? root me blood child? bastard with bastard? daisy fanny gladstone gus
        if (!incest) {//far bastards in arms(gus gladstone) + not related may huey ..gus grandpa far bastard, may huey far in blood siblings mix
            if (!(isIsSiblings1Far && isIsSiblings2Far)) {
                if (isSiblings1 && isSiblings2 && N1_initials != null && N2_initials != null)
                {
                    if (N1.contains(family_MAIN_lastname) && N2.contains(family_MAIN_lastname))
                        System.out.println("\nFar Siblings in arms");
                    else if (!N1.contains(family_MAIN_lastname) && !N2.contains(family_MAIN_lastname))
                        System.out.println("\nFar Bastards in Arms");
                    else if (!N1.contains(family_MAIN_lastname) && N2.contains(family_MAIN_lastname)
                        || N1.contains(family_MAIN_lastname) && !N2.contains(family_MAIN_lastname)
                        || !N1.contains(family_MAIN_lastname) && !N2.contains(family_MAIN_lastname)
                        && N1_initials.contains(family_MAIN_lastname) || N2_initials.contains(family_MAIN_lastname))
                        System.out.println("\n Far not related child's with \"in\" Blood Duck Parent");
                    else
                        System.out.println("\nFar Siblings in arms");
                }
                else if (N1_initials != null && N1_initials.contains(family_MAIN_lastname)
                        && N2_initials != null && N2_initials.contains(family_MAIN_lastname) && !N1.contains(family_MAIN_lastname)
                        || !N2.contains(family_MAIN_lastname) && they_are == null && exist_in_list)
                    System.out.println("\nFar Bastard child");
                else if (!they_have.isEmpty())
                    System.out.println("\n" + they_are + " (not related) " + they_have);
                else if ((N1_initials != null && N1_initials.contains(family_MAIN_lastname)) && (N2_initials != null && N2_initials.contains(family_MAIN_lastname)))
                    System.out.println("\nFar Child");
                else if (they_are != null && !they_are.isEmpty() && they_have==null || they_have.isEmpty())
                    System.out.println("\n" + they_are + " (not related)");
                else if ((new_N2_p1 != null && (!new_N2_p1.isEmpty() && !isBlood_mix2)) || (new_N1_p1 != null
                        && (!new_N1_p1.isEmpty() && !isBlood_mix2)) && !(they_are != null && they_are.contains("husband")
                        || they_are != null && they_are.contains("wife")))
                    System.out.println("\n (not related)");
                else System.out.println("\n" + they_are + " (not related)");
            } else System.out.println("\nSiblings in arms");
        } else if (isBlood_mix1 || isBlood_mix2){
            System.out.println("\nFar siblings in Arm");
            //System.out.println("\nBastard Child");
        } else
            System.out.println("\n" + they_are + " (related) " + they_have);
    }

    private static void find_shared_root(String FName1, String FName2){
        if(isTop_root_parent1 && isTop_root_parent2){
//            if(isBlood_mix1&&isBlood_mix2)
//                they_are="incest";
//            else
//                they_are="natural";
            if (N1_initials != null && N1_initials.toLowerCase().contains(N1))
                N1=N1_initials;
            if (N1_initials_child != null && N1_initials_child.toLowerCase().contains(N1))
                N1=N1_initials_child;
            if (N2_initials != null && N2_initials.toLowerCase().contains(N2))
                N2=N2_initials;
            if (N2_initials_child != null && N2_initials_child.toLowerCase().contains(N2))
                N2=N2_initials_child;

            return;
        }
        boolean yes_new_parentS1=false, yes_new_parentS2=false;
        name_N1p1_not_set=false;
        name_N1p2_not_set=false;
        name_N2p1_not_set=false;
        name_N2p2_not_set=false;
        String[] clear_n1, clear_n2;
        for (Generation g : Generation.getObj_wFM) {
            String[] ch1_arr = g.getChild().toLowerCase().split(" ");
            String[] p_arr = g.getName().toLowerCase().split(" ");
            String ch1 = ch1_arr[0];
            if (ch1.equals(FName1)){//g.getName().equals(FName1) ==
                name_N1p1_not_set = !name_N1p1_not_set;  // false
                name_N1p2_not_set = !name_N1p2_not_set;  // false
                if (name_N1p1_not_set) {
                    new_N1_p1 = g.getName();  // gets full name of N1p1
                    if (N1_initials==null) {
                        N1_initials = g.getName();
                        N1_initials_child = g.getChild();
                    }
//                    if (g.getChild()==null)
//                        N1_initials = g.getName();
//                    else
//                        N1_initials = g.getChild();
                    name_N1p1_not_set = true;
                    name_N1p2_not_set = false;
                }
                if (name_N1p2_not_set) {
                    new_N1_p2 = g.getName();  // gets full name of N1p2
                    name_N1p2_not_set=true;
                }
                yes_new_parentS1=true;
                name_N1p2_not_set=false;
            }
            if (ch1.equals(FName2)){
                name_N2p1_not_set = !name_N2p1_not_set;  // false
                name_N2p2_not_set = !name_N2p2_not_set;  // false
                if (name_N2p1_not_set) {
                    new_N2_p1 = g.getName();  // gets full name of N2p1
                    if (N2_initials==null) {
                        N2_initials = g.getName();
                        N2_initials_child = g.getChild();
                    }
//                    if (g.getChild()==null)
//                        N2_initials = g.getName();
//                    else
//                        N2_initials = g.getChild();
                    name_N2p2_not_set = false;
                }
                if (name_N2p2_not_set) {
                    new_N2_p2 = g.getName();  // gets full name of N2p2
                    N2_initials = g.getChild();
                    name_N2p2_not_set=true;
                }
                yes_new_parentS2=true;
                name_N2p2_not_set=false;
            }

            if (take_once==false && FName1!= null && FName1.equals(p_arr[0])){
                take_once=true;
                N1_initials=g.getName();
                N1_initials_child = g.getChild();
            }
            if (take_once==false && FName2!= null && FName2.equals(p_arr[0]))
            {
                take_once=true;
                N2_initials=g.getName();
                N2_initials_child = g.getChild();
            }
//            if(!g.getName().equals(N1_initials) && FName2.equals(p_arr[0]))
//                N2_initials=g.getName();
//            if(!g.getName().equals(N2_initials) && FName1.equals(p_arr[0]))
//                N1_initials=g.getName();
        }
        if (!yes_new_parentS1 && !yes_new_parentS2){
            isTop_root_parent1 = true;
            isTop_root_parent2 = true;
        }

        else {
            //if (!new_N1_p1.equals(null) && !new_N1_p2.equals(null)) { //with equal null pointer exception always func
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
        //need to store both names somewhere? by default keeps the not null
        find_shared_root(new_N1_p1, new_N2_p1);
    }

    private static void input_checking_scenario_Loop(int input, String FName1, String FName2, String[] allStrings) {

        // Make CSV input for Only Name Sections for Cols NOT EQUAL to 1 which will be "relations" part
        // Make Only name Sections First capitalize
        for (int i = 0; i < allStrings.length; i++) {
            timesCsv.incrementAndGet();
            String capFirstMove;
            String[] capFirst = null;
            String save_new_Cap = "";
            if (i != 1) {
                capFirstMove = allStrings[i];
                capFirst = capFirstMove.split(" ");
                for (int j = 0; j < Objects.requireNonNull(capFirst).length; j++) {
                    String makeCap = capFirst[j];

                    // csv first Start Of File has a special character (\uFEFF AKA== "BOM" char utf-8)
                    // so we just from charAt(0) to 1 only and only then!
                    final String s = makeCap.toUpperCase().charAt(0) + makeCap.substring(1, makeCap.length());
                    if (timesCsv.get() == 1) {
                        if (j != 0)
                            capFirst[j] = s;
                        else
                            capFirst[j] = makeCap.toUpperCase().charAt(1) + makeCap.substring(2, makeCap.length());
                    } else
                        capFirst[j] = s;

                    if (j == 0)
                        save_new_Cap += capFirst[j];
                    else
                        save_new_Cap += " " + capFirst[j];
                    if (j == capFirst.length - 1)
                        allStrings[i] = save_new_Cap;
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
                name_sorted_List.add(new Generation(allStrings[0], allStrings[1]));
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
                    who_is_husband_wife_List.put(String.valueOf(new Random().nextInt()), new Generation(allStrings[0], allStrings[1], allStrings[2], "null"));

                // check father or mother both have in common for Create ".dot" File .. DB simulation with Hashmap uses PK
                // as new Random and Values the object to access itself
                if ((allStrings[1].equals("mother")) || (allStrings[1].equals("father"))) {
                    who_is_father_mother_List.put(String.valueOf(new Random().nextInt()), gen = new Generation(allStrings[0], allStrings[1], allStrings[2]));
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
                    who_is_father_mother_List.put(String.valueOf(new Random().nextInt()), new Generation(allStrings[0], allStrings[1], allStrings[2]));
                }
            }
        }
    }

    public static boolean isNumeric(String strNum) {

        if (strNum == null) {
            return false;
        }
        return pattern.matcher(strNum).matches();
    }

    public static void main(String[] args) {
        //Creating the text menu
        String csvFile = Objects.requireNonNull(Family.class.getClassLoader().getResource("family.csv")).getPath();
//        try {
//            Scanner fam_lst = new Scanner(System.in);
//            System.out.print("\nWhat is Main Family Lastname?: ");
//            family_MAIN_lastname = fam_lst.nextLine().toLowerCase(Locale.ROOT);
//            if (isNumeric(family_MAIN_lastname) || StringUtils.isNumericSpace(family_MAIN_lastname))
//                throw new IOException();
//            String capFirst = family_MAIN_lastname;
//            family_MAIN_lastname = capFirst.toUpperCase().charAt(0) + capFirst.substring(1, capFirst.length());
//        } catch (IOException e) {
//            System.out.println("Input Error ... Program Exits ...");
//            e.printStackTrace();
//            System.exit(1);
//        }
        family_MAIN_lastname = "Duck";
        System.out.println("***This is the " + family_MAIN_lastname + " family tree app***");
        System.out.println("***Main Menu***");
        try (Scanner menu = new Scanner(System.in)) {
            System.out.println("Press 1 to read the family members list \nPress 2 to create an alphabetically sorted family members list \nPress 3 to use the relationship app \nPress 4 to generate the .dot file");
            int input = menu.nextInt();
            switch (input) {
                case 1 -> readcsv(csvFile, input, null, null);
                case 2 -> {
                    //It is important to run readcsv() first, for sortcsv() to work
                    readcsv(csvFile, input, null, null);
                    System.out.println("\n");
                    sortcsv();
                }
                case 3 -> searchNameRelations(csvFile, input);
                case 4 -> {
                    name_sorted_List.clear();
                    createDot(csvFile, input);
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

/*
    private static void find_shared_root(String FName1, String FName2, String pFname1, String pFname2){
        if(isTop_root_parent1 && isTop_root_parent2){
            return;
        }
        boolean yes_new_parentS1=false, yes_new_parentS2=false;
        name_N1p1_not_set=false;
        name_N1p2_not_set=false;
        name_N2p1_not_set=false;
        name_N2p2_not_set=false;
        for (Generation g : Generation.getObj_wFM) {
            String[] ch1_arr = g.getChild().toLowerCase().split(" ");
            String ch1 = ch1_arr[0];
            if (ch1.equals(FName1)){//g.getName().equals(FName1) ==
                name_N1p1_not_set = !name_N1p1_not_set;  // false
                name_N1p2_not_set = !name_N1p2_not_set;  // false
                if (name_N1p1_not_set) {
                    new_N1_p1 = g.getName();  // gets full name of N1p1
                    name_N1p1_not_set = true;
                    name_N1p2_not_set = false;
                }
                if (name_N1p2_not_set) {
                    new_N1_p2 = g.getName();  // gets full name of N1p2
                    name_N1p2_not_set=true;
                }
                yes_new_parentS1=true;
                name_N1p2_not_set=false;
            }
            if (ch1.equals(FName2)){
                name_N2p1_not_set = !name_N2p1_not_set;  // false
                name_N2p2_not_set = !name_N2p2_not_set;  // false
                if (name_N2p1_not_set) {
                    new_N2_p1 = g.getName();  // gets full name of N2p1
                    name_N2p2_not_set = false;
                }
                if (name_N2p2_not_set) {
                    new_N2_p2 = g.getName();  // gets full name of N2p2
                    name_N2p2_not_set=true;
                }
                yes_new_parentS2=true;
                name_N2p2_not_set=false;
            }
        }
//        if (!yes_new_parentS1 && !yes_new_parentS2){
//            isTop_root_parent1 = true;
//            isTop_root_parent2 = true;
//        }
        if (!yes_new_parentS1) isTop_root_parent1=true;
        if (!yes_new_parentS2) isParent_of_parent=true;
        if (isTop_root_parent1 && isTop_root_parent2){}
        else
        {
            new_N1_p1 = FName1;
            new_N1_p2 = FName2;
        }
        System.out.println("code here");
        find_shared_root(new_N1_p1, new_N1_p2, new_N2_p1, new_N2_p2 );
    }
 */


/*

 if (!incest) {
            if (!(isIsSiblings1Far && isIsSiblings2Far)) {
                if (isSiblings1 && isSiblings2 && N1_initials != null && N2_initials != null)
                {
                    if (isSiblings1 && isSiblings2)
                    System.out.println("\nFar Siblings in arms");
                    else if ((!isBlood_mix1 || !isBlood_mix2)
                        && (N1_initials != null && N1_initials.contains(family_MAIN_lastname))
                        && (N2_initials != null && N2_initials.contains(family_MAIN_lastname))){
                        System.out.println("\nFar Siblings in arms");
                    }
                }
                else if (!they_have.isEmpty())
                    System.out.println("\n" + they_are + " (not related) " + they_have);
                else if (isBlood_mix1 || isBlood_mix2)
                    System.out.println("\nBastard Child");
                else if ((N1_initials != null && N1_initials.contains(family_MAIN_lastname)) && (N2_initials != null && N2_initials.contains(family_MAIN_lastname)))
                    System.out.println("\nFar Child");//System.out.println("\n (not related)");
//                else if (they_are==null && ((new_N2_p1 == null && new_N1_p1 != null) || (new_N2_p1 != null  && new_N1_p1 == null)) && (!isBlood_mix1 && !isBlood_mix2))//!= or == they are?
//                    System.out.println("\n (not related)");//if this removed the below if else always kick in for the above donald grandpa ludwig fanny
                else if ((new_N2_p1 != null && (!new_N2_p1.isEmpty() && !isBlood_mix2)) || (new_N1_p1 != null
                        && (!new_N1_p1.isEmpty() && !isBlood_mix2)) && !(they_are != null && they_are.contains("husband")
                        || they_are != null && they_are.contains("wife")))
                    System.out.println("\n (not related)");//System.out.println("\nFar Child");
//                else if (isBlood_mix1 || isBlood_mix2)
//                    System.out.println("\nBastard Child");
                else System.out.println("\n" + they_are + " (not related)");
            } else System.out.println("\nSiblings in arms");
        } else if (isBlood_mix1 || isBlood_mix2){
            System.out.println("\nBastard Child");
        } else
            System.out.println("\n" + they_are + " (related) " + they_have);

 */


/*

        if (!incest) {
            if (!(isIsSiblings1Far && isIsSiblings2Far)) {
//                if (isSiblings1 && isSiblings2 && N1_initials != null && N1_initials.contains(family_MAIN_lastname)
//                && N2_initials != null && N2_initials.contains(family_MAIN_lastname) && !isBlood_mix1 || !isBlood_mix2)//why initials null?
//                    System.out.println("\nFar Siblings in arms");
                 if (isSiblings1 && isSiblings2 && (N1_initials != null && N1_initials.contains(family_MAIN_lastname) && isBlood_mix1)
                        && (N2_initials != null && N2_initials.contains(family_MAIN_lastname) && !isBlood_mix2))
                    System.out.println("\nFar Bastards in arms");
               else if (isSiblings1 && isSiblings2 && (N1_initials != null && N1_initials.contains(family_MAIN_lastname) && !isBlood_mix1)
                        && (N2_initials != null && N2_initials.contains(family_MAIN_lastname) && isBlood_mix2))
                    System.out.println("\nFar Bastards in arms");
                else if (isSiblings1 && isSiblings2) System.out.println("\nFar Siblings in arms");
                else if (!they_have.isEmpty())
                    System.out.println("\n" + they_are + " (not related) " + they_have);
                else if (isBlood_mix1 || isBlood_mix2)
                    System.out.println("\nBastard Child");
                else if ((N1_initials != null && N1_initials.contains(family_MAIN_lastname))
                        && (N2_initials != null && N2_initials.contains(family_MAIN_lastname)))
                    System.out.println("\nFar Child");
                else if (new_N2_p1 != null && !new_N2_p1.isEmpty() || new_N1_p1 != null && !new_N1_p1.isEmpty()
                        && (they_are != null && they_are.contains("husband") || they_are != null && they_are.contains("wife")))
                    System.out.println("\n" + they_are + " (not related)");
                else System.out.println("\n (not related)");
            } else System.out.println("\nSiblings in arms");
        } else if (isBlood_mix1 || isBlood_mix2){
            System.out.println("\nBastard Child");
        } else
            System.out.println("\n" + they_are + " (related) " + they_have);

 */



/*

       if (!incest) {
            if (!(isIsSiblings1Far && isIsSiblings2Far)) {
//                if (isSiblings1 && isSiblings2 && N1_initials != null && N1_initials.contains(family_MAIN_lastname)
//                && N2_initials != null && N2_initials.contains(family_MAIN_lastname) && !isBlood_mix1 || !isBlood_mix2)//why initials null?
//                    System.out.println("\nFar Siblings in arms");
                if (isSiblings1 && isSiblings2 && N1_initials_child != null && N1_initials_child.contains(family_MAIN_lastname)
                && N2_initials_child != null && !N2_initials_child.contains(family_MAIN_lastname) && !isBlood_mix1 || !isBlood_mix2)//why initials null?
                    System.out.println("\nFar Bastards in arms");
                else if (isSiblings1 && isSiblings2 && N1_initials_child != null && !N1_initials_child.contains(family_MAIN_lastname)
                        && N2_initials_child != null && N2_initials_child.contains(family_MAIN_lastname) && !isBlood_mix1 || !isBlood_mix2)//why initials null?
                    System.out.println("\nFar Bastards in arms");
//                 if (isSiblings1 && isSiblings2 && (N1_initials_child != null && N1_initials_child.contains(family_MAIN_lastname) && isBlood_mix1)
//                        && (N2_initials_child != null && N2_initials_child.contains(family_MAIN_lastname) && !isBlood_mix2))
//                    System.out.println("\nFar Bastards in arms");
//               else if (isSiblings1 && isSiblings2 && (N1_initials_child != null && N1_initials_child.contains(family_MAIN_lastname) && !isBlood_mix1)
//                        && (N2_initials_child != null && N2_initials_child.contains(family_MAIN_lastname) && isBlood_mix2))
//                    System.out.println("\nFar Bastards in arms");
                else if (isSiblings1 && isSiblings2) System.out.println("\nFar Siblings in arms");
                else if (!they_have.isEmpty())
                    System.out.println("\n" + they_are + " (not related) " + they_have);
                else if (isBlood_mix1 || isBlood_mix2)
                    System.out.println("\nBastard Child");
                else if ((N1_initials != null && N1_initials.contains(family_MAIN_lastname))
                        && (N2_initials != null && N2_initials.contains(family_MAIN_lastname)))
                    System.out.println("\nFar Child");
                else if (new_N2_p1 != null && !new_N2_p1.isEmpty() || new_N1_p1 != null && !new_N1_p1.isEmpty()
                        && (they_are != null && they_are.contains("husband") || they_are != null && they_are.contains("wife")))
                    System.out.println("\n" + they_are + " (not related)");
                else System.out.println("\n (not related)");
            } else System.out.println("\nSiblings in arms");
        } else if (isBlood_mix1 || isBlood_mix2){
            System.out.println("\nBastard Child");
        } else
            System.out.println("\n" + they_are + " (related) " + they_have);

 */

/*

if (!incest) {//far bastards in arms(gus gladstone) + not related may huey ..gus grandpa far bastard, may huey far in blood siblings mix
            if (!(isIsSiblings1Far && isIsSiblings2Far)) {
                if (isSiblings1 && isSiblings2 && N1_initials != null && N2_initials != null)
                {
                    if (N1_initials.contains(family_MAIN_lastname) && N1_initials_child != null
                            && !N1_initials_child.contains(family_MAIN_lastname)
                        && N2_initials.contains(family_MAIN_lastname) && N2_initials_child != null
                            && !N2_initials_child.contains(family_MAIN_lastname))
                        System.out.println("\n Far not related child's with \"in\" Blood Duck Parent");
                    else if (N1_initials.contains(family_MAIN_lastname) && N1_initials_child != null
                            && N1_initials_child.contains(family_MAIN_lastname)
                            && N2_initials.contains(family_MAIN_lastname) && N2_initials_child != null
                            && N2_initials_child.contains(family_MAIN_lastname))
                        System.out.println("\nFar Siblings in arms");
                    else
                        System.out.println("\n Far not related child's with \"in\" Blood Duck Parent");
                }
                else if (N1_initials.contains(family_MAIN_lastname)
                        && N2_initials.contains(family_MAIN_lastname))
                    System.out.println("\nFar Bastard child");
                else if (!they_have.isEmpty())
                    System.out.println("\n" + they_are + " (not related) " + they_have);
//                else if (isBlood_mix1 || isBlood_mix2)
//                    System.out.println("\nBastard Child");
                else if ((N1_initials != null && N1_initials.contains(family_MAIN_lastname)) && (N2_initials != null && N2_initials.contains(family_MAIN_lastname)))
                    System.out.println("\nFar Child");//System.out.println("\n (not related)");
                else if ((new_N2_p1 != null && (!new_N2_p1.isEmpty() && !isBlood_mix2)) || (new_N1_p1 != null
                        && (!new_N1_p1.isEmpty() && !isBlood_mix2)) && !(they_are != null && they_are.contains("husband")
                        || they_are != null && they_are.contains("wife")))
                    System.out.println("\n (not related)");//System.out.println("\nFar Child");
                else System.out.println("\n" + they_are + " (not related)");
            } else System.out.println("\nSiblings in arms");
        } else if (isBlood_mix1 || isBlood_mix2){
            System.out.println("\nBastard Child");
        } else
            System.out.println("\n" + they_are + " (related) " + they_have);

 */
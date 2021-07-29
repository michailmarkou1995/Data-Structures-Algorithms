package org.family;

import org.family.famillytree.Generation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

//dux dumbella, grandma grandpa, julious jira, quackmore fanny, fanny poopy, louis ludwig
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
 2) push github
 3) make resources Local Path instead of Read from File
 4) make web viz out of this like python */

public class Family {

    public static String N1, N2;
    public static boolean blood_hus = false, blood_wife = false;
    //Creating an Arraylist that stores objects <Person>
    static List<Generation> myList = new ArrayList<Generation>();
    static List<Generation> myList1 = new ArrayList<Generation>();
    static HashMap<String, Generation> myList2 = new HashMap<String, Generation>();
    static HashMap<String, String> hashNamesObj = new LinkedHashMap<String, String>();
    static ArrayList<String> arr = new ArrayList<String>();
    static List<List<String>> records1 = new ArrayList<>();
    static List<List<String>> records2 = new ArrayList<>();
    static HashMap<String, String> childs = new HashMap<String, String>();
    //StringBuffer
    static String they_are, they_are_not, they_have = "", root_p = null, husband, wife, last_name_husband, last_name_wife;
    static boolean related_as_same_root = false, not_children_of_parent = false, isSiblings1 = false, isSiblings2 = false, not_related_as_same_root = false, incest = false, add_they_have=false;
    static Generation gen = new Generation();

    //This function reads the csv file and stores its content on an Arraylist, printing the result
    public static void readcsv(String path, int input, String FName1_originalCase, String FName2_originalCase) {
        String line = "";
        final String csvSplitBy = ",";
        String FName1="", FName2="";
        if (FName1_originalCase == null && FName2_originalCase == null)
        {
        }
        else
        {
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
        Collections.sort(myList, new Comparator<Generation>() {

            //compares series of pairs of elements and based on result of compare it sees if elements from that pair should be swapped or not
            public int compare(Generation o1, Generation o2) {
                // compare two instance of `Score` and return `int` as result.
                return ~o2.getName().compareTo(o1.getName());
                // use ~ to reverse order
            }
        });
        //loop that prints the sorted result
        for (int i = 0; i < myList.size(); i++) {
            System.out.println(myList.get(i));
        }
        // Create csv
        FileWriter writer;
        try {
            //creating object writer with a path and charset format
            writer = new FileWriter("resources/Duck_generated.csv", Charset.forName("utf-8"));

            //this loop uses Filewriter "write" function to print the result on the newly created csv file
            for (Generation s : myList) {
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
        System.out.print("Enter 1st full character Person\nName: ");
        try (Scanner name1 = new Scanner(System.in)) {
            String Personname1 = name1.nextLine();
            Scanner name2 = new Scanner(System.in);
            System.out.print("Enter 2nd full character Person\nName: ");
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
            writerF = new FileWriter("resources/toGraphViz.dot", Charset.forName("utf-8"));

            writerF.write("digraph DUCK {\n");
            writerF.write("rankdir=LR;\n");
            writerF.write("size=\"8,5\"\n");
            writerF.write("node [shape = rectangle] [color=black];\n");

            myList2.entrySet().forEach(entry -> {
                System.out.println("\"" + entry.getValue().getName() + "\" -> \"" + entry.getValue().getChild() + "\" [label=\""
                        + entry.getValue().getRelated() + "\"];");
                try {
                    writerF.write("\"" + entry.getValue().getName() + "\" -> \"" + entry.getValue().getChild() + "\" [label=\""
                            + entry.getValue().getRelated() + "\"];\n");
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
        for (Generation g : Generation.getObj) {
            String[] names_childs_equals = g.getChild().toLowerCase().split(" ");
            for (int i = 0; i < names_childs_equals.length; i++) {
                if (N1.equals(names_childs_equals[i])) {
                    isSiblings1 = true;
                }
                if (N2.equals(names_childs_equals[i]))
                    isSiblings2 = true;
            }
        }

        // if they are something (2 inputs) meaning 3 cols how many childs have List?
        if (input == 3) {
            //if (related_as_same_root) { ///////////old replaced with add_they_have
            if (add_they_have) {
                AtomicInteger count = new AtomicInteger();
                //System.out.print("\n" + they_are); ///////////old just commented out
                if (!not_children_of_parent) {
                    childs.entrySet().forEach(entryChild -> {
                        if (childs.size() == 1) {
                            they_have += " and this is their child [" + entryChild.getValue() + "]";
                        } else {
                            count.getAndIncrement();
                            if (count.get() == 1)
                                they_have += "and have these children's [" + entryChild.getValue();
                            else if (count.get() != 1 && count.get() != childs.size())
                                they_have += "," + entryChild.getValue();
                            else they_have += "," + entryChild.getValue() + "]";
                        }
                    });
                }
            } //related_as_same_root = false;//else not_related_as_same_root = true; ///////////old replaced the second just commented out
        }
////dux dumbella, grandma grandpa, julious jira, quackmore fanny, fanny poopy, louis ludwig, quackmore dumbella
 //scrooge dumbella
          if (incest==false)
          {
              if (isSiblings1 && isSiblings2)
                  System.out.println("\nSiblings in arms");
              else if (!they_have.isEmpty())
                  System.out.println("\n" + they_are + " (not related) " + they_have);
              else
                  System.out.println("\nnot related");
          }
          else if (incest==true)
          {
              System.out.println("\n" + they_are + " (related) " + they_have);
          }
          else
          {
              System.out.println("\nnot related");
          }

        ///////////OLD FUNCTIONALLITY RECOVER by uncommenting THE 10 / slashes
//        if (((not_related_as_same_root == true == ((isSiblings1 == isSiblings2) == true)) != incest)) {//&& they_have.isEmpty()
//            if ((!(isSiblings1 == isSiblings2) != incest)) {//&& !they_have.isEmpty()
//                if (childs.size() == 1)
//                    System.out.println(they_have);
//                else if (((!not_children_of_parent && related_as_same_root) != incest) && they_have.isEmpty() == false)
//                    System.out.println(" and have these children's [" + they_have + "]");
//                else if ((!not_children_of_parent && related_as_same_root) != incest) {
//                } else if (incest)
//                    System.out.println(" They are also Siblings/Incest and have these children's [" + they_have + "]");
//            } else
//                System.out.println("Siblings in Arms");
//        } else if ((!they_are.isEmpty() != !incest) /*&& they_have.isEmpty() == false*/) {
//            if (childs.size() == 1)
//                System.out.println(they_have);
//            else if ((!not_children_of_parent && related_as_same_root) != incest)
//                System.out.println(" and have these children's [" + they_have + "]");
//        } else if (!they_are.isEmpty()  && they_have.isEmpty() == false) {
//            String inc = "(related)";
//            if (childs.size() == 1)
//                if (!related_as_same_root)
//                    System.out.println(they_have);
//                else
//                    System.out.println(they_have+ " " + inc);
//            else if ((!not_children_of_parent && related_as_same_root))
//                System.out.println(" and have these children's [" + they_have + "]");
//        } else if (!they_are.isEmpty() && they_have.isEmpty() == true) {
//        } else if (!they_are.isEmpty()) {
//        } else System.out.println("They Are not Related");
    }

    /* kane se olous osous no incest na leei no related + opoioi ine makrinoi na leei not related */
    private static void input_checking_scenario_Loop(int input, String FName1, String FName2, String[] allStrings) {
        String[] decoy_strings=allStrings.clone();
        String[] allStringsLC=decoy_strings;
        for (int i = 0; i < allStrings.length; i++) {
            allStringsLC[i]=allStrings[i].toLowerCase(Locale.ROOT); //str.substring(0,1).toUpperCase()
        }

        // menu 1 print List of CSV after read
        if (input == 1)
            System.out.println(Arrays.toString(allStrings));

        // sort csv used
        if (input != 3 && input != 4)
            if (allStrings.length == 2) {
                myList.add(new Generation(allStrings[0], allStrings[1]));
            }

        // Search and Fine Relations part
        if (input == 3) {
            if (allStringsLC[0].contains(FName1.toLowerCase())) {
                records1.add(Arrays.asList(allStrings));
            }
            // only if they have 3 csv columns
            if (allStrings.length == 3) {

                // check father or mother both have in common for Create ".dot" File .. DB simulation with Hashmap uses PK
                // as new Random and Values the object to access itself
                if ((allStrings[1].equals("mother")) || (allStrings[1].equals("father"))) {
                    myList2.put(String.valueOf(new Random().nextInt()), gen = new Generation(allStrings[0], allStrings[1], allStrings[2]));
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
                        for (int i = 0; i < hus_name.length; i++) {
                            if (hus_name[i].equals("Duck")) blood_hus = true;
                        }
                    }
                    if (allStrings[1].equals("wife")) {
                        wife = allStrings[1];
                        String[] hus_wife = allStrings[0].split(" ");
                        for (int i = 0; i < hus_wife.length; i++) {
                            if (hus_wife[i].equals("Duck")) blood_wife = true;
                        }
                    }
                    if (blood_hus == blood_wife) incest = true;

                    they_are = allStrings[0] + " is " + allStrings[1] + " of " + allStrings[2];

                    add_they_have = true;
                    //related_as_same_root =true; ///////////old replaced with add_they_have
                } //else they_are_not = "not related"; ///////////old replaced with add_they_have
            }
        }
        if (input == 4) {
            //myList.clear();
            if (allStrings.length == 3) {
                if ((allStrings[1].equals("mother")) || (allStrings[1].equals("father"))) {
                    myList1.add(new Generation(allStrings[0], allStrings[1], allStrings[2]));
                    myList2.put(String.valueOf(new Random().nextInt()), new Generation(allStrings[0], allStrings[1], allStrings[2]));
                }
            }
        }
    }

    public static void main(String[] args) {
        //Creating the text menu
        String csvFile = Objects.requireNonNull(new Family().getClass().getClassLoader().getResource("family.csv")).getPath();
        System.out.println("***This is the Duck family tree app***");
        System.out.println("***Main Menu***");
        try (Scanner menu = new Scanner(System.in)) {
            System.out.println("Press 1 to read the family members list \nPress 2 to create an alphabetically sorted family members list \nPress 3 to use the relationship app \nPress 4 to generate the .dot file");
            int input = menu.nextInt();
            switch (input) {
                case 1:
                    readcsv(csvFile, input, null, null);
                    break;
                case 2:
                    //It is important to run readcsv() first, for sortcsv() to work
                    readcsv(csvFile, input, null, null);
                    System.out.println("\n");
                    sortcsv();
                    break;
                case 3:
                    searchNameRelations(csvFile, input);
                    break;
                case 4:
                    myList.clear();
                    createDot(csvFile, input);
                    break;
                default:
                    System.out.println("Please try again!");
            }
        } catch (Exception e) {
            System.out.println("Input Error ... Program Exits ...");
            e.printStackTrace();
            System.exit(1);
        }
    }
}

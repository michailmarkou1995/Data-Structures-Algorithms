package org.family;

import org.family.famillytree.Generation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/*
.\dot.exe -Tsvg .\toGraphviz.dotbak
 1) TO-DO make menu 3, 4 with 3? uses enter a name and see local tree like a short story father-grandfather + children
   1.1) give Manios
   1.2) Make Loop Menu
   1.3) Read csv search
   1.4) remove 1.3 and make graph search convert before search
 2) push github
 3) make resources Local Path instead of Read from File
 4) make web viz out of this like python */

public class Family {

    //Creating an Arraylist that stores objects <Person>
    static List<Generation> myList = new ArrayList<Generation>();
    static List<Generation> myList1 = new ArrayList<Generation>();
    static HashMap<String, Generation> myList2 = new HashMap<String, Generation>();
    static HashMap<String, String> hashNamesObj = new LinkedHashMap<String, String>();
    static ArrayList<String> arr = new ArrayList<String>();
    static List<List<String>> records1 = new ArrayList<>();
    static List<List<String>> records2 = new ArrayList<>();
    static List<String> childs = new ArrayList<>();
    static HashMap<String, String> childs1 = new HashMap<String, String>();
    //static HashMap<Integer, String> childs1 = new HashMap<Integer, String>();
    //StringBuffer
    static String they_are, they_are_not, they_have="", root_p=null, husband,wife, last_name_husband, last_name_wife;
    static boolean related = false, not_childrens = false, isSiblings1=false, isSiblings2=false, they_are_not_B=false
            , incest=false;
    static Generation gen = new Generation();
    public static String N1,N2;
    public static boolean blood_hus=false, blood_wife=false;

    //This function reads the csv file and stores its content on an Arraylist, printing the result
    public static void readcsv(String path, int input, String FName1, String FName2) {
        String line = "";
        final String csvSplitBy = ",";
        N1=FName1;
        N2=FName2;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            //this loop parses every line of the csv file and stores it on the Arraylist mylist
            while ((line = br.readLine()) != null) {
                String[] allStrings = line.split(csvSplitBy);
                if (input == 1)
                    System.out.println(Arrays.toString(allStrings));
                if (input != 3 && input != 4)
                    if (allStrings.length == 2) {
                        myList.add(new Generation(allStrings[0], allStrings[1])); //that has no use to much USE /*allString*/ to store it Somewhere !!!!
                    }
                if (input == 3) {
                    if (allStrings[0].contains(FName1)) {
//                        hashNamesObj.put("Name", allStrings[0]);
//                        hashNamesObj.put("Childs", allStrings[1]);
//                        hashNamesObj.put("Gender", allStrings[2]);
                        records1.add(Arrays.asList(allStrings));
                    }
//                    if (allStrings[0].contains(FName2))
//                    {
//                        records2.add(Arrays.asList(allStrings));
//                    }
                    if (allStrings.length == 3) {

                        //check father or mother both have in common
                        if ((allStrings[1].equals("mother")) || (allStrings[1].equals("father"))) {
                            myList2.put(String.valueOf(new Random().nextInt()), gen = new Generation(allStrings[0], allStrings[1], allStrings[2]));
                            //System.out.println(gen);
/*                            System.out.println(gen.getName().contains(FName1));
                            if(gen.getName().contains(FName1)) System.out.println("YEAH");
                            if (FName1.equals(myList2.containsValue(gen.getName().equals(FName1)))){
                                System.out.println(FName1.equals(myList2.containsValue(gen)) +"hi");
                            }*/
                        }
//                        else if (FName1 == myList2.containsValue(new Generation(allStrings[0], allStrings[1], allStrings[2]))){
//
//                        }
                        if ((allStrings[0].contains(FName1) && (allStrings[1].contains("mother") || allStrings[1].contains("father"))
                                || allStrings[0].contains(FName2) && (allStrings[1].contains("mother") || allStrings[1].contains("father")))) {
                            //&& allStrings[0].contains(FName1) != allStrings[2].contains(FName2)
                            //&& allStrings[0].contains(FName2) != allStrings[2].contains(FName1))//allStrings[2].contains(FName2))
                            if ((allStrings[0].contains(FName1) && (allStrings[1].contains("mother") || allStrings[1].contains("father"))
                                    == allStrings[2].contains(FName2))
                                    || (allStrings[0].contains(FName2) && (allStrings[1].contains("mother") || allStrings[1].contains("father"))
                                    == allStrings[2].contains(FName1))) {
                                not_childrens = true;
                            }
                            childs.add(allStrings[2]);
                            childs1.put(allStrings[2],allStrings[2]);
                        }
                        if (allStrings[0].contains(FName1) && allStrings[2].contains(FName2)
                        || allStrings[2].contains(FName1) && allStrings[0].contains(FName2)) {
//                            System.out.print(allStrings[0] + " is " + allStrings[1] + " of "
//                                    + allStrings[2] + " and have these children's ");
                            // if (== father) {//if they are related with the root they are siblings
                               // {
                                    //related = true; or siblings = true;
                                    /*Kane graphs implement EDW????
                                    * MyList2 run edw? kai sto 3? na bris ta Parent?*/
                                //}
                            //}

                            if(allStrings[1].equals("husband")) {
                                husband=allStrings[1];
                                String[] hus_name = allStrings[0].split(" ");
                                for (int i = 0; i < hus_name.length; i++) {
                                    if(hus_name[i].equals("Duck") ) blood_hus = true;
                                }
                            }
                            if(allStrings[1].equals("wife")) {
                                wife=allStrings[1];
                                String[] hus_wife = allStrings[0].split(" ");
                                for (int i = 0; i < hus_wife.length; i++) {
                                    if(hus_wife[i].equals("Duck")) blood_wife = true;
                                }
                            }
                            if (blood_hus==blood_wife) incest = true;

                            they_are = allStrings[0] + " is " + allStrings[1] + " of " + allStrings[2];
                            related = true;
/*                            System.out.println(myList2.entrySet().forEach(entry->{
                                //kane kapws prin i meta to else if na bris to getRelated?
                            }););*/
                        } /*else if (allStrings[0].contains(FName1) == myList2.containsValue(allStrings[0].contains(FName1)
                                && allStrings[2].contains(FName2) == myList2.containsValue(allStrings[2].contains(FName2)))
//                                myList2.entrySet().forEach(entry -> {
//                            //System.out.println(entry.getKey() + " " + entry.getValue());
//                            System.out.println("\"" + entry.getValue().getName() + "\" -> \"" + entry.getValue().getChild() + "\" [label=\""
//                                    + entry.getValue().getRelated() + "\"];");
//                        });
                        ){


                        }*/ else they_are_not = "not related";
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
            br.close();
            if (input == 3) {
                //System.out.println(records);
                //System.out.println(records1);
                for (int i = 0; i < records1.size(); i++) {
//                    System.out.println(records1);
//                    System.out.println(records1.contains(FName2));
//                    if (records1.get(2).contains(FName2))
//                    System.out.println(FName1 + " is " + records1.get(1));
                }
                if (related) {
                    AtomicInteger count = new AtomicInteger();
                    System.out.print("\n" + they_are);
                    if (!not_childrens) {
                        //System.out.print(" and have these children's " + childs);
                        childs1.entrySet().forEach(entryChild -> {
                            //System.out.print(" and have these children's [" + entryChild.getValue() +"]");
                            if (childs1.size() == 1) {
                                they_have += " and this is their child [" + entryChild.getValue() + "]";
                            } else {
                                count.getAndIncrement();
                                if (count.get() == 1)
                                    they_have += entryChild.getValue();
                                else
                                    they_have += "," + entryChild.getValue();
                            }

                            //they_have += " and have these children's [" + entryChild.getValue() +"]";
                        });
                    }
                } else they_are_not_B=true;//System.out.println(they_are_not);
            }

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
                writer.write(" \n"); // newline
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void searchName(String csvFile, int input) {
        System.out.println("\n***Find Relationships***");
        System.out.print("Enter 1st full character Person\nName: ");
        try (Scanner name1 = new Scanner(System.in)) {
            String Personname1 = name1.nextLine();
            Scanner name2 = new Scanner(System.in);
            System.out.print("Enter 2nd full character Person\nName: ");
            String Personname2 = name2.nextLine();
            //myList.get()
            //System.out.println(myList);  //make this key value pair as object so [Generation{name='﻿Grandpa Duck', gender='male'}, Generation{name='Grandma Duck', gender='female'},....
            //becomes like object as key value as name + gender?
            readcsv(csvFile, input, Personname1, Personname2);
            for (Generation g : Generation.getObj) {
                String[] names_childs_equals = g.getChild().split(" ");
                for (int i = 0; i < names_childs_equals.length; i++) {
                    if (N1.equals(names_childs_equals[i])){
                        isSiblings1=true;
                    }
                    if (N2.equals(names_childs_equals[i]))
                        isSiblings2=true;
//                    if(names_childs_equals[i]=="Duck"){
//                        isSiblings1=true;
//                        isSiblings2=true;
//                    }
                    /*******************/
//                    if (wife == husband){
//                        for
//                    }
                }
            }
//            if(incest){
//                isSiblings1=true;
//                isSiblings2=true;
//            }

            if (((they_are_not_B==true == ((isSiblings1 == isSiblings2)==true)) != incest) ) {//&& they_have.isEmpty()
                if ((!(isSiblings1 == isSiblings2) != incest) ) {//&& !they_have.isEmpty()
                    if (childs1.size() == 1)
                        System.out.println(they_have);
                    else if ((!not_childrens && related) != incest)
                        System.out.println(" and have these children's [" + they_have + "]");
                    else if(incest)
                        System.out.println(" They are also Siblings/Incest and have these children's [" + they_have + "]");
                    //else if (!not_childrens && !related)
                    //System.out.println("Siblings in Arms");
                    //System.out.println(Generation.getObj);
                } else
                    System.out.println("Siblings in Arms");
            } else if (!they_are.isEmpty() != !incest){
                if (childs1.size() == 1)
                    System.out.println(they_have);
                else if ((!not_childrens && related) != incest)
                    System.out.println(" and have these children's [" + they_have + "]");
            } else if (!they_are.isEmpty()){
            } else System.out.println("They Are not Related");
        }

    }

    private static void createDot(String csvFile, int input) {
        readcsv(csvFile, input, null, null);
        FileWriter writerF;
        try {//Objects.requireNonNull(new Family().getClass().getClassLoader().getResource("toGraphviz.dot")).getPath()
            //creating object writer with a path and charset format
            writerF = new FileWriter("resources/toGraphViz.dot", Charset.forName("utf-8"));

            //this loop uses Filewriter "write" function to print the result on the newly created csv file
//            for(Generation s : myList)
//            {
//                writerF.write(s.toString());
//                writerF.write(" \n"); // newline
//            }
            writerF.write("digraph DUCK {\n");
            writerF.write("rankdir=LR;\n");
            writerF.write("size=\"8,5\"\n");
            writerF.write("node [shape = rectangle] [color=black];\n");

            //System.out.println(myList);
            for (int i = 0; i < myList1.size(); i++) {
                //System.out.println(myList1.get(i));
                //writeF.write(myList1.toString());
                //writerF.write(" \n"); // newline
            }

//            for (int i = 0; i < myList2.size(); i++) {
//                writerF.write(myList2.toString());
//                writerF.write(" \n"); // newline
//            }

            myList2.entrySet().forEach(entry -> {
                //System.out.println(entry.getKey() + " " + entry.getValue());
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

    public static void main(String[] args) {
        //Creating the text menu
        ClassLoader classloader = Thread.currentThread().getContextClassLoader();
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
                    searchName(csvFile, input);
                    break;
                case 4:
                    myList.clear();
                    createDot(csvFile, input);
                    break;
                default:
                    System.out.println("Please try again!");
            }
        }
    }
}

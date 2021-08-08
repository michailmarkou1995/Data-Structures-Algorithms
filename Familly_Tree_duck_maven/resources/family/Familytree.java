package org.family;

import org.family.famillytree.Family;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Familytree {

    static List<Family> myList = new ArrayList<Family>();
    static HashMap<String, Family> myList1 = new HashMap<String, Family>();
    static List<String> childs = new ArrayList<>();
    static String they_are, they_are_not;
    static boolean related = false;

    public static void readcsv(String path, int input, String FName1, String FName2) {
        String line = "";
        final String csvSplitBy = ",";
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while ((line = br.readLine()) != null) {
                String[] allStrings = line.split(csvSplitBy);
                if (input == 1)
                    System.out.println(Arrays.toString(allStrings));
                if (input != 3 && input != 4)
                    if (allStrings.length == 2) {
                        myList.add(new Family(allStrings[0], allStrings[1]));
                    }
                if (input == 3) {
                    if (allStrings.length == 3) {
                        if ((allStrings[1].equals("mother")) || (allStrings[1].equals("father"))) {
                            myList1.put(String.valueOf(new Random().nextInt()), new Family(allStrings[0], allStrings[1], allStrings[2]));
                        }

//                        if (allStrings[0].contains(FName1) && (allStrings[1].contains("mother?husband?") || allStrings[1].contains("father?wife"))
//                                && allStrings[2].contains(FName2)
//                                || allStrings[0].contains(FName2) && (allStrings[1].contains("husband") || allStrings[1].contains("wife"))
//                                && allStrings[2].contains(FName1))
//                        {
//                            //childs.add(allStrings[2]);
//                        }

                        if (allStrings[0].contains(FName1) && allStrings[2].contains(FName2)) {
                            they_are = allStrings[0] + " is " + allStrings[1] + " of " + allStrings[2];
                            related = true;
                        } else if (allStrings[2].contains(FName1) && allStrings[0].contains(FName2)) {
                            they_are = allStrings[0] + " is " + allStrings[1] + " of " + allStrings[2];
                            related = true;
                        } else they_are_not = "not related";
                    }
                }
                if (input == 4) {
                    if (allStrings.length == 3) {
                        if ((allStrings[1].equals("mother")) || (allStrings[1].equals("father"))) {
                            myList1.put(String.valueOf(new Random().nextInt()), new Family(allStrings[0], allStrings[1], allStrings[2]));
                        }
                    }
                }

            }
            if (input == 3) {
                if (related) {
                    myList1.entrySet().forEach(entry -> {
                        if (they_are.contains("husband") || they_are.contains("wife") && entry.getValue().getName().contains(FName1))
                            childs.add(entry.getValue().getChild());
                    });
                    System.out.print("\n" + they_are);
                    if (they_are.contains("husband") || they_are.contains("wife"))
                        System.out.print(" and have these children's " + childs);
                } else if (FName1.contains("Duck") && FName2.contains("Duck")) {
                    System.out.println("They Are Related");
                } else System.out.println(they_are_not);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void sortcsv() {
        Collections.sort(myList, new Comparator<Family>() {

            public int compare(Family o1, Family o2) {
                return ~o2.getName().compareTo(o1.getName());
            }
        });
        for (int i = 0; i < myList.size(); i++) {
            System.out.println(myList.get(i));
        }
        FileWriter writer;
        try {
            writer = new FileWriter("C:\\Users\\backt\\downloads\\Programs\\test\\Duck_generated.csv", StandardCharsets.UTF_16);

            for (Family s : myList) {
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
            readcsv(csvFile, input, Personname1, Personname2);
        }

    }

    private static void createDot(String csvFile, int input) {
        readcsv(csvFile, input, null, null);
        FileWriter writerF;
        try {
            writerF = new FileWriter("C:\\Users\\backt\\downloads\\Programs\\test\\toGraphviz.dot", StandardCharsets.UTF_8);

            writerF.write("digraph DUCK {\n");
            writerF.write("rankdir=LR;\n");
            writerF.write("size=\"8,5\"\n");
            writerF.write("node [shape = rectangle] [color=black];\n");

            myList1.entrySet().forEach(entry -> {
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //Creating the text menu
        String csvFile = "C:\\Users\\backt\\Downloads\\Programs\\test\\family.csv";
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
                    readcsv(csvFile, input, null, null);
                    System.out.println("\n");
                    sortcsv();
                    break;
                case 3:
                    searchName(csvFile, input);
                    break;
                case 4:
                    createDot(csvFile, input);
                    break;
                default:
                    System.out.println("Please try again!");
            }
        }
    }
}

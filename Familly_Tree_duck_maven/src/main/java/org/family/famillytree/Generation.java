package org.family.famillytree;

import java.util.ArrayList;
import java.util.List;

public class Generation {
    public static List<Generation> getObj_wFatherMother = new ArrayList<>();  // same info as child_list
    public static List<String> child_list = new ArrayList<>();  // one by one add
    public static List<String> child_list1 = new ArrayList<>();
    public List<String> child_list1_obj = new ArrayList<>();
    public static List<Generation> getObj_wHusWife = new ArrayList<>();
    private String name = new String(), gender, related, child, wife, husband;

    // simple ArrayList Sort
    public Generation(String allStringsName, String allStringsGender) {
        this.setName(allStringsName);
        this.gender = allStringsGender;
    }

    public Generation(String allStringsName, String allStringsRelated, String Child) {
        this.setName(allStringsName);
        this.related = allStringsRelated;
        this.child = Child;
        getObj_wFatherMother.add(this);
    }

    public Generation(String allStringsName, String Wife, String Husband, String allStringsRelated) {
        this.setName(allStringsName);
        this.related = allStringsRelated;
        this.wife = Wife;
        this.husband = Husband;
        getGetObj_wHusWife().add(this);
    }

    public Generation(String Name, String Gender, String related, String Wife, String Husband) {
        this.setName(Name);
        this.gender = Gender;
        this.related = related;
        this.wife = Wife;
        this.husband = Husband;
        getGetObj_wHusWife().add(this);
    }

    public Generation(String Name, String related, String Husband, int decoy1) {
        this.name = Name;
        this.related = related;
        this.husband = Husband;
    }

    public Generation(String Name, String related, String wife, String child, int decoy1, int decoy2) {
        //this.name = Name;
        this.husband = Name;
        this.related = related;
        this.wife = wife;
        //child_list.add(child);
        //child_list1_obj.add(child);
        //this.child += child;
    }

    public Generation() {
    }

    public static List<Generation> getGetObj_wHusWife() {
        return getObj_wHusWife;
    }

    public static void setGetObj_wHusWife(List<Generation> getObj_wHusWife) {
        Generation.getObj_wHusWife = getObj_wHusWife;
    }

    public static List<Generation> getGetObj_wFatherMother() {
        return getObj_wFatherMother;
    }

    public static void setGetObj_wFatherMother(List<Generation> getObj_wFatherMother) {
        Generation.getObj_wFatherMother = getObj_wFatherMother;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRelated() {
        return related;
    }

    public void setRelated(String related) {
        this.related = related;
    }

    public String getChild() {
        return child;
    }

    public void setChild(String child) {
        this.child = child;
    }

    public void setChildConcat(String child) {
        // if (this.child.contains("null"))// if null then null pointer exception do not use .contains func
        if (this.child == null) // this to every obj memory reference and not static
        {
            //this.child=child + " ";
            this.child = child; // does not leave trailing whitespace for future trim this way!
        } else
            this.child += ", " + child; // does not leave trailing whitespace for future trim this way!
        //this.child += child + " ";
    }

    public static List<String> getChild_list1() {
        return child_list1;
    }

    public static void setChild_list1(List<String> child_list1) {
        Generation.child_list1 = child_list1;
    }

    public static void setChild_list1_one_by_one(String child_list1) {
        Generation.child_list1.add(child_list1);
    }

    public static List<String> getChild_list() {
        return child_list;
    }

    public static void setChild_list(List<String> child_list) {
        Generation.child_list = child_list;
    }

    public static void setChild_list_one(String child_list) {
        Generation.child_list1.add(child_list);
    }

    public void setChild_list_oneObj(String child_list) {
        this.child_list1_obj.add(child_list);
    }

    public Generation setChildObj(String child) {
        this.child = child;
        return this;
    }

    public String getWife() {
        return wife;
    }

    public void setWife(String wife) {
        this.wife = wife;
    }

    public Generation setWifeObj(String wife) {
        this.wife = wife;
        return this;
    }

    public String getHusband() {
        return husband;
    }

    public void setHusband(String husband) {
        this.husband = husband;
    }

    public Generation setHusbandObj(String husband) {
        this.husband = husband;
        return this;
    }

    @Override
    public String toString() {
        return "Generation{" +
                "husband='" + husband + '\'' +
                ", wife='" + wife + '\'' +
                ", child='" + child + '\'' +
                ", child='" + child_list1_obj + '\'' +
                '}';
    }
}

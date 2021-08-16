package org.family.famillytree;

import java.util.ArrayList;
import java.util.List;

public class Generation implements Comparable<Generation> {
    public static List<Generation> getObj_wFatherMother = new ArrayList<>();  // same info as child_list
    public static List<Generation> getObj_wHusWife = new ArrayList<>();
    private final List<String> child_list1_obj = new ArrayList<>();
    private Long id;
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


    public Generation(String Name, String related, String wife, String child, int decoy1, int decoy2) {
        //this.name = Name;
        this.husband = Name;
        this.related = related;
        this.wife = wife;
        //if (child != null || !child.isEmpty() || !child.isBlank())  // null pointer if 1 line all together split them
        if (child != null)
            if (!child.isEmpty() || !child.isBlank())
                setChildConcat(child);
    }

    public Generation() {
    }

    public static List<Generation> getGetObj_wHusWife() {
        return getObj_wHusWife;
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

    public String getRelated() {
        return related;
    }

    public String getChild() {
        return child;
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

    public void setChild_list1_obj(String child) {
        this.child_list1_obj.add(child);
    }

    public String getWife() {
        return wife;
    }

    public void setWife(String wife) {
        this.wife = wife;
    }

    public String getHusband() {
        return husband;
    }

    public void setHusband(String husband) {
        this.husband = husband;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Generation{" +
                "husband='" + husband + '\'' +
                ", wife='" + wife + '\'' +
                ", child='" + child + '\'' +
                //", child='" + child_list1_obj + '\'' +
                '}';
    }

    @Override
    public int compareTo(Generation o) {
        // Natural order in field "Name"
        return this.getName().compareTo(o.getName());
    }
}

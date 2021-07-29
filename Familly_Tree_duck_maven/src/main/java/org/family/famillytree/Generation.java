package org.family.famillytree;

import java.util.ArrayList;
import java.util.List;

public class Generation {
    public static List<Generation> getObj = new ArrayList<>();
    private String name, gender, related, child, wife, husband;

    public Generation(String allStringsName, String allStringsGender) {
        this.setName(allStringsName);
        this.gender = allStringsGender;
    }

    public Generation(String allStringsName, String allStringsRelated, String Child) {
        this.setName(allStringsName);
        this.related = allStringsRelated;
        this.child = Child;
        getObj.add(this);
    }

    public Generation(String allStringsName, String Wife, String Husband, String allStringsRelated) {
        this.setName(allStringsName);
        this.related = allStringsRelated;
        this.wife = Wife;
        this.husband = Husband;
    }

    public Generation() {
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

    public static List<Generation> getGetObj() {
        return getObj;
    }

    public static void setGetObj(List<Generation> getObj) {
        Generation.getObj = getObj;
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

    @Override
    public String toString() {
        return "Generation{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", related='" + related + '\'' +
                ", child='" + child + '\'' +
                '}';
    }
}

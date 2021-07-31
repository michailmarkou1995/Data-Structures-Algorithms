package org.family.famillytree;

import java.util.ArrayList;
import java.util.List;

public class Generation {
    public static List<Generation> getObj_wFM = new ArrayList<>();
    public static List<Generation> getObj_wHW = new ArrayList<>();
    private String name, gender, related, child, wife, husband;

    public Generation(String allStringsName, String allStringsGender) {
        this.setName(allStringsName);
        this.gender = allStringsGender;
    }

    public Generation(String allStringsName, String allStringsRelated, String Child) {
        this.setName(allStringsName);
        this.related = allStringsRelated;
        this.child = Child;
        getObj_wFM.add(this);
    }

    public Generation(String allStringsName, String Wife, String Husband, String allStringsRelated) {
        this.setName(allStringsName);
        this.related = allStringsRelated;
        this.wife = Wife;
        this.husband = Husband;
        getGetObj_wHW().add(this);
    }

    public Generation() {
    }

    public static List<Generation> getGetObj_wHW() {
        return getObj_wHW;
    }

    public static void setGetObj_wHW(List<Generation> getObj_wHW) {
        Generation.getObj_wHW = getObj_wHW;
    }

    public static List<Generation> getGetObj_wFM() {
        return getObj_wFM;
    }

    public static void setGetObj_wFM(List<Generation> getObj_wFM) {
        Generation.getObj_wFM = getObj_wFM;
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

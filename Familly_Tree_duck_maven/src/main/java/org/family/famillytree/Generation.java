package org.family.famillytree;

import java.util.ArrayList;
import java.util.List;

public class Generation {
    private String name, gender, related, child;
    public static List<Generation> getObj = new ArrayList<>();

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

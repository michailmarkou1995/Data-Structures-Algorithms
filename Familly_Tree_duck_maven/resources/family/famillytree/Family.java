package org.family.famillytree;

public class Family {
    private String name, gender, related, child;

    public Family(String allStringsName, String allStringsGender) {
        this.setName(allStringsName);
        this.gender = allStringsGender;
    }

    public Family(String allStringsName, String allStringsRelated, String Child) {
        this.setName(allStringsName);
        this.related = allStringsRelated;
        this.child = Child;
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
        return "FamilyTree{" +
                "name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", related='" + related + '\'' +
                ", child='" + child + '\'' +
                '}';
    }
}

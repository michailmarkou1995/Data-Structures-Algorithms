package org.family.famillytree;

public class Parent extends Generation {

    public Parent(String allStringsName, String allStringsRelated, String Child) {
        super(allStringsName, allStringsRelated, Child);
    }

    @Override
    public String toString() {
        return "Generation{" +
                "name='" + getName() + '\'' +
                ", related='" + getRelated() + '\'' +
                ", child='" + getChild() + '\'' +
                '}';
    }
}

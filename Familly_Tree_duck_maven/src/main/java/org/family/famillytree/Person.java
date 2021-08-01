package org.family.famillytree;

public class Person extends Generation {

    public Person(String allStringsName, String allStringsGender) {
        super(allStringsName, allStringsGender);
    }

    @Override
    public String toString() {
        return "Generation{" +
                "name='" + getName() + '\'' +
                ", gender='" + getGender() + '\'' +
                '}';
    }
}

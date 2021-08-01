package org.family.famillytree;

public class Wife extends Generation {

    public Wife(String allStringsName, String Wife, String Husband) {
        super.setName(allStringsName);
        super.setWife(Wife);
        super.setHusband(Husband);
    }

    @Override
    public String toString() {
        return "Wife{" +
                "name='" + getName() + '\'' +
                ", related='" + getWife() + '\'' +
                ", husband='" + getHusband() + '\'' +
                '}';
    }
}

package org.family.famillytree;

public class Husband extends Generation {
    @Override
    public String toString() {
        return "Husband{" +
                "name='" + getName() + '\'' +
                ", related='" + getRelated() + '\'' +
                ", wife='" + getWife() + '\'' +
                '}';
    }
}

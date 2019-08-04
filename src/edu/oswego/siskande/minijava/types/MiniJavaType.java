package edu.oswego.siskande.minijava.types;

import java.util.Objects;

public class MiniJavaType {
    private final String name;
    private final String parent;

    public MiniJavaType(String name) {
        this.name = name;
        this.parent = null;
    }

    public MiniJavaType(String name, String parent) {
        this.name = name;
        this.parent = parent;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MiniJavaType type = (MiniJavaType) o;
        return Objects.equals(name, type.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

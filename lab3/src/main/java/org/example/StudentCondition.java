package org.example;

public enum StudentCondition {
    Refurb {
        @Override public String toString() {
            return "Odrabiający";
        }
    },
    Sick {
        @Override public String toString() {
            return "Chory";
        }
    },
    Absent {
        @Override public String toString() {
            return "Nieobecny";
        }
    },
    Other {
        @Override public String toString() {
            return "Inny";
        }
    };
    public static StudentCondition fromString(String name) {
        if(name.equals(StudentCondition.Refurb.toString())) {
            return Refurb;
        }
        if(name.equals(StudentCondition.Sick.toString())) {
            return Sick;
        }
        if(name.equals(StudentCondition.Absent.toString())) {
            return Absent;
        }
        return Other;
    }
}

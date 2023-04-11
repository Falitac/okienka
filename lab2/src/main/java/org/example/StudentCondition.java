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
}

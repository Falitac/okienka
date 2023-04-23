package org.example;

public class Main {
    public static void main(String[] args) {
        GradeBook gradeBook = new GradeBook();
        try {
            gradeBook.run();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

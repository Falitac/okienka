package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        Student student1 = new Student("Jan", "Kowalski", StudentCondition.Absent, 2000, 20.0);
        Student student2 = new Student("Jan", "Arman", StudentCondition.Absent, 2000, 20.0);

        System.out.println(student1.compareTo("Kowalski"));
        student1.print();

        ClassContainer container = new ClassContainer();
        container.addClass("Informatyka", 0.5);
        var itClass = container.getClass("Informatyka");

        if(itClass == null) {
            System.out.println("This class doesn't exist");
            return;
        }

        itClass.addStudent(student1);
        itClass.addStudent(student2);

        container.summary();
    }
}
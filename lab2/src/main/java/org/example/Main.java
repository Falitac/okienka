package org.example;

public class Main {
    public static void main(String[] args) {
        System.out.println("Grade book");
        Student student1 = new Student("Jan", "Kowalski", StudentCondition.Absent, 2000, 20.0);
        Student student2 = new Student("Jan", "Arman", StudentCondition.Refurb, 2000, 20.0);

        System.out.println("Equal? " + student1.compareTo("Kowalski")); // same
        System.out.println("Equal? " + student1.compareTo("Grabosz"));  // different
        student1.print();

        ClassContainer container = new ClassContainer();
        container.addClass("Informatyka", 0.1); // 10% * 64 -> max 6 osób
        if(!container.addClass("Informatyka", 0.25)) {
            System.out.println("We cannot add the same class to our container");
        }

        var itClass = container.getClass("Informatyka");

        if(itClass == null) {
            System.out.println("This class doesn't exist");
            return;
        }

        itClass.addStudent(student1);
        itClass.addStudent(student2);

        if(!itClass.addStudent(student2)) {
            System.out.println("Example of invalid student add operation");
        }

        student1.addPoints(4);
        container.summary();

        Student student3 = itClass.getStudent(student1);
        itClass.changeCondition(student2, StudentCondition.Sick);
        container.summary();

        student1.addPoints(3.5);
        container.summary();
        Student foundStudent = itClass.search("Arman");

        if(foundStudent != null) {
            System.out.println("Found him: ");
            foundStudent.print();
        }

        itClass.addStudent(new Student("Jan", "Klacz", StudentCondition.Sick, 1990, 2.0));
        itClass.addStudent(new Student("Jan", "Klakier", StudentCondition.Sick, 1990, 1.0));
        itClass.addStudent(new Student("Jan", "Klala", StudentCondition.Absent, 1990, 0.0));
        itClass.addStudent(new Student("Jan", "Lakomy", StudentCondition.Refurb, 1990, 2.0));
        itClass.addStudent(new Student("Jan", "Zielony", StudentCondition.Sick, 1990, 23.0));
        // na tych studentów miejsca NIE MA:
        itClass.addStudent(new Student("Jan", "Czerwony", StudentCondition.Sick, 1990, 26.0));
        itClass.addStudent(new Student("Jan", "Pechowiec", StudentCondition.Sick, 1990, 26.0));

        var foundStudents = itClass.searchPartial("la");

        System.out.println("-------------");
        System.out.println("Found students: ");
        for(var student : foundStudents) {
            student.print();
        }
        System.out.println("-------------");
        System.out.println("Refurb students count: " + itClass.countByCondition(StudentCondition.Refurb));
        System.out.println("Sick students count: " + itClass.countByCondition(StudentCondition.Sick));
        System.out.println("Absent students count: " + itClass.countByCondition(StudentCondition.Absent));

        System.out.println("-------------");
        System.out.println("Sorted students by names:");
        var sortedByNames = itClass.sortByNames();
        for(var student : sortedByNames) {
            student.print();
        }

        System.out.println("-------------");
        System.out.println("Sorted students by points:");
        var sortedByPoints = itClass.sortByPoints();
        for(var student : sortedByPoints) {
            student.print();
        }

        Student bestStudent = itClass.max();
        System.out.println("-------------");
        System.out.println("Best student:");
        if(bestStudent != null) {
            bestStudent.print();
        }

        container.addClass("Metalurgia", 0.5);
        container.addClass("Ceglarka", 0.95);
        container.addClass("Bałamustwo", 1.0);
        container.addClass("Hazardzistwo", 1.1);
        container.addClass("Hazardzistwo", -1);

        var emptyGroups = container.findEmpty();
        System.out.println("-------------");
        System.out.println("Empty classes:");
        for(var group : emptyGroups) {
            System.out.println("Class: " + group.getGroupName());
        }

        System.out.println("-------------");
        System.out.println("Let's kick some student:");
        itClass.getStudent(itClass.search("Klala"));

        container.summary();
    }
}
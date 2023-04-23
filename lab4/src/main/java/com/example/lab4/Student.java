package org.example;


public class Student implements Comparable<String> {
    String name;
    String surname;
    String email;
    StudentCondition condition;
    int birthDate;
    double points;
    private int id;
    static int indexer = 0;

    public int getId() {
        return id;
    }

    public Student(String name, String surname, StudentCondition condition, int birthDate, double points) {
        this.name = name;
        this.surname = surname;
        this.condition = condition;
        this.birthDate = birthDate;
        this.points = points;
        this.id = indexer;

        this.email = (name.substring(0, 1) + surname).toLowerCase() + "@student.agh.edu.pl";
        indexer++;
    }

    public void print() {
        System.out.println("Student info:");
        System.out.println("Imię: " + name);
        System.out.println("Nazwisko: " + surname);
        System.out.println("Rok urodzenia: " + birthDate);
        System.out.println("Stan: " + condition.toString());
        System.out.format("Punkty: %.2f\n", points);
        System.out.println("E-Mail: " + email);
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public StudentCondition getCondition() {
        return condition;
    }

    public int getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(int birthDate) {
        if(birthDate < 1900) {
            return;
        }
        this.birthDate = birthDate;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public void addPoints(double points) {
        if(points <= 0) {
            return;
        }
        this.points += points;
    }

    public void removePoints(double points) {
        if(points >= 0) {
            return;
        }
        this.points -= points;
        if(this.points < 0) {
            this.points = 0.0;
        }
    }

    @Override
    public int compareTo(String other) {
        return surname.compareTo(other);
    }

    public void setCondition(StudentCondition condition) {
        this.condition = condition;
    }
}

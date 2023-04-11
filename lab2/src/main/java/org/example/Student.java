package org.example;

import java.util.Comparator;

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
        indexer++;
    }

    public void print() {
        System.out.println("Student info:");
        System.out.println("Imię: " + name);
        System.out.println("Nazwisko: " + surname);
        System.out.println("Rok urodzenia:" + birthDate);
        System.out.println("Stan:" + condition.toString());
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
        this.birthDate = birthDate;
    }

    public double getPoints() {
        return points;
    }

    public void setPoints(double points) {
        this.points = points;
    }

    public void addPoints(double points) {
        this.points += points;
    }

    public void removePoints(double points) {
        this.points += points;
    }

    @Override
    public int compareTo(String other) {
        return surname.compareTo(other);
    }

    public void setCondition(StudentCondition condition) {
        this.condition = condition;
    }
}

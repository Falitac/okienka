package org.example;

import java.util.*;

public class Class {
    private String groupName;
    ArrayList<Student> studentList;
    static final public int maxCount = 64;
    private double percentage = 1.0;

    public Class(String groupName, double percentage) throws Exception {
        this.groupName = groupName;
        this.studentList = new ArrayList<>();
        if(percentage <= 0.0 || percentage > 1.0) {
            throw new Exception("Invalid capacity");
        }
        this.percentage = percentage;
    }

    public boolean addStudent(Student student) {
        if(studentList.size() >= (int)(maxCount * percentage)) {
            System.err.println("Limit studentow przekroczony");
            return false;
        }
        if(studentList.contains(student)) {
            System.out.println("Juz istnieje taki student");
            return false;
        }
        studentList.add(student);
        return true;
    }
    public boolean addPoints(Student student, double points) {
        int index = studentList.indexOf(student);
        if(index == -1) {
            return  false;
        }
        studentList.get(index).addPoints(points);
        return true;
    }
    public Student getStudent(Student student) {
        if(student.getPoints() <= 0.0) {
            System.out.println("Student dismiss");
            studentList.remove(student);
        }
        return student;
    }
    public boolean changeCondition(Student student, StudentCondition condition) {
        int index = studentList.indexOf(student);
        if(index == -1) {
            return  false;
        }
        studentList.get(index).setCondition(condition);
        return true;
    }
    public boolean removePoints(Student student, double points) {
        int index = studentList.indexOf(student);
        if(index == -1) {
            return  false;
        }
        studentList.get(index).removePoints(points);
        return true;
    }
    public Student search(String surname) {
        for(Student student : studentList) {
            if(student.compareTo(surname) == 0) {
                return student;
            }
        }
        return null;
    }
    public ArrayList<Student> searchPartial(String phrase) {
        ArrayList<Student> result = new ArrayList<Student>();
        for(Student student : studentList) {
            if(student.getSurname().toLowerCase().contains(phrase.toLowerCase())) {
                result.add(student);
            }
        }
        return result;
    }

    public int countByCondition(StudentCondition condition) {
        int result = 0;
        for(Student student : studentList) {
            if(student.getCondition() == condition) {
                result++;
            }
        }
        return result;
    }

    public void summary() {
        for(Student student : studentList) {
            student.print();
        }
    }

    public ArrayList<Student> sortByNames() {
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student student, Student t1) {
                return student.compareTo(t1.getSurname());
            }
        });
        return studentList;
    }
    public ArrayList<Student> sortByPoints() {
        Collections.sort(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student student, Student t1) {
                return Double.compare(student.getPoints(), t1.getPoints());
            }
        });
        return studentList;
    }
    public Student max() {
        return Collections.max(studentList, new Comparator<Student>() {
            @Override
            public int compare(Student student, Student t1) {
                return Double.compare(student.getPoints(), t1.getPoints());
            }
        });
    }

    public int getStudentCount() {
        return studentList.size();
    }

    public double getPercentage() {
        return percentage;
    }
    public double getRealPercentage() { return (double) getStudentCount() / (double)Class.maxCount / percentage; }
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void removeById(int id) {
        studentList.remove(id);
    }
}

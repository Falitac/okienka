package com.example.lab4;

import java.util.List;

public class CSVFormat {

    public static String asString(List<Student> students) {
        StringBuilder result = new StringBuilder();
        result.append("ID;Name;Surname;Condition;BirthDate;Points\n");
        for(Student student : students) {
            result.append(student.getId()); result.append(';');
            result.append(student.getName()); result.append(';');
            result.append(student.getSurname()); result.append(';');
            result.append(student.getCondition()); result.append(';');
            result.append(student.getBirthDate()); result.append(';');
            result.append(student.getPoints()); result.append(';');
            result.append('\n');
        }
        return result.toString();
    }
    public static String asString(ClassContainer groups) {
        return "";
    }
}

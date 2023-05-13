package com.example.lab4;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class AppController {
    ClassContainer groups = new ClassContainer();

    @GetMapping("/")
    public String index() {
        return "Pozdrowienia dla WIMIPU";
    }

    @PostMapping("/clear")
    public String clear() {
        groups = new ClassContainer();
        Student.resetIndexing();
        return "Successfully cleared";
    }

    // 1)
    @PostMapping("/api/student")
    public String addStudent(
            @RequestParam String courseName,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam StudentCondition condition,
            @RequestParam int birthDate,
            @RequestParam double points
    ) {
        if (!groups.exists(courseName)) {
            throw new ResponseStatusException(
                    HttpStatus.GONE, "Class not found"
            );
        }
        var group = groups.getClass(courseName);
        var student = new Student(name, surname, condition, birthDate, points);
        if(!group.addStudent(student)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Student already exists"
            );
        }

        return "Successfully added new student";
    }

    // 2)
    @DeleteMapping ("/api/student/{id}")
    public String removeStudent(
            @PathVariable int id,
            @RequestParam String courseName
    ) {
        if (!groups.exists(courseName)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Class not found"
            );
        }
        var group = groups.getClass(courseName);
        if(group.removeById(id)) {
            return "Successfully removed student";
        }
        throw new ResponseStatusException(
                HttpStatus.GONE, "Student does not exist"
        );
    }

    // 3)
    @GetMapping("/api/student/{id}/grade")
    public String getGrade(
            @PathVariable int id,
            @RequestParam String courseName
    ) {
        if (!groups.exists(courseName)) {
            throw new ResponseStatusException(
                    HttpStatus.GONE, "Class not found"
            );
        }
        var group = groups.getClass(courseName);
        var student = group.getById(id);
        if(student == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Student not found"
            );
        }

        return String.valueOf(student.getPoints());
    }

    // 4)
    @GetMapping(value="/api/student/csv", produces = "text/csv")
    public void getCSV(
            HttpServletResponse response
    ) {
        List<Student> allStudents = new ArrayList<>();
        for(var group : groups.groups.values()) {
            allStudents.addAll(group.studentList);
        }

        response.setContentType("text/csv");

        try {
            response.getWriter().print(CSVFormat.asString(allStudents));
        } catch (IOException e) {
            throw new ResponseStatusException(
                    HttpStatus.I_AM_A_TEAPOT, "Something terrible happened"
            );
        }
    }

    // 5)
    @GetMapping("/api/course")
    public String getCourse(
    ) {
        StringBuilder result = new StringBuilder();
        for(var group : groups.groups.keySet()) {
            result.append(group).append("; ");
        }
        return result.toString();
    }

    // 6)
    @PostMapping("/api/course")
    public String addCourse(
            @RequestParam String courseName,
            @RequestParam double capacity
    ) {
        if (groups.exists(courseName)) {
            return "Already exists";
        }
        try {
            groups.addClass(courseName, capacity);
        } catch (Exception e) {
            return "Invalid parameters";
        }
        return "Successfully added new course";
    }

    // 7)
    @DeleteMapping("/api/course/{id}")
    public String deleteCourse(
            @PathVariable int id
    ) {
        var group = groups.getClassById(id);
        if (group == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Class not found"
            );
        }
        if(groups.removeClass(group.getGroupName())) {
            return "Successfully removed course";
        }

        throw new ResponseStatusException(
                HttpStatus.FORBIDDEN, "DeleteCourse Operation failed"
        );
    }

    // 8)
    @GetMapping("/api/course/{id}/students")
    public String getGroupStudents(
            @PathVariable int id
    ) {
        var group = groups.getClassById(id);
        if (group == null) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Class not found"
            );
        }

        return CSVFormat.asString(group.studentList);
    }


    // 9)
    @GetMapping("/api/course/{id}/fill")
    public String getFill(
            @PathVariable int id
    ) {
        var group = groups.getClassById(id);
        if (group == null) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Class not found"
            );
        }

        return String.valueOf(group.getPercentage());
    }

    // 10)
    @PostMapping("/api/rating")
    public String getRating(
        @RequestParam int id,
        @RequestParam double rating
    ) {
        var group = groups.getClassById(id);
        if (group == null) {
            throw new ResponseStatusException(
                    HttpStatus.GONE, "Class not found"
            );
        }
        if(group.setRating(rating)) {
            return group.getGroupName() + "Rating: " + String.valueOf(group.getRating());
        }

        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Rating can't be negative!"
        );
    }
}
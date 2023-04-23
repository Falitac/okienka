package com.example.lab4;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class AppController {
    ClassContainer groups = new ClassContainer();

    @GetMapping("/")
    public String index() {
        return "Pozdrowienia dla WIMIPU";
    }

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
                    HttpStatus.NOT_FOUND, "Class not found"
            );
        }
        var group = groups.getClass(courseName);
        var student = new Student(name, surname, condition, birthDate, points);
        if(!group.addStudent(student)) {
            return "Already exists";
        }

        return "Success";
    }
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
        group.removeById(id);
        return "Success";
    }
    @GetMapping("/api/student/{id}/grade")
    public String getGrade(
            @PathVariable int id,
            @RequestParam String courseName
    ) {
        if (!groups.exists(courseName)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Class not found"
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
    @GetMapping("/api/student/{id}/csv")
    public String getCSV(
            @PathVariable int id
    ) {
        return "TODO";
    }
    @GetMapping("/api/course")
    public String addCourse(
    ) {
        StringBuilder result = new StringBuilder();
        for(var group : groups.groups.keySet()) {
            result.append(group).append("; ");
        }
        return result.toString();
    }
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
        return "Success";
    }
}
package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassContainer {
    private Map<String, Class> group = new HashMap<>();

    public boolean addClass(String className, double maxCapacity) {
        if(group.containsKey(className)) {
            System.out.println("There is already such a class");
            return false;
        }
        try {
            group.put(className, new Class(className, maxCapacity));
            return true;
        } catch(Exception e) {
            System.out.println("I will not add such a class because: " + e.getMessage());
        }
        return false;
    }

    public boolean removeClass(String className) {
        group.remove(className);
        return true;
    }

    public ArrayList<Class> findEmpty() {
        var result = new ArrayList<Class>();
        for(var classObject : group.values()) {
            if(classObject.getStudentCount() == 0) {
                result.add(classObject);
            }
        }
        return result;
    }
    public void summary() {
        System.out.println("-------------");
        System.out.println("Podsumowanie");
        var i = 0;
        for (var key : group.keySet()) {
            var classObject = group.get(key);
            double percentage = classObject.getPercentage();
            double inPractice = (double) classObject.getStudentCount() / (double)(Class.maxCount * percentage);
            System.out.println("Klasa: " + key);
            System.out.format("Zapełnienie: %.0f%%\n", percentage * 100.0);
            System.out.format("Zapełnienie w praktyce: %.0f%%\n", inPractice * 100.0);
            classObject.summary();
            System.out.println("-------------");
            i++;
        }
        if(i == 0) {
            System.out.println("Pusto");
            System.out.println("-------------");
        }
    }

    public Class getClass(String name) {
        return group.get(name);
    }
}

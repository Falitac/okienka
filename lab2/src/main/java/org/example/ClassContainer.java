package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassContainer {
    private HashMap<String, Class> group;

    public void addClass(String className, double maxCapacity) {
        this.group = new HashMap<>();
        try {
            group.put(className, new Class(className, maxCapacity));
        } catch(Exception e) {
            System.out.println("Sorry, we cannot add this class:" + e.getMessage());
        }

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
        for (var key : group.keySet()) {
            var classObject = group.get(key);
            System.out.println("Klasa: " + key);
            System.out.format("Zapełnienie: %.0f%%\n", classObject.getPercentage() * 100);
            classObject.summary();
            System.out.println("-------------");
        }
    }

    public Class getClass(String name) {
        return group.get(name);
    }
}

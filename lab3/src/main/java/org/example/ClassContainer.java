package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassContainer {
    public Map<String, Class> groups = new HashMap<>();
    public ArrayList<String> indexing = new ArrayList();

    public boolean addClass(String className, double maxCapacity) {
        if(className.equals("")) {
            return false;
        }
        if(groups.containsKey(className)) {
            System.out.println("There is already such a class");
            return false;
        }
        try {
            groups.put(className, new Class(className, maxCapacity));
            indexing.add(className);
            return true;
        } catch(Exception e) {
            System.out.println("I will not add such a class because: " + e.getMessage());
        }
        return false;
    }

    public boolean removeClass(String className) {
        groups.remove(className);
        indexing.remove(className);
        return true;
    }

    public ArrayList<Class> findEmpty() {
        var result = new ArrayList<Class>();
        for(var classObject : groups.values()) {
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
        for (var key : groups.keySet()) {
            var classObject = groups.get(key);
            System.out.println("Klasa: " + key);
            System.out.format("Zapełnienie: %.0f%%\n", classObject.getPercentage() * 100.0);
            System.out.format("Zapełnienie w praktyce: %.0f%%\n", classObject.getRealPercentage() * 100.0);
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
        return groups.get(name);
    }

    public int getCount() { return groups.size(); }

    public Class getClassById(int id) {
        return getClass(indexing.get(id));
    }
}

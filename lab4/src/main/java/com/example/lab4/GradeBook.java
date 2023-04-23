package com.example.lab4;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

public class GradeBook extends JFrame {
    JPanel container;
    JPanel tables;
    JPanel buttons;
    JTable studentTable;
    JTable classTable;
    StudentTable selectedClass;
    private JFormattedTextField searcher;

    public void run() throws Exception {
        ClassContainer classContainer = new ClassContainer();
        classContainer.addClass("Informatyka", 0.5);
        classContainer.addClass("Metalurgia", 0.25);
        classContainer.addClass("Bidaznastwo", 0.25);
        classContainer.addClass("Bidaznastwo", 0.25);

        classContainer.getClass("Informatyka").addStudent(new Student("Jan", "Klacz", StudentCondition.Sick, 2000, 20.0));
        classContainer.getClass("Informatyka").addStudent(new Student("Andrzej", "Kowalski", StudentCondition.Absent, 2000, 20.0));
        classContainer.getClass("Informatyka").addStudent(new Student("Jan", "Arman", StudentCondition.Refurb, 2000, 20.0));
        classContainer.getClass("Metalurgia").addStudent(new Student("Grzegorz", "Klacz", StudentCondition.Sick, 1990, 2.0));
        classContainer.getClass("Informatyka").addStudent(new Student("Jan", "Klakier", StudentCondition.Sick, 1990, 1.0));
        classContainer.getClass("Metalurgia").addStudent(new Student("Bartek", "Klala", StudentCondition.Absent, 1990, 0.0));
        classContainer.getClass("Informatyka").addStudent(new Student("Jan", "Lakomy", StudentCondition.Refurb, 1990, 2.0));
        classContainer.getClass("Metalurgia").addStudent(new Student("Wacław", "Zielony", StudentCondition.Sick, 1990, 23.0));
        classContainer.getClass("Informatyka").addStudent(new Student("Tomasz", "Czerwony", StudentCondition.Sick, 1990, 26.0));
        classContainer.getClass("Informatyka").addStudent(new Student("Paweł", "Pechowiec", StudentCondition.Sick, 1990, 26.0));

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Dziennik");
        this.setSize(800, 900);
        this.setBounds(0, 0, 800, 900);

        container = new JPanel();
        container.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;

        classTable = new JTable(new ClassTable(classContainer));
        classTable.setLayout(new GridBagLayout());
        classTable.setBounds(0, 0, 800, 900);
        classTable.setSize(800, 900);

        selectedClass = new StudentTable(null);
        studentTable = new JTable(selectedClass);
        studentTable.setLayout(new GridLayout(0, 1));
        studentTable.setBounds(800, 0, 800, 900);
        studentTable.setSize(800, 900);


        JScrollPane studentListSP = new JScrollPane(studentTable);
        JScrollPane classListSP = new JScrollPane(classTable);

        constraints.gridx = 0;
        container.add(studentListSP, constraints);
        constraints.gridx = 1;
        container.add(classListSP, constraints);

        classTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) { }
            @Override
            public void mousePressed(MouseEvent e) {
                var id = classTable.getSelectedRow();
                var group = classContainer.getClassById(id);
                studentTable.setModel(new StudentTable(group.searchPartial("")));

                var column = studentTable.getColumnModel().getColumn(2);
                var combo = new JComboBox();
                for(var i = 0; i < StudentCondition.values().length; i++) {
                    combo.addItem(StudentCondition.values()[i]);
                }
                column.setCellEditor(new DefaultCellEditor(combo));
            }

            @Override
            public void mouseReleased(MouseEvent e) { }

            @Override
            public void mouseEntered(MouseEvent e) { }

            @Override
            public void mouseExited(MouseEvent e) { }
        });

        buttons = new JPanel();
        constraints.fill = GridBagConstraints.BASELINE;
        constraints.gridx = 0;
        constraints.gridy = 1;
        container.add(buttons, constraints);


        var buttonRemove = new JButton("Usuń");
        buttonRemove.setSize(80, 30);
        buttonRemove.addActionListener(e -> {
            var classId = classTable.getSelectedRow();
            var group = classContainer.getClassById(classId);
            if(group == null) {
                return;
            }
            var studentId = studentTable.getSelectedRow();
            System.out.println("ID " + studentId);
            group.removeById(studentId);

            studentTable.setModel(new StudentTable(group.searchPartial(searcher.getText())));
            studentTable.updateUI();
            studentTable.repaint();
            studentTable.updateUI();
        });
        buttons.add(buttonRemove);

        var buttonSortByName = new JButton("Posortuj");
        buttonSortByName.setSize(80, 30);
        buttonSortByName.addActionListener(e -> {
            var classId = classTable.getSelectedRow();
            var group = classContainer.getClassById(classId);
            group.sortByNames();
            studentTable.updateUI();
        });
        buttons.add(buttonSortByName);

        var buttonSortByPoints = new JButton("Posortuj po punktach");
        buttonSortByPoints.setSize(80, 30);
        buttonSortByPoints.addActionListener(e -> {
            var classId = classTable.getSelectedRow();
            var group = classContainer.getClassById(classId);
            group.sortByPoints();
            studentTable.updateUI();
        });
        buttons.add(buttonSortByPoints);

        searcher = new JFormattedTextField();
        searcher.setColumns(16);
        buttons.add(new JLabel("Wyszukaj studenta: "));
        buttons.add(searcher);
        searcher.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }
            private void changed() {
                var id = classTable.getSelectedRow();
                var group = classContainer.getClassById(id);
                if(group == null) {
                    return;
                }
                studentTable.setModel(new StudentTable(group.searchPartial(searcher.getText())));
                studentTable.updateUI();
            }
        });

        this.add(container);
        this.pack();
        this.setVisible(true);
    }
}

class StudentTable extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Imię", "Nazwisko", "Stan", "Rok urodzenia", "Punkty", "E-Mail"};

    ArrayList<Student> students;

    public StudentTable(ArrayList<Student> students) {
        this.students = students;
    }

    public void setStudentClass(ArrayList<Student> students) {
        this.students = students;
    }


    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public int getRowCount() {
        if(students == null) return 1;
        return students.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public boolean isCellEditable(int rows, int cols) {
        return true;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(students == null) return null;
        if(rowIndex == students.size()) {
            if(columnIndex == 0) {
                return "Dodaj";
            }
            return "";
        }
        Student student = students.get(rowIndex);
        if(student == null) {
            return null;
        }
        switch (columnIndex) {
            case 0: return student.getName();
            case 1: return student.getSurname();
            case 2: return student.getCondition();
            case 3: return student.getBirthDate();
            case 4: return student.getPoints();
            case 5: return student.getEmail();
        }
        return null;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if(rowIndex == students.size()) {
            System.out.println("I am here");
//            studentClass.addStudent(new Student("<Imie>", "<Nazwisko>", StudentCondition.Absent, 1900, 20.0));
            return;
        }
        var student = students.get(rowIndex);
        if(student == null) {
            return;
        }
        switch (columnIndex) {
            case 0: student.setName(aValue.toString()); break;
            case 1: student.setSurname(aValue.toString()); break;
            case 2:
                var condition = StudentCondition.fromString(aValue.toString());
                student.setCondition(condition);
                break;
            case 3:
                student.setBirthDate(Integer.parseInt(aValue.toString()));
                break;
            case 4:
                student.setPoints(Double.parseDouble(aValue.toString()));
                break;
            case 5: student.setEmail(aValue.toString()); break;
        }
        fireTableDataChanged();
        fireTableRowsUpdated(rowIndex, columnIndex);
    }

}



class ClassTable extends AbstractTableModel {
    private static final String[] COLUMN_NAMES = {"Klasa", "Zapełnienie", "Zapełnienie w praktyce"};

    public ClassTable(ClassContainer groups) {
        this.classContainer = groups;
    }

    public void setClassContainer(ClassContainer classContainer) {
        this.classContainer = classContainer;
    }

    ClassContainer classContainer;

    @Override
    public String getColumnName(int col) {
        return COLUMN_NAMES[col];
    }

    @Override
    public int getRowCount() {
        return classContainer.getCount();
    }

    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(classContainer == null) {
            return null;
        }
        var group = classContainer.getClassById(rowIndex);
        if(group == null) {
            return null;
        }
        switch (columnIndex) {
            case 0: return group.getGroupName();
            case 1: return group.getPercentage();
            case 2: return group.getRealPercentage();
        }
        return null;
    }
}
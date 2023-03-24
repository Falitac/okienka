import java.util.*;
import java.lang.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;

class Calc {
    public enum State {
        Exit,
        Triangle,
        Circle,
        Square,
        Prism,
    }
    public static void main (String[] args) throws java.lang.Exception {
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 600, 900));
        panel.setLayout(new GridLayout(0, 1));

        JTextField field = new JTextField(10);
        field.addActionListener((event) -> {
            System.out.println("ehh");
        });
        panel.add(field, BorderLayout.CENTER);

        frame.add(panel, BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("App");
        frame.pack();
        frame.setVisible(true);

        ArrayList<Figure> figures = new ArrayList<Figure>();
        figures.add(new Circle(4.0));
        figures.add(new Square(4.0));
        figures.add(new Triangle(3, 4, 5));
        figures.add(new Triangle(3, 4, 5));

        for(int i = 0; i < figures.size(); i++) {
            System.out.println(figures.get(i).calculatePerimeter());
            System.out.println(figures.get(i).calculateArea());

            Printable print = (Printable)figures.get(i);
            print.print();
        }

        // for(;;) {
        //     State state = processInput();
        //     // processData(state)
        //     switch(state) {
        //         case Exit:
        //             System.out.println("Bye!");
        //             return;
        //         case Triangle:
        //             System.out.println("Triangle");
        //     }
        // }
    }

    private static State processInput() {
        clearScreen();
        System.out.println("Select Figure:");
        Scanner scanner = new Scanner(System.in);
        int choice = scanner.nextInt();

        switch(choice) {
            case 0: return State.Triangle;
            case 1: return State.Circle;
            case 2: return State.Square;
            case 3: return State.Prism;
        }
        return State.Exit;
    }

    public static void clearScreen() {  
        System.out.print("\033[H\033[2J");  
        System.out.flush();  
    } 
}

class GUI extends JFrame {

}

interface Printable {
    public void print();
}

abstract class Figure {
    abstract public double calculateArea();
    abstract public double calculatePerimeter();
}

class Triangle extends Figure implements Printable {
    public Triangle(double a, double b, double c) throws Exception {
        if(a <= 0 || b < 0 || c <= 0) {
            throw new Exception("Dimensions are dumb");
        }
        this.a = a;
        this.b = b;
        this.c = c;
    }
    public double calculateArea() {
        double p = calculatePerimeter() * 0.5;
        return Math.sqrt(p*(p-a)*(p-b)*(p-c));
    }

    public double calculatePerimeter() {
        return a + b + c;
    }

    public void print() {
        System.out.println("/\\, a:" + a + ", b:" + b + ", c:" + c);
        System.out.println("Area: " + calculateArea());
        System.out.println("Perimeter: " + calculatePerimeter());
    }

    private double a;
    private double b;
    private double c;
}

class Square extends Figure implements Printable {
    public Square(double a) throws Exception {
        if(a <= 0) {
            throw new Exception("Dimensions are dumb");
        }
        this.a = a;
    }

    public double calculateArea() {
        return a*a;
    }

    public double calculatePerimeter() {
        return 4*a;
    }

    public void print() {
        System.out.println("[], a: " + a);
        System.out.println("Area: " + calculateArea());
        System.out.println("Perimeter: " + calculatePerimeter());
    }
    private double a;
}

class Circle extends Figure implements Printable {
    public Circle(double radius) throws Exception {
        if(radius <= 0) {
            throw new Exception("Dimensions are dumb");
        }
        this.r = radius;
    }
    public double calculateArea() {
        return r * r * Math.PI;
    }

    public double calculatePerimeter() {
        return 2 * r * Math.PI;
    }

    public void print() {
        System.out.println("O, radius: " + r);
        System.out.println("Area: " + calculateArea());
        System.out.println("Perimeter: " + calculatePerimeter());
    }
    private double r;
}

class Prism extends Figure implements Printable {
    public Prism(double a, double height, int n) throws Exception {
        if(a < 0 || height < 0.0 || n < 0) {
            throw new Exception("Jiotaro");
        }
        this.a = a;
        this.height = height;
        this.n = n;
    } 

    public double calculateArea() {
        return n * (height * a + Math.cos(Math.PI / (double)n) * a * a);
    }

    public double calculatePerimeter() {
        return n * (height + 2 * a);
    }

    public double calculateVolume() {
        return height * Math.cos(Math.PI / (double)n) * a * a * 0.5 * n;
    }

    public void print() {
        System.out.println("3d[], a: " + a + ", height:" + height + ", n:" + n);
        System.out.println("Area: " + calculateArea());
        System.out.println("Perimeter: " + calculatePerimeter());
    }

    double a;
    double height;
    int n;
}


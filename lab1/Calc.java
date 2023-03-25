import java.util.*;
import java.io.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import javax.xml.crypto.Data;

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

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

        PrismCanvas canvas = new PrismCanvas(5);
        canvas.setBorder(BorderFactory.createEmptyBorder(20, 20, 800, 800));

        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 800, 250));
        panel.setLayout(new GridLayout(0, 1));

        JTextField field = new JTextField(10);
        field.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(field.getText());
            }
        });
        panel.add(new JLabel("Pole:"));
        panel.add(field, BorderLayout.CENTER);

        container.add(panel);
        container.add(canvas);

        frame.add(container);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Kalkulator, nie dla biedaków");
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

class PrismCanvas extends JPanel {
    public PrismCanvas(int n) {
        Timer timer = new Timer(16, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                repaint();
            }
        });
        timer.start();

        points = new double[3 * (2 * n + 2)];
        indices = new int[(n * 2) * 3 * 2];

        points[3 * (2 * n + 0) + 0] = 0.0;
        points[3 * (2 * n + 0) + 1] = -1.0;
        points[3 * (2 * n + 0) + 2] = 0.0;
        points[3 * (2 * n + 1) + 0] = 0.0;
        points[3 * (2 * n + 1) + 1] = 1.0;
        points[3 * (2 * n + 1) + 2] = 0.0;

        for(int i = 0; i < n; i++) {
            double theta = (double) i * Math.PI * 2.0 / (double)n;
            double radius = 1.0;
            points[3 * i + 0] = radius * Math.cos(theta);
            points[3 * i + 1] = -1.0f;
            points[3 * i + 2] = radius * Math.sin(theta);

            points[3 * (i + n) + 0] = radius * Math.cos(theta);
            points[3 * (i + n) + 1] = 1.0f;
            points[3 * (i + n) + 2] = radius * Math.sin(theta);

            // bottom
            indices[i * 3 + 0] = i;
            indices[i * 3 + 1] = 2 * n;
            indices[i * 3 + 2] = (i + 1) % n;

            // top
            indices[(i + n) * 3 + 0] = (i + n);
            indices[(i + n) * 3 + 1] = 2 * n + 1;
            indices[(i + n) * 3 + 2] = (((i + 1) % n + n));

            // side
            indices[(i + 2 * n) * 3 + 0] = i;
            indices[(i + 2 * n) * 3 + 1] = (i + 1) % n;
            indices[(i + 2 * n) * 3 + 2] = i + n;

            indices[(i + 3 * n) * 3 + 0] = (i + 1) % n;
            indices[(i + 3 * n) * 3 + 1] = ((i + 1) % n + n) ;
            indices[(i + 3 * n) * 3 + 2] = (i + n);
        }
    }

    @Override
    public void paint(Graphics g) {
      Graphics2D g2d = (Graphics2D) g;
      int w = g2d.getClipBounds().width;
      int h = g2d.getClipBounds().height;
      time += 0.01677777;

      g2d.clearRect(0, 0, w, h);

      g2d.setColor(Color.ORANGE);
      dist = (float) (2.2f + Math.cos(time * 5.2) / 2. + 1.0);

      for(int i = 0; i < indices.length / 3; i++) {
        double[][] p = new double[3][2];
        for(int j = 0; j < 3; j++) {
            double[] p0 =  toViewport(
                points[indices[i * 3 + j] * 3 + 0],
                points[indices[i * 3 + j] * 3 + 1],
                points[indices[i * 3 + j] * 3 + 2] + dist
            );
            p0 = toScreen(p0[0], p0[1], w, h);

            g2d.fillArc((int)p0[0], (int)p0[1], 4, 4, 0, 360);
            p[j] = p0;
        }
        g2d.drawLine((int)p[0][0], (int)p[0][1], (int)p[1][0], (int)p[1][1]);
        g2d.drawLine((int)p[1][0], (int)p[1][1], (int)p[2][0], (int)p[2][1]);
        g2d.drawLine((int)p[2][0], (int)p[2][1], (int)p[0][0], (int)p[0][1]);
      }
    }

    private double[] toViewport(double x, double y, double z) {
        double[] result = new double[2];

        result[0] = x / z;
        result[1] = y / z;

        return result;
    }

    private double[] toScreen(double x, double y, int w, int h) {
        double[] screenPoint = new double[2];
        screenPoint[0] = x + 1.0f;
        screenPoint[1] = -y + 1.0f;
        screenPoint[0] *= w * 0.5f;
        screenPoint[1] *= h * 0.5f;
        return screenPoint;
    }

    private double[] points;
    private int[] indices;

    private float dist = 5.0f;
    private double theta = 0.2f;
    private double time = 0.0;
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


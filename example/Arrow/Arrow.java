package Arrow;

import java.io.*;
import javax.swing.*;
import java.awt.*;

public class Arrow extends JComponent implements Serializable {
    public static final int WIDTH = 5, HEIGHT = 20;
    private int ID, x, y, speed = 1;

    // Конструктор стрелы
    public Arrow(int x, int y) {
        this.x = x;
        this.y = y;
        setSize(WIDTH, HEIGHT);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void move() {
        y -= speed; // Смещение стрелы вверх
    }

    // Отрисовка стрелы
    public void draw(Graphics g) {
        int[] xPoints = {x - 5, x + 5, x}; // X-координаты вершин треугольника
        int[] yPoints = {y, y, y - 10}; // Y-координаты вершин треугольника
        g.setColor(Color.BLACK);
        g.drawPolygon(xPoints, yPoints, 3); // Отрисовка контура стрелы
        g.fillPolygon(xPoints, yPoints, 3); // Заполнение стрелы
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}

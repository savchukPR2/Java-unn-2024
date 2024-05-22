package Customer;

import java.awt.*;
import java.io.*;
import Arrow.*;

// Класс игрока, представляющий объект игрока в игре
public class Customer implements Serializable {
    private String name; // Имя игрока
    private int score; // Очки игрока
    private int hits; // Количество попаданий
    private String wins = "0"; // Количество побед
    private int xPosition = 0; // Позиция по оси X
    private int playerSize = 25; // Размер игрока
    private long lastShotTime; // Время последнего выстрела
    private long cooldownMillis; // Время перезарядки выстрела
    private Color color; // Цвет игрока
    private int ID; // Идентификатор игрока
    private int x; // Позиция по оси X для стрелы
    private int y; // Позиция по оси Y для стрелы

    // Конструктор игрока
    public Customer(String name) {
        this.name = name;
        this.score = 0;
        this.hits = 0;
        this.cooldownMillis = 1000;
        this.lastShotTime = 0;
        color = Color.BLACK;
    }

    // Установка цвета игрока
    public void setColor(Color color) {
        this.color = color;
    }

    // Получение цвета игрока
    public Color getColor() {
        return color;
    }

    // Установка идентификатора игрока
    public void setID(int ID) {
        this.ID = ID;
    }

    // Получение позиции X игрока
    public int getXPosition() {
        return xPosition;
    }

    // Установка позиции X игрока
    public void setXPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    // Получение идентификатора игрока
    public int getID() {
        return ID;
    }

    // Получение имени игрока
    public String getName() {
        return name;
    }

    // Получение очков игрока
    public int getScore() {
        return score;
    }

    // Получение количества попаданий игрока
    public int getHits() {
        return hits;
    }

    // Установка количества попаданий игрока
    public void setHits(int hits) {
        this.hits = hits;
    }

    // Увеличение количества попаданий на 1
    public void increaseHits() {
        hits++;
    }

    // Получение размера игрока
    public int getPlayerSize() {
        return playerSize;
    }

    // Увеличение количества очков игрока
    public void increaseScore(int points) {
        score += points;
    }

    // Отрисовка игрока
    public Graphics draw(Graphics g, int width, int height) {
        int[] xPoints = {xPosition + width / 2, width / 2 - playerSize + xPosition, + width / 2 + playerSize + xPosition}; // X-координаты вершин игрока
        int[] yPoints = {0, height, height}; // Y-координаты вершин игрока
        x = xPosition + width / 2;
        y = 0;

        g.setColor(color); // Установка цвета игрока
        g.fillPolygon(xPoints, yPoints, 3); // Отрисовка игрока
        return g;
    }

    // Проверка возможности выстрела
    public boolean canShoot() {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastShotTime) >= cooldownMillis;
    }

    // Выстрел игрока
    public Arrow shoot() {
        if (canShoot()) {
            Arrow arrow = new Arrow(x, y); // Создание стрелы
            lastShotTime = System.currentTimeMillis();
            return arrow;
        }
        return null;
    }

    // Получение количества побед игрока
    public String getWins() {
        return wins;
    }

    // Установка количества побед игрока
    public void setWins(String wins) {
        this.wins = wins;
    }
}

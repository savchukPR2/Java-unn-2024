package Messages;

import Customer.*;

import java.awt.*;
import java.io.Serializable;

// Сообщение для установки цвета игрока
public class SetColorMessage implements Serializable {
    private Customer player; // Игрок
    private Color color; // Цвет

    // Конструктор сообщения для установки цвета игрока
    public SetColorMessage(Customer player, Color color) {
        this.player = player;
        this.color = color;
    }

    // Получение игрока
    public Customer getPlayer() {
        return player;
    }

    // Получение цвета
    public Color getColor() {
        return color;
    }
}

package Messages;

import Customer.*;

import java.io.Serializable;

// Сообщение для установки игрока
public class SetPlayerMessage implements Serializable {
    private Customer player; // Игрок

    // Конструктор сообщения для установки игрока
    public SetPlayerMessage(Customer player) {
        this.player = player;
    }

    // Получение игрока
    public Customer getPlayer() {
        return player;
    }
}

package Messages;

import Customer.*;
import java.io.Serializable;

// Сообщение о подключении игрока
public class ConnectMessage implements Serializable {
    private Customer player;

    // Конструктор сообщения о подключении игрока
    public ConnectMessage(Customer player) {
        this.player = player;
    }

    // Получение информации об игроке
    public Customer getPlayer() {
        return player;
    }
}

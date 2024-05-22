package Messages;

import Customer.*;
import java.io.Serializable;
import java.util.Map;

// Сообщение об обновлении игроков
public class UpdatePlayersMessage implements Serializable {
    private Map<Integer, Customer> players; // Карта игроков

    // Конструктор сообщения об обновлении игроков
    public UpdatePlayersMessage(Map<Integer, Customer> players) {
        this.players = players;
    }

    // Получение карты игроков
    public Map<Integer, Customer> getPlayers() {
        return players;
    }
}

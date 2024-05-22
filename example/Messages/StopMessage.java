package Messages;

import java.io.Serializable;

// Сообщение остановки игры
public class StopMessage implements Serializable {
    private boolean stop; // Флаг остановки игры
    private int playerID; // Идентификатор игрока

    // Конструктор сообщения об остановке игры
    public StopMessage(boolean stop, int playerID) {
        this.stop = stop;
        this.playerID = playerID;
    }

    // Получение флага остановки игры
    public boolean getStop() {
        return stop;
    }

    // Получение идентификатора игрока
    public int getPlayerID() {
        return playerID;
    }
}

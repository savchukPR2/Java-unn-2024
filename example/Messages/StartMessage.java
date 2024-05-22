package Messages;

import java.io.Serializable;

// Сообщение о начале игры
public class StartMessage implements Serializable {
    private boolean start; // Флаг начала игры
    private int playerID; // Идентификатор игрока

    // Конструктор сообщения о начале игры
    public StartMessage(boolean start, int playerID) {
        this.start = start;
        this.playerID = playerID;
    }

    // Получение флага начала игры
    public boolean getStart() {
        return start;
    }

    // Получение идентификатора игрока
    public int getPlayerID() {
        return playerID;
    }
}

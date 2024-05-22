package Messages;

import java.io.Serializable;

// Сообщение о событии игры (старт, пауза, выход)
public class GameEventMessage implements Serializable {
    // Перечисление типов событий игры
    public enum EventType {
        GAME_START, // Начало игры
        GAME_STOP,  // Пауза игры
        GAME_EXIT   // Выход из игры
    }

    private EventType eventType; // Тип события
    private int playerID; // Идентификатор игрока

    // Конструктор сообщения о событии игры
    public GameEventMessage(EventType eventType, int playerID) {
        this.eventType = eventType;
        this.playerID = playerID;
    }

    // Получение типа события игры
    public EventType getEventType() {
        return eventType;
    }

    // Получение идентификатора игрока
    public int getPlayerID() {
        return playerID;
    }
}

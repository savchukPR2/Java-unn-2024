package Messages;

import java.io.Serializable;

// Сообщение о завершении игры
public class EndGameMessage implements Serializable {
    private int ID; // Идентификатор игрока
    private int score; // Очки игрока
    private boolean isEquals; // Флаг равенства

    // Конструктор сообщения о завершении игры
    public EndGameMessage(int ID, int score, boolean isEquals) {
        this.ID = ID;
        this.score = score;
        this.isEquals = isEquals;
    }

    // Получение идентификатора игрока
    public int getID() {
        return ID;
    }

    // Получение количества очков игрока
    public int getScore() {
        return score;
    }

    // Проверка равенства
    public boolean isEquals() {
        return isEquals;
    }
}

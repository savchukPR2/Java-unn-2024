package Messages;

import java.io.Serializable;

// Сообщение о счете игрока
public class ScoreMessage implements Serializable {
    private int ID; // Идентификатор игрока
    private int score; // Счет игрока

    // Конструктор сообщения о счете игрока
    public ScoreMessage(int ID, int score) {
        this.ID = ID;
        this.score = score;
    }

    // Получение счета игрока
    public int getScore() {
        return score;
    }

    // Получение идентификатора игрока
    public int getID() {
        return ID;
    }
}

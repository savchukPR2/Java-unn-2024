package Messages;

import java.io.Serializable;

// Сообщение о выходе игрока
public class ExitMessage implements Serializable {
    private int ID; // Идентификатор игрока

    // Конструктор сообщения о выходе игрока
    public ExitMessage(int ID) {
        this.ID = ID;
    }

    // Получение идентификатора игрока
    public int getID() {
        return ID;
    }
}

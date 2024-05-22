package Messages;

import java.io.Serializable;
import Arrow.*;

// Сообщение о выстреле
public class ShootMessage implements Serializable {
    private int ID; // Идентификатор игрока
    private Arrow arrow; // Стрела

    // Конструктор сообщения о выстреле
    public ShootMessage(int ID, Arrow arrow) {
        this.ID = ID;
        this.arrow = arrow;
    }

    // Получение стрелы
    public Arrow getArrow() {
        return arrow;
    }

    // Получение идентификатора игрока
    public int getID() {
        return ID;
    }
}

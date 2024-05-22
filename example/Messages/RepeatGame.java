package Messages;

import java.io.Serializable;

// Сообщение о повторе игры
public class RepeatGame implements Serializable {
    private int id; // Идентификатор игрока
    private boolean repeat; // Флаг повтора игры

    // Конструктор сообщения о повторе игры
    public RepeatGame(int id, boolean repeat){
        this.id = id;
        this.repeat = repeat;
    }

    // Получение значения флага повтора игры
    public boolean isRepeat() {
        return repeat;
    }

    // Получение идентификатора игрока
    public int getId() {
        return id;
    }
}

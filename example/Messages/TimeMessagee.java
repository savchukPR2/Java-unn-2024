package Messages;

import java.io.Serializable;

// Сообщение о времени
public class TimeMessagee implements Serializable {
    private int time; // Время
    private boolean choser; // Флаг выбора

    // Конструктор сообщения о времени
    public TimeMessagee(int time, boolean choser) {
        this.time = time;
        this.choser = choser;
    }

    // Получение времени
    public int getTime() {
        return time;
    }

    // Проверка флага выбора
    public boolean isChoser() {
        return choser;
    }
}

package Messages;

import java.io.Serializable;
import java.util.Map;

// Сообщение о движении кругов
public class CircleMoveMessage implements Serializable {
    private Map<String, Integer> coordinateTarget;

    // Конструктор сообщения о движении кругов
    public CircleMoveMessage(Map<String, Integer> coordinateTarget) {
        this.coordinateTarget = coordinateTarget;
    }

    // Получение координат целей
    public Map<String, Integer> getCoordinateTarget() {
        return coordinateTarget;
    }
}

package Interface;

import java.awt.*;

// Интерфейс Windows определяет размеры окон и элементов интерфейса
public interface Windows {
    // Размеры окон
    int SIZE_SMALL_WINDOW_X = 500;
    int SIZE_SMALL_WINDOW_Y = 300;
    int SIZE_WINDOW_X = 1280;
    int SIZE_WINDOW_Y = 720;
    // Размеры панелей
    int SIZE_PANEL_X = SIZE_WINDOW_X;
    int SIZE_PANEL_Y = 70;
    // Коэффициенты для размеров кнопок
    double BUTTON_WIDTH_RATIO = 0.3;
    double BUTTON_HEIGHT_RATIO = 0.1;
    double SMALL_BUTTON = 0.5;

    // Метод для получения размеров кнопок
    default Dimension getButtonPreferredSize() {
        int buttonWidth = (int) (SIZE_WINDOW_X * BUTTON_WIDTH_RATIO);
        int buttonHeight = (int) (SIZE_WINDOW_Y * BUTTON_HEIGHT_RATIO);
        return new Dimension(buttonWidth, buttonHeight);
    }

    // Метод для получения размеров маленьких кнопок
    default Dimension getButtonPreferredSizeSmall() {
        int buttonWidth = (int) (SIZE_WINDOW_X * BUTTON_WIDTH_RATIO * SMALL_BUTTON);
        int buttonHeight = (int) (SIZE_WINDOW_Y * BUTTON_HEIGHT_RATIO * SMALL_BUTTON);
        return new Dimension(buttonWidth, buttonHeight);
    }

    // Метод для получения размеров окна
    default Dimension getWindowPreferredSize() {
        return new Dimension(SIZE_WINDOW_X, SIZE_WINDOW_Y);
    }
}

package PlayerArea;

import Interface.*;
import Customer.*;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class PlayerArea extends JPanel implements Windows {
    private Map<Integer, Customer> players;

    // Конструктор класса
    public PlayerArea() {
        setPreferredSize(new Dimension(Windows.SIZE_PANEL_X, Windows.SIZE_PANEL_Y));
        setBackground(Color.YELLOW);
    }

    // Метод для установки списка игроков
    public void setPlayers(Map<Integer, Customer> players){
        this.players = players;
    }

    // Переопределение метода отрисовки компонента
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Отрисовка каждого игрока в списке
        for(Customer player : players.values()){
            player.draw(g, getWidth(), getHeight());
        }
    }
}

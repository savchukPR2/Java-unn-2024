package CenterArea;

import Interface.*;
import PlayerArea.*;
import PlayerArea.PlayerArea;
import TargetArea.*;
import Arrow.*;
import Customer.*;
import Client.*;
import TargetArea.TargetArea;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public class CenterArea extends JPanel implements Windows {
    private PlayerArea PlayerArea;
    private TargetArea roundPanel;
    private Map<Integer, Customer> players;
    private List<Arrow> arrows;
    private boolean start = false;
    private Client client;

    // Конструктор класса
    public CenterArea(Map<Integer, Customer> players) {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        this.players = players;
        setFocusable(true);

        // Создание панели игроков и добавление на юг (нижняя часть) панели центра
        PlayerArea = new PlayerArea();
        add(PlayerArea, BorderLayout.SOUTH);
        PlayerArea.setPlayers(players);

        // Создание панели целей и добавление на север (верхняя часть) панели центра
        roundPanel = new TargetArea();
        add(roundPanel, BorderLayout.NORTH);
        // Инициализация списка стрел
        arrows = new ArrayList<>();
    }

    // Установка целей для раунда
    public void setTargets(Map<String, Integer> rounds) {
        roundPanel.setTargets(rounds);
    }

    // Обновление панели раунда
    public void updateRounds() {
        roundPanel.updateArrows();
    }

    // Добавление стрелы на панель центра
    public void addArrow(Arrow arrow) {
        arrow.setY(getHeight() - client.getPlayer().getPlayerSize());
        arrows.add(arrow);
        start = true;
    }

    // Обновление положения стрел на панели центра
    public void updateArrows() {
        Iterator<Arrow> iterator = arrows.iterator();
        while (iterator.hasNext()) {
            Arrow arrow = iterator.next();
            arrow.move();
            // Если стрела достигла верхней границы или границы панели раунда, добавить на панель раунда и удалить из списка стрел
            if (arrow.getY() <= 0 || arrow.getY() <= Windows.SIZE_PANEL_Y * 2) {
                roundPanel.addArrow(arrow);
                iterator.remove();
            }
        }
    }

    // Установка клиента
    public void setClient(Client client){
        this.client = client;
        roundPanel.setClient(client);
    }

    // Переопределение метода отрисовки компонента
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Если игра началась, отрисовать стрелы
        if(start) {
            for (Arrow arrow : arrows) {
                arrow.draw(g);
            }
        }
    }
}

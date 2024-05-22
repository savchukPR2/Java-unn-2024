package Tracking;

import Client.*;
import Interface.*;
import Customer.*;
import Logs.*;
import Game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class Tracking extends JPanel implements Windows {
    private Client Client;
    private Map<Integer, Customer> Players;
    private JPanel Logs;
    private Map<Customer, Logs> Сomparison = new HashMap<>();
    private JButton Start;
    private JButton Pause;
    private JButton Exit;
    private JLabel Timer;

    // Конструктор класса
    public Tracking() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(Windows.SIZE_PANEL_Y, Windows.SIZE_WINDOW_Y - 720));
        setBackground(Color.LIGHT_GRAY);

        // Панель для информации об игроках
        Logs = new JPanel();
        Logs.setLayout(new GridLayout(1, 0, 10, 2));

        // Панель для кнопок управления
        JPanel buttonInfoPanel = new JPanel();
        buttonInfoPanel.setLayout(new GridLayout(3, 3, 10, 2));
        addButtonControl(buttonInfoPanel);

        // Панель отображения
        JPanel infoButtonPanel = new JPanel();
        infoButtonPanel.setLayout(new BorderLayout());
        infoButtonPanel.add(Logs, BorderLayout.CENTER);
        infoButtonPanel.add(buttonInfoPanel, BorderLayout.EAST);

        JPanel instructionPanel = new JPanel();
        instructionPanel.setLayout(new FlowLayout());
        Timer = new JLabel("<html>Оставшееся время: </html>");
        instructionPanel.add(Timer);

        add(infoButtonPanel);
        add(instructionPanel);
    }

    // Метод для добавления информации об игроке на панель
    public void addPlayerInfo(Customer player) {
        Logs playerInfo = new Logs(player);
        Сomparison.put(player, playerInfo);
        Logs.add(playerInfo);
    }

    // Метод для обновления информации об игроке
    public void updateInfo(Customer player) {
        Сomparison.get(player).getHitsLabel().setText("Количество выстрелов: " + player.getHits());
        Сomparison.get(player).getScoreLabel().setText("Счет игрока: " + player.getScore());
    }

    // Установка клиента
    public void setClient(Client Client) {
        this.Client = Client;
    }

    // Установка списка игроков
    public void setPlayers(Map<Integer, Customer> Players) {
        this.Players = Players;
    }

    // Отключение кнопки "Начать игру"
    public void disableStart() {
        Start.setEnabled(false);
    }

    // Отключение кнопки "Пауза"
    public void disablePause() {
        Pause.setEnabled(false);
    }

    // Включение кнопки "Начать игру"
    public void enableStart() {
        Start.setEnabled(true);
    }

    // Включение кнопки "Пауза"
    public void enablePause() {
        Pause.setEnabled(true);
    }

    // Установка времени на таймере
    public void setTimer(int time) {
        if (time >= 60) {
            int minutes = time / 60;
            int remainingSeconds = time % 60;
            Timer.setText("Время игры: " + minutes + " мин " + remainingSeconds + " сек");
        } else {
            Timer.setText("Время игры: " + time + " сек");
        }
    }

    // Добавление кнопок управления на панель
    private void addButtonControl(JPanel buttonInfoPanel) {
        Start = new JButton("Начать игру");
        Pause = new JButton("Пауза");
        Exit = new JButton("Выход в меню");
        Start.setFocusable(false);
        Pause.setFocusable(false);
        Exit.setFocusable(false);

        Start.setPreferredSize(getButtonPreferredSizeSmall());

        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.startGame();
            }
        });

        Pause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.stopGame();
            }
        });

        Exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Client.exitGame();
            }
        });

        buttonInfoPanel.add(Start);
        buttonInfoPanel.add(Pause);
        buttonInfoPanel.add(Exit);
    }
}

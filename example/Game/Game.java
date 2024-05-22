package Game;

import Arrow.*;
import CenterArea.*;
import Tracking.*;
import Customer.*;
import Logs.*;
import Interface.*;
import Client.*;
import Tracking.Tracking;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Game extends JFrame implements Windows {
    private Map<Integer, Customer> players = new HashMap<>();
    private Tracking infoPanel;
    private CenterArea gamefieldPanel;
    private Client client;
    private Thread gameThread;

    // Конструктор класса Game
    public Game() {
        super("Меткий стрелок");
        setSize(Windows.SIZE_WINDOW_X - 300, Windows.SIZE_WINDOW_Y); // Установка размеров окна
        setLocationRelativeTo(null); // Позиционирование окна по центру экрана
    }

    // Метод устанавливает список игроков
    public void setPlayers(Map<Integer, Customer> players) {
        this.players = players;
    }

    // Метод устанавливает клиент
    public void setClient(Client client) {
        this.client = client;
    }

    // Метод запускает игру
    public void startGame() {
        infoPanel.disableStart(); // Отключение кнопки начала игры
        infoPanel.enablePause(); // Включение кнопки паузы

        if (gameThread != null && gameThread.isAlive()) { // Проверка наличия и активности потока игры
            gameThread.interrupt(); // Прерывание потока
        }

        gameThread = new Thread(this::gameLoop); // Создание нового потока для игрового цикла
        gameThread.start(); // Запуск потока
    }

    // Метод приостанавливает игру
    public void pauseGame() {
        infoPanel.disablePause(); // Отключение кнопки паузы
        infoPanel.enableStart(); // Включение кнопки начала игры

        if (gameThread != null && gameThread.isAlive()) { // Проверка наличия и активности потока игры
            gameThread.interrupt(); // Прерывание потока
        }
    }

    // Метод включает или отключает кнопку в зависимости от состояния игры
    public void enableButton(boolean isStop) {
        if (isStop) {
            infoPanel.enablePause(); // Включение кнопки паузы
        } else {
            infoPanel.enableStart(); // Включение кнопки начала игры
        }
    }

    // Метод отключает кнопки управления игрой
    public void disableButton() {
        infoPanel.disablePause(); // Отключение кнопки паузы
        infoPanel.disableStart(); // Отключение кнопки начала игры
    }

    // Метод добавляет информацию о игроках в информационную панель
    public void addPlayerInfo() {
        for (Map.Entry<Integer, Customer> entry : players.entrySet()) {
            Customer value = entry.getValue();
            infoPanel.addPlayerInfo(value); // Добавление информации об игроке в информационную панель
            infoPanel.repaint(); // Обновление отображения панели
        }
    }

    // Метод обновляет информацию об игроках в информационной панели
    public void updateInfo(Map<Integer, Customer> players) {
        this.players = players; // Обновление списка игроков
        for (Map.Entry<Integer, Customer> entry : players.entrySet()) {
            Customer value = entry.getValue();
            infoPanel.updateInfo(value); // Обновление информации об игроке в информационной панели
        }
    }

    // Метод получает панель игрового поля
    public CenterArea getGamefieldPanel() {
        return gamefieldPanel;
    }

    // Метод устанавливает время в информационной панели
    public void setTime(int time) {
        infoPanel.setTimer(time); // Установка времени в информационной панели
    }

    // Метод выполняет выстрел стрелы
    public void shoot(Arrow arrow) {
        gamefieldPanel.addArrow(arrow); // Добавление стрелы на игровое поле
    }

    // Метод устанавливает цели на игровом поле
    public void setTargets(Map<String, Integer> rounds) {
        gamefieldPanel.setTargets(rounds); // Установка целей на игровом поле
    }

    // Метод устанавливает панели игры
    public void setPanels() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Установка вертикального расположения панелей

        gamefieldPanel = new CenterArea(players); // Создание игровой панели
        infoPanel = new Tracking(); // Создание информационной панели

        mainPanel.add(gamefieldPanel); // Добавление игровой панели на главную панель
        mainPanel.add(infoPanel); // Добавление информационной панели на главную панель

        getContentPane().add(mainPanel, BorderLayout.CENTER); // Добавление главной панели на контейнер окна

        infoPanel.setClient(client); // Установка клиента в информационной панели
        infoPanel.setPlayers(players); // Установка списка игроков в информационной панели

        gamefieldPanel.setClient(client); // Установка клиента в игровой панели
        revalidate(); // Повторная проверка компонентов окна
        repaint(); // Перерисовка окна
    }

    // Метод игрового цикла
    private void gameLoop() {
        while (!Thread.currentThread().isInterrupted()) {
            gamefieldPanel.updateArrows(); // Обновление положения стрел на игровом поле
            gamefieldPanel.repaint(); // Перерисовка игрового поля
            gamefieldPanel.updateRounds(); // Обновление информации о целях на игровом поле
            try {
                Thread.sleep(1); // Задержка перед следующей итерацией игрового цикла
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Прерывание потока
            }
        }
    }
}

package Pause;

import Client.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static Interface.Windows.SIZE_SMALL_WINDOW_X;
import static Interface.Windows.SIZE_SMALL_WINDOW_Y;

// Окно паузы
public class Pause extends JFrame {
    private JTextArea infoTextArea;
    private JButton yesButton;
    private JButton noButton;
    private JLabel timerLabel;
    private Client client;
    private boolean isStop;

    // Конструктор для создания окна паузы
    public Pause(String name, boolean isStop) {
        setTitle("Пауза");
        setSize(SIZE_SMALL_WINDOW_X / 2, SIZE_SMALL_WINDOW_Y / 2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        this.isStop = isStop;

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JLabel text = new JLabel();

        if(isStop) {
            text.setText("Игрок " + name + " хочет остановить игру");
        } else {
            text.setText("Игрок " + name + " хочет начать(продолжить) игру");
        }

        mainPanel.add(text, BorderLayout.NORTH);

        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        timerLabel = new JLabel("Осталось 20 сек");
        timerLabel.setHorizontalAlignment(JLabel.RIGHT);
        mainPanel.add(timerLabel, BorderLayout.EAST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 2));

        yesButton = new JButton("Да");
        noButton = new JButton("Нет");
        buttonPanel.add(yesButton);
        buttonPanel.add(noButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчик нажатия кнопки "Да"
        yesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInfoToText(true);
                disableButton();
            }
        });

        // Обработчик нажатия кнопки "Нет"
        noButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInfoToText(false);
                disableButton();
            }
        });
    }

    // Добавление информации в текстовое поле в зависимости от выбора игрока
    public void addInfoToText(boolean chose) {
        if(chose) {
            client.chose(chose, isStop);
        } else {
            client.chose(chose, isStop);
        }
    }

    // Установка клиента
    public void setClient(Client client) {
        this.client = client;
    }

    // Установка сообщения о выборе игрока "Нет"
    public void playerNo(String name) {
        infoTextArea.append("Игрок " + name + " против \n");
    }

    // Установка сообщения о выборе игрока "Да"
    public void playerYes(String name) {
        infoTextArea.append("Игрок " + name + " за \n");
    }

    // Отключение кнопок после выбора
    public void disableButton() {
        yesButton.setEnabled(false);
        noButton.setEnabled(false);
    }

    // Обновление таймера
    public void updateTimer(int sec) {
        timerLabel.setText("Осталось " + sec + " сек");
    }
}

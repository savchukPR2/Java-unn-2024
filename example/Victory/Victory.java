package Victory;

import Client.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static Interface.Windows.SIZE_SMALL_WINDOW_X;
import static Interface.Windows.SIZE_SMALL_WINDOW_Y;

// Окно победы или ничьи
public class Victory extends JFrame {
    private JTextArea infoTextArea;
    private JButton exitButton;
    private Client client;

    // Конструктор для окна победы или ничьи
    public Victory(String name, int score, boolean isEquals) {
        setTitle("Победа");
        setSize(SIZE_SMALL_WINDOW_X / 2, SIZE_SMALL_WINDOW_Y / 2);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        JLabel textwin = new JLabel();

        if(isEquals) {
            textwin.setText("Ничья");
        } else {
            textwin.setText("Победил " + name + " со счетом: " + score);
        }

        mainPanel.add(textwin, BorderLayout.NORTH);

        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        infoTextArea.setFont(new Font("Arial", Font.BOLD, 14));
        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 1));

        exitButton = new JButton("Выход");
        buttonPanel.add(exitButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Обработчик нажатия кнопки "Выход"
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addInfoToText(false);
                exitButton.setEnabled(false);
            }
        });
    }

    // Метод для отправки информации о повторе игры клиенту
    private void addInfoToText(boolean repeat) {
        if(!repeat) {
            client.repeatGame(repeat);
        }
    }

    // Установка клиента для взаимодействия с сервером
    public void setClient(Client client) {
        this.client = client;
    }
}

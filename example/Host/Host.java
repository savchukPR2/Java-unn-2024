package Host;

import Interface.*;
import Client.*;
import Customer.*;
import Host.*;
import Role.*;
import Server.*;

import org.hibernate.*;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

// Класс для создания хоста
public class Host extends JFrame implements Windows, Serializable {

    private DefaultTableModel tableModel; // Модель таблицы
    private Customer player; // Игрок
    private Role mainMenu; // Главное меню
    private Client client; // Клиент
    private JPanel leftPanel; // Панель слева
    private JPanel rightPanel; // Панель справа
    private JScrollPane scrollPane; // Полоса прокрутки
    private JPanel inputPanel; // Панель ввода
    private SessionFactory sessionFactory; // Фабрика сессий Hibernate
    private int id; // Идентификатор
    private boolean connectToDB = false; // Флаг подключения к базе данных

    // Конструктор класса Host
    public Host() {
        setTitle("Создание хоста");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(Windows.SIZE_WINDOW_X / 2, Windows.SIZE_WINDOW_Y / 2);
        setLocationRelativeTo(null);

        // Добавление слушателя события закрытия окна
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });

        leftPanel = new JPanel();
        rightPanel = new JPanel();

        leftPanel.setLayout(new BorderLayout());
        rightPanel.setLayout(new BorderLayout());

        leftPanel.setBackground(Color.LIGHT_GRAY);
        rightPanel.setBackground(Color.WHITE);

        inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(0, 1, 0, 10));

        leftPanel.add(inputPanel, BorderLayout.NORTH);

        leftPanel.add(Box.createVerticalStrut(20), BorderLayout.CENTER);

        Dimension leftPanelSize = new Dimension(getWidth() / 3, getHeight());

        leftPanel.setPreferredSize(leftPanelSize);
        leftPanel.setMinimumSize(leftPanelSize);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(rightPanel, BorderLayout.CENTER);
        getContentPane().add(leftPanel, BorderLayout.EAST);
    }

    // Метод для многопользовательской игры
    public void multiplayer() {
        int port = 1234;
        Server server = new Server(player.getName(), port, 2, 20); // Предполагается, что количество игроков = 2 и другие параметры фиксированы
        if (connectToDB) {
            server.setSessionFactory(sessionFactory);
        }
        try {
            server.startServer(); // Запуск сервера
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        client = new Client(player); // Создание клиента
        client.setMenu(mainMenu); // Установка главного меню
        client.connectToServer("127.0.0.1", 1234); // Подключение к серверу на локальном хосте
        dispose(); // Закрытие текущего окна
    }

    // Метод для добавления информации в таблицу
    public void addToTable(String playerName, Color playerColor, String status) {
        Object[] rowData = {getPlayerNameWithColor(playerName, playerColor), status};
        tableModel.addRow(rowData);
    }

    // Метод для получения имени игрока с цветом
    private String getPlayerNameWithColor(String playerName, Color playerColor) {
        String colorHex = String.format("#%02x%02x%02x", playerColor.getRed(), playerColor.getGreen(), playerColor.getBlue());
        String playerNameWithColor = "<html><font color='" + colorHex + "'>" + playerName + "</font></html>";
        return playerNameWithColor;
    }

    // Метод для обработки подключения клиента
    public void clientConnected() {
        inputPanel.removeAll();
        inputPanel.revalidate();
        inputPanel.repaint();

    }

    // Метод для создания кнопки начала игры
    public void createButtonGame(Client client) {
        this.client = client;

        inputPanel.removeAll();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.revalidate();
        inputPanel.repaint();

        JPanel startPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton start = new JButton("Играть");
        start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("CreateGame");
                client.createGame();
            }
        });

        startPanel.add(start);
        inputPanel.add(startPanel, BorderLayout.NORTH);

    }

    // Метод для установки главного меню
    public void setMainMenu(Role mainMenu) {
        this.mainMenu = mainMenu;
    }

    // Метод для установки игрока
    public void setPlayer(Customer player) {
        this.player = player;
    }

    // Метод для очистки таблицы
    public void clearTable() {
        tableModel.setRowCount(0);
    }

    // Метод для создания таблицы
    public void createTable() {
        String[] columnNames = {"Игрок", "Статус"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);

        Font font = new Font("Arial", Font.PLAIN, 18);
        table.setFont(font);

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);

        scrollPane = new JScrollPane(table);

        rightPanel.add(scrollPane);
        rightPanel.repaint();
        rightPanel.revalidate();
    }

    // Метод для установки сессий Hibernate
    public void setSessionFactory(SessionFactory sessionFactory, int id) {
        this.sessionFactory = sessionFactory;
        connectToDB = true;
        this.id = id;
    }

    // Метод для установки клиента
    public void setClient(Client client) {
        this.client = client;
    }
}

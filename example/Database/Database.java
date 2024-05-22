package Database;

import Entity.*;
import Role.*;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import javax.swing.*;
import java.awt.*;

import static Interface.Windows.SIZE_SMALL_WINDOW_X;
import static Interface.Windows.SIZE_SMALL_WINDOW_Y;

public class Database extends JFrame {
    private static SessionFactory sessionFactory;

    public Database() {
        // Установка параметров окна
        setTitle("Меткий стрелок");
        setSize(SIZE_SMALL_WINDOW_X, SIZE_SMALL_WINDOW_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Создание основной панели
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        // Создание метки для статуса
        JLabel statusLabel = new JLabel(" ");
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Arial", Font.BOLD, 18));
        mainPanel.add(statusLabel);

        // Панель для ввода имени пользователя
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JTextField nameField = new JTextField(15);
        inputPanel.add(nameField);

        // Панель для кнопки аутентификации
        JPanel buttonPanel = new JPanel();
        JButton authButton = new JButton("Войти в игру");
        buttonPanel.add(authButton);

        // Добавление панелей на основную панель
        mainPanel.add(inputPanel);
        mainPanel.add(buttonPanel);

        add(mainPanel);

        // Обработчик кнопки аутентификации
        authButton.addActionListener(e -> {
            String username = nameField.getText();

            // Проверка на пустое имя пользователя
            if (username.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Поля не могут быть пустыми", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Аутентификация пользователя
            int id = authenticateUser(username);

            // Если аутентификация прошла успешно
            if (id == -1) {
                JOptionPane.showMessageDialog(this, "Неверное имя пользователя", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Закрытие текущего окна и открытие основного меню
            dispose();
            Role mainMenu = new Role();
            mainMenu.setSessionFactory(sessionFactory, id);
            mainMenu.setHost();
            mainMenu.setVisible(true);
        });

        try {
            // Подключение к базе данных
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Comparison.class)
                    .buildSessionFactory();

        } catch (Exception ex) {
            // Обработка ошибки подключения к базе данных
            statusLabel.setText("<html>Подключение недоступно</html>");
            inputPanel.setVisible(false);
            buttonPanel.setVisible(false);
            ex.printStackTrace();
        }

        setVisible(true);
    }

    // Метод аутентификации пользователя
    private int authenticateUser(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<Comparison> query = session.createQuery("FROM Comparison WHERE username = :username", Comparison.class);
            query.setParameter("username", username);
            Comparison user = query.uniqueResult();

            // Проверка на существование пользователя
            if (user == null) {
                return -1;
            }

            return user.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    // Точка входа в программу
    public static void main(String[] args) {
        new Database();
    }
}

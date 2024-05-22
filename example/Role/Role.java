package Role;

import Entity.*;
import Host.*;
import Customer.*;
import Server.*;
import Interface.*;
import Connect.*;

import org.hibernate.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Role extends JFrame implements Windows {
    private SessionFactory sessionFactory;
    private boolean connectToDB = false;
    private int id;

    // Установка режима: создание игры или подключение к серверу
    public void setHost() {
        HostOrConnect hostOrConnect = new HostOrConnect(this);
        if (connectToDB) {
            hostOrConnect.setSessionFactory(sessionFactory, id);
        }
        hostOrConnect.setVisible(true);
    }

    // Установка сессий Hibernate и идентификатора пользователя
    public void setSessionFactory(SessionFactory sessionFactory, int id) {
        this.sessionFactory = sessionFactory;
        connectToDB = true;
        this.id = id;
    }

    // Внутренний класс для выбора роли: создание игры или подключение к серверу
    class HostOrConnect extends JFrame {
        private SessionFactory sessionFactory;
        private boolean connectToDB = false;
        private Role mainMenu;
        private int id;

        // Конструктор для окна выбора роли
        public HostOrConnect(Role mainMenu) {
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(300, 200);
            setLocationRelativeTo(null);
            this.mainMenu = mainMenu;

            JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
            buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            JButton becomeHostButton = new JButton("I am a host!");
            JButton connectToServerButton = new JButton("I am a player!");

            buttonPanel.add(becomeHostButton);
            buttonPanel.add(connectToServerButton);

            getContentPane().add(buttonPanel);

            // Обработчик нажатия кнопки "I am a player!"
            connectToServerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!connectToDB) {
                        dispose();
                    } else {
                        Customer player = new Customer(getPlayerName());
                        player.setWins(getInfo());
                        dispose();
                        mainMenu.setVisible(false);

                        Connect networkGameMenu = new Connect(mainMenu, player);
                    }
                }
            });

            // Обработчик нажатия кнопки "I am a host!"
            becomeHostButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (!connectToDB) {
                        dispose();
                    } else {
                        Customer player = new Customer(getPlayerName());
                        player.setWins(getInfo());
                        dispose();
                        mainMenu.setVisible(false);

                        Host createHost = new Host();
                        createHost.setMainMenu(mainMenu);
                        createHost.setPlayer(player);
                        createHost.setSessionFactory(sessionFactory, id);
                        createHost.multiplayer();
                    }
                }
            });
        }

        // Установка сессий Hibernate и идентификатора пользователя
        public void setSessionFactory(SessionFactory sessionFactory, int id) {
            this.sessionFactory = sessionFactory;
            connectToDB = true;
            this.id = id;
        }

        // Получение имени игрока из базы данных
        public String getPlayerName() {
            try (Session session = sessionFactory.openSession()) {
                Comparison user = session.get(Comparison.class, id);
                if (user != null) {
                    return user.getUsername();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }

        // Получение информации о победах из базы данных
        public String getInfo() {
            try (Session session = sessionFactory.openSession()) {
                Comparison user = session.get(Comparison.class, id);
                if (user != null) {
                    return user.getInfo();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "";
        }
    }
}

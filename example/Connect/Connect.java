package Connect;

import Client.*;
import Host.*;
import Role.*;
import Interface.*;
import Customer.*;

// Класс для соединения с сервером
public class Connect implements Windows {

    private Role mainMenu; // Главное меню
    private Customer player; // Игрок

    // Конструктор класса Connect
    public Connect(Role mainMenu, Customer player) {
        this.mainMenu = mainMenu;
        this.player = player;

        connectToServer("127.0.0.1", 1234); // Подключение к серверу по указанному IP-адресу и порту
    }

    // Метод для подключения к серверу
    private void connectToServer(String ipAddress, int port) {
        Client client = new Client(player); // Создание клиента
        client.setMenu(mainMenu); // Установка главного меню
        client.setNetworkMenu(this); // Установка сетевого меню
        client.connectToServer(ipAddress, port); // Подключение к серверу по указанному IP-адресу и порту
    }
}

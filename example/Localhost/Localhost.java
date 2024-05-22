package org.example.Localhost;

import Database.*;

// Главный класс для запуска приложения
public class Localhost {
    public static void main(String[] args) {
        // Создание объекта для установления соединения с базой данных
        Database connectionToDB = new Database();
        // Отображение окна подключения к базе данных
        connectionToDB.setVisible(true);
    }
}

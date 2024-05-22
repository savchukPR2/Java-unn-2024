package Entity;

import javax.persistence.*;

@Entity
@Table(name = "users") // Указание таблицы базы данных
public class Comparison {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Генерация ID автоматически
    private int id;

    @Column(name = "username")
    private String username;

    @Column(name = "info", columnDefinition = "JSON") // Определение столбца с типом JSON
    private String info;

    // Конструктор по умолчанию
    public Comparison() { }

    // Конструктор с параметрами
    public Comparison(String username, String info) {
        this.username = username;
        this.info = info;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

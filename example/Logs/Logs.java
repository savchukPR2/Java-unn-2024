package Logs;

import Customer.*;

import javax.swing.*;

public class Logs extends JPanel {
    private JLabel playerNameLabel;
    private JLabel scoreLabel;
    private JLabel hitsLabel;
    private JLabel winsLabel;
    private Customer player;

    // Конструктор класса
    public Logs(Customer player) {
        this.player = player;
        playerNameLabel = new JLabel("Игрок: " + player.getName());
        scoreLabel = new JLabel("Счет игрока: " + player.getScore());
        hitsLabel = new JLabel("Количество выстрелов: " + player.getHits());
        winsLabel = new JLabel("Количество побед: " + player.getWins());

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        add(playerNameLabel);
        add(scoreLabel);
        add(hitsLabel);
        add(winsLabel);
    }

    // Геттеры и сеттеры для меток с информацией об игроке
    public JLabel getPlayerNameLabel() {
        return playerNameLabel;
    }

    public void setPlayerNameLabel(JLabel playerNameLabel) {
        this.playerNameLabel = playerNameLabel;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    public void setScoreLabel(JLabel scoreLabel) {
        this.scoreLabel = scoreLabel;
    }

    public JLabel getHitsLabel() {
        return hitsLabel;
    }

    public void setHitsLabel(JLabel hitsLabel) {
        this.hitsLabel = hitsLabel;
    }

    public JLabel getWinsLabel() {
        return winsLabel;
    }

    public void setWinsLabel(JLabel winsLabel) {
        this.winsLabel = winsLabel;
    }
}

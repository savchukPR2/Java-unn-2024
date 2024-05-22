package TargetArea;

import Interface.*;
import Client.*;
import Target.*;
import Arrow.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TargetArea extends JPanel implements Windows {
    private ArrayList<Target> rounds = new ArrayList<>();
    private int sizeBigTarget = 90;
    private int sizeSmallTarget = 50;
    private Client client;
    private List<Arrow> arrows;
    private boolean start = false;

    // Конструктор класса
    public TargetArea() {
        setPreferredSize(new Dimension(Windows.SIZE_PANEL_X, Windows.SIZE_PANEL_Y * 2));
        setBackground(Color.WHITE);
        arrows = new ArrayList<>();
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Установка мишеней при изменении размера панели
                setTarget();
                Target.setPanelWidth(getWidth());
                client.setTargets(rounds);
                removeComponentListener(this);
            }
        });
    }

    public void setClient(Client client) {
        this.client = client;
    }


    public void setTargets(Map<String, Integer> targets) {
        for (Map.Entry<String, Integer> entry : targets.entrySet()) {
            String name = entry.getKey();
            Integer coordinate = entry.getValue();
            for (Target target : rounds) {
                if (target.getName().equals(name)) {
                    target.setRoundX(coordinate);
                    repaint();
                    break;
                }
            }
        }
    }

    public ArrayList<Target> setTarget() {
        int bigTargetX = (getSize().width - sizeBigTarget) / 2;
        int bigTargetY = (getSize().height - sizeBigTarget);
        int smallTargetX = (getSize().width - sizeSmallTarget) / 2;
        int bigSpeed = 1;

        Target bigTarget = new Target(sizeBigTarget, Color.WHITE, bigTargetX, bigTargetY, bigSpeed, "big", getWidth() - sizeBigTarget);
        Target smallTarget = new Target(sizeSmallTarget, Color.WHITE, smallTargetX, 0, bigSpeed * 2, "smallLeft", getWidth());

        rounds.add(bigTarget);
        rounds.add(smallTarget);
        return rounds;
    }

    public void addArrow(Arrow arrow) {
        arrows.add(arrow);
        System.out.println("X = " + arrow.getX() +"\t Y = " + arrow.getY());
        start = true;
    }

    public void updateArrows() {
        Iterator<Arrow> iterator = arrows.iterator();
        while (iterator.hasNext()) {
            Arrow arrow = iterator.next();
            arrow.move();
            // Проверка на столкновение стрелы с мишенью или достижение верхней границы
            if (arrow.getY() <= 0 || isCollision(arrow.getX(),arrow.getY(), arrow.getID())) {
                iterator.remove();
            }
        }
    }

    // Проверка на столкновение стрелы с мишенью
    private boolean isCollision(int arrowX, int arrowY, int id) {
        for (Target round : rounds) {
            int roundX = round.getRoundX();
            int roundY = round.getRoundY();
            int roundSize = round.getSize();

            if (arrowX >= roundX && arrowX <= (roundX + roundSize) &&
                    arrowY >= roundY && arrowY <= (roundY + roundSize)) {
                if(round.getName().equals("big")) {
                    client.updatePlayer(1, id);
                } else {
                    client.updatePlayer(2, id);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawLine(0, sizeSmallTarget / 2, getSize().width,  sizeSmallTarget / 2);
        g.drawLine(0, getHeight() - (sizeBigTarget / 2), getWidth(), getHeight() - (sizeBigTarget / 2));
        // Отрисовка мишеней
        for(Target round : rounds){
            round.draw(g);
        }
        // Если игра началась, отрисовка стрел
        if(start) {
            for (Arrow arrow : arrows) {
                arrow.draw(g);
            }
        }
    }
}

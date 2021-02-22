package com.gorlov;

import javax.imageio.ImageIO;
import javax.imageio.stream.FileImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Main extends JFrame implements Runnable {

    private final static Integer WIDTH_WINDOW = 600;
    private final static Integer HIGHT_WINDOW = 600;
    private static PetsAbstractClass pets;
    private static int countAct = 0;
    private static int logic = 0;
    private static Graphics g;
    private static Image room;
    public static Thread t;
    private static Boolean gameAlive = true;

    public Main() {
        try {
            room = ImageIO.read(new FileImageInputStream(new File("resources/room.png")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JFrame frame = new JFrame("Игра тамагочи");
        frame.setResizable(false);
        JPanel panelDraw = new PanelDraw();
        JButton eatButton = new JButton("Кормить");
        eatButton.setLocation(0, HIGHT_WINDOW - 100);
        eatButton.setSize(100, 100);
        JButton sleepButton = new JButton("Спать");
        eatButton.setLocation(0, HIGHT_WINDOW - 100);
        eatButton.setSize(100, 100);
        frame.add(panelDraw);
        frame.setPreferredSize(new Dimension(WIDTH_WINDOW, HIGHT_WINDOW));
        frame.pack();
        frame.setVisible(true);
        pets = new DogClass("Жужа", panelDraw.getGraphics());
        t = new Thread(this);
        t.start();
        Thread statisticThreead = new Thread(new Runnable() {

            private Integer tickHunger = 0;
            private Integer tickSleep = 0;

            @Override
            public void run() {
                while (true) {
                    if (tickHunger == 50) {
                        InterfaceClass.hunger -= 20;
                        tickHunger = 0;
                    }
                    if (tickSleep == 100) {
                        InterfaceClass.sleep -= 20;
                        tickSleep = 0;
                    }
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    tickHunger++;
                    tickSleep++;
                    if (InterfaceClass.hunger == 0 || InterfaceClass.hp == 0 || InterfaceClass.sleep == 0) {
                        gameAlive = false;
                        JOptionPane.showMessageDialog(frame, "Ваш питомец помер! В следующий раз будьте внимательнее");
                        break;
                    }
                }
            }
        });
        statisticThreead.start();
    }

    public static void main(String[] args) {
        JFrame.setDefaultLookAndFeelDecorated(false);
        new Main();
    }

    private int choiceDirecrion(){
        if (countAct >= new Random().nextInt(20) + 10) {
            countAct = 0;
            logic = new Random().nextInt(300);
        } else if (pets.getX() < 10) {
            logic = 0;
        } else if (pets.getX() >= Main.WIDTH_WINDOW - 120) {
            logic = 100;
        }
        return logic;
    }

    @Override
    public void run() {
        while (gameAlive) {
            if (!PetsAbstractClass.isMousePress() && pets.y < 430) {
                pets.fall();
            }
            if (!PetsAbstractClass.isMousePress()) {
                pets.Draw(choiceDirecrion());
            } else {
                pets.carryOver();
            }
            countAct++;
        }
    }

    private static class PanelDraw extends JPanel {

        public PanelDraw() {
            setPreferredSize(new Dimension(Main.WIDTH_WINDOW, Main.HIGHT_WINDOW));
            addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {

                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    if (pets != null) pets.move(e.getX(), e.getY());
                }
            });

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    pets.click(e.getX(), e.getY());
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {

                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Main.g = g;
        }
    }

}

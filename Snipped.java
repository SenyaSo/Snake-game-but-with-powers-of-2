package com.mycompany.snipped;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class Snipped {

    public static void main(String[] args) {

        int boardWidth = 600;
        int boardHeight = boardWidth;
        JMenuBar menuBar = new JMenuBar();
        JMenu levelMenu = new JMenu("Уровни");
        JMenuItem level1Item = new JMenuItem("Уровень 1");
        JMenuItem level2Item = new JMenuItem("Уровень 2");
        JMenuItem level3Item = new JMenuItem("Уровень 3");
        levelMenu.add(level1Item);
        levelMenu.add(level2Item);
        levelMenu.add(level3Item);
        menuBar.add(levelMenu);

        JMenu helpMenu = new JMenu("Информация");
        JMenuItem rulesItem = new JMenuItem("Правила игры");
        helpMenu.add(rulesItem);
        menuBar.add(helpMenu);

        JFrame frame = new JFrame("Змейка и двойка"); 

        frame.setJMenuBar(menuBar);

        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true); //Меняется на fase
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        SnakeGAme snakeGame1 = new SnakeGAme(boardWidth, boardHeight);
        SnakeGAme snakeGame2 = new SnakeGAme(boardWidth, boardHeight);
        SnakeGAme snakeGame3 = new SnakeGAme(boardWidth, boardHeight);

        rulesItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] op = {"Управление", "Правила"};
                int ch = JOptionPane.showOptionDialog(null, "Выберите страницу для просмора", "Меню", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, op, op[0]);
                if (ch == 0) {
                    JOptionPane.showMessageDialog(null, "Движение: WASD и  ↓→↑←.\n\nНачать игру: 'Пробел'.\n\nРестарт: 'r'.", "Управление", JOptionPane.INFORMATION_MESSAGE);
                } else if (ch == 1) {
                    JOptionPane.showMessageDialog(null, "Уровень 1: Нужно есть только те числа, которые относятся к степеням 2-ки \n(игра закончится, если врезаться в стены или съесть неправильное число).\n\nУровень 2: Нужно есть степени двойки в порядке возрастания \n(игра закончится при поедании числа 2048).\n\nУровень 3: Нужно есть есть только то число, в зависимости от степени двойки на экране \n(игра закончится, если врезаться в стену или съесть неправильное число).");
                }
            }
        });

        level1Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе уровня 1
                frame.remove(snakeGame2);
                frame.remove(snakeGame3);
                snakeGame1.setLevel(1);
                snakeGame1.tile();
                snakeGame1.placeFood();
                frame.add(snakeGame1);
                frame.pack();
                snakeGame1.requestFocusInWindow();
            }
        });

        level2Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе уровня 2
                frame.remove(snakeGame1);
                frame.remove(snakeGame3);
                snakeGame2.setLevel(2);
                snakeGame2.tile();
                snakeGame2.setPower(1);
                snakeGame2.placeFood3();
                frame.add(snakeGame2);
                frame.pack();
                snakeGame2.requestFocusInWindow();
            }
        });

        level3Item.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Действия при выборе уровня 3
                frame.remove(snakeGame1);
                frame.remove(snakeGame2);
                snakeGame3.setLevel(3);
                snakeGame3.placeFood4();
                frame.add(snakeGame3);
                frame.pack();
                snakeGame3.requestFocusInWindow();
            }
        });
    }
}

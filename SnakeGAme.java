package com.mycompany.snipped;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.JPanel;
import javax.swing.Timer;

public class SnakeGAme extends JPanel implements ActionListener, KeyListener {

    private class Tile {

        int x;
        int y;
        Integer value;

        Tile(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    private int boardWidth;
    private int boardHeight;
    private int tileSize = 35;

    private boolean hmm = false;

    //Змея
    private Tile snakeHead;
    private ArrayList<Tile> snakeBody;

    //Еда
    private Random random = new Random();
    private Tile food1 = new Tile(1, 1);
    private Tile food2 = new Tile(1, 1);

    private Tile food3;
    private Tile food4;
    private Tile food5;

    void tile() {
        food3 = new Tile(1, 1);
        food4 = new Tile(1, 1);
        food5 = new Tile(1, 1);
    }

    private Set<Integer> eatenFoods;

    //Таймер
    private int velocityX;
    private int velocityY;
    private Timer gameLoop;

    private boolean gameOver = false;
    private boolean isMoving = false;
    private int power;
    private int level;

    private int[] powersOfTwo = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11};
    private int currentPowerIndex = -1;

    SnakeGAme(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.WHITE);
        addKeyListener(this);
        setFocusable(true);

        snakeHead = new Tile(5, 5);
        snakeBody = new ArrayList<Tile>();

        random = new Random();
        eatenFoods = new HashSet<>();

        velocityX = 1;
        velocityY = 0;

        //Игровой таймер (чем больше число, тем быстрее)
        gameLoop = new Timer(250, this);
        gameLoop.start();
    }

    Graphics gg;

    //Пробел - начало игры
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (!isMoving) {
                isMoving = true;
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_Z) {
            if (hmm == false) {
                hmm = true;
            } else {
                hmm = false;
            }
        }
        //Генеиация уровня в зависимости от выбранного уровня 
        //Указание кнопки для рестарта игры 
        if (gameOver) {
            if (e.getKeyCode() == KeyEvent.VK_R) {
                gameOver = false;
                gameLoop.start();
                snakeHead = new Tile(5, 5);
                snakeBody = new ArrayList<Tile>();

                random = new Random();
                eatenFoods = new HashSet<>();

                velocityX = 1;
                velocityY = 0;
                isMoving = false;

                if (level == 1) {
                    placeFood();
                } else if (level == 2) {
                    power = 1;
                    placeFood3();
                } else {
                    placeFood4();
                }
            }
        }

        //Назначеник кнопок для передвижения
        if (e.getKeyCode() == KeyEvent.VK_W && velocityY != 1 || e.getKeyCode() == KeyEvent.VK_UP) {
            velocityX = 0;
            velocityY = -1;
        } else if (e.getKeyCode() == KeyEvent.VK_S && velocityY != -1 || e.getKeyCode() == KeyEvent.VK_DOWN) {
            velocityX = 0;
            velocityY = 1;
        } else if (e.getKeyCode() == KeyEvent.VK_A && velocityX != 1 || e.getKeyCode() == KeyEvent.VK_LEFT) {
            velocityX = -1;
            velocityY = 0;
        } else if (e.getKeyCode() == KeyEvent.VK_D && velocityX != -1 || e.getKeyCode() == KeyEvent.VK_RIGHT) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //Отрисовка еды
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
        draw2(g);

        if (hmm == true) {
            Draw(g);
        }
    }

    private void Draw(Graphics g) {
        g.setColor(Color.darkGray);
        g.fill3DRect(1 * tileSize, 1 * tileSize, tileSize, tileSize, true);
        g.fill3DRect(3 * tileSize, 1 * tileSize, tileSize, tileSize, true);
        g.fill3DRect(1 * tileSize, 2 * tileSize, tileSize, tileSize, true);
        g.fill3DRect(3 * tileSize, 2 * tileSize, tileSize, tileSize, true);

        g.fill3DRect(1 * tileSize, 5 * tileSize, tileSize, tileSize, true);
        g.fill3DRect(2 * tileSize, 5 * tileSize, tileSize, tileSize, true);
        g.fill3DRect(3 * tileSize, 5 * tileSize, tileSize, tileSize, true);

        g.fill3DRect(4 * tileSize, 4 * tileSize, tileSize, tileSize, true);
        g.fill3DRect(0 * tileSize, 4 * tileSize, tileSize, tileSize, true);
    }

    private void draw(Graphics g) {
        //Еда 1
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, tileSize / 2));
        g.drawString(String.valueOf(food1.value), food1.x * tileSize + tileSize / 4, food1.y * tileSize + tileSize / 2);

        //Еда 2
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, tileSize / 2));
        g.drawString(String.valueOf(food2.value), food2.x * tileSize + tileSize / 4, food2.y * tileSize + tileSize / 2);

        if (level != 3) {
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, tileSize / 2));
            g.drawString(String.valueOf(food3.value), food3.x * tileSize + tileSize / 4, food3.y * tileSize + tileSize / 2);

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, tileSize / 2));
            g.drawString(String.valueOf(food4.value), food4.x * tileSize + tileSize / 4, food4.y * tileSize + tileSize / 2);

            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, tileSize / 2));
            g.drawString(String.valueOf(food5.value), food5.x * tileSize + tileSize / 4, food5.y * tileSize + tileSize / 2);
        }

        //Голова змеи
        g.setColor(Color.darkGray);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);

        //Тело змеи
        for (int i = 0; i < snakeBody.size(); i++) {
            Tile snakePart = snakeBody.get(i);
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
        }

        //Счёт
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if (gameOver) {
            g.setColor(Color.red);
            g.drawString("Проигрыш: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        } else {
            g.drawString("Результат: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        }
    }

    public void placeFood() {
        //Уровень 1 Случайное расположение степеней 2-ки     
        if (level == 1) {
            food1.value = (int) Math.pow(2, random.nextInt(11));
            food1.x = random.nextInt(boardWidth / tileSize - 3) + 1;
            food1.y = random.nextInt(boardHeight / tileSize - 3) + 1;
            placeFood2();
        } else {
            food2.value = (int) Math.pow(2, random.nextInt(11));
            food2.x = random.nextInt(boardWidth / tileSize - 3) + 1;
            food2.y = random.nextInt(boardHeight / tileSize - 3) + 1;
        }
    }

    public void placeFood3() {
        //Уровень 2 Генерация степени двойки в прядке возрастания    //2048   
        if (power <= 2048) {
            food1.value = power;
            food1.x = random.nextInt(boardWidth / tileSize - 2) + 1;
            food1.y = random.nextInt(boardHeight / tileSize - 2) + 1;
            power *= 2;
        } else {
            gameLoop.stop();
        }
        placeFood2();
    }

    private void placeFood2() {
        //Уровень 1-2 (Второе случайное число)
        int selectedNumber = random.nextInt(100) + 1; // Генерируем случайное число от 1 до 100
        int selectedNumber2 = random.nextInt(100) + 1;
        int selectedNumber3 = random.nextInt(100) + 1;
        int selectedNumber4 = random.nextInt(100) + 1;

        while ((selectedNumber & (selectedNumber - 1)) == 0 && selectedNumber != 0) {
            selectedNumber = random.nextInt(100) + 1;
        }
        food2.value = selectedNumber;
        food2.x = random.nextInt(boardWidth / tileSize - 2) + 1;
        food2.y = random.nextInt(boardHeight / tileSize - 2) + 1;
        // Проверка второго числа
        if (level != 3) {
            while ((selectedNumber2 & (selectedNumber2 - 1)) == 0 && selectedNumber2 != 0) {
                selectedNumber2 = random.nextInt(100) + 1;
            }         
            do {
                food3.value = selectedNumber2;
                food3.x = random.nextInt(boardWidth / tileSize - 2) + 1;
                food3.y = random.nextInt(boardHeight / tileSize - 2) + 1;
            } while (food3.x == food1.x && food3.y == food1.y);

            while ((selectedNumber3 & (selectedNumber3 - 1)) == 0 && selectedNumber3 != 0) {
                selectedNumber3 = random.nextInt(100) + 1;
            }           
            do {
                food4.value = selectedNumber3;
                food4.x = random.nextInt(boardWidth / tileSize - 2) + 1;
                food4.y = random.nextInt(boardHeight / tileSize - 2) + 1;
            } while ((food4.x == food1.x && food4.y == food1.y) || (food4.x == food2.x && food4.y == food2.y));

            while ((selectedNumber4 & (selectedNumber4 - 1)) == 0 && selectedNumber4 != 0) {
                selectedNumber4 = random.nextInt(100) + 1;
            }           
            do {
                food5.value = selectedNumber4;
                food5.x = random.nextInt(boardWidth / tileSize - 2) + 1;
                food5.y = random.nextInt(boardHeight / tileSize - 2) + 1;
            } while ((food5.x == food1.x && food5.y == food1.y) || (food5.x == food2.x && food5.y == food2.y) || (food5.x == food4.x && food5.y == food4.y));
        }
    }

    private void draw2(Graphics g) {
        //Уровень 3 Вывод сообщения о нужной степени
        if (currentPowerIndex != -1) {
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.drawString(" Степень 2^" + powersOfTwo[currentPowerIndex], 125, 35);
        }
    }

    private int generateRandomPower() {
        //Уровень 3 Генерация случной степени двойки для вывода в сообщение draw2
        int randomIndex = random.nextInt(powersOfTwo.length);
        return powersOfTwo[randomIndex];
    }

    void placeFood4() {
        //Уровень 3 Генерация второй еды, не похожей с первой
        placeFood();
        currentPowerIndex = generateRandomPower();
        while ((int) Math.pow(2, powersOfTwo[currentPowerIndex]) == food2.value) {
            currentPowerIndex = generateRandomPower();
        }
        food1.value = (int) Math.pow(2, powersOfTwo[currentPowerIndex]);
        food1.x = random.nextInt(boardWidth / tileSize - 2) + 1;
        food1.y = random.nextInt(boardHeight / tileSize - 2) + 1;
    }

    public void move() {
        if (isMoving) {
            //Кушание в зависимости от уровня
            if (collision(snakeHead, food1)) {
                snakeBody.add(new Tile(food1.x, food1.y));
                if (level == 1) {
                    placeFood();
                } else if (level == 2) {
                    placeFood3();
                } else {
                    placeFood4();
                }
            }

            for (int i = snakeBody.size() - 1; i >= 0; i--) {
                Tile snakePart = snakeBody.get(i);
                if (i == 0) { //right before the head
                    snakePart.x = snakeHead.x;
                    snakePart.y = snakeHead.y;
                } else {
                    Tile prevSnakePart = snakeBody.get(i - 1);
                    snakePart.x = prevSnakePart.x;
                    snakePart.y = prevSnakePart.y;
                }
            }

            snakeHead.x += velocityX;
            snakeHead.y += velocityY;

            for (int i = 0; i < snakeBody.size(); i++) {
                Tile snakePart = snakeBody.get(i);

                //Конец игры, если игрок врежется в себя
                if (collision(snakeHead, snakePart)) {
                    gameOver = true;
                }
            }

            //Конец игры, если игрок съест ненужное число 
            if (snakeHead.x * tileSize < 0 || snakeHead.x * tileSize > boardWidth - tileSize || snakeHead.y * tileSize < 0 || snakeHead.y * tileSize > boardHeight - tileSize) {
                gameOver = true;
            }
            if (collision(snakeHead, food2)) {
                gameOver = true;
            }
            if (level != 3) {
                if (collision(snakeHead, food3)) {
                    gameOver = true;
                }
                if (collision(snakeHead, food4)) {
                    gameOver = true;
                }
                if (collision(snakeHead, food5)) {
                    gameOver = true;
                }
            }
        }
    }

    //Определение коллизии поля
    private boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    //Остановка игры, если проиграл
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
        }
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    //Получение информации о выбранном уровня
    public int getLevel() {
        return level;
    }

    //Выбор уровня
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }
}

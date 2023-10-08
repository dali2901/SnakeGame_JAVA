import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends JPanel implements KeyListener {


    //視窗的每一格有多大
    public static final int cellSize = 20;

    //視窗的大小是400X400
    public static int width = 400;
    public static int height = 400;


    //總共有幾欄幾列
    public static int row = height / cellSize;
    public static int column = width / cellSize;

    private Snake snake;
    private Fruit fruit;
    private Timer timer;
    private int speed = 100; //設定給Timer每隔多久就要執行一次
    private static String direction;

    //預防bug，當keypress按下去後，畫面刷新前，按下其他的keypress無效
    private boolean allowKeyPress;

    private int score;
    private int highestScore;
    String desktop = System.getProperty("user.home") + "//Desktop";
    String myfile = desktop + "filename.txt";
    public Main() {

//        snake = new Snake();
//        fruit = new Fruit();
//
//        //預設往右
//        direction = "Right";
//        //設定true因為遊戲剛開始時可以用eventListener
//        allowKeyPress = true;

        // --------上放因為與resetGame() 裡的程式碼重複 所以可以直接執行  resetGame()

        resetGame();

        //開始監聽
        addKeyListener(this);

        //讀取最高分數
        readHighestScore();
    }

    private void setTimer() {

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, 0, speed);  // scheduleAtFixedRate 方法的目的是 「每一個固定的時間裡他會執行一件事情」
    }


    public void resetGame() {

        score = 0;
        if (snake != null) {
            snake.getSnakeBody().clear();
        }

        allowKeyPress = true;
        direction = "Right";
        snake = new Snake();
        fruit = new Fruit();
        setTimer();
    }


    @Override
    public void paintComponent(Graphics g) {
//        System.out.println("We are calling paintComponent");
// ------------------蛇有沒有吃到自己的邏輯------------------(讓頭的(x,y)去對比身體的(x,y))


        ArrayList<Node> snakeBody = snake.getSnakeBody();
        Node head = snakeBody.get(0);
        for (int i = 1; i < snake.getSnakeBody().size(); i++) {

            if (snakeBody.get(i).x == head.x && snakeBody.get(i).y == head.y) {

                allowKeyPress = false;
                timer.cancel();
                timer.purge();

                int response = JOptionPane.showInternalOptionDialog(this, "Game Over !! Your Score is "+ score + " Try Again??",
                        "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, null, JOptionPane.YES_OPTION);

                writeFile(score);

                switch (response) {

                    case JOptionPane.CLOSED_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.NO_OPTION:
                        System.exit(0);
                        break;
                    case JOptionPane.YES_OPTION:
                        resetGame();
                        return;
                }
            }

        }


// ------------------蛇有沒有吃到自己的邏輯------------------


        //把背景畫成黑色
        g.fillRect(0, 0, width, height);
        fruit.drawFruit(g);
        snake.drawSnake(g);


        //下方移除蛇的尾巴，並放到蛇的頭
        int snakeX = snake.getSnakeBody().get(0).x;
        int snakeY = snake.getSnakeBody().get(0).y;

        // if 往右走  x += cellSize  y不變
        // if 往左走  x  -= cellSize  y不變
        // if 往上走  y -= cellSize   x不變
        // if 往下走  y += cellSize  x不變

        if (direction.equals("Left")) {
            snakeX -= cellSize;
        } else if (direction.equals("Up")) {
            snakeY -= cellSize;
        } else if (direction.equals("Right")) {
            snakeX += cellSize;
        } else if (direction.equals("Down")) {
            snakeY += cellSize;
        }

        //根據snakeX , snakeY去找新的頭
        Node newHead = new Node(snakeX, snakeY);
        //------------------有沒有吃到Fruit的邏輯------------------(有吃到身體就要拉長，就不Remove身體)

        if (snake.getSnakeBody().get(0).x == fruit.getX() && snake.getSnakeBody().get(0).y == fruit.getY()) {

            //System.out.println("吃到了");
            // 待辦邏輯
            //1.Set Fruit to a new location
            //2.drawFruit
            //3.score++

            fruit.setNewLocation(snake);
            fruit.drawFruit(g);
            score++;

        } else {

            //移除尾巴
            snake.getSnakeBody().remove(snake.getSnakeBody().size() - 1);
        }
        //------------------有沒有吃到Fruit的邏輯------------------


        //加入新頭
        snake.getSnakeBody().add(0, newHead);

        allowKeyPress = true; //全部都執行完後 讓他等於true 代表使用者可以再重新按一次螢幕
        requestFocusInWindow();


    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(width, height);
    }

    public static void main(String[] args) {

        JFrame window = new JFrame("SnakeGame");

        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setContentPane(new Main());
        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        window.setResizable(false);


    }

    //上方實作keyListener ，這邊改寫為了控制蛇
    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println(e.getKeyCode());

        if (allowKeyPress) {
            if (e.getKeyCode() == 37 && !direction.equals("Right")) {
                direction = "Left";
            } else if (e.getKeyCode() == 38 && !direction.equals("Down")) {
                direction = "Up";
            } else if (e.getKeyCode() == 39 && !direction.equals("Left")) {
                direction = "Right";
            } else if (e.getKeyCode() == 40 && !direction.equals("Up")) {
                direction = "Down";
            }

            allowKeyPress = false;   //在下一次 被改成true之前 都沒辦法再用keypress，(在paintComponent把他改成true
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }


    //用來讀取最高分的Function
    public void readHighestScore(){

        try {
            File myObj = new File(myfile);
            Scanner myReader = new Scanner(myObj);
            highestScore = myReader.nextInt();
            myReader.close();
        }catch (FileNotFoundException e){
            highestScore =0;
            try{
                File myObj = new File(myfile);
                if(myObj.createNewFile()){
                    System.out.println("File created :" + myObj.getName());
                }
                FileWriter myWriter = new FileWriter(myObj.getName());
                myWriter.write(""+0);
            }catch (IOException e1){
                System.out.println("An Error Occured");
                e1.printStackTrace();
            }

        }
    }

    //遊戲結束時，分數比現有的所有分數要高的話，要更新的邏輯
    public  void writeFile(int score){

        try{
            FileWriter myWriter = new FileWriter(myfile);
            if(score>highestScore){
                myWriter.write(""+score);
                highestScore = score;
            }else {
                myWriter.write(""+highestScore);
            }
            myWriter.close();
        }catch (IOException e){
            e.printStackTrace();
        }


    }


}
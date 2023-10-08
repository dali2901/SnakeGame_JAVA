import javax.swing.*;
import java.awt.*;

public class Fruit {

    private int x;
    private int y;


    private ImageIcon fruitImg;

    public Fruit() {
//        fruitImg = new ImageIcon("fruit.png");
        fruitImg = new ImageIcon(getClass().getResource("fruit.png"));
        this.x = (int) (Math.floor(Math.random() * Main.column) * Main.cellSize);
        this.y = (int) (Math.floor(Math.random() * Main.row) * Main.cellSize);
    }


    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void drawFruit(Graphics g) {

        // 一開始水果示意圖的橢圓形
        //        g.setColor(Color.PINK);
        //        g.fillOval(this.x, this.y, Main.cellSize, Main.cellSize);

        //下方要放真的水果圖片
        fruitImg.paintIcon(null, g, this.x, this.y);
    }

    public void setNewLocation(Snake snake) {


        int newX;
        int newY;
        boolean overlapping;  //設定用來判斷目前所選的新位置是否跟蛇身重疊

        do {

            newX = (int) (Math.floor(Math.random() * Main.column) * Main.cellSize);
            newY = (int) (Math.floor(Math.random() * Main.row) * Main.cellSize);
            overlapping = check_overlap(newX, newY, snake);
        } while (overlapping); //如果水果出現的位置跟身體有重疊，就會不斷再找新位置

        this.x = newX;
        this.y = newY;
    }


    //判斷水果新產生的位置是否有跟身體重疊的邏輯
    private boolean check_overlap(int x, int y, Snake s) {

        for (int j = 0; j < s.getSnakeBody().size(); j++) {

            if (x == s.getSnakeBody().get(j).x && y == s.getSnakeBody().get(j).y) {
                return true;
            }
        }
        return false;
    }

}

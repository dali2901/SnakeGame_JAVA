import java.awt.*;
import java.util.ArrayList;

public class Snake {


    private ArrayList<Node> snakeBody;

    public Snake() {
        snakeBody = new ArrayList<>();
        snakeBody.add(new Node(80, 0));
        snakeBody.add(new Node(60, 0));
        snakeBody.add(new Node(40, 0));
        snakeBody.add(new Node(20, 0));
    }


    public ArrayList<Node> getSnakeBody() {

        return snakeBody;
    }

    public void drawSnake(Graphics g) {
//      for(Node node :snakeBody){}   //本來這樣寫
//      為了讓頭與身體有差別的邏輯，所以這樣寫

        for (int i = 0; i < snakeBody.size(); i++) {

            if (i == 0) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.ORANGE);
            }

            Node node = snakeBody.get(i);
            //當蛇超出視窗邊界的邏輯處理
            if (node.x >= Main.width) {
                node.x = 0;
            }

            if (node.x < 0) {
                node.x = Main.width - Main.cellSize;
            }

            if (node.y >= Main.height) {
                node.y = 0;
            }

            if (node.y < 0) {
                node.y = Main.height - Main.cellSize;
            }

            g.fillOval(node.x, node.y, Main.cellSize, Main.cellSize);
        }
    }

}

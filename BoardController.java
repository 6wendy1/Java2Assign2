package application.ui;

import application.client.Client;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class BoardController implements Initializable {
    private static final int PLAY_1 = 1;
    private static final int PLAY_2 = 2;
    private static final int EMPTY = 0;
    private static final int BOUND = 90;
    private static final int OFFSET = 15;

    @FXML
    public Pane base_square;

    @FXML
    public Rectangle game_panel;

    @FXML
//    private TextField nameField;

    public Client client;

    private static boolean TURN = false;
    private boolean ABLE;
    private boolean WAIT;
    private int id;

    private static final int[][] chessBoard = new int[3][3];
    private static final boolean[][] flag = new boolean[3][3];


    @Override
    public void initialize(URL location, ResourceBundle resources) {//界面打开后的初始化操作
        game_panel.setOnMouseClicked(
                event -> {
                    int x = (int) (event.getX() / BOUND);
                    int y = (int) (event.getY() / BOUND);
                    if(ABLE&&!WAIT) {
                            System.out.println(id);
                            int i=0;
                            if (id==1){
                                TURN=true;
                                client.ps.println("1:finish");
                                client.ps.flush();
                                System.out.println("1Continue successfully.");
                                i=2;
                            }else if(id==2){
                                TURN=false;
                                client.ps.println("2:finish");
                                client.ps.flush();
                                System.out.println("2Continue successfully.");
                                i=1;
                            }
                        refreshBoard(x, y);
                        client.ps.printf("[i,x,y,m]:%d:%d,%d,%d\n", i,x, y,chessBoard[x][y]);
                        client.ps.flush();
                            WAIT=!WAIT;
//                            client.ps.println("It's "+i+"'s turn.");
//                            client.ps.flush();
//                            TURN = !TURN;
//                            ABLE = !ABLE;
                    }else {
                        System.out.println("It's not your turn.");
//                        if (!WAIT) ABLE=true;
                    }
        }
        );
    }



    public void initBoard(){
        for (int i=0; i<2; i++)
            for (int j=0; j<2; j++)
                chessBoard[i][j] = EMPTY;    //初始化棋盘棋子信息
    }

    public boolean refreshBoard (int x, int y) {
        if (chessBoard[x][y] == EMPTY) {
            chessBoard[x][y] = TURN ? PLAY_1 : PLAY_2;//当turn为FALSE时，棋盘格设为2，反之为1
            drawChess();//
            System.out.println("refresh");
            return true;
        }
        return false;
    }

    public boolean resetBoard(int x,int y,int m){
        if (chessBoard[x][y] == EMPTY) {
            chessBoard[x][y] =m;//当turn为FALSE时，棋盘格设为2，反之为1
            drawChess();//
            System.out.println("reset");
            return true;
        }
        return false;
    }

    public void setCell(String s){
        String []ss=s.split(",");
        int i=Integer.parseInt(ss[0]);
        int x=Integer.parseInt(ss[1]);
        int y=Integer.parseInt(ss[2]);
        int m=Integer.parseInt(ss[3]) ;
//        chessBoard[x][y]=m;
        resetBoard(x,y,m);
//        System.out.println(chessBoard[x][y]);
//        if (judgeCol(s)||judgeRow(s)||judgeDeputyDiagonal(s)||judgeMainDiagonal(s)){
//                            System.out.println("You are win!");
////                            ps.println("You are win!");
////                            ps.flush();
//                        }else if(judgeEqual()){
//                            System.out.println("End in a draw!");
////                            ps.println("End in a draw!");
////                            ps.flush();
//                        }
    }

    public boolean judgeRow(String s) {
        String []ss=s.split(",");
        int x=Integer.parseInt(ss[1]);
        int m=Integer.parseInt(ss[3]) ;
        int count = 0;
        for (int i = 0; i < 3; i++) {
            if (chessBoard[x][i] == m)
                count++;
        }
        return count == 3;
    }

    public boolean judgeCol(String s) {
        String []ss=s.split(",");
        int y=Integer.parseInt(ss[2]);
        int m=Integer.parseInt(ss[3]) ;
        int count = 0;
        for (int i = 0; i < 3; i++) {
            if (chessBoard[i][y] == m)
                count++;
        }
        return count == 3;
    }

    public boolean judgeMainDiagonal(String s) {
        String []ss=s.split(",");
        int x=Integer.parseInt(ss[1]);
        int y=Integer.parseInt(ss[2]);
        int m=Integer.parseInt(ss[3]) ;
        int count = 1;
        if (x==0&&y==0){
            for (int i = x, j = y; i < 2 && j < 2; i++, j++) {
            if (chessBoard[i + 1][j + 1] == m)
                count++;
            System.out.println("1你运行了吗"+count);
        }
        }else if (x==1&y==1){
            for (int i = x, j = y; i < 2 && j < 2; i++, j++) {
                if (chessBoard[i + 1][j + 1] == m)
                    count++;
            }
            for (int i = x, j = y; i >0 && j > 0; i--, j--) {
                if (chessBoard[i - 1][j - 1] == m) {
                    count++;
                }
            }
            System.out.println("2你运行了吗"+count);
        }else if (x==2&&y==2){
            for (int i = x, j = y; i >0 && j >0; i--, j--) {
                if (chessBoard[i - 1][j - 1] == m){
                    count++;
            }
                System.out.println("3你运行了吗"+count);
            }}
//        for (int i = x, j = y; i < 1 && j < 1; i++, j++) {
//            if (chessBoard[i + 1][j + 1] == m)
//                count++;
//            System.out.println("1你运行了吗"+count);
//        }
//        for (int i = x, j = y; i >= 1 && j >= 1; i--, j--) {
//                if (chessBoard[i - 1][j - 1] == m){
//                    count++;
//            }
//             if (i+1<3&&j+1<3){
//                if (chessBoard[i + 1][j + 1] == m)
//                    count++;
//            }
//            System.out.println("2你运行了吗"+count);
//        }

        System.out.println("judgecount"+count);
        return count == 3;
    }

    public boolean judgeDeputyDiagonal(String s) {
        String []ss=s.split(",");
        int x=Integer.parseInt(ss[1]);
        int y=Integer.parseInt(ss[2]);
        int m=Integer.parseInt(ss[3]) ;
        int count = 1;
        if (x==2&&y==0){
            for (int i = x, j = y; i > 0 && j < 2; i--, j++) {
                if (i - 1 != 0 && j + 1 != 0) {
                    if (chessBoard[i - 1][j + 1] == m)
                        count++;
                }
            }
        }
        else if (x==1&&y==1){
            for (int i = x, j = y; i > 0 && j < 2; i--, j++) {
                    if (chessBoard[i - 1][j + 1] == m) {
                        count++;
                    }
            }
            for (int i = x, j = y; i > 0 && j < 2; i--, j++) {
                    if (chessBoard[i + 1][j - 1] == m){
                        count++;
                }
            }
        }
        else if (x==0&&y==2){
            for (int i = x, j = y; i > 0 && j < 2; i--, j++) {
                if (chessBoard[i + 1][j - 1] == m){
                    count++;
                }
            }
        }
        return count == 3;
    }

    public boolean judgeEqual(){
        int count=0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (chessBoard[i][j]>0){
                    count++;
                }
            }
        }
        return count == 9;
    }

    private void drawChess () {
        for (int i = 0; i < chessBoard.length; i++) {
            for (int j = 0; j < chessBoard[0].length; j++) {
                if (flag[i][j]) {
                    // This square has been drawing, ignore.
                    continue;
                }
                switch (chessBoard[i][j]) {
                    case PLAY_1://1->circle
                        drawCircle(i, j);
                        break;
                    case PLAY_2://2->line
                        drawLine(i, j);
                        break;
                    case EMPTY:
                        // do nothing
                        break;
                    default:
                        System.err.println("Invalid value!");
                }
            }
        }
    }

    private void drawCircle (int i, int j) {
        Circle circle = new Circle();
        base_square.getChildren().add(circle);
        circle.setCenterX(i * BOUND + BOUND / 2.0 + OFFSET);
        circle.setCenterY(j * BOUND + BOUND / 2.0 + OFFSET);
        circle.setRadius(BOUND / 2.0 - OFFSET / 2.0);
        circle.setStroke(Color.RED);
        circle.setFill(Color.TRANSPARENT);
        flag[i][j] = true;
    }

    private void drawLine (int i, int j) {
        Line line_a = new Line();
        Line line_b = new Line();
        base_square.getChildren().add(line_a);
        base_square.getChildren().add(line_b);
        line_a.setStartX(i * BOUND + OFFSET * 1.5);
        line_a.setStartY(j * BOUND + OFFSET * 1.5);
        line_a.setEndX((i + 1) * BOUND + OFFSET * 0.5);
        line_a.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_a.setStroke(Color.BLUE);

        line_b.setStartX((i + 1) * BOUND + OFFSET * 0.5);
        line_b.setStartY(j * BOUND + OFFSET * 1.5);
        line_b.setEndX(i * BOUND + OFFSET * 1.5);
        line_b.setEndY((j + 1) * BOUND + OFFSET * 0.5);
        line_b.setStroke(Color.BLUE);
        flag[i][j] = true;
    }

    public void setAble(boolean ABLE) {
        this.ABLE = ABLE;
    }

    public boolean getAble(){
        return this.ABLE;
    }

    public void setWAIT(boolean wait) {
        this.WAIT = wait;
    }

        public void setId(int i) {
        this.id=i;
    }
//
    public int getId(){
        return this.id;
    }

    public  int[][] getChessBoard(){
        int [][]board=chessBoard;
        return board;
    }
//    public void setNameField(String s){
//        this.nameField=new TextField(s);
//    }
//
//    public TextField getNameField() {
//        return nameField;
//    }
}

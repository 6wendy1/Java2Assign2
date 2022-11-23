package application.client;

import application.server.Server;
import application.ui.BoardController;
import javafx.application.Platform;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Client extends Thread{

  private Socket socket = null;
  private String name;
  private DataInputStream ds;
  public PrintStream ps;
  private boolean linked=false;
  private boolean turn=false;
  public BoardController controller;
  public static boolean gameover=false;
  public Client(){
        linkServer();
    }
  public int  anotherId=0;

  public int getAnotherId() {
        return anotherId;
    }

  public void setAnotherId(int anotherId) {
        this.anotherId = anotherId;
    }

  public static void setGameover(boolean gameover) {
        Server.gameover = gameover;
    }
  public void linkServer() {
//        name = chess.getNameField().getText();
        if (socket == null) {
            try {
                socket = new Socket(InetAddress.getLocalHost(), 10088);
                if (socket != null) {
                    ps = new PrintStream(socket.getOutputStream());
                    ds=new DataInputStream(socket.getInputStream());
//                    ps.println("LINK:" + name);
                    ps.flush();
                }
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    public void passInfo(){
//        if (!turn){
//
//        }
//    }
//
//    public void setLinked(boolean linked) {
//        this.linked = linked;
//    }
//    public boolean isLinked() {
//        return linked;
//    }

//    public void matchButtonClick(){
//        //发送匹配消息MATCH到服务器
//        if (socket != null){
//            ps.println("MATCH");
//            ps.flush();
//        }
//    }
//
//    public void quitButtonClick(){
//        //发送匹配消息QUIT到服务器
//        if (socket != null){
//            ps.println("QUIT");
//            ps.flush();
////            chess.getLinkInfo().setText("退出连接");
//        }
//    }

    @Override
    public void run() {
        while (true) {
            String line = null;
            try {
                line = ds.readLine();//客户端收到的信息
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (line != null) {
                System.out.println("Receive: " + line);
                if (line.equalsIgnoreCase("MATCH!YOU GO FIRST!")){
                    System.out.println("Begin!");
                    controller.setId(1);
                    this.anotherId=2;
                    System.out.println(controller.getId());
                    controller.setAble(true);
                    System.out.println(controller.getAble());
                    controller.setWAIT(false);
                }else if (line.equalsIgnoreCase("MATCH")){
                    System.out.println("Begin!");
                    controller.setId(2);
                    this.anotherId=1;
//                    System.out.println(controller.getId());
                    controller.setAble(false);
                    System.out.println(controller.getAble());
                }
                else if (line.equalsIgnoreCase("Continue")){
//                    System.out.println("1 CONTINUED");
                    controller.setAble(true);
                    controller.setWAIT(false);
                } else if (line.startsWith("[i,x,y,m]")) {
                    String []s=line.split(":");
//                    for (int i = 0; i <s.length ; i++) {
//                        System.out.println("zhelishi"+s[i]);
//                    }
                    String infor=s[1];
                    String finalAnotherid = String.valueOf(this.anotherId);
                    Platform.runLater(() -> {
                        controller.setCell(infor);
                        boolean test=controller.judgeMainDiagonal(infor);
                        System.out.println("judge"+test);
                        if (controller.judgeCol(infor)||controller.judgeRow(infor)||controller.judgeDeputyDiagonal(infor)||controller.judgeMainDiagonal(infor)){
//                            System.out.println("You are win!");
                            ps.println("66666");
                            ps.flush();
                            ps.println("Winner:"+ finalAnotherid);
                            ps.flush();
                            System.out.println("You are lose!");
//                            ps.println("You are lose!");
//                            ps.flush();
                        }else if(controller.judgeEqual()){
//                            System.out.println("End in a draw!");
                            ps.println("End in a lose!");
                            ps.flush();
                        }
                    });
                }
//
            }
        }
    }

}





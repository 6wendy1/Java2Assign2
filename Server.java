package application.server;

import javax.sound.sampled.Port;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {
    public static boolean gameover = false;
    public static final int port = 10088;
    ServerSocket server;
    private Socket socket = null;//用来指代客户的socket
    PrintStream ps;
    DataInputStream ds;

    private List<Player> players=new ArrayList<>();//负责存储各个客户端的输入流输出流和name
    private Link link;//负责连接的线程
    private Connection connection;
    public PlayerConnection pc=new PlayerConnection();

//    private PlayerConnection pc;




    public Server() throws IOException {
    }

    public void init() {
        players = new Vector<>();   //构造线程安全的Vector
        try {
            server = new ServerSocket(port);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Server is open on port " + port);
        link = new Link();
        Thread t = new Thread(link);
        t.start();
        connection = new Connection();
        connection.start();
    }

    public class Link implements Runnable {//在调用的时候需要唤醒线程

        public void run() {
            while (true) {
                if (players.size() < 10) {
                    try {
                        socket = server.accept();//是server端的端口
                        System.out.print("client found: ");
                        System.out.println(socket);
                        Player player = new Player(socket);
                        synchronized (new Link()) {
                            players.add(player);
                        }
//                        System.out.println(players.size());
//                        System.out.println(player.name);
                        player.send("LINK:SUCCESS");//客户端收到信息
                        player.send("PLEASE WAIT!");
                        player.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    //    public void sendLinked(){
//        ps.println("Connected successfully!");
//        ps.flush();
//    }


    class Connection extends Thread {
        public void run() {
            Player player1 = null;
            Player player2 = null;
            while (true) {
                synchronized(new Connection()) {
                                        for (Player player : players) {
                        if (player.getStatus() == 0) {
                            player1 = player;
                            pc.setPlayer1(player);
                            break;
                        }
                    }
                    for (Player player : players) {
                        if (player.getStatus() == 0 && player != player1) {
                            player2 = player;
                            pc.setPlayer2(player);
                            break;
                        }
                    }
                    if (player1 != null && player2 != null)
                        break;
                }
            }
            System.out.println(player1 + " ----- player2 " + player2);
            player1.setStatus(2);
//            player1.setTurn(1);
            player1.send("MATCH!YOU GO FIRST!");
//            System.out.println(player1.getOwnport());
            player2.setStatus(2);
//            player2.setTurn(2);
            player2.send("MATCH");
//            System.out.println(player2.getOwnport());
//            while (true) {
                //if (player1 != null && player2 != null) {
//                    compositions.add(new Composition(player1, player2));

//                    try {//test
//                        System.out.println("player1:"+player1.receive());
//                        System.out.println("player2:"+player2.receive());
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                    System.out.println(player1.getFinish() + " " + player2.getFinish());
        }
    }

//    class playControl extends Thread{
//        public void run(){
//            while (true){
//                synchronized (new playControl()){
//                    for (Player player : players) {
//                        if (player.getTurn() == 1) {
//
//                        }else if (player.getTurn() == 2){
//
//                        }else{
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//    }


    public class Player extends Thread {//server分出的协程去和客户端通信
        Socket s;
        String name;
        InetAddress ip;
        int port;
        PrintStream ps;//服务器发出的信息
        DataInputStream ds;//服务器收到的信息
        int status = 0;     //0表示未点击匹配的，1表示点击匹配的, 2表示匹配成功
        //        public static final int port=8888;
        private int turn;//初始为0，1表示先手，2表示后手

        private boolean FINISH;
        String line = null;


        public Player(Socket s) throws IOException {
            try {
                ps = new PrintStream(s.getOutputStream());
                ds = new DataInputStream(s.getInputStream());
                this.ip = s.getLocalAddress();
                this.s = s;
                this.port=s.getPort();
            } catch (IOException e) {
                e.printStackTrace();
            }
            turn = 0;
        }

        /**
         * player线程主要工作：负责对应客户端的通信任务（匹配，更新棋盘）
         */
        public void run() {
            while (true) {
                try {
                    line = ds.readLine();//服务器收到的信息
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (line != null) {
                    System.out.println("playerserver:"+line);
                    if (line.equalsIgnoreCase("1:finish")){
                        pc.getPlayer1().setFinish(true);
                        pc.player1Finish();
                    }else if (line.equalsIgnoreCase("2:finish")){
                        pc.getPlayer2().setFinish(true);
                        pc.player2Finish();
                    }else if(line.startsWith("[i,x,y,m]")){
                        String []ss=line.split(":");
                        String id=ss[1];
                        String value=ss[2];
                        if (id.equalsIgnoreCase("1")){
                            pc.getPlayer1().send("[i,x,y,m]:2,"+value);
//                            System.out.println("yes");
                        }else {
                            pc.getPlayer2().send("[i,x,y,m]:1,"+value);
                        }
                    }else if (line.startsWith("Winner")){
                        System.out.println("666:"+line);
                        String []ss=line.split(":");
                        String idd=ss[1];
                        System.out.println(ss[1]);
                        if (idd.equalsIgnoreCase("1")){
                            System.out.println("winnerid:"+idd);
                            pc.getPlayer1().send("You(player1) are win!");
                            pc.getPlayer1().send("GameOver");
                            pc.getPlayer2().send("GameOver");

//                            System.exit(0);
                        }
                        else if (idd.equals("2")){
                                System.out.println("winnerid:"+idd);
                                pc.getPlayer2().send("You(player2) are win!");
                            pc.getPlayer1().send("GameOver");
                            pc.getPlayer2().send("GameOver");


//                            System.exit(0);
                    }
                    }else if (line.startsWith("End")){
                        pc.getPlayer2().send("End in a lose!");
                        pc.getPlayer1().send("End in a lose!");
                        pc.getPlayer1().send("GameOver");
                        pc.getPlayer2().send("GameOver");
//                        System.exit(0);
                    }
                }
            }
        }

        public String getLine(){
            if (line!=null) return line;
            else return "receive wrong";
        }

//        public String receive() throws IOException {
//            if (line!=null) return line;
//            else return "receive wrong";
//        }

        public void send(String msg) {//向客户端发送的信息
            ps.println(msg);
            ps.flush();
        }






        public void close() {
            try {
                ds.close();
                ps.close();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public int getTurn() {
            return turn;
        }

        public void setTurn(int turn) {
            this.turn = turn;
        }

        public InetAddress getIp() {
            return ip;
        }

        public int getOwnport() {
            return port;
        }

        public void setFinish(boolean b){
            this.FINISH=b;

        }
        public boolean getFinish(){
            return this.FINISH;
        }

        @Override
        public String toString() {
            return "Player{" +
                    "s=" + s +
                    ", name='" + name + '\'' +
                    ", ip=" + ip +
                    ", ps=" + ps +
                    ", ds=" + ds +
                    ", status=" + status +
                    '}';
        }


    }


    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.init();
    }


}

package application.server;

public class PlayerConnection{
    private Server.Player player1;
    private Server.Player player2;

    public void setPlayer1(Server.Player player){
        player1 = player;
    }

    public Server.Player getPlayer1() {
        return player1;
    }

    public void setPlayer2(Server.Player player){
        player2 = player;
    }
    // p1 finished
    public Server.Player getPlayer2() {
        return player2;
    }

    public void player1Finish() {
        if (player1.getFinish()) {
            System.out.println(player1.getFinish());
            System.out.println("player1:ok");
            player2.send("Continue");
            System.out.println("send successfully");
            player1.setFinish(false);
        }
    }
        public void player2Finish() {
            if (player2.getFinish()) {
                System.out.println("player2:ok");
                player1.send("Continue");
                player2.setFinish(false);
            }
        }

    }

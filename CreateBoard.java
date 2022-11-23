package application.ui;

import application.client.Client;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CreateBoard extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
//            Client client1=new Client();
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getClassLoader().getResource("mainUI.fxml"));
            Pane root = fxmlLoader.load();


            BoardController controller = fxmlLoader.getController();
            Client client = new Client();
            client.controller = controller;
            client.controller.client = client;
            client.start();


//            if (controller.getId()==1){
//                controller.setNameField("player 1");
//            }else if(controller.getId()==2){
//                controller.setNameField("player 1");
//            }else {
//                controller.setNameField("not match");
//            }
            primaryStage.setTitle("Tic Tac Toe");
            primaryStage.setScene(new Scene(root));
            primaryStage.setResizable(false);
            primaryStage.show();

//            client1.ps.println("woshi client");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) {
//        launch(args);
//    }
}

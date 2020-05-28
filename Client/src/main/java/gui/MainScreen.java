package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro8.JMetro;


public class MainScreen extends Application {
    private Stage stage;




    @Override
    public void start(Stage primaryStage) throws Exception  {
        Parent root;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("views/mainscreen.fxml"));
        primaryStage.setTitle("Dashboard - Go Green");

        //Applies Light theme to mainscreen
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        System.out.println("Theme set to JMetro Light");

        Scene scene = new Scene(root, 1280, 720);
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args)  {
        launch(args);
    }

}

package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.styles.jmetro8.JMetro;


public class LoginScreen extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception  {
        Parent root;
        root = FXMLLoader.load(getClass()
                .getClassLoader().getResource("views/loginscreen_alt.fxml"));
        primaryStage.setTitle("Hello World");

        //Applies Light theme to mainscreen
        //new JMetro(JMetro.Style.LIGHT).applyTheme(root);
        System.out.println("Theme set to JMetro Light");

        primaryStage.setScene(new Scene(root, 1150, 600));
        System.out.println(root.getStylesheets());

        primaryStage.initStyle(StageStyle.UNDECORATED);
        new JMetro(JMetro.Style.LIGHT).applyTheme(root);

        primaryStage.show();
    }

    public static void main(String[] args)  {
        launch(args);
    }

}

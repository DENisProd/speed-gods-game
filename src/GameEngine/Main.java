package GameEngine;

import GameEngine.Scenes.*;
import javafx.application.Application;
import javafx.scene.*;
import javafx.stage.Stage;


public class Main extends Application {

    public int width = 1280;
    public int height = 720;

    private static Stage window;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        window = primaryStage;

        window.setTitle("Speed God");
        window.setResizable(false);
        window.setMaxWidth(width);
        window.setMaxHeight(height);
        //window.setFullScreen(true);
        //window.setMaximized(true);
        window.setScene(new Menu(new Group()));
        window.show();
    }

    public static double getWidth()
    {
        return window.getWidth();
    }
    public static double getHeight()
    {
        return window.getHeight();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void setScene(Scene scene)
    {
        window.setScene(scene);
    }
}

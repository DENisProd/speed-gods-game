package GameEngine.Scenes;

import GameEngine.Main;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;

public class Menu extends Scene {

    private double width, height;

    public Menu(Group root) {
        super(root, Main.getWidth(), Main.getHeight());

        width = Main.getWidth();
        height = Main.getHeight();

        Image bg = new Image("/Resources/Screens/Background.png");
        ImageView background = new ImageView();
        background.setImage(bg);
        root.getChildren().add(background);

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                Main.setScene(new Garage(new Group()));
            }
        });
    }
}

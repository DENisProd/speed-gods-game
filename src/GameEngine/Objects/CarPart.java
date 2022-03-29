package GameEngine.Objects;

import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.*;

public class CarPart extends ImageView {

    public CarPart(Image img, int xPos, int yPos)
    {
        this.setImage(img);
        this.setX(xPos);
        this.setY(yPos);
        this.setFitWidth(img.getWidth());
        this.setFitHeight(img.getHeight());
    }
}

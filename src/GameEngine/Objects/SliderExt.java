package GameEngine.Objects;

import com.sun.javafx.scene.control.skin.SliderSkin;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.control.Skin;
import javafx.scene.control.Slider;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Paint;

import java.lang.reflect.Field;

public class SliderExt extends Slider {

    private Paint leftTopFill;

    private Paint rightBottomFill;

    private StackPane track;

    private StackPane thumb;

    public SliderExt(Paint leftTopFill, Paint rightBottomFill) {
        super();
        initialize(leftTopFill, rightBottomFill);
    }

    public SliderExt(double min, double max, double value, Paint leftTopFill, Paint rightBottomFill) {
        super(min, max, value);
        initialize(leftTopFill, rightBottomFill);
    }

    private void initialize(Paint leftTopFill, Paint rightBottomFill) {
        this.leftTopFill = leftTopFill;
        this.rightBottomFill = rightBottomFill;
        valueProperty().addListener(obv -> updateTrackBackground());
    }

    @Override
    protected Skin<?> createDefaultSkin() {
        Skin skin = super.createDefaultSkin();
        initTrackAndThumb((SliderSkin)skin);
        return skin;
    }

    private void initTrackAndThumb(SliderSkin skin) {
        try {
            Field trackField = SliderSkin.class.getDeclaredField("track");
            trackField.setAccessible(true);
            track = (StackPane) trackField.get(skin);

            Field thumbField = SliderSkin.class.getDeclaredField("thumb");
            thumbField.setAccessible(true);
            thumb = (StackPane) thumbField.get(skin);
        } catch ( NoSuchFieldException | IllegalAccessException ex ) {
            throw new RuntimeException("", ex);
        }
        updateTrackBackground();
    }

    private void updateTrackBackground() { updateTrackBackground(track, thumb, leftTopFill, rightBottomFill); }

    protected void updateTrackBackground(StackPane track, StackPane thumb, Paint leftTopFill, Paint rightBottomFill) {
        Platform.runLater(() -> {
            Insets leftTopInsets;
            Insets rightBottomInsets;
            if ( getOrientation() == Orientation.HORIZONTAL ) {
                leftTopInsets = new Insets(0, track.getWidth() - thumb.getLayoutX(), 0, 0);
                rightBottomInsets = new Insets(0, 0, 0, thumb.getLayoutX());
            } else {
                leftTopInsets = new Insets(0, 0, track.getHeight() - thumb.getLayoutY(), 0);
                rightBottomInsets = new Insets(thumb.getLayoutY(), 0, 0, 0);
            }

            BackgroundFill leftTopBackground = new BackgroundFill(leftTopFill, null, leftTopInsets);
            BackgroundFill rightBottomBackground = new BackgroundFill(rightBottomFill, null, rightBottomInsets);

            track.setBackground(new Background(leftTopBackground, rightBottomBackground));
        });
    }

}

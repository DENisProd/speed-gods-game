package GameEngine.Scenes;

import GameEngine.Main;
import GameEngine.Objects.Car;
import GameEngine.Objects.SliderExt;
import javafx.animation.AnimationTimer;
import javafx.animation.FadeTransition;
import javafx.geometry.Side;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.beans.value.*;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class Garage extends Scene {

    private Car car;

    private Image background;
    private ImageView bgView;
    private CheckBox checkBox;

    private double width, height;

    Race race;

    private static final int ADJUST_TYPE_HUE = 1;
    private static final int ADJUST_TYPE_CONTRAST = 2;
    private static final int ADJUST_TYPE_SATURATION = 3;

    public Garage(Group root) {
        super(root, Main.getWidth(), Main.getHeight());

        width = Main.getWidth();
        height = Main.getHeight();

        Rectangle rectangle = new Rectangle();
        rectangle.setOpacity(0);
        rectangle.setHeight(height);
        rectangle.setWidth(width);
        rectangle.setFill(Color.BLACK);
        rectangle.setVisible(false);
        //rectangle.se

        Label contrastLabel = new Label("Contrast:");
        Label hueLabel = new Label("Hue:");
        Label saturationLabel = new Label("Saturation:");

        contrastLabel.setFont(new Font("Arial", 16));
        contrastLabel.setTextFill(Color.web("63a68b"));
        hueLabel.setFont(new Font("Arial", 16));
        hueLabel.setTextFill(Color.web("#63a68b"));
        saturationLabel.setFont(new Font("Arial", 16));
        saturationLabel.setTextFill(Color.web("#63a68b"));

        background = new Image("/Resources/Screens/GarageBG3.png");

        Slider contrastSlider = this.createSlider(ADJUST_TYPE_CONTRAST, 0, 1);
        Slider hueSlider = this.createSlider(ADJUST_TYPE_HUE,-1,1);
        Slider saturationSlider = this.createSlider(ADJUST_TYPE_SATURATION,-1,1);

        bgView = new ImageView();
        bgView.setImage(background);
        bgView.setFitWidth(Main.getWidth());
        bgView.setFitHeight(Main.getHeight());

        car = new Car("Vaz2109",1, 2, 35,(int)(width/3.5), (int)height/2, true);
        car.setScaleY(height/260/2.5);
        car.setScaleX(width/600/1.9);
        car.setGarage(true);

        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.W) {
                car.setGarage(false);
                car.playAudio("StartGo.wav");
                rectangle.setVisible(true);
                new AnimationTimer() {
                    int secondss = 0;
                    private long lastTime = 0;

                    public void handle(long currentNanoTime) {

                        if (currentNanoTime > lastTime + 1_000_000_000) {
                            secondss++;
                            lastTime = currentNanoTime;
                        }
                        if(secondss>=4)
                        {
                            car.stopAudio();
                            race = new Race(new Group(),car);
                            Main.setScene(race);
                            //race.setCar(car);
                            this.stop();
                        }
                    }
                }.start();
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(3.0),rectangle);
                fadeTransition.setFromValue(0);
                fadeTransition.setByValue(1);
                fadeTransition.play();
            }});

        VBox root1 = new VBox();
        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.setTabMinHeight(20);
        tabPane.setTabMinWidth(40);
        tabPane.setLayoutX(785);
        tabPane.setLayoutY(175);
        tabPane.setStyle("-fx-background-color: 3D3D3D;" +
                         ".tab{-fx-background-color: 696969;}" +
                         ".tab:selected{-fx-background-color: 696969}" +
                         ".tab:focused{-fx-background-color: transperent;}" +
                         ".tab-pane * .tab-header-background{-fx-background-color: transperent;}");
        Tab tabP = new Tab("Цвет");

        Group rootP = new Group(); tabP.setContent(rootP);

        root1.setPadding(new Insets(5,10,10,10));

        root1.getChildren().addAll(contrastLabel, contrastSlider,
                hueLabel, hueSlider,
                saturationLabel, saturationSlider);

        rootP.getChildren().addAll(root1);

        Tab tabN = new Tab("Тип краски");

        Group rootN = new Group(); tabN.setContent(rootN);

        checkBox = new CheckBox("Матовый цвет");
        checkBox.setOpacity(1);
        checkBox.setStyle("-fx-background-color:gray");
        checkBox.setPadding(new Insets(5,5,5,5));
        rootN.getChildren().add(checkBox);

        tabPane.getTabs().addAll(tabP,tabN);
        root.getChildren().addAll(bgView,car, tabPane, rectangle);
    }

    private Slider createSlider(final int adjustType,int min,int max) {
        Slider slider = new SliderExt(min,max,0,Color.web("bbbbbb"), Color.web("222222"));
        slider.setBlockIncrement(0.1);
        slider.setScaleY(1.5);

        slider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observable, //
                                Number oldValue, Number newValue) {
                ColorAdjust colorAdjust = new ColorAdjust();
                switch (adjustType) {
                    case ADJUST_TYPE_HUE:
                        //car.getColorAdjust().setHue(newValue.doubleValue());
                        colorAdjust.setHue(newValue.doubleValue());

                        colorAdjust.setContrast(car.getColorAdjust(car.getPartPaintName()).getContrast());
                        colorAdjust.setSaturation(car.getColorAdjust(car.getPartPaintName()).getSaturation());
                        car.setColorAdjust(car.getPartPaintName(),colorAdjust);
                        break;
                    case ADJUST_TYPE_CONTRAST:
                        colorAdjust.setContrast(newValue.doubleValue());
                        if(checkBox.isSelected()) {
                                car.getBloom(car.getPartPaintName()).setThreshold(1 - (newValue.doubleValue()));
                        }else{
                            car.getBloom(car.getPartPaintName()).setThreshold(1);
                        }
                        colorAdjust.setHue(car.getColorAdjust(car.getPartPaintName()).getHue());
                        colorAdjust.setSaturation(car.getColorAdjust(car.getPartPaintName()).getSaturation());

                        car.setColorAdjust(car.getPartPaintName(),colorAdjust);
                        break;
                    case ADJUST_TYPE_SATURATION:
                        colorAdjust.setSaturation(newValue.doubleValue());

                        colorAdjust.setContrast(car.getColorAdjust(car.getPartPaintName()).getContrast());
                        colorAdjust.setHue(car.getColorAdjust(car.getPartPaintName()).getHue());
                        car.setColorAdjust(car.getPartPaintName(),colorAdjust);
                        break;
                }
            }
        });
        return slider;
    }
    public Car getCar()
    {
        return car;
    }
}

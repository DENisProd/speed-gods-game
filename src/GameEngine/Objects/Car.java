package GameEngine.Objects;

import GameEngine.Controllers.ColorController;
import javafx.scene.Group;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.*;

import java.io.File;
import java.io.IOException;

public class Car extends Group {

    private static ImageView tireTrackView, carBottomView, carBrakeDiskView, carBrakeDiskView2,
            carWheelView, carWheelView2, carBodyView, carGlassView, carGlassBackView,
            carTuningView, carLightsView, carBrakeLampView;
    private Image carImg, carBottom, carGlassBack, carGlass, carLights, carBrakeLamp;

    private int xDefault;

    private Image[] carWheel, carBrakeDisk, carTuning;
    private Image tireTrack;

    private Bloom bloom, bloom1;

    private String path = "/Resources/Cars/";
    private String absolPath;
    private String carName;
    private String partPaintName = "Body";

    private MediaPlayer carStart;
    private MediaPlayer carBrake;
    double contr;
    double hue;
    double sat;
    private boolean isGarage = false;
    //AudioClip carStart;

    private ColorController controller;

    public Car(String name, int tun, int wheel, int landing, int xPos, int yPos, boolean isGarage) {
        xDefault = xPos;
        this.isGarage = isGarage;
        carName = name;

        carWheel = new Image[10];
        carBrakeDisk = new Image[10];
        carTuning = new Image[10];
        bloom = new Bloom();
        bloom.setThreshold(1.0);
        bloom1 = new Bloom();
        bloom1.setThreshold(1.0);

        carWheel[0] = new Image("/Resources/Cars/Wheels/Wheel1.png");
        carWheel[1] = new Image("/Resources/Cars/Wheels/Wheel2.png");
        carWheel[2] = new Image("/Resources/Cars/Wheels/Wheel3.png");
        carBrakeDisk[0] = new Image("/Resources/Cars/Wheels/BrakeDisk1.png");
        carBrakeDisk[1] = new Image("/Resources/Cars/Wheels/BrakeDisk1.png");
        carBrakeDisk[2] = new Image("/Resources/Cars/Wheels/BrakeDisk3.png");
        carTuning[0] = new Image(path + name + "/" + name + "_Tuning0.png");
        carTuning[1] = new Image(path + name + "/" + name + "_Tuning1.png");

        tireTrack = new Image("/Resources/Other/TireTrack.png");

        carBottom = new Image(path + name + "/" + name + "_Bottom.png");
        carImg = new Image(path + name + "/" + name + ".png");
        carGlass = new Image(path + name + "/" + name + "_Glass.png");
        carGlassBack = new Image(path + name + "/" + name + "_GlassBack.png");
        carLights = new Image(path + name + "/" + name + "_Lights.png");
        carBrakeLamp = new Image(path + name + "/" + name + "_BrakeLamp.png");

        absolPath = getClass().getResource(path+name+"/").getPath();

        tireTrackView = new CarPart(tireTrack, 0, yPos + 225-landing);
        tireTrackView.setVisible(false);
        tireTrackView.setFitWidth(xPos+500);

        carBottomView = new CarPart(carBottom, xPos, yPos);
        carBrakeDiskView = new CarPart(carBrakeDisk[wheel], xPos + 62, yPos + 148 - landing);
        carBrakeDiskView2 = new CarPart(carBrakeDisk[wheel], xPos + 445, yPos + 148 - landing);
        carWheelView = new CarPart(carWheel[wheel], xPos + 62, yPos + 148 - landing);
        carWheelView2 = new CarPart(carWheel[wheel], xPos + 445, yPos + 148 - landing);
        carWheelView2.setRotate(85);
        carBodyView = new CarPart(carImg, xPos, yPos);
        carGlassView = new CarPart(carGlass, xPos, yPos);
        carGlassBackView = new CarPart(carGlassBack, xPos, yPos);
        carTuningView = new CarPart(carTuning[tun], xPos, yPos);
        carLightsView = new CarPart(carLights, xPos, yPos);

        update();

        Group groupBodyView = new Group();
        groupBodyView.getChildren().add(carBodyView);
        groupBodyView.setEffect(bloom);
        Group groupTuningView = new Group();
        groupTuningView.getChildren().add(carTuningView);
        groupTuningView.setEffect(bloom1);

        carBrakeLampView = new CarPart(carBrakeLamp, xPos, yPos);
        carBrakeLampView.setVisible(false);

        this.getChildren().addAll(tireTrackView, carBottomView, carBrakeDiskView, carBrakeDiskView2,
                carWheelView, carWheelView2, groupBodyView, carGlassBackView, carGlassView, groupTuningView,
                carLightsView, carBrakeLampView);
    }

    public void update()
    {
        if(isGarage) {
            carBodyView.setOnMouseEntered((MouseEvent event) -> {
                carBodyView.setEffect(createOnMouseEntered("Body"));
                event.consume();
            });
            carBodyView.setOnMouseExited((MouseEvent event) -> {
                carBodyView.setEffect(createOnMouseExited());
                event.consume();
            });
            carBodyView.setOnMouseClicked((MouseEvent event) -> {
                partPaintName = "Body";
                event.consume();
            });

            carTuningView.setOnMouseEntered((MouseEvent event) -> {

                carTuningView.setEffect(createOnMouseEntered("Bumpers"));
                event.consume();
            });
            carTuningView.setOnMouseExited((MouseEvent event) -> {

                carTuningView.setEffect(createOnMouseExited());
                event.consume();
            });
            carTuningView.setOnMouseClicked((MouseEvent event) -> {
                partPaintName = "Bumpers";
                event.consume();
            });
        }
    }

    public void upd()
    {

    }

    public void setBottomImage(Image img) {
        carBottomView.setImage(img);
    }

    public void setBodyImage(Image img) {
        carBodyView.setImage(img);
    }

    public void setWheelsImage(int num) {
        carWheelView.setImage(carWheel[num]);
        carWheelView2.setImage(carWheel[num]);
    }

    public void setGlassImage(Image img) {
        carGlassView.setImage(img);
    }

    public void setBrakeDiskImage(int num) {
        carBrakeDiskView.setImage(carBrakeDisk[num]);
        carBrakeDiskView.setImage(carBrakeDisk[num]);
    }

    public double getCarY() {
        return carBodyView.getY();
    }

    public ColorAdjust createOnMouseEntered(String name)
    {
        try {
            ColorAdjust colorAdjust1 = new ColorAdjust();
            contr = getColorAdjust(name).getContrast();
            sat = getColorAdjust(name).getSaturation();
            hue = getColorAdjust(name).getHue();
            colorAdjust1.setContrast((getColorAdjust(name).getContrast())-0.2);
            colorAdjust1.setSaturation((getColorAdjust(name).getSaturation())-0.2);
            colorAdjust1.setHue((getColorAdjust(name).getHue())-0.2);
            return colorAdjust1;
        }catch (Exception e) {
            //e.printStackTrace();
        }
        return null;
    }

    public ColorAdjust createOnMouseExited()
    {
        ColorAdjust colorAdjust1 = new ColorAdjust();
        colorAdjust1.setContrast(contr);
        colorAdjust1.setSaturation(sat);
        colorAdjust1.setHue(hue);
        return  colorAdjust1;
    }

    public double getWheelsRotate() {
        return carWheelView.getRotate();
    }

    public void setWheelsRotate(double rotate) {
        double rot1 = carWheelView.getRotate();
        double rot2 = carWheelView2.getRotate();
        carWheelView.setRotate(rot1 + rotate);
        carWheelView2.setRotate(rot2 + rotate);
    }

    public void carMoveY(float move) {
        carBottomView.setY(carBottomView.getY() + move);
        carBodyView.setY(carBodyView.getY() + move);
        carBrakeDiskView.setY(carBrakeDiskView.getY() + move);
        carBrakeDiskView2.setY(carBrakeDiskView2.getY() + move);
        carWheelView.setY(carWheelView.getY() + move);
        carWheelView2.setY(carWheelView2.getY() + move);
        carGlassView.setY(carGlassView.getY() + move);
        carTuningView.setY(carTuningView.getY() + move);
        carLightsView.setY(carLightsView.getY() + move);
        carGlassBackView.setY(carGlassBackView.getY() + move);
        carBrakeLampView.setY(carBrakeLampView.getY() + move);
        tireTrackView.setY(tireTrackView.getY() + move);
    }

    public void carMoveX(int move) {
        carBottomView.setX(carBottomView.getX() + move);
        carBodyView.setX(carBodyView.getX() + move);
        carBrakeDiskView.setX(carBrakeDiskView.getX() + move);
        carBrakeDiskView2.setX(carBrakeDiskView2.getX() + move);
        carWheelView.setX(carWheelView.getX() + move);
        carWheelView2.setX(carWheelView2.getX() + move);
        carGlassView.setX(carGlassView.getX() + move);
        carGlassBackView.setX(carGlassBackView.getX() + move);
        carTuningView.setX(carTuningView.getX() + move);
        carLightsView.setX(carLightsView.getX() + move);
        carBrakeLampView.setX(carBrakeLampView.getX() + move);
        tireTrackView.setX(tireTrackView.getX() + move);
    }

    public void carX(int move) {
        carBottomView.setX(move);
        carBodyView.setX(move);
        carBrakeDiskView.setX(move);
        carBrakeDiskView2.setX(move);
        carWheelView.setX(move);
        carWheelView2.setX(move);
        carGlassView.setX(move);
        carGlassBackView.setX(move);
        carTuningView.setX(move);
        carLightsView.setX(move);
        carBrakeLampView.setX(move);
        tireTrackView.setX(move);
    }

    public void brake(boolean brake) {
        if (brake) {
            //tireTrackView.setVisible(true);
            carBrakeLampView.setVisible(true);
            //playAudio("Brake.wav");
        }
        if (!brake) {
            tireTrackView.setVisible(false);
            carBrakeLampView.setVisible(false);
            //carStart.play();
        }
    }

    public String getPartPaintName()
    {
        return partPaintName;
    }

    public void setLanding(int landing) {
        carBrakeDiskView.setY(carBrakeDiskView.getY() - landing);
        carBrakeDiskView2.setY(carBrakeDiskView2.getY() - landing);
        carWheelView.setY(carWheelView.getY() - landing);
        carWheelView2.setY(carWheelView2.getY() - landing);
        tireTrackView.setY(tireTrackView.getY() - landing);
    }

    public void setColorAdjust(String part, ColorAdjust colorAdjust1) {
        if (part == "Body") {
            carBodyView.setEffect(colorAdjust1);
        }
        if (part == "Bumpers") {
            carTuningView.setEffect(colorAdjust1);
        }
    }

    public ColorAdjust getColorAdjust(String part)
    {
        if (part == "Body") {
            return (ColorAdjust) carBodyView.getEffect();
        }
        if (part == "Bumpers") {
            return (ColorAdjust) carTuningView.getEffect();
        }else{
            return null;
        }
    }

    public void playAudio(String file) {
            Media sound = new Media(new File(absolPath+file).toURI().toString());
            carStart = new MediaPlayer(sound);
            carStart.play();
        //mediaPlayer.play();
        //AudioClip soundMyNoise = new AudioClip(new File("noises/roll.wav").toURI().toString());
        //carStart = new AudioClip(absolPath+file);
    }

    public void stopAudio()
    {
        carStart.stop();
    }

    public Bloom getBloom(String part)
    {
        if (part == "Body") {
            return bloom;
        }
        if (part == "Bumpers") {
            return bloom1;
        }else{
            return null;
        }
    }
    public ImageView getCarBodyView()
    {
        return carBodyView;
    }

    public void setGarage(boolean garage)
    {
        isGarage = garage;
    }
}

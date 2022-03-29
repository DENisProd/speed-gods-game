package GameEngine.Scenes;

import GameEngine.Main;
import GameEngine.Objects.Car;
import GameEngine.Objects.CarPart;
import javafx.animation.AnimationTimer;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Race extends Scene {

    private ImageView trackBackView, trackView, speedBottomView, speedLineView, speedTopView;
    private ArrayList<ImageView> coins;
    private Image track, trackBack, speedBottom, speedLine, speedTop, coin;
    private Car car;
    private double wheelRotate;
    private Random random;
    private int coinsCount;
    private int[] coinsCoordsY;
    private int[] coinsCoordsX;
    private float speed;
    private float speedY;
    private int seconds;
    private double width, height;
    private MotionBlur motionBlur;
    private boolean isDrive = false;
    private boolean isRest;
    private Group trackGroup;

    private boolean wPressed = false;
    private boolean upPressed = false;
    private boolean sPressed = false;
    private boolean downPressed = false;

    private double hue, contrast, saturation, brightness;



    public Race(Group root) {
        super(root, Main.getWidth(), Main.getHeight());

        width = Main.getWidth();
        height = Main.getHeight();

        init(root);
    }

    public Race(Group root, Car car) {
        super(root, Main.getWidth(), Main.getHeight());

        width = Main.getWidth();
        height = Main.getHeight();

        this.car = car;

        init(root);
    }

    public Race(Group root, Car car, boolean isRestart) {
        super(root, Main.getWidth(), Main.getHeight());

        isRest = isRestart;
        width = Main.getWidth();
        height = Main.getHeight();

        this.car = car;

        init(root);
    }

    private void init(Group root)
    {
        trackView = new ImageView();
        trackBackView = new ImageView();

        motionBlur = new MotionBlur();
        motionBlur.setRadius(0);
        motionBlur.setAngle(-180);

        random = new Random();
        coinsCount = random.nextInt(50);
        System.out.println(coinsCount);
        coinsCoordsY = new int[coinsCount];
        coinsCoordsX = new int[coinsCount];
        coins = new ArrayList<ImageView>();

        for(int i=0;i<coinsCount;i++)
        {
            int nom = random.nextInt(2)+1;
            Image img = new Image("/Resources/Other/Coin"+(nom*5)+".png");
            coins.add(new CarPart(img,random.nextInt(300)*100,100+(random.nextInt(5)*100)));
            coins.get(i).setEffect(motionBlur);
        }

        track = new Image("/Resources/Tracks/Track2.png");
        trackBack = new Image("/Resources/Tracks/Track2_Back.png");
        speedBottom = new Image("/Resources/Other/Speed_Bottom.png");
        speedLine = new Image("/Resources/Other/Speed_Line.png");
        speedTop = new Image("/Resources/Other/Speed_Top.png");
        coin = new Image("/Resources/Other/Coin5.png");

        trackView.setImage(track);
        trackView.setFitHeight(height);
        trackView.setX(-30);

        trackBackView.setImage(trackBack);
        trackBackView.setFitHeight(height);

        speedBottomView = new CarPart(speedBottom, (int)width-230,(int)height-230);
        speedLineView = new CarPart(speedLine, (int)width-230,(int)height-230);
        speedTopView = new CarPart(speedTop, (int)width-230,(int)height-230);

        //car = new Car("Vaz2109",0, 0, 0,50, 430);

        //car.setColorAdjust("Body",new ColorAdjust());
        //car.setColorAdjust("Bumpers",new ColorAdjust());

        if(isDrive) car.playAudio("Start.wav");

        if(!isRest) car.carMoveX(-300);

        car.setScaleY(height/260/3);
        car.setScaleX(width/600/2.32);
        car.setGarage(false);
        car.update();



        this.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.W) {
                    wPressed = true;
                } else if (ke.getCode() == KeyCode.S) {
                    sPressed = true;
                } else if (ke.getCode() == KeyCode.UP) {
                    upPressed = true;
                } else if (ke.getCode() == KeyCode.DOWN) {
                    downPressed = true;
                } else if (ke.getCode() == KeyCode.R)
                {
                    speed=0;
                    Main.setScene(new Race(new Group(),car,true));
                    car.stopAudio();
                } else if (ke.getCode() == KeyCode.C)
                {
                    speed=0;
                    ColorAdjust colorAdjust1 = new ColorAdjust();
                    colorAdjust1.setHue(0);
                    colorAdjust1.setSaturation(0);
                    colorAdjust1.setContrast(0);
                    car.setColorAdjust("Body",colorAdjust1);
                    Main.setScene(new Garage(new Group()));
                    car.stopAudio();
                }
            }
        });

        this.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent ke) {
                if (ke.getCode() == KeyCode.W) {
                    wPressed = false;
                    speedY=0;
                } else if (ke.getCode() == KeyCode.S) {
                    sPressed = false;
                    speedY=0;
                } else if (ke.getCode() == KeyCode.UP) {
                    upPressed = false;
                } else if (ke.getCode() == KeyCode.DOWN) {
                    downPressed = false;
                    car.brake(false);
                }
            }
        });
        Group coinsGr = new Group();
        for(int i=0;i<coinsCount;i++) {
            coinsGr.getChildren().add(coins.get(i));
        }
        trackGroup = new Group();
        trackGroup.getChildren().addAll(trackView, trackBackView, coinsGr);
        //coinsGr.getChildren().add(coins[0]);
        root.getChildren().addAll(trackGroup, car, speedBottomView, speedLineView, speedTopView);

        new AnimationTimer() {
            private long lastTime = 0;
            public void handle(long currentNanoTime) {
                car.upd();
                if (currentNanoTime > lastTime + 1_000_000_000) {
                    seconds++;
                    lastTime = currentNanoTime;
                    //System.out.println(speed);
                }

                if(trackView.getX()<-(trackView.getFitWidth()/2))
                {

                }
                ////////////////////////////////////////////////////////////////////////
                if(wPressed) if(car.getCarY()>50) speedY=-4;
                if(sPressed) if (car.getCarY()<height-230) speedY=4;
                if(upPressed)
                {
                    speed += 1;
                    if (speed>1 && speed<15) car.brake(true);
                    if (speed>15) car.brake(false);
                }
                if(downPressed)
                {
                    if(speed>1.0) speed -= 2;
                    if (speed>1.0) car.brake(true);
                }
                ///////////////////////////////////////////////////////////////////////
                boolean col = false;

                if (!coins.isEmpty())
                    for(int i=0;i<coins.size();i++) {
                        System.out.println(i + " ; " + (trackGroup.getLayoutX()+coins.get(i).getX()));
                        if((trackGroup.getLayoutX()+coins.get(i).getX())<0) coins.remove(i);
                        if(isIntersect(coins.get(i), car.getCarBodyView()))
                        {
                            System.out.println("BOOO");
                            col = true;
                            coins.get(i).setVisible(false);
                        }
                    }
                if(col) System.out.println("HI");


                if(speed!=0 && speed>0.1 ) speed-=0.1;
                trackGroup.setLayoutX(trackGroup.getLayoutX()-(speed/2));
                car.carMoveY(speedY);
                if(speed>20)
                {
                    trackBackView.setVisible(true);
                    trackView.setVisible(false);
                    motionBlur.setRadius(15);
                }else if (speed > 15 && speed<=20)
                {
                    trackBackView.setEffect(new Blend());
                    //trackView.setEffect(motionBlur);
                }else if (speed>30)
                {
                    trackBackView.setEffect(motionBlur);
                }
                if(speed<=10)
                {
                    trackView.setEffect(new Blend());
                    trackView.setVisible(true);
                    trackBackView.setVisible(false);
                    motionBlur.setRadius(0);
                }else if (speed>10 && speed<15)
                {
                    //motionBlur.setRadius(motionBlur.getRadius()-2);
                }

                wheelRotate = car.getWheelsRotate();
                if(speed>0) car.setWheelsRotate(speed);
                if(car.getCarY()<100) car.carMoveY((int) (100-car.getCarY()));
                if(car.getCarY()>height-350) car.carMoveY((int) ((height-350)-car.getCarY()));

                speedLineView.setRotate(speed/2);
                //System.out.println(seconds);
                //System.out.println(speed);
            }
        }.start();
    }

    public void setCar(Car car)
    {
        this.car = car;
        car.setScaleY(height/260/3);
        car.setScaleX(width/600/2.32);
    }

    //пересечение квадратов
    public boolean isIntersect(ImageView a, ImageView b){
        return (((a.getParent().getLayoutX()+a.getX()) < (b.getX() + b.getFitWidth())) &&
                (b.getX() < ((a.getParent().getLayoutX()+a.getX()) + a.getFitWidth())) &&
                ((a.getParent().getLayoutY()+a.getY()) < (b.getY() + b.getFitHeight())) &&
                (b.getY() < ((a.getParent().getLayoutY()+a.getY()) + a.getFitHeight())));
    }

    /*public void setSaturation(double saturation)
    {
        car.getColorAdjust().setSaturation(saturation);
    }
    public void setHue(double hue)
    {
        car.getColorAdjust().setHue(hue);
    }
    public void setContrast(double contrast)
    {
        car.getColorAdjust().setContrast(contrast);
    }
    public void setBrightness(double brightness)
    {
        car.getColorAdjust().setBrightness(brightness);
    }*/
    }
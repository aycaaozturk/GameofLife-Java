package jpp.gol.ui;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import jpp.gol.io.StandardWorldLoader;
import jpp.gol.io.WorldLoader;
import jpp.gol.logic.GameLogic;
import jpp.gol.logic.ObservableGameLogicDecorator;
import jpp.gol.logic.StandardGameLogic;
import jpp.gol.logic.WorldChangedListener;
import jpp.gol.model.CellState;
import jpp.gol.model.World;
import jpp.gol.rules.Rules;
import jpp.gol.rules.StandardRules;

import java.io.*;




public class GameOfLife extends Application implements WorldChangedListener {

    private int widthOfACell = 50;
    private BorderPane root;
    private HBox topMenu;
    private ObservableGameLogicDecorator gamelogic;
    private TextField widthFeld;
    private TextField heightFeld;
    private Button newButton;
    private Button loadButton;
    private Button exitButton;

    private HBox BottomMenu;
    private Button start;
    private Button pause;
    private Button speedUp;
    private Button slowDown;
    private Label speedLabel;
    private Label currentSpeed;
    private boolean playOrPause = false;
    private long update;
    private double currentSpeedValue;
    private long stepDelay;


    private Canvas worldCanvas;

    private Canvas gameCanvas;
    private GraphicsContext gc;

    private double stageWidth;
    private double stageHeight;

    int width;
    int height;
    World world;
    Rules rules;

    private volatile boolean running = false;
    private Thread thread;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Game Of Life");
        root = new BorderPane();


        root.setBackground(new Background(new BackgroundFill(Color.DARKGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        createTopMenu(stage, this);
        createBottomMenu();

        stageWidth = stage.getWidth();
        stageHeight = stage.getHeight();
        gameCanvas = new Canvas(400, 400);
        gc = gameCanvas.getGraphicsContext2D();

        gameCanvas.addEventHandler(MouseEvent.MOUSE_PRESSED, (MouseEvent event) -> {
            int x = (int) event.getX() / widthOfACell;
            int y = (int) event.getY() / widthOfACell;
            if (!playOrPause) {
                gamelogic.changeState(x, y);
            }
            //oyun duraklatildiginda degistirebiliyirouz

        });

        stage.widthProperty().addListener((obs, oldVal, newVal) -> {
            stageWidth = stage.getWidth();
            stageHeight = stage.getHeight();
            showWorld();

        });

        stage.heightProperty().addListener((obs, oldVal, newVal) -> {
            stageWidth = stage.getWidth();
            stageHeight = stage.getHeight();
            showWorld();
        });


        root.setTop(topMenu);

        root.setCenter(gameCanvas);


        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        world = new World(10, 10);
        rules = new StandardRules();
        gamelogic = new ObservableGameLogicDecorator(new StandardGameLogic(world, rules));
        gamelogic.addWorldChangedListener(this);
        showWorld();
    }

    @Override
    public void stop(){
        running = false;
        System.exit(0);
    }

    public void createTopMenu(Stage stage,  GameOfLife thisGameOfLife) {
        this.topMenu = new HBox();
        topMenu.setSpacing(10);
        widthFeld = new TextField("10");
        heightFeld = new TextField("10");
        newButton = new Button("New");
        loadButton = new Button("Load");
        exitButton = new Button("Exit");
        topMenu.getChildren().addAll(widthFeld, heightFeld, newButton, loadButton, exitButton);
        exitButton.setOnAction(e -> {
            running = false;
            System.exit(0);
        });

//        newButton.setOnAction(e -> {
//            int width = Integer.parseInt(widthFeld.getText());
//            int height = Integer.parseInt(heightFeld.getText());
//            World world = new World(width, height);
//            Rules rules = new StandardRules();
//            gamelogic = new ObservableGameLogicDecorator(new StandardGameLogic(world, rules));
//            gamelogic.addWorldChangedListener(this);
//            showWorld();
//
//        });

        newButton.setOnAction(e -> {
            if (!playOrPause) {
                width = Integer.parseInt(widthFeld.getText());
                height = Integer.parseInt(heightFeld.getText());
                world = new World(width, height);
                rules = new StandardRules();
                gamelogic = new ObservableGameLogicDecorator(new StandardGameLogic(world, rules));
                gamelogic.addWorldChangedListener(this);
                showWorld();
            }

        });

        final FileChooser fileChooser = new FileChooser();
        loadButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            try {
                                InputStream in = new FileInputStream(file);
                                WorldLoader wl = new StandardWorldLoader();
                                world =  wl.load(in);
                                rules = new StandardRules();
                                gamelogic = new ObservableGameLogicDecorator(new StandardGameLogic(world, rules));
                                gamelogic.addWorldChangedListener(thisGameOfLife);
                                showWorld();

                            } catch (FileNotFoundException fnfe) {
                                fnfe.printStackTrace();
                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });


    }


    public void showWorld() {
        gc.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
        if (gamelogic == null) {
            return;
        }
        double minDimension = Math.min(stageHeight - 200, stageWidth);
        double maxCells = Math.max(gamelogic.getWorld().getHeight(), gamelogic.getWorld().getWidth());
        widthOfACell = (int) (minDimension / maxCells);
        gameCanvas.setWidth(gamelogic.getWorld().getWidth() * widthOfACell);
        gameCanvas.setHeight(gamelogic.getWorld().getHeight() * widthOfACell);

        for (int y = 0; y < gamelogic.getWorld().getHeight(); y++) {
            for (int x = 0; x < gamelogic.getWorld().getWidth(); x++) {
                CellState cell = gamelogic.getWorld().get(x, y);
                gc.setFill(Color.BLACK);
                gc.fillRect(x * widthOfACell, y*  widthOfACell, widthOfACell, widthOfACell);
                if (cell == CellState.DEAD) {
                    gc.setFill(Color.RED);
                } else {
                    gc.setFill(Color.GREEN);
                }

                gc.fillRect(x * widthOfACell + 1, y*  widthOfACell + 1, widthOfACell - 2, widthOfACell - 2);

            }


        }

    }

    @Override
    public void onChange(World world) {
        Platform.runLater(() -> {
            showWorld();
        });
    }

    public void createBottomMenu() {
        this.BottomMenu = new HBox();
        BottomMenu.setSpacing(10);
        start = new Button("Start");
        pause = new Button("Pause");
        speedLabel = new Label("Abspielgeschwindigkeit: ");
        currentSpeedValue = 500.0;
        stepDelay = (long) (currentSpeedValue);
        String speedString = String.format("%.0f", currentSpeedValue);
        currentSpeed = new Label(speedString);
        speedUp = new Button("+50");
        slowDown = new Button("-50");


        slowDown.setOnAction(e -> {
            if (!playOrPause) {
                double betragOfcurrentSpeed = Math.abs(currentSpeedValue - 50);
                currentSpeedValue = Math.max(100, betragOfcurrentSpeed);
                stepDelay = (long) (currentSpeedValue);
                currentSpeed.setText(String.format("%.0f", currentSpeedValue));
            }


        });
        speedUp.setOnAction(e -> {
            if (!playOrPause) {
                currentSpeedValue = Math.min(2000, currentSpeedValue + 50);
                stepDelay = (long) (currentSpeedValue);
                currentSpeed.setText(String.format("%.0f", currentSpeedValue));
            }


        });
        running = true;
        thread = new Thread(() -> {
            while (running) {
                if (playOrPause) {
                    try {
                        Thread.sleep(stepDelay);
                        gamelogic.step();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        });

        thread.start();

//        timer = new AnimationTimer() {
//            @Override
//            public void handle(long now) {
//                if (playOrPause) {
//                    if (now - update >= stepDelay * 1000000) {
//                        gamelogic.step();
//                        Platform.runLater(() -> {
//                                    showWorld();
//                        });
//                        update = now;
//                    }
//
//                }
//            }
//        };

        start.setOnAction(event -> {
            playOrPause = true;
//            timer.start();
        });
        pause.setOnAction(event -> {
            playOrPause = false;
//            timer.stop();
        });

        BottomMenu.getChildren().addAll(start, pause, speedLabel, slowDown, currentSpeed, speedUp);
        root.setBottom(BottomMenu);

    }


}
package com.example.snakeladder;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;

public class SnakeLadder extends Application {

    public static final int tileSize=40, width=10, height=10;
    public static final int buttonLine = height*tileSize+50, infoLine = buttonLine-30;

    private static Dice dice = new Dice();

    private Player playerOne, playerTwo;

    private boolean gameStarted = false, playerOneTurn = false, playerTwoTurn = false;

    private PauseTransition playerOneTransition = new PauseTransition(Duration.millis(900)); // 1 second
    private PauseTransition playerTwoTransition = new PauseTransition(Duration.millis(900)); // 1 second

    private Pane createContent(){
        Pane root = new Pane();
        root.setPrefSize(width*tileSize, height*tileSize+100);

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Tile tile = new Tile(tileSize);
                tile.setTranslateX(j*tileSize);
                tile.setTranslateY(i*tileSize);
                root.getChildren().add(tile);
            }
        }

        Image img = new Image("C:\\Users\\jangi\\IdeaProjects\\SnakeLadder\\src\\resources\\snakes-ladders-board.jpg");
        ImageView board = new ImageView();
        board.setImage(img);
        board.setFitHeight(height*tileSize);
        board.setFitWidth(width*tileSize);
        root.getChildren().add(board);

        //Buttons
        Button playerOneButton = new Button("Player 1");
        Button playerTwoButton = new Button("Player 2");
        Button startButton = new Button("Start");
        playerOneButton.setTranslateY(buttonLine);
        playerOneButton.setTranslateX(40);
        playerOneButton.setDisable(true);
        playerOneButton.setStyle("-fx-background-color: Black;-fx-background-radius: 8px;-fx-text-fill: white;-fx-font-size: 10pt; -fx-font-weight: bold;");


        playerTwoButton.setTranslateY(buttonLine);
        playerTwoButton.setTranslateX(300);
        playerTwoButton.setDisable(true);
        playerTwoButton.setStyle("-fx-background-color: Red;-fx-background-radius: 8px;-fx-text-fill: white;-fx-font-size: 10pt; -fx-font-weight: bold;");

        startButton.setTranslateY(buttonLine);
        startButton.setTranslateX(180);
        startButton.setStyle("-fx-background-color: #1f355e;-fx-background-radius: 8px;-fx-text-fill: white;-fx-font-size: 11pt; -fx-font-weight: bold;");;

        //infoLine
        Label playerOneLabel = new Label("");
        Label playerTwoLabel = new Label("");
        Label diceLabel = new Label("Let’s Play it!");
        playerOneLabel.setStyle("-fx-text-fill: Black;-fx-font-size: 10pt; -fx-font-weight: bold;");
        playerTwoLabel.setStyle("-fx-text-fill: Red;-fx-font-size: 10pt; -fx-font-weight: bold;");

        diceLabel.setStyle("-fx-text-fill: #1f355e;-fx-font-size: 12pt; -fx-font-weight: bold;");

        playerOneLabel.setTranslateY(infoLine);
        playerOneLabel.setTranslateX(32);

        playerTwoLabel.setTranslateY(infoLine);
        playerTwoLabel.setTranslateX(290);

        diceLabel.setTranslateY(infoLine);
        diceLabel.setTranslateX(158);

        playerOne = new Player(tileSize-7, Color.BLACK,"P1");
        playerTwo = new Player(tileSize-12, Color.RED,"P2");


        //Player moves
        playerOneButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(gameStarted){
                    if(playerOneTurn){
                        int diceValue = dice.getRolledDiceValue();
                        diceLabel.setText("Dice Value: " + diceValue);
                        playerOne.movePlayer(diceValue);

                            //check for winner
                            if (playerOne.isWinner()) {
                                diceLabel.setText("Whoa! " + playerOne.getName() + ", won.");
                                playerOneTurn = false;
                                playerOneButton.setDisable(true);
                                playerOneLabel.setText("");

                                playerTwoTurn = false;
                                playerTwoButton.setDisable(true);
                                playerTwoLabel.setText("");

                                startButton.setDisable(false);
                                startButton.setText("Restart");
                                startButton.setTranslateX(170);
                                gameStarted = false;
                            } else {
                                playerOneTurn = false;
                                playerOneButton.setDisable(true);
                                playerOneLabel.setText("");

                                playerTwoTransition.setOnFinished(event -> {
                                    // After the pause, re-enable the button and continue the game
                                    playerTwoTurn = true;
                                    playerTwoButton.setDisable(false);
                                    playerTwoLabel.setText("Your Turn! " + playerTwo.getName());
                                });
                                playerTwoTransition.play();
                            }
                    }
                }
            }
        });

        playerTwoButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if(gameStarted){
                    if(playerTwoTurn){
                        int diceValue = dice.getRolledDiceValue();
                        diceLabel.setText("Dice Value: " + diceValue);
                        playerTwo.movePlayer(diceValue);

                        //check for winner
                        if(playerTwo.isWinner()){
                            diceLabel.setText("Whoa! " + playerTwo.getName() + ", won.");
                            playerTwoTurn = false;
                            playerTwoButton.setDisable(true);
                            playerTwoLabel.setText("");

                            playerOneTurn = false;
                            playerOneButton.setDisable(true);
                            playerOneLabel.setText("");

                            startButton.setDisable(false);
                            startButton.setText("Restart");
                            startButton.setTranslateX(178);
                            gameStarted = false;
                        }
                        else{
                            playerTwoTurn = false;
                            playerTwoButton.setDisable(true);
                            playerTwoLabel.setText("");

                            playerOneTransition.setOnFinished(event -> {
                                // After the pause, re-enable the button and continue the game
                                playerOneTurn = true;
                                playerOneButton.setDisable(false);
                                playerOneLabel.setText("Your Turn! " + playerOne.getName());
                            });
                            playerOneTransition.play();

                        }
                    }
                }
            }
        });

        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                diceLabel.setText("Game Started");
                startButton.setDisable(true);
                startButton.setText("Start");
                startButton.setTranslateX(180);
                playerOneTurn=true;
                gameStarted=true;
                playerOneLabel.setText("Your Turn! " + playerOne.getName());
                playerOneButton.setDisable(false);
                playerOne.startingPosition();

                playerTwoTurn=false;
                playerTwoLabel.setText("");
                playerTwoButton.setDisable(true);
                playerTwo.startingPosition();
            }
        });

        root.getChildren().addAll(playerOneButton, playerTwoButton,startButton,
                playerOneLabel,playerTwoLabel, diceLabel,
                playerOne.getCoin(), playerTwo.getCoin()
        );

        return root;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(createContent());
        stage.setTitle("Sanke & Ladder");
        // Load the favicon image (replace "favicon.ico" with your actual file path)
        Image favicon = new Image("C:\\Users\\jangi\\IdeaProjects\\SnakeLadder\\src\\resources\\favicon.jpg");

        // Set the favicon for the primary stage
        stage.getIcons().add(favicon);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
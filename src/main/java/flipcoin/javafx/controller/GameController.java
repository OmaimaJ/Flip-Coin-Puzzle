// CHECKSTYLE:OFF
package flipcoin.javafx.controller;

import flipcoin.Player3;
import flipcoin.PlayerDao;
import flipcoin.StoringData;
import flipcoin.results.GameResult3;
import flipcoin.results.GameResultDao;
import flipcoin.state.FlipCoinState;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.net.URL;
import java.util.*;

@Slf4j
public class GameController {

    private FXMLLoader fxmlLoader  = new FXMLLoader();

    private GameResultDao gameResultDao;
    private PlayerDao playerDao;
    private List<Integer> clickedCoin= new ArrayList<>();
    private Player3 player1 = new Player3();
    private Player3 player2 = new Player3();
    private FlipCoinState gameState;
    private List<Image> coinImages;
    private int turn=0;
    private int player1Steps =0;
    private int player2Steps =0;
    private Player3 winner= new Player3();

    @FXML
    private Label playerTurn;

    @FXML
    private Label messageLabel;

    @FXML
    private Label errorLabel;

    @FXML
    private GridPane gameGrid;

    @FXML
    private Button resetButton;

    @FXML
    private Button flipButton;

    @FXML
    private Button giveUpButton;

    private BooleanProperty gameOver = new SimpleBooleanProperty();

//    public void setPlayerName1(String playerName) {
//        this.player1.setPlayerName(playerName);
//        playerTurn.setText(playerName);
//        this.player1.setWins(0);
//    }
//    public void setPlayerName2(String playerName) {
//        this.player2.setPlayerName(playerName);
//        playerTurn.setText(playerName);
//        this.player2.setWins(0);
//    }


    @FXML
    public void initialize() {
        player1= new Player3(StoringData.getP1(),0);
        player2 = new Player3(StoringData.getP2(),0);
        gameResultDao = GameResultDao.getInstance();
        playerDao = PlayerDao.getInstance();
        Optional<Player3> data1 = playerDao.find(player1.getPlayerName());
        data1.ifPresent((Player3 d) -> {player1.setWins(d.getWins()); });

        Optional<Player3> data2 = playerDao.find(this.player2.getPlayerName());
        data2.ifPresent((Player3 d) -> { this.player2.setWins(d.getWins()); });

        coinImages = List.of(
                new Image(getClass().getResource("/images/head.png").toExternalForm()),
                new Image(getClass().getResource("/images/tail.png").toExternalForm())
        );
        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                log.info("Game is over");
                log.debug("Saving result to database...");

                if (((Optional<Player3>) playerDao.find(player1.getPlayerName())).isPresent()) {
                    playerDao.update(player1.getPlayerName(),player1.getWins());
                } else {
                    playerDao.persist(player1);
                }

                if (((Optional<Player3>) playerDao.find(player2.getPlayerName())).isPresent()) {
                    playerDao.update(player2.getPlayerName(),player2.getWins());
                } else {
                    playerDao.persist(player2);
                }


                gameResultDao.persist(createGameResult());}


        });
        resetGame();

    }

    private void resetGame() {
         player1Steps =0;
       player2Steps =0;
        turn = 0;
        playerTurn.setText(player1.getPlayerName());
        gameState = new FlipCoinState();
        gameOver.setValue(false);
        displayGameState();
        Platform.runLater(() -> messageLabel.setText("Good luck, " + player1.getPlayerName() + " & "+ player2.getPlayerName()+ "!"));
    }

    private void displayGameState() {
        for (int i = 0; i < 10; i++) {
            ImageView view = (ImageView) gameGrid.getChildren().get(i);
            if (view.getImage() != null) {
                log.trace("Image(0, {}) = {}", i, view.getImage().getUrl());
            }
            if(gameState.getCoinBoard()[i]=='H')
                view.setImage(coinImages.get(0));
            else if (gameState.getCoinBoard()[i]=='T')
                view.setImage(coinImages.get(1));
            else
                view.setImage(null);

        }
    }

    public void handleClickOnCoin(MouseEvent mouseEvent) {
        int col = GridPane.getColumnIndex((Node) mouseEvent.getSource());
        log.debug("Coin (0, {}) is pressed", col);
        Set<Integer> clickedCoinSet = new HashSet<>(clickedCoin);

        clickedCoin.add(col);
        ((Node) mouseEvent.getSource()).setStyle("-fx-effect: innershadow(gaussian, #039ed3, 10, 1.0, 0, 0);");
    }

    public void handleFlipButton(ActionEvent actionEvent) {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        errorLabel.setText("");
        List<Integer> clickedCoinSet = new ArrayList<>((new HashSet<>(clickedCoin)));;

        Collections.sort(clickedCoin);

        if (clickedCoin.size() > 3 || clickedCoin.size() < 1)
            errorLabel.setText("Please select 1,2, or 3 coins");
        else if (gameState.isRightMostH(clickedCoin.get(clickedCoin.size() - 1))) {
            for (int i = 0; i < clickedCoinSet.size(); i++) {
                gameState.doFlip(clickedCoinSet.get(i));
            }
            if (turn == 0) {
                player1Steps ++;

                playerTurn.setText(player2.getPlayerName());
                turn = 1;
            } else {
                player2Steps++;
                turn = 0;
                playerTurn.setText(player1.getPlayerName());
            }

            displayGameState();

            if (gameState.isGameFinished()) {


                if (turn == 1){
                    winner= player1;
                    this.player1.setWins(this.player1.getWins()+1);

                }
                else{
                    winner= player2 ;
                    this.player2.setWins(this.player2.getWins()+1);
                }
                gameOver.setValue(true);
                messageLabel.setText("Congratulations, " + winner.getPlayerName() + "!");
                log.info("Player {} has solved the game.", winner.getPlayerName());
                flipButton.setDisable(true);
                playerTurn.setText("");
                resetButton.setDisable(true);
                giveUpButton.setText("Finish");
            }
        } else
            errorLabel.setText("Right Most is not Head.");


        for (int i = 0; i < clickedCoin.size(); i++)
            (gameGrid.getChildren().get(clickedCoin.get(i))).setStyle(null);

        clickedCoin = new ArrayList<>();
    }
    public void handleResetButton(ActionEvent actionEvent)  {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Resetting game...");
        resetGame();
    }

    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        String buttonText = ((Button) actionEvent.getSource()).getText();
        log.debug("{} is pressed", buttonText);
        if (buttonText.equals("Give Up")) {
            log.info("The game has been given up");
        }
        gameOver.setValue(true);
        log.info("Loading high scores scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private GameResult3 createGameResult() {
        GameResult3 result = GameResult3.builder()
                .player1(player1.getPlayerName())
                .player2(player2.getPlayerName())
                .winner(winner.getPlayerName())
                .movesByPlayer1(player1Steps)
                .movesByPlayer2(player2Steps)
                .solved(gameState.isGameFinished())
                .build();
        return result;
    }

}
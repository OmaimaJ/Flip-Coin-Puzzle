package flipcoin.javafx.controller;

import flipcoin.results.GameResult3;
import flipcoin.results.GameResultDao;
import flipcoin.state.FlipCoinState;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class GameController {

    private FXMLLoader fxmlLoader  = new FXMLLoader();

    private GameResultDao gameResultDao;
    private List<Integer> clickedCoin= new ArrayList<>();
    private String playerName1;
    private String playerName2;
    private FlipCoinState gameState;
    private List<Image> coinImages;
    private int turn=0;
    private String winnerName;

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

    public void setPlayerName1(String playerName) {
        this.playerName1 = playerName;
        playerTurn.setText(playerName1);
    }
    public void setPlayerName2(String playerName) { this.playerName2 = playerName; }


    @FXML
    public void initialize() {
        gameResultDao = GameResultDao.getInstance();
        coinImages = List.of(
                new Image(getClass().getResource("/images/head.png").toExternalForm()),
                new Image(getClass().getResource("/images/tail.png").toExternalForm())
        );
        gameOver.addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                gameResultDao.persist(createGameResult());
            }
        });
        resetGame();
    }

    private void resetGame() {
        turn = 0;
        playerTurn.setText(playerName1);
        gameState = new FlipCoinState();
        gameOver.setValue(false);
        displayGameState();
        Platform.runLater(() -> messageLabel.setText("Good luck, " + playerName1 + " & "+ playerName2+ "!"));
    }

    private void displayGameState() {
        for (int i = 0; i < 10; i++) {
            ImageView view = (ImageView) gameGrid.getChildren().get(i);
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
        Set<Integer> clickedCoinSet = new HashSet<>(clickedCoin);

        clickedCoin.add(col);
        ((Node) mouseEvent.getSource()).setStyle("-fx-effect: innershadow(gaussian, #039ed3, 10, 1.0, 0, 0);");
    }

    public void handleFlipButton() {
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
                playerTurn.setText(playerName2);
                turn = 1;
            } else {
                turn = 0;
                playerTurn.setText(playerName1);
            }

            displayGameState();

            if (gameState.isGameFinished()) {
                gameOver.setValue(true);
                if (turn == 1)
                    winnerName= playerName1 ;
                else
                    winnerName= playerName2 ;
                messageLabel.setText("Congratulations, " + winnerName + "!");
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
    public void handleResetButton()  {
        resetGame();
    }

    public void handleGiveUpButton(ActionEvent actionEvent) throws IOException {
        gameOver.setValue(true);
        fxmlLoader.setLocation(getClass().getResource("/fxml/highscores.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

    private GameResult3 createGameResult() {
        GameResult3 result = GameResult3.builder()
                .player(turn == 0 ? playerName2 : playerName1)
                .solved(gameState.isGameFinished())
                .build();
        return result;
    }

}
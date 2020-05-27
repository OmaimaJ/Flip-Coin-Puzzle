// CHECKSTYLE:OFF
package flipcoin.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import flipcoin.results.GameResult3;
import flipcoin.results.GameResultDao;
import lombok.extern.slf4j.Slf4j;


import java.io.IOException;
import java.util.List;

@Slf4j
public class HighScoreController {

    private FXMLLoader fxmlLoader = new FXMLLoader();

    private GameResultDao gameResultDao;

    @FXML
    private TableView<GameResult3> highScoreTable;

    @FXML
    private TableColumn<GameResult3, String> player;

    @FXML
    private void initialize() {
        log.debug("Loading high scores...");
        gameResultDao = GameResultDao.getInstance();
        List<GameResult3> highScoreList = gameResultDao.findBest(5);

        player.setCellValueFactory(new PropertyValueFactory<>("player"));

        ObservableList<GameResult3> observableResult = FXCollections.observableArrayList();
        observableResult.addAll(highScoreList);

        highScoreTable.setItems(observableResult);
    }

    public void handleRestartButton(ActionEvent actionEvent) throws IOException {
        log.debug("{} is pressed", ((Button) actionEvent.getSource()).getText());
        log.info("Loading launch scene...");
        fxmlLoader.setLocation(getClass().getResource("/fxml/launch.fxml"));
        Parent root = fxmlLoader.load();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    }

}

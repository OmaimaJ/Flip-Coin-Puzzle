// CHECKSTYLE:OFF
package flipcoin.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.io.IOException;

@Slf4j
public class LaunchController {


    private FXMLLoader fxmlLoader = new FXMLLoader();

    @FXML
    private TextField playerNameTextField1;

    @FXML
    private TextField playerNameTextField2;

    @FXML
    private Label errorLabel1;

    @FXML
    private Label errorLabel2;

    public void startAction(ActionEvent actionEvent) throws IOException {
        if (playerNameTextField1.getText().isEmpty()) {
            errorLabel1.setText("Enter your name!");
        }
        else if (playerNameTextField2.getText().isEmpty()) {
            errorLabel2.setText("Enter your name!");
        }
        else {
            fxmlLoader.setLocation(getClass().getResource("/fxml/game.fxml"));
            Parent root = fxmlLoader.load();
            fxmlLoader.<GameController>getController().setPlayerName1(playerNameTextField1.getText());
            fxmlLoader.<GameController>getController().setPlayerName2(playerNameTextField2.getText());
            Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
            log.info("The players name is set to {} & {}, loading game scene", playerNameTextField1.getText(), playerNameTextField2.getText());
        }
    }

}

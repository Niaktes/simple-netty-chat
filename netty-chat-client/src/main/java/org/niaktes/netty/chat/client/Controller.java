package org.niaktes.netty.chat.client;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Controller implements Initializable {

    private Network network;

    @FXML
    TextField messageField;

    @FXML
    TextArea mainArea;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        network = new Network((args -> mainArea.appendText((String)args[0])));
    }

    public void sendMessageAction() {
        network.sendMessage(messageField.getText());
        messageField.clear();
        messageField.requestFocus();
    }

    public void exitAction() {
        network.close();
        Platform.exit();
    }
}
package ru.geekbrains.july_chat.chat_app;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import ru.geekbrains.july_chat.chat_app.net.ChatMessageService;
import ru.geekbrains.july_chat.chat_app.net.MessageProcessor;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class MainChatController implements Initializable, MessageProcessor {
    public VBox loginPanel;
    public TextField loginField;
    public PasswordField passwordField;
    private ChatMessageService chatMessageService;
    private String nickName;
    public VBox mainChatPanel;
    public TextArea mainChatArea;
    public ListView<String> contactList;
    public TextField inputField;
    public Button btnSendMessage;
    private static String historyFile = "history.txt";
    private static int lastHistoryLines = 100;


    public void mockAction(ActionEvent actionEvent) {

    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void sendMessage(ActionEvent actionEvent) {
        String text = inputField.getText();
        if (text.isEmpty()) return;
        chatMessageService.send(this.nickName + ": " + text);
        inputField.clear();
    }


    @Override
    public void processMessage(String message) {
        Platform.runLater(() -> parseMessage(message));
    }

    public void sendAuth(ActionEvent actionEvent) {
        if (loginField.getText().isBlank() || passwordField.getText().isBlank()) return;
        chatMessageService.connect();
        chatMessageService.send("auth: " + loginField.getText() + " " + passwordField.getText());
    }

    private void parseMessage(String message) {
        if (message.startsWith("authok: ")) {
            this.nickName = message.substring(8);
            loginPanel.setVisible(false);
            mainChatPanel.setVisible(true);
        } else if (message.startsWith("ERROR: ")) {
            showError(message);
        } else if (message.startsWith("$.list: ")) {
            ObservableList<String> list = FXCollections.observableArrayList(message.substring(8).split("\\s"));
            contactList.setItems(list);
        } else {
            appendText(message);
            writeHistory(message);
        }
    }

    private void appendText(String text) {
        mainChatArea.appendText(text + System.lineSeparator());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(message);

        alert.showAndWait();

    }

    private void writeHistory(String message) {
        try {
            FileWriter f = new FileWriter(historyFile, true);
            f.write(new Date() + ": " + message.replace(System.lineSeparator(), "") + System.lineSeparator());
            f.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void readHistory() {
        try {
            Path path = Path.of(historyFile);
            Stream<String> lines = Files.lines(path);
            String[] data = lines.toArray(size -> new String[size]);
            lines.close();
            int startPos = data.length >= lastHistoryLines ? data.length - lastHistoryLines : 0;
            for (int i = startPos; i < data.length; i++) {
                appendText(data[i]);
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        List<String> contacts = Arrays.asList("Vasya", "Petya", "Masha", "Kolya", "Sergey");
//        ObservableList<String> list = FXCollections.observableArrayList("Vasya", "Petya", "Masha", "Kolya", "Sergey");
//        contactList.setItems(list);
        this.chatMessageService = new ChatMessageService(this);
        readHistory();
    }
}

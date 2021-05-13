import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.attribute.FileAttribute;
import java.util.Date;

public class ChatController {
    private static boolean continueRead = true;


    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;

    @FXML
    private TextArea allMessages;
    @FXML
    private TextField msgText;

    @FXML
    private Button sendBtn;
    @FXML
    private javafx.scene.layout.GridPane parentBox;

    @FXML
    private void initialize() throws IOException {
        try {
            openLoginWindow();
            if (!AuthController.isConnected()) {
                Platform.runLater(() -> {
                    Platform.exit();
                    System.exit(0);
                });
            } else {

                Main.mainStage.show();
                Main.mainStage.setTitle(Main.mainStage.getTitle() + " (" + Config.nick + ")");
                openConnection();
                addCloseListener();
                //Stage window = (Stage) sendBtn.getScene().getWindow();
                //window.show();


            }
        } catch (IOException e) {
            new Alerts().showAlert("Ошибка подключения", "Сервер не работает", "Не забудь включить сервер!", Alert.AlertType.ERROR);
            e.printStackTrace();
            throw e;
        }
    }

    private void openLoginWindow() throws IOException {
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("auth.fxml"));
        Stage loginStage = new Stage();
        loginStage.initModality(Modality.APPLICATION_MODAL);
        loginStage.setScene(new Scene(root));
        loginStage.setTitle("Вход");
        loginStage.showAndWait();
    }

    private void openConnection() throws IOException {
        socket = ServerConnection.getSocket();
        in = new DataInputStream(socket.getInputStream());
        out = new DataOutputStream(socket.getOutputStream());

        new Thread(() -> {
            try {
                while (socket.isConnected()) {
                    System.out.println("Готовы считывать");
                    String strFromServer = in.readUTF();
                    saveHistory(strFromServer); // почему sender cant be null?
                    System.out.println("Считал" + strFromServer);
                    if (strFromServer.equalsIgnoreCase("/end")) {
                        System.out.println("Конец цикла");
                        break;
                    }
                    allMessages.appendText(strFromServer + "\n");
                }
            } catch (Exception e) {
                if (e.getClass().getSimpleName().equals("EOFException"))
                    Platform.runLater(() -> {
                        new Alerts().showAlert("Сервер разорвал соединение!");
                        Platform.exit();
                        System.exit(0);
                    });
                e.printStackTrace();
            } finally {
                try {
                    in.close();
                    out.close();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    private void addCloseListener() {
        EventHandler<WindowEvent> onCloseRequest = Main.mainStage.getOnCloseRequest();
        Main.mainStage.setOnCloseRequest(event -> {
            closeConnection();
            if (onCloseRequest != null) {
                onCloseRequest.handle(event);
            }
        });
    }

    private void closeConnection() {
        try {
            out.writeUTF("/end");
            socket.close();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onSendBtnClick(ActionEvent actionEvent) {
        if (!msgText.getText().trim().isEmpty()) {
            try {
                //saveHistory("Me", msgText.getText().trim()); // Сервер обратно все равно транслирует
                out.writeUTF(msgText.getText().trim());
                msgText.clear();
                msgText.requestFocus();
            } catch (IOException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    new Alerts().showAlert(e.getMessage());
                });

            }
        }
    }

    public void onKeyPressedDummy(KeyEvent keyEvent) {
        msgText.requestFocus();
    }

    public void onMsgTextKeyPressed(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER && keyEvent.isControlDown())
            onSendBtnClick(null);

    }

    private void saveHistory(String sender, String message) {
        try {
            if (message.startsWith("\\")) return;
            int senderPos = message.indexOf(": ");
            if (sender.isEmpty() && senderPos > 0) {
                sender = message.substring(0, senderPos);
                message = message.substring(senderPos + 2);
            }

            FileWriter f = new FileWriter("history_" + sender + ".txt", true);
            f.write(new Date() + ": " + message + "\n");
            f.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void saveHistory(String message) {
        saveHistory("", message);
    }

}

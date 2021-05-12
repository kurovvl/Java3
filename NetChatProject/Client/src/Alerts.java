import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class Alerts {
    public void showAlert(String aTitle, String aHeader, String aContent, Alert.AlertType aType) {
        Alert alert = new Alert(aType);
        alert.setTitle(aTitle);
        alert.setHeaderText(aHeader);
        alert.setContentText(aContent);
        alert.showAndWait();
    }

    public void showAlert(String aContent) {
        showAlert("Внимание!", "Что-то не так!", aContent, Alert.AlertType.WARNING);
    }

}

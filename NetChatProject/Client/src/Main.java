import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.net.Socket;

public class Main extends Application {
    static Stage mainStage;


    /// Напоминалка для себя в будущем - FX отключен с версии JDK 9+, для смены JDK в Idea Ctrl+Alt+Shift+S
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage = primaryStage;
        
        Parent root = FXMLLoader.load(ClassLoader.getSystemResource("mainForm.fxml"));
        primaryStage.setTitle("Сетевой чат");
        primaryStage.setScene(new Scene(root, 610, 275));
        primaryStage.setResizable(false);
        //primaryStage.show();
    }
}

package com.grokmail.client.views;

import com.grokmail.client.controllers.ComposeController;
import com.grokmail.client.controllers.LoginController;
import com.grokmail.client.controllers.MainController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewFactory {

    private static ViewFactory instance = new ViewFactory();

    private ViewFactory() {}

    public static ViewFactory getInstance() {
        return instance;
    }

    public void showLoginWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grokmail/client/login.fxml"));
        LoginController controller = new LoginController();
        loader.setController(controller);
        createStage(loader, "Login");
    }

    public void showMainWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grokmail/client/main.fxml"));
        MainController controller = new MainController();
        loader.setController(controller);
        createStage(loader, "GrokMailClient");
    }

    public void showComposeWindow() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/grokmail/client/compose.fxml"));
        ComposeController controller = new ComposeController();
        loader.setController(controller);
        createStage(loader, "Compose Email");
    }

    private void createStage(FXMLLoader loader, String title) {
        Scene scene = null;
        try {
            if (loader.getLocation() == null) {
                throw new IllegalStateException("FXML file not found: " + loader.getLocation());
            }
            scene = new Scene(loader.load());
            scene.getStylesheets().add(getClass().getResource("/com/grokmail/client/styles.css").toExternalForm());
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load FXML: " + e.getMessage());
        }
        Stage stage = new Stage();
        stage.setTitle(title);
        stage.setScene(scene);
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
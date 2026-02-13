package com.grokmail.client;

import com.grokmail.client.views.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) {
        ViewFactory.getInstance().showLoginWindow();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
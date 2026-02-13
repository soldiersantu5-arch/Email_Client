package com.grokmail.client.controllers;

import com.grokmail.client.EmailManager;
import com.grokmail.client.views.ViewFactory;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField imapServerField;

    @FXML
    private TextField smtpServerField;

    @FXML
    private void loginAction() {
        String email = emailField.getText();
        String password = passwordField.getText();
        String imap = imapServerField.getText().isEmpty() ? "imap.gmail.com" : imapServerField.getText();
        String smtp = smtpServerField.getText().isEmpty() ? "smtp.gmail.com" : smtpServerField.getText();

        if (EmailManager.getInstance().login(email, password, imap, smtp)) {
            ViewFactory.getInstance().showMainWindow();
            ViewFactory.getInstance().closeStage((Stage) emailField.getScene().getWindow());
        } else {
            // Show error dialog (omitted for brevity, add Alert)
            System.out.println("Login failed");
        }
    }
}
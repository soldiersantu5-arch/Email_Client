package com.grokmail.client.controllers;

import com.grokmail.client.EmailManager;
import com.grokmail.client.views.ViewFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.stream.Collectors;

public class ComposeController {

    @FXML
    private TextField toField;

    @FXML
    private TextField subjectField;

    @FXML
    private HTMLEditor htmlEditor;

    @FXML
    private ListView<String> attachmentsList;

    private ObservableList<File> attachments = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        attachmentsList.setItems(attachments.stream().map(File::getName).collect(Collectors.toCollection(FXCollections::observableArrayList)));
    }

    @FXML
    private void attachAction() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Attach File");
        File file = chooser.showOpenDialog(null);
        if (file != null) {
            attachments.add(file);
            attachmentsList.getItems().add(file.getName());
        }
    }

    @FXML
    private void sendAction() {
        EmailManager.getInstance().sendEmail(toField.getText(), subjectField.getText(), htmlEditor.getHtmlText(), attachments);
        ViewFactory.getInstance().closeStage((Stage) toField.getScene().getWindow());
    }
}
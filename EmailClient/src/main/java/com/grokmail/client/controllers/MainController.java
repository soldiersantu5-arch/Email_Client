package com.grokmail.client.controllers;

import com.grokmail.client.EmailManager;
import com.grokmail.client.models.EmailMessage;
import com.grokmail.client.views.ViewFactory;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.web.WebView;
import jakarta.mail.Folder;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import java.util.Date;

public class MainController {

    @FXML
    private TreeView<String> foldersTree;

    @FXML
    private TableView<EmailMessage> emailsTable;

    @FXML
    private TableColumn<EmailMessage, String> senderCol;

    @FXML
    private TableColumn<EmailMessage, String> subjectCol;

    @FXML
    private TableColumn<EmailMessage, Date> dateCol;

    @FXML
    private WebView emailWebView;

    @FXML
    private TextField searchField;

    private EmailManager emailManager = EmailManager.getInstance();

    private ObservableList<EmailMessage> displayedMessages = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        senderCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSender()));
        subjectCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSubject()));
        dateCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDate()));

        emailsTable.setItems(displayedMessages);

        TreeItem<String> root = new TreeItem<>("Folders");
        foldersTree.setRoot(root);
        foldersTree.setShowRoot(false); // Hide the root node to prevent selection
        emailManager.getFolders().forEach(folder -> {
            TreeItem<String> item = new TreeItem<>(folder.getName());
            root.getChildren().add(item);
        });
        root.setExpanded(true);

        foldersTree.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.getValue().equals("Folders")) {
                loadFolder(newVal.getValue());
            } else {
                Platform.runLater(() -> displayedMessages.clear()); // Clear table if root is selected
            }
        });

        emailsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                emailWebView.getEngine().loadContent(newVal.getContent() + newVal.getAttachmentListHTML());
            }
        });

        searchField.textProperty().addListener((obs, oldVal, newVal) -> searchEmails(newVal));
    }

    private void loadFolder(String folderName) {
        new Thread(() -> {
            try {
                Folder folder = emailManager.ensureFolderOpen(folderName);
                if ((folder.getType() & Folder.HOLDS_MESSAGES) == 0) {
                    Platform.runLater(() -> displayedMessages.clear());
                    return;
                }
                Message[] messages = folder.getMessages();
                ObservableList<EmailMessage> emailMessages = FXCollections.observableArrayList();
                for (Message msg : messages) {
                    try {
                        emailMessages.add(new EmailMessage(msg));
                    } catch (MessagingException e) {
                        e.printStackTrace(); // Log individual message errors
                    }
                }
                Platform.runLater(() -> {
                    displayedMessages.setAll(emailMessages);
                    try {
                        if (folder.isOpen()) {
                            folder.close(false); // Close folder to prevent leaks
                        }
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }
                });
            } catch (MessagingException e) {
                e.printStackTrace();
                Platform.runLater(() -> displayedMessages.clear());
            }
        }).start();
    }

    private void searchEmails(String query) {
        if (query.isEmpty()) {
            return;
        }
        ObservableList<EmailMessage> filtered = FXCollections.observableArrayList();
        for (EmailMessage msg : displayedMessages) {
            if (msg.getSubject().toLowerCase().contains(query.toLowerCase()) ||
                    msg.getSender().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(msg);
            }
        }
        emailsTable.setItems(filtered);
    }

    @FXML
    private void composeAction() {
        ViewFactory.getInstance().showComposeWindow();
    }
}
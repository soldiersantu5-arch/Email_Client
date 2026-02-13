package com.grokmail.client;

import com.grokmail.client.models.EmailMessage;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class EmailManager {

    private static EmailManager instance = new EmailManager();

    private Store store;
    private Session smtpSession;
    private String email;
    private String password;
    private String imapHost;
    private String smtpHost;
    private List<Folder> folders = new ArrayList<>();

    private EmailManager() {}

    public static EmailManager getInstance() {
        return instance;
    }

    public boolean login(String email, String password, String imapHost, String smtpHost) {
        this.email = email;
        this.password = password;
        this.imapHost = imapHost;
        this.smtpHost = smtpHost;

        Properties imapProps = new Properties();
        imapProps.put("mail.imap.host", imapHost);
        imapProps.put("mail.imap.port", "993");
        imapProps.put("mail.imap.ssl.enable", "true");
        imapProps.put("mail.imap.connectiontimeout", "10000");
        imapProps.put("mail.imap.timeout", "10000");
        Session imapSession = Session.getInstance(imapProps);
        try {
            store = imapSession.getStore("imap");
            store.connect(email, password);
            Folder root = store.getDefaultFolder();
            folders = Arrays.stream(root.list("*"))
                    .filter(folder -> {
                        try {
                            return (folder.getType() & Folder.HOLDS_MESSAGES) != 0;
                        } catch (MessagingException e) {
                            return false;
                        }
                    })
                    .collect(Collectors.toList());
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }

        Properties smtpProps = new Properties();
        smtpProps.put("mail.smtp.host", smtpHost);
        smtpProps.put("mail.smtp.port", "587");
        smtpProps.put("mail.smtp.auth", "true");
        smtpProps.put("mail.smtp.starttls.enable", "true");
        smtpSession = Session.getInstance(smtpProps, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(email, password);
            }
        });

        return true;
    }

    public List<Folder> getFolders() {
        return folders;
    }

    public Folder getFolderByName(String name) {
        return folders.stream().filter(f -> f.getName().equals(name)).findFirst().orElse(null);
    }

    public Folder ensureFolderOpen(String folderName) throws MessagingException {
        Folder folder = getFolderByName(folderName);
        if (folder == null) {
            throw new MessagingException("Folder not found: " + folderName);
        }
        if (!store.isConnected()) {
            store.connect(email, password);
        }
        if (!folder.isOpen()) {
            folder.open(Folder.READ_ONLY);
        }
        return folder;
    }

    public void sendEmail(String to, String subject, String content, List<File> attachments) {
        try {
            MimeMessage message = new MimeMessage(smtpSession);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);

            MimeMultipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(content, "text/html; charset=utf-8");
            multipart.addBodyPart(textPart);

            for (File file : attachments) {
                MimeBodyPart attachPart = new MimeBodyPart();
                attachPart.attachFile(file);
                multipart.addBodyPart(attachPart);
            }

            message.setContent(multipart);
            Transport.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
package com.grokmail.client.models;

import jakarta.mail.*;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMultipart;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmailMessage {

    private Message message;
    private String sender;
    private String subject;
    private Date date;
    private String content;
    private List<String> attachmentList = new ArrayList<>();

    public EmailMessage(Message message) throws MessagingException {
        this.message = message;
        try {
            this.sender = message.getFrom() != null && message.getFrom().length > 0 ? message.getFrom()[0].toString() : "Unknown";
            this.subject = message.getSubject() != null ? message.getSubject() : "(No Subject)";
            this.date = message.getSentDate() != null ? message.getSentDate() : new Date();
            this.content = getTextFromMessage();
            fetchAttachments();
        } catch (Exception e) {
            throw new MessagingException("Failed to parse message", e);
        }
    }

    private String getTextFromMessage() throws MessagingException, IOException {
        if (message.isMimeType("text/plain")) {
            return message.getContent().toString();
        } else if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (bodyPart.isMimeType("text/plain")) {
                    return bodyPart.getContent().toString();
                } else if (bodyPart.isMimeType("text/html")) {
                    return bodyPart.getContent().toString();
                }
            }
        }
        return "";
    }

    private void fetchAttachments() throws MessagingException, IOException {
        if (message.isMimeType("multipart/*")) {
            MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
            for (int i = 0; i < mimeMultipart.getCount(); i++) {
                BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                if (Part.ATTACHMENT.equalsIgnoreCase(bodyPart.getDisposition()) ||
                        bodyPart.getFileName() != null) {
                    attachmentList.add(bodyPart.getFileName());
                }
            }
        }
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public Date getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getAttachmentListHTML() {
        StringBuilder html = new StringBuilder();
        if (!attachmentList.isEmpty()) {
            html.append("<br><b>Attachments:</b><ul>");
            for (String attachment : attachmentList) {
                html.append("<li>").append(attachment).append("</li>");
            }
            html.append("</ul>");
        }
        return html.toString();
    }
}
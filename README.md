A modern Gmail-inspired email client built with JavaFX 22 and Jakarta Mail API. Features IMAP/SMTP support, light/dark themes, email previews, and robust error handling.

# 📧 GrokMailClient

![JavaFX](https://img.shields.io/badge/JavaFX-22-blue?logo=java)
![Jakarta Mail](https://img.shields.io/badge/Jakarta%20Mail-2.0.1-orange)
![Maven](https://img.shields.io/badge/Maven-3.8+-green?logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-lightgrey)

**GrokMailClient** is a modern, **Gmail-inspired email client** built with **JavaFX 22** and the **Jakarta Mail API** (successor to JavaMail).  
Developed as part of a **CodeClause Internship**, this project provides a user-friendly interface for managing emails via **IMAP** and **SMTP** protocols.  

It features a sleek sidebar, email previews, a light/dark theme toggle, and robust error handling — making it ideal for **personal use, or learning JavaFX with email APIs**.

---

## ✨ Features

### 🎨 Gmail-Inspired UI
- Sidebar with folder navigation (`ListView`)
- Header bar with **"GrokMail" logo**, search, and compose/theme toggle buttons
- Email list with **sender, subject, snippet preview, and date**
- Split pane for email list and content preview
- Light/dark theme toggle for accessibility

### 📩 Email Functionality (via Jakarta Mail API)
- Connect to **IMAP servers** (e.g., `imap.gmail.com`) to fetch folders and emails
- Send emails with attachments via **SMTP** (e.g., `smtp.gmail.com`)
- View email content + attachments in a `WebView`
- Search emails by sender, subject, or snippet

### 🧑‍💻 User Experience
- Auto-selects **INBOX** on startup
- Status feedback via bottom loading label
- Responsive design with hover effects + smooth animations

### ⚡ Error Handling
- Graceful handling of **IMAP connection issues** (`FolderClosedException`)
- Prevention of invalid folder selections
- Fixes for **FXML** and **JavaFX version mismatches**

---

## 📂 Directory Structure

```

EmailClient2.0/
├── pom.xml                     # Maven configuration
├── README.md                   # Project documentation
├── src/
│   ├── main/
│   │   ├── java/com/grokmail/client/
│   │   │   ├── App.java                   # Main app entry point
│   │   │   ├── EmailManager.java          # Manages IMAP/SMTP
│   │   │   ├── models/EmailMessage.java   # Email data model
│   │   │   ├── controllers/               # JavaFX Controllers
│   │   │   │   ├── LoginController.java
│   │   │   │   ├── MainController.java
│   │   │   │   ├── ComposeController.java
│   │   │   ├── views/ViewFactory.java     # UI stage manager
│   ├── resources/com/grokmail/client/
│   │   ├── login.fxml
│   │   ├── main.fxml
│   │   ├── compose.fxml
│   │   ├── styles.css
└── target/
├── classes/
├── client-1.0-SNAPSHOT.jar

````

---

## ⚙️ Prerequisites

- **JDK 21** → Required for JavaFX 22 + Jakarta Mail  
- **Maven 3.8+** → Dependency management & build  
- **Internet Connection** → For IMAP/SMTP servers  
- **Email Account** (e.g., Gmail with IMAP enabled + app-specific password)

---

## 🚀 Setup & Installation

### 1. Clone the Project
```bash
git clone https://github.com/your-username/GrokMailClient.git
cd GrokMailClient
````

### 2. Configure Email Account

#### Gmail Setup

* Enable **IMAP**:
  `Settings > Forwarding and POP/IMAP > Enable IMAP`
* Generate an **App Password**:
  `Google Account > Security > App Passwords > Select "Mail" > Device: Windows Computer`
* Servers:

    * IMAP: `imap.gmail.com` (port 993)
    * SMTP: `smtp.gmail.com` (port 587)

#### Other Providers

* Use respective IMAP/SMTP server details (Outlook, Yahoo, etc.).

---

### 3. Build the Project

```bash
mvn clean install
```

Verify compiled resources exist in:
`target/classes/com/grokmail/client/`

---

### 4. Run the Application

#### Using Maven

```bash
mvn clean javafx:run
```

#### Using IDE (IntelliJ/Eclipse)

* Refresh Maven dependencies
* Run `App.java`
* Ensure **JDK 21** is selected

#### Using JAR

```bash
mvn clean package

java --module-path %USERPROFILE%\.m2\repository\org\openjfx\javafx-controls\22\javafx-controls-22.jar;%USERPROFILE%\.m2\repository\org\openjfx\javafx-fxml\22\javafx-fxml-22.jar;%USERPROFILE%\.m2\repository\org\openjfx\javafx-web\22\javafx-web-22.jar ^
--add-modules javafx.controls,javafx.fxml,javafx.web -jar target/client-1.0-SNAPSHOT.jar
```

---

### 5. Test the Application

* ✅ Login with email credentials
* ✅ Sidebar folders auto-load (INBOX selected)
* ✅ Email list shows sender, subject, snippet, and date
* ✅ WebView shows content (split pane 60%)
* ✅ Light/Dark theme toggle works
* ✅ Test search, compose, and folder navigation

---

## 📦 Dependencies

Defined in `pom.xml`:

* **JavaFX 22**
  `org.openjfx:javafx-controls:22`
  `org.openjfx:javafx-fxml:22`
  `org.openjfx:javafx-web:22`

* **Jakarta Mail API**
  `com.sun.mail:jakarta.mail:2.0.1`

* **Maven Compiler Plugin** → `3.13.0` (JDK 21)

* **JavaFX Maven Plugin** → `0.0.8`

---

## 🛠️ Troubleshooting

* **FXML LoadException** → Remove `dividerPositions` from FXML (set in `MainController.java`)
* **JavaFX Version Mismatch** → Clear `.m2/repository/org/openjfx` and rebuild
* **IMAP/SMTP Errors** → Check server, password, internet
* **Unsafe Warning** → Safe to ignore on JDK 21

---

## 🚧 Future Improvements

* 📎 Attachment downloading (extend `EmailMessage.java`)
* 🗑️ Email deletion/flagging with Jakarta Mail Flags API
* 🔄 Refresh button for folders/emails
* 📦 Native executable packaging with:

  ```bash
  jpackage --name GrokMailClient --input target --main-jar client-1.0-SNAPSHOT.jar --type app-image
  ```

---


## 👨‍💻 Developed By
**Santosh M C**  

---


## 📜 License

This project is licensed under the **MIT License** – you are free to use, modify, and distribute with attribution.

---

## 🌟 Acknowledgements

* [JavaFX](https://openjfx.io/)
* [Jakarta Mail](https://eclipse-ee4j.github.io/mail/)
* [CodeClause Internship](https://codeclause.com/)

```

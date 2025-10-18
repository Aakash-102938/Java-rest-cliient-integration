import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.animation.*;
import javafx.util.Duration;
import java.util.*;

import javax.swing.plaf.synth.Region;

import java.lang.classfile.Label;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.scene.chart.*;


public class nm3 extends Application {
    
    private static Map<String, User> userDatabase = new HashMap<>();
    private static User currentUser = null;
    private static List<Task> userTasks = new ArrayList<>();
    private static Map<LocalDate, Integer> activityData = new HashMap<>();
    
    private Stage primaryStage;
    private String currentPage = "dashboard";
    
   
    static class User {
        String name;
        String email;
        String password;
        String avatarPath; // Path to profile image
        
        User(String name, String email, String password) {
            this.name = name;
            this.email = email;
            this.password = password;
            this.avatarPath = null; // TODO: Load from backend
        }
    }
    
    
    static class Task {
        String id;
        String title;
        String description;
        boolean completed;
        String priority; // high, medium, low
        LocalDate dueDate;
        LocalDate createdDate;
        
        Task(String title, String description, String priority, LocalDate dueDate) {
            this.id = UUID.randomUUID().toString();
            this.title = title;
            this.description = description;
            this.completed = false;
            this.priority = priority;
            this.dueDate = dueDate;
            this.createdDate = LocalDate.now();
        }
    }
    
    @Override
    public void start(Stage stage) {
    this.primaryStage = stage;
   
    primaryStage.getIcons().add(new Image("file:D:/naan muthalvan project/applogo.png"));
    initializeSampleData(); // Remove this when connecting to backend
    showCreateAccountPage();
    primaryStage.setTitle("Modern Account System");
    primaryStage.show();
    }
    
    
    private void initializeSampleData() {
        LocalDate today = LocalDate.now();
        Random rand = new Random();
        for (int i = 0; i < 365; i++) {
            LocalDate date = today.minusDays(i);
            activityData.put(date, rand.nextInt(15));
        }
    }
    
    private void showCreateAccountPage() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #b8887e;");
        
        HBox cardContainer = new HBox();
        cardContainer.setMaxWidth(1100);
        cardContainer.setMaxHeight(650);
        cardContainer.setEffect(new DropShadow(40, Color.rgb(0, 0, 0, 0.3)));
        
        VBox leftSide = createFormSection();
        StackPane rightSide = createIllustrationSection();
        
        HBox.setHgrow(leftSide, Priority.ALWAYS);
        HBox.setHgrow(rightSide, Priority.ALWAYS);
        cardContainer.getChildren().addAll(leftSide, rightSide);
        
        root.getChildren().add(cardContainer);
        
        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
    }
    
    private VBox createFormSection() {
        VBox formContainer = new VBox(20);
        formContainer.setAlignment(Pos.CENTER);
        formContainer.setPadding(new Insets(50, 60, 50, 60));
        formContainer.setMinWidth(500);
        formContainer.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 25 0 0 25;");
        
        Label titleLabel = new Label("Create account");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 46));
        titleLabel.setStyle("-fx-text-fill: #000000;");
        
        Label subtitleLabel = new Label("Let's get started with your 30 days trial");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #666666;");
        
        VBox titleBox = new VBox(8);
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setPadding(new Insets(0, 0, 20, 0));
        
        TextField nameField = createStyledTextField("Name");
        TextField emailField = createStyledTextField("Email");
        HBox passwordBox = createPasswordField();
        
        Button createBtn = new Button("Create account");
        createBtn.setPrefWidth(350);
        createBtn.setPrefHeight(52);
        createBtn.setStyle("-fx-background-color: #9d7b7b; -fx-text-fill: white; " +
                          "-fx-font-size: 15px; -fx-font-weight: 600; " +
                          "-fx-background-radius: 12; -fx-cursor: hand;");
        
        createBtn.setOnMouseEntered(e -> 
            createBtn.setStyle("-fx-background-color: #8a6a6a; -fx-text-fill: white; " +
                             "-fx-font-size: 15px; -fx-font-weight: 600; " +
                             "-fx-background-radius: 12; -fx-cursor: hand;"));
        
        createBtn.setOnMouseExited(e -> 
            createBtn.setStyle("-fx-background-color: #9d7b7b; -fx-text-fill: white; " +
                             "-fx-font-size: 15px; -fx-font-weight: 600; " +
                             "-fx-background-radius: 12; -fx-cursor: hand;"));
        
        createBtn.setOnAction(e -> handleCreateAccount(nameField, emailField, passwordBox));
        
        HBox loginBox = new HBox(5);
        loginBox.setAlignment(Pos.CENTER);
        Label alreadyLabel = new Label("Already have an account?");
        alreadyLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px;");
        Hyperlink loginLink = new Hyperlink("Login");
        loginLink.setStyle("-fx-text-fill: #9d7b7b; -fx-font-weight: bold; -fx-font-size: 13px;");
        loginLink.setOnAction(e -> showLoginPage());
        loginBox.getChildren().addAll(alreadyLabel, loginLink);
        
        VBox fieldsBox = new VBox(15);
        fieldsBox.getChildren().addAll(nameField, emailField, passwordBox);
        fieldsBox.setAlignment(Pos.CENTER);
        
        formContainer.getChildren().addAll(titleBox, fieldsBox, createBtn, loginBox);
        
        return formContainer;
    }
    
    private TextField createStyledTextField(String prompt) {
        TextField field = new TextField();
        field.setPromptText(prompt);
        field.setPrefWidth(350);
        field.setPrefHeight(52);
        field.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                      "-fx-border-color: #e0e0e0; -fx-border-width: 1.5; " +
                      "-fx-border-radius: 12; -fx-font-size: 14px; " +
                      "-fx-prompt-text-fill: #aaaaaa; -fx-padding: 15;");
        
        field.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                field.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                             "-fx-border-color: #9d7b7b; -fx-border-width: 2; " +
                             "-fx-border-radius: 12; -fx-font-size: 14px; -fx-padding: 15;");
            } else {
                field.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                             "-fx-border-color: #e0e0e0; -fx-border-width: 1.5; " +
                             "-fx-border-radius: 12; -fx-font-size: 14px; -fx-padding: 15;");
            }
        });
        
        return field;
    }
    
    private HBox createPasswordField() {
        PasswordField passwordField = new PasswordField();
        TextField textField = new TextField();
        
        passwordField.setPromptText("Password");
        textField.setPromptText("Password");
        passwordField.setPrefSize(350, 52);
        textField.setPrefSize(350, 52);
        
        String baseStyle = "-fx-background-color: white; -fx-background-radius: 12; " +
                "-fx-border-color: #e0e0e0; -fx-border-width: 1.5; " +
                "-fx-border-radius: 12; -fx-font-size: 14px; " +
                "-fx-prompt-text-fill: #aaaaaa; -fx-padding: 0 40 0 15;";
        
        passwordField.setStyle(baseStyle);
        textField.setStyle(baseStyle);
        textField.setVisible(false);
        textField.setManaged(false);
        
        passwordField.textProperty().bindBidirectional(textField.textProperty());
        
        Button toggleBtn = new Button("👁");
        toggleBtn.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; " +
                "-fx-cursor: hand; -fx-font-size: 16px;");
        toggleBtn.setFocusTraversable(false);
        
        toggleBtn.setOnAction(e -> {
            boolean showing = passwordField.isVisible();
            passwordField.setVisible(!showing);
            passwordField.setManaged(!showing);
            textField.setVisible(showing);
            textField.setManaged(showing);
            toggleBtn.setText(showing ? "🙈" : "👁");
        });
        
        StackPane passwordStack = new StackPane();
        passwordStack.setPrefSize(350, 52);
        passwordStack.getChildren().addAll(passwordField, textField, toggleBtn);
        StackPane.setAlignment(toggleBtn, Pos.CENTER_RIGHT);
        StackPane.setMargin(toggleBtn, new Insets(0, 10, 0, 0));
        
        HBox passwordBox = new HBox(passwordStack);
        passwordBox.setAlignment(Pos.CENTER);
        passwordBox.setUserData(passwordField);
        return passwordBox;
    }
    
    private StackPane createIllustrationSection() {
        StackPane illustrationPane = new StackPane();
        illustrationPane.setStyle(
            "-fx-background-color: linear-gradient(to bottom, " +
            "#7d5d7d 0%, #b8988b 25%, #d9a896 50%, #f5c89f 75%, #e8b4a8 100%); " +
            "-fx-background-radius: 0 25 25 0;"
        );

        // Load local image and display using ImageView
        Image image = new Image("file:D:/naan muthalvan project/heroimg.png", 600, 600, true, true);
        ImageView imageView = new ImageView(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setFitWidth(500); // Adjust as needed
        imageView.setOpacity(0.95);

        illustrationPane.getChildren().add(imageView);
        return illustrationPane;
    }
    
    private ImageView createPlaceholderImage() {
        // Placeholder for when image is not found
        // TODO: Replace with actual image loading
        Rectangle placeholder = new Rectangle(500, 500, Color.web("#e0e0e0"));
        Label placeholderText = new Label("🖼️\nImage Here");
        placeholderText.setStyle("-fx-text-fill: #999999; -fx-font-size: 32px; -fx-text-alignment: center;");
        
        StackPane stack = new StackPane(placeholder, placeholderText);
        ImageView iv = new ImageView();
        iv.setFitWidth(500);
        iv.setFitHeight(500);
        return iv;
    }
    
    private void handleCreateAccount(TextField nameField, TextField emailField, HBox passwordBox) {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        PasswordField passwordField = (PasswordField) passwordBox.getUserData();
        String password = passwordField.getText();
        
        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields");
            return;
        }
        
        if (userDatabase.containsKey(email)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Email already exists! Please use a different email.");
            return;
        }
        
        // TODO: Backend API call - POST /api/register
        // Send: {name, email, password (hashed)}
        User newUser = new User(name, email, password);
        userDatabase.put(email, newUser);
        currentUser = newUser;
        
        showSuccessPage(name);
    }
    
    // ========== SUCCESS PAGE ==========
    private void showSuccessPage(String userName) {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #f5f5f5, #e8e8e8);");
        
        VBox container = new VBox(30);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        
        StackPane checkCircle = createAnimatedCheckmark();
        
        Label successLabel = new Label("Account Created Successfully!");
        successLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        successLabel.setStyle("-fx-text-fill: #2ecc71;");
        
        Label welcomeLabel = new Label("Welcome, " + userName + "!");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 20));
        welcomeLabel.setStyle("-fx-text-fill: #555555;");
        
        Label infoLabel = new Label("Your account has been created. You can now login.");
        infoLabel.setFont(Font.font("Arial", 14));
        infoLabel.setStyle("-fx-text-fill: #888888;");
        
        Button loginBtn = new Button("Go to Login");
        loginBtn.setPrefWidth(250);
        loginBtn.setPrefHeight(50);
        loginBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                         "-fx-font-size: 16px; -fx-font-weight: 600; " +
                         "-fx-background-radius: 25; -fx-cursor: hand;");
        
        loginBtn.setOnMouseEntered(e -> 
            loginBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white; " +
                            "-fx-font-size: 16px; -fx-font-weight: 600; " +
                            "-fx-background-radius: 25; -fx-cursor: hand;"));
        
        loginBtn.setOnMouseExited(e -> 
            loginBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                            "-fx-font-size: 16px; -fx-font-weight: 600; " +
                            "-fx-background-radius: 25; -fx-cursor: hand;"));
        
        loginBtn.setOnAction(e -> showLoginPage());
        
        container.getChildren().addAll(checkCircle, successLabel, welcomeLabel, infoLabel, loginBtn);
        root.getChildren().add(container);
        
        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
    }
    
    private StackPane createAnimatedCheckmark() {
        StackPane pane = new StackPane();
        
        Circle outerCircle = new Circle(80);
        outerCircle.setFill(Color.TRANSPARENT);
        outerCircle.setStroke(Color.web("#2ecc71"));
        outerCircle.setStrokeWidth(5);
        
        Circle innerCircle = new Circle(80);
        innerCircle.setFill(Color.web("#d4f4dd"));
        
        Label checkmark = new Label("✓");
        checkmark.setFont(Font.font("Arial", FontWeight.BOLD, 80));
        checkmark.setStyle("-fx-text-fill: #2ecc71;");
        checkmark.setOpacity(0);
        
        pane.getChildren().addAll(innerCircle, outerCircle, checkmark);
        
        ScaleTransition scaleIn = new ScaleTransition(Duration.millis(400), pane);
        scaleIn.setFromX(0);
        scaleIn.setFromY(0);
        scaleIn.setToX(1);
        scaleIn.setToY(1);
        scaleIn.setInterpolator(Interpolator.EASE_OUT);
        
        FadeTransition fadeCheckmark = new FadeTransition(Duration.millis(300), checkmark);
        fadeCheckmark.setFromValue(0);
        fadeCheckmark.setToValue(1);
        fadeCheckmark.setDelay(Duration.millis(300));
        
        RotateTransition rotate = new RotateTransition(Duration.millis(600), checkmark);
        rotate.setFromAngle(-180);
        rotate.setToAngle(0);
        rotate.setDelay(Duration.millis(300));
        
        scaleIn.play();
        fadeCheckmark.play();
        rotate.play();
        
        return pane;
    }
    
    // ========== LOGIN PAGE ==========
    private void showLoginPage() {
        StackPane root = new StackPane();
        root.setStyle("-fx-background-color: #b8887e;");
        
        VBox container = new VBox(25);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(50));
        container.setMaxWidth(450);
        container.setStyle("-fx-background-color: #f5f5f5; -fx-background-radius: 25;");
        container.setEffect(new DropShadow(40, Color.rgb(0, 0, 0, 0.3)));
        
        Label titleLabel = new Label("Welcome Back");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        titleLabel.setStyle("-fx-text-fill: #000000;");
        
        Label subtitleLabel = new Label("Login to your account");
        subtitleLabel.setFont(Font.font("Arial", 14));
        subtitleLabel.setStyle("-fx-text-fill: #666666;");
        
        VBox titleBox = new VBox(8);
        titleBox.getChildren().addAll(titleLabel, subtitleLabel);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setPadding(new Insets(0, 0, 20, 0));
        
        TextField emailField = createStyledTextField("Email");
        HBox passwordBox = createPasswordField();
        
        Button loginBtn = new Button("Login");
        loginBtn.setPrefWidth(350);
        loginBtn.setPrefHeight(52);
        loginBtn.setStyle("-fx-background-color: #9d7b7b; -fx-text-fill: white; " +
                         "-fx-font-size: 15px; -fx-font-weight: 600; " +
                         "-fx-background-radius: 12; -fx-cursor: hand;");
        
        loginBtn.setOnMouseEntered(e -> 
            loginBtn.setStyle("-fx-background-color: #8a6a6a; -fx-text-fill: white; " +
                            "-fx-font-size: 15px; -fx-font-weight: 600; " +
                            "-fx-background-radius: 12; -fx-cursor: hand;"));
        
        loginBtn.setOnMouseExited(e -> 
            loginBtn.setStyle("-fx-background-color: #9d7b7b; -fx-text-fill: white; " +
                            "-fx-font-size: 15px; -fx-font-weight: 600; " +
                            "-fx-background-radius: 12; -fx-cursor: hand;"));
        
        loginBtn.setOnAction(e -> handleLogin(emailField, passwordBox));
        
        HBox signupBox = new HBox(5);
        signupBox.setAlignment(Pos.CENTER);
        Label noAccountLabel = new Label("Don't have an account?");
        noAccountLabel.setStyle("-fx-text-fill: #888888; -fx-font-size: 13px;");
        Hyperlink signupLink = new Hyperlink("Sign Up");
        signupLink.setStyle("-fx-text-fill: #9d7b7b; -fx-font-weight: bold; -fx-font-size: 13px;");
        signupLink.setOnAction(e -> showCreateAccountPage());
        signupBox.getChildren().addAll(noAccountLabel, signupLink);
        
        VBox fieldsBox = new VBox(15);
        fieldsBox.getChildren().addAll(emailField, passwordBox);
        fieldsBox.setAlignment(Pos.CENTER);
        
        container.getChildren().addAll(titleBox, fieldsBox, loginBtn, signupBox);
        root.getChildren().add(container);
        
        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
    }
    
    private void handleLogin(TextField emailField, HBox passwordBox) {
        String email = emailField.getText().trim();
        PasswordField passwordField = (PasswordField) passwordBox.getUserData();
        String password = passwordField.getText();
        
        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "Validation Error", "Please fill in all fields");
            return;
        }
        
        // TODO: Backend API call - POST /api/login
        // Send: {email, password}
        // Receive: {token, user_data}
        User user = userDatabase.get(email);
        
        if (user == null) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Email not found! Please create an account first.");
            return;
        }
        
        if (!user.password.equals(password)) {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Incorrect password! Please try again.");
            return;
        }
        
        currentUser = user;
        showHomePage();
    }
    
    // ========== HOME PAGE ==========
    private void showHomePage() {
        currentPage = "dashboard";
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f8f9fa;");
        
        HBox navbar = createNavbar();
        root.setTop(navbar);
        
        VBox mainContent = createDashboardContent();
        root.setCenter(mainContent);
        
        Scene scene = new Scene(root, 1400, 800);
        primaryStage.setScene(scene);
    }
    
    private HBox createNavbar() {
        HBox navbar = new HBox();
        navbar.setPadding(new Insets(15, 30, 15, 30));
        navbar.setStyle("-fx-background-color: white; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setSpacing(20);
        
        Label logoLabel = new Label("MyDashboard");
        logoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoLabel.setStyle("-fx-text-fill: #9d7b7b;");
        
        // Navigation buttons
        HBox navButtons = new HBox(10);
        navButtons.setAlignment(Pos.CENTER_LEFT);
        
        Button dashBtn = createNavButton("🏠 Dashboard", "dashboard");
        Button analyticsBtn = createNavButton("📊 Analytics", "analytics");
        Button tasksBtn = createNavButton("✓ Tasks", "tasks");
        Button settingsBtn = createNavButton("⚙️ Settings", "settings");
        
        navButtons.getChildren().addAll(dashBtn, analyticsBtn, tasksBtn, settingsBtn);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        // User profile section
        HBox profileSection = new HBox(15);
        profileSection.setAlignment(Pos.CENTER);
        
        Label usernameLabel = new Label(currentUser.name);
        usernameLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 15));
        usernameLabel.setStyle("-fx-text-fill: #333333;");
        
        StackPane profileIcon = createProfileIcon();
        
        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                          "-fx-font-size: 12px; -fx-padding: 8 20; " +
                          "-fx-background-radius: 15; -fx-cursor: hand;");
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            showLoginPage();
        });
        
        profileSection.getChildren().addAll(profileIcon, usernameLabel, logoutBtn);
        
        navbar.getChildren().addAll(logoLabel, navButtons, spacer, profileSection);
        
        return navbar;
    }
    
    private Button createNavButton(String text, String page) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; " +
                    "-fx-font-size: 14px; -fx-padding: 10 20; -fx-cursor: hand; " +
                    "-fx-border-radius: 8; -fx-background-radius: 8;");
        
        if (currentPage.equals(page)) {
            btn.setStyle("-fx-background-color: #9d7b7b; -fx-text-fill: white; " +
                        "-fx-font-size: 14px; -fx-padding: 10 20; -fx-cursor: hand; " +
                        "-fx-border-radius: 8; -fx-background-radius: 8;");
        }
        
        btn.setOnMouseEntered(e -> {
            if (!currentPage.equals(page)) {
                btn.setStyle("-fx-background-color: #f0f0f0; -fx-text-fill: #333333; " +
                            "-fx-font-size: 14px; -fx-padding: 10 20; -fx-cursor: hand; " +
                            "-fx-border-radius: 8; -fx-background-radius: 8;");
            }
        });
        
        btn.setOnMouseExited(e -> {
            if (!currentPage.equals(page)) {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #666666; " +
                            "-fx-font-size: 14px; -fx-padding: 10 20; -fx-cursor: hand; " +
                            "-fx-border-radius: 8; -fx-background-radius: 8;");
            }
        });
        
        btn.setOnAction(e -> navigateToPage(page));
        
        return btn;
    }
    
    private void navigateToPage(String page) {
        currentPage = page;
        BorderPane root = (BorderPane) primaryStage.getScene().getRoot();
        root.setTop(createNavbar());
        
        switch (page) {
            case "dashboard":
                root.setCenter(createDashboardContent());
                break;
            case "analytics":
                root.setCenter(createAnalyticsContent());
                break;
            case "tasks":
                root.setCenter(createTasksContent());
                break;
            case "settings":
                root.setCenter(createSettingsContent());
                break;
        }
    }
    
    private StackPane createProfileIcon() {
        StackPane profileIcon = new StackPane();
        
       
        
        Circle circle = new Circle(25);
        circle.setFill(Color.web("#9d7b7b"));
        circle.setStroke(Color.web("#8a6a6a"));
        circle.setStrokeWidth(2);
        
        String initial = currentUser.name.substring(0, 1).toUpperCase();
        Label initialLabel = new Label(initial);
        initialLabel.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        initialLabel.setStyle("-fx-text-fill: white;");
        
        profileIcon.getChildren().addAll(circle, initialLabel);
        
        return profileIcon;
    }
    
    // ========== DASHBOARD CONTENT ==========
    private VBox createDashboardContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        
        // Welcome header with animation
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label welcomeLabel = new Label("Welcome back, " + currentUser.name + "! 👋");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        welcomeLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label dateLabel = new Label(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM d, yyyy")));
        dateLabel.setFont(Font.font("Arial", 14));
        dateLabel.setStyle("-fx-text-fill: #7f8c8d;");
        
        VBox headerText = new VBox(5);
        headerText.getChildren().addAll(welcomeLabel, dateLabel);
        
        header.getChildren().add(headerText);
        
        // Stats cards
        HBox statsCards = new HBox(20);
        statsCards.setAlignment(Pos.CENTER);
        
        VBox tasksCard = createStatCard("📝", String.valueOf(userTasks.size()), "Total Tasks", "#3498db");
        VBox completedCard = createStatCard("✓", String.valueOf(userTasks.stream().filter(t -> t.completed).count()), "Completed", "#2ecc71");
        VBox pendingCard = createStatCard("⏳", String.valueOf(userTasks.stream().filter(t -> !t.completed).count()), "Pending", "#f39c12");
        VBox projectsCard = createStatCard("📊", "12", "Projects", "#9b59b6");
        
        statsCards.getChildren().addAll(tasksCard, completedCard, pendingCard, projectsCard);
        
        // Quick actions
        HBox quickActions = new HBox(20);
        quickActions.setAlignment(Pos.CENTER);
        
        VBox card1 = createDashboardCard("📊", "Analytics", "View your statistics and insights", () -> navigateToPage("analytics"));
        VBox card2 = createDashboardCard("⚙️", "Settings", "Manage your account preferences", () -> navigateToPage("settings"));
        VBox card3 = createDashboardCard("📝", "Tasks", "Organize your to-do list", () -> navigateToPage("tasks"));
        
        quickActions.getChildren().addAll(card1, card2, card3);
        
        // Recent activity
        VBox activitySection = createRecentActivitySection();
        
        content.getChildren().addAll(header, statsCards, quickActions, activitySection);
        
        // Fade in animation
        FadeTransition fade = new FadeTransition(Duration.millis(500), content);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        
        return content;
    }
    
    private VBox createStatCard(String icon, String value, String label, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(200, 150);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(40));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", 14));
        labelText.setStyle("-fx-text-fill: #7f8c8d;");
        
        card.getChildren().addAll(iconLabel, valueLabel, labelText);
        
        // Hover animation
        card.setOnMouseEntered(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.05);
            st.setToY(1.05);
            st.play();
        });
        
        card.setOnMouseExited(e -> {
            ScaleTransition st = new ScaleTransition(Duration.millis(200), card);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });
        
        return card;
    }
    
    private VBox createDashboardCard(String icon, String title, String description, Runnable action) {
        VBox card = new VBox(15);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(30));
        card.setPrefSize(300, 200);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
                     "-fx-cursor: hand;");
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(50));
        
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 13));
        descLabel.setStyle("-fx-text-fill: #7f8c8d;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(250);
        descLabel.setAlignment(Pos.CENTER);
        
        card.getChildren().addAll(iconLabel, titleLabel, descLabel);
        
        card.setOnMouseEntered(e -> 
            card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 15; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 5); " +
                         "-fx-cursor: hand;"));
        
        card.setOnMouseExited(e -> 
            card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2); " +
                         "-fx-cursor: hand;"));
        
        card.setOnMouseClicked(e -> action.run());
        
        return card;
    }
    
    private VBox createRecentActivitySection() {
        VBox section = new VBox(15);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Recent Activity");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setStyle("-fx-text-fill: #2c3e50;");
        
        VBox activities = new VBox(10);
        activities.getChildren().addAll(
            createActivityItem("Task completed", "Finished project documentation", "2 hours ago"),
            createActivityItem("New task added", "Review pull request #42", "5 hours ago"),
            createActivityItem("Settings updated", "Changed notification preferences", "1 day ago")
        );
        
        section.getChildren().addAll(title, activities);
        
        return section;
    }
    
    private HBox createActivityItem(String title, String description, String time) {
        HBox item = new HBox(15);
        item.setAlignment(Pos.CENTER_LEFT);
        item.setPadding(new Insets(10));
        item.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        Circle dot = new Circle(6, Color.web("#3498db"));
        
        VBox textBox = new VBox(3);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setStyle("-fx-text-fill: #7f8c8d;");
        textBox.getChildren().addAll(titleLabel, descLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Label timeLabel = new Label(time);
        timeLabel.setFont(Font.font("Arial", 11));
        timeLabel.setStyle("-fx-text-fill: #95a5a6;");
        
        item.getChildren().addAll(dot, textBox, spacer, timeLabel);
        
        return item;
    }
    
    // ========== ANALYTICS CONTENT ==========
    private ScrollPane createAnalyticsContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        
        Label titleLabel = new Label("Analytics Dashboard 📊");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // TODO: Fetch from backend - GET /api/analytics/activity
        VBox activityGraph = createActivityHeatmap();
        
        // Project quality metrics
        HBox metrics = new HBox(20);
        metrics.setAlignment(Pos.CENTER);
        
        VBox qualityChart = createProjectQualityChart();
        VBox performanceChart = createPerformanceChart();
        
        metrics.getChildren().addAll(qualityChart, performanceChart);
        
        // Statistics summary
        HBox stats = new HBox(20);
        stats.setAlignment(Pos.CENTER);
        
        VBox stat1 = createAnalyticCard("🔥", "45", "Day Streak", "#e74c3c");
        VBox stat2 = createAnalyticCard("⭐", "892", "Total Contributions", "#f39c12");
        VBox stat3 = createAnalyticCard("📈", "23%", "Growth Rate", "#2ecc71");
        VBox stat4 = createAnalyticCard("🎯", "94%", "Completion Rate", "#3498db");
        
        stats.getChildren().addAll(stat1, stat2, stat3, stat4);
        
        content.getChildren().addAll(titleLabel, activityGraph, metrics, stats);
        
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f8f9fa; -fx-background-color: #f8f9fa;");
        
        FadeTransition fade = new FadeTransition(Duration.millis(500), scroll);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        
        return scroll;
    }
    
    private VBox createActivityHeatmap() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Activity Contribution Graph");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        GridPane heatmap = new GridPane();
        heatmap.setHgap(3);
        heatmap.setVgap(3);
        
        LocalDate today = LocalDate.now();
        
        for (int week = 0; week < 52; week++) {
            for (int day = 0; day < 7; day++) {
                LocalDate date = today.minusWeeks(51 - week).minusDays(6 - day);
                int contributions = activityData.getOrDefault(date, 0);
                
                Rectangle cell = new Rectangle(12, 12);
                cell.setArcWidth(3);
                cell.setArcHeight(3);
                
                // Color based on contribution level (like GitHub)
                if (contributions == 0) {
                    cell.setFill(Color.web("#ebedf0"));
                } else if (contributions < 5) {
                    cell.setFill(Color.web("#9be9a8"));
                } else if (contributions < 10) {
                    cell.setFill(Color.web("#40c463"));
                } else if (contributions < 15) {
                    cell.setFill(Color.web("#30a14e"));
                } else {
                    cell.setFill(Color.web("#216e39"));
                }
                
                Tooltip tooltip = new Tooltip(contributions + " contributions on " + date);
                Tooltip.install(cell, tooltip);
                
                heatmap.add(cell, week, day);
            }
        }
        
        container.getChildren().addAll(title, heatmap);
        
        return container;
    }
    
    private VBox createProjectQualityChart() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.setPrefWidth(450);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Project Quality Metrics");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Score");
        
        BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);
        chart.setTitle("Quality Analysis");
        chart.setLegendVisible(false);
        chart.setPrefHeight(300);
        
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Code Quality", 85));
        series.getData().add(new XYChart.Data<>("Documentation", 92));
        series.getData().add(new XYChart.Data<>("Testing", 78));
        series.getData().add(new XYChart.Data<>("Security", 88));
        
        chart.getData().add(series);
        
        container.getChildren().addAll(title, chart);
        
        return container;
    }
    
    private VBox createPerformanceChart() {
        VBox container = new VBox(15);
        container.setPadding(new Insets(25));
        container.setPrefWidth(450);
        container.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                          "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Performance Trends");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Week");
        yAxis.setLabel("Tasks Completed");
        
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        chart.setTitle("Weekly Performance");
        chart.setLegendVisible(false);
        chart.setPrefHeight(300);
        
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>(1, 12));
        series.getData().add(new XYChart.Data<>(2, 18));
        series.getData().add(new XYChart.Data<>(3, 15));
        series.getData().add(new XYChart.Data<>(4, 22));
        series.getData().add(new XYChart.Data<>(5, 28));
        series.getData().add(new XYChart.Data<>(6, 24));
        
        chart.getData().add(series);
        
        container.getChildren().addAll(title, chart);
        
        return container;
    }
    
    private VBox createAnalyticCard(String icon, String value, String label, String color) {
        VBox card = new VBox(10);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(25));
        card.setPrefSize(250, 150);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label iconLabel = new Label(icon);
        iconLabel.setFont(Font.font(40));
        
        Label valueLabel = new Label(value);
        valueLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        valueLabel.setStyle("-fx-text-fill: " + color + ";");
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", 14));
        labelText.setStyle("-fx-text-fill: #7f8c8d;");
        
        card.getChildren().addAll(iconLabel, valueLabel, labelText);
        
        return card;
    }
    
    // ========== TASKS CONTENT ==========
    private ScrollPane createTasksContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        
        HBox header = new HBox(20);
        header.setAlignment(Pos.CENTER_LEFT);
        
        Label titleLabel = new Label("Task Management ✓");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button addTaskBtn = new Button("+ New Task");
        addTaskBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                           "-fx-font-size: 14px; -fx-padding: 12 24; " +
                           "-fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;");
        addTaskBtn.setOnMouseEntered(e -> 
            addTaskBtn.setStyle("-fx-background-color: #2980b9; -fx-text-fill: white; " +
                               "-fx-font-size: 14px; -fx-padding: 12 24; " +
                               "-fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;"));
        addTaskBtn.setOnMouseExited(e -> 
            addTaskBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                               "-fx-font-size: 14px; -fx-padding: 12 24; " +
                               "-fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;"));
        addTaskBtn.setOnAction(e -> showAddTaskDialog());
        
        header.getChildren().addAll(titleLabel, spacer, addTaskBtn);
        
        // Filter tabs
        HBox filterTabs = new HBox(10);
        filterTabs.setAlignment(Pos.CENTER_LEFT);
        
        Button allTab = createFilterTab("All (" + userTasks.size() + ")", true);
        Button activeTab = createFilterTab("Active (" + userTasks.stream().filter(t -> !t.completed).count() + ")", false);
        Button completedTab = createFilterTab("Completed (" + userTasks.stream().filter(t -> t.completed).count() + ")", false);
        
        filterTabs.getChildren().addAll(allTab, activeTab, completedTab);
        
        // Tasks list
        VBox tasksList = new VBox(15);
        
        if (userTasks.isEmpty()) {
            VBox emptyState = createEmptyTaskState();
            tasksList.getChildren().add(emptyState);
        } else {
            for (Task task : userTasks) {
                tasksList.getChildren().add(createTaskCard(task));
            }
        }
        
        content.getChildren().addAll(header, filterTabs, tasksList);
        
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f8f9fa; -fx-background-color: #f8f9fa;");
        
        FadeTransition fade = new FadeTransition(Duration.millis(500), scroll);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        
        return scroll;
    }
    
    private Button createFilterTab(String text, boolean active) {
        Button btn = new Button(text);
        String activeStyle = "-fx-background-color: #3498db; -fx-text-fill: white; " +
                            "-fx-font-size: 14px; -fx-padding: 10 20; " +
                            "-fx-background-radius: 8; -fx-cursor: hand;";
        String inactiveStyle = "-fx-background-color: white; -fx-text-fill: #666666; " +
                              "-fx-font-size: 14px; -fx-padding: 10 20; " +
                              "-fx-background-radius: 8; -fx-cursor: hand; " +
                              "-fx-border-color: #e0e0e0; -fx-border-width: 1; -fx-border-radius: 8;";
        
        btn.setStyle(active ? activeStyle : inactiveStyle);
        
        return btn;
    }
    
    private VBox createEmptyTaskState() {
        VBox empty = new VBox(20);
        empty.setAlignment(Pos.CENTER);
        empty.setPadding(new Insets(60));
        empty.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                      "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label icon = new Label("📝");
        icon.setFont(Font.font(80));
        
        Label title = new Label("No tasks yet");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        title.setStyle("-fx-text-fill: #2c3e50;");
        
        Label subtitle = new Label("Create your first task to get started!");
        subtitle.setFont(Font.font("Arial", 14));
        subtitle.setStyle("-fx-text-fill: #7f8c8d;");
        
        empty.getChildren().addAll(icon, title, subtitle);
        
        return empty;
    }
    
    private HBox createTaskCard(Task task) {
        HBox card = new HBox(20);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(20));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                     "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);");
        
        // Checkbox
        CheckBox checkBox = new CheckBox();
        checkBox.setSelected(task.completed);
        checkBox.setStyle("-fx-font-size: 16px;");
        checkBox.setOnAction(e -> {
            task.completed = checkBox.isSelected();
            // TODO: Backend update - PUT /api/tasks/{id}
            refreshTasksPage();
        });
        
        // Task info
        VBox taskInfo = new VBox(8);
        HBox.setHgrow(taskInfo, Priority.ALWAYS);
        
        Label titleLabel = new Label(task.title);
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        titleLabel.setStyle("-fx-text-fill: " + (task.completed ? "#95a5a6" : "#2c3e50") + ";");
        if (task.completed) {
            titleLabel.setStyle(titleLabel.getStyle() + "-fx-strikethrough: true;");
        }
        
        Label descLabel = new Label(task.description);
        descLabel.setFont(Font.font("Arial", 13));
        descLabel.setStyle("-fx-text-fill: #7f8c8d;");
        descLabel.setWrapText(true);
        
        HBox metadata = new HBox(15);
        metadata.setAlignment(Pos.CENTER_LEFT);
        
        Label priorityLabel = new Label(getPriorityIcon(task.priority) + " " + task.priority.toUpperCase());
        priorityLabel.setFont(Font.font("Arial", 11));
        priorityLabel.setStyle("-fx-text-fill: " + getPriorityColor(task.priority) + "; " +
                              "-fx-background-color: " + getPriorityBgColor(task.priority) + "; " +
                              "-fx-padding: 4 10; -fx-background-radius: 12;");
        
        if (task.dueDate != null) {
            Label dueDateLabel = new Label("📅 " + task.dueDate.format(DateTimeFormatter.ofPattern("MMM d")));
            dueDateLabel.setFont(Font.font("Arial", 11));
            dueDateLabel.setStyle("-fx-text-fill: #7f8c8d;");
            metadata.getChildren().add(dueDateLabel);
        }
        
        metadata.getChildren().add(0, priorityLabel);
        
        taskInfo.getChildren().addAll(titleLabel, descLabel, metadata);
        
        // Action buttons
        HBox actions = new HBox(10);
        actions.setAlignment(Pos.CENTER);
        
        Button editBtn = new Button("✏️");
        editBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; " +
                        "-fx-font-size: 16px; -fx-cursor: hand; -fx-padding: 8;");
        editBtn.setOnAction(e -> showEditTaskDialog(task));
        
        Button deleteBtn = new Button("🗑️");
        deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e74c3c; " +
                          "-fx-font-size: 16px; -fx-cursor: hand; -fx-padding: 8;");
        deleteBtn.setOnAction(e -> {
            // TODO: Backend delete - DELETE /api/tasks/{id}
            userTasks.remove(task);
            refreshTasksPage();
        });
        
        actions.getChildren().addAll(editBtn, deleteBtn);
        
        card.getChildren().addAll(checkBox, taskInfo, actions);
        
        // Hover effect
        card.setOnMouseEntered(e -> 
            card.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 12; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.15), 12, 0, 0, 3);"));
        card.setOnMouseExited(e -> 
            card.setStyle("-fx-background-color: white; -fx-background-radius: 12; " +
                         "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 8, 0, 0, 2);"));
        
        return card;
    }
    
    private String getPriorityIcon(String priority) {
        switch (priority.toLowerCase()) {
            case "high": return "🔴";
            case "medium": return "🟡";
            case "low": return "🟢";
            default: return "⚪";
        }
    }
    
    private String getPriorityColor(String priority) {
        switch (priority.toLowerCase()) {
            case "high": return "#e74c3c";
            case "medium": return "#f39c12";
            case "low": return "#2ecc71";
            default: return "#95a5a6";
        }
    }
    
    private String getPriorityBgColor(String priority) {
        switch (priority.toLowerCase()) {
            case "high": return "#ffe6e6";
            case "medium": return "#fff4e6";
            case "low": return "#e6f9e6";
            default: return "#f0f0f0";
        }
    }
    
    private void showAddTaskDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Task");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");
        
        Label title = new Label("Create New Task");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        TextField titleField = createStyledTextField("Task title");
        TextArea descArea = new TextArea();
        descArea.setPromptText("Task description");
        descArea.setPrefRowCount(3);
        descArea.setStyle("-fx-background-radius: 8; -fx-border-color: #e0e0e0; " +
                         "-fx-border-radius: 8; -fx-border-width: 1.5;");
        
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        priorityBox.setValue("Medium");
        priorityBox.setStyle("-fx-background-radius: 8;");
        priorityBox.setPrefWidth(350);
        
        DatePicker datePicker = new DatePicker();
        datePicker.setPromptText("Due date");
        datePicker.setPrefWidth(350);
        
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
                          "-fx-padding: 10 25; -fx-background-radius: 8; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Create Task");
        saveBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                        "-fx-padding: 10 25; -fx-background-radius: 8; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> {
            if (titleField.getText().trim().isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "Validation", "Please enter a task title");
                return;
            }
            
            Task newTask = new Task(
                titleField.getText().trim(),
                descArea.getText().trim(),
                priorityBox.getValue().toLowerCase(),
                datePicker.getValue()
            );
            
            // TODO: Backend create - POST /api/tasks
            userTasks.add(newTask);
            dialog.close();
            refreshTasksPage();
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(title, titleField, descArea, 
            new Label("Priority:"), priorityBox, 
            new Label("Due Date:"), datePicker, buttons);
        
        Scene scene = new Scene(content, 450, 550);
        dialog.setScene(scene);
        dialog.show();
    }
    
    private void showEditTaskDialog(Task task) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit Task");
        
        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: white;");
        
        Label title = new Label("Edit Task");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        
        TextField titleField = createStyledTextField("Task title");
        titleField.setText(task.title);
        
        TextArea descArea = new TextArea();
        descArea.setText(task.description);
        descArea.setPrefRowCount(3);
        descArea.setStyle("-fx-background-radius: 8; -fx-border-color: #e0e0e0; " +
                         "-fx-border-radius: 8; -fx-border-width: 1.5;");
        
        ComboBox<String> priorityBox = new ComboBox<>();
        priorityBox.getItems().addAll("High", "Medium", "Low");
        priorityBox.setValue(task.priority.substring(0, 1).toUpperCase() + task.priority.substring(1));
        priorityBox.setPrefWidth(350);
        
        DatePicker datePicker = new DatePicker();
        datePicker.setValue(task.dueDate);
        datePicker.setPrefWidth(350);
        
        HBox buttons = new HBox(15);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        
        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
                          "-fx-padding: 10 25; -fx-background-radius: 8; -fx-cursor: hand;");
        cancelBtn.setOnAction(e -> dialog.close());
        
        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                        "-fx-padding: 10 25; -fx-background-radius: 8; -fx-cursor: hand;");
        saveBtn.setOnAction(e -> {
            task.title = titleField.getText().trim();
            task.description = descArea.getText().trim();
            task.priority = priorityBox.getValue().toLowerCase();
            task.dueDate = datePicker.getValue();
            
            // TODO: Backend update - PUT /api/tasks/{id}
            dialog.close();
            refreshTasksPage();
        });
        
        buttons.getChildren().addAll(cancelBtn, saveBtn);
        
        content.getChildren().addAll(title, titleField, descArea, 
            new Label("Priority:"), priorityBox, 
            new Label("Due Date:"), datePicker, buttons);
        
        Scene scene = new Scene(content, 450, 550);
        dialog.setScene(scene);
        dialog.show();
    }
    
    private void refreshTasksPage() {
        navigateToPage("tasks");
    }
    
    // ========== SETTINGS CONTENT ==========
    private ScrollPane createSettingsContent() {
        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        
        Label titleLabel = new Label("Settings ⚙️");
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setStyle("-fx-text-fill: #2c3e50;");
        
        // Profile settings
        VBox profileSection = createProfileSettings();
        
        // Account settings
        VBox accountSection = createAccountSettings();
        
        // Notification settings
        VBox notificationSection = createNotificationSettings();
        
        // Appearance settings
        VBox appearanceSection = createAppearanceSettings();
        
        // Privacy settings
        VBox privacySection = createPrivacySettings();
        
        content.getChildren().addAll(titleLabel, profileSection, accountSection, 
                                     notificationSection, appearanceSection, privacySection);
        
        ScrollPane scroll = new ScrollPane(content);
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: #f8f9fa; -fx-background-color: #f8f9fa;");
        
        FadeTransition fade = new FadeTransition(Duration.millis(500), scroll);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
        
        return scroll;
    }
    
    private VBox createProfileSettings() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Profile Settings");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Profile picture section with proper spacing and alignment
        HBox profilePicSection = new HBox(20); // 20px spacing
        profilePicSection.setAlignment(Pos.CENTER_LEFT);

        // Profile icon
        StackPane currentAvatar = createProfileIcon();
        currentAvatar.setScaleX(2);
        currentAvatar.setScaleY(2);

        // Profile picture label and upload button in a VBox
        VBox avatarActions = new VBox(10); // 10px spacing
        Label avatarLabel = new Label("Profile Picture");
        avatarLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));

        Button uploadBtn = new Button("Upload New Photo");
        uploadBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; " +
                           "-fx-padding: 8 16; -fx-background-radius: 6; -fx-cursor: hand;");
        uploadBtn.setOnAction(e -> {
            // TODO: Implement file chooser and upload to backend
            showAlert(Alert.AlertType.INFORMATION, "Feature", "Profile picture upload will be implemented with backend integration");
        });

        avatarActions.getChildren().addAll(avatarLabel, uploadBtn);
        profilePicSection.getChildren().addAll(currentAvatar, avatarActions);
        
        // Name field
        VBox nameField = createSettingField("Full Name", currentUser.name);
        
        // Email field (read-only)
        VBox emailField = createSettingField("Email Address", currentUser.email);
        TextField emailTf = (TextField) emailField.getChildren().get(1);
        emailTf.setEditable(false);
        emailTf.setStyle(emailTf.getStyle() + "-fx-opacity: 0.6;");
        
        // Bio field
        Label bioLabel = new Label("Bio");
        bioLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        TextArea bioArea = new TextArea();
        bioArea.setPromptText("Tell us about yourself...");
        bioArea.setPrefRowCount(3);
        bioArea.setStyle("-fx-background-radius: 8; -fx-border-color: #e0e0e0; " +
                        "-fx-border-radius: 8; -fx-border-width: 1.5;");
        
        Button saveProfileBtn = new Button("Save Changes");
        saveProfileBtn.setStyle("-fx-background-color: #2ecc71; -fx-text-fill: white; " +
                               "-fx-padding: 12 24; -fx-background-radius: 8; -fx-cursor: hand; -fx-font-weight: bold;");
        saveProfileBtn.setOnAction(e -> {
            // TODO: Backend update - PUT /api/users/{id}
            showAlert(Alert.AlertType.INFORMATION, "Success", "Profile updated successfully!");
        });
        
        section.getChildren().addAll(title, profilePicSection, nameField, emailField, 
                                     bioLabel, bioArea, saveProfileBtn);
        
        return section;
    }
    
    private VBox createAccountSettings() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Account Security");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Change password
        VBox passwordSection = new VBox(15);
        Label passwordLabel = new Label("Change Password");
        passwordLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 16));
        
        HBox currentPwdBox = createPasswordField();
        ((PasswordField)currentPwdBox.getUserData()).setPromptText("Current password");
        
        HBox newPwdBox = createPasswordField();
        ((PasswordField)newPwdBox.getUserData()).setPromptText("New password");
        
        HBox confirmPwdBox = createPasswordField();
        ((PasswordField)confirmPwdBox.getUserData()).setPromptText("Confirm new password");
        
        Button changePwdBtn = new Button("Update Password");
        changePwdBtn.setStyle("-fx-background-color: #e67e22; -fx-text-fill: white; " +
                             "-fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        changePwdBtn.setOnAction(e -> {
            // TODO: Backend password change - POST /api/users/change-password
            showAlert(Alert.AlertType.INFORMATION, "Success", "Password changed successfully!");
        });
        
        passwordSection.getChildren().addAll(passwordLabel, currentPwdBox, newPwdBox, confirmPwdBox, changePwdBtn);
        
        // Two-factor authentication
        HBox twoFactorBox = new HBox(20);
        twoFactorBox.setAlignment(Pos.CENTER_LEFT);
        twoFactorBox.setPadding(new Insets(15));
        twoFactorBox.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        VBox twoFactorInfo = new VBox(5);
        Label twoFactorLabel = new Label("Two-Factor Authentication");
        twoFactorLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        Label twoFactorDesc = new Label("Add an extra layer of security to your account");
        twoFactorDesc.setFont(Font.font("Arial", 12));
        twoFactorDesc.setStyle("-fx-text-fill: #7f8c8d;");
        twoFactorInfo.getChildren().addAll(twoFactorLabel, twoFactorDesc);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        CheckBox twoFactorToggle = new CheckBox();
        twoFactorToggle.setStyle("-fx-font-size: 16px;");
        
        twoFactorBox.getChildren().addAll(twoFactorInfo, spacer, twoFactorToggle);
        
        section.getChildren().addAll(title, passwordSection, twoFactorBox);
        
        return section;
    }
    
    private VBox createNotificationSettings() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Notification Preferences");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        VBox notificationOptions = new VBox(10);
        
        notificationOptions.getChildren().addAll(
            createToggleSetting("Email Notifications", "Receive email updates about your activity", true),
            createToggleSetting("Task Reminders", "Get reminded about upcoming task deadlines", true),
            createToggleSetting("Weekly Summary", "Receive a weekly summary of your progress", false),
            createToggleSetting("Mobile Push Notifications", "Get push notifications on your mobile device", true)
        );
        
        section.getChildren().addAll(title, notificationOptions);
        
        return section;
    }
    
    private VBox createAppearanceSettings() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Appearance");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        // Theme selection
        VBox themeBox = new VBox(10);
        Label themeLabel = new Label("Theme");
        themeLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        HBox themeOptions = new HBox(15);
        
        ToggleGroup themeGroup = new ToggleGroup();
        
        RadioButton lightTheme = new RadioButton("Light");
        lightTheme.setToggleGroup(themeGroup);
        lightTheme.setSelected(true);
        lightTheme.setStyle("-fx-font-size: 13px;");
        
        RadioButton darkTheme = new RadioButton("Dark");
        darkTheme.setToggleGroup(themeGroup);
        darkTheme.setStyle("-fx-font-size: 13px;");
        
        RadioButton autoTheme = new RadioButton("Auto");
        autoTheme.setToggleGroup(themeGroup);
        autoTheme.setStyle("-fx-font-size: 13px;");
        
        themeOptions.getChildren().addAll(lightTheme, darkTheme, autoTheme);
        themeBox.getChildren().addAll(themeLabel, themeOptions);
        
        // Language selection
        VBox langBox = new VBox(10);
        Label langLabel = new Label("Language");
        langLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        ComboBox<String> langCombo = new ComboBox<>();
        langCombo.getItems().addAll("English", "Spanish", "French", "German", "Japanese");
        langCombo.setValue("English");
        langCombo.setPrefWidth(250);
        
        langBox.getChildren().addAll(langLabel, langCombo);
        
        section.getChildren().addAll(title, themeBox, langBox);
        
        return section;
    }
    
    private VBox createPrivacySettings() {
        VBox section = new VBox(20);
        section.setPadding(new Insets(25));
        section.setStyle("-fx-background-color: white; -fx-background-radius: 15; " +
                        "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.1), 10, 0, 0, 2);");
        
        Label title = new Label("Privacy & Data");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        
        VBox privacyOptions = new VBox(10);
        
        privacyOptions.getChildren().addAll(
            createToggleSetting("Profile Visibility", "Make your profile visible to others", true),
            createToggleSetting("Activity Status", "Show when you're active", false),
            createToggleSetting("Share Analytics", "Help improve the app by sharing usage data", true)
        );
        
        // Danger zone
        VBox dangerZone = new VBox(15);
        dangerZone.setPadding(new Insets(20));
        dangerZone.setStyle("-fx-background-color: #ffe6e6; -fx-background-radius: 8; -fx-border-color: #e74c3c; -fx-border-width: 1; -fx-border-radius: 8;");
        
        Label dangerTitle = new Label("⚠️ Danger Zone");
        dangerTitle.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        dangerTitle.setStyle("-fx-text-fill: #e74c3c;");
        
        Button exportDataBtn = new Button("Export My Data");
        exportDataBtn.setStyle("-fx-background-color: #95a5a6; -fx-text-fill: white; " +
                              "-fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        exportDataBtn.setOnAction(e -> {
            showAlert(Alert.AlertType.INFORMATION, "Export", "Your data export will be sent to your email");
        });
        
        Button deleteAccountBtn = new Button("Delete Account");
        deleteAccountBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; " +
                                 "-fx-padding: 10 20; -fx-background-radius: 8; -fx-cursor: hand;");
        deleteAccountBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Delete Account");
            confirm.setHeaderText("Are you sure?");
            confirm.setContentText("This action cannot be undone. All your data will be permanently deleted.");
            confirm.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // TODO: Backend delete - DELETE /api/users/{id}
                    showLoginPage();
                }
            });
        });
        
        HBox dangerButtons = new HBox(15);
        dangerButtons.getChildren().addAll(exportDataBtn, deleteAccountBtn);
        
        dangerZone.getChildren().addAll(dangerTitle, dangerButtons);
        
        section.getChildren().addAll(title, privacyOptions, dangerZone);
        
        return section;
    }
    
    private VBox createSettingField(String label, String value) {
        VBox fieldBox = new VBox(8);
        
        Label labelText = new Label(label);
        labelText.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        
        TextField field = new TextField(value);
        field.setPrefWidth(350);
        field.setStyle("-fx-background-radius: 8; -fx-border-color: #e0e0e0; " +
                      "-fx-border-radius: 8; -fx-border-width: 1.5; -fx-padding: 10;");
        
        fieldBox.getChildren().addAll(labelText, field);
        
        return fieldBox;
    }
    
    private HBox createToggleSetting(String title, String description, boolean defaultValue) {
        HBox box = new HBox(20);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setPadding(new Insets(15));
        box.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 8;");
        
        VBox textBox = new VBox(5);
        Label titleLabel = new Label(title);
        titleLabel.setFont(Font.font("Arial", FontWeight.MEDIUM, 14));
        Label descLabel = new Label(description);
        descLabel.setFont(Font.font("Arial", 12));
        descLabel.setStyle("-fx-text-fill: #7f8c8d;");
        descLabel.setWrapText(true);
        descLabel.setMaxWidth(600);
        textBox.getChildren().addAll(titleLabel, descLabel);
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        CheckBox toggle = new CheckBox();
        toggle.setSelected(defaultValue);
        toggle.setStyle("-fx-font-size: 16px;");
        
        box.getChildren().addAll(textBox, spacer, toggle);
        
        return box;
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
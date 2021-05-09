package com.Amezq;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;

public class LoginView {


    private Label title;
    private VBox mainLayout;
    private VBox registerLayout;
    private HBox buttonLayout;
    protected TextField usernameIn, passwordIn;
    private ObservableList<String> userType;
    final private ComboBox optionsMenu;
    private Scene scene;
    protected Button loginButton;
    protected Button registerBtn;
    protected Button backBtn, doneRegBtn;
    protected ArrayList<TextField> inputBoxes = new ArrayList<TextField>();
    protected Label invalidLogin = new Label("Incorrect username or password");


    LoginView()
    {
        userType = FXCollections.observableArrayList(
                "Admin",
                        "Nurse",
                        "Patient"
        );

        for (int i = 0; i < 13; i++) {
            inputBoxes.add(new TextField());
        }

        optionsMenu = new ComboBox(userType);
        mainLayout = new VBox(20.f);
        registerLayout = new VBox(5.f);
        registerLayout.setPrefSize(600, 600);
        usernameIn = new TextField("username");
        usernameIn.setMaxWidth(200.f);
        passwordIn = new TextField("password");
        passwordIn.setMaxWidth(200.f);
        loginButton = new Button("Login");
        loginButton.setMaxSize(120, 20);
        loginButton.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        registerBtn = new Button("Register");
        registerBtn.setMaxSize(100, 20);
        registerBtn.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        buttonLayout = new HBox(5);
        buttonLayout.getChildren().addAll(registerBtn, loginButton);
        buttonLayout.setPadding(new Insets(0,0,0,200));
        title = new Label("UIHealth Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setTextFill(Color.STEELBLUE);

        invalidLogin.setTextFill(Color.RED);
        invalidLogin.setVisible(false);

        mainLayout.getChildren().addAll(title, optionsMenu, usernameIn, passwordIn, buttonLayout, invalidLogin);
        mainLayout.setAlignment(Pos.CENTER);
        mainLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        mainLayout.setPadding(new Insets(40.f));
        registerLayout.setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        scene = new Scene(mainLayout, 600.f, 600.f, Color.LIGHTBLUE);
        createRegisterPatientPage();
    }

    public Scene getScene()
    {
        return this.scene;
    }

    public String getUsername()
    {
        return this.usernameIn.getText();
    }
    public String getPassword() {return this.passwordIn.getText();}
    public String getUserType() {return this.optionsMenu.getValue().toString();}

    private void createRegisterPatientPage() {

        Label title = new Label("New Patient Registration Page");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 25));
        title.setTextFill(Color.STEELBLUE);

        Label ssn = new Label("SSN");
        HBox box1 = new HBox(5);
        Label fname = new Label("First Name");
        HBox box2 = new HBox(5);
        Label mi = new Label("MI");
        HBox box3 = new HBox(5);
        Label lname = new Label("Last Name");
        HBox box4 = new HBox(5);
        Label Age = new Label("Age");
        HBox box5 = new HBox(5);
        Label gender = new Label("Gender");
        HBox box6 = new HBox(5);
        Label race = new Label("Race");
        HBox box7 = new HBox(5);
        Label occupation = new Label("Occupation");
        HBox box8 = new HBox(5);
        Label med_hist = new Label("Medical History");
        HBox box9 = new HBox(5);
        Label address = new Label("Address");
        HBox box10 = new HBox(5);
        Label phone = new Label("Phone#");
        HBox box11 = new HBox(5);
        Label username = new Label("username");
        HBox box12 = new HBox(5);
        Label password = new Label("Password");
        HBox box13 = new HBox(5);



        registerLayout.getChildren().add(title);
        box1.getChildren().addAll(inputBoxes.get(0), ssn);

        box2.getChildren().addAll(inputBoxes.get(1), fname);
        box3.getChildren().addAll(inputBoxes.get(2), mi);
        box4.getChildren().addAll(inputBoxes.get(3), lname);
        box5.getChildren().addAll( inputBoxes.get(4), Age);
        box6.getChildren().addAll(inputBoxes.get(5), gender);
        box7.getChildren().addAll(inputBoxes.get(6), race);
        box8.getChildren().addAll(inputBoxes.get(7), occupation);
        box9.getChildren().addAll(inputBoxes.get(8), med_hist);
        box10.getChildren().addAll(inputBoxes.get(9), address);
        box11.getChildren().addAll(inputBoxes.get(10), phone);
        box12.getChildren().addAll(inputBoxes.get(11), username);
        box13.getChildren().addAll(inputBoxes.get(12), password);

        backBtn = new Button("Back");
        doneRegBtn = new Button("Done");
        HBox btnLayout = new HBox(4);
        btnLayout.getChildren().addAll(backBtn, doneRegBtn);
        registerLayout.getChildren().addAll(box1, box2, box3, box4, box5, box6, box7, box8, box9
        , box10, box11, box12, box13, btnLayout);

        registerLayout.setPadding(new Insets(60, 0, 0, 150));

    }

    public void showRegisterPatientPage () {this.scene.setRoot(registerLayout);}
    public void showMainLogin() {this.scene.setRoot(mainLayout);}



}

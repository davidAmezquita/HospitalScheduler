package com.Amezq;

import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class AdminView {
    private Scene adminScene;
    private Label title;
    private VBox layout;
    private MenuBar bar;
    private Menu nurseMenu = new Menu("Nurses");
    private Menu vaccMenu = new Menu("Vaccines");
    private Menu infoMenu = new Menu("Information");
    protected MenuItem regNurse, updateNurse, delNurse;
    protected MenuItem updateVacc;
    protected MenuItem nurseInfo, patientInfo;
    private HBox row1 = new HBox(10.f);
    private HBox row2 = new HBox(2.f);
    private HBox row3 = new HBox(5.f);
    private HBox row4 = new HBox(2.f);
    protected HBox row5 = new HBox();
    protected ArrayList<TextField> inputBoxes = new ArrayList<TextField>();
    protected Button addNurseBtn = new Button("Add");
    protected Button deleteNurseBtn = new Button("Delete Nurse");
    protected Button findNurseBtn = new Button("Select Nurse");
    protected Button updateNurseBtn = new Button("Update Nurse");
    protected Button addVaccineBtn = new Button("Add");
    protected Button updateRepoBtn = new Button("Update");
    protected Button getNurseInfo = new Button("Get Nurse Info");
    protected Button getPatientInfo = new Button("Get Patient Info");
    protected ComboBox<String> vaccType;
    protected ComboBox<String> Nurses;
    protected ComboBox<String> patients;
    private Label vaccineInfoLabel = new Label("");
    protected Label feedBack = new Label();

    AdminView() {

        layout = new VBox(10.f);

        //create title
        title = new Label("Admin");
        title.setPrefWidth(600); title.setPrefHeight(50);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setPadding(new Insets(5, 0,0, 0));

        //create nurse submenus
        regNurse = new MenuItem("Register");
        updateNurse = new MenuItem("Update");
        delNurse = new MenuItem("Delete");
        nurseMenu.getItems().addAll(regNurse, updateNurse, delNurse);
        nurseMenu.setStyle("-fx-font-size: 10; -fx-padding: 15");

        //create vaccine submenu
        updateVacc = new MenuItem("Update");
        vaccMenu.getItems().add(updateVacc);
        vaccMenu.setStyle("-fx-font-size: 10; -fx-padding: 15");

        //create information submenus
        nurseInfo = new MenuItem("Nurse");
        patientInfo = new MenuItem("Patient");
        infoMenu.getItems().addAll(nurseInfo, patientInfo);
        infoMenu.setStyle("-fx-font-size: 10; -fx-padding: 15");

        bar = new MenuBar();
        bar.setMaxSize(600, 100);
        bar.setStyle("-fx-border-style: solid; -fx-border-color: black; -fx-text-color: white");
        bar.getMenus().addAll(nurseMenu, vaccMenu, infoMenu);

        for (int i = 0; i < 8; i++) {
            inputBoxes.add(new TextField());
            inputBoxes.get(i).setPrefWidth(65.f);
        }

        vaccType = new ComboBox<String>(FXCollections
                .observableArrayList("Pfizer", "Moderna", "Johnson & Johnson"));

        vaccineInfoLabel.setFont(Font.font("Arial", 20));
        vaccineInfoLabel.setTextFill(Color.DARKRED);

        Nurses = new ComboBox<String>();
        patients = new ComboBox<String>();

        row5.setPadding(new Insets(20, 0, 0, 0));
        row5.getChildren().add(feedBack);
        layout.getChildren().addAll(title, bar, row1, row2, row3, row4, row5);

        adminScene = new Scene(layout, 600, 600, Color.LIGHTGREEN);

    }

    public void displayFeedBackStr(String msg, boolean success) {
        this.feedBack.setVisible(true);
        this.feedBack.setText(msg);

        if (success) {
            this.feedBack.setTextFill(Color.GREEN);
        }
        else {
            this.feedBack.setTextFill(Color.RED);
        }
    }


    public Scene getScene() { return this.adminScene;}

    public void createRegMenu() {
        clearRows();

        Label id = new Label("Employe ID");
        Label fname = new Label("First Name");
        fname.setPadding(new Insets(0, 0, 0, 2));
        Label MI = new Label("MI");
        MI.setPadding(new Insets(0, 0, 0, 18));
        Label Lname = new Label("Last Name");
        Lname.setPadding(new Insets(0, 0, 0, 23));
        Label Age = new Label("Age");
        Age.setPadding(new Insets(0, 0, 0, 17));
        Label Gender = new Label("Gender");
        Gender.setPadding(new Insets(0, 0, 0, 28));
        Label Address = new Label("Address");
        Address.setPadding(new Insets(0, 0, 0, 18));
        Label Phone = new Label("Phone#");
        Phone.setPadding(new Insets(0, 0, 0, 18));

        row1.getChildren().addAll(id, fname, MI, Lname, Age, Gender, Address, Phone);

        for (int i = 0; i < 8; i++) {
            row2.getChildren().add(inputBoxes.get(i));
        }

        row2.getChildren().add(addNurseBtn);
    }

    public void showDeleteScreen(ArrayList<String> ids) {
        clearRows();

        Nurses.getItems().clear();
        Nurses.getItems().addAll(ids);

        Label delLabel = new Label("Delete nurse with ID: ");
        delLabel.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        row1.getChildren().addAll(delLabel, Nurses, deleteNurseBtn);
    }

    public void showUpdateNurseScreen(ArrayList<String> ids) {
        clearRows();

        Label label_1 = new Label("Select nurse to update: ");
        label_1.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        Nurses.getItems().clear();
        Nurses.getItems().addAll(ids);

        row1.getChildren().addAll(label_1, Nurses, findNurseBtn);
    }

    public void displayNurse(Nurse n) {
        row2.getChildren().clear();
        row3.getChildren().clear();
        row4.getChildren().clear();
        feedBack.setVisible(false);

        Label nInfo = new Label(n.getInfoToString());
        nInfo.setFont(Font.font("Arial", 20));
        nInfo.setTextFill(Color.DARKRED);
        row2.getChildren().add(nInfo);

        Label label2 = new Label("Enter what to update:\n" +
                "NurseID        First Name          MI          Last Name         Age           Gender");
        row3.getChildren().add(label2);

        for (int i = 1; i < 7; i++) {
            row4.getChildren().add(inputBoxes.get(i));
        }

        row4.getChildren().add(updateNurseBtn);
    }

    public void showUpdateVaccMenu(String info) {
        clearRows();

        Label label1 = new Label("Vaccine to update: ");
        label1.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        Label label2 = new Label("Add To Current Quantity: ");
        label2.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        Label label3 = new Label("Or Set Sew Quantity: ");
        label3.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        HBox addBox = new HBox(5);
        HBox setBox = new HBox(5);
        addBox.getChildren().addAll(label2, inputBoxes.get(0), addVaccineBtn);
        setBox.getChildren().addAll(label3, inputBoxes.get(1), updateRepoBtn);
        row1.getChildren().addAll(label1, vaccType);
        row2.getChildren().add(addBox);
        row3.getChildren().add(setBox);

        displayVaccInfo(info);
        row4.getChildren().add(vaccineInfoLabel);
    }

    public void displayVaccInfo(String info) {


        vaccineInfoLabel.setText(info);
    }

    public void showAllNurseInfoMenu(ArrayList<String> ids) {
        clearRows();
        Label label1 = new Label("Select Nurse: ");
        label1.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        Nurses.getItems().clear();
        for (String s : ids) {
            Nurses.getItems().add(s);
        }

        HBox hbox1 = new HBox(5);

        hbox1.getChildren().addAll(label1, Nurses, getNurseInfo);
        row1.getChildren().add(hbox1);

    }

    public void displayNurseInfo(String nurseInfo, String nurseSchedule) {
        row2.getChildren().clear();
        row3.getChildren().clear();
        row4.getChildren().clear();

        Label label2 = new Label(nurseInfo);
        Label label3 = new Label("Nurse Schedule:");
        Label label4 = new Label(nurseSchedule);

        label2.setFont(Font.font("Arial", FontWeight.NORMAL, 25));
        label2.setTextFill(Color.DARKRED);
        label3.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        label4.setFont(Font.font("Arial", FontWeight.NORMAL, 25));
        label4.setTextFill(Color.DARKRED);

        row2.getChildren().add(label2);
        row3.getChildren().add(label3);
        row4.getChildren().add(label4);
    }

    public void showPatientInfoMenu(ArrayList<String> ids) {
        clearRows();

        Label label1 = new Label("Select patient: ");
        label1.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        HBox box1 = new HBox(5);

        for(String s : ids) {
            patients.getItems().add(s);
        }

        box1.getChildren().addAll(label1, patients, getPatientInfo);

        row1.getChildren().addAll(box1);

    }

    public void displayPatientInfo(String patientInfo, ArrayList<String> schedule, String vaccHistory) {
        row2.getChildren().clear();
        row3.getChildren().clear();
        row4.getChildren().clear();

        Label label = new Label("Information         ");
        Label info = new Label(patientInfo);

        Label schedule1 = new Label();
        String temp  = "";
        for (String s : schedule) {
            temp += s + "\n";
        }
        schedule1.setText(temp);

        Label label1 = new Label("Schedule            ");
        Label vaccinations = new Label(vaccHistory);
        Label label2 = new Label("History    ");

        info.setFont(Font.font("Arial", 18));
        info.setTextFill(Color.DARKRED);
        schedule1.setFont(Font.font("Arial", 18));
        schedule1.setTextFill(Color.DARKRED);
        vaccinations.setFont(Font.font("Arial", 18));
        vaccinations.setTextFill(Color.DARKRED);

        label.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        label1.setFont(Font.font("Arial", FontWeight.BOLD, 15));
        label2.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        row2.getChildren().addAll(label, info);
        row3.getChildren().addAll(label1, schedule1);
        row4.getChildren().addAll(label2, vaccinations);
    }

    public void clearRows() {
        row1.getChildren().clear();
        row2.getChildren().clear();
        row3.getChildren().clear();
        row4.getChildren().clear();

        feedBack.setVisible(false);
    }
}

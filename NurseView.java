package com.Amezq;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;


public class NurseView {
    private Scene theScene;
    //labels
    private Label nurseLabel;
    private VBox layout;  //main layout
    //menu bar items
    private HBox row1, row2;
    private MenuBar bar = new MenuBar();
    private Menu accMenu = new Menu("Account info");
    private Menu scheduleMenu = new Menu("Schedule");
    private Menu vaccination = new Menu("Vaccination");
    protected MenuItem showInfo, updateInfo, updateSchedule;
    protected MenuItem cancelTime, vacc, viewPatientMedInfo;
    protected ArrayList<TextField> inputBoxes = new ArrayList<TextField>();
    protected Button updateBtn = new Button("Update");
    protected Button updateScheduleBtn = new Button("Save");
    protected Button deleteTime = new Button("Remove");
    protected Button recordVacc = new Button("Record Vaccination");
    protected Button selectPatient = new Button("View History");
    protected ListView<String> list = new ListView<String>();
    protected ComboBox<String> patients = new ComboBox<>();


    NurseView() {

        //set label
        nurseLabel = new Label("WELCOME BACK");
        nurseLabel.setPrefWidth(600); nurseLabel.setPrefHeight(50);
        nurseLabel.setFont(Font.font("Arial", 20));
        nurseLabel.setTextFill(Color.WHITE);
        nurseLabel.setPadding(new Insets(5, 0, 0, 200));
        nurseLabel.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        layout = new VBox(2);
        layout.setStyle("-fx-background-color: lightcyan");

        showInfo = new MenuItem("Show info");
        updateInfo = new MenuItem("Update Info");
        updateSchedule = new MenuItem("Schedule Time");
        cancelTime = new MenuItem("Cancel Time");
        vacc = new MenuItem("Record Vaccination");
        viewPatientMedInfo = new MenuItem("Patient History");

        accMenu.getItems().addAll(showInfo, updateInfo);
        accMenu.setStyle("-fx-font-size: 10; -fx-padding: 15");
        scheduleMenu.getItems().addAll(updateSchedule, cancelTime);
        scheduleMenu.setStyle("-fx-font-size: 10; -fx-padding: 15");
        vaccination.getItems().addAll(vacc, viewPatientMedInfo);
        vaccination.setStyle("-fx-font-size: 10; -fx-padding: 15");

        //add menus
        bar.getMenus().addAll(accMenu, scheduleMenu, vaccination);
        bar.setMaxSize(600, 100);
        bar.setBackground(new Background(new BackgroundFill(Color.CYAN, CornerRadii.EMPTY, Insets.EMPTY)));
        bar.setStyle("-fx-border-style: solid; -fx-border-color: white");

        row1 = new HBox(5.f);
        row2 = new HBox(5.f);

        for (int i = 0; i < 7; i++) {
            inputBoxes.add(new TextField());
        }

        layout.getChildren().addAll(nurseLabel, bar, row1, row2);

        this.theScene = new Scene(layout, 600, 600, Color.LIGHTGREEN);
    }

    public Scene getScene() {
        return this.theScene;
    }

    public void showUpdateScene() {
        row1.getChildren().clear();
        row2.getChildren().clear();

        Label updateLabel = new Label(" Address            Phone Number");
        updateLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        updateLabel.setStyle("-fx-margin-top: 10px");

        row1.getChildren().add(updateLabel);
        row2.getChildren().addAll(inputBoxes.get(0), inputBoxes.get(1), updateBtn);
    }

    public void displayNurseInfo(String theInfo, String schedule) {
        row1.getChildren().clear();
        row2.getChildren().clear();

        Label info = new Label(theInfo);
        info.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        row1.getChildren().add(info);

        Label nurseSchedule = new Label("Schedule: \n" + schedule);
        nurseSchedule.setFont(Font.font("Arial", 15));
        nurseSchedule.setTextFill(Color.BLACK);



        row2.getChildren().add(nurseSchedule);
        row2.setPadding(new Insets(10, 0 ,10, 0));
        row2.setStyle("-fx-border-style: solid; -fx-border-color: white; -fx-border-width: 5px;");

    }

    public void showUpdateScheduleMenu(ArrayList<String> times) {
        row1.getChildren().clear();
        row2.getChildren().clear();

        Label label1 = new Label("Available times:");
        label1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        label1.setPadding(new Insets(10, 0, 0 ,0));
        row1.getChildren().add(label1);


        list.setPrefSize(500, 400);
        int size = times.size();
        for (int i = 0; i < size; i++) {
            list.getItems().add(times.get(i));
        }

        row2.getChildren().addAll(list, updateScheduleBtn);

    }

    public void showCancelScheduleMenu(ArrayList<String> times) {
        row1.getChildren().clear();
        row2.getChildren().clear();

        Label label1 = new Label("Choose a time to remove:");
        label1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        row1.getChildren().add(label1);

        list.setPrefSize(400, 400);

        for (String s : times) {
            list.getItems().add(s);
        }

        row2.getChildren().addAll(list, deleteTime);

    }

    public void showVaccinationMenu(ArrayList<String> thePatients, String vacInfo) {
        row1.getChildren().clear();
        row2.getChildren().clear();

        patients.getItems().clear();
        patients.getItems().addAll(thePatients);

        Label patientID = new Label("Select patient: ");
        patientID.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        row1.getChildren().addAll(patientID, patients, recordVacc);

        row2.getChildren().addAll(new Label("Your patients: \n"), new Label(vacInfo));

    }

    public void showVaccUpdate(String newVacInfo) {
        row2.getChildren().clear();
        row2.getChildren().addAll(new Label("Your patients: "), new Label(newVacInfo));
    }

    public void showPatientHistoryMenu(ArrayList<String> patients) {
        row1.getChildren().clear();
        row2.getChildren().clear();
        this.patients.getItems().clear();

        this.patients.getItems().addAll(patients);

        Label label = new Label("Select patient: ");
        label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        row1.getChildren().addAll(label, this.patients, selectPatient);
    }

    public void showPatientHistory(String hist) {
        row2.getChildren().clear();
        if (hist == null) {
            Label label = new Label("\nNo history to display");
            label.setFont(Font.font("Arial", 15));
            label.setTextFill(Color.RED);
            row2.getChildren().add(label);

        }else {
            Label label = new Label("\n"+hist);
            label.setFont(Font.font("Arial", 15));
            label.setTextFill(Color.DARKRED);
            row2.getChildren().add(label);
        }
    }





}

package com.Amezq;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;

public class PatientView {
    private Scene scene;
    private VBox layout;
    private HBox row1 , row2;
    private Label title;
    private MenuBar bar;
    private Menu infoMenu, vaccinationsMenu;
    protected MenuItem updateInfo, viewInfo, schedueVacc, cancelVacc, viewVacInfo;
    protected ArrayList<TextField> inputBoxes;
    protected Button updateBtn = new Button("Update Information");
    protected ListView<String> list = new ListView<String>();
    protected Button scheduleBtn = new Button("Select");
    protected Button cancelBtn = new Button("Cancel");

    PatientView() {

        layout = new VBox();
        layout.setBackground(new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY)));
        title = new Label("  WELCOME BACK");
        title.setPrefSize(600, 50);
        title.setTextFill(Color.WHITE);
        title.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        title.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));

        infoMenu = new Menu("Information");
        infoMenu.setStyle("-fx-font-size: 10; -fx-padding: 15");
        vaccinationsMenu = new Menu("Vaccination");
        vaccinationsMenu.setStyle("-fx-font-size: 10; -fx-padding: 15");
        updateInfo = new MenuItem("Update Info");
        viewInfo = new MenuItem("View Info");
        schedueVacc = new MenuItem("Schedule Vaccination");
        cancelVacc = new MenuItem("Cancel Vaccination");
        viewVacInfo = new MenuItem("Vaccine Info");
        infoMenu.getItems().addAll(viewInfo, updateInfo);
        vaccinationsMenu.getItems().addAll(schedueVacc, cancelVacc, viewVacInfo);
        bar = new MenuBar();
        bar.getMenus().addAll(infoMenu, vaccinationsMenu);

        bar.setMaxSize(600, 100);
        bar.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        bar.setStyle("-fx-border-style: solid; -fx-border-color: white");

        row1 = new HBox(2);
        row2 = new HBox(2);
        layout.getChildren().addAll(title, bar, row1, row2);


        inputBoxes = new ArrayList<TextField>();
        list.setPrefSize(590, 300);

        for (int i = 0; i < 11; i++) {
            inputBoxes.add(new TextField());
        }

        scene = new Scene(layout, 600, 600);

    }

    public Scene getScene() {return this.scene;}

    public void showViewInfoMenu(String info, String schedule, String history) {
        row1.getChildren().clear();
        row2.getChildren().clear();

        Label label1 = new Label("\n"+info);
        label1.setFont(Font.font("Arial", FontWeight.BOLD, 15));

        row1.getChildren().add(label1);
        Label label2 = new Label();
        if (schedule == null) {
            label2.setText("\nSchedule Vaccination: "+ "No appointments on record.");
        }
        else {
            label2.setText("\nSchedule Vaccination: "+ schedule.substring(3));
        }


        label2.setFont(Font.font("Arial", 20));
        label2.setTextFill(Color.DARKRED);


        Label label3 = new Label("\nHistory: "+ history);
        label3.setFont(Font.font("Arial", 10));
        label3.setTextFill(Color.DARKRED);

        VBox v1 = new VBox(10);
        v1.getChildren().addAll(label2, label3);

        row2.getChildren().add(v1);

    }

    public void showUpdateMenu() {
        row1.getChildren().clear();
        row2.getChildren().clear();

        Label label = new Label("Enter information to be updated: ");
        label.setFont(Font.font("Arial", FontPosture.REGULAR, 20));
        label.setTextFill(Color.GRAY);
        row1.getChildren().add(label);

        VBox col1 = new VBox(18);
        col1.setBackground(new Background(new BackgroundFill(Color.WHITESMOKE, CornerRadii.EMPTY, Insets.EMPTY)));
        col1.setStyle("-fx-border-style: solid; -fx-border-color: gray");

        col1.getChildren().addAll(
                new Label("SSN: "),
                new Label("First Name: "),
                new Label("MI: "),
                new Label("Last Name: "),
                new Label("Age: "),
                new Label("Gender: "),
                new Label("Race: "),
                new Label("Medical History: "),
                new Label("Occupation: "),
                new Label("Address: "),
                new Label("Phone#: ")
        );

        for (Node n : col1.getChildren()) {
            n.setStyle("-fx-font-size: 20; -fx-font-family: Arial; -fx-font-weight: bold;" +
                        "-fx-padding-top: 5px;");


        }
        row2.getChildren().add(col1);

        VBox col2 = new VBox(16);
        for(TextField t: inputBoxes) {
            col2.getChildren().add(t);
        }

        row2.getChildren().addAll(col2, updateBtn);


    }

    public void showScheduleMenu(ArrayList<String> times) {
        row1.getChildren().clear();
        row2.getChildren().clear();

        if (times == null) {
            Label label = new Label("Cannot schedule new time until you've received " +
                    "\nyour currently scheduled vaccination.");
            label.setTextFill(Color.STEELBLUE);
            label.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            row1.getChildren().add(label);
        }
        else {

            Label label = new Label("Select a time to schedule a vaccination: ");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            row1.getChildren().addAll(label, scheduleBtn);

            list.getItems().clear();

            for (String s : times) {
                list.getItems().add(s);
            }

            row2.getChildren().add(list);
        }
    }

    public void showCancelMenu(ArrayList<String> times) {
        row1.getChildren().clear();
        row2.getChildren().clear();

        if (times.size() <= 0) {
            Label label = new Label("No schedule available");
            label.setTextFill(Color.RED);
            label.setFont(Font.font("Arial", FontWeight.BOLD, 25));
            row1.getChildren().add(label);
        }
        else {
            Label label = new Label("You may cancel the following vaccination: ");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 15));
            label.setPadding(new Insets(10, 0, 0, 0));


            list.getItems().clear();
            list.getItems().addAll(times);

            row1.getChildren().add(label);
            cancelBtn.setPrefSize(100, 33);
            row2.getChildren().addAll(list, cancelBtn);
        }


    }

    public void showVacInfoMenu(String name, String info) {
        row1.getChildren().clear();
        row2.getChildren().clear();

        if (name == null || info == null) {
            Label label = new Label("No information to display");
            label.setFont(Font.font("Arial", FontWeight.BOLD, 25));
            label.setTextFill(Color.RED);
        }
        else {

            Label label = new Label("Information about your vaccine: " + name);
            label.setFont(Font.font("Arial", FontWeight.BOLD, 20));
            label.setTextFill(Color.STEELBLUE);

            row1.getChildren().add(label);

            Label theInfo = new Label(info);
            theInfo.setFont(Font.font("Arial", 15));
            theInfo.setPadding(new Insets(10, 0, 0, 0));

            row2.getChildren().add(theInfo);
        }
    }
}

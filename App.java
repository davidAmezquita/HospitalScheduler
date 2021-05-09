package com.Amezq;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.ArrayList;


/**
 * ModelView of UIHealth. Decides which View to show.
 * Retrieves data from the Model. Sends data back to View to display.
 */
public class App extends Application {

    private String userType;
    private AdminView adminView = null;
    private NurseView nurseView = null;
    private PatientView patientView = null;
    //private Model database;
    private String patient = null;
    private String nurseID = null;

    @Override
    public void start(Stage stage) {
        stage.setTitle("UIHealth");
        LoginView loginView =  new LoginView();
        Model model = new Model();
        model.initConnection();

        patientView = new PatientView();
        nurseView = new NurseView();
        adminView = new AdminView();

        //takes user to patient registration page
        loginView.registerBtn.setOnAction(e-> {
            loginView.showRegisterPatientPage();
        });

        loginView.backBtn.setOnAction(e-> {
            loginView.showMainLogin();
        });

        loginView.doneRegBtn.setOnAction(e->{
            StringBuilder info = new StringBuilder();
            for (TextField t : loginView.inputBoxes) {
                info.append(t.getText()).append("@");
            }

            String id = model.registerPatient(info.toString());
            if(id != null) {
                patient = id;
                stage.setScene(patientView.getScene());
            }

        });

        loginView.loginButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                userType = loginView.getUserType();

                System.out.println(userType);
                if(userType.equals("Nurse"))
                {
                    nurseID = model.verifyNurse(loginView.getUsername(), loginView.getPassword());
                    if (nurseID == null) {
                        //invalid credentials
                        loginView.invalidLogin.setVisible(true);
                        loginView.usernameIn.requestFocus();
                    }
                    else {
                        loginView.invalidLogin.setVisible(false);
                        stage.setScene(nurseView.getScene());
                        stage.show();
                    }
                }
                else if (userType.equals("Patient")) {
                    patient = model.verifyPatient(loginView.getUsername(), loginView.getPassword());

                    if (patient == null) {
                        //invalid credentials
                        loginView.invalidLogin.setVisible(true);
                        loginView.usernameIn.requestFocus();
                    }
                    else{
                        loginView.invalidLogin.setVisible(false);
                        stage.setScene(patientView.getScene());
                        stage.show();
                    }
                }
                else if(userType.equals("Admin")) {
                    stage.setScene(adminView.getScene());
                    stage.show();
                }
            }
        });


        //Implement nurse view controller ---------------------------------------------------
        nurseView.showInfo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Nurse temp = model.getNurseInfo(nurseID);
                nurseView.displayNurseInfo(temp.getInfoToString(), temp.getSchedule());
            }
        });

        //update the information of the nurse
        nurseView.updateInfo.setOnAction(e-> {nurseView.showUpdateScene();});
        nurseView.updateBtn.setOnAction(e-> {
            model.updateNurse_N(nurseID,
                                nurseView.inputBoxes.get(0).getText(),
                                nurseView.inputBoxes.get(1).getText());
            nurseView.inputBoxes.get(0).clear();
            nurseView.inputBoxes.get(1).clear();
        });

        //get times available for scheduling
        nurseView.updateSchedule.setOnAction(e-> {
            nurseView.list.getItems().clear();  //clear list before updating it with new info
            ArrayList<String> s = model.getAvailableTime(nurseID);
            nurseView.showUpdateScheduleMenu(s);


        });

        //get selected time slot and added to the nurse schedule
        nurseView.updateScheduleBtn.setOnAction(e -> {
            ObservableList<String> selectedDate = nurseView.list.getSelectionModel().getSelectedItems();
            String[] time_slot = selectedDate.get(0).split(":");

            model.updateNurseSchedule(nurseID, time_slot[0]);

            nurseView.list.getItems().remove(selectedDate.get(0));

        });

        //allow nurse to remove time schedule from database
        nurseView.cancelTime.setOnAction(e-> {
            nurseView.list.getItems().clear();
            ArrayList<String> s = model.getNurseSchedule(nurseID);
            nurseView.showCancelScheduleMenu(s);
        });

        nurseView.deleteTime.setOnAction(e-> {
            ObservableList<String> selectedDate = nurseView.list.getSelectionModel().getSelectedItems();
            String[] time_slot = selectedDate.get(0).split(":");
            model.removeNurseScheduledTime(nurseID, time_slot[0]);
            nurseView.list.getItems().remove(selectedDate.get(0));
        });


        //display vaccination menu
        nurseView.vacc.setOnAction(e-> {
            String s = model.getVaccinationRecord(nurseID);
            ArrayList<String> patients = model.getNursePatients(nurseID);
            nurseView.showVaccinationMenu(patients, s);
        });

        //record vaccination
        nurseView.recordVacc.setOnAction(e-> {
            String patientID = nurseView.patients.getValue();
            model.recordVaccination(patientID);
            String s = model.getVaccinationRecord(nurseID);
            nurseView.showVaccUpdate(s);

        });

        //view medical history of patient
        nurseView.viewPatientMedInfo.setOnAction(e->{
            ArrayList<String> patients = model.getNursePatients(nurseID);
            nurseView.showPatientHistoryMenu(patients);
        });

        nurseView.selectPatient.setOnAction(e->{
            String thePatient = nurseView.patients.getValue();
            String s = model.getPatientMedicalHist(thePatient);
            nurseView.showPatientHistory(s);


        });

        //-------------------------------------------------------------------End of nurse view controller


        //AdminView controller ------------------------------------------------------------------------------------

        //display the register nurse screen on the admin view
        adminView.regNurse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                adminView.createRegMenu();
            }
        });

        //add nurse to database
        adminView.addNurseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                model.registerNurse(
                        adminView.inputBoxes.get(0).getText(),
                        adminView.inputBoxes.get(1).getText(),
                        adminView.inputBoxes.get(2).getText(),
                        adminView.inputBoxes.get(3).getText(),
                        adminView.inputBoxes.get(4).getText(),
                        adminView.inputBoxes.get(5).getText(),
                        adminView.inputBoxes.get(6).getText(),
                        adminView.inputBoxes.get(7).getText()
                );

                for (TextField t : adminView.inputBoxes) {
                    t.clear();
                }
            }
        });

        //display delete nurse menu on the view
        adminView.delNurse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                ArrayList<String> ids = model.getAllNurses();
                adminView.showDeleteScreen(ids);
            }
        });

        //delete a nurse from the database
        adminView.deleteNurseBtn.setOnAction(e-> {
            String nurseID = adminView.Nurses.getValue();
            model.deleteNurse(nurseID);
            adminView.Nurses.getItems().remove(nurseID);
            //adminView.displayFeedBackStr("Nurse successfully deleted", true);
        });

        //display the update nurse menu to the view
        adminView.updateNurse.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                ArrayList<String> ids = model.getAllNurses();
                adminView.showUpdateNurseScreen(ids);
            }
        });

        adminView.findNurseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String id = adminView.Nurses.getValue();
                adminView.displayNurse(model.getNurseInfo(id));
            }
        });

        //update nurse database
        adminView.updateNurseBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String id = adminView.Nurses.getValue();
                model.updateNurse_A(
                        id,
                        adminView.inputBoxes.get(1).getText(),
                        adminView.inputBoxes.get(2).getText(),
                        adminView.inputBoxes.get(3).getText(),
                        adminView.inputBoxes.get(4).getText(),
                        adminView.inputBoxes.get(5).getText(),
                        adminView.inputBoxes.get(6).getText()
                );

                for (TextField t : adminView.inputBoxes) {
                    t.clear();
                }

                adminView.displayNurse(model.getNurseInfo(id));
                adminView.displayFeedBackStr("Update successful", true);

            }
        });

        //show update vaccine menu
        adminView.updateVacc.setOnAction(e-> {
            String s = model.getRepoInfo();
            adminView.showUpdateVaccMenu(s);
        });

        adminView.addVaccineBtn.setOnAction(e-> {
            model.updateVaccineRepo(
                    adminView.inputBoxes.get(0).getText(),
                    adminView.vaccType.getValue().toString(),
                    0
            );

            adminView.inputBoxes.get(0).clear();
            String s = model.getRepoInfo();
            adminView.displayVaccInfo(s);
        });

        adminView.updateRepoBtn.setOnAction(e-> {
            model.updateVaccineRepo(
                    adminView.inputBoxes.get(1).getText(),
                    adminView.vaccType.getValue().toString(),
                    1
            );
            adminView.inputBoxes.get(1).clear();
            String s = model.getRepoInfo();
            adminView.displayVaccInfo(s);
        });

        adminView.nurseInfo.setOnAction(e-> {
            ArrayList<String> ids = model.getAllNurses();
            adminView.showAllNurseInfoMenu(ids);
        });


        adminView.getNurseInfo.setOnAction(e-> {
            String nurseID = adminView.Nurses.getValue();
            Nurse temp = model.getNurseInfo(nurseID);
            adminView.displayNurseInfo(temp.getInfoToString(), temp.getSchedule());
        });

        adminView.patientInfo.setOnAction(e-> {
            ArrayList<String> ids = model.getAllPatients();
            adminView.showPatientInfoMenu(ids);
        });

        adminView.getPatientInfo.setOnAction(e-> {
            String id = adminView.patients.getValue();
            String pInfo = model.getPatientInfo(id);
            ArrayList<String> pSchedule = model.getPatientSchedule(id);
            String vaccHist = model.getVaccHistory(id);


            adminView.displayPatientInfo(pInfo, pSchedule ,vaccHist);
        });

        //------------------------------------------------------------------------------------end admin functionality

        //patient controller --------------------------------------------------------------------------------

        //get patient information from the database and display it to the paitent view
        patientView.viewInfo.setOnAction(e-> {
            String s = model.getPatientInfo(patient);
            ArrayList<String> s2 = model.getPatientSchedule(patient);
            String schedule;
            if (s2.isEmpty()) {
                schedule = null;
            }
            else {
                schedule = s2.get(0);
            }

            String history = model.getVaccHistory(patient);

            if (history.isBlank()) {
                history = "No vaccination history on record.";
            }


            patientView.showViewInfoMenu(s, schedule, history);
        });

        //display update menu
        patientView.updateInfo.setOnAction(e-> {
            patientView.showUpdateMenu();
        });

        //grab information entered in the update menu and send it to the database
        patientView.updateBtn.setOnAction(e->{
            ArrayList<String> info = new ArrayList<String>();


            boolean noSSNFlag = false;
            for (TextField t : patientView.inputBoxes) {
                if (t.getText().equals("") && !noSSNFlag) {
                    info.add(patient);
                    noSSNFlag = true;
                }
                else {
                    info.add(t.getText());
                }
                t.clear();
            }

            model.updatePatientInfo(info, noSSNFlag);
        });

        //get available times for scheduling patient
        patientView.schedueVacc.setOnAction(e-> {
            ArrayList<String> times = model.getAvailableSchedulePatient(patient);
            patientView.showScheduleMenu(times);
        });

        //schedule patients vaccination
        patientView.scheduleBtn.setOnAction(e-> {
            ObservableList<String> timeSelected = patientView.list.getSelectionModel().getSelectedItems();
            String[] s = timeSelected.get(0).split(":");

            model.schedulePatient(patient, s[0], s[s.length - 1]);
            patientView.list.getItems().remove(timeSelected.get(0));

        });

        //get the patient schedule
        patientView.cancelVacc.setOnAction(e->{
            ArrayList<String> s = model.getPatientSchedule(patient);
            patientView.showCancelMenu(s);
        });

        patientView.cancelBtn.setOnAction(e-> {
            String timeSelected = patientView.list.getSelectionModel().getSelectedItems().get(0);
            String[] s = timeSelected.split(":");
            model.cancelPatientSchedule(patient, s[0]);

            patientView.list.getItems().remove(timeSelected);
        });

        //view vaccine information
        patientView.viewVacInfo.setOnAction(e->{
            String vacName = model.getPatientVaccine(patient);
            String vacInfo = model.getVaccineInfo(vacName);
            patientView.showVacInfoMenu(vacName, vacInfo);
        });


        //-------------------------------------------------------------------------------------end of patient controller

        stage.setOnCloseRequest(e->{model.closeConnection();});
        stage.setScene(loginView.getScene());
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }

}
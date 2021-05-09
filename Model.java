package com.Amezq;

import java.sql.*;
import java.util.ArrayList;


public class Model {

    private Connection conn = null;
    private String url = "***";
    Model(){
    }

    public void initConnection() {
        try {
            conn = DriverManager.getConnection(url, "***", "****!");
        }catch (SQLException e)
        {
            System.out.println("connection failed");
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public void closeConnection() {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    //functions for admin view ------------------------------------------------------------------
    public ArrayList<String> getAllNurses() {
        ArrayList<String> nurseIDs = new ArrayList<String>();

        String query = "select EmpID from nurse;";

        try(Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            while(set.next()) {
                nurseIDs.add(set.getString("EmpID"));
            }
        }catch(SQLException e) {
            e.getSQLState();
        }

        return nurseIDs;
    }

    public ArrayList<String> getAllPatients() {
        ArrayList<String> ids = new ArrayList<String>();

        String query = "select SSN from patient;";

        try(Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            while (set.next()) {
                ids.add(set.getString("SSN"));
            }
        }catch (SQLException e) {
            e.getSQLState();
        }

        return ids;
    }

    public Nurse getNurseInfo(String nurseID) {

        String query = "select * from nurse where nurse.EmpID = "+ nurseID;
        Nurse nurse = null;
        if (conn != null) {

            try (Statement stm = conn.createStatement()) {
                ResultSet set = stm.executeQuery(query);
                while(set.next()) {
                    int EmpID = set.getInt("EmpID");
                    int Age = set.getInt("Age");
                    String Fname = set.getString("Fname");
                    String MI = set.getString("MI");
                    String Lname = set.getString("Lname");
                    String Gender = set.getString("Gender");
                    String Address = set.getString("Address");
                    String Phone_Num = set.getString("Phone_Num");

                    nurse = new Nurse(EmpID, Age, Fname, MI, Lname, Gender, Address, Phone_Num);
                }
            }catch (SQLException e) {
                e.getSQLState();
            }
        }


        String query2 = String.format("select * from schedule where Time_slot in " +
                "( select Time_slot from nurse_schedule where NurseID = %s) order by Time_slot;", nurseID);

        String schedule = "";
        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query2);

            while (set.next()) {

                int start = Integer.parseInt(set.getString("Start_time"));
                schedule += set.getString("theDate") + ": ";
                schedule += set.getString("theDay") + " ";
                schedule += set.getString("Start_time") + ":00" + "-";
                schedule += set.getString("End_time") + ":00";

                if (start >= 8 && start < 12) {
                    schedule += " am\n";
                }
                else {
                    schedule += " pm\n";
                }
            }
        }catch (SQLException e) {
            e.getSQLState();
        }

        //System.out.println(schedule);
        if (nurse != null) {
            nurse.setSchedule(schedule);
        }

        return nurse;
    }

    public void registerNurse(
            String id, String fname, String mi, String Lname, String Age,
            String Gender, String Address, String Phone_Num)
    {

        String query = String.format("insert into nurse values (" +
                "'%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s' );",
                id, fname, mi, Lname, Age, Gender, Address, Phone_Num);


        String userName = fname.substring(0, 3) + Phone_Num.substring(8, 10);

        String nurseAccount = String.format("insert into n_accounts values ("+
                "'%s', '%s', '%s' );",id, userName, "password");


        try {
            if (!conn.isClosed()) {
                System.out.println("Adding nurse");
                Statement stm = conn.createStatement();
                stm.executeUpdate(query);
                stm.executeUpdate(nurseAccount);
            }
        }catch (SQLException e)
        {
            e.getSQLState();
        }

    }

    public void deleteNurse(String nurseID) {
        String query = "delete from nurse where EmpID = " + nurseID;

        updateDB(query);
    }

    //update nurse based on admin configurations
    public void updateNurse_A(String origID, String newID, String fname, String mi, String Lname, String Age,
                            String Gender) {


        String query = "update nurse set ";

        boolean prevArg = false;
        if (!newID.equals("")) {
            query += "EmpID = "+newID;
            prevArg = true;
        }
        if(!fname.equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }

            query += "Fname = '"+fname+ "'";
        }
        if(!mi.equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }
            query += "MI = '"+mi+ "'";
        }
        if(!Lname.equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }

            query += "Lname = '"+Lname+ "'";
        }
        if(!Age.equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }
            query += "Age = "+Age;
        }
        if(!Gender.equals(""))
        {
            if (prevArg) { query += ", "; }
            query += "Gender = '"+Gender+ "'";
        }

        query += " where EmpID = " + origID;

        System.out.println(query);

        updateDB(query);

    }

    public String getRepoInfo() {
        String info = "";

        String query = "select * from repository;";

        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            while (set.next()) {
                info += String.format("Vaccine Name: %s | Amount: %s \n",
                                        set.getString("Vacc_Name"), set.getString("Available"));
            }
        }catch (SQLException e ) {
            e.getSQLState();
        }
        return info;
    }
    public void updateVaccineRepo(String number, String vaccineName, int flag) {

        //update repo to new quantity
        if (flag == 1) {
            String query = String.format("update repository set Available = %s " +
                                  "where Vacc_Name = '%s';", number, vaccineName);
            updateDB(query);
        }
        else {

            String query = String.format("select Available from repository where Vacc_Name = '%s';", vaccineName);
            try(Statement stm = conn.createStatement()) {
                ResultSet set = stm.executeQuery(query);
                set.next();
                int availVacc = Integer.parseInt(set.getString("Available")); //get current amount of vaccine
                int amountToAdd = Integer.parseInt(number);

                //add new quantity to new current amount
                String query1 = String.format("update repository set Available = %s " +
                                              "where Vacc_name = '%s'", availVacc + amountToAdd, vaccineName);
                updateDB(query1);
            }catch (SQLException e) {
                e.getSQLState();
            }
        }
    }

    //functions for nurse view --------------------------------------------------------------------

    //update nurse based on Nurse configurations
    public void updateNurse_N(String ID, String Address, String Phone_num) {
        String query = "update nurse set ";

        boolean flag = false;
        if (!Address.equals("")) {
            flag = true;
            query += "Address = " + "'"+Address +"'";
        }
        if (!Phone_num.equals("")) {
            if (flag) {
                query += ", ";
            }

            query += "Phone_num = " + "'" + Phone_num + "'";
        }

        query += " where EmpID = " + ID;

        updateDB(query);

        //System.out.println(query);
    }

    public ArrayList<String> getNurseSchedule(String nurseID) {

        ArrayList<String> times = new ArrayList<String>();

        //grab nurse schedule
        String query2 = String.format("select * from schedule where Time_slot in (" +
                                "select Time_slot from nurse_schedule where NurseID = %s )" +
                                "order by Time_slot;", nurseID);

        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query2);
            while (set.next()) {

                int start = Integer.parseInt(set.getString("Start_time"));
                if (start >= 8 && start < 12) {
                    times.add(String.format("%s: %-5s, %2s %2s:00-%s:00 am", set.getString("Time_slot"),
                            set.getString("theDate"), set.getString("theDay"), set.getString("Start_time"), set.getString("End_time")));
                }
                else {
                    times.add(String.format("%s: %-5s, %2s %2s:00-%s:00 pm", set.getString("Time_slot"),
                            set.getString("theDate"), set.getString("theDay"), set.getString("Start_time"), set.getString("End_time")));
                }
            }

        }catch (SQLException e) {
            //System.out.println("failed to get times.");
            e.printStackTrace();
        }


        return times;
    }

    // returns available times for nurse to schedule,
    // excluding times already scheduled by nurse with the ID given by the parameter
    public ArrayList<String> getAvailableTime (String NurseID) {

        //String times = "";
        ArrayList<String> times = new ArrayList<String>();

        String query = String.format("select * from schedule where Time_slot not in"+
                " (select Time_slot from ( select Time_slot, Count(Time_slot) as Num_nurse from nurse_schedule group by Time_slot )" +
                " as %s where Time_slot in (select Time_slot from nurse_schedule where NurseID = '%s')" +
                " or %s.Num_nurse > 12 ) ;", "Table1", NurseID, "Table1");


        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            while (set.next()) {

                int start = Integer.parseInt(set.getString("Start_time"));
                if (start >= 8 && start < 12) {
                    times.add(String.format("%s: %-5s, %2s %2s:00-%s:00 am", set.getString("Time_slot"),
                    set.getString("theDate"), set.getString("theDay"), set.getString("Start_time"), set.getString("End_time")));
                }
                else {
                    times.add(String.format("%s: %-5s, %2s %2s:00-%s:00 pm", set.getString("Time_slot"),
                    set.getString("theDate"), set.getString("theDay"), set.getString("Start_time"), set.getString("End_time")));
                }
            }

        }catch (SQLException e) {
            //System.out.println("failed to get times.");
            e.printStackTrace();
        }

        return times;
    }

    public void updateNurseSchedule(String nurseID, String time) {
        String query = String.format("insert into nurse_schedule values (%s, %s);", nurseID, time);

        updateDB(query);
    }

    public void removeNurseScheduledTime(String nurseID, String timeSlot) {

        String query = String.format("delete from nurse_schedule where NurseID = %s and Time_slot = %s", nurseID, timeSlot);

        updateDB(query);
    }

    public String getVaccinationRecord(String nurseID) {

        String info = "";

        String query = String.format("select * from vac_records where NurseID = %s and Dose = 0;", nurseID);

        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            while (set.next()) {
                info += String.format("[ Patient SSN: %s | Dose: %s | Vaccine: %s | Time_slot: %s ]\n",
                        set.getString("Pssn"), set.getString("Dose"), set.getString("Vac_Name"), set.getString("Time_Slot"));

            }
        }catch (SQLException e) {
            e.getSQLState();
        }

        return info;
    }

    public void recordVaccination(String SSN) {
        int dose = 0;
        String query = String.format("select Dose from vac_records where Pssn = '%s'", SSN);

        try(Statement stm = conn.createStatement()) {
            ResultSet r = stm.executeQuery(query);
            if (r.next()) {
                dose = r.getInt("Dose");
            }

        }catch (SQLException e) {
            e.getSQLState();
        }

        String query2 = String.format("update vac_records set Dose = %d where Pssn = '%s';", dose + 1, SSN);
        updateDB(query2);
    }

    public ArrayList<String> getNursePatients(String NurseID) {
        ArrayList<String> patients = new ArrayList<>();

        String query = String.format("select Pssn, NurseID from vac_records where NurseID = '%s';", NurseID);

        try(Statement stm = conn.createStatement()) {
            ResultSet r = stm.executeQuery(query);

            while (r.next()) {
                patients.add(r.getString("Pssn"));
            }

        }catch (SQLException e) {
            e.getSQLState();
        }

        return patients;
    }

    public String getPatientMedicalHist(String patientID) {
        String result = null;
        String query = String.format("select Med_History from patient where SSN = '%s';", patientID);

        try(Statement stm = conn.createStatement()) {
            ResultSet r = stm.executeQuery(query);
            while (r.next()) {
                result = r.getString("Med_History");
            }
        }catch (SQLException e) {
            e.getSQLState();
        }

        return result;

    }

    //queries for the patient---------------------------------------------------------------
    public String registerPatient(String info) {

        String[] s = info.split("@");

        String query = String.format("insert into patient values ('%s', '%s', '%s', '%s', %s, '%s', '%s'," +
                "'%s', '%s', '%s', '%s');", s[0], s[1], s[2], s[3], s[4], s[5], s[6], s[7], s[8], s[10], s[9]);

        String query2 = String.format("insert into p_accounts values ('%s', '%s', '%s');", s[0], s[11], s[12]);

        boolean t1 = updateDB(query);
        boolean t2 = updateDB(query2);

        if (t1 && t2) {
            return s[0];
        }

        return null;
    }

    public String getPatientInfo(String ssn) {
        String info = "";
        String query = String.format("select * from patient where SSN = '%s';", ssn);

        try (Statement stm = conn.createStatement()){
            ResultSet set = stm.executeQuery(query);
            while (set.next()) {
                info += String.format("SSN: %s\n Name: %s %s %s\n Gender: %s\n Age: %s\n Race: %s\n" +
                        "Occupation: %s\n Medical History: %s\n Address: %s\n Phone#: %s",
                        set.getString("SSN"), set.getString("Fname"), set.getString("MI"), set.getString("Lname"),
                        set.getString("Gender"), set.getString("Age"), set.getString("Race"),
                        set.getString("Occupation"), set.getString("Med_History"), set.getString("Address"),
                        set.getString("Phone_Num"));

            }
        }catch (SQLException e) {
            e.getSQLState();
        }

        return info;
    }

    public void updatePatientInfo(ArrayList<String> info, boolean noSNN) {

        String query = "update patient set ";

        boolean prevArg = false;
        if (!info.get(0).equals("") && !noSNN) {
            query += "SSN = "+info.get(0);
            prevArg = true;
        }
        if(!info.get(1).equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }

            query += "Fname = '"+info.get(1)+ "'";
        }
        if(!info.get(2).equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }
            query += "MI = '"+info.get(2)+ "'";
        }
        if(!info.get(3).equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }

            query += "Lname = '"+info.get(3)+ "'";
        }
        if(!info.get(4).equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }
            query += "Age = "+info.get(4);
        }
        if(!info.get(5).equals(""))
        {
            if (prevArg) { query += ", "; }
            query += "Gender = '"+info.get(5)+ "'";
        }
        if(!info.get(6).equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }
            query += "Race = '"+info.get(6)+ "'";
        }
        if(!info.get(7).equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }

            query += "Med_History = '"+info.get(7)+ "'";
        }
        if(!info.get(8).equals(""))
        {
            if (prevArg) { query += ", "; }
            else {prevArg = true; }
            query += "Occupation = "+info.get(8);
        }
        if(!info.get(9).equals(""))
        {
            if (prevArg) { query += ", "; }
            query += "Address = '"+info.get(9)+ "'";
        }
        if(!info.get(10).equals(""))
        {
            if (prevArg) { query += ", "; }
            query += "Phone_Num = '"+info.get(10)+ "'";
        }

        query += " where SSN = '" + info.get(0) +"';";

        System.out.println(query);
        updateDB(query);

    }

    public ArrayList<String> getAvailableSchedulePatient(String ssn) {

        String verifyQuery = String.format("select Pssn, Dose from vac_records where Pssn = '%s';", ssn);
        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(verifyQuery);
            if (set.next()) {
                int dose = set.getInt("Dose");
                if (dose == 0) {
                    //cant schedule a vaccination until they complete there current vaccination
                    return null;
                }
            }
        }catch (SQLException e) {
            e.getSQLState();
        }



        String query =
            String.format("select Time_slot, theDay, theDate, Start_time, End_time, Fname, Lname from nurse join (" +
                "select schedule.*, T3.NurseID from schedule join (select NurseID, Time_slot from nurse_schedule" +
                " where exists ( select NurseID, Time_slot from ( select NurseID, Time_Slot, Count(Time_Slot) as No_Patients from vac_records" +
                " group by NurseID, Time_Slot) as T1 where T1.No_Patients >= 10 " +
                "and T1.NurseID != nurse_schedule.NurseID or T1.Time_slot != nurse_schedule.Time_slot" +
                ")) as T3 on schedule.Time_slot = T3.Time_slot ) as T4 on T4.NurseID = nurse.EmpID;");

        ArrayList<String> times = new ArrayList<String>();
        try (Statement stm = conn.createStatement()) {

            ResultSet set = stm.executeQuery(query);

            while (set.next()) {

                int start = Integer.parseInt(set.getString("Start_time"));
                if (start >= 8 && start < 12) {
                    times.add(String.format("%s: Date: %s | Day: %s | Time: %s:00-%s:00 am | Nurse: %s %s",
                            set.getString("Time_slot"),
                            set.getString("theDate"),
                            set.getString("theDay"),
                            set.getString("Start_time"),
                            set.getString("End_time"),
                            set.getString("Fname"),
                            set.getString("Lname")
                    ));
                }
                else {
                    times.add(String.format("%s: Date: %s | Day: %s | Time: %s:00-%s:00 pm | Nurse: %s %s ",
                            set.getString("Time_slot"),
                            set.getString("theDate"),
                            set.getString("theDay"),
                            set.getString("Start_time"),
                            set.getString("End_time"),
                            set.getString("Fname"),
                            set.getString("Lname")
                    ));
                }
            }

        }catch (SQLException e){
            e.getSQLState();
        }

        return times;
    }

    public void schedulePatient(String SSN, String time_slot, String nurse) {

        String[] name = nurse.split("\\s+");
        String query = String.format("select EmpID from nurse where Fname ='%s' and Lname='%s';", name[1], name[2]);

        //check if there are vaccines available
        String query2 = String.format("select * from repository");

        try (Statement stm = conn.createStatement()) {
            ResultSet set1 = stm.executeQuery(query);
            set1.next();
            String nurseID = set1.getString("EmpID");

            ResultSet set2 = stm.executeQuery(query2);
            String vaccine = "";

            int available = 0;
            int onHold = 0;
            while(set2.next()) {
                String a = set2.getString("Available");
                String hold = set2.getString("OnHold");
                available = Integer.parseInt(a);
                onHold = Integer.parseInt(hold);

                if (available > 0 ) {
                    vaccine = set2.getString("Vacc_Name");
                    break;
                }
            }

            String query3 = String.format("insert into vac_records values ('%s', %s, %s, '%s', %s);",
                                        SSN, nurseID, "0", vaccine, time_slot);

            stm.executeUpdate(query3);

            //update repository putting the given vaccine on hold
            String query4 = String.format("update repository set Available = %d, OnHold = %d where Vacc_Name = '%s';",
                                    available-1, onHold+1, vaccine);
            updateDB(query4);

        }catch (SQLException e) {
            e.getSQLState();
        }
    }

    public ArrayList<String> getPatientSchedule(String SSN) {
        ArrayList<String> list = new ArrayList<>();

        String query = String.format("select * from schedule where Time_slot in" +
                "( select Time_Slot from vac_records where Pssn = '%s' and Dose = 0 );", SSN);

        try (Statement stm = conn.createStatement()){
            ResultSet set = stm.executeQuery(query);
            while(set.next()){
                int start = Integer.parseInt(set.getString("Start_time"));
                if (start >= 8 && start < 12) {
                    list.add(String.format("%s:  %s | %s | %s:00-%s:00 am", set.getString("Time_slot"), set.getString("theDate"),
                            set.getString("theDay"), set.getString("Start_time"), set.getString("End_time")));
                } else {
                    list.add(String.format("%s:  %s | %s | %s:00-%s:00 pm", set.getString("Time_slot"), set.getString("theDate"),
                            set.getString("theDay"), set.getString("Start_time"), set.getString("End_time")));
                }
            }
        }catch (SQLException e) {

        }
        return list;
    }

    public void cancelPatientSchedule(String SSN, String timeSlot){
        String query = String.format("delete from vac_records where Pssn = '%s' and Time_Slot = %s;", SSN, timeSlot);
        updateDB(query);
    }

    public String getVaccHistory(String SSN) {
        String info = "";
        String query = String.
                format("select T1.NurseID, T1.Dose, T1.Vac_Name, theDate from schedule join (" +
                        "select NurseID, Dose, Vac_Name, Time_Slot from vac_records " +
                        "where Pssn = '%s' and Dose = 1" +
                        ") as T1 on schedule.Time_slot = T1.Time_Slot;", SSN);


        try(Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            while(set.next()) {
                System.out.println("here");
                info += String.format("Nurse ID: %s | Vaccine: %s | Dose: %s | Date: %s\n",
                set.getString("NurseID"), set.getString("Vac_Name"), set.getString("Dose"), set.getString("theDate"));
            }
        }catch (SQLException e ) {
            e.getSQLState();
        }


        return info;
    }

    public String getPatientVaccine(String SSN) {
        String query = String.format("select Vac_Name from vac_records where Pssn = '%s';", SSN);
        String vacName = null;
        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            if (set.next()) {
                vacName = set.getString("Vac_Name");
            }
        }
        catch (SQLException e) {
            e.getSQLState();
        }

        return vacName;
    }

    public String getVaccineInfo(String vacName) {
        String query = String.format("select Optional_Des from vaccine where Name = '%s';", vacName);
        String info = null;
        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            if (set.next()) {
                info = set.getString("Optional_Des");
            }
        }
        catch (SQLException e) {
            e.getSQLState();
        }

        return info;
    }

    public String verifyPatient(String userName, String psw) {
        String patient = null;

        String query = String.format("select Pssn from p_accounts where username = '%s' and password = '%s';", userName, psw);

        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            if(set.next()) {
                patient = set.getString("Pssn");
            }

        }catch (SQLException e) {
            e.getSQLState();
        }

        return patient;
    }

    public String verifyNurse(String userName, String psw) {
        String nurse = null;

        String query = String.format("select ID from n_accounts where username = '%s' and password = '%s';", userName, psw);

        try (Statement stm = conn.createStatement()) {
            ResultSet set = stm.executeQuery(query);
            if(set.next()) {
                nurse = set.getString("ID");
            }

        }catch (SQLException e) {
            e.getSQLState();
        }
        return nurse;
    }


    //update the database
    private boolean updateDB(String query) {
        try{
            if (!conn.isClosed()) {
                Statement stm = conn.createStatement();
                stm.executeUpdate(query);
                return true;
            }

        }catch (SQLException e){
            e.getSQLState();
        }
        return false;
    }

}

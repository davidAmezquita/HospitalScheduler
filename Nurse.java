package com.Amezq;

public class Nurse {

    private int EmpID, Age;
    private String Fname, MI, Lname;
    private String Gender, Address, Phone_Num;
    private String schedule;

    Nurse(int id, int age, String fname, String mi, String lname, String gender,
            String address, String phone_num)
    {
        this.EmpID = id;
        this.Age = age;
        this.Fname = fname;
        this.MI = mi;
        this.Lname = lname;
        this.Gender = gender;
        this.Address = address;
        this.Phone_Num = phone_num;
        this.schedule = "";
    };

    public String getInfoToString() {
        String info = "ID: "+ EmpID + "\nName: " + Fname + " " + MI + " " + Lname + "\nAge: " + Age +
                "\nGender: " +  Gender + "\nAddress: " + Address + "\nPhone#: " + Phone_Num;

        return info;
    }

    public void setSchedule(String s) {
        this.schedule = s;
    }

    public String getSchedule() {
        return this.schedule;
    }



}

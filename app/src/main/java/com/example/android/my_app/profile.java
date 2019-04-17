package com.example.android.my_app;

public class profile {
    public String userAge;
    public String userEmail;
    public String userName;
    public String userDeaf;
    public String fName;
    public String fRelation;
    public String fNo;
    public float value;
    public String msg;
    public profile(){}
    public profile(String userAge,String userEmail,String userName,String userDeaf,String fName,String fRelation,String fNo)
    {
        this.userAge = userAge;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userDeaf = userDeaf;
        this.fName = fName;
        this.fRelation = fRelation;
        this.fNo = fNo;
    }
    public profile(String userAge,String userEmail,String userName,String userDeaf,String fName,String fRelation,String fNo,float value,String msg)
    {
        this.userAge = userAge;
        this.userName = userName;
        this.userEmail = userEmail;
        this.userDeaf = userDeaf;
        this.fName = fName;
        this.fRelation = fRelation;
        this.fNo = fNo;
        this.value = value;
        this.msg = msg;
    }


    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserDeaf() {
        return userDeaf;
    }

    public void setUserDeaf(String userDeaf) {
        this.userDeaf = userDeaf;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getfRelation() {
        return fRelation;
    }

    public void setfRelation(String fRelation) {
        this.fRelation = fRelation;
    }

    public String getfNo() {
        return fNo;
    }

    public void setfNo(String fNo) {
        this.fNo = fNo;
    }
}

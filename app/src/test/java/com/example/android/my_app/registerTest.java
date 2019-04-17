package com.example.android.my_app;

import org.junit.Test;

import static org.junit.Assert.*;

public class registerTest {

    @Test
    // when the length of the password is less than 6
    public void validate_pass_less_than_6() {
        //String name,password,email,age,deaf,familyMember,relation,number;
        register r = new register();
        r.name = "Tanya";
        r.password="1234";
        r.email="tanyagarg630@gmail.com";
        r.age = "21";
        assertEquals(false,r.validate());

    }

    @Test
    //When there exists one or more empty fields
    public void validate_Empty_field() {
        //String name,password,email,age,deaf,familyMember,relation,number;
        register r = new register();
        r.name = "";
        r.password="1234";
        r.email="tanyagarg630@gmail.com";
        r.age = "21";
        assertEquals(false,r.validate());

    }

    @Test
    //When the mail ID given is incorrect
    public void validate_incorrect_email_syntax() {
        //String name,password,email,age,deaf,familyMember,relation,number;
        register r = new register();
        r.name = "khk";
        r.password="1234";
        r.email="tanyag0mail.com";
        r.age = "21";
        assertEquals(false,r.validate());

    }

    @Test
    //When all the given details are given
    public void validate_correct() {
        //String name,password,email,age,deaf,familyMember,relation,number;
        register r = new register();
        r.name = "rit";
        r.password="1234567";
        r.email="bharadwaj.ritwik@gmail.com";
        r.age = "21";
        assertEquals(true,r.validate());

    }

}
package com.example.android.my_app;

import java.util.ArrayList;

public class Paragraph{
    int number;
    ArrayList<Sentence> sentences;

    Paragraph(int number){
        this.number = number;
        sentences = new ArrayList<Sentence>();
    }
}

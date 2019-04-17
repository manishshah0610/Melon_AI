package com.example.android.my_app;

import android.util.Log;
import android.widget.Toast;

import org.junit.Test;

import static org.junit.Assert.*;

public class Classification_ActivityTest {

    @Test
    public void classifyTest() {
        Classification_Activity c = new Classification_Activity();
        short[] p = new short[65489];
        for(int i=0;i<65489;i++)
        {
            int mx = Math.min(i,256);
            p[i]=(short)mx;
        }
        int xi = 1;
        String r = c.Classify(xi,p);
        System.out.println(r);
        assertEquals("Street music",r);
    }
}
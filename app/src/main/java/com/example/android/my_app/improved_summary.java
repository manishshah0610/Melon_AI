package com.example.android.my_app;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class improved_summary {
    SummaryTool summary;
    boolean flag = false;
    Context context;

    public String Str;
    public improved_summary(String a,Context con) {
        Str = a;
        context = con;
    }

    public String returnSummary()
    {
        summary = new SummaryTool(Str);
        Log.d("SummaryTool Created","yes");

        summary.init();
        Log.d("Initialized","yes");

        summary.extractSentenceFromContext();

        Log.d("Extracted","yes");
        summary.groupSentencesIntoParagraphs();

        Log.d("Grouped","yes");
        //summary.printSentences();
        summary.createIntersectionMatrix();

        summary.createDictionary();
        summary.createSummary();

        //summary.printSummary();
        Log.i("IMP","AAAAAAAAAAAAAAAAAA");
        return summary.printSummary();
    }
	/*public void func(String a){
		SummaryTool summary = new SummaryTool(a);

		summary.init();

		summary.extractSentenceFromContext();
		summary.groupSentencesIntoParagraphs();
		summary.printSentences();
		summary.createIntersectionMatrix();

		//System.out.println("INTERSECTION MATRIX");
		//summary.printIntersectionMatrix();

		summary.createDictionary();
		//summary.printDicationary();

		System.out.println("SUMMMARY");
		summary.createSummary();
		summary.printSummary();

	}*/

}


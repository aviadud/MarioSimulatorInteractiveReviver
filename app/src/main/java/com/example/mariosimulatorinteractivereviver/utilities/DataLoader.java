package com.example.mariosimulatorinteractivereviver.utilities;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.example.mariosimulatorinteractivereviver.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;

public class DataLoader {
    private static final int BUFFER_SIZE = 1024;

    public static String jsonFilePathToString(InputStream inputStream) throws IOException{
        Writer writer = new StringWriter();
        char[] buffer = new char[BUFFER_SIZE];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } finally {
            inputStream.close();
        }
        return writer.toString();
    }

    public static ArrayList<Button> addOptionButtonsToLayout(NavigationDataBase.Scene.ControlTime controlTime,
                                                     RelativeLayout layout, Context context){
        int marginInPixels = context.getResources().getDimensionPixelSize(R.dimen.button_margin);
        ArrayList<Button> result = new ArrayList<>(controlTime.getNumberOfOptions());
        for (int i = 0; i < controlTime.getNumberOfOptions(); i++) {
            Button currentButton = new Button(context);
            currentButton.setId(i);
            currentButton.setText(controlTime.getOptionButton(i));

            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i == 0) {
                layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
            }
            else{
                layoutParams.addRule(RelativeLayout.RIGHT_OF, i-1);
            }
            layoutParams.setMargins(marginInPixels, marginInPixels, marginInPixels, marginInPixels);

            layout.addView(currentButton, layoutParams);
            result.add(currentButton);
        }
        return result;
    }



}

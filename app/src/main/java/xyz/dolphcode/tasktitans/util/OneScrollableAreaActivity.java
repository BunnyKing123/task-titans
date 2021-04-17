package xyz.dolphcode.tasktitans.util;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public abstract class OneScrollableAreaActivity extends AppCompatActivity {

    protected int selection = 0;
    protected ArrayList<String> iterable;
    protected TextView displayText;
    protected String defaultText;

    protected void selectionLeft() {
        selection--;
        checkSelection();
    }

    protected void selectionRight() {
        selection++;
        checkSelection();
    }

    protected void checkSelection() {
        int iterableSize = iterable.size();
        if (selection > iterableSize - 1)
            selection = iterableSize - 1;

        if (selection < 0)
            selection = 0;

        displayText();
    }

    protected void displayText() {
        if (iterable.size() == 0) {
            displayText.setText(defaultText);
        } else {
            displayText.setText(iterable.get(selection));
        }
    }

}

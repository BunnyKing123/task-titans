package xyz.dolphcode.tasktitans.util;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

// The One Scrollable Area Activity handles screens that have an text area whose contents change when a left or right button is pressed
// An example is the inventory in the game screen
public abstract class OneScrollableAreaActivity extends AppCompatActivity {

    protected int selection = 0; // Represents the selection in the list of all possible selections
    protected ArrayList<String> iterable; // Represents the list of text to be selected from and displayed
    protected TextView displayText; // Represents where the selection will be displayed
    protected String defaultText; // Represents what is displayed when the contents of iterable are empty

    // Goes back a selection
    protected void selectionLeft() {
        selection--;
        checkSelection();
    }

    // Moves up a selection
    protected void selectionRight() {
        selection++;
        checkSelection();
    }

    // Makes sure the selection number is not out of bounds
    protected void checkSelection() {
        int iterableSize = iterable.size();
        if (selection > iterableSize - 1) // Check if the selection is greater than the maximum index
            selection = iterableSize - 1;

        if (selection < 0) // Check if the selection is less than 0
            selection = 0;

        displayText(); // Update the text view
    }

    // Updates the textview that the selection is displayed on
    protected void displayText() {
        if (iterable.size() == 0) { // If iterable is empty we display the default text
            displayText.setText(defaultText);
        } else { // Otherwise display the selection
            displayText.setText(iterable.get(selection));
        }
    }

}

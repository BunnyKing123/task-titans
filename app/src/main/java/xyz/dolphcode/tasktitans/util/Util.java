package xyz.dolphcode.tasktitans.util;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import xyz.dolphcode.tasktitans.MainActivity;

// The Util class contains all universal utility functions that are invoked throughout the program
// It is marked as final so that it can't be extended because it does not need to be extended
public final class Util {

    // A list of every possible character that can be used in an ID
    public static String CHAR_LIST = "abcdefghijklmnopqrstuvwxyz1234567890";

    // Private constructor so that the class can't be instantiated because it does not need to be
    private Util() {}

    // Automatically adds an OnClickListener to a button to make it switch screens
    public static void addSwitchScreenAction(Button btn, final Class switchTo, final AppCompatActivity switchFrom) {
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchFrom.startActivity(new Intent(switchFrom, switchTo));
            }
        });
    }

    // Automatically adds an OnClickListener to a button to make it switch screens and carry necessary user info
    public static void addSwitchWithUser(Button btn, final Class switchTo, final AppCompatActivity switchFrom, final String id) {
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(switchFrom, switchTo);
                intent.putExtra("ID", id);
                switchFrom.startActivity(intent);
            }
        });
    }

    // Transfers all extras passed over from the previous screen to the new intent
    // For context an Intent is used to switch screens, data can also be stored in Intents to carry on to the next screen when switching
    public static void transferData(Intent prev, Intent current) {
        for (String key:prev.getExtras().keySet()) { // Each key is used to get each piece of data from the previous Intent
            Object obj = prev.getExtras().get(key);
            // Convert to the appropriate datatype
            if (obj instanceof String) {
                current.putExtra(key, (String) obj);
            } else {
                current.putExtra(key, ((Integer) obj).intValue()); // Converting from the wrapper class instance to its primitive version
            }
        }
    }

    // Generates a random string of "len" length using the character list at the top of the Util class
    // Primarily used for generating IDs
    public static String generateRandomString(int len) {
        String random = "";
        for (int i = 0; i < len; i++) {
            char character = CHAR_LIST.charAt((int) Math.round(Math.random() * (CHAR_LIST.length() - 1))); // Picks out a random character
            character = (int) Math.round(Math.random()) == 1 ? Character.toUpperCase(character) : character; // Randomly decides if the character should be uppercase or not
            random += character; // Add the selected character to the rest of the string
        }
        return random;
    }

    // Splits separated by a specific character or sequence of characters
    public static String joinList(List<String> list, String separator) {
        String txt = "";
        // This code takes care of empty lists and lists with only one item that would normally cause an error with the code below
        if (list.size() == 0) {
            return txt; // Return empty string if there is nothing in the list
        } else if (list.size() == 1) {
            return list.get(0); // Return the only item in the list if the list only has one item
        }

        for (int i = 0; i < list.size() - 2; i++) { // Leave out final item in list
            if (!list.get(i).isEmpty()) // Primarily used for chat which always started with a blank string for some reason, this fixed that
                txt = txt + list.get(i) + separator;
        }
        txt = txt + list.get(list.size() - 1); // Add final item in list without separator
        return txt;
    }

    // Split string into list by some separator
    public static List<String> toList(String str, String separator) {
        List<String> list = new ArrayList<String>();
        for (String item:str.split(separator)) { // Split function converts to an array so we want to convert that to a list object
            list.add(item);
        }
        return list;
    }

}

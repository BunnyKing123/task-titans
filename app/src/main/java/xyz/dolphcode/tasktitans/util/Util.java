package xyz.dolphcode.tasktitans.util;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import xyz.dolphcode.tasktitans.MainActivity;

public class Util {

    public static String CHAR_LIST = "abcdefghijklmnopqrstuvwxyz1234567890";

    public static void addSwitchScreenAction(Button btn, final Class switchTo, final AppCompatActivity switchFrom) {
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switchFrom.startActivity(new Intent(switchFrom, switchTo));
            }
        });
    }

    public static void addSwitchWithUser(Button btn, final Class switchTo, final AppCompatActivity switchFrom, final String id) {
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(switchFrom, switchTo);
                intent.putExtra("ID", id);
                switchFrom.startActivity(intent);
            }
        });
    }

    public static void transferData(Intent prev, Intent current) {
        for (String key:prev.getExtras().keySet()) {
            Log.v("TEST", key);
            Object obj = prev.getExtras().get(key);
            if (obj instanceof String) {
                current.putExtra(key, (String) obj);
            } else {
                current.putExtra(key, ((Integer) obj).intValue());
            }
        }
    }

    public static String generateRandomString(int len) {
        String random = "";
        for (int i = 0; i < len; i++) {
            char character = CHAR_LIST.charAt((int) Math.round(Math.random() * (CHAR_LIST.length() - 1)));
            character = (int) Math.round(Math.random()) == 1 ? Character.toUpperCase(character) : character;
            random += character;
        }
        Log.v("Random ID", random);
        return random;
    }

    public static String joinList(List<String> list, String separator) {
        String txt = "";
        if (list.size() == 0) {
            return txt;
        } else if (list.size() == 1) {
            return list.get(0);
        }

        for (int i = 0; i < list.size() - 2; i++) {
            if (!list.get(i).isEmpty())
                txt = list.get(i) + "\n";
        }
        txt = list.get(list.size() - 1);
        return txt;
    }

    public static List<String> toList(String str, String separator) {
        List<String> list = new ArrayList<String>();
        for (String item:str.split(separator)) {
            list.add(item);
        }
        return list;
    }

}

package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import xyz.dolphcode.tasktitans.util.Util;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button loginBtn =  findViewById(R.id.titleLoginBtn);
        Button signupBtn =  findViewById(R.id.titleSignupBtn);

        Util.addSwitchScreenAction(loginBtn, LoginActivity.class, MainActivity.this);
        Util.addSwitchScreenAction(signupBtn, SignupActivity.class, MainActivity.this);
    }
}

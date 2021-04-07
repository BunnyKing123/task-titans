package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button loginBtn =  findViewById(R.id.ssoLoginBtn);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.ssoLoginUserTxt)).getText().toString();
                String password = ((EditText) findViewById(R.id.ssoLoginPasswordTxt)).getText().toString();

                User user = Client.getUser(username, password);

                if (user != null) {
                    Intent intent = new Intent(LoginActivity.this, GameActivity.class);
                    intent.putExtra("ID", user.getID());
                    startActivity(intent);
                }
            }
        });
    }
}

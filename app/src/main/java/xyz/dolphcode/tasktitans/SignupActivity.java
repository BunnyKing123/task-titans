package xyz.dolphcode.tasktitans;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;

public class SignupActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Button signupBtn =  findViewById(R.id.registerSignupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String username = ((EditText) findViewById(R.id.registerUserTxt)).getText().toString();
                String password = ((EditText) findViewById(R.id.registerPasswordTxt)).getText().toString();
                String email = ((EditText) findViewById(R.id.registerEmailTxt)).getText().toString();
                String displayName = ((EditText) findViewById(R.id.registerDisplayNameTxt)).getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) {
                    Intent intent = new Intent(SignupActivity.this, AppearanceActivity.class);
                    intent.putExtra("USERNAME", username);
                    intent.putExtra("PASS", password);
                    intent.putExtra("EMAIL", email);
                    intent.putExtra("DISPLAY", displayName);

                    startActivity(intent);
                }
            }
        });
    }
}

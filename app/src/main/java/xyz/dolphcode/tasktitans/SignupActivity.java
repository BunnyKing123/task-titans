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
import xyz.dolphcode.tasktitans.util.Util;

// The SignupActivity class deals with signing up
public class SignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Util.setupConnectionChangedHandler(SignupActivity.this);

        Button signupBtn =  findViewById(R.id.registerSignupBtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Get the contents of each of the text fields
                String username = ((EditText) findViewById(R.id.registerUserTxt)).getText().toString();
                String password = ((EditText) findViewById(R.id.registerPasswordTxt)).getText().toString();
                String email = ((EditText) findViewById(R.id.registerEmailTxt)).getText().toString();
                String displayName = ((EditText) findViewById(R.id.registerDisplayNameTxt)).getText().toString();

                if (!username.isEmpty() && !password.isEmpty()) { // Make sure required fields are filled out first
                    Intent intent = new Intent(SignupActivity.this, DetailsActivity.class);
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

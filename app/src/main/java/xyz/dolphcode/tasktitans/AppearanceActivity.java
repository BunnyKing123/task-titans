package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public class AppearanceActivity extends AppCompatActivity {

    // Object fields need to be fields to be accessed by Button listener
    private int skinColor = 0;
    private int hairColor = 0;
    private boolean gender = false;

    Intent prevData; // Contains data passed from previous screen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);

        prevData = getIntent(); // Store intent passed from previous screen

        Button finishBtn =  findViewById(R.id.appearanceFinBtn);
        Button skin1Btn =  findViewById(R.id.skin1Btn);
        Button skin2Btn =  findViewById(R.id.skin2Btn);
        Button skin3Btn =  findViewById(R.id.skin3Btn);
        Button skin4Btn =  findViewById(R.id.skin4Btn);
        Button hair1Btn = findViewById(R.id.hair1Btn);
        Button hair2Btn = findViewById(R.id.hair2Btn);
        Button hair3Btn = findViewById(R.id.hair3Btn);
        Button hair4Btn = findViewById(R.id.hair4Btn);
        ImageButton maleBtn = findViewById(R.id.maleBtn);
        ImageButton femaleBtn = findViewById(R.id.femaleBtn);

        skin1Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {skinColor = 0;}});
        skin2Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {skinColor = 1;}});
        skin3Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {skinColor = 2;}});
        skin4Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {skinColor = 3;}});
        hair1Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {hairColor = 0;}});
        hair2Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {hairColor = 1;}});
        hair3Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {hairColor = 2;}});
        hair4Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {hairColor = 3;}});

        maleBtn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {gender = true;}});
        femaleBtn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {gender = false;}});

        finishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AppearanceActivity.this, GameActivity.class);
                Util.transferData(prevData, intent);

                // Create new User object with all sign up data
                User user = User.UserBuilder.createUser(prevData.getStringExtra("USERNAME"), prevData.getStringExtra("PASS"))
                        .setEmail(prevData.getStringExtra("EMAIL"))
                        .setDisplayName(prevData.getStringExtra("DISPLAY"))
                        .setColor(skinColor, false)
                        .setColor(hairColor, true)
                        .setSpecialty(prevData.getIntExtra("RACE", 0), true)
                        .setSpecialty(prevData.getIntExtra("CLASS", 0), false)
                        .setGender(gender)
                        .build();

                // Add user to database
                Client.updateUser(user);

                // Send ID to next screen to get User
                intent.putExtra("ID", user.getID());

                AppearanceActivity.this.startActivity(intent);
            }
        });
    }
}

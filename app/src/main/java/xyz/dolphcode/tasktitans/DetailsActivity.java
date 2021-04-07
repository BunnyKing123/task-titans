package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public class DetailsActivity extends AppCompatActivity {

    Intent prevData;

    private int raceID = 0;
    private int classID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        prevData = getIntent();

        Button finishBtn =  findViewById(R.id.detailFinBtn);
        ImageButton race1Btn =  findViewById(R.id.humanBtn);
        ImageButton race2Btn =  findViewById(R.id.orcBtn);
        ImageButton race3Btn =  findViewById(R.id.elfBtn);
        ImageButton class1Btn = findViewById(R.id.fighterBtn);
        ImageButton class2Btn = findViewById(R.id.archerBtn);
        ImageButton class3Btn = findViewById(R.id.mageBtn);

        race1Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {raceID = 0;}});
        race2Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {raceID = 1;}});
        race3Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {raceID = 2;}});
        class1Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {classID = 0;}});
        class2Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {classID = 1;}});
        class3Btn.setOnClickListener(new View.OnClickListener() { public void onClick(View v) {classID = 2;}});

        finishBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(DetailsActivity.this, GameActivity.class);

                User user = User.UserBuilder.createUser(prevData.getStringExtra("USERNAME"), prevData.getStringExtra("PASS"))
                        .setEmail(prevData.getStringExtra("EMAIL"))
                        .setDisplayName(prevData.getStringExtra("DISPLAY"))
                        .setColor(prevData.getIntExtra("SKIN", 0), false)
                        .setColor(prevData.getIntExtra("HAIR", 0), true)
                        .setSpecialty(raceID, true)
                        .setSpecialty(classID, false)
                        .setGender(prevData.getBooleanExtra("GENDER", false))
                        .build();

                Client.updateUser(user);
                intent.putExtra("ID", user.getID());

                DetailsActivity.this.startActivity(intent);
            }
        });
    }
}

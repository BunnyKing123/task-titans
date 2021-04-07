package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import xyz.dolphcode.tasktitans.util.Util;

public class AppearanceActivity extends AppCompatActivity {
    private int skinColor = 0;
    private int hairColor = 0;
    private boolean gender = false;

    Intent prevData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appearance);

        prevData = getIntent();

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
                Intent intent = new Intent(AppearanceActivity.this, DetailsActivity.class);
                Util.transferData(prevData, intent);
                intent.putExtra("SKIN", skinColor);
                intent.putExtra("HAIR", hairColor);
                intent.putExtra("GENDER", gender);

                AppearanceActivity.this.startActivity(intent);
            }
        });
    }
}

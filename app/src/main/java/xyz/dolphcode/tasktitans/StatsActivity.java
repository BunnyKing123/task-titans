package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.resources.Abilities;
import xyz.dolphcode.tasktitans.util.Util;

public class StatsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    User user;
    TextView strength;
    TextView intelligence;
    TextView constitution;
    TextView dexterity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Button backBtn = findViewById(R.id.statsBackButton);
        strength = findViewById(R.id.characterStrengthLabel);
        intelligence = findViewById(R.id.characterIntelLabel);
        constitution = findViewById(R.id.characterConstLabel);
        dexterity = findViewById(R.id.characterDextLabel);
        TextView money = findViewById(R.id.statsMoneyText);
        TextView mana = findViewById(R.id.statsManaText);

        user = Client.getUser(getIntent().getStringExtra("ID"));

        updateStats();

        money.setText("" + user.getMoney());

        Spinner skills = findViewById(R.id.skillsDropdown);

        // Code based on code in the Android Studio documentation
        String[] skillsArray = {user.getSkill()};

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, skillsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        skills.setAdapter(adapter);
        skills.setOnItemSelectedListener(this);
        // Documentation based code ends here

        Button strengthMod = findViewById(R.id.strengthModBtn);
        Button intelMod = findViewById(R.id.intelModBtn);
        Button constMod = findViewById(R.id.constModBtn);
        Button dextMod = findViewById(R.id.dextModBtn);

        strengthMod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user.addModifiers(0, 0, 1, 0);
                updateStats();
            }
        });

        intelMod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user.addModifiers(0, 1, 0, 0);
                updateStats();
            }
        });

        constMod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user.addModifiers(1, 0, 0, 0);
                updateStats();
            }
        });

        dextMod.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                user.addModifiers(0, 0, 0, 1);
                updateStats();
            }
        });

        Util.addSwitchWithUser(backBtn, GameActivity.class, StatsActivity.this, user.getID());
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        String key = parent.getItemAtPosition(pos).toString();
        TextView desc = findViewById(R.id.skillDesc);

        desc.setText(Abilities.ABILITIES.get(key));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Required override
    }

    public void updateStats() {
        strength.setText("Strength: " + (user.getBaseStrength() + user.getStrengthMod()));
        intelligence.setText("Intelligence: " + (user.getBaseIntel() + user.getIntelMod()));
        constitution.setText("Constitution: " + (user.getBaseConst() + user.getConstMod()));
        dexterity.setText("Dexterity: " + (user.getBaseDext() + user.getDextMod()));
    }
}

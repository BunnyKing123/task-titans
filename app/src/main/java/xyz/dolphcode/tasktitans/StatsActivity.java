package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Button backBtn = findViewById(R.id.statsBackButton);
        TextView stats = findViewById(R.id.characterStatsLabel);
        TextView money = findViewById(R.id.statsMoneyText);
        TextView mana = findViewById(R.id.statsManaText);

        User user = Client.getUser(getIntent().getStringExtra("ID"));

        stats.setText("Strength: " + (user.getBaseStrength() + user.getStrengthMod()) +
                "\nDexterity: " + (user.getBaseDext() + user.getDextMod()) +
                "\nIntelligence: " + (user.getBaseIntel() + user.getIntelMod()) +
                "\nConstitution: " + (user.getBaseConst() + user.getConstMod()));

        money.setText("" + user.getMoney());

        Spinner skills = findViewById(R.id.skillsDropdown);
        // Code based on code in the Android Studio documentation
        String[] skillsArray = {user.getSkill()};

        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, skillsArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        skills.setAdapter(adapter);
        skills.setOnItemSelectedListener(this);

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
}

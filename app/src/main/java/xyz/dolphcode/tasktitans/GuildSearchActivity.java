package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import xyz.dolphcode.tasktitans.util.Util;

public class GuildSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guild_search);

        Button backBtn =  findViewById(R.id.guildJoinBackBtn);
        Button searchBtn =  findViewById(R.id.searchBtn);
        Button createBtn = findViewById(R.id.createGuildBtn);

        String id = getIntent().getStringExtra("ID");

        Util.addSwitchWithUser(backBtn, GameActivity.class, GuildSearchActivity.this, id);
        Util.addSwitchWithUser(createBtn, GuildCreateActivity.class, GuildSearchActivity.this, id);
    }
}

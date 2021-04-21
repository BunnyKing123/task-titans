package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.Guild;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public class GuildCreateActivity extends AppCompatActivity {

    String id;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guild_create);

        Util.setupConnectionChangedHandler(GuildCreateActivity.this);

        Button createBtn = findViewById(R.id.guildCreateDoneBtn);

        user = Client.getUser(getIntent().getStringExtra("ID"));
        id = user.getID();

        createBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String guildName = ((EditText) findViewById(R.id.guildNameInputTxt)).getText().toString();

                Guild guild = Guild.GuildBuilder.createGuild(guildName, id)
                        .build();
                Client.updateGuild(guild);

                user.setGuild(guild.getGuildID());

                Intent intent = new Intent(GuildCreateActivity.this, GuildActivity.class);
                intent.putExtra("ID", id);
                intent.putExtra("GUILDID", guild.getGuildID());
                GuildCreateActivity.this.startActivity(intent);
            }
        });
    }
}

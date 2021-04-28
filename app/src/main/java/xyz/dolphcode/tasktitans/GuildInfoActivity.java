package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.Guild;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public class GuildInfoActivity extends AppCompatActivity {

    Guild guild;
    User user;
    TextView details;
    String guildID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guild_info);

        Util.setupConnectionChangedHandler(GuildInfoActivity.this);

        guildID = getIntent().getStringExtra("GUILDID");
        guild = Client.getGuild(guildID);
        user = Client.getUser(getIntent().getStringExtra("ID"));

        details = findViewById(R.id.guildInfo);
        details.setText("Member Count: " + guild.getMemberIDs().size());

        Button backBtn = findViewById(R.id.guildInfoBackBtn);

        Util.addSwitchWithUser(backBtn, GameActivity.class, GuildInfoActivity.this, user.getID());
    }
}

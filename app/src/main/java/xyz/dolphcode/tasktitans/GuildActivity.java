package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.Guild;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public class GuildActivity extends AppCompatActivity {

    Guild guild;
    User user;
    TextView chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guild);

        Button backBtn = findViewById(R.id.guildBackBtn);
        Button sendBtn = findViewById(R.id.chatSendBtn);

        guild = Client.getGuild(getIntent().getStringExtra("GUILDID"));
        user = Client.getUser(getIntent().getStringExtra("ID"));

        chat = findViewById(R.id.chatBox);
        chat.setText(guild.getDBChat());

        Util.addSwitchWithUser(backBtn, GameActivity.class, GuildActivity.this, user.getID());
        sendBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String chatSend = ((EditText) findViewById(R.id.guildChatSendTxt)).getText().toString();
                if (!chatSend.isEmpty()) {
                    guild.chatMessage(chatSend, user);
                    chat.setText(guild.getDBChat());
                }
            }
        });
    }
}

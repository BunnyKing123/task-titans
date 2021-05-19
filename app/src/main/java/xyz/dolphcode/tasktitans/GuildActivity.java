package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.DatabaseObserver;
import xyz.dolphcode.tasktitans.database.guilds.Guild;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.database.guilds.GuildTask;
import xyz.dolphcode.tasktitans.util.Util;

public class GuildActivity extends AppCompatActivity implements DatabaseObserver {

    Guild guild;
    User user;
    TextView chat;
    TextView name;
    TextView challenge;
    Button challengeCompleteBtn;
    String guildID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guild);

        Client.addObserver(this);
        Util.setupConnectionChangedHandler(GuildActivity.this);

        Button backBtn = findViewById(R.id.guildBackBtn);
        Button sendBtn = findViewById(R.id.chatSendBtn);

        guildID = getIntent().getStringExtra("GUILDID");
        guild = Client.getGuild(guildID);
        user = Client.getUser(getIntent().getStringExtra("ID"));

        challenge = findViewById(R.id.guildChallenge);

        chat = findViewById(R.id.chatBox);
        chat.setText(guild.getDBChat());

        name = findViewById(R.id.guildTitle);
        name.setText(guild.getGuildName());

        challenge = findViewById(R.id.guildChallenge);
        challengeCompleteBtn = findViewById(R.id.guildChallegeComplete);
        challengeCompleteBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (guild.getGuildTaskType() == GuildTask.TASK && !challenge.getText().equals("Guild task completed!")) {
                    ArrayList<String> completionList = new ArrayList<String>();
                    for (String id : guild.getGuildCompletions().split("-")) {
                        if (!id.isEmpty())
                            completionList.add(id);
                    }
                    if (!completionList.contains(user.getID())) {
                        guild.completeTask(user);
                        user.addRewards(50, 20, 50);
                        updateChallenge(guild);
                    }
                } else if (guild.getGuildTaskType() == GuildTask.BOSS && !challenge.getText().equals("Boss defeated!")) {
                    Intent intent = new Intent(GuildActivity.this, StatsActivity.class);
                    intent.putExtra("ID", user.getID());
                    GuildActivity.this.startActivity(intent);
                }
            }
        });

        this.updateChallenge(guild);

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

    // Returns false if the guild task does not need to be regenerated
    private boolean updateChallenge(Guild guild) {
        Calendar today = TaskTitansApp.TODAY;
        Calendar deadline = Util.dateToCal(guild.getGuildTaskDeadline());
        if (deadline.get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR) || deadline.get(Calendar.YEAR) < today.get(Calendar.YEAR)) {
            return true;
        }

        if (guild.getGuildTaskType() == GuildTask.TASK) {
            ArrayList<String> completionList = new ArrayList<String>();
            for (String id:guild.getGuildCompletions().split("-")) {
                if (!id.isEmpty())
                    completionList.add(id);
            }
            if (completionList.contains(user.getID())) {
                challenge.setText("Guild task completed!");
            } else {
                challenge.setText(guild.getGuildTaskName());
            }
            challengeCompleteBtn.setText("Finish");
        } else {
            if (guild.getGuildBossHP() <= 0) {
                challenge.setText("Boss defeated!");
            } else {
                challenge.setText(guild.getGuildTaskName() + ": " + guild.getGuildBossHP() + "/" + GuildTask.BOSSES.get(guild.getGuildTaskName()));
            }
            challengeCompleteBtn.setText("Attack");
        }

        return false;
    }

    @Override
    public void databaseChanged() {
        guild = Client.getGuild(guildID);
        chat.setText(guild.getDBChat());
        name.setText(guild.getGuildName());

        if (updateChallenge(guild)) {
            guild.regenerateChallenge();
        }
    }
}

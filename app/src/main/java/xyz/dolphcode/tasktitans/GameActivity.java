package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.Guild;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.util.Util;

public class GameActivity extends AppCompatActivity {

    String id;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button guildNavBtn =  findViewById(R.id.guildNavBtn);
        Button taskNavBtn =  findViewById(R.id.taskNavBtn);
        Button shopNavBtn =  findViewById(R.id.shopNavBtn);
        Button statsNavBtn =  findViewById(R.id.statsNavBtn);

        TextView stats = findViewById(R.id.profileInfo);
        TextView money = findViewById(R.id.profileMoneyText);
        TextView mana = findViewById(R.id.profileManaText);

        user = Client.getUser(getIntent().getStringExtra("ID"));
        id = user.getID();

        stats.setText("Name: " + user.getDisplayName() + "\nHP: " + user.getHP() + "/" + user.getMaxHP() + "\nLevel: " + User.convertToLevel(user.getXP()));
        money.setText("" + user.getMoney());
        mana.setText("" + user.getMana());
        Util.addSwitchWithUser(taskNavBtn, TasksActivity.class, GameActivity.this, id);
        Util.addSwitchScreenAction(shopNavBtn, ShopActivity.class, GameActivity.this);
        Util.addSwitchWithUser(statsNavBtn, StatsActivity.class, GameActivity.this, id);

        guildNavBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;

                Guild userGuild = Client.getGuild(user.getGuildID());
                if (user.getGuildID().contentEquals("none") || userGuild == null) {
                    if (userGuild == null) {
                        user.clearGuild();
                        Client.updateUser(user);
                    }

                    intent = new Intent(GameActivity.this, GuildSearchActivity.class);
                } else {
                    intent = new Intent(GameActivity.this, GuildActivity.class);
                    intent.putExtra("GUILDID", userGuild.getGuildID());
                }

                intent.putExtra("ID", id);

                GameActivity.this.startActivity(intent);
            }
        });
    }
}

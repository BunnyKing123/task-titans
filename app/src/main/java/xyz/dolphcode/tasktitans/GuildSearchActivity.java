package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.Guild;
import xyz.dolphcode.tasktitans.util.OneScrollableAreaActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class GuildSearchActivity extends OneScrollableAreaActivity {

    ArrayList<Guild> results = new ArrayList<Guild>();
    EditText searchBar;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guild_search);

        iterable = new ArrayList<String>();
        defaultText = "No guilds found";
        displayText = findViewById(R.id.guildSearchedText);
        checkSelection();
        displayText();

        Button backBtn =  findViewById(R.id.guildJoinBackBtn);
        Button searchBtn =  findViewById(R.id.searchBtn);
        Button createBtn = findViewById(R.id.createGuildBtn);
        Button joinBtn = findViewById(R.id.guildJoinBtn);

        ImageButton leftBtn = findViewById(R.id.guildSearchLeftBtn);
        ImageButton rightBtn = findViewById(R.id.guildSearchRightBtn);

        searchBar = findViewById(R.id.guildSearchBarTxt);

        id = getIntent().getStringExtra("ID");

        leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectionLeft();
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectionRight();
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                results = Client.searchGuilds(searchBar.getText().toString());
                iterable = new ArrayList<String>();
                for (Guild g:results) {
                    iterable.add(g.getGuildName());
                }
                checkSelection();
                displayText();
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (iterable.size() > 0) {
                    Intent intent = new Intent(GuildSearchActivity.this, GuildActivity.class);
                    intent.putExtra("ID", id);
                    intent.putExtra("GUILDID", results.get(selection).getGuildID());
                    results.get(selection).addMember(id);
                    GuildSearchActivity.this.startActivity(intent);
                }
            }
        });

        Util.addSwitchWithUser(backBtn, GameActivity.class, GuildSearchActivity.this, id);
        Util.addSwitchWithUser(createBtn, GuildCreateActivity.class, GuildSearchActivity.this, id);
    }
}

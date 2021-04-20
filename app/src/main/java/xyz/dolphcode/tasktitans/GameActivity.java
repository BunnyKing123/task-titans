package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.Guild;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.resources.item.Item;
import xyz.dolphcode.tasktitans.resources.item.Items;
import xyz.dolphcode.tasktitans.util.OneScrollableAreaActivity;
import xyz.dolphcode.tasktitans.util.Util;

public class GameActivity extends OneScrollableAreaActivity {

    String id;
    User user;
    Button selectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Button guildNavBtn =  findViewById(R.id.guildNavBtn);
        Button taskNavBtn =  findViewById(R.id.taskNavBtn);
        Button shopNavBtn =  findViewById(R.id.shopNavBtn);
        Button statsNavBtn =  findViewById(R.id.statsNavBtn);

        ImageButton leftBtn = findViewById(R.id.inventoryLeftBtn);
        ImageButton rightBtn = findViewById(R.id.inventoryRightBtn);
        selectBtn = findViewById(R.id.inventoryEquipBtn);

        TextView stats = findViewById(R.id.profileInfo);
        TextView money = findViewById(R.id.profileMoneyText);
        TextView mana = findViewById(R.id.profileManaText);

        ImageView profile = findViewById(R.id.profilePicture);

        user = Client.getUser(getIntent().getStringExtra("ID"));
        id = user.getID();

        iterable = new ArrayList<String>();
        for (String str:user.getInventory().split("-")) {
            if (!str.isEmpty()) {
                iterable.add(str);
            }
        }
        displayText = findViewById(R.id.inventoryText);
        defaultText = "Whoa, your bag is empty!";
        checkSelection();
        displayText();
        if (iterable.size() > 0)
            checkIfEquipped(iterable.get(selection));

        profile.setImageResource(Util.getProfileImage(this, user));

        stats.setText("Name: " + user.getDisplayName() + "\nHP: " + user.getHP() + "/" + user.getMaxHP() + "\nLevel: " + User.convertToLevel(user.getXP()));
        money.setText("" + user.getMoney());
        mana.setText("" + user.getMana());
        Util.addSwitchWithUser(taskNavBtn, TasksActivity.class, GameActivity.this, id);
        Util.addSwitchWithUser(shopNavBtn, ShopActivity.class, GameActivity.this, id);
        Util.addSwitchWithUser(statsNavBtn, StatsActivity.class, GameActivity.this, id);

        leftBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectionLeft();
                if (iterable.size() > 0)
                    checkIfEquipped(iterable.get(selection));
            }
        });

        rightBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                selectionRight();
                if (iterable.size() > 0)
                    checkIfEquipped(iterable.get(selection));
            }
        });

        selectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (iterable.size() > 0) {
                    String item = iterable.get(selection);
                    boolean isPet = Items.ITEMS.get(item).getItemType() == Item.PET;
                    if (selectBtn.getText().toString().contentEquals("Equip")) {
                        user.equip(item, isPet);
                    } else {
                        user.clearEquipment(isPet);
                    }
                    checkIfEquipped(item);
                }
            }
        });

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

    public void checkIfEquipped(String item) {
        if ((user.getEquipment().contentEquals(item) || user.getPet().contentEquals(item)) && !item.isEmpty()) {
            selectBtn.setText("Unequip");
        } else {
            selectBtn.setText("Equip");
        }
    }
}

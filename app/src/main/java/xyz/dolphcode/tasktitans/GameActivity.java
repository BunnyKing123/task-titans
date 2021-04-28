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

// The GameActivity class is connected to the game activity which is the first screen the user enters after logging in or signing up
public class GameActivity extends OneScrollableAreaActivity {

    String id;
    User user;
    Button selectBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Util.setupConnectionChangedHandler(GameActivity.this);

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

        // Get the user data
        user = Client.getUser(getIntent().getStringExtra("ID"));
        id = user.getID();

        // The selector should contain items in the player's inventory
        // Iterable is set to the names of the items
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

        // We will check if the first item in the inventory is equipped so we know if the player should be able to equip or unequip the item
        if (iterable.size() > 0)
            checkIfEquipped(iterable.get(selection));

        // Set profile picture
        profile.setImageResource(Util.getProfileImage(this, user));

        // Display information in text boxes
        stats.setText("Name: " + user.getDisplayName() + "\nHP: " + user.getHP() + "/" + user.getMaxHP() + "\nLevel: " + User.convertToLevel(user.getXP()));
        money.setText("" + user.getMoney());
        mana.setText("" + user.getMana());

        Util.addSwitchWithUser(taskNavBtn, TasksActivity.class, GameActivity.this, id);
        Util.addSwitchWithUser(shopNavBtn, ShopActivity.class, GameActivity.this, id);
        Util.addSwitchWithUser(statsNavBtn, StatsActivity.class, GameActivity.this, id);

        // When the player looks through the inventory we need to check whether or not the item is equipped and
        // adjust the purpose of the equip/unequip button accordingly
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

        // Either equips or unequips the selected item depending on whether or not the item is equipped already
        selectBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (iterable.size() > 0) {
                    String item = iterable.get(selection);

                    // Since the player has a pet and equipment slot we need to check if the item selected is a pet or equipment
                    boolean isPet = Items.ITEMS.get(item).getItemType() == Item.PET;

                    if (selectBtn.getText().toString().contentEquals("Equip")) {
                        user.equip(item, isPet); // Set the item in the slot
                    } else {
                        user.clearEquipment(isPet); // If we are unequipping the item just clear the equipment in the slot
                    }
                    checkIfEquipped(item);
                }
            }
        });

        // Handles switching to the guild screen
        guildNavBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent;

                Guild userGuild = Client.getGuild(user.getGuildID());

                // The screen the user is sent to depends on whether or not they are in a guild at the present moment
                if (user.getGuildID().contentEquals("none") || userGuild == null) {
                    // Checks to make sure the player's guild variable is set to "none" rather than leaving it null
                    if (userGuild == null) {
                        user.clearGuild();
                        Client.updateUser(user);
                    }

                    intent = new Intent(GameActivity.this, GuildSearchActivity.class); // Go to the search activity/screen
                } else {
                    intent = new Intent(GameActivity.this, GuildActivity.class); // Go to the guild activity
                    intent.putExtra("GUILDID", userGuild.getGuildID()); // Send the user's guild ID
                }

                intent.putExtra("ID", id);

                GameActivity.this.startActivity(intent);
            }
        });
    }

    // Checks if the selected inventory item is equipped by the player
    public void checkIfEquipped(String item) {
        if ((user.getEquipment().contentEquals(item) || user.getPet().contentEquals(item)) && !item.isEmpty()) {
            selectBtn.setText("Unequip"); // If it is give the player the option to unequip
        } else {
            selectBtn.setText("Equip"); // Otherwise the player should be able to equip it
        }
    }
}

package xyz.dolphcode.tasktitans;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.lang.reflect.Array;
import java.util.ArrayList;

import xyz.dolphcode.tasktitans.database.Client;
import xyz.dolphcode.tasktitans.database.User;
import xyz.dolphcode.tasktitans.resources.item.Item;
import xyz.dolphcode.tasktitans.resources.item.Items;
import xyz.dolphcode.tasktitans.util.Util;

public class ShopActivity extends AppCompatActivity {

    String id;
    User user;
    Spinner shopItems;
    ArrayAdapter shopItemsAdapter;
    ArrayList<String> setArray = new ArrayList<String>();
    String[] equipmentArray;
    String[] petArray;

    String selectedItem = "";
    boolean shopSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        id = getIntent().getStringExtra("ID");
        user = Client.getUser(id);

        Button backBtn =  findViewById(R.id.shopBackBtn);
        Button buyBtn = findViewById(R.id.shopBuyBtn);

        // Create an adapter for each item type
        ArrayList<String> equipment = new ArrayList<String>();
        ArrayList<String> pets = new ArrayList<String>();
        for (String key: Items.ITEMS.keySet()) {
            if (Items.ITEMS.get(key).getItemType() == Item.EQUIPMENT) {
                equipment.add(key);
            } else {
                pets.add(key);
            }
        }

        equipmentArray = equipment.toArray(new String[equipment.size()]);
        petArray = pets.toArray(new String[equipment.size()]);

        // Item List
        shopItems = findViewById(R.id.itemsDropdown);
        shopItemsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, setArray);
        shopItemsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopItems.setAdapter(shopItemsAdapter);
        shopItems.setOnItemSelectedListener(new ItemSpinnerListener(this));

        Spinner shopType = findViewById(R.id.shopsDropdown);
        // Code based on code in the Android Studio documentation
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.shop_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        shopType.setAdapter(adapter);
        shopType.setOnItemSelectedListener(new ShopTypeSpinnerListener(this));

        Spinner shopItems = findViewById(R.id.itemsDropdown);

        Util.addSwitchWithUser(backBtn, GameActivity.class, ShopActivity.this, id);

        buyBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!selectedItem.isEmpty()) {
                    Item item = Items.ITEMS.get(selectedItem);
                    if (item != null) {
                        int cost = item.getItemCost();
                        if (user.getMoney() >= cost) {
                            user.addMoney(-cost);
                            user.addToInventory(item.getItemName());
                        }
                    }
                }
            }
        });
    }

    private class ShopTypeSpinnerListener implements AdapterView.OnItemSelectedListener {

        ShopActivity outer;

        public ShopTypeSpinnerListener (ShopActivity outer) {
            this.outer = outer;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getItemAtPosition(position).toString().contentEquals("Equipment")) {
                while (setArray.size() > 0) { setArray.remove(0); }
                for (String str:equipmentArray) { setArray.add(str); }
                shopItemsAdapter.notifyDataSetChanged();
                outer.shopSelected = true;
            } else if (parent.getItemAtPosition(position).toString().contentEquals("Equipment")) {
                while (setArray.size() > 0) { setArray.remove(0); }
                for (String str:petArray) { setArray.add(str); }
                shopItemsAdapter.notifyDataSetChanged();
                outer.shopSelected = true;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    private class ItemSpinnerListener implements AdapterView.OnItemSelectedListener {

        ShopActivity outer;

        public ItemSpinnerListener (ShopActivity outer) {
            this.outer = outer;
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (outer.shopSelected) {
                outer.selectedItem = parent.getItemAtPosition(position).toString();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}

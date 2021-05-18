package xyz.dolphcode.tasktitans.resources.item;

import java.util.HashMap;

// The item class represents a possible item
public class Item {

    public static final int EQUIPMENT = 0;
    public static final int PET = 1;

    public static final int ALL_CLASS = 2;
    public static final int FIGHTER_EXCLUSIVE = 3;
    public static final int ARCHER_EXCLUSIVE = 4;
    public static final int MAGE_EXCLUSIVE = 5;

    private String name;
    private String desc;
    private int itemType = EQUIPMENT;
    private int classRequirement = ALL_CLASS;
    private int shopCost = 0;
    private HashMap<Bonus, Integer> bonuses = new HashMap<Bonus, Integer>();

    public Item(String name) {
        this.name = name;
    }

    // Sets the item description, optional
    public Item itemDescription(String desc) {
        this.desc = desc;
        return this;
    }

    // Sets the type of item to a pet, clamping is not needed because # is not manually inputted
    public Item isPetItem() {
        this.itemType = PET;
        return this;
    }

    // Sets the type of item to equipment, is also default
    public Item isEquipment() {
        this.itemType = EQUIPMENT;
        return this;
    }

    // Sets the class requirement used to determine which items appear in the shop for the player
    // Clamps the requirement to the required values
    public Item classRequirement(int requirement) {
        if (requirement > 5) { this.classRequirement = 5;
        } else if (requirement < 2) { this.classRequirement = 2;
        } else { this.classRequirement = requirement; }

        return this;
    }

    // Sets the cost of the item in the shop
    public Item cost(int money) {
        this.shopCost = money;
        return this;
    }

    // Adds a bonus to the list of bonuses this equipment gives
    public Item addBonus(Bonus bonus, int amount) {
        bonuses.put(bonus, amount);
        return this;
    }

    public String getItemName() { return name; }
    public String getDescription() { return desc; }
    public int getItemType() { return itemType; }
    public double getBonus(Bonus bonus) {
        if (bonuses.get(bonus) == null) {
            return 1.0;
        }
        return (bonuses.get(bonus) / 100.0) + 1.0;
    }
    public int getItemCost() { return this.shopCost; }

    public enum Bonus {
        ATTACK, DEFENSE, MANA, MONEY // Items can boost attack damage, defense, mana gain and money gain
    }

}

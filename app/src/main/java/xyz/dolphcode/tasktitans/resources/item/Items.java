package xyz.dolphcode.tasktitans.resources.item;

import java.util.HashMap;

public final class Items {

    private Items() {}

    public static final HashMap<String, Item> ITEMS = new HashMap<String, Item>();

    public static final Item FANCY_HAT = new Item("Fancy Wizard Hat")
            .itemDescription("A fancy wizard's hat lined with gold and covered with sigils. Every time you recover mana you recover 10% more")
            .classRequirement(Item.MAGE_EXCLUSIVE).cost(300)
            .addBonus(Item.Bonus.MANA, 10);

    public static final Item THICK_HELMET = new Item("Thick Helmet")
            .itemDescription("A very thick helmet, it's quite heavy though. Damage you take is reduced by 20%")
            .classRequirement(Item.FIGHTER_EXCLUSIVE).cost(300)
            .addBonus(Item.Bonus.DEFENSE, 20);

    public static final Item TELESCOPIC_GLASSES = new Item("Telescopic Glasses")
            .itemDescription("Great for precise shots, and with precision comes deadly power. You deal 20% more damage")
            .classRequirement(Item.ARCHER_EXCLUSIVE).cost(300)
            .addBonus(Item.Bonus.ATTACK, 20);

    public static Item GOLDEN_CAT_MASK = new Item("Golden Cat Mask")
            .itemDescription("The golden cat mask will bring you great fortune. You gain an additional 50% more money every time you gain money")
            .classRequirement(Item.ALL_CLASS).cost(1000)
            .addBonus(Item.Bonus.MONEY, 50);

    public static Item MIMIC_PET = new Item("Mimic Pet")
            .itemDescription("The mimic pet is very good at finding treasure. You gain an additional 20% more money every time you gain money")
            .isPetItem().cost(500)
            .addBonus(Item.Bonus.MONEY, 20);

    static {
        ITEMS.put(FANCY_HAT.getItemName(), FANCY_HAT);
        ITEMS.put(THICK_HELMET.getItemName(), THICK_HELMET);
        ITEMS.put(TELESCOPIC_GLASSES.getItemName(), TELESCOPIC_GLASSES);
        ITEMS.put(GOLDEN_CAT_MASK.getItemName(), GOLDEN_CAT_MASK);
        ITEMS.put(MIMIC_PET.getItemName(), MIMIC_PET);
    }

}

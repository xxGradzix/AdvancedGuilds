package me.xxgradzix.advancedclans.guildshideoutsystem;

import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionDto;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionVariant;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;

public class ItemManager {

    public static ItemStack foodRationTier3;
    public static ItemStack foodRationTier2;
    public static ItemStack foodRationTier1;

    public static ItemStack toolTier1;
    public static ItemStack toolTier2;
    public static ItemStack toolTier3;


    public static void init() {
        createFoodRation1();
        createFoodRation2();
        createFoodRation3();

        createToolPack1();
        createToolPack2();
        createToolPack3();
    }

    public static void createFoodRation3() {
        ItemStack item = new ItemStack(Material.MUSHROOM_STEW);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ʀᴀᴄᴊᴀ żʏᴡɴᴏśᴄɪᴏᴡᴀ #a18b3dɪɪɪ"));
        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴢᴀᴘᴇᴡɴɪᴀ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ #a18b3dɪɪɪ &7ᴘᴏᴢɪᴏᴍᴜ"));
        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴍᴏżɴᴀ ᴜżʏć ᴅᴏ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ &8(&a-90 &7ᴍɪɴ ᴄᴢᴀꜱᴜ ᴏᴄᴢᴇᴋɪᴡᴀɴɪᴀ&8)"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        foodRationTier3 = item;
    }

    public static void createFoodRation2() {
        ItemStack item = new ItemStack(Material.PUMPKIN_PIE);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ʀᴀᴄᴊᴀ żʏᴡɴᴏśᴄɪᴏᴡᴀ #a18b3dɪɪ"));
        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴢᴀᴘᴇᴡɴɪᴀ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ #a18b3dɪɪ &7ᴘᴏᴢɪᴏᴍᴜ"));
        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴍᴏżɴᴀ ᴜżʏć ᴅᴏ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ &8(&a-60 &7ᴍɪɴ ᴄᴢᴀꜱᴜ ᴏᴄᴢᴇᴋɪᴡᴀɴɪᴀ&8)"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        foodRationTier2 = item;
    }
    public static void createFoodRation1() {
        ItemStack item = new ItemStack(Material.COOKED_BEEF);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ʀᴀᴄᴊᴀ żʏᴡɴᴏśᴄɪᴏᴡᴀ #a18b3dɪ"));
        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴢᴀᴘᴇᴡɴɪᴀ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ #a18b3dɪ &7ᴘᴏᴢɪᴏᴍᴜ"));
        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴍᴏżɴᴀ ᴜżʏć ᴅᴏ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ &8(&a-30 &7ᴍɪɴ ᴄᴢᴀꜱᴜ ᴏᴄᴢᴇᴋɪᴡᴀɴɪᴀ&8)"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        foodRationTier1 = item;
    }

    public static void createToolPack1() {
        ItemStack item = new ItemStack(Material.WOODEN_PICKAXE);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴢᴇꜱᴛᴀᴡ ᴜʟᴇᴘꜱᴢᴏɴʏᴄʜ ɴᴀʀᴢęᴅᴢɪ #a18b3dɪ"));
        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴍᴏżɴᴀ ᴜżʏć ᴅᴏ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ &8(&a+10% &7ꜱᴢᴀɴꜱ ɴᴀ ᴘᴏᴡᴏᴅᴢᴇɴɪᴇ&8)"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        toolTier1 = item;
    }
    public static void createToolPack2() {
        ItemStack item = new ItemStack(Material.IRON_PICKAXE);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴢᴇꜱᴛᴀᴡ ᴜʟᴇᴘꜱᴢᴏɴʏᴄʜ ɴᴀʀᴢęᴅᴢɪ #a18b3dɪɪ"));
        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴍᴏżɴᴀ ᴜżʏć ᴅᴏ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ &8(&a+20% &7ꜱᴢᴀɴꜱ ɴᴀ ᴘᴏᴡᴏᴅᴢᴇɴɪᴇ&8)"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        toolTier2 = item;
    }
    public static void createToolPack3() {
        ItemStack item = new ItemStack(Material.GOLDEN_PICKAXE);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴢᴇꜱᴛᴀᴡ ᴜʟᴇᴘꜱᴢᴏɴʏᴄʜ ɴᴀʀᴢęᴅᴢɪ #a18b3dɪɪɪ"));
        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴍᴏżɴᴀ ᴜżʏć ᴅᴏ ᴡᴢᴍᴏᴄɴɪᴇɴɪᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ &8(&a+30% &7ꜱᴢᴀɴꜱ ɴᴀ ᴘᴏᴡᴏᴅᴢᴇɴɪᴇ&8)"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        toolTier3 = item;
    }

    public static ItemStack getMainHallUpgradeButton(int price, boolean isUnlocked, boolean isFinished, Long secondsTillReady) {
        ItemStack item = new ItemStack(Material.BRICKS);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7ꜱᴛʀᴇꜰᴀ ꜱᴛᴀᴄᴊɪ ʀᴢᴇᴍɪᴇśʟɴɪᴄᴢʏᴄʜ"));

        ArrayList<String> lore = new ArrayList<>();


        lore.add(" ");

        if(isUnlocked && isFinished) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            lore.add(ColorFixer.addColors("&aᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴊᴇꜱᴛ ᴊᴜż ᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&aᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴘʀᴢᴇᴊść ᴅᴏ ᴍᴇɴᴜ ᴜʟᴇᴘꜱᴢᴇń ꜱᴛᴀᴄᴊɪ ʀᴢᴇᴍɪᴇśʟɴɪᴄᴢʏᴄʜ"));
        } else if (isUnlocked) {

            lore.add(ColorFixer.addColors("&eᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌᴏ ᴋᴜᴘɪᴏɴᴇ ɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏɴ́ᴄᴢᴏɴᴇ ᴢᴀ&7: &6" + MessageManager.secondsToTimeFormat(Math.toIntExact(secondsTillReady))));


        } else {
            lore.add(ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏᴅᴀᴛᴋᴏᴡą̨ ᴘʀᴢᴇꜱᴛʀᴢᴇń ɴᴀ ʀᴏᴢᴡóᴊ ɢɪʟᴅɪ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + price + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&a%lᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴋᴜᴘɪć ᴜʟᴇᴘꜱᴢᴇɴɪᴇ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&cᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴊᴇꜱᴛ ᴡʏᴍᴀɢᴀɴᴇ ᴅᴏ ᴢᴀᴋᴜᴘᴜ ᴋᴏʟᴇᴊɴʏᴄʜ ᴜʟᴇᴘꜱᴢᴇń:"));
            lore.add(ColorFixer.addColors("&c- ᴋᴏᴡᴀʟ"));
            lore.add(ColorFixer.addColors("&c- ᴢᴀᴋʟɪɴᴀᴄᴢ "));
        }

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack getBlacksmithUpgradeButton(int price, boolean isUnlocked, boolean isFinished, long secondsTillReady) {
        ItemStack item = new ItemStack(Material.ANVIL);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴋᴜźɴɪᴀ ɢɪʟᴅʏᴊɴᴀ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        if(isUnlocked && isFinished) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            lore.add(ColorFixer.addColors("&aᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴊᴇꜱᴛ ᴊᴜż ᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
        } else if (isUnlocked) {

            lore.add(ColorFixer.addColors("&eᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌᴏ ᴋᴜᴘɪᴏɴᴇ ɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏɴ́ᴄᴢᴏɴᴇ ᴢᴀ&7: &6" + MessageManager.secondsToTimeFormat(Math.toIntExact(secondsTillReady))));
        } else {

            lore.add(ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏꜱᴛęᴘ ᴅᴏ ᴋᴜźɴɪ ɢɪʟᴅʏᴊɴᴇᴊ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + price + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&a%lᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴋᴜᴘɪć ᴜʟᴇᴘꜱᴢᴇɴɪᴇ"));
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack getSorcererUpgradeButton(int price, boolean isUnlocked, boolean isFinished, int secondsTillReady) {
        ItemStack item = new ItemStack(Material.ENCHANTING_TABLE);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴢᴀᴋʟɪɴᴀᴄᴢ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        if(isUnlocked && isFinished) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            lore.add(ColorFixer.addColors("&aᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴊᴇꜱᴛ ᴊᴜż ᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
        } else if (isUnlocked) {

            lore.add(ColorFixer.addColors("&eᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌᴏ ᴋᴜᴘɪᴏɴᴇ ɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏńᴄᴢᴏɴᴇ ᴢа&7: &6" + MessageManager.secondsToTimeFormat(secondsTillReady)));
        } else {

            lore.add(ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏꜱᴛęᴘ ᴅᴏ ᴢᴀᴋʟɪɴᴀᴄᴢᴀ ɢɪʟᴅʏᴊɴᴇɢᴏ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + price + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&a%lᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴋᴜᴘɪć ᴜʟᴇᴘꜱᴢᴇɴɪᴇ"));
        }

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }
    public static ItemStack getVentureUpgradeButton(int price, boolean isUnlocked, boolean isFinished, int secondsTillReady) {
        ItemStack item = new ItemStack(Material.CHEST_MINECART);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴇᴋꜱᴘᴇᴅʏᴄᴊᴇ ɢɪʟᴅʏᴊɴᴇ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        if(isUnlocked && isFinished) {
            itemMeta.addEnchant(Enchantment.LUCK, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            lore.add(ColorFixer.addColors("&aᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴊᴇꜱᴛ ᴊᴜż ᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
        } else if (isUnlocked) {

            lore.add(ColorFixer.addColors("&eᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌᴏ ᴋᴜᴘɪᴏɴᴇ ɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏńᴄᴢᴏɴᴇ ᴢа&7: &6" + MessageManager.secondsToTimeFormat(secondsTillReady)));
        } else {

            lore.add(ColorFixer.addColors("&7ᴅᴀᴊᴇ ᴍᴏżʟɪᴡᴏść ᴡʏꜱʏᴌᴀɴɪᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ ɢɪʟᴅʏᴊɴʏᴄʜ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + price + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&a%lᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴋᴜᴘɪć ᴜʟᴇᴘꜱᴢᴇɴɪᴇ"));
        }

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }


    public static ItemStack getUpgradeButton(GuildHideout.Upgrade upgrade, int price, boolean bought, boolean finished, Long timeTOCompletionSeconds) {
        return switch (upgrade) {
            case STATION_HALL -> getMainHallUpgradeButton(price, bought, finished, timeTOCompletionSeconds);
            case BLACKSMITH -> getBlacksmithUpgradeButton(price, bought, finished, timeTOCompletionSeconds);
            case SORCERER -> getSorcererUpgradeButton(price, bought, finished, Math.toIntExact(timeTOCompletionSeconds));
            case VENTURE -> getVentureUpgradeButton(price, bought, finished, Math.toIntExact(timeTOCompletionSeconds));
            default -> null;
        };
    }

    public static @NotNull ItemStack getStartExpeditionItem(double chance, int expeditionLevel, ExpeditionDto.ExpeditionObjective objective, long completionTime) {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setCustomModelData(1010);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴇᴋꜱᴘᴇᴅʏᴄᴊᴇ"));

        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");

        switch (objective) {
            case STONE -> lore.add(ColorFixer.addColors("&7ᴄᴇʟ: &fᴋᴀᴍɪᴇń"));
            case WOOD -> lore.add(ColorFixer.addColors("&7ᴄᴇʟ: &fᴅʀᴇᴡɴᴏ"));
            case CRYSTALS -> lore.add(ColorFixer.addColors("&7ᴄᴇʟ: &fᴋʀʏꜱᴛᴀłʏ"));
        }
        switch (expeditionLevel) {
            case 1 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &fłᴀᴛᴡᴀ"));
            case 2 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &fᴛʀᴜᴅɴᴀ"));
            case 3 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &fʙᴀʀᴅᴢᴏ ᴛʀᴜᴅɴᴀ"));
        }
        LocalDateTime dateTime = LocalDateTime.ofEpochSecond(completionTime, 0, ZoneOffset.UTC);
        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ꜱᴢᴀɴꜱᴀ ɴᴀ ᴘᴏᴡᴏᴅᴢᴇɴɪᴇ: &f" + chance * 100 + "%"));
        lore.add(" ");
        lore.add("ɢᴏᴅᴢɪɴᴀ ᴜᴋᴏńᴄᴢᴇɴɪ: " + dateTime.getHour() + ":" + dateTime.getMinute() + ":" + dateTime.getSecond());
        lore.add(" ");
        lore.add("ᴋʟɪᴋɴɪᴊ ᴀʙʏ ʀᴏᴢᴘᴏᴄᴢąć");


        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack getDifficultyItem(double chance) {
        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = item.getItemMeta();
        if(chance < 0.35) {
            itemMeta.setCustomModelData(3);
            itemMeta.setDisplayName(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &4ᴛʀᴜᴅɴʏ &8&l[&4" + (int)(chance * 100) + "%&8&l]"));
        } else if (chance < 0.55) {
            itemMeta.setCustomModelData(2);
            itemMeta.setDisplayName(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &eśʀᴇᴅɴɪ &8&l[&e" + (int)(chance * 100) + "%&8&l]"));
        } else {
            itemMeta.setCustomModelData(1);
            itemMeta.setDisplayName(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &aŁᴀᴛᴡʏ &8&l[&a" + (int)(chance * 100) + "%&8&l]"));
        }

        item.setItemMeta(itemMeta);
        return item;
    }

    public static @NotNull ItemStack createObjectiveGuiItem(ExpeditionVariant variant, int buttonSlot) {
        ItemStack item;
        String itemName;

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");


        switch (variant.getObjective()) {
            case WOOD -> {
                itemName = "&7ᴛᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊᴀ ᴜᴢᴜᴘᴇᴌɴɪ ᴢᴀᴘᴀꜱʏ ᴅʀᴇᴡɴᴀ".replace("&", "§");
            }
            case STONE -> {
                itemName = "&7ᴛᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊᴀ ᴜᴢᴜᴘᴇᴌɴɪ ᴢᴀᴘᴀꜱʏ ᴋᴀᴍɪᴇɴɪ".replace("&", "§");
            }
            default -> {
                itemName = "&7ᴛᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊᴀ ᴜᴢᴜᴘᴇᴌɴɪ ᴢᴀᴘᴀꜱʏ ᴋʀʏꜱᴢᴛᴀᴌóᴡ".replace("&", "§");
            }
        }

        int customModelData = 0;

        switch (buttonSlot) {
            case 0 -> {
                item = new ItemStack(Material.NAME_TAG);
                switch (variant.getLevel()) {
                    default -> {
                        customModelData = 1;
                    }
                    case 2 -> {
                        customModelData = 2;
                    }
                    case 3 -> {
                        customModelData = 3;
                    }
                }
            }
            case 2 -> {
                switch (variant.getObjective()) {
                    case STONE -> {
                        item = new ItemStack(Material.STONE);
                    }
                    case WOOD -> {
                        item = new ItemStack(Material.OAK_LOG);
                    }
                    default -> {
                        item = new ItemStack(Material.EMERALD);
                    }
                }
            }
            default -> {
                item = new ItemStack(Material.MAP);
                customModelData = 1010;
            }
        }

        switch (variant.getLevel()) {
            case 1 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &aŁᴀᴛᴡʏ &8&l[&a" + (int)(variant.getBaseChance() * 100) + "%&8&l]"));
            case 2 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &eśʀᴇᴅɴɪ &8&l[&e" + (int)(variant.getBaseChance() * 100) + "%&8&l]"));
            case 3 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &4ᴛʀᴜᴅɴʏ &8&l[&4" + (int)(variant.getBaseChance() * 100) + "%&8&l]"));
        }
        lore.add("&7ᴄᴢᴀꜱ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: ".replace("&", "§") + MessageManager.secondsToTimeFormat((int) variant.getCooldownSeconds()));

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴇᴋꜱᴘᴇᴅʏᴄᴊᴇ"));

        lore.add(" ");
        lore.add("§8§lᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴡʏʙʀᴀć");

        itemMeta.setCustomModelData(customModelData);

        itemMeta.setLore(lore);

        itemMeta.setDisplayName(itemName);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack createBonusSuppliedItem(boolean isSupplied) {
        ItemStack item = new ItemStack(Material.LIME_DYE);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        itemMeta.setDisplayName(ColorFixer.addColors("&7&lᴅᴏᴅᴀᴛᴋᴏᴡᴇ ᴢᴀᴘᴀꜱʏ"));

        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");

        if(isSupplied) {
            lore.add(ColorFixer.addColors("&aᴢᴀᴘᴀꜱʏ ᴅᴏᴅᴀɴᴇ"));
            itemMeta.setCustomModelData(1);
        } else {
            lore.add(ColorFixer.addColors("&cᴢᴀᴘᴀꜱʏ ɴɪᴇ ᴅᴏᴅᴀɴᴇ"));
            itemMeta.setCustomModelData(2);
        }
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }
}

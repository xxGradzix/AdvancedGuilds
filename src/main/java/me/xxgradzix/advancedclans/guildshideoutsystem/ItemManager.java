package me.xxgradzix.advancedclans.guildshideoutsystem;

import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionDto;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionGui;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionVariant;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

import static me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionDto.ExpeditionObjective.STONE;
import static me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionDto.ExpeditionObjective.WOOD;

public class ItemManager {

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

        ItemStack item = new ItemStack(Material.CREEPER_BANNER_PATTERN);

        ItemMeta itemMeta = item.getItemMeta();

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
                        customModelData = 3;
                    }
                    case 3 -> {
                        customModelData = 4;
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
            case 1 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &aŁᴀᴛᴡʏ &8&l[&a" + (int)(variant.getChance() * 100) + "%&8&l]"));
            case 2 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &eśʀᴇᴅɴɪ &8&l[&e" + (int)(variant.getChance() * 100) + "%&8&l]"));
            case 3 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &4ᴛʀᴜᴅɴʏ &8&l[&4" + (int)(variant.getChance() * 100) + "%&8&l]"));
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
}

package me.xxgradzix.advancedclans.guildshideoutsystem;

import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

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
            case VENTURE -> getVentureUpgradeButton(price, bought, finished, Math.toIntExact(timeTOCompletionSeconds));
            default -> null;
        };
    }
}

package me.xxgradzix.advancedclans.guildshideoutsystem;

import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.GuildPanel.GuildPanelItemManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionItemManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.storage.StorageItemManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HideoutGeneralItemManager {


    public static void init() {
        StorageItemManager.init();
        ExpeditionItemManager.init();
        GuildPanelItemManager.init();
    }

    // TODO mistrz wojny zamiast wojen w panelu

    public static ItemStack getArenaUpgradeButton(int price, boolean isUnlocked, boolean isFinished, Long secondsTillReady) {
        ItemStack item = new ItemStack(Material.BRICKS);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7ꜱᴛʀᴇꜰᴀ ɴɪᴇʟᴇɢᴀʟɴʏᴄʜ ʙɪᴢɴᴇꜱóᴡ"));

        ArrayList<String> lore = new ArrayList<>();


        lore.add(" ");

        if(isUnlocked && isFinished) {
            itemMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            lore.add(ColorFixer.addColors("&aᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴊᴇꜱᴛ ᴊᴜż ᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&aᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴘʀᴢᴇᴊść ᴅᴏ ᴍᴇɴᴜ ꜱᴛʀᴇꜰʏ ɴɪᴇʟᴇɢᴀʟɴʏᴄʜ ʙɪᴢɴᴇꜱóᴡ"));
        } else if (isUnlocked) {

            lore.add(ColorFixer.addColors("&eᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌᴏ ᴋᴜᴘɪᴏɴᴇ ɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏɴ́ᴄᴢᴏɴᴇ ᴢᴀ&7: &6" + MessageManager.secondsToTimeFormat(Math.toIntExact(secondsTillReady))));


        } else {
            lore.add(ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏᴅᴀᴛᴋᴏᴡą̨ ᴘʀᴢᴇꜱᴛʀᴢᴇń ɴᴀ ɴɪᴇʟᴇɢᴀʟɴᴇ ʙɪᴢɴᴇꜱʏ ɢɪʟᴅɪɪ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + price + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&a%lᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴋᴜᴘɪć ᴜʟᴇᴘꜱᴢᴇɴɪᴇ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&cᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴅᴀᴊᴇ ᴅᴏꜱᴛęᴘ ᴅᴏ:"));
            lore.add(ColorFixer.addColors("&c- ᴘᴏᴅᴢɪᴇᴍɴᴀ ᴀʀᴇɴᴀ"));
            lore.add(ColorFixer.addColors("&c- ᴄᴢᴀʀɴʏ ʀʏɴᴇᴋ"));
        }

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack getPremiumGuildCoin() {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(ColorFixer.addColors("&2ᴍᴏɴᴇᴛᴀ ᴘʀᴇᴍɪᴜᴍ"));
        itemMeta.setCustomModelData(1);
        itemMeta.setMaxStackSize(99);
        item.setItemMeta(itemMeta);
        return item;
    }


    public static ItemStack getCurrentCooldownItem(int secondsToComplete) {

        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴘʀᴢʏꜱᴘɪᴇꜱᴢ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        lore.add(ColorFixer.addColors("&7ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴀᴋᴏɴ́ᴄᴢʏ ꜱɪᴇ̨ ᴢᴀ&7: &6" + MessageManager.secondsToTimeFormatSkipSeconds(secondsToComplete)));

        lore.add(" ");


        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴘᴘᴍ ᴀʙʏ ᴘᴏᴍɪɴąć ᴄᴢᴀꜱ ᴏᴄᴢᴇᴋɪᴡᴀɴɪᴀ" + MessageManager.secondsToTimeFormatSkipSeconds(secondsToComplete)));

        int price = secondsToComplete /(60 * 30);
        lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ: &6" + price + " ᴍᴏɴᴇᴛ ᴘʀᴇᴍɪᴜᴍ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }


    public static @NotNull ItemStack getRequiredPremiumCoins(int secondsToComplete, boolean displayPrice) {

        int price = secondsToComplete /(60 * 30);

        ItemStack item;
        ItemMeta itemMeta;

        if(displayPrice) {
            item = new ItemStack(Material.GLASS_PANE);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(3020000 + price);
        } else {
            item = new ItemStack(Material.MAP);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(1010);
        }


        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴘʀᴢʏꜱᴘɪᴇꜱᴢ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        lore.add(ColorFixer.addColors("&7ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴀᴋᴏńᴄᴢʏ ꜱɪę ᴢᴀ&7: &6" + MessageManager.secondsToTimeFormatSkipSeconds(secondsToComplete)));

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴘᴘᴍ ᴀʙʏ ᴘᴏᴍɪɴąć ᴄᴢᴀꜱ ᴏᴄᴢᴇᴋɪᴡᴀɴɪᴀ" + MessageManager.secondsToTimeFormatSkipSeconds(secondsToComplete)));

        lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ: &6" + price + " ᴍᴏɴᴇᴛ ᴘʀᴇᴍɪᴜᴍ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

}



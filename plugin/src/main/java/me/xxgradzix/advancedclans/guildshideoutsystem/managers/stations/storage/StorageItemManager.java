package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.storage;

import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

import static me.xxgradzix.advancedclans.messages.MessageManager.getRomanNumerals;

public class StorageItemManager {


    public static void init() {

    }

    public static @NotNull ItemStack getStorageItem(int num, boolean isOwner, boolean hasAccess, boolean isUnlocked) {

        if(isOwner && isUnlocked) hasAccess = true;

        ItemStack item;
        ItemMeta itemMeta;

        if(isUnlocked) {
            if(hasAccess) {
                item = new ItemStack(Material.MAP);
                itemMeta = item.getItemMeta();
                itemMeta.setCustomModelData(1010);
            } else {
                item = new ItemStack(Material.NAME_TAG);
                itemMeta = item.getItemMeta();
                itemMeta.setCustomModelData(666666);
            }
        } else {
            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(666666);

        }


        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        String romeNum = getRomanNumerals(num);
        itemMeta.setDisplayName(ColorFixer.addColors("&7&lᴍᴀɢᴀᴢʏɴ #a18b3d" + romeNum));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        if(!hasAccess) {
            lore.add(ColorFixer.addColors("&7ɴɪᴇ ᴍᴀꜱᴢ ᴅᴏꜱᴛęᴘᴜ ᴅᴏ ᴛᴇɢᴏ ᴍᴀɢᴀᴢʏɴᴜ, ᴘᴏᴘʀᴏś ʟɪᴅᴇʀᴀ ꜱᴡᴏᴊᴇᴊ ɢɪʟᴅɪ ᴏ ᴜᴘʀᴀᴡɴɪᴇɴɪᴀ"));
        } else {
            if(isOwner) {
                lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ #a18b3dʟᴘᴍ &7ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴀɢᴀᴢʏɴ"));
                lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ #a18b3dᴘᴘᴍ &7ᴀʙʏ ᴇᴅʏᴛᴏᴡᴀᴄ ᴜᴘʀᴀᴡɴɪᴇɴɪᴀ"));

            } else {
                lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴀɢᴀᴢʏɴ"));
            }
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static @NotNull ItemStack getPermissionItem(StoragePermissionSlotType storagePermissionSlotType, UUID uuid, boolean contains) {

        if(storagePermissionSlotType.equals(StoragePermissionSlotType.HEAD)) {
            ItemStack item = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(uuid));
            skullMeta.setDisplayName(ColorFixer.addColors("&7&l" + Bukkit.getOfflinePlayer(uuid).getName()));
            skullMeta.setCustomModelData(1);
            item.setItemMeta(skullMeta);
            return item;
        }

        ItemStack item = new ItemStack(Material.LIME_DYE);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7&l" + Bukkit.getOfflinePlayer(uuid).getName()));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ " + (contains ? "&cᴜꜱᴜɴąć" : "&aᴅᴏᴅᴀć") + "&7 ᴘᴇʀᴍɪꜱję"));

        if(contains) {
            itemMeta.setCustomModelData(1);
            itemMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            itemMeta.setCustomModelData(2);
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack getPersonalStorageItem() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setCustomModelData(1010);

        itemMeta.setDisplayName(ColorFixer.addColors("&7&lᴍᴀɢᴀᴢʏɴ ᴘᴇʀꜱᴏɴᴀʟɴʏ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴘʀᴢᴇᴅᴍɪᴏᴛʏ ᴡ ᴛʏᴍ ᴍᴀɢᴀᴢʏɴɪᴇ ʙęᴅą ᴅᴏꜱᴛęᴘɴᴇ ᴛʏʟᴋᴏ ᴅʟᴀ ᴄɪᴇʙɪᴇ"));
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴀɢᴀᴢʏɴ"));
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }


    public static ItemStack getPreviousPage(boolean isActive) {
        ItemStack item = new ItemStack(Material.GHAST_TEAR);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName((ColorFixer.addColors("&7ᴘᴏᴘʀᴢᴇᴅɴɪᴀ ꜱᴛʀᴏɴᴀ")));

        ArrayList<String> lore = new ArrayList<>();

        if(!isActive) {
            lore.add(ColorFixer.addColors("&7ᴊᴇꜱᴛᴇś ᴊᴜż ɴᴀ ᴘɪᴇʀᴡꜱᴢᴇᴊ ꜱᴛʀᴏɴɪᴇ"));
        } else {
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴍɪᴇɴɪć ɴᴀ ᴘᴏᴘʀᴢᴇᴅɴɪą ꜱᴛʀᴏɴᴇ"));
        }
        itemMeta.setLore(lore);

        itemMeta.setCustomModelData(99000 + (isActive ? 2 : 1));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getNextPage(boolean isActive) {
        ItemStack item = new ItemStack(Material.GHAST_TEAR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName((ColorFixer.addColors("&7ɴᴀꜱᴛęᴘɴᴀ ꜱᴛʀᴏɴᴀ")));
        ArrayList<String> lore = new ArrayList<>();
        if(!isActive) {
            lore.add(ColorFixer.addColors("&7ᴊᴇꜱᴛᴇś ᴊᴜż ɴᴀ ᴏꜱᴛᴀᴛɴɪᴇᴊ ꜱᴛʀᴏɴɪᴇ"));
        } else {
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴍɪᴇɴɪć ɴᴀ ɴᴀꜱᴛęᴘɴą ꜱᴛʀᴏɴᴇ"));
        }

        itemMeta.setLore(lore);

        itemMeta.setCustomModelData(66000 + (isActive ? 2 : 1));
        item.setItemMeta(itemMeta);
        return item;
    }


}

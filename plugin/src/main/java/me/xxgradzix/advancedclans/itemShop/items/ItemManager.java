package me.xxgradzix.advancedclans.itemShop.items;

import dev.triumphteam.gui.builder.item.BaseItemBuilder;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductPriceType;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class ItemManager {

    public static ItemStack equal1Button;
    public static ItemStack equal64Button;
    public static ItemStack plus1Button;

    public static ItemStack plus16Button;
    public static ItemStack minus1Button;
    public static ItemStack minus16Button;

    public static ItemStack returnButton;
    public static ItemStack addNewItem;

    public static ItemStack nextPageButton;
    public static ItemStack previousPageButton;


    public static void init() {

        createEqual1Button();
        createEqual64Button();
        createPlus1Button();
        createPlus16Button();
        createMinus1Button();
        createMinus16Button();

        createReturnButton();
        createAddNewItem();

        createNextPageButton();
        createPreviousPageButton();

    }

    private static void createNextPageButton() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "ɴᴀꜱᴛęᴘɴᴀ ᴋᴀᴛᴇɢᴏʀɪᴀ");

        item.setItemMeta(meta);

        nextPageButton = item;
    }

    private static void createPreviousPageButton() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        meta.setDisplayName(ChatColor.GRAY + "" + ChatColor.BOLD + "ᴘᴏᴘʀᴢᴇᴅɴɪᴀ ᴋᴀᴛᴇɢᴏʀɪᴀ");

        item.setItemMeta(meta);

        previousPageButton = item;
    }

    private static void createReturnButton() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);
        meta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "ᴘᴏᴡʀóᴛ ᴅᴏ ꜱᴋʟᴇᴘᴜ");

        item.setItemMeta(meta);

        returnButton = item;
    }

    public static ItemStack createCategoryButton(String categoryName) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + categoryName);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack createProductBuyButton(ItemStack itemStack, double totalPrice, int stacksMultiplier, int stackSize, ShopType paymentMethod) {

        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        List<String> lore = meta.getLore();
        if(lore == null) {
            lore = new ArrayList<>();
        }
        meta.setDisplayName(ChatColor.GREEN + "" + ChatColor.BOLD + "ᴋᴜᴘ");
        lore.add(ColorFixer.addColors("&7ᴘʀᴢᴇᴅᴍɪᴏᴛ&8: &7" + (itemStack.getItemMeta().getDisplayName().isEmpty() ? itemStack.getType().name() : itemStack.getItemMeta().getDisplayName())));
        lore.add(ColorFixer.addColors("&7ɪʟᴏść: &a" + stacksMultiplier + "x " + stackSize));
        lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ: &6" + totalPrice));
        lore.add(ColorFixer.addColors("&7ᴡᴀʟᴜᴛᴀ: &7" + paymentMethod.getNameFormated()));

        meta.setLore(lore);
        item.setItemMeta(meta);

        item.setAmount(1);
        return item;
    }

    public static ItemStack createProductDisplayButton(ItemStack itemStack, Map<ShopType, ItemShopProductPriceType> prices) {

        ItemStack item = new ItemStack(itemStack);
        ItemMeta meta = item.getItemMeta();

        List<String> lore = meta.getLore();
        if(lore == null) {
            lore = new ArrayList<>();
        }

        if(!prices.isEmpty()) {
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴘʀᴢᴇᴊść ᴅᴏ ᴍᴇɴᴜ ᴢᴀᴋᴜᴘᴜ"));
        } else {
            lore.add(ChatColor.RED + "ᴀᴋᴛᴜᴀʟɴɪᴇ ɴɪᴇ ᴅᴏꜱᴛęᴘɴᴇ");
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static void createEqual1Button() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        meta.setDisplayName(ChatColor.RED + "" +  ChatColor.BOLD + "ᴜꜱᴛᴀᴡ 1");

        item.setItemMeta(meta);

        equal1Button = item;
    }
    private static void createEqual64Button() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        meta.setDisplayName(ChatColor.GREEN + "" +  ChatColor.BOLD + "ᴜꜱᴛᴀᴡ ᴍᴀx");

        item.setItemMeta(meta);

        equal64Button = item;
    }

    private static void createPlus1Button() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        meta.setDisplayName(ChatColor.GREEN + "" +  ChatColor.BOLD + "+1");

        item.setItemMeta(meta);

        plus1Button = item;
    }

    private static void createPlus16Button() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        meta.setDisplayName(ChatColor.GREEN + "" +  ChatColor.BOLD + "+16");

        item.setItemMeta(meta);

        plus16Button = item;
    }

    private static void createMinus1Button() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        meta.setDisplayName(ChatColor.RED + "" +  ChatColor.BOLD + "-1");

        item.setItemMeta(meta);

        minus1Button = item;
    }

    private static void createMinus16Button() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta meta = item.getItemMeta();
        meta.setCustomModelData(1010);

        meta.setDisplayName(ChatColor.RED + "" +  ChatColor.BOLD + "-16");

        item.setItemMeta(meta);

        minus16Button = item;
    }

    public static ItemStack createChoosePaymentOptionButton(ShopType paymentMethod, Set<ShopType> availablePaymentMethods) {

        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setCustomModelData(1010);

        itemMeta.setDisplayName(ChatColor.GOLD + "" + ChatColor.BOLD + "ᴢᴍɪᴇń ᴍᴇᴛᴏᴅᴇ ᴘʟᴀᴄᴇɴɪᴀ");

        List<String> lore = new ArrayList<>();

        for (ShopType shopType : availablePaymentMethods) {

            if(shopType == paymentMethod) {
                lore.add(ColorFixer.addColors("&a&l→ ".replace(" ", "\u2007") + shopType.getName() ));
            } else {
                lore.add(ColorFixer.addColors("&7  ".replace(" ", "\u2007") + shopType.getName()));
            }
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static ItemStack createStackMultiplicatorButton(int multtiplier) {
        ItemStack item = new ItemStack(Material.GOLD_INGOT);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ColorFixer.addColors("&7&lᴍɴᴏżɴɪᴋ&8: &6" + multtiplier));
        meta.setCustomModelData(50500 + multtiplier);

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ʟᴘᴍ ᴀʙʏ ᴢᴡɪęᴋꜱᴢʏć"));
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴘᴘᴍ ᴀʙʏ ᴢᴍɴɪᴇᴊꜱᴢʏć"));

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }
    public static void createAddNewItem() {

        ItemStack item = new ItemStack(Material.CRAFTING_TABLE);
        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.GREEN + "" +  ChatColor.BOLD + "ᴅᴏᴅᴀᴊ ᴘʀᴇᴅᴍɪᴏᴛ");

        ArrayList<String> lore = new ArrayList<>();

        lore.add(ColorFixer.addColors("&7ɴᴀᴊᴇᴅᴢ ᴘʀᴢᴇᴅᴍɪᴏᴛᴇᴍ ɪ ᴋʟɪᴋɴɪᴊ, ᴀʙʏ ᴅᴏᴅᴀć ᴘʀᴇᴅᴍɪᴏᴛ"));

        item.setItemMeta(meta);


        addNewItem = item;

    }
}

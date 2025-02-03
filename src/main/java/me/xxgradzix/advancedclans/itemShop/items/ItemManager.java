package me.xxgradzix.advancedclans.itemShop.items;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {

    public static double price;

    public static ItemStack timeCoinShopButton;
    public static ItemStack killCoinShopButton;
    public static ItemStack moneyShopButton;

    public static void init() {
        createTimeCoinShopButton();
        createKillsCoinShopButton();
        createMoneyCoinShopButton();
    }

    public static ItemStack createCategoryButton(String categoryName) {
        ItemStack item = new ItemStack(Material.BOOK);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.BOLD + categoryName);
        item.setItemMeta(meta);
        return item;
    }
    public static ItemStack createProductButton(ItemStack itemStack, int price) {
        ItemStack item = new ItemStack(itemStack);
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        if(lore == null) {
            lore = new ArrayList<>();
        }
        lore.add(" ");
        lore.add(ChatColor.GRAY + "Cena tego przedmiotu to: " + ChatColor.GREEN + price + "$");
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    private static void createTimeCoinShopButton() {
        ItemStack item = new ItemStack(Material.CLOCK);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GREEN + "Sklep za czas");

        List<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add("Kliknij aby kupić przedmioty za czas spedzony w grze");

        meta.setLore(lore);

        item.setItemMeta(meta);

        timeCoinShopButton = item;
    }
    private static void createKillsCoinShopButton() {
        ItemStack item = new ItemStack(Material.IRON_SWORD);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.RED + "Sklep za zabójstwa");

        List<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add("Kliknij aby kupić przedmioty za zabójstwa");
        // TODO masz do wydania x coinsow

        meta.setLore(lore);

        item.setItemMeta(meta);

        killCoinShopButton = item;
    }
    private static void createMoneyCoinShopButton() {

        ItemStack item = new ItemStack(Material.GOLD_INGOT);

        ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(ChatColor.BOLD + "" + ChatColor.GOLD + "Sklep za pieniadze");

        List<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add("Kliknij aby kupić przedmioty za pieniadze");

        meta.setLore(lore);

        item.setItemMeta(meta);

        moneyShopButton = item;
    }
}

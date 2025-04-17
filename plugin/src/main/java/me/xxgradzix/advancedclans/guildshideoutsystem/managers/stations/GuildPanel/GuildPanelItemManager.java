package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.GuildPanel;

import entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.entities.hideout.fields.UpgradeImpl;
import com.xxgradzix.advancedguildsapi.exceptions.hideOuts.UpgradeWasNotBoughtException;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GuildPanelItemManager {


    public static ItemStack playerListItem;
    public static ItemStack shieldItem;
    public static ItemStack homeItemItem;
    public static ItemStack questsItem;
    public static ItemStack warItem;
    public static ItemStack allianceItem;

    public static ItemStack deputyButton;
    public static ItemStack kickPlayerButton;
    public static ItemStack permissionsButton;

    public static ItemStack shieldInfo;
    public static ItemStack homeInfo;
    public static ItemStack questsInfo;
    public static ItemStack warInfo;

    public static ItemStack stationHallInfo;
    public static ItemStack illegalBusinessInfo;


    public static void init() {
        createPlayerListItem();
        createShieldItem();
        createHomeItemItem();
        createQuestsItem();
        createWarItem();
        createAllianceItem();

        createDeputyButton();
        createPermissionsButton();
        createKickPlayerButton();

        createShieldInfo();
        createHomeInfo();
        createQuestsInfo();
        createWarInfo();
        createStationHallInfo();
        createIllegalBusinessInfo();

    }
    private static void createShieldInfo() {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("#a18b3dɪɴꜰᴏʀᴍᴀᴄᴊᴇ"));

        List<String> lore = new ArrayList<>();


        lore.add(ColorFixer.addColors("&7" + "&aᴀᴋᴛʏᴡɴᴀ ᴛᴀʀᴄᴢᴀ&7 ᴏᴄʜʀᴏɴɪ ɢɪʟᴅɪę ᴘʀᴢᴇᴅ ᴀᴛᴀᴋᴀᴍɪ ɪɴɴʏᴄʜ"));
        lore.add(ColorFixer.addColors("&7" + "ᴘᴏᴅᴄᴢᴀꜱ ᴡʏᴢɴᴀᴄᴢᴏɴʏᴄʜ ɢᴏᴅᴢɪɴ, ᴏᴅ &a22:00 &7ᴅᴏ &a18:00,"));
        lore.add(ColorFixer.addColors("&7" + "ɴɪᴇ ᴍᴏżɴᴀ ᴢᴀᴀᴛᴀᴋᴏᴡᴀć ɢɪʟᴅɪɪ."));
        lore.add(ColorFixer.addColors("&7" + " "));
        lore.add(ColorFixer.addColors("&7" + "ᴄᴢᴀꜱ ᴛʀᴡᴀɴɪᴀ ᴛᴀʀᴄᴢʏ ᴍᴏżɴᴀ ᴡʏᴅłᴜżʏć, ᴜᴢᴜᴘᴇłɴɪᴀᴊąᴄ ᴢᴀꜱᴏʙʏ"));
        lore.add(ColorFixer.addColors("&7" + "&8 - &7ɪᴍ ᴡɪęᴄᴇᴊ &bᴄᴢłᴏɴᴋóᴡ&7, ᴛʏᴍ ᴡɪęᴋꜱᴢᴇ ᴢᴀɢᴏᴛᴏᴡᴀɴɪᴀ"));

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        shieldInfo = item;
    }

    private static void createStationHallInfo() {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("#a18b3dɪɴꜰᴏʀᴍᴀᴄᴊᴇ"));

        List<String> lore = new ArrayList<>();


        lore.add(ColorFixer.addColors("&7" + "ꜱᴛʀᴇꜰᴀ ᴡᴀʀꜱᴢᴛᴀᴛóᴡ ᴘᴏᴢᴡᴀʟᴀ ɴᴀ ᴡʏᴋᴜᴘɪᴇɴɪᴇ ꜱᴛᴀɴᴏᴡɪꜱᴋ"));
        lore.add(ColorFixer.addColors("&7" + "ʀᴢᴇᴍɪᴇśʟɴɪᴄᴢʏᴄʜ:"));
        lore.add(ColorFixer.addColors("&7" + " &8- &7ᴋᴏᴡᴀʟᴀ"));
        lore.add(ColorFixer.addColors("&7" + " &8- &7ᴢᴀᴋʟɪɴᴀᴄᴢᴀ"));
        lore.add(ColorFixer.addColors("&7" + " &8- &7ᴋᴀᴘɪᴛᴀɴᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ"));

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        stationHallInfo = item;
    }


    private static void createIllegalBusinessInfo() {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("#a18b3dɪɴꜰᴏʀᴍᴀᴄᴊᴇ"));

        List<String> lore = new ArrayList<>();


        lore.add(ColorFixer.addColors("&7" + "ꜱᴛʀᴇꜰᴀ ʙɪᴢɴᴇꜱᴜ ᴘᴏᴢᴡᴀʟᴀ ɴᴀ ᴏᴛᴡᴀʀᴄɪᴇ ʀʏɴᴋᴜ ᴅʟᴀ ɪɴɴʏᴄʜ"));
        lore.add(ColorFixer.addColors("&7" + "ɢʀᴀᴄᴢʏ, ᴏʀɢᴀɴɪᴢᴏᴡᴀɴɪᴇ ᴛᴜʀɴɪᴇᴊóᴡ, ᴀ ᴛᴀᴋżᴇ ᴡʏꜱᴛᴀᴡɪᴀɴɪᴇ"));
        lore.add(ColorFixer.addColors("&7" + "ᴘʀᴢᴇᴅᴍɪᴏᴛóᴡ ɴᴀ ᴀᴜᴋᴄᴊᴇ ꜱᴇʀᴡᴇʀᴏᴡą."));

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        illegalBusinessInfo = item;
    }

    private static void createHomeInfo() {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("#a18b3dɪɴꜰᴏʀᴍᴀᴄᴊᴇ"));

        List<String> lore = new ArrayList<>();

//        lore.add(ColorFixer.addColors("&7" + "ᴘᴏᴢɪᴏᴍ ᴋʀʏᴊóᴡᴋɪ ᴅᴇᴛᴇʀᴍɪɴᴜᴊᴇ ʟɪᴄᴢʙę ᴅᴏꜱᴛęᴘɴʏᴄʜ ᴜʟᴇᴘꜱᴢᴇń"));
//        lore.add(ColorFixer.addColors("&7" + "ᴅʟᴀ ɢɪʟᴅɪɪ. ᴀᴡᴀɴꜱ ɴᴀ ᴡʏżꜱᴢʏ ᴘᴏᴢɪᴏᴍ ᴡʏᴍᴀɢᴀ ᴏꜱɪąɢɴɪęᴄɪᴀ "));
//        lore.add(ColorFixer.addColors("&7" + "ᴏᴋʀᴇśʟᴏɴᴇɢᴏ ᴘʀᴏɢᴜ xᴘ, ᴘᴏ ᴄᴢʏᴍ ᴍᴏżɴᴀ ᴢᴀᴋᴜᴘɪć ᴋᴏʟᴇᴊɴʏ ᴘᴏᴢɪᴏᴍ."));
//        lore.add(ColorFixer.addColors("&7" + "xᴘ ᴢᴅᴏʙʏᴡᴀ ꜱɪę ᴘᴏᴘʀᴢᴇᴢ ᴡʏᴋᴏɴʏᴡᴀɴɪᴇ ᴢᴀᴅᴀń ᴋʀʏᴊóᴡᴋɪ ʟᴜʙ "));
//        lore.add(ColorFixer.addColors("&7" + "ᴜᴄᴢᴇꜱᴛɴɪᴄᴢąᴄ ᴡ ᴡᴏᴊɴᴀᴄʜ ᴢ ɪɴɴʏᴍɪ ɢɪʟᴅɪᴀᴍɪ."));


        lore.add(ColorFixer.addColors("&7" + "&aᴋʀʏᴊóᴡᴋᴀ&7 ᴘᴏᴢᴡᴀʟᴀ ɴᴀ ʀᴏᴢᴡóᴊ ᴜᴍɪᴇᴊęᴛɴᴏśᴄɪ ɢɪʟᴅɪɪ"));
        lore.add(ColorFixer.addColors("&7" + "ɪ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴇ ɴᴏᴡʏᴄʜ ᴜʟᴇᴘꜱᴢᴇń."));
        lore.add(ColorFixer.addColors("&7" + " "));
        lore.add(ColorFixer.addColors("&7" + "ᴀᴡᴀɴꜱᴏᴡᴀɴɪᴇ ɴᴀ ᴡʏżꜱᴢʏ ᴘᴏᴢɪᴏᴍ ᴡʏᴍᴀɢᴀ &bxᴘ&7"));
        lore.add(ColorFixer.addColors("&7" + "ᴘᴏᴢʏꜱᴋɪᴡᴀɴᴇɢᴏ &8ᴘʀᴢᴇᴢ ᴡʏᴋᴏɴʏᴡᴀɴɪᴇ &aᴢᴀᴅᴀń&7 ʟᴜʙ"));
        lore.add(ColorFixer.addColors("&7" + "ᴜᴄᴢᴇꜱᴛɴɪᴄᴢąᴄ ᴡ ᴡᴏᴊɴᴀᴄʜ ᴢ ɪɴɴʏᴍɪ ɢɪʟᴅɪᴀᴍɪ."));

//        lore.add(ColorFixer.addColors("&7" + "ɴᴀ ᴋᴀżᴅʏᴍ &8ᴘᴏᴢɪᴏᴍɪᴇ &7ᴜᴢᴜᴘᴇłɴɪᴀꜱᴢ &8ᴋʀʏᴊóᴡᴋę."));


        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        homeInfo = item;
    }

    private static void createQuestsInfo() {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("#a18b3dɪɴꜰᴏʀᴍᴀᴄᴊᴇ"));

        List<String> lore = new ArrayList<>();

//        lore.add(ColorFixer.addColors("&7" + "ᴢᴀᴅᴀɴɪᴀ ᴋʀʏᴊóᴡᴋɪ ᴛᴏ ᴄᴏᴛʏɢᴏᴅɴɪᴏᴡᴇ ᴍɪꜱᴊᴇ ᴅᴏꜱᴛęᴘɴᴇ ᴅʟᴀ"));
//        lore.add(ColorFixer.addColors("&7" + "ᴡłᴀśᴄɪᴄɪᴇʟɪ ᴋʀʏᴊóᴡᴇᴋ. ᴋᴀżᴅᴀ ᴋʀʏᴊóᴡᴋᴀ ʙɪᴇʀᴢᴇ ᴡ ɴɪᴄʜ ᴜᴅᴢɪᴀł,"));
//        lore.add(ColorFixer.addColors("&7" + "ᴀ ɴᴀ ᴋᴏɴɪᴇᴄ ᴛʏɢᴏᴅɴɪᴀ ᴘᴏꜱᴛęᴘʏ ꜱą ᴡᴇʀʏꜰɪᴋᴏᴡᴀɴᴇ. "));
//        lore.add(ColorFixer.addColors("&7" + "ᴡ ᴢᴀʟᴇżɴᴏśᴄɪ ᴏᴅ ᴡʏɴɪᴋᴜ ᴘʀᴢʏᴢɴᴀᴡᴀɴᴇ ꜱą ᴘᴜɴᴋᴛʏ xᴘ:"));
//        lore.add(ColorFixer.addColors("&7" + " "));
//        lore.add(ColorFixer.addColors("&7" + "1. ᴍɪᴇᴊꜱᴄᴇ: x punktów"));
//        lore.add(ColorFixer.addColors("&7" + "2. ᴍɪᴇᴊꜱᴄᴇ: x punktów"));
//        lore.add(ColorFixer.addColors("&7" + "3. ᴍɪᴇᴊꜱᴄᴇ: x punktów"));
//        lore.add(ColorFixer.addColors("&7" + "4. ᴍɪᴇᴊꜱᴄᴇ: x punktów"));


        lore.add(ColorFixer.addColors("&7" + "&aᴢᴀᴅᴀɴɪᴀ ᴋʀʏᴊóᴡᴋɪ&7 ᴛᴏ &8ᴄᴏᴛʏɢᴏᴅɴɪᴏᴡᴇ ᴍɪꜱᴊᴇ,"));
        lore.add(ColorFixer.addColors("&7" + "ᴅᴏ ᴋᴛóʀʏᴄʜ ᴘʀᴢʏꜱᴛęᴘᴜᴊą ᴘᴏꜱɪᴀᴅᴀᴄᴢᴇ."));
        lore.add(ColorFixer.addColors("&7" + "ᴡ ᴘʀᴏᴄᴇꜱɪᴇ ᴍɪꜱᴊɪ, ɢɪʟᴅɪᴇ ꜱᴀ ᴡʏʟɪᴄᴢᴀɴᴇ &8ɴᴀ ᴘᴏᴅꜱᴛᴀᴡɪᴇ"));
        lore.add(ColorFixer.addColors("&7" + "ᴘʀᴏɢʀᴇꜱᴜ ꜱᴘʀᴀᴡᴅᴢᴀɴᴇɢᴏ ᴡ ɴᴀᴊᴡʏżꜱᴢʏᴍ ᴘᴜɴᴋᴄɪᴇ."));
        lore.add(ColorFixer.addColors("&7" + " "));
        lore.add(ColorFixer.addColors("&7" + "ᴘʀᴏɢʀᴇꜱꜱᴏᴡᴀɴɪᴇ:"));
        lore.add(ColorFixer.addColors("&7" + MessageManager.getRomanNumerals(1) + " ᴍɪᴇᴊꜱᴄᴇ&8: &7x punktów"));
        lore.add(ColorFixer.addColors("&7" + MessageManager.getRomanNumerals(2) + " ᴍɪᴇᴊꜱᴄᴇ&8: &7x punktów"));
        lore.add(ColorFixer.addColors("&7" + MessageManager.getRomanNumerals(3) + " ᴍɪᴇᴊꜱᴄᴇ&8: &7x punktów"));
        lore.add(ColorFixer.addColors("&7" + MessageManager.getRomanNumerals(4) + " ᴍɪᴇᴊꜱᴄᴇ&8: &7x punktów"));

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        questsInfo = item;
    }

    private static void createWarInfo() {
        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("#a18b3dɪɴꜰᴏʀᴍᴀᴄᴊᴇ"));

        List<String> lore = new ArrayList<>();

        lore.add(ColorFixer.addColors("&7" + "&cᴡᴏᴊɴʏ ɢɪʟᴅɪɪ&7 ᴛᴏ ꜱᴛᴀʀᴄɪᴀ ᴍɪęᴅᴢʏ ɢɪʟᴅɪᴀᴍɪ,"));
        lore.add(ColorFixer.addColors("&7" + "ᴋᴛóʀᴇ ᴘᴏᴢᴡᴀʟᴀᴊą ᴢᴅᴏʙʏᴡᴀć ᴘᴜɴᴋᴛʏ &bxᴘ &7ᴅʟᴀ ᴋʀʏᴊóᴡᴋɪ"));
        lore.add(ColorFixer.addColors("&7" + " "));
        lore.add(ColorFixer.addColors("&7" + "ᴘᴏᴅᴄᴢᴀꜱ ᴡᴏᴊɴʏ, ᴏʙɪᴇ ɢɪʟᴅɪᴇ ᴢᴅᴏʙʏᴡᴀᴊą ᴘᴜɴᴋᴛʏ ᴢᴀ "));
        lore.add(ColorFixer.addColors("&7" + "ʀóżɴᴇ ᴀᴋᴛʏᴡɴᴏśᴄɪ, ᴛᴀᴋɪᴇ ᴊᴀᴋ:"));
        lore.add(ColorFixer.addColors("&7" + "&8 - &7ᴇʟɪᴍɪɴᴏᴡᴀɴɪᴇ ᴄᴢłᴏɴᴋóᴡ ᴘʀᴢᴇᴄɪᴡɴᴇᴊ ɢɪʟᴅɪɪ"));
        lore.add(ColorFixer.addColors("&7" + "&8 - &7ᴘʀᴢᴇᴊęᴄɪᴇ ᴋᴏɴᴛʀᴏʟᴏᴡᴀɴʏᴄʜ ᴘᴜɴᴋᴛóᴡ ɴᴀ ᴍᴀᴘɪᴇ"));
        lore.add(ColorFixer.addColors("&7" + "&8 - &7ᴏʙʀᴏɴᴀ ᴋʀʏᴊóᴡᴋɪ ᴘʀᴢᴇᴅ ᴀᴛᴀᴋᴀᴍɪ ᴘʀᴢᴇᴄɪᴡɴɪᴋᴀ"));
        lore.add(ColorFixer.addColors("&7" + " "));
        lore.add(ColorFixer.addColors("&7" + "ᴘᴏ ᴢᴀᴋᴏńᴄᴢᴇɴɪᴜ ᴡᴏᴊɴʏ ᴡʏɴɪᴋɪ ꜱą ᴘᴏᴅꜱᴜᴍᴏᴡʏᴡᴀɴᴇ"));
        lore.add(ColorFixer.addColors("&7" + "ᴏʀᴀᴢ ᴘʀᴢʏᴢɴᴀᴡᴀɴᴇ ꜱą ɴᴀɢʀᴏᴅʏ:"));
        lore.add(ColorFixer.addColors("&7" + "&8 - &7ᴢᴡʏᴄɪęꜱᴋᴀ ɢɪʟᴅɪᴀ ᴏᴛʀᴢʏᴍᴜᴊᴇ &bxᴘ &7ᴏʀᴀᴢ ᴢᴀꜱᴏʙʏ"));
        lore.add(ColorFixer.addColors("&7" + "&8 - &7ᴘʀᴢᴇɢʀᴀɴᴀ ɢɪʟᴅɪᴀ ᴛʀᴀᴄɪ ᴄᴢęść &bxᴘ"));
        lore.add(ColorFixer.addColors("&7" + "&8 - &7ᴡ ᴘʀᴢʏᴘᴀᴅᴋᴜ ʀᴇᴍɪꜱᴜ ᴏʙɪᴇ ɢɪʟᴅɪᴇ ᴛʀᴀᴄą &bxᴘ"));


        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);

        warInfo = item;

    }


    private static void createPlayerListItem() {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);

        itemMeta.setDisplayName(ColorFixer.addColors("&5&lᴄᴢłᴏɴᴋᴏᴡɪᴇ ɢɪʟᴅɪɪ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴇɴᴜ ᴢᴀʀᴢąᴅᴢᴀɴɪᴀ ᴄᴢłᴏɴᴋᴀᴍɪ ɢɪʟᴅɪɪ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        playerListItem = item;

    }

    private static void createShieldItem() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("&7&lᴏʙʀᴏɴᴀ ɢɪʟᴅɪɪ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴇɴᴜ ᴏʙʀᴏɴʏ ɢɪʟᴅɪɪ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        shieldItem = item;
    }

    private static void createHomeItemItem() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("&b&lᴘᴀɴᴇʟ ɢłóᴡɴʏ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴘᴀɴᴇʟ ᴘᴏᴢɪᴏᴍᴜ ɢɪʟᴅɪɪ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        homeItemItem = item;
    }

    private static void createQuestsItem() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("#e4d5b7&lᴢᴀᴅᴀɴɪᴀ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴇɴᴜ ᴢᴀᴅᴀń"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        questsItem = item;
    }

    private static void createWarItem() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("&c&lᴡᴏᴊɴʏ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴇɴᴜ ᴡᴏᴊᴇɴ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        warItem = item;
    }

    private static void createAllianceItem() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("&a&lꜱᴏᴊᴜꜱᴢᴇ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴇɴᴜ ꜱᴏᴊᴜꜱᴢʏ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        allianceItem = item;
    }



    private static void createKickPlayerButton() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("&c&lᴡʏʀᴢᴜć"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴡʏʀᴢᴜᴄɪć ɢʀᴀᴄᴢᴀ ᴢ ɢɪʟᴅɪɪ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        kickPlayerButton = item;
    }

    private static void createPermissionsButton() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("&b&lᴘᴇʀᴍɪꜱᴊᴇ ɢɪʟᴅʏᴊɴᴇ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴏᴛᴡᴏʀᴢʏć ᴍᴇɴᴜ ᴘᴇʀᴍɪꜱᴊɪ ɢʀᴀᴄᴢᴀ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        permissionsButton = item;
    }

    private static void createDeputyButton() {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName(ColorFixer.addColors("&6&lᴜꜱᴛᴀᴡ ᴢᴀꜱᴛęᴘᴄᴇ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ aby ᴍɪᴀɴᴏᴡᴀć ɢʀᴀᴄᴢᴀ ɴᴀ ᴢᴀꜱᴛęᴘᴄę"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        deputyButton = item;
    }

    public static ItemStack createPlayerListHead(OfflinePlayer player, String name, String role) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

        boolean online = player.isOnline();

        itemMeta.setOwningPlayer(player);

        itemMeta.setCustomModelData(1);
        itemMeta.setDisplayName(ColorFixer.addColors(name));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(role);
        lore.add(" ");
        lore.add(ColorFixer.addColors(online ? "&a&lᴏɴʟɪɴᴇ" : "&c&lᴏғғʟɪɴ"));
        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴡʏʙʀᴀć"));

        itemMeta.setLore(lore);
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createPlayerInfoHead(OfflinePlayer player) {

        ItemStack item = new ItemStack(Material.PLAYER_HEAD);

        SkullMeta itemMeta = (SkullMeta) item.getItemMeta();

        itemMeta.setOwningPlayer(player);

        itemMeta.setCustomModelData(2);
        itemMeta.setDisplayName(" ");

        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);

        item.setItemMeta(itemMeta);
        return item;
    }


    public static ItemStack getHideoutLeftArrowItem(boolean isActive, int previousPage) {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);

        itemMeta.setDisplayName((ColorFixer.addColors("&7ᴘᴏᴘʀᴢᴇᴅɴɪᴀ ꜱᴛʀᴏɴᴀ")));

        ArrayList<String> lore = new ArrayList<>();

        if (isActive) {
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴍɪᴇɴɪć ᴀᴋᴛᴜᴀʟɴą ꜱᴛʀᴏɴᴇ ɴᴀ: " + previousPage));
        }
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getHideoutRightArrowItem(boolean isActive, int nextPage) {
        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);
        itemMeta.setDisplayName((ColorFixer.addColors("&7ɴᴀꜱᴛęᴘɴᴀ ꜱᴛʀᴏɴᴀ")));
        ArrayList<String> lore = new ArrayList<>();

        if(isActive) {
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴍɪᴇɴɪć ᴀᴋᴛᴜᴀʟɴą ꜱᴛʀᴏɴᴇ  ɴᴀ: " + nextPage));
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }



    /** HIDEOUT UPGRADE BUTTONS **/

    public static ItemStack areaUpgradeButton(UpgradeInfoHolder upgradeInfoHolder) {

        boolean isBought = upgradeInfoHolder.isBought();
        boolean isFinished = upgradeInfoHolder.isFinished();

        long secondsTillReady;
        try {
            secondsTillReady = upgradeInfoHolder.getTimeToCompletionSeconds();
        } catch (UpgradeWasNotBoughtException e) {
            secondsTillReady = -1;
        }


        ItemStack item;
        ItemMeta itemMeta;

        if(isBought) {
            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();

            switch (upgradeInfoHolder.getUpgrade()) {
                case UpgradeImpl.STORAGE -> itemMeta.setCustomModelData(20101);
                case UpgradeImpl.STATION_HALL -> itemMeta.setCustomModelData(20202);
                case UpgradeImpl.ILLEGAL_BUSINESS_HALL -> itemMeta.setCustomModelData(20203);
                default -> throw new IllegalStateException("Unexpected value: " + upgradeInfoHolder.getUpgrade());
            }

        } else {
            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();

            switch (upgradeInfoHolder.getUpgrade()) {
                case UpgradeImpl.STORAGE -> itemMeta.setCustomModelData(20101);
                case UpgradeImpl.STATION_HALL -> itemMeta.setCustomModelData(20102);
                case UpgradeImpl.ILLEGAL_BUSINESS_HALL -> itemMeta.setCustomModelData(20103);
                default -> throw new IllegalStateException("Unexpected value: " + upgradeInfoHolder.getUpgrade());
            }
        }


        itemMeta.setDisplayName(ColorFixer.addColors("&7&lᴜʟᴇᴘꜱᴢᴇɴɪᴇ &8&l[" + upgradeInfoHolder.getUpgrade().getUpgradeName() + "&8&l]"));


        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");

        if(isBought && isFinished) {
            lore.add(ColorFixer.addColors("&a&lᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴘʀᴢᴇᴊść ᴅᴏ ᴍᴇɴᴜ ᴜʟᴇᴘꜱᴢᴇɴɪᴀ"));
        } else if (isBought) {
            lore.add(ColorFixer.addColors("&7ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀłᴏ ᴋᴜᴘɪᴏɴᴇ ɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏńᴄᴢᴏɴᴇ ᴢᴀ&8: &6" + MessageManager.secondsToTimeFormatSkipSeconds(Math.toIntExact(secondsTillReady))));
        } else {
            lore.add(ColorFixer.addColors("&c&lᴢᴀʙʟᴏᴋᴏᴡᴀɴᴇ"));
            lore.add(ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏᴅᴀᴛᴋᴏᴡą̨ ᴘʀᴢᴇꜱᴛʀᴢᴇń ɴᴀ ʀᴏᴢᴡóᴊ ɢɪʟᴅɪ"));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + UpgradeImpl.STATION_HALL.getPrice() + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors("&7ᴄᴢᴀꜱ ᴛᴡᴏʀᴢᴇɴɪᴀ: &e" + MessageManager.secondsToTimeFormatSkipSeconds(UpgradeImpl.STATION_HALL.getCoolDown())));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴋᴜᴘɪć ᴜʟᴇᴘꜱᴢᴇɴɪᴇ"));
            lore.add(ColorFixer.addColors(" "));
            lore.addAll(upgradeInfoHolder.getUpgrade().getLoreReview());
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }


    public static ItemStack buyAreaUpgradeButton(UpgradeInfoHolder upgradeInfoHolder) {

        boolean isBought = upgradeInfoHolder.isBought();
        boolean isFinished = upgradeInfoHolder.isFinished();
        long secondsTillReady;
        try {
            secondsTillReady = upgradeInfoHolder.getTimeToCompletionSeconds();
        } catch (UpgradeWasNotBoughtException e) {
            secondsTillReady = -1;
        }

        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);

        itemMeta.setDisplayName(ColorFixer.addColors("&a&lᴋᴜᴘ &8&l[" + upgradeInfoHolder.getUpgrade().getUpgradeName() + "&8&l]"));

        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏᴅᴀᴛᴋᴏᴡą̨ ᴘʀᴢᴇꜱᴛʀᴢᴇń ɴᴀ ʀᴏᴢᴡóᴊ ɢɪʟᴅɪ"));

        lore.add(ColorFixer.addColors(" "));

        if (isBought && !isFinished) {
            itemMeta.setDisplayName(ColorFixer.addColors(upgradeInfoHolder.getUpgrade().getUpgradeName()));
            lore.add(ColorFixer.addColors("&eᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌᴏ ᴋᴜᴘɪᴏɴᴇ ɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏɴ́ᴄᴢᴏɴᴇ ᴢᴀ&7: &6" + MessageManager.secondsToTimeFormatSkipSeconds(Math.toIntExact(secondsTillReady))));
        } else {
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + upgradeInfoHolder.getUpgrade().getPrice() + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors("&7ᴄᴢᴀꜱ ᴛᴡᴏʀᴢᴇɴɪᴀ: &e" + MessageManager.secondsToTimeFormatSkipSeconds(upgradeInfoHolder.getUpgrade().getCoolDown())));
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getWorkshopBuyButton(UpgradeInfoHolder upgradeInfoHolder) {

        boolean isBought = upgradeInfoHolder.isBought();
        boolean isFinished = upgradeInfoHolder.isFinished();

        long secondsTillReady;

        try {
            secondsTillReady = upgradeInfoHolder.getTimeToCompletionSeconds();
        } catch (UpgradeWasNotBoughtException e) {
            secondsTillReady = -1;
        }


        ItemStack item;
        ItemMeta itemMeta;

        if(isBought) {
            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();

            itemMeta.setCustomModelData(30100);
        } else {
            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(30100);
        }

        itemMeta.setDisplayName(ColorFixer.addColors(upgradeInfoHolder.getUpgrade().getUpgradeName() + " &8&l[" + (isBought ? (isFinished ? "&a&lᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ" : "&5&lᴡ ᴛʀᴀᴋᴄɪᴇ") : "&c&lᴢᴀʙʟᴏᴋᴏᴡᴀɴᴇ") + "&8&l]"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        if(isBought && isFinished) {
            lore.add(ColorFixer.addColors("&aᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴊᴇꜱᴛ ᴊᴜż ᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
        } else if (isBought) {
            lore.add(ColorFixer.addColors("&7ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌłᴏ ᴋᴜᴘɪᴏɴᴇ ɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏńᴄᴢᴏɴᴇ ᴢᴀ&8: &6" + MessageManager.secondsToTimeFormatSkipSeconds(Math.toIntExact(secondsTillReady))));
        } else {
            lore.addAll(upgradeInfoHolder.getUpgrade().getLoreReview());
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + upgradeInfoHolder.getUpgrade().getPrice() + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors("&7ᴄᴢᴀꜱ ᴛᴡᴏʀᴢᴇɴɪᴀ: &e" + MessageManager.secondsToTimeFormatSkipSeconds(upgradeInfoHolder.getUpgrade().getCoolDown())));
            lore.add(ColorFixer.addColors(" "));
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴋᴜᴘɪć ᴜʟᴇᴘꜱᴢᴇɴɪᴇ"));
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;

    }

    public static ItemStack soonButton() {

        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);

        itemMeta.setDisplayName(ColorFixer.addColors("&c&lᴀᴋᴛᴜᴀʟɴɪᴇ ɴɪᴇᴅᴏꜱᴛęᴘɴᴇ"));

        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getMaxPlayersItem(int hideoutLevel) {

        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setCustomModelData(31000 + hideoutLevel);

        itemMeta.setDisplayName(ColorFixer.addColors("&3&lᴍᴀᴋꜱʏᴍᴀʟɴᴀ ɪʟᴏść ᴄᴢłᴏɴᴋóᴡ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴀᴋᴛᴜᴀʟɴᴀ ᴍᴀᴋꜱʏᴍᴀʟɴᴀ ɪʟᴏść ᴄᴢłᴏɴᴋóᴡ ᴡ ɢɪʟᴅɪɪ&8: &6" + (8 + 4 * hideoutLevel)));
        lore.add(" ");
        if(hideoutLevel < 5) {
            lore.add(ColorFixer.addColors("&7ᴘᴏᴅɴɪᴇś ᴘᴏᴢɪᴏᴍ ᴋʀʏᴊóᴡᴋɪ ᴀʙʏ ᴢᴡɪęᴋꜱᴢʏᴄ ᴍᴀᴋꜱʏᴍᴀʟɴą"));
            lore.add(ColorFixer.addColors("&7ɪʟᴏść ɢʀᴀᴄᴢʏ ᴅᴏ &6" + (8 + 4 * (hideoutLevel + 1))));
        } else {
            lore.add(ColorFixer.addColors("&7ᴏꜱɪąɢɴɪęᴛᴏ ᴍᴀᴋꜱʏᴍᴀʟɴᴀ ɪʟᴏść ᴄᴢłᴏɴᴋóᴡ ᴡ ɢɪʟᴅɪɪ"));
        }

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getMaxLivesItem(int hideoutLevel) {

        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setCustomModelData(32000 + hideoutLevel);

        itemMeta.setDisplayName(ColorFixer.addColors("&c&lᴍᴀᴋꜱʏᴍᴀʟɴᴀ ɪʟᴏść żʏć ᴋʀʏᴊóᴡᴋɪ"));

        ArrayList<String> lore = new ArrayList<>();

        lore.add(ColorFixer.addColors("&7ᴀᴋᴛᴜᴀʟɴᴀ ᴍᴀᴋꜱʏᴍᴀʟɴᴀ ɪʟᴏść żʏć ɢɪʟᴅɪɪ&8: &6" + hideoutLevel));
        lore.add(" ");

        if(hideoutLevel < 5) {
            lore.add(ColorFixer.addColors("&7ᴘᴏᴅɴɪᴇś ᴘᴏᴢɪᴏᴍ ᴋʀʏᴊóᴡᴋɪ ᴀʙʏ ᴢᴡɪęᴋꜱᴢʏᴄ"));
            lore.add(ColorFixer.addColors("&7ᴍᴀᴋꜱʏᴍᴀʟɴą ɪʟᴏść żʏć ᴅᴏ &4" + (hideoutLevel + 1)));

        } else {
            lore.add(ColorFixer.addColors("&7ᴏꜱɪąɢɴɪęᴛᴏ ᴍᴀᴋꜱʏᴍᴀʟɴᴀ ɪʟᴏść żʏć ɢɪʟᴅɪɪ"));
        }

        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        return item;
    }


    public static ItemStack getMaxShieldItem(int hideoutLevel) {

        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setCustomModelData(33000 + hideoutLevel);

        itemMeta.setDisplayName(ColorFixer.addColors("&b&lᴍᴀᴋꜱʏᴍᴀʟɴᴀ ᴅłᴜɢᴏść ᴀᴋᴛʏᴡɴᴇᴊ ᴛᴀʀᴄᴢʏ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorFixer.addColors("&7ᴀᴋᴛᴜᴀʟɴᴀ ᴍᴀᴋꜱʏᴍᴀʟɴᴀ ᴅłᴜɢᴏść ᴛʀᴡᴀɴɪᴀ ᴛᴀʀᴄᴢʏ&8: &b" + hideoutLevel + " ᴅɴɪ"));
        lore.add(" ");
        if(hideoutLevel < 5) {
            lore.add(ColorFixer.addColors("&7ᴘᴏᴅɴɪᴇś ᴘᴏᴢɪᴏᴍ ᴋʀʏᴊóᴡᴋɪ ᴀʙʏ ᴢᴡɪęᴋꜱᴢʏᴄ ᴍᴀᴋꜱʏᴍᴀʟɴą"));
            lore.add(ColorFixer.addColors("&7ᴅłᴜɢᴏść ᴛʀᴡᴀɴɪᴀ ᴛᴀʀᴄᴢʏ ᴅᴏ &4" + (hideoutLevel + 1) + " ᴅɴɪ"));
        } else {
            lore.add(ColorFixer.addColors("&7ᴏꜱɪąɢɴɪęᴛᴏ ᴍᴀᴋꜱʏᴍᴀʟɴą ᴅłᴜɢᴏść ᴛʀᴡᴀɴɪᴀ ᴛᴀʀᴄᴢʏ"));
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }


    public static ItemStack getOtherItem(int hideoutLevel) {

        ItemStack item = new ItemStack(Material.NAME_TAG);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setCustomModelData(34001);

        itemMeta.setDisplayName(ColorFixer.addColors("&a&lɪɴɴᴇ ᴍᴏᴅʏꜰɪᴋᴀᴛᴏʀʏ"));

//        ArrayList<String> lore = new ArrayList<>();
//        lore.add(ColorFixer.addColors("&7ᴀᴋᴛᴜᴀʟɴᴀ ᴍᴀᴋꜱʏᴍᴀʟɴᴀ ᴅłᴜɢᴏść ᴛʀᴡᴀɴɪᴀ ᴛᴀʀᴄᴢʏ&8: &b" + hideoutLevel + " ᴅɴɪ"));
//        if(hideoutLevel < 5) {
//            lore.add(ColorFixer.addColors("&7ᴘᴏᴅɴɪᴇś ᴘᴏᴢɪᴏᴍ ᴋʀʏᴊóᴡᴋɪ ᴀʙʏ ᴢᴡɪęᴋꜱᴢʏᴄ ᴍᴀᴋꜱʏᴍᴀʟɴą ᴅłᴜɢᴏść ᴛʀᴡᴀɴɪᴀ ᴛᴀʀᴄᴢʏ ᴅᴏ &4" + (hideoutLevel + 1) + " ᴅɴɪ"));
//        } else {
//            lore.add(ColorFixer.addColors("&7ᴏꜱɪąɢɴɪęᴛᴏ ᴍᴀᴋꜱʏᴍᴀʟɴą ᴅłᴜɢᴏść ᴛʀᴡᴀɴɪᴀ ᴛᴀʀᴄᴢʏ"));
//        }

        item.setItemMeta(itemMeta);
        return item;
    }


    public static ItemStack getHideoutTierItem(int hideoutLevel) {
        ItemStack item = new ItemStack(Material.GHAST_TEAR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName((ColorFixer.addColors("&7ᴛɪᴇʀ " + MessageManager.getRomanNumerals(hideoutLevel))));
        ArrayList<String> lore = new ArrayList<>();

        lore.add(ColorFixer.addColors("&7ᴀᴋᴛᴜᴀʟɴʏ ᴘᴏᴢɪᴏᴍ ᴋʀʏᴊóᴡᴋɪ&8: &6" + hideoutLevel));
        itemMeta.setLore(lore);
        itemMeta.setCustomModelData(10000 + hideoutLevel);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getUpgradeItem(boolean canUpgrade, int slot, int price, int hideoutExperience, int expLowerCap, int expUpperCap, int hideoutLevel, Optional<Integer> integer, boolean isMaxLevel) {

        ItemStack item;
        ItemMeta itemMeta;

        if(canUpgrade && !isMaxLevel && slot == 2) {

            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(20000);

        } else {
            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(20001);
        }


        itemMeta.setDisplayName((ColorFixer.addColors("&6&lᴜʟᴇᴘꜱᴢ")));

        ArrayList<String> lore = new ArrayList<>();
        if(canUpgrade) {
            lore.add(ColorFixer.addColors("&bxᴘ&8: &b" + hideoutExperience + " &8/ &b" + expUpperCap));
        } else {
            lore.add(ColorFixer.addColors("&bxᴘ&8: &7" + hideoutExperience + " &8/ &b" + expUpperCap));
        }
        lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ&8: &6" + price + " ᴍᴏɴᴇᴛ"));

        if(isMaxLevel) {
            lore.add(" ");
            lore.add(ColorFixer.addColors("&7&2ᴏꜱɪąɢɴɪęᴛᴏ ᴍᴀᴋꜱʏᴍᴀʟɴʏ ᴘᴏᴢɪᴏᴍ ᴋʀʏᴊóᴡᴋɪ"));
        } else if (canUpgrade) {
            lore.add(" ");
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴜʟᴇᴘꜱᴢʏć ɴᴀ ᴘᴏᴢɪᴏᴍ &6" + (hideoutLevel + 1)));
        }

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getExpPercentItem(int percent, int hideoutExperience, int lossLevelCap, int upperLevelCap) {

        ItemStack item = new ItemStack(Material.MAP);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(1010);

        itemMeta.setDisplayName(ColorFixer.addColors("&b&lxᴘ &8&l[&7" + percent + "%]"));

        ArrayList<String> lore = new ArrayList<>();

        if(percent>=100) {
            lore.add(ColorFixer.addColors("&bxᴘ&8: &b" + hideoutExperience + " &8/ &b" + upperLevelCap));
        } else {
            lore.add(ColorFixer.addColors("&bxᴘ&8: &7" + hideoutExperience + " &8/ &b" + upperLevelCap));
        }
        if(lossLevelCap > 0) {
            lore.add(ColorFixer.addColors("&c&lɢᴅʏ ᴅᴏśᴡɪᴀᴅᴄᴢᴇɴɪᴇ ᴋʀʏᴊóᴡᴋɪ ꜱᴘᴀᴅɴɪᴇ ᴘᴏɴɪżᴇᴊ &4" + lossLevelCap));
            lore.add(ColorFixer.addColors("&c&lᴘᴏᴢɪᴏᴍ ᴋʀʏᴊóᴡᴋɪ ᴢᴏꜱᴛᴀɴɪᴇ ᴏʙɴɪżᴏɴʏ"));
        }

        item.setItemMeta(itemMeta);
        return item;
    }


    public static ItemStack getStorageBuyButton(UpgradeInfoHolder upgradeInfoHolder, int storageNumber) {

        ItemStack item;
        ItemMeta itemMeta;

        if(upgradeInfoHolder.isFinished()) {
            item = new ItemStack(Material.MAP);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(1010);
        } else {
            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(666666);
        }

        itemMeta.setDisplayName(ColorFixer.addColors("&8&l[&aᴍᴀɢᴀᴢʏɴ &6#" + storageNumber + "&8&l]"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        if(upgradeInfoHolder.isBought()) {
            lore.add(ColorFixer.addColors("&a&lᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
        } else {
            lore.add(ColorFixer.addColors("&c&lɴɪᴇ ᴏᴅʙʟᴏᴋᴏᴡᴀɴᴇ"));
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + upgradeInfoHolder.getUpgrade().getPrice() + " ᴍᴏɴᴇᴛ"));
            lore.add(" ");
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴋᴜᴘɪć ᴍᴀɢᴀᴢʏɴ"));
        }
        itemMeta.setLore(lore);

        item.setItemMeta(itemMeta);
        return item;
    }
}



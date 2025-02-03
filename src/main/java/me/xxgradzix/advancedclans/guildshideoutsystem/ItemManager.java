package me.xxgradzix.advancedclans.guildshideoutsystem;

import me.xxgradzix.advancedclans.data.database.entities.hideout.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionDto;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionVariant;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.storage.StoragePermissionSlotType;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ItemManager {

    public static ItemStack foodRationTier3;
    public static ItemStack foodRationTier2;
    public static ItemStack foodRationTier1;

    public static ItemStack toolTier1;
    public static ItemStack toolTier2;
    public static ItemStack toolTier3;

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
        createFoodRation1();
        createFoodRation2();
        createFoodRation3();

        createToolPack1();
        createToolPack2();
        createToolPack3();

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

    // TODO mistrz wojny zamiast wojen w panelu

    /** VENTURE BOOST ITEMS **/

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

    public static ItemStack getBlacksmithUpgradeButton(int price, boolean isUnlocked, boolean isFinished, long secondsTillReady) {

        ItemStack item = new ItemStack(Material.ANVIL);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴋᴜźɴɪᴀ ɢɪʟᴅʏᴊɴᴀ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        if(isUnlocked && isFinished) {
            itemMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
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
            itemMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
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
            itemMeta.addEnchant(Enchantment.LUCK_OF_THE_SEA, 1, true);
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


    public static @NotNull ItemStack getUpgradeButton(GuildHideout.Upgrade upgrade, int price, boolean bought, boolean finished, Long timeTOCompletionSeconds) {
        return switch (upgrade) {
//            case STATION_HALL -> getMainHallUpgradeButton(price, bought, finished, timeTOCompletionSeconds);
            case BLACKSMITH -> getBlacksmithUpgradeButton(price, bought, finished, timeTOCompletionSeconds);
            case SORCERER -> getSorcererUpgradeButton(price, bought, finished, Math.toIntExact(timeTOCompletionSeconds));
            case VENTURE -> getVentureUpgradeButton(price, bought, finished, Math.toIntExact(timeTOCompletionSeconds));
            default -> throw new IllegalStateException("Unexpected value: " + upgrade);
        };
    }

    public static @NotNull ItemStack getStartExpeditionItem(double chance, int expeditionLevel, ExpeditionDto.ExpeditionObjective objective, long completionTime) {

        ItemStack item = new ItemStack(Material.MAP);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setCustomModelData(1010);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴇᴋꜱᴘᴇᴅʏᴄᴊᴇ"));

        ArrayList<String> lore = new ArrayList<>();

        lore.add(" ");

        switch (objective) {
            case STONE -> lore.add(ColorFixer.addColors("&7ᴄᴇʟ: &fᴋᴀᴍɪᴇń"));
            case WOOD -> lore.add(ColorFixer.addColors("&7ᴄᴇʟ: &fᴅʀᴇᴡɴᴏ"));
            case CRYSTALS -> lore.add(ColorFixer.addColors("&7ᴄᴇʟ: &fᴋʀʏꜱᴢᴛᴀŁʏ"));
        }
        switch (expeditionLevel) {
            case 1 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &fłᴀᴛᴡᴀ"));
            case 2 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &fᴛʀᴜᴅɴᴀ"));
            case 3 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &fʙᴀʀᴅᴢᴏ ᴛʀᴜᴅɴᴀ"));
        }
        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ꜱᴢᴀɴꜱᴀ ɴᴀ ᴘᴏᴡᴏᴅᴢᴇɴɪᴇ: &f" + chance * 100 + "%"));
        lore.add(" ");
        lore.add("ᴄᴢᴀꜱ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: " + MessageManager.secondsToTimeFormat(Math.toIntExact(completionTime)));
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
            itemMeta.setCustomModelData(1003);
            itemMeta.setDisplayName(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &4ᴛʀᴜᴅɴʏ &8&l[&4" + (int)(chance * 100) + "%&8&l]"));
        } else if (chance < 0.55) {
            itemMeta.setCustomModelData(1002);
            itemMeta.setDisplayName(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &eśʀᴇᴅɴɪ &8&l[&e" + (int)(chance * 100) + "%&8&l]"));
        } else {
            itemMeta.setCustomModelData(1001);
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
                        customModelData = 1001;
                    }
                    case 2 -> {
                        customModelData = 1002;
                    }
                    case 3 -> {
                        customModelData = 1003;
                    }
                }
            }
            case 1 -> {
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
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
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

    public static ItemStack createFoodBonusSuppliedItem(int tier) {
        ItemStack item = new ItemStack(Material.LIME_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setDisplayName(ColorFixer.addColors("&7&lᴅᴏᴅᴀᴛᴋᴏᴡᴇ ᴢᴀᴘᴀꜱʏ"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        switch (tier) {
            case 3 -> {
                lore.add(ColorFixer.addColors("&aᴢᴀᴘᴀꜱʏ ᴅᴏᴅᴀɴᴇ"));
                lore.add(ColorFixer.addColors("&7ᴀᴋᴛʏᴡɴʏ ʙᴏɴᴜꜱ&8: &a-90&7 ᴍɪɴ"));
                itemMeta.setCustomModelData(1);
            }
            case 2 -> {
                lore.add(ColorFixer.addColors("&aᴢᴀᴘᴀꜱʏ ᴅᴏᴅᴀɴᴇ"));
                lore.add(ColorFixer.addColors("&7ᴀᴋᴛʏᴡɴʏ ʙᴏɴᴜꜱ&8: &a-60&7 ᴍɪɴ"));
                itemMeta.setCustomModelData(1);
            }
            case 1 -> {
                lore.add(ColorFixer.addColors("&aᴢᴀᴘᴀꜱʏ ᴅᴏᴅᴀɴᴇ"));
                lore.add(ColorFixer.addColors("&7ᴀᴋᴛʏᴡɴʏ ʙᴏɴᴜꜱ&8: &a-30&7 ᴍɪɴ"));
                itemMeta.setCustomModelData(1);
            }
            default -> {
                lore.add(ColorFixer.addColors("&cᴢᴀᴘᴀꜱʏ ɴɪᴇ ᴅᴏᴅᴀɴᴇ"));
                itemMeta.setCustomModelData(2);
            }
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack createToolBonusSuppliedItem(int tier) {
        ItemStack item = new ItemStack(Material.LIME_DYE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        itemMeta.setDisplayName(ColorFixer.addColors("&7&lᴅᴏᴅᴀᴛᴋᴏᴡᴇ ᴢᴀᴘᴀꜱʏ"));
        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        switch (tier) {
            case 3 -> {
                lore.add(ColorFixer.addColors("&aᴢᴀᴘᴀꜱʏ ᴅᴏᴅᴀɴᴇ"));
                lore.add(ColorFixer.addColors("&7ᴀᴋᴛʏᴡɴʏ ʙᴏɴᴜꜱ&8: &a+30%&7 ꜱᴢᴀɴꜱ"));
                itemMeta.setCustomModelData(1);
            }
            case 2 -> {
                lore.add(ColorFixer.addColors("&aᴢᴀᴘᴀꜱʏ ᴅᴏᴅᴀɴᴇ"));
                lore.add(ColorFixer.addColors("&7ᴀᴋᴛʏᴡɴʏ ʙᴏɴᴜꜱ&8: &a+20%&7 ꜱᴢᴀɴꜱ"));
                itemMeta.setCustomModelData(1);
            }
            case 1 -> {
                lore.add(ColorFixer.addColors("&aᴢᴀᴘᴀꜱʏ ᴅᴏᴅᴀɴᴇ"));
                lore.add(ColorFixer.addColors("&7ᴀᴋᴛʏᴡɴʏ ʙᴏɴᴜꜱ&8: &a+10%&7 ꜱᴢᴀɴꜱ"));
                itemMeta.setCustomModelData(1);
            }
            default -> {
                lore.add(ColorFixer.addColors("&cᴢᴀᴘᴀꜱʏ ɴɪᴇ ᴅᴏᴅᴀɴᴇ"));
                itemMeta.setCustomModelData(2);
            }
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static @NotNull ItemStack getExpeditionStorageItem(int num, boolean isOwner, boolean hasAccess) {
        ItemStack item = new ItemStack(Material.CHEST);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        itemMeta.addItemFlags(ItemFlag.HIDE_DYE);
        itemMeta.addItemFlags(ItemFlag.HIDE_ADDITIONAL_TOOLTIP);
        itemMeta.addItemFlags(ItemFlag.HIDE_DESTROYS);
        itemMeta.addItemFlags(ItemFlag.HIDE_PLACED_ON);
        itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        String romeNum = MessageManager.getRomanNumerals(num);
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
        ItemStack item = new ItemStack(Material.NAME_TAG);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7&lᴍᴀɢᴀᴢʏɴ ᴘᴇʀꜱᴏɴᴀʟɴʏ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");
        lore.add(ColorFixer.addColors("ᴘʀᴢᴇᴅᴍɪᴏᴛʏ ᴡ ᴛʏᴍ ᴍᴀɢᴀᴢʏɴɪᴇ ʙęᴅą ᴅᴏꜱᴛęᴘɴᴇ ᴛʏʟᴋᴏ ᴅʟᴀ ᴄɪᴇʙɪᴇ"));
        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴏᴛᴡᴏʀᴢʏć ᴍᴀɢᴀᴢʏɴ"));

        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

    public static @NotNull ItemStack getCurrentExpeditionItem(ExpeditionDto.ExpeditionObjective objective, boolean isFinished, int expeditionLevel, int secondsToComplete) {

        ItemStack item = new ItemStack(Material.CHEST);

        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName(ColorFixer.addColors("&7ᴀᴋᴛᴜᴀʟɴᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊᴀ"));

        ArrayList<String> lore = new ArrayList<>();
        lore.add(" ");

        if(isFinished) {
            lore.add(ColorFixer.addColors("&aᴇᴋꜱᴘᴇᴅʏᴄᴊᴀ ᴊᴇꜱᴛ ɢᴏᴛᴏᴡᴀ ᴅᴏ ᴏᴅᴇʙʀᴀɴɪᴀ"));
        } else {
            lore.add(ColorFixer.addColors("&7ᴇᴋꜱᴘᴇᴅʏᴄᴊᴀ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏńᴄᴢᴏɴᴀ ᴢᴀ&7: &6" + MessageManager.secondsToTimeFormatSkipSeconds(secondsToComplete)));
        }

        lore.add(" ");
        lore.add(ColorFixer.addColors("&7ᴄᴇʟ: &f" + objective.toString().toLowerCase()));
        switch (expeditionLevel) {
            case 1 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &aŁᴀᴛᴡʏ"));
            case 2 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &eśʀᴇᴅɴɪ"));
            case 3 -> lore.add(ColorFixer.addColors("&7ᴘᴏᴢɪᴏᴍ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ: &4ᴛʀᴜᴅɴʏ"));
        }

        if(!isFinished) {
            lore.add(" ");
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴘᴘᴍ ᴀʙʏ ᴘᴏᴍɪɴąć ᴄᴢᴀꜱ ᴏᴄᴢᴇᴋɪᴡᴀɴɪᴀ" ));

            int price = secondsToComplete /(60 * 30);
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ: &6" + price + " ᴍᴏɴᴇᴛ ᴘʀᴇᴍɪᴜᴍ"));
        }
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

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

    public static ItemStack getSmallLeftArrowItem(boolean isActive, int previousPage) {

        ItemStack item = new ItemStack(Material.GHAST_TEAR);
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName((ColorFixer.addColors("&7ᴘᴏᴘʀᴢᴇᴅɴɪᴀ ꜱᴛʀᴏɴᴀ")));

        ArrayList<String> lore = new ArrayList<>();

        if (isActive) {
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴍɪᴇɴɪć ᴀᴋᴛᴜᴀʟɴą ꜱᴛʀᴏɴᴇ ɴᴀ: " + previousPage));
        }
        itemMeta.setLore(lore);

        itemMeta.setCustomModelData(99000 + (isActive ? 2 : 1));
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack getSmallRightArrowItem(boolean isActive, int nextPage) {
        ItemStack item = new ItemStack(Material.GHAST_TEAR);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName((ColorFixer.addColors("&7ɴᴀꜱᴛęᴘɴᴀ ꜱᴛʀᴏɴᴀ")));
        ArrayList<String> lore = new ArrayList<>();

        if(isActive) {
            lore.add(ColorFixer.addColors("&7ᴋʟɪᴋɴɪᴊ ᴀʙʏ ᴢᴍɪᴇɴɪć ᴀᴋᴛᴜᴀʟɴą ꜱᴛʀᴏɴᴇ  ɴᴀ: " + nextPage));
        }

        itemMeta.setLore(lore);

        itemMeta.setCustomModelData(66000 + (isActive ? 2 : 1));
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
                case GuildHideout.Upgrade.STORAGE -> itemMeta.setCustomModelData(20101);
                case GuildHideout.Upgrade.STATION_HALL -> itemMeta.setCustomModelData(20202);
                case GuildHideout.Upgrade.ILLEGAL_BUSINESS_HALL -> itemMeta.setCustomModelData(20203);
            }

        } else {
            item = new ItemStack(Material.NAME_TAG);
            itemMeta = item.getItemMeta();

            switch (upgradeInfoHolder.getUpgrade()) {
                case GuildHideout.Upgrade.STORAGE -> itemMeta.setCustomModelData(20101);
                case GuildHideout.Upgrade.STATION_HALL -> itemMeta.setCustomModelData(20102);
                case GuildHideout.Upgrade.ILLEGAL_BUSINESS_HALL -> itemMeta.setCustomModelData(20103);
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
            lore.add(ColorFixer.addColors("&7ᴄᴇɴᴀ ᴏᴅʙʟᴏᴋᴏᴡᴀɴɪᴀ: &e" + GuildHideout.Upgrade.STATION_HALL.getPrice() + " ᴍᴏɴᴇᴛ"));
            lore.add(ColorFixer.addColors("&7ᴄᴢᴀꜱ ᴛᴡᴏʀᴢᴇɴɪᴀ: &e" + MessageManager.secondsToTimeFormatSkipSeconds(GuildHideout.Upgrade.STATION_HALL.getCoolDown())));
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
//            switch (upgradeInfoHolder.getUpgrade()) {
//                case GuildHideout.Upgrade.BLACKSMITH -> itemMeta.setCustomModelData(1501);
//                case GuildHideout.Upgrade.SORCERER -> itemMeta.setCustomModelData(1502);
//                case GuildHideout.Upgrade.VENTURE -> itemMeta.setCustomModelData(1503);
//            }
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
            item = new ItemStack(Material.MAP);
            itemMeta = item.getItemMeta();
            itemMeta.setCustomModelData(1010);
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


    public static @NotNull ItemStack getRequiredPremiumCoins(int secondsToComplete) {

        ItemStack item = new ItemStack(Material.GLASS_PANE);

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

        itemMeta.setCustomModelData(3020000 + price);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        return item;
    }

}



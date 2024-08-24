package me.xxgradzix.advancedclans.config;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import org.checkerframework.checker.units.qual.C;

public class Config extends OkaeriConfig {

    @Comment("Config")
    @Comment("Max players in guild")
    public static int maxPlayers = 15;
    @Comment("Online player color")
    public static String onlinePlayerColor = "§a";
    @Comment("Offline player color")
    public static String offlinePlayerColor = "§c";
    @Comment("No clan placeholder")
    public static String noClan = "§cʙʀᴀᴋ ᴋʟᴀɴᴜ";
    @Comment("Min players for ranking")
    public static int minPlayersForRanking = 3;
    @Comment("Clan minimum to ranking message")
    public static String noEnoughPlayerForRankingInClan = "§7ᴘᴏᴛʀᴢᴇʙᴀ §cᴍɪɴ " + minPlayersForRanking + "§7 ᴏꜱ.";
    @Comment("Player does not exist")
    public static String nonePlayer = "§7ɢʀᴀᴄᴢ ɴɪᴇ ɪꜱᴛɴɪᴇᴊᴇ";
    @Comment("should payment be enabled")
    public static boolean paymentEnabled = false;
    @Comment("Alliance limit")
    public static int allianceLimit = 5;
    @Comment("Default points")
    public static int defaultPoints = 1000;
    @Comment("Should alliance pvp be enabled globally")
    public static boolean pvpAllianceEnabledGlobally = false;
    @Comment("Should death message be sent")
    public static boolean shouldSendDeathMessage = true;
    @Comment("Should system of anti abuse be enabled")
    public static boolean antiAbuseSystemEnabled = true;
    @Comment("Anti system rank cooldown")
    public static int antiSystemRankCooldown = 60 * 60 * 20 * 30;
    @Comment("Has not clan")
    public static String hasNotClan = "§aɢʀᴀᴄᴢ ɴɪᴇ ᴍᴀ ᴋʟᴀɴᴜ";
    @Comment("Has clan")
    public static String hasClan = "§aɢʀᴀᴄᴢ ɴɪᴇ ᴍᴀ ᴋʟᴀɴᴜ";
    @Comment("Formated clan tag")
    public static String formatTag = "§7{tag}";
    @Comment("Formated user points")
    public static String formatUserPoints = "§7{points}";
    @Comment("Formated clan points")
    public static String formatClanPoints = "§7{points}";
    @Comment("tag, when players are in same clan")
    public static String formatMember = "§a{tag}";
    @Comment("tag, when players are in clan alliance")
    public static String formatAlliance =  "§b{tag}";
    @Comment("tag, when normal")
    public static String formatNormal =  "§c{tag}";
}
package me.xxgradzix.advancedclans.messages;

import eu.okaeri.configs.OkaeriConfig;
import eu.okaeri.configs.annotation.Comment;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.NotNull;

import static me.xxgradzix.advancedclans.messages.MessageType.*;

public class MessageManager extends OkaeriConfig {

    @Comment("Messages")
    @Comment("Only for players message")
    public static String ONLY_FOR_PLAYERS = "&7ᴛʏʟᴋᴏ ɢʀᴀᴄᴢ ᴍᴏᴢᴇ ᴜᴢʏᴄ ᴛᴇᴊ ᴋᴏᴍᴇɴᴅʏ";
    @Comment("No permission message")
    public static String NO_PERMISSION = "&7ɴɪᴇ ᴍᴀsᴢ ᴜᴘʀᴀᴡɴɪᴇɴ ᴅᴏ ᴜᴢʏᴄɪᴀ ᴛᴇᴊ ᴋᴏᴍᴇɴᴅʏ";
    @Comment("Player not found message")
    public static String PLAYER_NOT_FOUND = "&7ɢʀᴀᴄᴢ ɴɪᴇ ᴢᴏsᴛᴀʟ ᴢɴᴀʟᴇᴢɪᴏɴʏ";

    @Comment("Player not online")
    public static String PLAYER_NOT_ONLINE = "&7ɢʀᴀᴄᴢ ɴɪᴇ ᴊᴇsᴛ ᴏɴʟɪɴᴇ";
    @Comment("Clan not found message, argument: #a18b3d{clan}&7")
    public static String CLAN_NOT_FOUND = "&7ᴋʟᴀɴ #a18b3d{clan}&7 ɴɪᴇ ᴢᴏsᴛᴀʟ ᴢɴᴀʟᴇᴢɪᴏɴʏ";

    @Comment("Clan already exists message, argument: #a18b3d{clan}&7")
    public static String CLAN_ALREADY_EXISTS = "&7ᴋʟᴀɴ #a18b3d{clan}&7 ᴊᴜᴢ ɪsᴛɴɪᴇᴊᴇ";

    @Comment("Clan created message, argument: #a18b3d{clan}&7")
    public static String CLAN_CREATED = "&7ᴋʟᴀɴ #a18b3d{clan}&7 ᴢᴏsᴛᴀʟ sᴛᴡᴏʀᴢᴏɴʏ";

    @Comment("Clan deleted message, argument: #a18b3d{clan}&7")
    public static String CLAN_DELETED = "&7ᴋʟᴀɴ #a18b3d{clan}&7 ᴢᴏsᴛᴀʟ ᴜsᴜɴɪᴇᴛʏ";

    @Comment("Clan joined message, argument: #a18b3d{clan}&7")
    public static String CLAN_JOINED = "&7ᴅᴏʟᴀᴄᴢʏʟᴇs ᴅᴏ ᴋʟᴀɴᴜ #a18b3d{clan}&7";

    @Comment("Clan left message, argument: #a18b3d{clan}&7")
    public static String CLAN_LEFT = "&7ᴏᴅsᴢᴇᴅʟᴇs ᴢ ᴋʟᴀɴᴜ #a18b3d{clan}&7";

    @Comment("Clan leader set message, argument: #a18b3d{clan}&7")
    public static String CLAN_LEADER_SET = "&7ɴᴏᴡʏᴍ ʟɪᴅᴇʀᴇᴍ ᴋʟᴀɴᴜ #a18b3d{clan}&7 ᴢᴏsᴛᴀʟ #a18b3d{player}&7";

    @Comment("Clan deputy set message, argument: #a18b3d{clan}&7")
    public static String CLAN_DEPUTY_SET = "&7ɴᴏᴡʏᴍ ᴢᴀsᴛᴇᴘᴄᴀ ʟɪᴅᴇʀᴀ ᴋʟᴀɴᴜ #a18b3d{clan}&7 ᴢᴏsᴛᴀʟ #a18b3d{player}&7";

    @Comment("Clan invite message sender, argument: {player}")
    public static String CLAN_INVITED_SENDER = "&7ᴢᴀᴘʀᴏꜱɪᴌᴇś ɢʀᴀᴄᴢᴀ #a18b3d{player}&7 ᴅᴏ ᴋʟᴀɴᴜ";

    @Comment("clan invite canceled message sender, argument: #a18b3d{player}&7")
    public static String CLAN_INVITE_CANCELED_SENDER = "&7ᴜsᴜɴᴀʟᴇs ᴢᴀᴘʀᴏsᴢᴇɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ ᴅʟᴀ ɢʀᴀᴄᴢᴀ #a18b3d{player}&7";

    @Comment("Clan invite canceled message reciever, argument: #a18b3d{clan}&7")
    public static String CLAN_INVITE_CANCELED_RECIEVER = "&7ᴢᴀᴘʀᴏsᴢᴇɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ #a18b3d{clan}&7 ᴢᴏsᴛᴀʟᴏ ᴀɴᴜʟᴏᴡᴀɴᴇ";

    @Comment("Clan invite received message, argument: {clan}")
    public static String CLAN_INVITE_RECEIVED = "&7ᴏᴛʀᴢʏᴍᴀʟᴇs ᴢᴀᴘʀᴏsᴢᴇɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ #a18b3d{clan}&7";

    @Comment("Clan invite accepted message, argument: #a18b3d{clan}&7")
    public static String CLAN_INVITE_ACCEPTED = "&7ᴢᴀᴘʀᴏsᴢᴇɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ #a18b3d{clan}&7 ᴢᴏsᴛᴀʟᴏ ᴢᴀᴀᴋᴄᴇᴘᴛᴏᴡᴀɴᴇ";

    @Comment("Clan invite declined message, argument: #a18b3d{clan}&7")
    public static String CLAN_INVITE_DECLINED = "&7ᴢᴀᴘʀᴏsᴢᴇɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ #a18b3d{clan}&7 ᴢᴏsᴛᴀʟᴏ ᴏᴅʀᴢᴜᴄᴏɴᴇ";

    @Comment("You are not owner of this clan message")
    public static String NOT_OWNER = "&7ɴɪᴇ ᴊᴇsᴛᴇs ᴡʟᴀsᴄɪᴄɪᴇʟᴇᴍ ᴛᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("Succesfully set new owner message")
    public static String NEW_OWNER_SET = "&7ɴᴏᴡʏᴍ ᴡʟᴀsᴄɪᴄɪᴇʟᴇᴍ ᴋʟᴀɴᴜ ᴢᴏsᴛᴀʟ #a18b3d{player}&7";

    @Comment("You are not deputy of this clan message")
    public static String NOT_DEPUTY = "&7ɴɪᴇ ᴊᴇsᴛᴇs ᴢᴀsᴛᴇᴘᴄᴀ ᴛᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("You are not member of this clan message")
    public static String NOT_MEMBER = "&7ɴɪᴇ ᴊᴇsᴛᴇs ᴄᴢʟᴏɴᴋɪᴇᴍ ᴛᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("You are already member of this clan message")
    public static String ALREADY_MEMBER = "&7ᴊᴜᴢ ᴊᴇsᴛᴇs ᴄᴢʟᴏɴᴋɪᴇᴍ ᴛᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("You are already owner of this clan message")
    public static String ALREADY_OWNER = "&7ᴊᴜᴢ ᴊᴇsᴛᴇs ᴡʟᴀsᴄɪᴄɪᴇʟᴇᴍ ᴛᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("You are already deputy of this clan message")
    public static String ALREADY_DEPUTY = "&7ᴊᴜᴢ ᴊᴇsᴛᴇs ᴢᴀsᴛᴇᴘᴄᴀ ᴛᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("Pvp enabled message")
    public static String PVP_ENABLED = "&7ᴘᴠᴘ ᴢᴏsᴛᴀʟᴏ ᴡʟᴀᴄᴢᴏɴᴇ";

    @Comment("Pvp disabled message")
    public static String PVP_DISABLED = "&7ᴘᴠᴘ ᴢᴏsᴛᴀʟᴏ ᴡʏʟᴀᴄᴢᴏɴᴇ";

    @Comment("Limit members reached message")
    public static String LIMIT_MEMBERS_REACHED = "&7ᴏsɪᴀɢɴɪᴇᴛᴏ ʟɪᴍɪᴛ ᴄᴢʟᴏɴᴋᴏᴡ";

    @Comment("Limit alliances reached message")
    public static String LIMIT_ALLIANCES_REACHED = "&7ᴏsɪᴀɢɴɪᴇᴛᴏ ʟɪᴍɪᴛ sᴏᴊᴜsᴢɴɪᴋᴏᴡ";

    @Comment("Player belongs to another clan message")
    public static String PLAYER_BELONGS_TO_ANOTHER_CLAN = "&7ɢʀᴀᴄᴢ ɴᴀʟᴇᴢʏ ᴊᴜᴢ ᴅᴏ ɪɴɴᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("player does not belong to your clan")
    public static String PLAYER_DOES_NOT_BELONG_TO_YOUR_CLAN = "&7ɢʀᴀᴄᴢ ɴɪᴇ ɴᴀʟᴇᴢʏ ᴅᴏ ᴛᴡᴏᴊᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("player does not belong to any clan")
    public static String PLAYER_DOES_NOT_BELONG_TO_ANY_CLAN = "&7ɢʀᴀᴄᴢ ɴɪᴇ ɴᴀʟᴇᴢʏ ᴅᴏ żᴀᴅɴᴇɢᴏ ᴋʟᴀɴᴜ";

    @Comment("cannot kick yourself")
    public static String CANNOT_KICK_YOURSELF = "&7ɴɪᴇ ᴍᴏᴢᴇsᴢ ᴡʏʀᴢᴜᴄɪć sᴀᴍ sɪᴇʙɪᴇ";

    @Comment("cannot kick owner")
    public static String CANNOT_KICK_OWNER = "&7ɴɪᴇ ᴍᴏᴢᴇsᴢ ᴡʏʀᴢᴜᴄɪć ᴡʟᴀsᴄɪᴄɪᴇʟᴀ";

    @Comment("successfully kicked player, argument: {player}")
    public static String SUCCESSFULLY_KICKED_PLAYER = "&7ᴘᴏᴍʏśʟɴɪᴇ ᴡʏʀᴢᴜᴄɪᴌᴇś ɢʀᴀᴄᴢᴀ #a18b3d{player}&7 ᴢ ᴋʟᴀɴᴜ";

    @Comment("kicked from clan")
    public static String KICKED_FROM_CLAN = "&7Zostałeś wyrzucony z klanu";

    @Comment("Clan info message, arguments {tag}, {owner}, {deputy-owner}, {points}, {members-online}, {members-size}, {rank}, {members}")
    public static String CLAN_INFO =
            """
                    &8|&7 ᴛᴀɢ ᴋʟᴀɴᴜ #a18b3d{tag}\s
                    &8|&7 ᴡʟᴀꜱᴄɪᴄɪᴇʟ #a18b3d{owner}\s
                    &8|&7 ᴢᴀꜱᴛᴇᴘᴄᴀ #a18b3d{deputy-owner}\s
                    &8|&7 ʀᴀɴᴋɪɴɢ #a18b3d{points}\s
                    &8|&7 ɢʀᴀᴄᴢʏ ᴏɴʟɪɴᴇ #a18b3d{members-online}\s
                    &8|&7 ᴡꜱᴢʏꜱᴄʏ ɢʀᴀᴄᴢᴇ #a18b3d{members-size}\s
                    &8|&7 ᴍɪᴇᴊꜱᴄᴇ ᴡ ʀᴀɴᴋɪɴɢᴜ #a18b3d{rank}\s
                    &8|&7 ɢʀᴀᴄᴢᴇ: #a18b3d{members}""";


    @Comment("Not received invite from clan message, argument {clan}")
    public static String NOT_RECEIVED_INVITE = "&7ɴɪᴇ ᴏᴛʀᴢʏᴍᴀʟᴇś ᴢᴀᴘʀᴏsᴢᴇɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ #a18b3d{clan}&7";

    @Comment("You already belong to clan")
    public static String YOU_ALREADY_BELONG_TO_CLAN = "&7ᴊᴜż ɴᴀʟᴇżʏsᴢ ᴅᴏ ᴋʟᴀɴᴜ";

    @Comment("tag length is not correct")
    public static String TAG_LENGTH = "&7ᴅᴌᴜɢᴏść ᴛᴀɢᴜ ᴍᴜꜱɪ ʙʏć ᴡɪęᴋꜱᴢᴀ ɴɪż 3 ɪ ᴍɴɪᴇᴊꜱᴢᴀ ɴɪż 5";

    @Comment("Tag already exists")
    public static String TAG_ALREADY_EXISTS = "&7ᴛᴀɢ ᴊᴜż ᴊᴇꜱᴛ ᴢᴀᴊęᴛʏ";

    @Comment("Cannot leave as clan owner")
    public static String CANNOT_LEAVE_WHILE_OWNER = "&7ɴɪᴇ ᴍᴏżᴇꜱᴢ ᴏᴘᴜśᴄɪć ᴋʟᴀɴᴜ ʙᴏ ᴊᴇꜱᴛᴇś ʟɪᴅᴇʀᴇᴍ";

    @Comment("Left clan message")
    public static String LEAVE_CLAN = "&7ᴏᴘᴜśᴄɪʟᴇś ᴋʟᴀɴ";

    @Comment("Cannot alliance your own clan message")
    public static String CANNOT_ALLIANCE_YOUR_OWN_CLAN = "&7ɴɪᴇ ᴍᴏżᴇꜱᴢ ᴡᴢɪąść ꜱᴏᴊᴜꜱᴢᴜ ᴢ ᴡʟᴀsɴʏᴍ ᴋʟᴀɴᴇᴍ";

    @Comment("Alliance disabled message")
    public static String ALLIANCE_DISBANDED = "&7ꜱᴏᴊᴜꜱᴢ ᴢᴏꜱᴛᴀł ʀᴏᴢᴡɪąᴢᴀɴʏ";

    @Comment("Alliance already exists message")
    public static String ALLIANCE_ALREADY_EXISTS = "&7ᴊᴜż ᴍᴀꜱᴢ ꜱᴏᴊᴜꜱᴢ ᴢ ᴛʏᴍ ᴋʟᴀɴᴇᴍ";

    @Comment("Alliance created message")
    public static String ALLIANCE_CREATED = "&7ꜱᴏᴊᴜꜱᴢ ᴢᴏꜱᴛᴀł ᴜꜱᴛᴀɴᴏᴡɪᴏɴʏ";

    @Comment("clan invited to alliance message, argument: {clan}")
    public static String INVITED_CLAN_TO_ALLIANCE = "&7ᴢᴀᴘʀᴏꜱɪᴌᴇś ᴋʟᴀɴ #a18b3d{clan}&7 ᴅᴏ ꜱᴏᴊᴜꜱᴢᴜ";

    @Comment("clan invite canceled from alliance message, argument: {clan}")
    public static String CANCELED_INVITE_FROM_ALLIANCE = "&7ᴜꜱᴜɴᴀʟᴇś ᴢᴀᴘʀᴏꜱᴢᴇɴɪᴇ ᴅᴏ ꜱᴏᴊᴜꜱᴢᴜ ᴋʟᴀɴᴜ #a18b3d{clan}&7";

    @Comment("clan invite canceled from alliance message, argument: {clan}")
    public static String CANCELED_INVITE_TO_ALLIANCE = "&7ᴋʟᴀɴ #a18b3d{clan}&7 ᴏᴅʀᴢᴜᴄɪᴌ ᴢᴀᴘʀᴏꜱᴢᴇɴɪᴇ ᴅᴏ ꜱᴏᴊᴜꜱᴢᴜ";

    @Comment("clan invite accepted to alliance message, argument: {clan}")
    public static String ACCEPTED_INVITE_TO_ALLIANCE_RECIEVER = "&7ᴋʟᴀɴ #a18b3d{clan}&7 ᴢᴀᴀᴋᴀᴄᴇᴘᴛᴏᴡᴀᴌ ᴢᴀᴘʀᴏꜱᴢᴇɴɪᴇ ᴅᴏ ꜱᴏᴊᴜꜱᴢᴜ";

    @Comment("You are invited to alliance message, argument: {clan}")
    public static String YOUR_CLAN_WAS_INVITED_TO_ALLIANCE = "&7ᴋʟᴀɴ #a18b3d{clan}&7 ᴡʏꜱᴌᴀᴌ ᴡʏꜱᴌᴀᴌ ᴢᴀᴘʀᴏꜱᴢᴇɴɪᴇ ᴅᴏ ꜱᴏᴊᴜꜱᴢᴜ";

    @Comment("Deputy empty message")
    public static String DEPUTY_EMPTY = "&7ɴɪᴇ ᴢɴᴀʟᴇᴢɪᴏɴᴏ ᴢᴀꜱᴛᴇᴘᴄʏ ᴅʟᴀ ᴋʟᴀɴᴜ";

    @Comment("Deputy removed message")
    public static String DEPUTY_REMOVED = "&7ᴢᴀꜱᴛᴇᴘᴄᴀ ᴢᴏꜱᴛᴀł ᴜꜱᴜɴɪᴇᴛʏ";

    @Comment("Already deputy message, argument: {player}")
    public static String PLAYER_ALREADY_DEPUTY = "&7ɢʀᴀᴄᴢ #a18b3d{player}&7 ᴊᴜż ᴊᴇꜱᴛ ᴢᴀꜱᴛᴇᴘᴄą";


    @Comment("Player info message, arguments {player}, {kills}, {deaths}, {points}, {tag}, {rank}")
    public static String PLAYER_INFO = """
            &8|&7 ɪᴍɪᴇ #a18b3d{player}\s
            &8|&7 ᴢᴀʙóᴊꜱᴛᴡᴀ #a18b3d{kills}\s
            &8|&7 śᴍɪᴇʀᴄɪ #a18b3d{deaths}\s
            &8|&7 ʀᴀɴᴋɪɴɢ #a18b3d{points}\s
            &8|&7 ᴛᴀɢ ᴋʟᴀɴᴜ #a18b3d{tag}\s
            &8|&7 ᴘᴏᴢʏᴄᴊᴀ #a18b3d{rank}\s
            """;

    @Comment("Clan help message")
    public static String HELP_MESSAGE = """
            &8|&7 ᴘᴏᴍᴏᴄ\s
            &8|&7 /ᴋʟᴀɴ ꜱᴛᴡóʀᴢ <ᴛᴀɢ> - ᴢᴀᴋʀᴇᴀᴛᴏᴡᴀɴɪᴇ ᴋʟᴀɴᴜ\s
            &8|&7 /ᴋʟᴀɴ ᴜꜱᴜń - ᴜsᴜɴɪęᴄɪᴇ ᴋʟᴀɴᴜ\s
            &8|&7 /ᴋʟᴀɴ ᴅᴏłąᴄᴢ <ᴛᴀɢ> - ᴅᴏʟąᴄᴢᴇɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ\s
            &8|&7 /ᴋʟᴀɴ ᴏᴘᴜść - ᴏᴅʟąᴄᴢᴇɴɪᴇ ᴏᴅ ᴋʟᴀɴᴜ\s
            &8|&7 /ᴋʟᴀɴ ɪɴꜰᴏ - ɪɴғᴏʀᴍᴀᴄᴊᴇ ᴏ ᴋʟᴀɴɪᴇ\s
            &8|&7 /ᴋʟᴀɴ ᴢᴀᴘʀᴏś <ɢʀᴀᴄᴢ> - ᴢᴀᴘʀᴏꜱɪɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ\s
            &8|&7 /ᴋʟᴀɴ ᴏᴅʀᴢᴜᴄ <ɢʀᴀᴄᴢ> - ᴏᴅʀᴢᴜᴄᴇɴɪᴇ ᴢᴀᴘʀᴏꜱɪɴɪᴇ ᴅᴏ ᴋʟᴀɴᴜ\s
            &8|&7 /ᴋʟᴀɴ ᴜꜱᴛᴀᴡᴢᴀꜱᴛᴇᴘᴄᴇ <ɢʀᴀᴄᴢ> - ᴢᴀꜱᴛᴇᴘᴄᴀ ᴅᴏ ᴋʟᴀɴᴜ\s
            &8|&7 /ᴋʟᴀɴ ᴜꜱᴛᴀᴡʟɪᴅᴇʀᴀ <ɢʀᴀᴄᴢ> - ᴜꜱᴛᴀᴡɪᴇɴɪᴇ ʟɪᴅᴇʀᴀ ᴋʟᴀɴᴜ\s
            &8|&7 /ᴋʟᴀɴ ᴘᴠᴘ - ᴡʟᴀᴄᴢᴇɴɪᴇ/ᴡʏʟᴀᴄᴢᴇɴɪᴇ ᴘᴠᴘ\s
            """;

    @Comment("Player help message")
    public static String PLAYER_HELP_MESSAGE = """
            &8|&7 ᴘᴏᴍᴏᴄ\s
            &8|&7 /ɢʀᴀᴄᴢ <ɢʀᴀᴄᴢ> - ɪɴғᴏʀᴍᴀᴄᴊᴇ ᴏ ɢʀᴀᴄᴢᴜ\s
            """;

    @Comment("Admin command when world is not starting with 'guild' ")
    public static String NOT_IN_GUILD_WORLD = "&7ᴋʀʏᴊóᴡᴋᴀ ɢɪʟᴅᴀ ᴍᴏżᴇ ʙʏć ᴛʏʟᴋᴏ ᴡ śᴡɪᴇᴄɪᴇ ᴢ ᴘʀᴢᴇᴅʀᴏꜱᴛᴋɪᴇᴍ \"ɢᴜɪʟᴅ\"";

    @Comment("You don't belong to any clan")
    public static String YOU_DONT_BELONG_TO_ANY_CLAN = "&7ɴɪᴇ ɴᴀʟᴇżʏꜱᴢ ᴅᴏ żᴀᴅɴᴇɢᴏ ᴋʟᴀɴᴜ";
    @Comment("Your clan does not have hideout")
    public static String YOUR_CLAN_DOES_NOT_HAVE_HIDEOUT = "&7ᴛᴡóᴊ ᴋʟᴀɴ ɴɪᴇ ᴍᴀ ᴋʀʏᴊóᴡᴋɪ";
    @Comment("Hideout already have this upgrade")
    public static String HIDEOUT_ALREADY_HAVE_THIS_UPGRADE = "&7ᴋʀʏᴊóᴡᴋᴀ ᴊᴜż ᴍᴀ ᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ";
    @Comment("Hideout upgrade bought")
    public static String UPGRADE_IS_BOUGHT = "&7ᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌᴏ ᴊᴜż ᴋᴜᴘɪᴏɴᴇ";

    @Comment("Hideout upgrade not finished yet")
    public static String UPGRADE_IS_NOT_FINISHED_YET = "&7ᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀᴌᴏ ᴊᴜż ᴋᴜᴘɪᴏɴᴇ ᴀʟᴇ ɴɪᴇ ᴊᴇꜱᴛ ᴊᴇꜱᴢᴄᴢᴇ ɢᴏᴛᴏᴡᴇ";

    @Comment("Does not belong to this hideout")
    public static String YOU_DONT_BELONG_TO_THIS_HIDEOUT = "&7ɴɪᴇ ɴᴀʟᴇżʏꜱᴢ ᴅᴏ ᴛᴇᴊ ᴋʀʏᴊóᴡᴋɪ";

    @Comment("Hideout does not exist")
    public static String HIDEOUT_DOES_NOT_EXIST = "&7ᴋʀʏᴊóᴡᴋᴀ ɴɪᴇ ɪꜱᴛɴɪᴇᴊᴇ";

    public static void sendMessageFormated(Player player, String message, MessageType type) {
        message = ColorFixer.addColors(message);
        switch (type) {
            case TITLE:
                player.sendTitle(message, null, 15, 70, 15);
                break;
            case SUBTITLE:
                player.sendTitle(null, message, 15, 70, 15);
                break;
            case ACTIONBAR:
                player.sendActionBar(TextComponent.fromLegacyText(message));
                break;
            case CHAT:
                player.sendMessage(message);
                break;
        }
    }


    public static void broadcastMessageFormated(String message, MessageType messageType) {
        message = ColorFixer.addColors(message);
        switch (messageType) {
            case TITLE:
                for(Player p : Bukkit.getOnlinePlayers())
                    p.sendTitle(message, null, 15, 70, 15);
                break;
            case SUBTITLE:
                for(Player p : Bukkit.getOnlinePlayers())
                    p.sendTitle(null, message, 15, 70, 15);
                break;
            case ACTIONBAR:
                for(Player p : Bukkit.getOnlinePlayers())
                    p.sendActionBar(TextComponent.fromLegacyText(message));
                break;
            case CHAT:
                Bukkit.broadcastMessage(message);
                break;
        }
    }

    public static String secondsToTimeFormat(int seconds) {
        int minutes = seconds / 60;
        int sec = seconds % 60;
        int hours = minutes / 60;
        minutes %= 60;
        StringBuilder sb = new StringBuilder();
        if(hours > 0) {
            sb.append(hours).append(" godzin ");
        }
        if(minutes > 0) {
            sb.append(minutes).append(" minut ");
        }
        if(sec > 0) {
            sb.append(sec).append(" sekund");
        }

        return sb.toString();
    }
}
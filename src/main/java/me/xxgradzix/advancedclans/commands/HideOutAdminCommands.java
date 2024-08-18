package me.xxgradzix.advancedclans.commands;

import com.fastasyncworldedit.core.Fawe;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.util.SideEffectSet;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.InvalidHideoutWorldNameException;
import me.xxgradzix.advancedclans.messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.LazyMetadataValue;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.InvalidObjectException;

import static me.xxgradzix.advancedclans.controllers.GuildHideOutController.paste;

public class HideOutAdminCommands implements CommandExecutor {

    private final AdvancedGuilds plugin;
    private final GuildHideOutController guildHideOutController;

    public HideOutAdminCommands(AdvancedGuilds plugin, GuildHideOutController guildHideOutController) {
        this.plugin = plugin;
        this.guildHideOutController = guildHideOutController;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if(!(commandSender instanceof Player player)) {
            commandSender.sendMessage(MessageManager.ONLY_FOR_PLAYERS);
            return false;
        }

        World world = player.getWorld();

        if(!world.getName().startsWith("guild")){
            player.sendMessage(MessageManager.NOT_IN_GUILD_WORLD);
            return false;
        }


        String temp;
        try {
            temp = strings[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            temp = "1";
        }

        switch (temp) {

            case "1" -> {

                try {
                    guildHideOutController.resetHideOutCompletelyOrCreate(world.getName());
                } catch (InvalidObjectException e) {
                    player.sendMessage("Taki świat nie istnieje");
                    return false;
                } catch (InvalidHideoutWorldNameException e) {
                    player.sendMessage("Kryjowke mozna otworzyc tylko w swiecie o nazwie z prefixem 'guild_'");
                    return false;
                }
            }
            case "2" -> {

                User cachedUser = ClanAndUserDataManager.getCachedUser(player.getUniqueId());

                try {
                    GuildHideOutDataManager.occupyHideOut(world.getName(), ClanAndUserDataManager.getCachedClan(cachedUser.getClanTag()));
                } catch (HideOutDoesNotExistException e) {
                    player.sendMessage(MessageManager.HIDEOUT_DOES_NOT_EXIST);
                }

            }
            case "3" -> {

                Location location = player.getLocation();

                String worldName = strings[1];

                BlockData blockData = Material.LODESTONE.createBlockData();

                Block block = location.getBlock();

                block.setBlockData(blockData);

                // add metadata to block
                block.setMetadata("hideout", new LazyMetadataValue(plugin, () -> worldName));
            }
        }



        return false;
    }

}

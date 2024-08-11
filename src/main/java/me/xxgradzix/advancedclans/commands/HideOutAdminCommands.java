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
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;

public class HideOutAdminCommands implements CommandExecutor {

    private final AdvancedGuilds plugin;

    public HideOutAdminCommands(AdvancedGuilds plugin) {
        this.plugin = plugin;
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

        Location loc = new Location(world, 0, 100, 0);

        String temp;
        try {
            temp = strings[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            temp = "1";
        }

        switch (temp) {

//            case "1" -> {
//                File file = new File(plugin.getDataFolder().getAbsolutePath() + "/guild.schem");
//
//                player.sendMessage("Paste schem");
//                paste(loc, file);
//
//                guildHideOutDataManager.createNewHideOut(world.getName());
//            }
            case "2" -> {


                GuildHideOutDataManager.resetOrCreateHideOut(world.getName());
            }
            case "3" -> {




            }

        }



        return false;
    }

}

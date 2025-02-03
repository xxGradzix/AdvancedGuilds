package me.xxgradzix.advancedclans.listener.clan;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.clan.Clan;
import me.xxgradzix.advancedclans.data.database.entities.clan.User;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.UserController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class AsyncPlayerChatListener implements Listener {

    private AdvancedGuilds plugin;
    
    private UserController userController;
//    private Config config;

    public AsyncPlayerChatListener(AdvancedGuilds plugin, UserController userController){
        this.plugin = plugin;
//        this.config = plugin.getConfigPlugin();
        this.userController = userController;
    }
    @EventHandler(priority = EventPriority.LOWEST)
    public void onSendMessage(AsyncChatEvent event)
    {
        Player player = event.getPlayer();
        Component messageComponent = event.message();
        if(!(messageComponent instanceof TextComponent))
            return;

        TextComponent textMessage = (TextComponent) messageComponent;
        String message = textMessage.content();

        User user = ClanAndUserDataService.getCachedUser(player.getUniqueId());
        if(!user.hasClan())
            return;

        Clan clan = ClanAndUserDataService.getCachedClan(user.getClanTag());
        if(message.startsWith("!!"))
        {
            event.setCancelled(true);
            message = message.substring(2);
            if(message.length()==0)
                return;

            Set<UUID> uuids = new HashSet<>(clan.getMembers());
            for (String allianceTag : clan.getAlliances()) {
                Clan alliance = ClanAndUserDataService.getCachedClan(allianceTag);
                if(alliance == null)
                    continue;
                uuids.addAll(alliance.getMembers());
            }
            for (UUID memberUUID : uuids) {
                Player memberPlayer = Bukkit.getPlayer(memberUUID);
                if(memberPlayer == null || memberPlayer.isOnline())
                    continue;
                MessageManager.sendMessageFormated(memberPlayer, MessageManager.ALLIANCE_CHAT_FORMAT
                        .replace("{tag}", clan.getTag())
                        .replace("{message}", message)
                        .replace("{player}", player.getName()), MessageType.CHAT);

            }

            return;
        }
        if(message.startsWith("!"))
        {
            event.setCancelled(true);
            message = message.substring(1);
            if(message.length()==0)
                return;


            for (UUID memberUUID : clan.getMembers()) {
                Player memberPlayer = Bukkit.getPlayer(memberUUID);
                if(memberPlayer == null || memberPlayer.isOnline())
                    continue;
                MessageManager.sendMessageFormated(memberPlayer, MessageManager.ALLIANCE_CHAT_FORMAT
                        .replace("{tag}", clan.getTag())
                        .replace("{message}", message)
                        .replace("{player}", player.getName()), MessageType.CHAT);
            }
            return;
        }

    }

}

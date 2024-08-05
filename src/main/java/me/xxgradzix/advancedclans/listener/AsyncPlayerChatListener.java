package me.xxgradzix.advancedclans.listener;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.manager.UserManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

public class AsyncPlayerChatListener implements Listener {

    private AdvancedGuilds plugin;
    
    private UserManager userManager;
//    private Config config;

    public AsyncPlayerChatListener(AdvancedGuilds plugin, UserManager userManager){
        this.plugin = plugin;
//        this.config = plugin.getConfigPlugin();
        this.userManager = userManager;
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

        User user = userManager.getUserData().get(player.getUniqueId());
        if(!user.hasClan())
            return;

        Clan clan = user.getClan();
        if(message.startsWith("!!"))
        {
            event.setCancelled(true);
            message = message.substring(2);
            if(message.length()==0)
                return;

            // TODO SEND alliance message
//            MessageUtil.sendMessageAlliance(clan, config.formatAllianceMessage
//                    .replace("{tag}", clan.getTag())
//                    .replace("{message}", message)
//                    .replace("{player}", player.getName())
//            );
            return;
        }
        if(message.startsWith("!"))
        {
            event.setCancelled(true);
            message = message.substring(1);
            if(message.length()==0)
                return;

            // TODO SEND clan message
//            MessageUtil.sendMessage(clan, config.formatClanMessage
//                    .replace("{tag}", clan.getTag())
//                    .replace("{message}", message)
//                    .replace("{player}", player.getName())
//            );
            return;
        }

    }

}

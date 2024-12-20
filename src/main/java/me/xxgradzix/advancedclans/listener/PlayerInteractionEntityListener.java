package me.xxgradzix.advancedclans.listener;

import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.manager.CooldownManager;
import me.xxgradzix.advancedclans.manager.UserManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public class PlayerInteractionEntityListener implements Listener {

//    private final LangMessage lang;
    private final UserManager userManager;
    private final CooldownManager cooldownManager;

    public PlayerInteractionEntityListener(UserManager userManager, CooldownManager cooldownManager) {
//        this.lang = lang;
        this.userManager = userManager;
        this.cooldownManager = cooldownManager;
    }

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
        Player player = event.getPlayer();

        if(event.getRightClicked() instanceof Player clickPlayer) {
            if(!player.isSneaking())
                return;
            // disable double action
            if(event.getHand() == EquipmentSlot.OFF_HAND) return;
            // check user has cooldown
            if(cooldownManager.hasCooldown(player)) {
//                MessageUtil.sendMessage(player, lang.langSlowDown);
                // TODO Send message to player about cooldown
                return;
            }
            // add cooldown
            cooldownManager.addCooldown(player);

            // find clicked user data
            Optional<User> userByPlayer = userManager.findUserByPlayer(clickPlayer);
            if(userByPlayer.isPresent()) {
                User user = userByPlayer.get();
                // send player message about clicked user
                userManager.infoPlayer(player, user);
            }

        }
    }



}

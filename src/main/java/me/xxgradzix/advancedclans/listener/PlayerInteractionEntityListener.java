package me.xxgradzix.advancedclans.listener;

import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.manager.CooldownManager;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.UserController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.Optional;

public class PlayerInteractionEntityListener implements Listener {

//    private final LangMessage lang;
    private final UserController userController;
    private final CooldownManager cooldownManager;

    public PlayerInteractionEntityListener(UserController userController, CooldownManager cooldownManager) {
//        this.lang = lang;
        this.userController = userController;
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
                MessageManager.sendMessageFormated(player, MessageManager.SLOW_DOWN, MessageType.CHAT);
                return;
            }
            // add cooldown
            cooldownManager.addCooldown(player);

            // find clicked user data
            Optional<User> userByPlayer = userController.findUserByPlayer(clickPlayer);
            if(userByPlayer.isPresent()) {
                User user = userByPlayer.get();
                // send player message about clicked user
                userController.infoPlayer(player, user);
            }

        }
    }



}

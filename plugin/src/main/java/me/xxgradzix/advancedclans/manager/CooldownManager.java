package me.xxgradzix.advancedclans.manager;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class CooldownManager {

    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    public boolean hasCooldown(Player player) {
        Long timeMS = cooldown.get(player.getUniqueId());
        return timeMS!=null && timeMS >= System.currentTimeMillis();
    }

    public void addCooldown(Player player) {
        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + 1000L);
    }

    public void delPlayerFromCooldown(Player player) {
        cooldown.remove(player.getUniqueId());
    }


}

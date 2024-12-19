package me.xxgradzix.advancedclans.guildshideoutsystem.managers;

import org.bukkit.event.inventory.InventoryAction;

@FunctionalInterface
public interface Action {

    void execute();

}
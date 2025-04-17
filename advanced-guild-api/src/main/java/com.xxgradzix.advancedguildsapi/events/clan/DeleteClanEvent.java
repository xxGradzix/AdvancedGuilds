package com.xxgradzix.advancedguildsapi.events.clan;

import entities.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeleteClanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player player;
    private final Clan clanImpl;
    private boolean isCancelled;
    public DeleteClanEvent(Player player, Clan clanImpl) {
        this.player = player;
        this.clanImpl = clanImpl;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }

    public Player getPlayer() {
        return player;
    }

    public Clan getClan() {
        return clanImpl;
    }
}

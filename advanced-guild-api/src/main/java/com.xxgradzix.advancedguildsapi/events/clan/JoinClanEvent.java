package com.xxgradzix.advancedguildsapi.events.clan;

import
entities.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class JoinClanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player joiningPlayer;
    private final Clan clanImpl;
    private boolean isCancelled;
    public JoinClanEvent(Clan clanImpl, Player joiningPlayer) {
        this.joiningPlayer = joiningPlayer;
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

    public Player getJoiningPlayer() {
        return joiningPlayer;
    }

    public Clan getClan() {
        return clanImpl;
    }
}

package com.xxgradzix.advancedguildsapi.events.clan;

import entities.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerKickClanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID kickedPlayerUUID;
    private final Clan clanImpl;
    private boolean isCancelled;
    public PlayerKickClanEvent(Clan clanImpl, UUID kickedPlayerUUID) {
        this.kickedPlayerUUID = kickedPlayerUUID;
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

    public UUID getKickedPlayer() {
        return kickedPlayerUUID;
    }

    public Clan getClan() {
        return clanImpl;
    }
}

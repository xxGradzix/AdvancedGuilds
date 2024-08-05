package me.xxgradzix.advancedclans.events;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.UUID;

public class PlayerKickClanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final UUID kickedPlayerUUID;
    private final Clan clan;
    private boolean isCancelled;
    public PlayerKickClanEvent(Clan clan, UUID kickedPlayerUUID) {
        this.kickedPlayerUUID = kickedPlayerUUID;
        this.clan = clan;
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
        return clan;
    }
}

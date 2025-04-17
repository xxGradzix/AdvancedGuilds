package com.xxgradzix.advancedguildsapi.events.clan;

import entities.Clan;
import entities.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeleteDeputyEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Clan clanImpl;

    private final User removedDeputyUUID;
    private boolean isCancelled;
    public DeleteDeputyEvent(Clan clanImpl, User userImpl) {
        this.clanImpl = clanImpl;
        this.removedDeputyUUID = userImpl;
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

    public User getRemovedDeputyUUID() {
        return removedDeputyUUID;
    }

    public Clan getClan() {
        return clanImpl;
    }

}

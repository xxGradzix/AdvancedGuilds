package com.xxgradzix.advancedguildsapi.events.clan;

import entities.Clan;
import entities.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ChangeOwnerClanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Clan clanImpl;
    private final User previousOwner;
    private final User newOwner;
    private boolean isCancelled;
    public ChangeOwnerClanEvent(Clan clanImpl, User previousOwner, User newOwner) {
        this.clanImpl = clanImpl;
        this.previousOwner = previousOwner;
        this.newOwner = newOwner;
        this.isCancelled = false;
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

    public Clan getClan() {
        return clanImpl;
    }

    public User getPreviousOwner() {
        return previousOwner;
    }
    public User getNewOwner() {
        return newOwner;
    }
}

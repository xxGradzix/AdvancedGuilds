package me.xxgradzix.advancedclans.events;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


public class ChangeOwnerClanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Clan clan;
    private final User previousOwner;
    private final User newOwner;
    private boolean isCancelled;
    public ChangeOwnerClanEvent(Clan clan, User previousOwner, User newOwner) {
        this.clan = clan;
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
        return clan;
    }

    public User getPreviousOwner() {
        return previousOwner;
    }
    public User getNewOwner() {
        return newOwner;
    }
}

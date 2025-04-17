package com.xxgradzix.advancedguildsapi.events.clan;

import entities.Clan;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class CreateAllianceEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Clan firstClanImpl;
    private final Clan secondClanImpl;
    private boolean isCancelled;
    public CreateAllianceEvent(Clan firstClanImpl, Clan secondClanImpl) {
        this.firstClanImpl = firstClanImpl;
        this.secondClanImpl = secondClanImpl;
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


    public Clan getFirstClan() {
        return firstClanImpl;
    }

    public Clan getSecondClan() {
        return secondClanImpl;
    }
}

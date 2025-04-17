package com.xxgradzix.advancedguildsapi.events.clan;

import entities.Clan;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeputyChangeClanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Clan clanImpl;
    private final Player owner;
    private final OfflinePlayer deputy;
    private boolean isCancelled;
    public DeputyChangeClanEvent(Clan clanImpl, Player owner, OfflinePlayer deputy) {
        this.clanImpl = clanImpl;
        this.owner = owner;
        this.deputy = deputy;
        this.isCancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Clan getClan() {
        return clanImpl;
    }

    public OfflinePlayer getDeputy() {
        return deputy;
    }

    public Player getOwner() {
        return owner;
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


}

package com.xxgradzix.advancedguildsapi.events.clan;

import entities.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InviteClanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player invitedPlayer;
    private final Clan clanImpl;
    private boolean isCancelled;
    public InviteClanEvent(Clan clanImpl, Player invitedPlayer) {
        this.invitedPlayer = invitedPlayer;
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

    public Player getInvitedPlayer() {
        return invitedPlayer;
    }

    public Clan getClan() {
        return clanImpl;
    }
}

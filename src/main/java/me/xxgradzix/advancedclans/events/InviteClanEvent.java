package me.xxgradzix.advancedclans.events;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class InviteClanEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private final Player invitedPlayer;
    private final Clan clan;
    private boolean isCancelled;
    public InviteClanEvent(Clan clan, Player invitedPlayer) {
        this.invitedPlayer = invitedPlayer;
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

    public Player getInvitedPlayer() {
        return invitedPlayer;
    }

    public Clan getClan() {
        return clan;
    }
}

package me.xxgradzix.advancedclans.events;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeputyChangeClanEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Clan clan;
    private final Player owner;
    private final Player deputy;
    private boolean isCancelled;
    public DeputyChangeClanEvent(Clan clan, Player owner, Player deputy) {
        this.clan = clan;
        this.owner = owner;
        this.deputy = deputy;
        this.isCancelled = false;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Clan getClan() {
        return clan;
    }

    public Player getDeputy() {
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

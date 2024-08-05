package me.xxgradzix.advancedclans.events;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DeleteDeputyEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Clan clan;

    private final User removedDeputyUUID;
    private boolean isCancelled;
    public DeleteDeputyEvent(Clan clan, User user) {
        this.clan = clan;
        this.removedDeputyUUID = user;
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
        return clan;
    }

}

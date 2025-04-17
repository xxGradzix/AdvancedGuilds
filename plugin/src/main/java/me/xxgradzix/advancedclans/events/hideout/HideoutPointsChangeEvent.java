package me.xxgradzix.advancedclans.events.hideout;

import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HideoutPointsChangeEvent extends Event implements Cancellable {

    @Getter
    private final GuildHideout hideout;

    @Getter
    private int previousPoints;

    @Getter
    @Setter
    private int newPoints;

    private boolean isCancelled;

    public HideoutPointsChangeEvent(GuildHideout hideout, int previousPoints, int newPoints) {
        this.hideout = hideout;
        this.previousPoints = previousPoints;
        this.newPoints = newPoints;
        isCancelled = false;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        isCancelled = b;
    }
}

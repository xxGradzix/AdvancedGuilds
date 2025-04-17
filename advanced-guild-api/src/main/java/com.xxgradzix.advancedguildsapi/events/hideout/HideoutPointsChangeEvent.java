package com.xxgradzix.advancedguildsapi.events.hideout;

import entities.GuildHideout;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HideoutPointsChangeEvent extends Event implements Cancellable {

    private final GuildHideout hideout;

    private int previousPoints;

    private int newPoints;



    public void setNewPoints(int newPoints) {
        this.newPoints = newPoints;
    }

    public GuildHideout getHideout() {
        return hideout;
    }

    public int getPreviousPoints() {
        return previousPoints;
    }

    public int getNewPoints() {
        return newPoints;
    }

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

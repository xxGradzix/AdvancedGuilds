package com.xxgradzix.advancedguildsapi.events.hideout;

import entities.GuildHideout;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HideoutLevelChangeEvent extends Event {

    private final GuildHideout hideout;

    private int previousLevel;

    private int newLevel;

    public void setNewLevel(int newLevel) {
        this.newLevel = newLevel;
    }

    public GuildHideout getHideout() {
        return hideout;
    }

    public int getPreviousLevel() {
        return previousLevel;
    }

    public int getNewLevel() {
        return newLevel;
    }

    public HideoutLevelChangeEvent(GuildHideout hideout, int previousLevel, int newLevel) {
        this.hideout = hideout;
        this.previousLevel = previousLevel;
        this.newLevel = newLevel;
    }

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }
}

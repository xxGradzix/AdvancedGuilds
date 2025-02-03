package me.xxgradzix.advancedclans.events.hideout;

import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class HideoutLevelChangeEvent extends Event {

    @Getter
    private final GuildHideout hideout;

    @Getter
    private int previousLevel;

    @Getter
    @Setter
    private int newLevel;

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

package me.xxgradzix.advancedclans.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PointsChangeUserEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final Player killer;
    private final Player victim;
    private int pointKiller;
    private int pointVictim;
    private boolean isCancelled;
    public PointsChangeUserEvent(Player killer, Player victim, int pointKiller, int pointVictim) {
        this.killer = killer;
        this.victim = victim;
        this.pointKiller = pointKiller;
        this.pointVictim = pointVictim;
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

    public Player getKiller() {
        return killer;
    }

    public Player getVictim() {
        return victim;
    }

    public int getPointKiller() {
        return pointKiller;
    }

    public int getPointVictim() {
        return pointVictim;
    }

    public void setPointKiller(int pointKiller) {
        this.pointKiller = pointKiller;
    }

    public void setPointVictim(int pointVictim) {
        this.pointVictim = pointVictim;
    }
}

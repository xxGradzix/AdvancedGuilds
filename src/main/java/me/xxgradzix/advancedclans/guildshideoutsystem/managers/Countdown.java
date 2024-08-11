package me.xxgradzix.advancedclans.guildshideoutsystem.managers;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;

public class Countdown implements Runnable {

    private final JavaPlugin plugin;

    private Integer assignedTaskId;

    private final int seconds;
    private int secondsLeft;

    private final Hologram hologram;

    private final Action action;

    public Countdown(final JavaPlugin plugin, final int seconds, Location location, Action action) {

        this.plugin = plugin;
        this.seconds = seconds;
        this.secondsLeft = seconds;
        this.action = action;

        String hologramName = "guild_" + location.getBlockX() + location.getBlockY() + location.getBlockZ();

        Hologram tempHol = DHAPI.getHologram(hologramName);
        DHAPI.removeHologram(hologramName);
        if(tempHol != null) tempHol.unregister();


        this.hologram = DHAPI.createHologram(hologramName, location, Arrays.asList(" ", " "));
        hologram.register();
        hologram.showAll();
    }

    public static String format(final long mills) {
        long seconds = mills / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        long days = hours / 24;

        seconds %= 60;
        minutes %= 60;
        hours %= 24;

        StringBuilder formattedTime = new StringBuilder();
        if (days > 0) {
            formattedTime.append(days).append("d ");
        }
        if (hours > 0) {
            formattedTime.append(hours).append("h ");
        }
        if (minutes > 0) {
            formattedTime.append(minutes).append("m ");
        }
        if (seconds > 0 || (mills > 0 && formattedTime.length() == 0)) {
            formattedTime.append(seconds).append("s");
        }

        return formattedTime.toString().trim();
    }

//    @Override
//    public void run() {
//        if (this.secondsLeft < 1) {
//            this.scheduleTimer();
//            if (this.assignedTaskId != null) {
//                Bukkit.getScheduler().cancelTask(this.assignedTaskId);
//            }
//            return;
//        }
//        DHAPI.setHologramLine(this.hologram, 0, ChatColor.GREEN+ "Ten generator odnowi sie za: " + format(this.secondsLeft * 1000));
//        hologram.updateAll();
//        --this.secondsLeft;
//    }

    @Override
    public void run() {
        if (this.secondsLeft < 1) {
//            this.secondsLeft = this.seconds; // Reset the countdown
            action.execute();
            this.killTask();
            return;
        } else {
            // Only update the hologram when there are significant changes
//            if (this.secondsLeft == this.seconds) {
                DHAPI.setHologramLine(this.hologram, 0, ChatColor.GRAY+ "To ulepszenie zostanie ukończone za:");
                DHAPI.setHologramLine(this.hologram, 1, ChatColor.GREEN + format(this.secondsLeft * 1000));

                hologram.updateAll();
//            }
            --this.secondsLeft;
        }
    }

    public int getTotalSeconds() {
        return this.seconds;
    }

    public int getSecondsLeft() {
        return this.secondsLeft;
    }

    public Integer getTaskId() {
        return this.assignedTaskId;
    }

    public void killTask() {
//        hologram.delete();
        hologram.destroy();
        hologram.unregister();

        hologram.getLocation().getWorld().spawnParticle(Particle.CLOUD, 120, 2, 2 ,2, 0.005);
        Bukkit.getScheduler().cancelTask(this.assignedTaskId);
    }

    public void scheduleTimer() {
        // If a task is already running, don't schedule a new one
        if (this.assignedTaskId == null || Bukkit.getScheduler().isCurrentlyRunning(this.assignedTaskId)) {
            this.assignedTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, this, 0L, 20L);
        }
    }
}
package me.xxgradzix.advancedclans.guildshideoutsystem.managers;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public class Countdown extends BukkitRunnable {

    private final JavaPlugin plugin;

    private Integer assignedTaskId;

    private int secondsLeft;

    private final Hologram hologram;

    private final Action action;

    public Countdown(final JavaPlugin plugin, final int seconds, Location location, Action action) {

        this.plugin = plugin;
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
        if (seconds > 0 || (mills > 0 && formattedTime.isEmpty())) {
            formattedTime.append(seconds).append("s");
        }
        return formattedTime.toString().trim();
    }

    @Override
    public void run() {
        if (this.secondsLeft < 1) {
            action.execute();
            this.killTask();
            cancel();
        } else {
            DHAPI.setHologramLine(this.hologram, 0, ChatColor.GRAY+ "ᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏńᴄᴢᴏɴᴇ ᴢᴀ:");
            DHAPI.setHologramLine(this.hologram, 1, ChatColor.GREEN + format(this.secondsLeft * 1000L));

            hologram.updateAll();
            --this.secondsLeft;
        }
    }

    public int getTaskId() {
        return this.assignedTaskId;
    }

    public void killTask() {

        hologram.getLocation().getWorld().spawnParticle(Particle.CLOUD, hologram.getLocation(), 120, 2, 2 ,2, 0.005);
        hologram.destroy();
        hologram.unregister();

        Bukkit.getScheduler().cancelTask(this.assignedTaskId);
    }

    public void scheduleTimer() {
        if (this.assignedTaskId == null || Bukkit.getScheduler().isCurrentlyRunning(this.assignedTaskId)) {
            this.assignedTaskId = runTaskTimer(this.plugin, 0L, 20L).getTaskId();
        }
    }
}
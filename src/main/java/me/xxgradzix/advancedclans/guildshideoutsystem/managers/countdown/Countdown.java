package me.xxgradzix.advancedclans.guildshideoutsystem.managers.countdown;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import lombok.Getter;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.utills.HideoutTimeEvent;
import me.xxgradzix.advancedclans.messages.MessageManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Countdown extends BukkitRunnable implements HideoutTimeEvent {

    public enum HologramType {
        GUILD_UPGRADE
    }

    public static final ConcurrentHashMap<String, Countdown> countdowns = new ConcurrentHashMap<>();

    private final JavaPlugin plugin;

    private Integer assignedTaskId;

    private int secondsLeft;

    private Hologram hologram;

    private final Action countdownFinishAction;

//    private Player owner = null;
    @Getter
    private CountdownOwner owner;

    private HologramType hologramType;

    public int secondsLeft() {
        return this.secondsLeft;
    }

    public Countdown(final JavaPlugin plugin, CountdownOwner owner, HologramType hologramType, final int seconds, Location location, Action countdownFinishAction) {

        if(!hologramType.equals(HologramType.GUILD_UPGRADE)) throw new IllegalArgumentException("Invalid hologram type for this constructor");

        this.plugin = plugin;
        this.secondsLeft = seconds;
        this.countdownFinishAction = countdownFinishAction;
        this.hologramType = hologramType;

        this.owner = owner;

        String hologramName = "guild_" + location.getBlockX() + location.getBlockY() + location.getBlockZ();

        Hologram tempHol = DHAPI.getHologram(hologramName);
        DHAPI.removeHologram(hologramName);
        if(tempHol != null) tempHol.unregister();
        this.hologram = DHAPI.createHologram(hologramName, location, Arrays.asList(" ", " "));

        countdowns.put(hologram.getId(), this);
        hologram.register();
        hologram.showAll();
    }

    public Countdown(final JavaPlugin plugin, CountdownOwner owner, HologramType hologramType, final int seconds, Location location, Action action, List<Player> playersToShow) {

        this(plugin, owner, hologramType, seconds, location, action);

//        owner = playersToShow.getFirst();

        if(hologram != null) {
            hologram.hideAll();
            for (Player player : playersToShow) {
                hologram.setShowPlayer(player);
            }
        }
    }

    @Override
    public void run() {
        if (this.secondsLeft < 1) {
            countdownFinishAction.execute();
            hologram.getLocation().getWorld().spawnParticle(Particle.CLOUD, hologram.getLocation(), 120, 2, 2 ,2, 0.005);
            this.killTask();
            cancel();
        } else {

            switch (hologramType) {
                case GUILD_UPGRADE -> {
                    DHAPI.setHologramLine(this.hologram, 0, ChatColor.GRAY+ "ᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴢᴏꜱᴛᴀɴɪᴇ ᴜᴋᴏńᴄᴢᴏɴᴇ ᴢᴀ:");
                    DHAPI.setHologramLine(this.hologram, 1, ChatColor.GREEN + MessageManager.secondsToTimeFormat(this.secondsLeft));
                }
                default -> throw new IllegalArgumentException("Invalid hologram type");
            }

            hologram.updateAll();
            --this.secondsLeft;
        }
    }

    public int getTaskId() {
        return this.assignedTaskId;
    }

    public void killTask() {
        countdowns.remove(this.hologram.getId());
        hologram.destroy();
        hologram.unregister();
        Bukkit.getScheduler().cancelTask(this.assignedTaskId);
    }

    public void fastForward() {
        this.secondsLeft = 0;
    }

    public void scheduleTimer() {
        if (this.assignedTaskId == null || Bukkit.getScheduler().isCurrentlyRunning(this.assignedTaskId)) {
            this.assignedTaskId = runTaskTimer(this.plugin, 0L, 20L).getTaskId();
        }
    }
}
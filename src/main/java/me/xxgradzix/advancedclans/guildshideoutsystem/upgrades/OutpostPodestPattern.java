package me.xxgradzix.advancedclans.guildshideoutsystem.upgrades;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class OutpostPodestPattern implements UpgradePattern {

    private final File schemFile;
    private final int coolDown;

    private final int price;

    public OutpostPodestPattern(JavaPlugin plugin) {
        this.schemFile = new File(plugin.getDataFolder().getAbsolutePath() + "/outpost-podest.schem");
        this.price = 0;
        this.coolDown = 0;
    }

    @Override
    public File getSchemFile() {
        return schemFile;
    }

    @Override
    public int getCoolDown() {
        return coolDown;
    }

    @Override
    public double getHologramLocationX() {
        return 0;
    }

    @Override
    public double getHologramLocationY() {
        return 0;
    }

    @Override
    public double getHologramLocationZ() {
        return 0;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public Location getHologramLocation(World world) {
        return new Location(world, getHologramLocationX(), getHologramLocationY(), getHologramLocationZ());
    }
}

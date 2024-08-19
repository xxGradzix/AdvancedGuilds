package me.xxgradzix.advancedclans.guildshideoutsystem.upgrades;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BlackSmithUpgradePattern implements UpgradePattern {

    private final File schemFile;
    private final int coolDown;

    private final int price;

    public BlackSmithUpgradePattern(JavaPlugin plugin) {
        this.schemFile = new File(plugin.getDataFolder().getAbsolutePath() + "/blacksmith-upgrade.schem");
        this.price = 1_000;
        this.coolDown = 20;
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
        return 27.0;
    }

    @Override
    public double getHologramLocationY() {
        return 95.5;
    }

    @Override
    public double getHologramLocationZ() {
        return -50.0;
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

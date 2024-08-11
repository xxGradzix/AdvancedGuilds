package me.xxgradzix.advancedclans.guildshideoutsystem.upgrades;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class BlackSMithUpgrade implements UpgradePattern {

    private final File schemFile;
    private final int coolDown;

    private final int price;

    public BlackSMithUpgrade(JavaPlugin plugin) {
        this.schemFile = new File(plugin.getDataFolder().getAbsolutePath() + "/blacksmith-upgrade.schem");
        this.price = 1_000;
        this.coolDown = 90;
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
        return -234.0;
    }

    @Override
    public double getHologramLocationY() {
        return 101.5;
    }

    @Override
    public double getHologramLocationZ() {
        return 88.0;
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

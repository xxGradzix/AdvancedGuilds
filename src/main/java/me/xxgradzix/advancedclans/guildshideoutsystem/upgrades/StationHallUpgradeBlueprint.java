package me.xxgradzix.advancedclans.guildshideoutsystem.upgrades;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class StationHallUpgradeBlueprint implements UpgradeBlueprint {

    private final File schemFile;
    private final int coolDown;

    private final int price;

    public StationHallUpgradeBlueprint(JavaPlugin plugin) {
        this.schemFile = new File(plugin.getDataFolder().getAbsolutePath() + "/upgrade-station-hall.schem");
        this.price = 10_000;
        this.coolDown = 10 * 60 * 60;
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
        return 13.5;
    }

    @Override
    public double getHologramLocationY() {
        return 99.5;
    }

    @Override
    public double getHologramLocationZ() {
        return -43.5;
    }

    @Override
    public int getPrice() {
        return price;
    }

    @Override
    public Location getHologramLocation(World world) {
        return new org.bukkit.Location(world, getHologramLocationX(), getHologramLocationY(), getHologramLocationZ());
    }
}

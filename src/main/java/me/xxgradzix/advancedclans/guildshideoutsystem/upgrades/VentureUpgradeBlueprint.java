package me.xxgradzix.advancedclans.guildshideoutsystem.upgrades;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class VentureUpgradeBlueprint implements UpgradeBlueprint {

    private final File schemFile;
    private final int coolDown;

    private final int price;

    public VentureUpgradeBlueprint(JavaPlugin plugin) {
        this.schemFile = new File(plugin.getDataFolder().getAbsolutePath() + "/venture_station.schem");
        this.price = 1_000;
        this.coolDown = 5 * 60 * 60;
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
        return 35.0;
    }

    @Override
    public double getHologramLocationY() {
        return 95.5;
    }

    @Override
    public double getHologramLocationZ() {
        return -38.0;
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

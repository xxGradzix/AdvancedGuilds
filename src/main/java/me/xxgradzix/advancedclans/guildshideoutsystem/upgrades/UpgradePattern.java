package me.xxgradzix.advancedclans.guildshideoutsystem.upgrades;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;

public interface UpgradePattern {

    File getSchemFile();

    int getCoolDown();

    Location getHologramLocation(World world);
    double getHologramLocationX();
    double getHologramLocationY();
    double getHologramLocationZ();

    int getPrice();
}

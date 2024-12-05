package me.xxgradzix.advancedclans.guildshideoutsystem.upgrades;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;

public class ResetHideoutBlueprint implements UpgradeBlueprint {
    private static final File file = new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/guild.schem");

    @Override
    public File getSchemFile() {
        return file;
    }

    @Override
    public int getCoolDown() {
        return 0;
    }

    @Override
    public Location getHologramLocation(World world) {
        return null;
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
        return 0;
    }
}

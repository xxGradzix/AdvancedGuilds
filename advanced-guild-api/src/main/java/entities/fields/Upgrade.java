package entities.fields;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.List;

public interface Upgrade {

    public File getSchemFile();

    public int getCoolDown();
    public int getPrice();
    public double getHologramLocationX();
    public double getHologramLocationY();

    public double getHologramLocationZ();
    public List<String> getLoreReview();
    public String getUpgradeName();

    public Location getHologramLocation(World world);

}

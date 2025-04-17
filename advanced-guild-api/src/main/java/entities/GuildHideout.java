package entities;

import com.xxgradzix.advancedguildsapi.events.hideout.HideoutLevelChangeEvent;
import com.xxgradzix.advancedguildsapi.events.hideout.HideoutPointsChangeEvent;
import entities.fields.Upgrade;
import entities.fields.UpgradeInfoHolder;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface GuildHideout {

    void setClanTag(Clan clanImpl);

    UpgradeInfoHolder getUpgradeHolder(Upgrade upgrade);

    void setUpgradeHolder(UpgradeInfoHolder holder);

    boolean hasBoughtUpgrade(Upgrade upgrade);


    boolean hasFinishedUpgrade(Upgrade upgrade);

    void reset();

    Location getTeleportLocation();

    int getMaxPlayers();
    int getMaxLives();

    int getMaxShieldDays();

    LocalDateTime getShieldEndDateTime();
    void supplyShieldByHours(int hours);

    int getLowerLevelCap();

    int getLossLevelCap() ;

    int getUpperLevelCap();

    void updateHideoutLevel();

    boolean upgradeHideout();
    void addExperience(int xp);
    void removeExperience(int xp);

    String getHideoutID();
    int getHideoutLevel();

    String getClanTag();

    Location getEntryBlockLocation();

    HashMap<Upgrade, UpgradeInfoHolder> getHideoutUpgrades();

    void setEntryBlockLocation(Location location);

    int getHideoutExperience();
}

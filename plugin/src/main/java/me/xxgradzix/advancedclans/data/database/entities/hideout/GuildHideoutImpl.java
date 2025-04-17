package me.xxgradzix.advancedclans.data.database.entities.hideout;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import entities.Clan;
import entities.GuildHideout;
import entities.fields.Upgrade;
import entities.fields.UpgradeInfoHolder;
import lombok.*;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.hideout.fields.UpgradeInfoHolderImpl;
import me.xxgradzix.advancedclans.data.database.persister.*;
import com.xxgradzix.advancedguildsapi.events.hideout.HideoutLevelChangeEvent;
import com.xxgradzix.advancedguildsapi.events.hideout.HideoutPointsChangeEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "advancedguilds_guild_hideout")
public class GuildHideoutImpl implements GuildHideout {

    private static final AdvancedGuilds plugin = AdvancedGuilds.instance;

    /** FIELDS **/

    @Getter
    @DatabaseField(id = true, unique = true)
    private String hideoutID;

    @Getter
    @Setter
    @DatabaseField
    private int hideoutLevel;

    @Getter
    @Setter
    @DatabaseField
    private int hideoutExperience;

    @Getter
    @Setter
    @DatabaseField
    private int currentLives;

    @DatabaseField(persisterClass = LocalDateTimeClassPersister.class, columnDefinition = "LONGBLOB")
    private LocalDateTime shieldEndDateTime;

    @Getter
    @DatabaseField(persisterClass = UpgradeInfoHolderPersisterMap.class, columnDefinition = "LONGBLOB")
    private HashMap<Upgrade, UpgradeInfoHolder> hideoutUpgrades;

    @Getter
    @Setter
    @DatabaseField(persisterClass = LocationPersister.class, columnDefinition = "LONGBLOB")
    private Location entryBlockLocation;

    @Getter
    @DatabaseField
    private String clanTag;


    /** CONSTRUCTORS **/

    public GuildHideoutImpl(String hideOutWorldName) {
        this.hideoutID = hideOutWorldName;
        this.hideoutUpgrades = new HashMap<>();

        this.hideoutLevel = 1;
        this.currentLives = getMaxLives();
        this.shieldEndDateTime = LocalDateTime.now();

        clanTag = null;
        entryBlockLocation = null;
    }

    /** METHODS **/

    @Override
    public void setClanTag(Clan clanImpl) {
        this.clanTag = clanImpl.getTag();
    }

    @Override
    public UpgradeInfoHolder getUpgradeHolder(Upgrade upgradeImpl) {
        return hideoutUpgrades.computeIfAbsent(upgradeImpl, k -> new UpgradeInfoHolderImpl((long) -1, false, upgradeImpl));
    }

    @Override
    public void setUpgradeHolder(UpgradeInfoHolder holder) {
        hideoutUpgrades.put(holder.getUpgrade(), holder);
    }

    @Override
    public boolean hasBoughtUpgrade(Upgrade upgradeImpl) {
        UpgradeInfoHolder holder = hideoutUpgrades.get(upgradeImpl);
        if(holder == null) return false;
        return holder.isBought();
    }

    @Override
    public boolean hasFinishedUpgrade(Upgrade upgradeImpl) {
        UpgradeInfoHolder holder = hideoutUpgrades.get(upgradeImpl);
        if(holder == null) return false;
        return holder.isFinished();
    }

    @Override
    public void reset() {
        hideoutUpgrades = new HashMap<>();
        hideoutLevel = 1;
        hideoutExperience = 0;
        currentLives = getMaxLives();
        shieldEndDateTime = LocalDateTime.now();
        clanTag = null;
    }

    @Override
    public Location getTeleportLocation() {
        return new Location(Bukkit.getWorld(hideoutID), 0.5, 100, 0.5, 180, 0);
    }

    @Override
    public int getMaxPlayers() {
        return 8 + (hideoutLevel * 4);
    }

    @Override
    public int getMaxLives() {
        return hideoutLevel;
    }

    @Override
    public int getMaxShieldDays() {
        return hideoutLevel;
    }

    @Override
    public LocalDateTime getShieldEndDateTime() {
        return shieldEndDateTime.isBefore(LocalDateTime.now()) ? LocalDateTime.now() : shieldEndDateTime;
    }

    @Override
    public void supplyShieldByHours(int hours) {
        LocalDateTime newShieldEndDateTime = getShieldEndDateTime();
        newShieldEndDateTime = newShieldEndDateTime.plusHours(hours);
        shieldEndDateTime = newShieldEndDateTime;
    }

    private static final List<Integer> levelCaps = new ArrayList<Integer>() {{
        add(0, 0);
        add(1, 0);
        add(2, 1000);
        add(3, 2000);
        add(4, 3000);
        add(5, 4000);
    }};

    @Override
    public int getLowerLevelCap() {
        return (int) (levelCaps.get(hideoutLevel));
    }

    @Override
    public int getLossLevelCap() {
        return (int) (getLowerLevelCap() - ((getUpperLevelCap() - getLowerLevelCap()) * 0.2));
    }

    @Override
    public int getUpperLevelCap() {
        try {
            return levelCaps.get(hideoutLevel + 1);
        } catch (IndexOutOfBoundsException e) {
            return levelCaps.getLast();
        }
    }

    @Override
    public void updateHideoutLevel() {
        int currentXP = this.hideoutExperience;
        int newLevel = this.hideoutLevel;

        if(getLossLevelCap() > currentXP) {
            newLevel--;

            HideoutLevelChangeEvent event = new HideoutLevelChangeEvent(this, this.hideoutLevel, newLevel);

            Bukkit.getPluginManager().callEvent(event);

            newLevel = event.getNewLevel();

            this.hideoutLevel = newLevel;
            return;
        }

    }

    @Override
    public boolean upgradeHideout() {

        if(this.hideoutLevel >= 5) return false;

        int nextLevelCap = levelCaps.get(this.hideoutLevel + 1);

        if(this.hideoutExperience < nextLevelCap) return false;

        int newLevel = this.hideoutLevel + 1;

        HideoutLevelChangeEvent event = new HideoutLevelChangeEvent(this, this.hideoutLevel, newLevel);

        Bukkit.getPluginManager().callEvent(event);

        newLevel = event.getNewLevel();

        this.hideoutLevel = newLevel;

        return true;

    }

    @Override
    public void addExperience(int xp) {

        int newExperience = this.hideoutExperience;

        int nextLevelCap = getUpperLevelCap();

        newExperience += xp;

        HideoutPointsChangeEvent event = new HideoutPointsChangeEvent(this, this.hideoutExperience, newExperience);

        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()) return;

        newExperience = event.getNewPoints();

        if(newExperience >= nextLevelCap) {
            newExperience = nextLevelCap;
        }

        this.hideoutExperience = newExperience;

        updateHideoutLevel();
    }

    @Override
    public void removeExperience(int xp) {

        int newExperience = this.hideoutExperience;

        newExperience -= xp;

        HideoutPointsChangeEvent event = new HideoutPointsChangeEvent(this, this.hideoutExperience, newExperience);

        Bukkit.getPluginManager().callEvent(event);

        if(event.isCancelled()) return;

        newExperience = event.getNewPoints();

        if(newExperience < 0) {
            newExperience = 0;
        }

        this.hideoutExperience = newExperience;

        updateHideoutLevel();
    }

}

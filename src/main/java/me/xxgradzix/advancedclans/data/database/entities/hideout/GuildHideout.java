package me.xxgradzix.advancedclans.data.database.entities.hideout;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.persister.*;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DatabaseTable(tableName = "advancedguilds_guild_hideout")
public class GuildHideout {


    @Getter
    @DatabaseField(id = true, unique = true)
    private String worldName;

    @DatabaseField(persisterClass = UpgradeInfoHolderPersisterMap.class, columnDefinition = "LONGBLOB")
    private HashMap<Upgrade, UpgradeInfoHolder> hideoutUpgrades;

    @Getter
    @Setter
    @DatabaseField(persisterClass = LocationPersister.class, columnDefinition = "LONGBLOB")
    private Location entryBlockLocation;

    @DatabaseField
    private String clanTag;

    public GuildHideout(String hideOutWorldName) {
        this.worldName = hideOutWorldName;
        this.hideoutUpgrades = new HashMap<>();
        clanTag = null;
        entryBlockLocation = null;
    }

    public void setClanTag(Clan clan) {
        this.clanTag = clan.getTag();
    }

    public UpgradeInfoHolder getUpgradeHolder(Upgrade upgrade) {
        return hideoutUpgrades.computeIfAbsent(upgrade, k -> new UpgradeInfoHolder((long) -1, false));
    }

    public void setUpgradeHolder(Upgrade upgrade, UpgradeInfoHolder holder) {
        hideoutUpgrades.put(upgrade, holder);
    }

    public boolean hasBoughtUpgrade(Upgrade upgrade) {
        UpgradeInfoHolder holder = hideoutUpgrades.get(upgrade);
        if(holder == null) return false;
        return holder.isBought();
    }
    public long getTimeToCompletionSeconds(Upgrade upgrade) throws UpgradeWasNotBoughtException {
        UpgradeInfoHolder holder = hideoutUpgrades.get(upgrade);
        if(holder == null) return -1;
        return holder.getTimeToCompletionSeconds();
    }

    public boolean hasFinishedUpgrade(Upgrade upgrade) {

        UpgradeInfoHolder holder = hideoutUpgrades.get(upgrade);

        if(holder == null) return false;

        return holder.isFinished();
    }

    public void reset() {
        hideoutUpgrades = new HashMap<>();
        clanTag = null;
    }

    public Location getTeleportLocation() {
        return new Location(Bukkit.getWorld(worldName), 0.5, 100, 0.5, 180, 0);
    }

    public enum Upgrade {
        STATION_HALL, BLACKSMITH, WHICH, RESET, SORCERER, VENTURE, OUTPOST_PODEST, OUTPOST_HUT
    }

}

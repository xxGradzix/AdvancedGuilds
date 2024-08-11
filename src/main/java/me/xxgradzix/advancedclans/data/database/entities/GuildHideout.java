package me.xxgradzix.advancedclans.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import me.xxgradzix.advancedclans.data.database.persister.*;

import java.util.*;

//@Data
//@AllArgsConstructor
@AllArgsConstructor
@NoArgsConstructor
@Data
@DatabaseTable(tableName = "advancedguilds_guild_hideout")
public class GuildHideout {


    public void reset() {
        hideoutUpgrades = new HashMap<>();
        clan = null;
    }

    public enum Upgrade {
        STATION_HALL, BLACKSMITH, WHICH, SORCERER
    }

    @Getter
    @DatabaseField(id = true, unique = true)
    private String worldName;
//
//    @DatabaseField(persisterClass = UpgradeBooleanMap.class)
//    private HashMap<Upgrade, Boolean> hideoutUpgrades;

//    @DatabaseField(persisterClass = UpgradeEntitySet.class)
//    private Set<UpgradeEntity> hideoutUpgrades;

    @DatabaseField(persisterClass = UpgradeInfoHolderPersisterMap.class)
    private HashMap<Upgrade, UpgradeInfoHolder> hideoutUpgrades;

    @DatabaseField(foreign = true)
    private Clan clan;


    public UpgradeInfoHolder getUpgradeHolder(Upgrade upgrade) {
        return hideoutUpgrades.get(upgrade);
    }
    public void setUpgradeHolder(Upgrade upgrade, UpgradeInfoHolder holder) {
        hideoutUpgrades.put(upgrade, holder);
    }

    public boolean hasBoughtUpgrade(Upgrade upgrade) {

        UpgradeInfoHolder holder = hideoutUpgrades.get(upgrade);

        if(holder == null) return false;

        return holder.isBought();
    }

    public boolean hasFinishedUpgrade(Upgrade upgrade) {

        UpgradeInfoHolder holder = hideoutUpgrades.get(upgrade);

        if(holder == null) return false;

        return holder.isFinished();
    }



}

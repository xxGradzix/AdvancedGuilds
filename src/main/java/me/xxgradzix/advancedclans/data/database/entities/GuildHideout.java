package me.xxgradzix.advancedclans.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import me.xxgradzix.advancedclans.data.database.entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.persister.*;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;

import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DatabaseTable(tableName = "advancedguilds_guild_hideout")
public class GuildHideout {


    @Getter
    @DatabaseField(id = true, unique = true)
    private String worldName;

    @DatabaseField(persisterClass = UpgradeInfoHolderPersisterMap.class)
    private HashMap<Upgrade, UpgradeInfoHolder> hideoutUpgrades;

    @DatabaseField
    private String clanTag;

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

    public enum Upgrade {
        STATION_HALL, BLACKSMITH, WHICH, SORCERER
    }

}

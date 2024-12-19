package me.xxgradzix.advancedclans.data.database.entities.hideout;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.persister.*;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@DatabaseTable(tableName = "advancedguilds_guild_hideout")
public class GuildHideout {

    private static final AdvancedGuilds plugin = AdvancedGuilds.instance;

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
        STATION_HALL(new File(plugin.getDataFolder().getAbsolutePath() + "/upgrade-station-hall.schem"),
                10 * 60 * 60,
                1000,
                13.5,
                99.5,
                -43.5
                ),
        BLACKSMITH(new File(plugin.getDataFolder().getAbsolutePath() + "/blacksmith-upgrade.schem"),
                5 * 60 * 60,
                1000,
                27.0,
                95.5,
                -50.0
                ),
        WHICH(new File(plugin.getDataFolder().getAbsolutePath() + "/upgrade-which.schem"),
                5 * 60 * 60,
                1000,
                40.5,
                95.5,
                -50.0
                ),
        RESET(new File(plugin.getDataFolder().getAbsolutePath() + "/guild.schem"),
                0,
                0,
                0,
                0,
                0
                ),
        SORCERER(new File(plugin.getDataFolder().getAbsolutePath() + "/sorcery-station.schem"),
                5 * 60 * 60,
                1000,
                26.0,
                95.5,
                -38.0
                ),
        VENTURE(new File(plugin.getDataFolder().getAbsolutePath() + "/venture_station.schem"),
                5 * 60 * 60,
                1000,
                35,
                95.5,
                -38),
        OUTPOST_PODEST(new File(plugin.getDataFolder().getAbsolutePath() + "/outpost-podest.schem"),
                0,
                0,
                0,
                0,
                0
                ),
        OUTPOST_HUT(new File(plugin.getDataFolder().getAbsolutePath() + "/outpost-hut.schem"),
                0,
                0,
                0,
                0,
                0
                ),
        ARENA(new File(plugin.getDataFolder().getAbsolutePath() + "/upgrade-arena.schem"),
                20 * 60 * 60,
                1000,
                0.5,
                99.5,
                -54.5
                );

        @Getter
        private File schemFile;
        @Getter
        private int coolDown;
        @Getter
        private int price;
        @Getter
        private double hologramLocationX;
        @Getter
        private double hologramLocationY;
        @Getter
        private double hologramLocationZ;


        Upgrade(File schemFile, int coolDown, int price, double hologramLocationX, double hologramLocationY, double hologramLocationZ) {
            this.schemFile = schemFile;
            this.coolDown = coolDown;
            this.price = price;
            this.hologramLocationX = hologramLocationX;
            this.hologramLocationY = hologramLocationY;
            this.hologramLocationZ = hologramLocationZ;
        }

        public Location getHologramLocation(World world){
            return new Location(world, getHologramLocationX(), getHologramLocationY(), getHologramLocationZ());
        }

    }

}

package me.xxgradzix.advancedclans.data.database.entities.hideout.storage;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.persister.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Set;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "hideout_guild_storage")
public class GuildlStorageEntity {


    @Getter
    @DatabaseField(id = true, unique = true)
    private String hideoutName;


    @Getter
    @Setter
    @DatabaseField(persisterClass = ItemStackArrayPersister.class, columnDefinition = "LONGBLOB")
    private ItemStack[] inventory1;

    @Getter
    @Setter
    @DatabaseField(persisterClass = ItemStackArrayPersister.class, columnDefinition = "LONGBLOB")
    private ItemStack[] inventory2;

    @Getter
    @Setter
    @DatabaseField(persisterClass = ItemStackArrayPersister.class, columnDefinition = "LONGBLOB")
    private ItemStack[] inventory3;

    @Getter
    @Setter
    @DatabaseField(persisterClass = ItemStackArrayPersister.class, columnDefinition = "LONGBLOB")
    private ItemStack[] inventory4;

    @Getter
    @Setter
    @DatabaseField(persisterClass = ItemStackArrayPersister.class, columnDefinition = "LONGBLOB")
    private ItemStack[] inventory5;

    @Getter
    @Setter
    @DatabaseField(persisterClass = UUIDSetPersister.class, columnDefinition = "LONGBLOB")
    private Set<UUID> inventory1Access;

    @Getter
    @Setter
    @DatabaseField(persisterClass = UUIDSetPersister.class, columnDefinition = "LONGBLOB")
    private Set<UUID> inventory2Access;

    @Getter
    @Setter
    @DatabaseField(persisterClass = UUIDSetPersister.class, columnDefinition = "LONGBLOB")
    private Set<UUID> inventory3Access;

    @Getter
    @Setter
    @DatabaseField(persisterClass = UUIDSetPersister.class, columnDefinition = "LONGBLOB")
    private Set<UUID> inventory4Access;

    @Getter
    @Setter
    @DatabaseField(persisterClass = UUIDSetPersister.class, columnDefinition = "LONGBLOB")
    private Set<UUID> inventory5Access;

    public GuildlStorageEntity(String hideoutName) {
        this.hideoutName = hideoutName;
    }

}

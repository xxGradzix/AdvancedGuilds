package me.xxgradzix.advancedclans.data.database.entities.hideout.storage;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.xxgradzix.advancedclans.data.database.persister.Inventory2;
import me.xxgradzix.advancedclans.data.database.persister.InventoryPersister;
import me.xxgradzix.advancedclans.data.database.persister.ItemStackArrayPersister;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "hideout_personal_storage")
public class PersonalStorageEntity {


    @Getter
    @DatabaseField(id = true, unique = true)
    private UUID id;

    @Getter
    @DatabaseField(persisterClass = ItemStackArrayPersister.class, columnDefinition = "LONGBLOB")
    private ItemStack[] inventory;

    public PersonalStorageEntity(Player player, Inventory inventory) {
        this.id = player.getUniqueId();
        this.inventory = inventory.getContents();

    }

}

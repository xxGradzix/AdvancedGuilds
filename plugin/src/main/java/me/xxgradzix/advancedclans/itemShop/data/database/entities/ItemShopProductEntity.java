package me.xxgradzix.advancedclans.itemShop.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.itemShop.data.database.persisters.ItemStackPersister;
import org.bukkit.inventory.ItemStack;

@DatabaseTable(tableName = "advancedguilds_itemshop_product")
@Data
@NoArgsConstructor
public class ItemShopProductEntity {



    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(persisterClass = ItemStackPersister.class, columnDefinition = "BLOB")
    private ItemStack product;

//    @DatabaseField
//    private int cost;
    @DatabaseField
    private int slot;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "category_id")
    private ItemShopCategoryEntity category;

//    @DatabaseField
//    private ShopType shopType;

    public ItemShopProductEntity(ItemStack product,  int slot, ItemShopCategoryEntity category) {
        this.product = product;
//        this.cost = cost;
        this.slot = slot;
        this.category = category;
    }
}

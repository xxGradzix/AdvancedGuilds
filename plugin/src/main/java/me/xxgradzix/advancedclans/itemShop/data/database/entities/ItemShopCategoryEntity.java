package me.xxgradzix.advancedclans.itemShop.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.itemShop.data.database.persisters.ItemStackPersister;
import org.bukkit.inventory.ItemStack;

@DatabaseTable(tableName = "advancedguilds_itemshop_category")
@Getter
@Setter
@NoArgsConstructor
public class ItemShopCategoryEntity {

    @DatabaseField(generatedId = true, columnName = "category_id" )
    private Long id;

    @DatabaseField
    private String name;

    @DatabaseField
    private int categorySlot;

    @DatabaseField(persisterClass = ItemStackPersister.class, columnDefinition = "BLOB")
    private ItemStack categoryIcon;

//    public ItemShopCategoryEntity(String name) {
//        this.name = name;
////        this.shopType = shopType;
//    }

    public ItemShopCategoryEntity(String name, int categorySlot, ItemStack categoryIcon) {
        this.name = name;
        this.categorySlot = categorySlot;
        this.categoryIcon = categoryIcon;
    }
}



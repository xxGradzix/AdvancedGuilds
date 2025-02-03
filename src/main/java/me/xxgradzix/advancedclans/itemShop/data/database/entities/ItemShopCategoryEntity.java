package me.xxgradzix.advancedclans.itemShop.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.*;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;

@DatabaseTable(tableName = "advancedguilds_itemshop_category")
@Getter
@Setter
@NoArgsConstructor
public class ItemShopCategoryEntity {

    @DatabaseField(generatedId = true, columnName = "category_id" )
    private Long id;

    @DatabaseField
    private String name;

    @DatabaseField(columnName = "shopType")
    private ShopType shopType;

    public ItemShopCategoryEntity(String name, ShopType shopType) {
        this.name = name;
        this.shopType = shopType;
    }
}



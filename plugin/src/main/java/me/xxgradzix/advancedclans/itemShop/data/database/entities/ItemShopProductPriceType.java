package me.xxgradzix.advancedclans.itemShop.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;

@DatabaseTable(tableName = "advancedguilds_itemshop_product_price_type")
@Data
@NoArgsConstructor
public class ItemShopProductPriceType {

    @DatabaseField(generatedId = true)
    private Long id;

    @DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "product_id")
    private ItemShopProductEntity product;

    @DatabaseField
    private ShopType shopType;

    @DatabaseField
    private int price;

    public ItemShopProductPriceType(ItemShopProductEntity product, ShopType shopType, int price) {
        this.product = product;
        this.shopType = shopType;
        this.price = price;
    }

    public ItemShopProductEntity getProduct() {
        return product;
    }

    public void setProduct(ItemShopProductEntity product) {
        this.product = product;
    }

    public ShopType getShopType() {
        return shopType;
    }

    public void setShopType(ShopType shopType) {
        this.shopType = shopType;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
package me.xxgradzix.advancedclans.itemShop.data.database.managers;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductPriceType;

public class ItemShopProductPriceTypeRepository extends EntityRepository<ItemShopProductPriceType, Long> {

    public ItemShopProductPriceTypeRepository(ConnectionSource connectionSource) {
        super(connectionSource, ItemShopProductPriceType.class);
    }

}


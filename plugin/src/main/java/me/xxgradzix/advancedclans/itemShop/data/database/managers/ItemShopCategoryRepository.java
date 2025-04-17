package me.xxgradzix.advancedclans.itemShop.data.database.managers;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;

public class ItemShopCategoryRepository extends EntityRepository<ItemShopCategoryEntity, Long> {

    public ItemShopCategoryRepository(ConnectionSource connectionSource) {
        super(connectionSource, ItemShopCategoryEntity.class);
    }
}
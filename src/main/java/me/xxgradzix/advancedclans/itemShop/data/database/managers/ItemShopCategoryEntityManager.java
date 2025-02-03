package me.xxgradzix.advancedclans.itemShop.data.database.managers;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;

public class ItemShopCategoryEntityManager extends EntityRepository<ItemShopCategoryEntity, Long> {

    public ItemShopCategoryEntityManager(ConnectionSource connectionSource) {
        super(connectionSource, ItemShopCategoryEntity.class);
    }
}
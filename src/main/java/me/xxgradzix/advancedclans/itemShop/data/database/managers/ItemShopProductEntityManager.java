package me.xxgradzix.advancedclans.itemShop.data.database.managers;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;

public class ItemShopProductEntityManager extends EntityRepository<ItemShopProductEntity, Long> {
    public ItemShopProductEntityManager(ConnectionSource connectionSource) {
        super(connectionSource, ItemShopProductEntity.class);
    }
}
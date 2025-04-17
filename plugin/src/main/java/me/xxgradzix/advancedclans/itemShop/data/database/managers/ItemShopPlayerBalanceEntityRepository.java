package me.xxgradzix.advancedclans.itemShop.data.database.managers;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopPlayerBalanceEntity;

import java.util.UUID;

public class ItemShopPlayerBalanceEntityRepository extends EntityRepository<ItemShopPlayerBalanceEntity, UUID> {

    public ItemShopPlayerBalanceEntityRepository(ConnectionSource connectionSource) {
        super(connectionSource, ItemShopPlayerBalanceEntity.class);
    }
}
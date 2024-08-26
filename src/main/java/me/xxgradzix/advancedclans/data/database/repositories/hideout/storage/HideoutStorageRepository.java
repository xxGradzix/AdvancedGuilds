package me.xxgradzix.advancedclans.data.database.repositories.hideout.storage;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.entities.hideout.storage.GuildlStorageEntity;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;

public class HideoutStorageRepository extends EntityRepository<GuildlStorageEntity, String> {

    public HideoutStorageRepository(ConnectionSource connectionSource) {
        super(connectionSource, GuildlStorageEntity.class);
    }
}
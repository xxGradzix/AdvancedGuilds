package me.xxgradzix.advancedclans.data.database.repositories.hideout.storage;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.entities.hideout.storage.PersonalStorageEntity;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;

import java.util.UUID;

public class PersonalStorageRepository extends EntityRepository<PersonalStorageEntity, UUID> {

    public PersonalStorageRepository(ConnectionSource connectionSource) {
        super(connectionSource, PersonalStorageEntity.class);
    }
}
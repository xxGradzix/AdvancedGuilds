package me.xxgradzix.advancedclans.data.database.services.hideout;

import me.xxgradzix.advancedclans.data.database.entities.hideout.storage.GuildlStorageEntity;
import me.xxgradzix.advancedclans.data.database.entities.hideout.storage.PersonalStorageEntity;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.storage.HideoutStorageRepository;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.storage.PersonalStorageRepository;

import java.sql.SQLException;
import java.util.List;

public class StorageEntityDataManager {

    private static PersonalStorageRepository personalStorageRepository;
    private static HideoutStorageRepository hideoutStorageRepository;

    public StorageEntityDataManager(PersonalStorageRepository personalStorageRepository, HideoutStorageRepository hideoutStorageRepository) {
        StorageEntityDataManager.personalStorageRepository = personalStorageRepository;
        StorageEntityDataManager.hideoutStorageRepository = hideoutStorageRepository;
    }

    public static void createOrUpdatePersonalStorageEntity(PersonalStorageEntity personalStorageEntity) {
        try {
            personalStorageRepository.createOrUpdateEntity(personalStorageEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void createOrUpdateGuildStorageEntity(GuildlStorageEntity guildlStorageEntity) {
        try {
            hideoutStorageRepository.createOrUpdateEntity(guildlStorageEntity);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<PersonalStorageEntity> getAllPersonalStorageEntities() {
        try {
            return personalStorageRepository.getAllEntities();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<GuildlStorageEntity> getAllGuildStorageEntities() {
        try {
            return hideoutStorageRepository.getAllEntities();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




}

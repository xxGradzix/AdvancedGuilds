package me.xxgradzix.advancedclans.data.database.services;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.repositories.ClanEntityRepository;
import me.xxgradzix.advancedclans.data.database.repositories.UserEntityRepository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ClanAndUserDataManager {

    private static ClanEntityRepository clanEntityRepository;
    private static UserEntityRepository userEntityRepository;

    private static final HashMap<String, Clan> clansData = new HashMap<>();
    private static final HashMap<UUID, User> userData = new HashMap<>();

    public ClanAndUserDataManager(ClanEntityRepository clanEntityRepository, UserEntityRepository userEntityRepository) {
        ClanAndUserDataManager.clanEntityRepository = clanEntityRepository;
        ClanAndUserDataManager.userEntityRepository = userEntityRepository;
    }

    public static User getCachedUser(UUID uuid) {
//        try {
            if(uuid == null) return null;
//            return userEntityRepository.getEntityById(uuid);
            return userData.get(uuid);
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static Clan getCachedClan(String tag) {
//        try {
            if(tag == null) return null;
//            return clanEntityRepository.getEntityById(tag);
            return clansData.get(tag.toUpperCase());
//        } catch (SQLException e) {
//            throw new RuntimeException(e);
//        }
    }

    public static void updateClan(Clan clan) {
        try {
            clansData.put(clan.getTag().toUpperCase(), clan);

            clanEntityRepository.createOrUpdateEntity(clan);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUser(User user) {
        try {
            userEntityRepository.createOrUpdateEntity(user);
            userData.put(user.getUuid(), user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteClan(Clan clan) {
        try {
            clanEntityRepository.deleteEntity(clan);
            clansData.remove(clan.getTag().toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void loadAllClans() {
        try {
            for(Clan clan : clanEntityRepository.getAllEntities()) {
                clansData.put(clan.getTag().toUpperCase(), clan);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void loadAllUsers() {
        try {
            for (User user : userEntityRepository.getAllEntities()) {
                userData.put(user.getUuid(), user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static Collection<Clan> getAllCachedClans() {

        return clansData.values();
    }

    public static Collection<User> getAllCachedUsers() {
        return userData.values();
    }
}

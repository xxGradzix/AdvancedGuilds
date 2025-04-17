package me.xxgradzix.advancedclans.data.database.services.clansCore;

import entities.Clan;
import entities.User;
import me.xxgradzix.advancedclans.data.database.entities.clan.ClanImpl;
import me.xxgradzix.advancedclans.data.database.entities.clan.UserImpl;
import me.xxgradzix.advancedclans.data.database.repositories.clans.ClanEntityRepository;
import me.xxgradzix.advancedclans.data.database.repositories.clans.UserEntityRepository;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.UUID;

public class ClanAndUserDataService {

    private static ClanEntityRepository clanEntityRepository;
    private static UserEntityRepository userEntityRepository;

    private static final HashMap<String, Clan> clansData = new HashMap<>();
    private static final HashMap<UUID, User> userData = new HashMap<>();

    public ClanAndUserDataService(ClanEntityRepository clanEntityRepository, UserEntityRepository userEntityRepository) {
        ClanAndUserDataService.clanEntityRepository = clanEntityRepository;
        ClanAndUserDataService.userEntityRepository = userEntityRepository;
    }

    public static User getCachedUser(UUID uuid) {
            if(uuid == null) return null;
            return userData.get(uuid);
    }

    public static Clan getCachedClan(String tag) {
            if(tag == null) return null;
            return clansData.get(tag.toUpperCase());
    }

    public static void updateClan(Clan clanImpl) {

        try {
            clansData.put(clanImpl.getTag().toUpperCase(), clanImpl);
            clanEntityRepository.createOrUpdateEntity(clanImpl);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateUser(User userImpl) {
        try {
            userEntityRepository.createOrUpdateEntity(userImpl);
            userData.put(userImpl.getUuid(), userImpl);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void deleteClan(Clan clanImpl) {
        try {
            clanEntityRepository.deleteEntity(clanImpl);
            clansData.remove(clanImpl.getTag().toUpperCase());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void loadAllClans() {
        try {
            for(Clan clanImpl : clanEntityRepository.getAllEntities()) {
                clansData.put(clanImpl.getTag().toUpperCase(), clanImpl);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public static void loadAllUsers() {
        try {
            for (User userImpl : userEntityRepository.getAllEntities()) {
                userData.put(userImpl.getUuid(), userImpl);
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

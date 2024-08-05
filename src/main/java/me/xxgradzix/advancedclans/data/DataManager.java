package me.xxgradzix.advancedclans.data;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.repositories.ClanEntityRepository;
import me.xxgradzix.advancedclans.data.database.repositories.UserEntityRepository;

import java.sql.SQLException;
import java.util.List;

public class DataManager {

    private final ClanEntityRepository clanEntityRepository;
    private final UserEntityRepository userEntityRepository;

    public DataManager(ClanEntityRepository clanEntityRepository, UserEntityRepository userEntityRepository) {
        this.clanEntityRepository = clanEntityRepository;
        this.userEntityRepository = userEntityRepository;
    }

    public void updateClan(Clan clan) {
        try {
            clanEntityRepository.createOrUpdateEntity(clan);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateUser(User user) {
        try {
            userEntityRepository.createOrUpdateEntity(user);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteClan(Clan clan) {
        try {
            clanEntityRepository.deleteEntity(clan);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createIfNotExist(Clan clan) {
        try {
            clanEntityRepository.createEntityIfNotExists(clan);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Clan> getAllClans() {
        try {
            return clanEntityRepository.getAllEntities();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createUser(User player) {
        try {
            userEntityRepository.createEntityIfNotExists(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    public List<User> getAllUsers() {
        try {
            return userEntityRepository.getAllEntities();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}

package me.xxgradzix.advancedclans.data.database.repositories.clans;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.entities.clan.User;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;

import java.util.UUID;

public class UserEntityRepository extends EntityRepository<User, UUID> {
    public UserEntityRepository(ConnectionSource connectionSource) {
        super(connectionSource, User.class);
    }
}

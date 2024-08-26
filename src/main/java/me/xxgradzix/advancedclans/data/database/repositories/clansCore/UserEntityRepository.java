package me.xxgradzix.advancedclans.data.database.repositories.clansCore;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;

import java.util.UUID;

public class UserEntityRepository extends EntityRepository<User, UUID> {
    public UserEntityRepository(ConnectionSource connectionSource) {
        super(connectionSource, User.class);
    }
}

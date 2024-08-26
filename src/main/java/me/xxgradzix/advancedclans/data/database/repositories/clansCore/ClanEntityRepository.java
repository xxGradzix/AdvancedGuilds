package me.xxgradzix.advancedclans.data.database.repositories.clansCore;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;

public class ClanEntityRepository extends EntityRepository<Clan, String> {
    public ClanEntityRepository(ConnectionSource connectionSource) {
        super(connectionSource, Clan.class);
    }

}

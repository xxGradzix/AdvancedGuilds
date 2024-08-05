package me.xxgradzix.advancedclans.data.database.repositories;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.entities.Clan;

public class ClanEntityRepository extends EntityRepository<Clan, String> {
    public ClanEntityRepository(ConnectionSource connectionSource) {
        super(connectionSource, Clan.class);
    }

}

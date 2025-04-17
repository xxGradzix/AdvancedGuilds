package me.xxgradzix.advancedclans.data.database.repositories.clans;

import com.j256.ormlite.support.ConnectionSource;
import entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.clan.ClanImpl;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;

public class ClanEntityRepository extends EntityRepository<Clan, String> {
    public ClanEntityRepository(ConnectionSource connectionSource) {
        super(connectionSource, Clan.class);
    }

}

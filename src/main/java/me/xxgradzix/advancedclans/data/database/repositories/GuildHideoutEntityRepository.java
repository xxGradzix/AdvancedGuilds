package me.xxgradzix.advancedclans.data.database.repositories;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;

public class GuildHideoutEntityRepository extends EntityRepository<GuildHideout, String> {
    public GuildHideoutEntityRepository(ConnectionSource connectionSource) {
        super(connectionSource, GuildHideout.class);
    }

}

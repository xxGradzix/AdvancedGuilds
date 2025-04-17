package me.xxgradzix.advancedclans.data.database.repositories.hideout;

import com.j256.ormlite.support.ConnectionSource;
import entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideoutImpl;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;

public class GuildHideoutEntityRepository extends EntityRepository<GuildHideout, String> {
    public GuildHideoutEntityRepository(ConnectionSource connectionSource) {
        super(connectionSource, GuildHideout.class);
    }

}

package me.xxgradzix.advancedclans.data.database.repositories.hideout.venture;

import com.j256.ormlite.support.ConnectionSource;
import me.xxgradzix.advancedclans.data.database.entities.hideout.venture.VentureReward;
import me.xxgradzix.advancedclans.data.database.repositories.EntityRepository;

public class VentureRewardRepository extends EntityRepository<VentureReward, Long> {

    public VentureRewardRepository(ConnectionSource connectionSource) {
        super(connectionSource, VentureReward.class);
    }
}

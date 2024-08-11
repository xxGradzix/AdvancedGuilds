package me.xxgradzix.advancedclans.data.database.entities;

public class UpgradeEntity {
    private final GuildHideout.Upgrade upgrade;

    private boolean bought;

//    private boolean finished;

    private Long timeOfCompletion;


    public UpgradeEntity(GuildHideout.Upgrade upgrade, Long timeOfCompletion, boolean bought) {
        this.upgrade = upgrade;
        this.timeOfCompletion = timeOfCompletion;
        this.bought = bought;
    }

    public GuildHideout.Upgrade getUpgrade() {
        return upgrade;
    }

    public boolean isBought() {
        return bought;
    }

    public Long getTimeOfCompletion() {
        return timeOfCompletion;
    }

    public boolean isFinished() {
        return System.currentTimeMillis() >= timeOfCompletion;
    }

}

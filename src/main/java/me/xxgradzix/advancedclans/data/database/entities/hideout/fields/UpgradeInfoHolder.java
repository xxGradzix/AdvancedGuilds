package me.xxgradzix.advancedclans.data.database.entities.hideout.fields;

import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;

import java.io.Serializable;

public class UpgradeInfoHolder implements Serializable {


    @Setter
    @Getter
    private boolean bought;

    @Getter
    private boolean forceFinish;

    @Setter
    @Getter
    private Long timeOfCompletion;

    @Getter
    private final GuildHideout.Upgrade upgrade;

    public UpgradeInfoHolder(Long timeOfCompletion, boolean bought, GuildHideout.Upgrade upgrade) {
        this.timeOfCompletion = timeOfCompletion;
        this.bought = bought;
        this.upgrade = upgrade;
        forceFinish = false;
    }

    public Long getTimeToCompletionSeconds() throws UpgradeWasNotBoughtException {
        if(!isBought()) throw new UpgradeWasNotBoughtException("Upgrade was not bought");
        return (timeOfCompletion - System.currentTimeMillis()) / 1000;
    }

    public boolean isFinished() {
        if(forceFinish) return true;
        if(!isBought()) return false;
        if(timeOfCompletion == null || timeOfCompletion < 0) return false;
        return System.currentTimeMillis() >= timeOfCompletion;
    }

    public void setFinished() {
        forceFinish = true;
    }
}

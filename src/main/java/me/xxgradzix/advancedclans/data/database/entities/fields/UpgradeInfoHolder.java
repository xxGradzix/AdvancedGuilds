package me.xxgradzix.advancedclans.data.database.entities.fields;

import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.exceptions.hideOuts.UpgradeWasNotBoughtException;

import java.io.Serializable;

@Setter
@Getter
public class UpgradeInfoHolder implements Serializable {

    private boolean bought;

    private boolean forceFinish;

    private Long timeOfCompletion;

    public UpgradeInfoHolder(Long timeOfCompletion, boolean bought) {
        this.timeOfCompletion = timeOfCompletion;
        this.bought = bought;
        forceFinish = false;
    }

    public Long getTimeToCompletionSeconds() throws UpgradeWasNotBoughtException {
        if(!isBought()) throw new UpgradeWasNotBoughtException("Upgrade was not bought");
        return (timeOfCompletion - System.currentTimeMillis()) / 1000;
    }
//    public Long getTimeToCompletionSeconds() throws UpgradeWasNotBoughtException {
//        if(!isBought()) throw new UpgradeWasNotBoughtException("Upgrade was not bought");
//        return (timeOfCompletion - System.currentTimeMillis());
//    }

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

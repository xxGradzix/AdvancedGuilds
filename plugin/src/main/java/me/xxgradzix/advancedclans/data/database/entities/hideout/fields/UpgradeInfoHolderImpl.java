package me.xxgradzix.advancedclans.data.database.entities.hideout.fields;

import entities.fields.Upgrade;
import entities.fields.UpgradeInfoHolder;
import lombok.Getter;
import lombok.Setter;
import com.xxgradzix.advancedguildsapi.exceptions.hideOuts.UpgradeWasNotBoughtException;

import java.io.Serializable;

public class UpgradeInfoHolderImpl implements UpgradeInfoHolder, Serializable {


    @Setter
    @Getter
    private boolean bought;

    @Getter
    private boolean forceFinish;

    @Setter
    @Getter
    private Long timeOfCompletion;

    @Getter
    private final Upgrade upgradeImpl;

    public UpgradeInfoHolderImpl(Long timeOfCompletion, boolean bought, Upgrade upgradeImpl) {
        this.timeOfCompletion = timeOfCompletion;
        this.bought = bought;
        this.upgradeImpl = upgradeImpl;
        forceFinish = false;
    }

    @Override
    public Upgrade getUpgrade() {
        return upgradeImpl;
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

package entities.fields;

import com.xxgradzix.advancedguildsapi.exceptions.hideOuts.UpgradeWasNotBoughtException;


public interface UpgradeInfoHolder {


    public void setBought(boolean bought);

    public void setTimeOfCompletion(Long timeOfCompletion);

    public boolean isBought();

    public boolean isForceFinish();

    public Long getTimeOfCompletion();

    public Upgrade getUpgrade();


    public Long getTimeToCompletionSeconds() throws UpgradeWasNotBoughtException;

    public boolean isFinished();

    public void setFinished();
}

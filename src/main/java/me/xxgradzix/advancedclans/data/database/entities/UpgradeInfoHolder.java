package me.xxgradzix.advancedclans.data.database.entities;

import lombok.Setter;

public class UpgradeInfoHolder {
    @Setter
    private boolean bought;

//    private boolean finished;
    @Setter
    private Long timeOfCompletion;


    public UpgradeInfoHolder(Long timeOfCompletion, boolean bought) {
        this.timeOfCompletion = timeOfCompletion;
        this.bought = bought;
    }

    public boolean isBought() {
        return bought;
    }

//    public Long getTimeOfCompletion() {
//        return timeOfCompletion;
//    }
    public Long getTimeTOCompletionSeconds() {
        return (timeOfCompletion - System.currentTimeMillis()) / 1000;
    }

    public Long getTimeOfCompletion() {
        return timeOfCompletion;
    }
    public boolean isFinished() {
        return System.currentTimeMillis() >= timeOfCompletion;
    }

}

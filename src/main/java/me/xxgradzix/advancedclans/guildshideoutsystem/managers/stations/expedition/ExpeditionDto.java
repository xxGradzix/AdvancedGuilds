package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition;

import lombok.Getter;
import lombok.Setter;

public class ExpeditionDto {

    public enum ExpeditionObjective {
        STONE, WOOD, CRYSTALS
    }

    private final double chance;

    @Getter
    @Setter
    private boolean forceFinish;

    @Getter
    private int expeditionLevel;

    @Getter
    private ExpeditionObjective objective;

    private final long cooldownTimeSeconds;

    private final long timeOfCompletionMills;

    public ExpeditionDto(double chance, int expeditionLevel, ExpeditionObjective objective, int cooldownTimeSeconds) {
        this.chance = chance;
        this.forceFinish = false;
        this.expeditionLevel = expeditionLevel;
        this.objective = objective;
        this.cooldownTimeSeconds = cooldownTimeSeconds;
        this.timeOfCompletionMills = System.currentTimeMillis() + 1000L *cooldownTimeSeconds;
    }

    public boolean isFinished() {
        return (timeOfCompletionMills <= System.currentTimeMillis()) || forceFinish;
    }

    public boolean isSuccessful() {
            return Math.random() <= chance;
        }

        public int secondsToCompletion() {
        long secsToCompletion = (timeOfCompletionMills - System.currentTimeMillis())/1000;
        return (int) secsToCompletion;
    }

}
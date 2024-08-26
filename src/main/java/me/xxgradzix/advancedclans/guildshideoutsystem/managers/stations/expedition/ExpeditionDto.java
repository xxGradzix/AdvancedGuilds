package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition;

import lombok.Getter;

public class ExpeditionDto {

    public enum ExpeditionObjective {
        STONE, WOOD, CRYSTALS
    }

    private final double chance;

    @Getter
    private int expeditionLevel;

    @Getter
    private ExpeditionObjective objective;

    private final long cooldownTimeSeconds;

    private final long timeOfCompletionMills;

    public ExpeditionDto(double chance, int expeditionLevel, ExpeditionObjective objective, long completionTimeSeconds) {
        this.chance = chance;
        this.expeditionLevel = expeditionLevel;
        this.objective = objective;
        this.cooldownTimeSeconds = completionTimeSeconds;
        this.timeOfCompletionMills = System.currentTimeMillis() + 1000*completionTimeSeconds;
    }

    public boolean isFinished() {
        return cooldownTimeSeconds <= System.currentTimeMillis();
    }

    public boolean isSuccessful() {
            return Math.random() <= chance;
        }

        public int secondsToCompletion() {
        long secsToCompletion = (timeOfCompletionMills - System.currentTimeMillis())/1000;
        return (int) secsToCompletion;
    }

}
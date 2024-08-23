package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis;

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

        private long completionTimeSeconds;

        public ExpeditionDto(double chance, int expeditionLevel, ExpeditionObjective objective, long completionTimeSeconds) {
            this.chance = chance;
            this.expeditionLevel = expeditionLevel;
            this.objective = objective;
            this.completionTimeSeconds = completionTimeSeconds;
        }

        public boolean isFinished() {
            return completionTimeSeconds * 1000 <= System.currentTimeMillis();
        }

        public boolean isSuccessful() {
            return Math.random() <= chance;
        }

        public int secondsToCompletion() {
            return (int) (completionTimeSeconds - (System.currentTimeMillis() / 1000));
        }

    }
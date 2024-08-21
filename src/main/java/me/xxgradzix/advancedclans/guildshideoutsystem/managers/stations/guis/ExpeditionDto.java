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

        private long completionTime;

        public ExpeditionDto(double chance, int expeditionLevel, ExpeditionObjective objective, long completionTime) {
            this.chance = chance;
            this.expeditionLevel = expeditionLevel;
            this.objective = objective;
            this.completionTime = completionTime;
        }

        public boolean isFinished() {
            return completionTime <= System.currentTimeMillis();
        }

        public boolean isSuccessful() {
            return Math.random() <= chance;
        }

        public int secondsToCompletion() {
            return (int) ((completionTime - System.currentTimeMillis()) / 1000);
        }

    }
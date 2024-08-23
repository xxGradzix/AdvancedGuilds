package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis;

import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;

public class ExpeditionVariant{
        @Getter
        private final ExpeditionDto.ExpeditionObjective objective;
        @Getter
        private final int level;
        @Getter
        private final long cooldownSeconds;
        @Getter
        private final double chance;

        public ExpeditionVariant(ExpeditionDto.ExpeditionObjective objective, int level, long cooldownSeconds) {
            this.objective = objective;
            this.level = level;
            this.cooldownSeconds = cooldownSeconds;
            this.chance = getRandomChance(level);
        }

        private static double getRandomChance(int level) {
            double chance = 0;
            switch (level) {
                case 2 -> {
                    chance = new Random().nextDouble(0.35, 0.55);
                }
                case 3 -> {
                    chance =  new Random().nextDouble(0.2, 0.35);
                }
                default -> {
                    chance = new Random().nextDouble(0.55, 0.8);
                }
            }
            BigDecimal bd = BigDecimal.valueOf(chance);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }

    }
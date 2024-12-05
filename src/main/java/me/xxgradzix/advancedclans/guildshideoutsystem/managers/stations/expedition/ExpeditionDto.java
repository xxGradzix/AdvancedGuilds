package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition;

import lombok.Getter;
import lombok.Setter;
import me.xxgradzix.advancedclans.utils.ColorFixer;

public class ExpeditionDto {

    public enum ExpeditionObjective {

        STONE(ColorFixer.addColors("&8ᴋᴀᴍɪᴇɴɪᴇ")), WOOD(ColorFixer.addColors("&eᴅʀᴇᴡɴᴏ")), CRYSTALS(ColorFixer.addColors("&8ᴍɪɴᴇʀᴀłʏ"));

        private String name;

        ExpeditionObjective(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Getter
    private final double chance;

    @Getter
    @Setter
    private boolean forceFinish;

    @Getter
    private int expeditionLevel;

    @Getter
    private ExpeditionObjective objective;


    @Getter
    private final int toolsSuplied;

    @Getter
    private final int foodSuplied;

    private final long cooldownTimeSeconds;

    private final long timeOfCompletionMills;


    public ExpeditionDto(double chance, int expeditionLevel, ExpeditionObjective objective, int toolsSuplied, int foodSuplied, int cooldownTimeSeconds) {
        this.chance = chance;
        this.toolsSuplied = toolsSuplied;
        this.foodSuplied = foodSuplied;
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
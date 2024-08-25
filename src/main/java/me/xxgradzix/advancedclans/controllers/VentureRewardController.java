package me.xxgradzix.advancedclans.controllers;

import me.xxgradzix.advancedclans.data.database.entities.hideout.venture.VentureReward;
import me.xxgradzix.advancedclans.data.database.services.VentureRewardDataManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.expedition.ExpeditionDto;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class VentureRewardController {


    public void addVentureReward(Player player, ExpeditionDto.ExpeditionObjective objective, int level, int minAmount, int maxAmount, ItemStack reward) {

        if(level < 1) {
            player.sendMessage("§cLevel must be greater than 0");
            return;
        }
        if(minAmount < 0) {
            player.sendMessage("§cMin amount must be greater than 0");
            return;
        }
        if(maxAmount < minAmount) {
            player.sendMessage("§cMax amount must be greater or equal min amount");
            return;
        }

        VentureReward ventureReward = new VentureReward(objective, reward, level, minAmount, maxAmount);

        VentureRewardDataManager.addVentureReward(ventureReward);
    }

    public List<VentureReward> getAllByObjectiveAndLevel(ExpeditionDto.ExpeditionObjective objective, int level) {
        return VentureRewardDataManager.getAllByObjectiveAndLevel(objective, level);
    }


}

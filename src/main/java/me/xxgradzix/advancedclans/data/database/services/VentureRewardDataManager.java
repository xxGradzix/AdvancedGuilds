package me.xxgradzix.advancedclans.data.database.services;

import me.xxgradzix.advancedclans.data.database.entities.hideout.venture.VentureReward;
import me.xxgradzix.advancedclans.data.database.repositories.venture.VentureRewardRepository;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.expedition.ExpeditionDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class VentureRewardDataManager {

    private static VentureRewardRepository ventureRewardRepository;

    public VentureRewardDataManager(VentureRewardRepository ventureRewardRepository) {
        VentureRewardDataManager.ventureRewardRepository = ventureRewardRepository;
    }

    public static void addVentureReward(VentureReward ventureReward) {
        try {
            ventureRewardRepository.createOrUpdateEntity(ventureReward);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static List<VentureReward> getAllByObjectiveAndLevel(ExpeditionDto.ExpeditionObjective objective, int level) {
        HashMap<String, Object> fields = new HashMap<>();
        fields.put("objective", objective);
        fields.put("diffLevel", level);
        try {
            return ventureRewardRepository.getAllEntitiesByFields(fields);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public static void removeVentureReward(VentureReward ventureReward) {
        try {
            ventureRewardRepository.deleteEntity(ventureReward);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}

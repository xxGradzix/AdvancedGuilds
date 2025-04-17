package me.xxgradzix.advancedclans.data.database.services.hideout;

import me.xxgradzix.advancedclans.data.database.entities.hideout.venture.VentureReward;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.venture.VentureRewardRepository;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionDto;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class VentureRewardDataService {

    private static VentureRewardRepository ventureRewardRepository;

    public VentureRewardDataService(VentureRewardRepository ventureRewardRepository) {
        VentureRewardDataService.ventureRewardRepository = ventureRewardRepository;
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

}

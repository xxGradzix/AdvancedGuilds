package me.xxgradzix.advancedclans.data.database.services;

import eu.decentsoftware.holograms.api.DHAPI;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.hideout.venture.VentureReward;
import me.xxgradzix.advancedclans.data.database.repositories.GuildHideoutEntityRepository;
import me.xxgradzix.advancedclans.data.database.repositories.venture.VentureRewardRepository;
import me.xxgradzix.advancedclans.exceptions.ClanDoesNotExistException;
import me.xxgradzix.advancedclans.exceptions.PlayerDoesNotBelongToClanException;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionDto;
import me.xxgradzix.advancedclans.guildshideoutsystem.upgrades.UpgradePattern;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static me.xxgradzix.advancedclans.controllers.GuildHideOutController.*;

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

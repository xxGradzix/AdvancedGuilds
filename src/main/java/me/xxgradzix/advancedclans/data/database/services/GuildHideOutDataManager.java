package me.xxgradzix.advancedclans.data.database.services;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.repositories.GuildHideoutEntityRepository;
import me.xxgradzix.advancedclans.exceptions.hideOuts.HideOutDoesNotExistException;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class GuildHideOutDataManager {

    private static final HashMap<String, GuildHideout> guildHideouts = new HashMap<>();

    private static GuildHideoutEntityRepository guildHideoutEntityRepository;


    public GuildHideOutDataManager(GuildHideoutEntityRepository guildHideoutEntityRepository) {
        GuildHideOutDataManager.guildHideoutEntityRepository = guildHideoutEntityRepository;
    }

    /** ADMIN METHODS **/

    public static GuildHideout resetOrCreateHideOut(String hideOutWorldName) {

        GuildHideout guildHideout = guildHideouts.get(hideOutWorldName);

        if (guildHideout == null) {
            guildHideout = new GuildHideout(hideOutWorldName, new HashMap<>(), null);
        }

        Clan clan = ClanAndUserDataManager.getCachedClan(guildHideout.getClanTag());

        if(clan != null){
            clan.setHideoutId(null);
            ClanAndUserDataManager.updateClan(clan);
        }

        guildHideout.reset();

        try {
            guildHideouts.put(hideOutWorldName, guildHideout);

            guildHideoutEntityRepository.createOrUpdateEntity(guildHideout);

            return guildHideout;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    /** PLAYER METHODS **/

    public static void occupyHideOut(String hideOutWorldName, Clan clan) throws HideOutDoesNotExistException {

        GuildHideout guildHideout = guildHideouts.get(hideOutWorldName);

        if (guildHideout == null) throw new HideOutDoesNotExistException("Hideout does not exist");

        guildHideout = resetOrCreateHideOut(hideOutWorldName);
        guildHideout.setClanTag(clan);

        clan.setHideoutId(guildHideout.getWorldName());
        ClanAndUserDataManager.updateClan(clan);

        updateHideOut(guildHideout);
    }


    public static void updateHideOut(GuildHideout hideout) {
        try {
            guildHideoutEntityRepository.createOrUpdateEntity(hideout);
            guildHideouts.put(hideout.getWorldName(), hideout);
        } catch (SQLException e) {
            // todo MESSAGE send message to player that sql exception occurred
        }
    }

    public static List<GuildHideout> loadAllHideOuts() {
        try {
            List<GuildHideout> allEntities = guildHideoutEntityRepository.getAllEntities();
            for(GuildHideout hideout : allEntities) {
                guildHideouts.put(hideout.getWorldName(), hideout);
            }
            return allEntities;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static GuildHideout getHideOut(@NotNull String name) {
        return guildHideouts.get(name);
    }
}

package me.xxgradzix.advancedclans.data.database.services;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.repositories.ClanEntityRepository;
import me.xxgradzix.advancedclans.data.database.repositories.GuildHideoutEntityRepository;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

public class GuildHideOutDataManager {

    private static HashMap<String, GuildHideout> guildHideouts = new HashMap<>();

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

            Clan clan = guildHideout.getClan();

            if(clan != null){
                clan.setHideout(null);
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


    public static void occupyHideOut(String hideOutWorldName, Clan clan) {

        GuildHideout guildHideout = guildHideouts.get(hideOutWorldName);

        if (guildHideout == null) {
            // TODO MESSAGE send message to player that hideout does not exist
            return;
        }

        guildHideout = resetOrCreateHideOut(hideOutWorldName);

        guildHideout.setClan(clan);
        clan.setHideout(guildHideout);

        ClanAndUserDataManager.updateClan(clan);
        updateHideOut(guildHideout);
    }


    public static void updateHideOut(GuildHideout hideout) {
        try {
            guildHideoutEntityRepository.createOrUpdateEntity(hideout);
            guildHideouts.put(hideout.getWorldName(), hideout);
        } catch (SQLException e) {
            // todo MESSAGE send message to player that sql exception occurred
            return;
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
}

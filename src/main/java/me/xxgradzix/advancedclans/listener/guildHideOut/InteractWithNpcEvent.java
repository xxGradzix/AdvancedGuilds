package me.xxgradzix.advancedclans.listener.guildHideOut;

import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.ClanController;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.UserController;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionGui;
import net.citizensnpcs.api.event.NPCClickEvent;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.npc.NPCRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

public class InteractWithNpcEvent implements Listener {



    @EventHandler
    public void onCitizensNPCClick(NPCRightClickEvent event) {

        Player player = event.getClicker();
        NPC npc = event.getNPC();

        Optional<User> optionalUser = UserController.findUserByUUID(player.getUniqueId());
        if(optionalUser.isEmpty()) return;
        User user = optionalUser.get();

        Clan clan = ClanAndUserDataManager.getCachedClan(user.getClanTag());
        String hideoutId = clan.getHideoutId();
        if(hideoutId == null || hideoutId.isEmpty()) return;

        NPCRegistry registry = npc.getOwningRegistry();
        String name = registry.getName();

        if(!name.equals(hideoutId + "hideout_npc_registry")) return;

        switch (npc.getId()) {
            case 1 -> {
                player.sendMessage("Sprzedawca - wkrótce ");
            }
            case 2 -> {
                AdvancedGuilds.instance.getServer().dispatchCommand(AdvancedGuilds.instance.getServer().getConsoleSender(), "openWitchGui " + player.getName());
            }
            case 3 -> {
                ExpeditionGui.openExpeditionGui(player);
            }
            case 4 -> {
                AdvancedGuilds.instance.getServer().dispatchCommand(AdvancedGuilds.instance.getServer().getConsoleSender(), "openupgradegui " + player.getName() + " 1");
            }
            case 5 -> {
                AdvancedGuilds.instance.getServer().dispatchCommand(AdvancedGuilds.instance.getServer().getConsoleSender(), "openEnchantGui " + player.getName());
            }
            case 6 -> {
                player.sendMessage("przewoźnik - wkrótce ");
            }
            case 7 -> {
                player.sendMessage("czarny rynek - wkrótce ");
            }
            case 8 -> {
                player.sendMessage("powrót na spawn - wkrótce ");
            }
            case 9 -> {
                player.sendMessage("bukmacher - wkrótce ");
            }
            case 10 -> {
                player.sendMessage("mistrz areny - wkrótce ");
            }
            default -> {
                player.sendMessage("Nieznany NPC");
            }
        }


    }



}

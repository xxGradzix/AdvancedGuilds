package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.storage;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.StorageGui;
import me.xxgradzix.advancedclans.controllers.ClanController;
import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import me.xxgradzix.advancedclans.controllers.UserController;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import org.bukkit.entity.Player;

import java.io.InvalidObjectException;
import java.util.HashMap;
import java.util.UUID;

import static com.ibm.icu.impl.ValidIdentifiers.Datatype.u;

public class ExpeditionStorage {

    private static UserController userController;
    private static GuildHideOutController hideOutController;
    private static ClanController clanController;

    public ExpeditionStorage(UserController userController, GuildHideOutController hideOutController, ClanController clanController) {
        ExpeditionStorage.userController = userController;
        ExpeditionStorage.clanController = clanController;
        ExpeditionStorage.hideOutController = hideOutController;
    }

    private static class HideoutGuis {
        private final HashMap<UUID, StorageGui> personalGuis = new HashMap<>();

        private final StorageGui gui1 = Gui.storage().rows(5).create();
        private final StorageGui gui2 = Gui.storage().rows(5).create();
        private final StorageGui gui3 = Gui.storage().rows(5).create();
        private final StorageGui gui4 = Gui.storage().rows(5).create();
        private final StorageGui gui5 = Gui.storage().rows(5).create();

        public StorageGui getGuiByNum(int num) throws InvalidObjectException {
            switch (num) {
                case 1 -> {
                    return gui1;
                }
                case 2 -> {
                    return gui2;
                }
                case 3 -> {
                    return gui3;
                }
                case 4 -> {
                    return gui4;
                }
                case 5 -> {
                    return gui5;
                }
                default -> throw new InvalidObjectException("Num " + num + " is invalid for this method");
            }
        }
    }

    private static final HashMap<GuildHideout, HideoutGuis> hideoutGuisMap = new HashMap<>();

    public static void openSharedGui(Player player, int guiNum) {

        GuildHideout guildHideout;
        try {
            guildHideout = hideOutController.getPlayerHideOut(player);
        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        }

        if(guildHideout == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }

        // TODO Check permisison

        HideoutGuis hideoutGuis = hideoutGuisMap.get(guildHideout);

        if(hideoutGuis == null) {
            hideoutGuis = new HideoutGuis();
            hideoutGuisMap.put(guildHideout, hideoutGuis);
        }

        StorageGui gui;
        try {
            gui = hideoutGuis.getGuiByNum(guiNum);
        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        }

        gui.open(player);


    }


}

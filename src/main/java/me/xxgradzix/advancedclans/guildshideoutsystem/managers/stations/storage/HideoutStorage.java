package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.storage;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.StorageGui;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.ClanController;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.UserController;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.hideout.storage.GuildlStorageEntity;
import me.xxgradzix.advancedclans.data.database.entities.hideout.storage.PersonalStorageEntity;
import me.xxgradzix.advancedclans.data.database.services.hideout.StorageEntityDataManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.io.InvalidObjectException;
import java.util.*;

public class HideoutStorage {

    private static GuildHideOutController hideOutController;
    private static ClanController clanController;

    public HideoutStorage(UserController userController, GuildHideOutController hideOutController, ClanController clanController) {
        HideoutStorage.hideOutController = hideOutController;
        HideoutStorage.clanController = clanController;
    }

    private static final HashMap<UUID, StorageGui> personalGuis = new HashMap<>();
    private static final HashMap<String, HideoutGuis> hideoutGuisMap = new HashMap<>();

    private static class HideoutGuis {

        private final StorageGui gui1 = Gui.storage().title(Component.text("Skrzynia 1")).rows(5).create();
        private final StorageGui gui2 = Gui.storage().title(Component.text("Skrzynia 2")).rows(5).create();
        private final StorageGui gui3 = Gui.storage().title(Component.text("Skrzynia 3")).rows(5).create();
        private final StorageGui gui4 = Gui.storage().title(Component.text("Skrzynia 4")).rows(5).create();
        private final StorageGui gui5 = Gui.storage().title(Component.text("Skrzynia 5")).rows(5).create();

        private final Set<UUID> gui1permissions = new HashSet<>();
        private final Set<UUID> gui2permissions = new HashSet<>();
        private final Set<UUID> gui3permissions = new HashSet<>();
        private final Set<UUID> gui4permissions = new HashSet<>();
        private final Set<UUID> gui5permissions = new HashSet<>();

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
        public Set<UUID> getPermissionsByNum(int num) {
            switch (num) {
                case 1 -> {
                    return gui1permissions;
                }
                case 2 -> {
                    return gui2permissions;
                }
                case 3 -> {
                    return gui3permissions;
                }
                case 4 -> {
                    return gui4permissions;
                }
                case 5 -> {
                    return gui5permissions;
                }
                default -> {
                    return null;
                }
            }
        }
        public void addPermission(int num, UUID uuid) throws InvalidObjectException {
            switch (num) {
                case 1 -> {
                    gui1permissions.add(uuid);
                }
                case 2 -> {
                    gui2permissions.add(uuid);
                }
                case 3 -> {
                    gui3permissions.add(uuid);
                }
                case 4 -> {
                    gui4permissions.add(uuid);
                }
                case 5 -> {
                    gui5permissions.add(uuid);
                }
                default -> throw new InvalidObjectException("Num " + num + " is invalid for this method");
            }
        }
        public void removePermission(int num, UUID uuid) throws InvalidObjectException {
            switch (num) {
                case 1 -> {
                    gui1permissions.remove(uuid);
                }
                case 2 -> {
                    gui2permissions.remove(uuid);
                }
                case 3 -> {
                    gui3permissions.remove(uuid);
                }
                case 4 -> {
                    gui4permissions.remove(uuid);
                }
                case 5 -> {
                    gui5permissions.remove(uuid);
                }
                default -> throw new InvalidObjectException("Num " + num + " is invalid for this method");
            }
        }

    }
    private static void openPersonalStorageGui(Player player) {

        StorageGui gui = personalGuis.get(player.getUniqueId());
        if(gui == null) {
            gui = Gui.storage().title(Component.text("Personal Storage")).rows(5).create();
            personalGuis.put(player.getUniqueId(), gui);
        }
        gui.open(player);

    }


    public static void openGuildStoragesGui(Player player) {

        GuildHideout playerHideOut = hideOutController.getPlayerHideOut(player);

        if(playerHideOut == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }

        Clan clan = clanController.getClan(playerHideOut.getClanTag());

        boolean isOwner = clan.getOwnerUUID().equals(player.getUniqueId());



        Gui gui = Gui.gui().rows(3).disableAllInteractions().title(Component.text("Guild Storage")).create();

        GuiItem personalStorage = ItemBuilder.from(ItemManager.getPersonalStorageItem()).asGuiItem();
        personalStorage.setAction(e -> openPersonalStorageGui(player));
        gui.setItem(10, personalStorage);

        for (int i = 0; i< 5; i++) {

            final boolean hasAccess = isOwner || getOrCreateHideoutGuis(playerHideOut).getPermissionsByNum(i+1).contains(player.getUniqueId());

            GuiItem expeditionStorage = ItemBuilder.from(ItemManager.getExpeditionStorageItem(i+1, isOwner, hasAccess)).asGuiItem();
            int finalI = i;
            expeditionStorage.setAction(e -> {

                if(!hasAccess) {
                    MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_HAVE_PERMISSION_TO_THIS_STORAGE, MessageType.CHAT);
                    return;
                }

                if(isOwner) {
                    if(e.getClick().isLeftClick()) {
                        openSharedGui(player, playerHideOut, finalI + 1);
                    }else if(e.getClick().isRightClick()) {
                        openPermissionGui(player, playerHideOut, finalI + 1);
                    }
                } else {
                    openSharedGui(player, playerHideOut, finalI + 1);
                }

            });
            gui.setItem(12 + i, expeditionStorage);
        }
        gui.open(player);
    }

    private static void openPermissionGui(Player player, GuildHideout playerHideOut, int guiNum) {

        Clan clan = clanController.getClan(playerHideOut.getClanTag());

        final HideoutGuis hideoutGuis = getOrCreateHideoutGuis(playerHideOut);

        Gui gui = Gui.gui().rows(6).disableAllInteractions().title(Component.text("Permission")).create();

        Set<UUID> permittedPlayers = hideoutGuis.getPermissionsByNum(guiNum);

        List<UUID> clanMembers = clan.getMembers();
        clanMembers.remove(clan.getOwnerUUID());

        gui.setCloseGuiAction(e -> {
            hideoutGuis.getPermissionsByNum(guiNum).clear();
            MessageManager.sendMessageFormated(player, MessageManager.PERMISSIONS_UPDATED, MessageType.CHAT);
        });

        for(UUID uuid : clanMembers) {

            GuiItem playerItem = ItemBuilder.from(ItemManager.getPermissionItem(uuid, permittedPlayers.contains(uuid))).asGuiItem();

            playerItem.setAction(e -> {
                if(permittedPlayers.contains(uuid)) {
                    permittedPlayers.remove(uuid);
                } else {
                    permittedPlayers.add(uuid);
                }
                gui.updateItem(e.getSlot(), ItemManager.getPermissionItem(uuid, permittedPlayers.contains(uuid)));
            });

            gui.addItem(playerItem);
        }

        gui.open(player);

    }

    private static HideoutGuis getOrCreateHideoutGuis(GuildHideout playerHideOut) {
        HideoutGuis hideoutGuis = hideoutGuisMap.get(playerHideOut.getWorldName());
        if(hideoutGuis == null) {
            hideoutGuis = new HideoutGuis();
            hideoutGuisMap.put(playerHideOut.getWorldName(), hideoutGuis);
        }
        return hideoutGuis;
    }

    private static void openSharedGui(Player player, GuildHideout guildHideout, int guiNum) {
        if(guildHideout == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }
        HideoutGuis hideoutGuis = getOrCreateHideoutGuis(guildHideout);
        try {
            StorageGui gui = hideoutGuis.getGuiByNum(guiNum);
            gui.open(player);
        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        }
    }


    public static void saveGuis() {

        personalGuis.forEach((uuid, gui) -> StorageEntityDataManager.createOrUpdatePersonalStorageEntity(new PersonalStorageEntity(uuid, gui.getInventory().getContents())));
        hideoutGuisMap.forEach((hideoutName, hideoutGuis) -> StorageEntityDataManager.createOrUpdateGuildStorageEntity(new GuildlStorageEntity(
                hideoutName,
                hideoutGuis.gui1.getInventory().getContents(),
                hideoutGuis.gui2.getInventory().getContents(),
                hideoutGuis.gui3.getInventory().getContents(),
                hideoutGuis.gui4.getInventory().getContents(),
                hideoutGuis.gui5.getInventory().getContents(),
                hideoutGuis.gui1permissions,
                hideoutGuis.gui2permissions,
                hideoutGuis.gui3permissions,
                hideoutGuis.gui4permissions,
                hideoutGuis.gui5permissions
        )));
    }

    public static void loadPersonalGuis() {
        StorageEntityDataManager.getAllPersonalStorageEntities().forEach(storageEntity -> {
            StorageGui gui = Gui.storage().title(Component.text("Personal Storage")).rows(5).create();
            gui.getInventory().setContents(storageEntity.getInventory());
            personalGuis.put(storageEntity.getId(), gui);
        });
    }

    public static void loadGuildGuis() {
        StorageEntityDataManager.getAllGuildStorageEntities().forEach(storageEntity -> {

            HideoutGuis hideoutGuis = new HideoutGuis();
            hideoutGuis.gui1.getInventory().setContents(storageEntity.getInventory1());
            hideoutGuis.gui2.getInventory().setContents(storageEntity.getInventory2());
            hideoutGuis.gui3.getInventory().setContents(storageEntity.getInventory3());
            hideoutGuis.gui4.getInventory().setContents(storageEntity.getInventory4());
            hideoutGuis.gui5.getInventory().setContents(storageEntity.getInventory5());

            hideoutGuis.gui1permissions.addAll(storageEntity.getInventory1Access());
            hideoutGuis.gui2permissions.addAll(storageEntity.getInventory2Access());
            hideoutGuis.gui3permissions.addAll(storageEntity.getInventory3Access());
            hideoutGuis.gui4permissions.addAll(storageEntity.getInventory4Access());
            hideoutGuis.gui5permissions.addAll(storageEntity.getInventory5Access());

            hideoutGuisMap.put(storageEntity.getHideoutName(), hideoutGuis);
        });
    }


}

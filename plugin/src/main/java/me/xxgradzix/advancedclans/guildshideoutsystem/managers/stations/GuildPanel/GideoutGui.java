package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.GuildPanel;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import entities.Clan;
import entities.GuildHideout;
import entities.User;
import entities.fields.UpgradeInfoHolder;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.entities.clan.ClanImpl;
import me.xxgradzix.advancedclans.data.database.entities.clan.UserImpl;
import me.xxgradzix.advancedclans.data.database.entities.hideout.fields.UpgradeImpl;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideoutImpl;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import me.xxgradzix.advancedclans.utils.transaction.TransactionUtils;
import me.xxgradzix.advancedclans.utils.transaction.buyProduct.TransactionActionProduct;
import me.xxgradzix.advancedclans.utils.transaction.buyProduct.TransactionProduct;
import me.xxgradzix.advancedclans.utils.transaction.priceType.TransactionEconomyPriceType;
import me.xxgradzix.advancedclans.utils.transaction.priceType.TransactionPriceType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class GideoutGui {

    private Player player;
    private final Clan clanImpl;
    private GuildHideout hideout;

    /** GUI ITEMS **/

    private final GuiItem playerListGuiItem = new GuiItem(GuildPanelItemManager.playerListItem, (event) -> {
        openPlayerList(1);
    });

    private final GuiItem shieldGuiItem = new GuiItem(GuildPanelItemManager.shieldItem, (event) -> {
        openShieldGui();
    });

    private final GuiItem homeItemGuiItem = new GuiItem(GuildPanelItemManager.homeItemItem, (event) -> {
        openHomeGui();
    });
    private final GuiItem questsGuiItem = new GuiItem(GuildPanelItemManager.questsItem, (event) -> {
        openQuestsGui();
    });
    private final GuiItem warGuiItem = new GuiItem(GuildPanelItemManager.warItem, (event) -> {
        openWarGui();
    });
    private final GuiItem allianceGuiItem = new GuiItem(GuildPanelItemManager.allianceItem, (event) -> {
        openAllianceGui();
    });

    private void openAllianceGui() {
        String title = (
                """
                Test line 1
                Test line 2
                Test line 3
                Test line 4
                """).replace("&", "§");

        Gui allianceGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(allianceGui);

        allianceGui.open(player);
    }

    /** STATIC GUI ITEMS **/

    private static final GuiItem shieldInfo = new GuiItem(GuildPanelItemManager.shieldInfo);
    private static final GuiItem homeInfo = new GuiItem(GuildPanelItemManager.homeInfo);
    private static final GuiItem questsInfo = new GuiItem(GuildPanelItemManager.questsInfo);
    private static final GuiItem warInfo = new GuiItem(GuildPanelItemManager.warInfo);

    /** CONSTRUCTOR **/

    public GideoutGui(Player player) {

        this.player = player;

        User userImpl = ClanAndUserDataService.getCachedUser(player.getUniqueId());

        clanImpl = ClanAndUserDataService.getCachedClan(userImpl.getClanTag());

        if (clanImpl == null) return;

        hideout = GuildHideOutController.getHideOut(clanImpl.getHideoutId());

        if (hideout == null) return;


        openHomeGui();

    }

    /** GUI METHODS **/


    /** SHIELD **/
    private void openShieldGui() {

        StringBuilder title = new StringBuilder("§f七七七七七七七七" + (System.currentTimeMillis()%2==0 ? "≡" : "≠"));


        hideout.getShieldEndDateTime();
        int maxShieldDays = hideout.getMaxShieldDays();

        title.append("七".repeat(82)).append("⎝七七".repeat(new Random().nextInt(10)));


        Gui shieldGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title.toString()))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(shieldGui);

        shieldGui.setItem(1, 9, shieldInfo);

        shieldGui.open(player);
    }

    /** HOME **/

    private void openHomeGui() {

        int hideoutLevel = hideout.getHideoutLevel();

        int hideoutExperience = hideout.getHideoutExperience();

        int expLowerCap = hideout.getLowerLevelCap();
        int expUpperCap = hideout.getUpperLevelCap();

        boolean canUpgrade = hideoutExperience >= expUpperCap;
        boolean isMaxLevel = hideoutLevel >= 5;

        StringBuilder title = new StringBuilder("§f七七七七七七七七≛");

        int expNodes = (int) (((double) (hideoutExperience-expLowerCap) / (expUpperCap-expLowerCap)) * 10);

        title.append("七".repeat(91)).append("⎛七七".repeat(expNodes));

        Gui homeGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title.toString()))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(homeGui);

        homeGui.setItem(1, 9, homeInfo);

        for (int slot = 1; slot <= 3; slot++) {

            {
                int percent = (int) (((double) (hideoutExperience-expLowerCap) / (expUpperCap-expLowerCap)) * 100);
                GuiItem percentItem = new GuiItem(GuildPanelItemManager.getExpPercentItem(percent, hideoutExperience, hideout.getLossLevelCap(), hideout.getUpperLevelCap()));
                homeGui.setItem(4, 4 + slot, percentItem);
            }

            {
                int price = hideoutLevel * 10000;
                GuiItem upgradeItem = new GuiItem(GuildPanelItemManager.getUpgradeItem(canUpgrade, slot, price, hideoutExperience, expLowerCap, expUpperCap, hideoutLevel, Optional.of(hideoutLevel+1), isMaxLevel));
                upgradeItem.setAction(event -> {

                    GuildHideOutController.upgradeHideoutLevel(hideout);
                    openHomeGui();
                });
                homeGui.setItem(5, 4 + slot, upgradeItem);
            }

        }

        {

            homeGui.setItem(2, 6, new GuiItem(GuildPanelItemManager.getHideoutTierItem(hideoutLevel)));

            GuiItem maxPlayersItem = new GuiItem(GuildPanelItemManager.getMaxPlayersItem(hideoutLevel));
            homeGui.setItem(2, 9, maxPlayersItem);
            GuiItem maxLivesItem = new GuiItem(GuildPanelItemManager.getMaxLivesItem(hideoutLevel));
            homeGui.setItem(3, 9, maxLivesItem);
            GuiItem maxShieldItem = new GuiItem(GuildPanelItemManager.getMaxShieldItem(hideoutLevel));
            homeGui.setItem(4, 9, maxShieldItem);
            GuiItem otherItem = new GuiItem(GuildPanelItemManager.getOtherItem(hideoutLevel));
            homeGui.setItem(5, 9, otherItem);




        }


        homeGui.open(player);
    }

    /** QUESTS **/

    private void openQuestsGui() {
        String title = ("&f七七七七七七七七≟").replace("&", "§");

        Gui questsGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(questsGui);

        questsGui.setItem(1, 9, questsInfo);


        questsGui.open(player);
    }

    /** WAR **/

    private void openWarGui() {
        String title = ("&f七七七七七七七七≚").replace("&", "§");

        Gui warGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(warGui);

        warGui.setItem(1, 9, warInfo);

        warGui.open(player);
    }

    /** PLAYER INFO **/

    private void openPlayerInfoGui(OfflinePlayer target) {

        StringBuilder title = new StringBuilder("&f七七七七七七七七".replace("&", "§"));

        boolean isLeader = clanImpl.getOwnerUUID().equals(target.getUniqueId());
        boolean isOfficer = clanImpl.getDeputyOwnerUUID().equals(target.getUniqueId());

        title.append(isLeader ? "≙" : (isOfficer ? "≘" : "≗"));
        title.append("§0").append(getNameWithOffset(target.getName(), 73));

        Gui playerInfoGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title.toString()))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(playerInfoGui);

        GuiItem guiItem = new GuiItem(GuildPanelItemManager.createPlayerInfoHead(target));

        playerInfoGui.setItem(2, 5, guiItem);

        playerInfoGui.setItem(4, 5, playerListGuiItem);

        GuiItem kickButton = new GuiItem(GuildPanelItemManager.kickPlayerButton);
        kickButton.setAction(event -> {
            player.closeInventory();
            player.performCommand("klan wyrzuc " + target.getName());
        });

        GuiItem permissionsButton = new GuiItem(GuildPanelItemManager.permissionsButton);

        permissionsButton.setAction(event ->
        {
            player.closeInventory();
            player.sendMessage("&cᴛᴀ ꜰᴜɴᴋᴄᴊᴀ ᴊᴇꜱᴛ ɴɪᴇ ᴅᴏꜱᴛęᴘɴᴀ");
        });
        GuiItem deputyButton = new GuiItem(GuildPanelItemManager.deputyButton);
        deputyButton.setAction(event -> {
            player.closeInventory();
            player.performCommand("klan ustawzastepce " + target.getName());
        });

        playerInfoGui.setItem(4, 6, kickButton);
        playerInfoGui.setItem(4, 7, permissionsButton);
        playerInfoGui.setItem(4, 8, deputyButton);

        playerInfoGui.open(player);
    }


    /** STORAGE GUI **/

    private void openStorageGui() {


        UpgradeInfoHolder stationHallUpgradeInfoHolder = hideout.getUpgradeHolder(UpgradeImpl.STATION_HALL);

        boolean isFinished = stationHallUpgradeInfoHolder.isFinished();

        Gui gui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text("§f七七七七七七七七≭"))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(gui);

        int vertSlot = 5;
        int storageNum = 1;
        for (UpgradeImpl upgradeImpl : List.of(
                UpgradeImpl.STORAGE_1,
                UpgradeImpl.STORAGE_2,
                UpgradeImpl.STORAGE_3
        )) {

            UpgradeInfoHolder upgradeInfoHolder = hideout.getUpgradeHolder(upgradeImpl);

            GuiItem guiItem = new GuiItem(GuildPanelItemManager.getStorageBuyButton(upgradeInfoHolder, storageNum));

            storageNum++;

            guiItem.setAction((a) -> {

                if(!upgradeInfoHolder.isBought()) {

                    TransactionProduct transactionProduct = new TransactionActionProduct() {
                        @Override
                        public void execute() {
                            GuildHideOutController.upgradeHideOut(hideout, upgradeImpl);
                            openStorageGui();
                        }
                    };

                    TransactionEconomyPriceType transactionPriceType = new TransactionEconomyPriceType(upgradeImpl.getPrice());

                    TransactionUtils.TransactionResponse purchase = TransactionUtils.purchase(player, List.of(transactionProduct), List.of(transactionPriceType));

                    MessageManager.sendMessageFormated(player, purchase.getTransactionMessage(), MessageType.CHAT);

                }

            });

            gui.setItem(4, vertSlot, guiItem);
            vertSlot++;

        }

        gui.open(player);
    }

    /** STATION HALL **/

    private void openStationHallGui() {

        StringBuilder title = new StringBuilder("&f七七七七七七七七".replace("&", "§"));

        UpgradeInfoHolder stationHallUpgradeInfoHolder = hideout.getUpgradeHolder(UpgradeImpl.STATION_HALL);

        boolean isFinished = stationHallUpgradeInfoHolder.isFinished();

        title.append(isFinished ? "≞" : "≝");

        Gui stationHallGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title.toString()))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(stationHallGui);

        stationHallGui.setItem(1, 9, new GuiItem(GuildPanelItemManager.stationHallInfo));

        if(isFinished) {

            int vertSlot = 5;
            for (UpgradeInfoHolder workshopUpgradeInfoHolder : List.of(
                    hideout.getUpgradeHolder(UpgradeImpl.BLACKSMITH),
                    hideout.getUpgradeHolder(UpgradeImpl.VENTURE),
                    hideout.getUpgradeHolder(UpgradeImpl.SORCERER)
            )) {

                GuiItem button = new GuiItem(GuildPanelItemManager.getWorkshopBuyButton(workshopUpgradeInfoHolder));

                if(!workshopUpgradeInfoHolder.isBought()) {

                    button.setAction(event -> {

                        TransactionProduct transactionProduct = new TransactionActionProduct() {
                            @Override
                            public void execute() {
                                GuildHideOutController.upgradeHideOut(hideout, workshopUpgradeInfoHolder.getUpgrade());
                                openStationHallGui();
                            }
                        };

                        // TODO add price to config
                        double price = 500;
                        TransactionPriceType transactionPriceType = new TransactionEconomyPriceType(price);

                        TransactionUtils.TransactionResponse purchase = TransactionUtils.purchase(player, List.of(transactionProduct), List.of(transactionPriceType));

                        MessageManager.sendMessageFormated(player, purchase.getTransactionMessage(), MessageType.CHAT);

                    });
                }

                stationHallGui.setItem(4, vertSlot, button);
                vertSlot++;
            }
            stationHallGui.setItem(4, 8, new GuiItem(GuildPanelItemManager.soonButton()));


        } else {

            GuiItem buyButton = new GuiItem(GuildPanelItemManager.buyAreaUpgradeButton(stationHallUpgradeInfoHolder));

            if(!stationHallUpgradeInfoHolder.isBought()) {

                buyButton.setAction(event -> {

                    TransactionProduct transactionProduct = new TransactionActionProduct() {
                        @Override
                        public void execute() {
                            GuildHideOutController.upgradeHideOut(hideout, stationHallUpgradeInfoHolder.getUpgrade());
                            openStationHallGui();
                        }
                    };

                    // TODO add price to config
                    double price = 1000;
                    TransactionPriceType transactionPriceType = new TransactionEconomyPriceType(price);

                    TransactionUtils.TransactionResponse purchase = TransactionUtils.purchase(player, List.of(transactionProduct), List.of(transactionPriceType));

                    MessageManager.sendMessageFormated(player, purchase.getTransactionMessage(), MessageType.CHAT);

                });
            }

            stationHallGui.setItem(5, 6, buyButton);
            stationHallGui.setItem(5, 7, buyButton);
            stationHallGui.setItem(4, 6, buyButton);
            stationHallGui.setItem(4, 7, buyButton);

        }

        stationHallGui.open(player);
    }

    /** BLACK MARKET **/

    private void openBlackMarketGui() {

        StringBuilder title = new StringBuilder("&f七七七七七七七七".replace("&", "§"));

        UpgradeInfoHolder illegalBusinessButton = hideout.getUpgradeHolder(UpgradeImpl.ILLEGAL_BUSINESS_HALL);

        title.append("≢");

        Gui illegalBusinessGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title.toString()))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(illegalBusinessGui);

        GuiItem buyButton = new GuiItem(GuildPanelItemManager.buyAreaUpgradeButton(illegalBusinessButton));

        illegalBusinessGui.setItem(1, 9, new GuiItem(GuildPanelItemManager.illegalBusinessInfo));

        if(!illegalBusinessButton.isBought()) {
            buyButton.setAction(event -> {

                TransactionProduct transactionProduct = new TransactionActionProduct() {
                    @Override
                    public void execute() {
                        GuildHideOutController.upgradeHideOut(hideout, illegalBusinessButton.getUpgrade());
                        openBlackMarketGui();
                    }
                };

                // TODO add price to config
                double price = 1500;
                TransactionPriceType transactionPriceType = new TransactionEconomyPriceType(price);

                TransactionUtils.TransactionResponse purchase = TransactionUtils.purchase(player, List.of(transactionProduct), List.of(transactionPriceType));

                MessageManager.sendMessageFormated(player, purchase.getTransactionMessage(), MessageType.CHAT);

            });
        }


        illegalBusinessGui.setItem(5, 6, buyButton);
        illegalBusinessGui.setItem(5, 7, buyButton);
        illegalBusinessGui.setItem(4, 6, buyButton);
        illegalBusinessGui.setItem(4, 7, buyButton);

        illegalBusinessGui.open(player);

    }

    /** PLAYER LIST **/

    private void openPlayerList(int page) {

        String title = "&f七七七七七七七七≜".replace("&", "§");

        Gui playerListGui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title))
                .rows(6)
                .disableAllInteractions()
                .create();

        setCommonButtons(playerListGui);

        List<UUID> members = clanImpl.getMembers();

        int pageSize = 8;
        int maxPage = (members.size() + pageSize - 1) / pageSize;

        if(page > maxPage) page = maxPage;
        if(page < 1) page = 1;

        boolean hasNextPage = page < maxPage;
        boolean hasPreviousPage = page > 1;

        List<Set<UUID>> membersPaginated = IntStream.range(0, maxPage)
                .mapToObj(i -> members.stream()
                        .skip((long) i * pageSize)
                        .limit(pageSize)
                        .collect(Collectors.toSet()))
                .toList();

        int row = 2;
        int column = 5;

        for (UUID member : membersPaginated.get(page - 1)) {

            OfflinePlayer offlinePlayer = Bukkit.getServer().getOfflinePlayer(member);

            boolean isLeader = clanImpl.getOwnerUUID().equals(member);
            boolean isOfficer = clanImpl.getDeputyOwnerUUID().equals(member);

            String role = isLeader ? ColorFixer.addColors("&cʟɪᴅᴇʀ") : (isOfficer ? ColorFixer.addColors("&bᴢᴀꜱᴛęᴘᴄᴀ") : ColorFixer.addColors("&7ᴄᴢłᴏɴᴇᴋ"));
            String name = isLeader ? ColorFixer.addColors("&c&l" + offlinePlayer.getName()) : (isOfficer ? ColorFixer.addColors("&b&l" + offlinePlayer.getName()) : ColorFixer.addColors("&7&l" + offlinePlayer.getName()));

            GuiItem playerItem = new GuiItem(GuildPanelItemManager.createPlayerListHead(offlinePlayer, name, role));

            playerItem.setAction(event -> {
                openPlayerInfoGui(offlinePlayer);
            });

            playerListGui.setItem(row, column, playerItem);

            GuiItem leftArrow = new GuiItem(GuildPanelItemManager.getHideoutLeftArrowItem(hasPreviousPage, page - 1));
            int finalPage = page;

            leftArrow.setAction(event -> {
                if(hasPreviousPage) openPlayerList(finalPage - 1);
            });

            GuiItem rightArrow = new GuiItem(GuildPanelItemManager.getHideoutRightArrowItem(hasNextPage, page + 1));
            rightArrow.setAction(event -> {
                if(hasNextPage) openPlayerList(finalPage + 1);
            });

            playerListGui.setItem(5, 6, leftArrow);
            playerListGui.setItem(5, 7, rightArrow);

            column++;

            if (column > 8) {
                column = 5;
                row++;
            }
        }

        playerListGui.open(player);
    }

    /** OTHER METHODS **/

    private void setCommonButtons(Gui gui) {

        gui.setItem(1, 1, playerListGuiItem);
        gui.setItem(1, 2, shieldGuiItem);
        gui.setItem(1, 3, homeItemGuiItem);

        gui.setItem(2, 1, questsGuiItem);
        gui.setItem(2, 2, warGuiItem);
        gui.setItem(2, 3, allianceGuiItem);


        UpgradeInfoHolder storageUpgradeInfoHolder = hideout.getUpgradeHolder(UpgradeImpl.STORAGE);

        GuiItem storageItemButton = new GuiItem(GuildPanelItemManager.areaUpgradeButton(storageUpgradeInfoHolder));
        storageItemButton.setAction((event) -> {
            openStorageGui();
        });
        gui.setItem(4, 1, storageItemButton);


        UpgradeInfoHolder stationHallUpgradeInfoHolder = hideout.getUpgradeHolder(UpgradeImpl.STATION_HALL);

        GuiItem stationHallItem = new GuiItem(GuildPanelItemManager.areaUpgradeButton(stationHallUpgradeInfoHolder));
        stationHallItem.setAction((event) -> {
            openStationHallGui();
        });
        gui.setItem(4, 2, stationHallItem);

        UpgradeInfoHolder illegalBusinessUpgradeHolder = hideout.getUpgradeHolder(UpgradeImpl.ILLEGAL_BUSINESS_HALL);

        GuiItem blackMarketItem = new GuiItem(GuildPanelItemManager.areaUpgradeButton(illegalBusinessUpgradeHolder));
        blackMarketItem.setAction((event) -> {
            openBlackMarketGui();
        });
        gui.setItem(4, 3, blackMarketItem);
    }


    /** UTILS **/

    private static final HashMap<String, String> charMap = new HashMap<>();

    static {
        charMap.put("a", "𐀀");
        charMap.put("b", "𐀁");
        charMap.put("c", "𐀂");
        charMap.put("d", "𐀃");
        charMap.put("e", "𐀄");
        charMap.put("f", "𐀅");
        charMap.put("g", "𐀆");
        charMap.put("h", "𐀇");
        charMap.put("i", "𐀈");
        charMap.put("j", "𐀉");
        charMap.put("k", "𐀊");
        charMap.put("l", "𐀋");
        charMap.put("m", "𐀌");
        charMap.put("n", "𐀍");
        charMap.put("o", "𐀎");
        charMap.put("p", "𐀏");
        charMap.put("q", "𐀐");
        charMap.put("r", "𐀑");
        charMap.put("s", "𐀒");
        charMap.put("t", "𐀓");
        charMap.put("u", "𐀔");
        charMap.put("v", "𐀕");
        charMap.put("w", "𐀖");
        charMap.put("x", "𐀗");
        charMap.put("y", "𐀘");
        charMap.put("z", "𐀙");
        charMap.put("A", "𐀚");
        charMap.put("B", "𐀛");
        charMap.put("C", "𐀜");
        charMap.put("D", "𐀝");
        charMap.put("E", "𐀞");
        charMap.put("F", "𐀟");
        charMap.put("G", "𐀠");
        charMap.put("H", "𐀡");
        charMap.put("I", "𐀢");
        charMap.put("J", "𐀣");
        charMap.put("K", "𐀤");
        charMap.put("L", "𐀥");
        charMap.put("M", "𐀦");
        charMap.put("N", "𐀧");
        charMap.put("O", "𐀨");
        charMap.put("P", "𐀩");
        charMap.put("Q", "𐀪");
        charMap.put("R", "𐀫");
        charMap.put("S", "𐀬");
        charMap.put("T", "𐀭");
        charMap.put("U", "𐀮");
        charMap.put("V", "𐀯");
        charMap.put("W", "𐀰");
        charMap.put("X", "𐀱");
        charMap.put("Y", "𐀲");
        charMap.put("Z", "𐀳");
        charMap.put("0", "𐀴");
        charMap.put("1", "𐀵");
        charMap.put("2", "𐀶");
        charMap.put("3", "𐀷");
        charMap.put("4", "⎔");
        charMap.put("5", "⎕");
        charMap.put("6", "⎖");
        charMap.put("7", "⎗");
        charMap.put("8", "⎘");
        charMap.put("9", "⎙");
        charMap.put("_", "⎚");
    }

    private static String getNameWithOffset(String name, int verticalOffset) {
        StringBuilder result = new StringBuilder();
        result.append("七".repeat(verticalOffset));
        for (Character character : name.toCharArray()) {
            name = name.replaceAll(String.valueOf(character), charMap.getOrDefault(String.valueOf(character), " "));
        }
        result.append(name);
        return result.toString();
    }

}

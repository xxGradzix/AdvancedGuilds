package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.ClanController;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.controllers.clansCOre.UserController;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.entities.hideout.venture.VentureReward;
import me.xxgradzix.advancedclans.data.database.services.hideout.VentureRewardDataManager;
import me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.utils.ItemUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager.getStartExpeditionItem;
import static me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionDto.ExpeditionObjective.*;

public class ExpeditionGui {

    private static final List<ExpeditionVariant> variants = new ArrayList<>();

    public static void shuffleExpeditions() {

        variants.clear();
        while (variants.size() < 6){
            int randomObjectiveIndex = new Random().nextInt(0, ExpeditionDto.ExpeditionObjective.values().length);
            ExpeditionDto.ExpeditionObjective randomObjective = values()[randomObjectiveIndex];
            int randomLevel= new Random().nextInt(1, 4);
            int randomCooldownFluctuation = new Random().nextInt(-5, 6);
            int cooldown = randomLevel * 60 * 60 + randomCooldownFluctuation * 15 * 60;
            int minCooldownValue = 30*60;
            if(cooldown < minCooldownValue) cooldown = minCooldownValue;
            variants.add(new ExpeditionVariant(randomObjective, randomLevel, cooldown));
        }
        variants.sort(Comparator.comparingInt(ExpeditionVariant::getLevel));
    }

    private static final HashMap<Player, ExpeditionDto> expeditionStatus = new HashMap<>();

    private static GuildHideOutController hideOutController;
    private static ClanController clanController;

    public ExpeditionGui(GuildHideOutController hideOutController, ClanController clanController) {
        ExpeditionGui.hideOutController = hideOutController;
        ExpeditionGui.clanController = clanController;
    }


    public static void openExpeditionGui(Player player) {

        User user = UserController.findUserByUUID(player.getUniqueId()).orElseThrow();

        if (!user.hasClan()) {
            Bukkit.getLogger().warning("User " + player.getName() + " tried to open expedition gui without clan");
            return;
        }

        Clan clan = clanController.getClan(user.getClanTag());

        if (clan == null) {
            Bukkit.getLogger().warning("User " + player.getName() + " tried to open expedition gui with wrong clan");
            return;
        }
        GuildHideout hideout = hideOutController.getPlayerHideOut(player);


        if (hideout == null) {
            MessageManager.sendMessageFormated(player, MessageManager.YOU_DONT_BELONG_TO_THIS_HIDEOUT, MessageType.CHAT);
            return;
        }

        boolean hasFinishedUpgrade = hideout.hasFinishedUpgrade(GuildHideout.Upgrade.VENTURE);

        if (!hasFinishedUpgrade) {
            MessageManager.sendMessageFormated(player, MessageManager.YOUR_HIDEOUT_DOES_NOT_HAVE_ACCESS_TO_THIS, MessageType.CHAT);
            return;
        }

        ExpeditionDto expeditionDto = expeditionStatus.getOrDefault(player, null);

        if(expeditionStatus.get(player) != null) {
//            if(expeditionDto.isFinished()) {
//                openFinishedExpeditionGui(player);
                openCurrentExpeditionGui(player);
//            }
//            else {
//                MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_PENDING, MessageType.CHAT);
//                MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_WILL_END_IN.replace("{timeleft}", MessageManager.secondsToTimeFormat(expeditionDto.secondsToCompletion())), MessageType.CHAT);
//            }
            return;
        }
        ventureChooseGui(player);
    }

    private static void ventureChooseGui(Player player) {

        Gui gui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text("&f七七七七七七七七ㇺ".replace("&", "§")))
                .rows(6)
                .create();

        int rep = 0;

        if(variants.isEmpty()) shuffleExpeditions();

        for(ExpeditionVariant variant : variants) {
            for (int i = 0; i<3; i++) {
                GuiItem item = new GuiItem(ItemManager.createObjectiveGuiItem(variant, i));

                item.setAction((a) -> venturePreparation(player, variant));
                int slot = 6 + i + rep * 9;
                if(slot > 53) break;
                gui.setItem(slot, item);
            }
            rep++;
        }
        gui.open(player);
    }


    private static void venturePreparation(Player player, ExpeditionVariant variant) {

        AtomicInteger foodTier = new AtomicInteger();
        AtomicInteger toolTier = new AtomicInteger();

        AtomicReference<Double> chance = new AtomicReference<>(calculateCurrentChance(variant.getBaseChance(), toolTier.get()));
        AtomicReference<Integer> cooldownSeconds = new AtomicReference<>(calculateCurrentCooldown((int) variant.getCooldownSeconds(), foodTier.get()));

        int foodSlot = 2;
        int toolSlot = 20;

        Gui gui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text("&f七七七七七七七七ㇰ".replace("&", "§")))
                .rows(3)
                .create();

        gui.setDragAction(event -> event.setCancelled(true));

        gui.setDefaultClickAction(event -> {
            if(event.getClickedInventory() != null && event.getClickedInventory().equals(gui.getInventory()) && event.getSlot() != toolSlot && event.getSlot() != foodSlot) {
                event.setCancelled(true);
                return;
            }
            switch (event.getAction()) {
                case PLACE_ALL, PLACE_ONE, PLACE_SOME, SWAP_WITH_CURSOR-> {

                    ItemStack item = event.getCursor();
                    if(item == null) break;
                    if(event.getSlot() == toolSlot) {
                        if(!item.isSimilar(ItemManager.toolTier1) && !item.isSimilar(ItemManager.toolTier2) && !item.isSimilar(ItemManager.toolTier3)) {
                            event.setCancelled(true);
                            break;
                        }
                    }
                    if(event.getSlot() == foodSlot) {
                        if(!item.isSimilar(ItemManager.foodRationTier3) && !item.isSimilar(ItemManager.foodRationTier2) && !item.isSimilar(ItemManager.foodRationTier1)) {
                            event.setCancelled(true);
                            break;
                        }
                    }
                    if(event.getClickedInventory() != null && event.getClickedInventory().equals(gui.getInventory())) event.setCancelled(false);
                }
                case PICKUP_ALL, PICKUP_HALF, PICKUP_ONE, PICKUP_SOME -> {
                    if(event.getClickedInventory() != null && !event.getClickedInventory().equals(gui.getInventory())) break;
                    if(event.getSlot() != toolSlot && event.getSlot() != foodSlot) event.setCancelled(true);
                }
                default -> event.setCancelled(true);
            }

            if(event.getSlot() == toolSlot || event.getSlot() == foodSlot || (event.getClickedInventory() != null && !event.getClickedInventory().equals(gui.getInventory()))) {

                Bukkit.getScheduler().runTaskLaterAsynchronously(AdvancedGuilds.instance, () -> {

                    ItemStack foodItem = gui.getInventory().getItem(foodSlot);

                    if(foodItem != null) {
                        if(foodItem.isSimilar(ItemManager.foodRationTier1)) {
                            foodTier.set(1);
                        } else if(foodItem.isSimilar(ItemManager.foodRationTier2)) {
                            foodTier.set(2);
                        } else if(foodItem.isSimilar(ItemManager.foodRationTier3)) {
                            foodTier.set(3);
                        } else {
                            foodTier.set(0);
                        }
                    } else {
                        foodTier.set(0);
                    }

                    ItemStack toolItem = gui.getInventory().getItem(toolSlot);
                    if (toolItem != null) {
                        if(toolItem.isSimilar(ItemManager.toolTier1)) {
                            toolTier.set(1);
                        } else if(toolItem.isSimilar(ItemManager.toolTier2)) {
                            toolTier.set(2);
                        } else if(toolItem.isSimilar(ItemManager.toolTier3)) {
                            toolTier.set(3);
                        } else {
                            toolTier.set(0);
                        }
                    } else {
                        toolTier.set(0);
                    }

                    chance.set(calculateCurrentChance(variant.getBaseChance(), toolTier.get()));
                    cooldownSeconds.set(calculateCurrentCooldown((int)variant.getCooldownSeconds(), foodTier.get()));

                    gui.updateItem(23, ItemManager.getDifficultyItem(chance.get()));
                    gui.updateItem(24, getStartExpeditionItem(chance.get(), variant.getLevel(), variant.getObjective(), variant.getCooldownSeconds()));
                    gui.updateItem(25, getStartExpeditionItem(chance.get(), variant.getLevel(), variant.getObjective(), variant.getCooldownSeconds()));

                    gui.updateItem(toolSlot+1, new GuiItem(ItemManager.createToolBonusSuppliedItem(toolTier.get())));
                    gui.updateItem(foodSlot+1, new GuiItem(ItemManager.createFoodBonusSuppliedItem(foodTier.get())));

                    }, 0);
                return;
            }
            event.setCancelled(true);
        });

        GuiItem expeditionFood = new GuiItem(ItemManager.createFoodBonusSuppliedItem(foodTier.get()));
        GuiItem expeditionTool = new GuiItem(ItemManager.createToolBonusSuppliedItem(toolTier.get()));

        gui.setCloseGuiAction(event -> {
            ItemStack food = gui.getInventory().getItem(foodSlot);
            ItemStack tool = gui.getInventory().getItem(toolSlot);

            if(food != null) {
                if(player.getInventory().firstEmpty() == -1) {
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(), food);
                } else {
                    player.getInventory().addItem(food);
                }
            }
            if(tool != null) {
                if(player.getInventory().firstEmpty() == -1) {
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(), tool);
                } else {
                    player.getInventory().addItem(tool);
                }
            }
        });

        GuiItem startExpedition = new GuiItem(ItemManager.getStartExpeditionItem(chance.get(), variant.getLevel(), variant.getObjective(), variant.getCooldownSeconds()));

        startExpedition.setAction((e) -> {
            gui.setCloseGuiAction(null);

            ExpeditionDto expeditionDto = new ExpeditionDto(chance.get(), variant.getLevel(), variant.getObjective(), cooldownSeconds.get());

            player.sendMessage("Expedition started with chance  " + chance); // TODO MESSAGE

            expeditionStatus.put(player, expeditionDto);

            gui.close(player);
        });

        List<VentureReward> rewards = expeditionRewards.getOrDefault(variant.getObjective(), new HashMap<>()).getOrDefault(variant.getLevel(), new ArrayList<>());
        for (int i = 5; i <=8; i++) {
            if(i-5 >= rewards.size()) break;
            ItemStack reward = rewards.get(i-5).getReward();
            GuiItem item = new GuiItem(reward);
            gui.setItem(i, item);
        }

        gui.setItem(foodSlot+1, expeditionFood);
        gui.setItem(toolSlot+1, expeditionTool);

        gui.setItem(23, new GuiItem(ItemManager.getDifficultyItem(chance.get())));
        gui.setItem(24, startExpedition);
        gui.setItem(25, startExpedition);

        gui.removeItem(foodSlot);
        gui.removeItem(toolSlot);
        gui.open(player);

    }

    private static double calculateCurrentChance(double chance, int toolTier) {

        switch (toolTier) {
            case 1 -> chance += 0.10;
            case 2 -> chance += 0.20;
            case 3 -> chance += 0.30;
        }
        if(chance > 1) chance = 1;
        return chance;
    }
    private static int calculateCurrentCooldown(int cooldownSec, int foodTier) {

        switch (foodTier) {
            case 1 -> cooldownSec -= 60 * 30;
            case 2 -> cooldownSec += 60 * 60;
            case 3 -> cooldownSec += 60 * 90;
        }
        if(cooldownSec < 60*15) cooldownSec = 60 * 15;
        return cooldownSec;
    }

//    private static void openFinishedExpeditionGui(Player player) {
//
//        ExpeditionDto expeditionDto = expeditionStatus.get(player);
//        if(expeditionDto == null) return;
//
//        if(expeditionDto.isSuccessful()) {
//            MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_SUCCESS, MessageType.CHAT);
//
//            if(expeditionRewards.isEmpty()) {
//                refreshAllExpeditionRewards();
//            }
//            List<VentureReward> rewards = expeditionRewards.getOrDefault(expeditionDto.getObjective(), new HashMap<>()).getOrDefault(expeditionDto.getExpeditionLevel(), new ArrayList<>());
//
//            for (VentureReward reward : rewards) {
//                int amount = reward.getRandomAmount();
//
//                while (amount > 0) {
//                    ItemStack item = reward.getReward().clone();
//                    item.setAmount(Math.min(amount, item.getMaxStackSize()));
//                    if(player.getInventory().firstEmpty() == -1) {
//                        player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
//                    } else {
//                        player.getInventory().addItem(item);
//                    }
//                    amount -= item.getAmount();
//                }
//            }
//
//        } else {
//            MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_FAILED, MessageType.CHAT);
//        }
//
//        expeditionStatus.remove(player);
//
//    }

    private static void openCurrentExpeditionGui(Player player) {

        ExpeditionDto expeditionDto = expeditionStatus.get(player);
        if(expeditionDto == null) return;

        Gui gui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text("Twoja ekspedycja".replace("&", "§")))
                .rows(3)
                .disableAllInteractions()
                .create();

        GuiItem expeditionItem = new GuiItem(ItemManager.getCurrentExpeditionItem(expeditionDto.getObjective(), expeditionDto.isFinished(), expeditionDto.getExpeditionLevel(), expeditionDto.secondsToCompletion()));

        expeditionItem.setAction((e) -> {


            if(e.getClick().isRightClick()) {

                if(!expeditionDto.isFinished()) {

                    ItemStack targetItem = new ItemStack(Material.EMERALD);
                    int requiredEmeralds = expeditionDto.secondsToCompletion() / (60 * 30);

                    int i = ItemUtil.calcItemAmount(player, targetItem);

                    if(i < requiredEmeralds) {
                        MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_NOT_ENOUGH_PREMIUM_MONEY, MessageType.CHAT);
                        return;
                    } else {
                        ItemUtil.removeItems(player, targetItem, i);
                        expeditionDto.setForceFinish(true);
                        MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_FORCED, MessageType.CHAT);
                        openCurrentExpeditionGui(player);
                    }
                }
            }

            if(expeditionDto.isFinished()) {

                if(expeditionDto.isSuccessful()) {
                    MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_SUCCESS, MessageType.CHAT);
                    if(expeditionRewards.isEmpty()) {
                        refreshAllExpeditionRewards();
                    }
                    List<VentureReward> rewards = expeditionRewards.getOrDefault(expeditionDto.getObjective(), new HashMap<>()).getOrDefault(expeditionDto.getExpeditionLevel(), new ArrayList<>());

                    for (VentureReward reward : rewards) {
                        int amount = reward.getRandomAmount();

                        while (amount > 0) {
                            ItemStack item = reward.getReward().clone();
                            item.setAmount(Math.min(amount, item.getMaxStackSize()));
                            if(player.getInventory().firstEmpty() == -1) {
                                player.getLocation().getWorld().dropItemNaturally(player.getLocation(), item);
                            } else {
                                player.getInventory().addItem(item);
                            }
                            amount -= item.getAmount();
                        }
                    }
                } else {
                    MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_FAILED, MessageType.CHAT);
                }
                expeditionStatus.remove(player);
                gui.close(player);

            } else {
                gui.close(player);
                MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_PENDING, MessageType.CHAT);
                MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_WILL_END_IN.replace("{timeleft}", MessageManager.secondsToTimeFormat(expeditionDto.secondsToCompletion())), MessageType.CHAT);
            }

        });

        gui.setItem(13, expeditionItem);

        gui.open(player);

    }
    private static final HashMap<ExpeditionDto.ExpeditionObjective, HashMap<Integer, List<VentureReward>>> expeditionRewards = new HashMap<>();

    private static void refreshAllExpeditionRewards() {
        for (ExpeditionDto.ExpeditionObjective objective : ExpeditionDto.ExpeditionObjective.values()) {
            HashMap<Integer, List<VentureReward>> rewards = new HashMap<>();
            for (int i = 1; i <= 3; i++) {
                List<VentureReward> allByObjectiveAndLevel = VentureRewardDataManager.getAllByObjectiveAndLevel(objective, i);
                rewards.put(i, allByObjectiveAndLevel);
            }
            expeditionRewards.put(objective, rewards);
        }
    }

    public static void refreshExpeditionRewards(ExpeditionDto.ExpeditionObjective objective, int level) {
        if(expeditionRewards.isEmpty()) {
            refreshAllExpeditionRewards();
            return;
        }
        List<VentureReward> allByObjectiveAndLevel = VentureRewardDataManager.getAllByObjectiveAndLevel(objective, level);

        HashMap<Integer, List<VentureReward>> integerListHashMap = expeditionRewards.getOrDefault(objective, new HashMap<>());
        integerListHashMap.put(level, allByObjectiveAndLevel);
        expeditionRewards.put(objective, integerListHashMap);
    }



}

package me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.controllers.ClanController;
import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import me.xxgradzix.advancedclans.controllers.UserController;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.InvalidObjectException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static com.ibm.icu.impl.ValidIdentifiers.Datatype.variant;
import static me.xxgradzix.advancedclans.guildshideoutsystem.ItemManager.getStartExpeditionItem;
import static me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionDto.ExpeditionObjective.*;

public class ExpeditionGui {

//    private enum ExpeditionStatus {
//        FAILED, PENDING, FINISHED_EASY, FINISHED_2, FINISHED_3
//    }

    public record ExpeditionVariant(ExpeditionDto.ExpeditionObjective objective, int level, long cooldownSeconds){

    }

    private static Set<ExpeditionVariant> variants = new HashSet<>();

    public static void shuffleExpeditions() {

        for (int i = 0; i<6; i++) {
            int randomObjectiveIndex = new Random().nextInt(0, values().length);
            ExpeditionDto.ExpeditionObjective randomObjective = values()[randomObjectiveIndex];
            int randomLevel= new Random().nextInt(0, 5);

            int randomCooldownFluctuation = new Random().nextInt(-5, 5);

            int cooldown = randomLevel * 60 * 60 + randomCooldownFluctuation * 15 * 60;

            int minCooldownValue = 30*60;
            if(cooldown < minCooldownValue) cooldown = minCooldownValue;

            variants.add(new ExpeditionVariant(randomObjective, randomLevel, cooldown));
        }

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
        GuildHideout hideout;
        try {
            hideout = hideOutController.getPlayerHideOut(player);
        } catch (InvalidObjectException e) {
            throw new RuntimeException(e);
        }

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
            if(expeditionDto.isFinished()) {
                openFinishedExpeditionGui(player);
                return;
            } else {
                MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_PENDING, MessageType.CHAT);
                player.sendMessage("Time to completion seconds: " + expeditionDto.secondsToCompletion());
                return;
            }
        }

        ventureChooseGui(player);
//        venturePreparation(player, variant);
    }



    private static void ventureChooseGui(Player player) {

        Gui gui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text("Wybierz ekspedycje"))
                .rows(6)
                .create();


        for(ExpeditionVariant variant : variants) {
            GuiItem item = new GuiItem(ItemManager.createObjectiveGuiItem(variant));
        }
        gui.open(player);

    }


    private static void venturePreparation(Player player, ExpeditionVariant variant) {

        AtomicBoolean foodSupplied = new AtomicBoolean(false);
        AtomicBoolean toolsSupplied = new AtomicBoolean(false);

        AtomicReference<Double> chance = new AtomicReference<>(calculateCurrentChance(variant.level, foodSupplied.get(), toolsSupplied.get()));

        int toolSlot = 29;
        int foodSlot = 33;

        Gui gui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text("Przygotuj ekspedycje"))
                .rows(6)
                .create();


        gui.getFiller().fill(new GuiItem(new ItemStack(Material.GRAY_STAINED_GLASS_PANE)));

        gui.setDefaultClickAction(event -> {

            switch (event.getAction()) {
                case PLACE_ALL, PLACE_ONE, PLACE_SOME, SWAP_WITH_CURSOR-> {
                    ItemStack item = event.getCursor();
                    if(item == null) return;
                    if(event.getSlot() == toolSlot) {
                        if(!item.getType().equals(Material.IRON_PICKAXE)) event.setCancelled(true);
                    }
                    if(event.getSlot() == foodSlot) {
                        if(!item.getType().equals(Material.COOKED_BEEF)) event.setCancelled(true);
                    }
                }
                case PICKUP_ALL, PICKUP_HALF, PICKUP_ONE, PICKUP_SOME -> {
                    if(event.getSlot() != toolSlot && event.getSlot() != foodSlot) event.setCancelled(true);
                }
                default -> event.setCancelled(true);
            }

            if(event.getSlot() == toolSlot || event.getSlot() == foodSlot || (event.getClickedInventory() != null && !event.getClickedInventory().equals(gui.getInventory()))) {

                Bukkit.getScheduler().runTaskLaterAsynchronously(AdvancedGuilds.instance, () -> {
                    toolsSupplied.set(gui.getInventory().getItem(toolSlot) != null && gui.getInventory().getItem(toolSlot).getType().equals(Material.IRON_PICKAXE));
                    foodSupplied.set(gui.getInventory().getItem(foodSlot) != null && gui.getInventory().getItem(foodSlot).getType().equals(Material.COOKED_BEEF));
                    chance.set(calculateCurrentChance(variant.level, foodSupplied.get(), toolsSupplied.get()));
                    gui.updateItem(13, getStartExpeditionItem(chance.get(), variant.level, variant.objective, variant.cooldownSeconds));
                    }, 0);
                return;
            }
            event.setCancelled(true);
        });

        GuiItem expeditionFood = ItemBuilder.from(new ItemStack(Material.COOKED_BEEF)).asGuiItem();
        GuiItem expeditionTool = ItemBuilder.from(new ItemStack(Material.DIAMOND_PICKAXE)).asGuiItem();

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

        GuiItem startExpedition = ItemBuilder.from(ItemManager.getStartExpeditionItem(chance.get(), variant.level, variant.objective, variant.cooldownSeconds)).asGuiItem();

        startExpedition.setAction((e) -> {
            gui.setCloseGuiAction(null);

            ExpeditionDto expeditionDto = new ExpeditionDto(chance.get(), variant.level, STONE, variant.cooldownSeconds);

            player.sendMessage("Expedition started with chance  " + chance); // TODO MESSAGE

            expeditionStatus.put(player, expeditionDto);

            gui.close(player);
        });

        gui.setItem(foodSlot-9, expeditionFood);
        gui.setItem(toolSlot-9, expeditionTool);

        gui.setItem(13, startExpedition);

        gui.removeItem(foodSlot);
        gui.removeItem(toolSlot);
        gui.open(player);

    }

    private static double calculateCurrentChance(int expeditionLevel, boolean toolsSupplied, boolean foodSupplied) {
        double chance = 0;
        switch (expeditionLevel) {
            case 1 -> chance = 0.6;
            case 2 -> chance = 0.4;
            case 3 -> chance = 0.3;
        }
        if(toolsSupplied) {
            chance *= 1.3;
        }
        if(foodSupplied) {
            chance *= 1.3;
        }
        if(chance > 1) chance = 1;
        return chance;
    }

    private static void openFinishedExpeditionGui(Player player) {

        ExpeditionDto expeditionDto = expeditionStatus.get(player);
        if(expeditionDto == null) return;

        if(expeditionDto.isSuccessful()) {
            MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_SUCCESS, MessageType.CHAT);

            for (String command : getExpeditionRewardsCommands(expeditionDto.getObjective(), expeditionDto.getExpeditionLevel())) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", player.getName()));
            }

        } else {
            MessageManager.sendMessageFormated(player, MessageManager.EXPEDITION_FAILED, MessageType.CHAT);
        }

        expeditionStatus.remove(player);

    }

    private static List<String> getExpeditionRewardsCommands(ExpeditionDto.ExpeditionObjective objective, int expeditionLevel) {

        return switch (objective) {

            case WOOD -> switch (expeditionLevel) {
                case 1 -> List.of("give %player% oak_log 32", "give %player% spruce_log 32");
                case 2 -> List.of("give %player% birch_log 32", "give %player% jungle_log 32");
                case 3 -> List.of("give %player% acacia_log 32", "give %player% dark_oak_log 32");
                default -> List.of();
            };
            case STONE -> switch (expeditionLevel) {
                case 1 -> List.of("give %player% cobblestone 32", "give %player% stone 32");
                case 2 -> List.of("give %player% andesite 32", "give %player% diorite 32");
                case 3 -> List.of("give %player% granite 32", "give %player% stone_bricks 32");
                default -> List.of();
            };
            case CRYSTALS -> switch (expeditionLevel) {
                case 1 -> List.of("give %player% lapis_lazuli 32", "give %player% redstone 32");
                case 2 -> List.of("give %player% emerald 32", "give %player% quartz 32");
                case 3 -> List.of("give %player% glowstone 32", "give %player% nether_quartz 32");
                default -> List.of();
            };

            default -> List.of();
        };

    }


}

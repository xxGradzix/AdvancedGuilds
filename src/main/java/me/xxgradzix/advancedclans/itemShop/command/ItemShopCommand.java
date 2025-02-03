package me.xxgradzix.advancedclans.itemShop.command;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.RequiredArgsConstructor;
import me.xxgradzix.advancedclans.itemShop.ItemShop;
import me.xxgradzix.advancedclans.itemShop.data.DataManager;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.itemShop.items.ItemManager;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class ItemShopCommand implements CommandExecutor {

    private final DataManager dataManager;

    // TODO NASK
    // TODO INTEL GDANSK LATo

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        chooseProduct(player, ShopType.MONEY);

        return false;
    }
//    public void chooseShopType(Player player) {
//        Gui chooseShopType = Gui.gui()
//                .title(Component.text("Wybierz Typ sklepu"))
//                .rows(3)
//                .disableAllInteractions()
//                .create();
//        GuiItem timeShop = new GuiItem(ItemManager.timeCoinShopButton);
//        GuiItem killsShop = new GuiItem(ItemManager.killCoinShopButton);
//        GuiItem moneyShop = new GuiItem(ItemManager.moneyShopButton);
//        timeShop.setAction((a) -> {
//            chooseProductCategory(player, ShopType.TIME);
//        });
//        killsShop.setAction((a) -> {
//            chooseProductCategory(player, ShopType.KILLS);
//        });
//        moneyShop.setAction((a) -> {
//            chooseProductCategory(player, ShopType.MONEY);
//        });
//        chooseShopType.setItem(2, 3, timeShop);
//        chooseShopType.setItem(2, 5, killsShop);
//        chooseShopType.setItem(2, 7, moneyShop);
//        chooseShopType.open(player);
//    }
//    public void chooseProductCategory(Player player, ShopType shopType) {
//
//        Gui chooseCategoryGui = Gui.gui()
//                .title(Component.text("Wybierz kategorie!"))
//                .rows(3)
//                .disableAllInteractions()
//                .create();
//
////        chooseCategoryGui.setCloseGuiAction((a) -> chooseShopType(player));
//        HashMap<ItemShopCategoryEntity, List<ItemShopProductEntity>> productsInCategories = dataManager.getItemShopProductsDividedByCategories(shopType);
//
//        for(ItemShopCategoryEntity categoryEntity : productsInCategories.keySet()){
//
//            GuiItem categoryGuiItem = new GuiItem(ItemManager.createCategoryButton(categoryEntity.getName()));
//
//            categoryGuiItem.setAction((categoryAction) -> {
//                chooseProductFromCategory(player, productsInCategories.getOrDefault(categoryEntity, new ArrayList<>()), shopType);
//            });
//
//            chooseCategoryGui.addItem(categoryGuiItem);
//        }
//        chooseCategoryGui.open(player);
//    }
//    private void chooseProductFromCategory(Player player, List<ItemShopProductEntity> productEntities, ShopType shopType) {
//        Gui chooseProductGui = Gui.gui()
//                .title(Component.text("Wybierz produkt!"))
//                .rows(3)
//                .disableAllInteractions()
//                .create();
//        for(ItemShopProductEntity product : productEntities) {
//            int price = product.getCost();
//
//            GuiItem productGuiItem = new GuiItem(ItemManager.createProductButton(product.getProduct(), price));
//
//            productGuiItem.setAction((productAction) -> {
//
//                if(dataManager.subtractMoneyFromPlayer(player, shopType, price)) {
//                    ItemStack item = product.getProduct();
//                    if (player.getInventory().firstEmpty() != -1) {
//                        player.getInventory().addItem(item);
//                    } else {
//                        player.getWorld().dropItemNaturally(player.getLocation(), item);
//                    }
//                }
//
//            });
//            chooseProductGui.addItem(productGuiItem);
//        }
//
//        chooseProductGui.open(player);
//    }
    private void chooseProduct(Player player, ShopType shopType) {

        Gui chooseProductGui = Gui.gui()
                .title(Component.text("Wybierz produkt!"))
                .rows(ItemShop.SHOP_SIZE)
                .disableAllInteractions()
                .create();

        for(ItemShopProductEntity product : dataManager.getItemShopProductsByShopType(shopType)) {

            int price = product.getCost();

            GuiItem productGuiItem = new GuiItem(ItemManager.createProductButton(product.getProduct(), price));

            productGuiItem.setAction((productAction) -> {

                if(dataManager.getPlayerBalance(player, shopType) < price) {
                    player.sendMessage("§cNie stać cię na ten produkt!");
                    return;
                }
                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage("§cNie masz wystarczająco miejsca w ekwipunku!");
                    return;
                }
                if(dataManager.subtractMoneyFromPlayer(player, shopType, price)) {
                    ItemStack item = product.getProduct();
                    if (player.getInventory().firstEmpty() != -1) {
                        player.getInventory().addItem(item);
                    } else {
                        player.getWorld().dropItemNaturally(player.getLocation(), item);
                    }
                }

            });
            chooseProductGui.setItem(product.getSlot(), productGuiItem);
        }

        chooseProductGui.open(player);
    }
}

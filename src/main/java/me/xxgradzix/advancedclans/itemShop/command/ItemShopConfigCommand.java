package me.xxgradzix.advancedclans.itemShop.command;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.RequiredArgsConstructor;
import me.xxgradzix.advancedclans.itemShop.ItemShop;
import me.xxgradzix.advancedclans.itemShop.data.DataManager;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ItemShopConfigCommand implements CommandExecutor, TabCompleter {

    private final DataManager dataManager;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        if(args.length < 3) {
            player.sendMessage("Musisz podac co chcesz zrobic");
            return false;
        }

        String shopTypeString = args[0].toLowerCase();
        ShopType shopType;

        try {
            shopType = ShopType.valueOf(shopTypeString.toUpperCase());
        } catch (EnumConstantNotPresentException e) {
            player.sendMessage("ShopType " + shopTypeString + " does not exists");
            return false;
        }

        String action = args[1].toLowerCase();

        if(!action.equalsIgnoreCase("create") && !action.equalsIgnoreCase("delete")) {
            player.sendMessage("Mozesz wykonac tylko akcje create albo delete");
            return false;
        }
        String entityType = args[2].toLowerCase();

        if(!"product".equalsIgnoreCase(entityType)) {
            player.sendMessage("Mozesz wykonac tylko tworzyc i usuwac produkty");
            return false;
        }

        if("create".equalsIgnoreCase(action)) {

            if("product".equalsIgnoreCase(entityType)) {
                if(args.length < 4) {
                    player.sendMessage("Musisz podac cene produktu");
                    return false;
                }
                String stringPrice = args[3].toLowerCase();
                int price;
                try {
                    price = Integer.parseInt(stringPrice);
                } catch (Exception e) {
                    player.sendMessage("Cena musi byc liczbą");
                    return false;
                }
//                if(args.length < 5) {
//                    player.sendMessage("Musisz podac cene produktu");
//                    return false;
//                }
//                String stringSlot = args[3].toLowerCase();
//                int slot;
//                try {
//                    slot = Integer.parseInt(stringPrice);
//                } catch (Exception e) {
//                    player.sendMessage("Slot musi byc liczbą");
//                    return false;
//                }
//                ItemStack item = player.getInventory().getItemInMainHand();
                selectSlotAndCreateProduct(player, shopType, price);
//                dataManager.createProduct(item, price, shopType, slot);
            }
        }
        if("delete".equalsIgnoreCase(action)) {
//            if("category".equalsIgnoreCase(entityType)) {
//
//                chooseCategoryToRemove(player, shopType);
//            }
            if("product".equalsIgnoreCase(entityType)) {
                chooseProductToRemoveFromShopType(player, shopType);
            }
        }


        return false;
    }

    private void selectSlotAndCreateProduct(Player player, ShopType shopType, int price) {
        Gui selectSlotGui = Gui.gui()
                .title(Component.text("Wybierz slot w ktorym chcesz umiescic produkt!"))
                .rows(ItemShop.SHOP_SIZE)
                .disableAllInteractions()
                .create();

        dataManager.getItemShopProductsByShopType(shopType).forEach(product -> {
            selectSlotGui.setItem(product.getSlot(), new GuiItem(product.getProduct().clone()));
        });

        selectSlotGui.setDefaultClickAction(event -> {
            ItemStack item = event.getCurrentItem();
            if(item != null && !item.getType().equals(Material.AIR)) {
                player.sendMessage("Slot nie moze byc zajety");
                return;
            }
            dataManager.createProduct(player.getInventory().getItemInMainHand(), price, shopType, event.getSlot());
            selectSlotGui.close(player);
        });
        selectSlotGui.open(player);

    }

    private void chooseProductToRemoveFromShopType(Player player, ShopType shopType) {
        Gui chooseProductGui = Gui.gui()
                .title(Component.text("Wybierz produkt do usuniecia!"))
                .rows(3)
                .disableAllInteractions()
                .create();

        for(ItemShopProductEntity product : dataManager.getItemShopProductsByShopType(shopType)) {

            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());

            productGuiItem.setAction((productAction) -> {
                chooseProductGui.removeItem(productGuiItem);
                dataManager.deleteProduct(product);
            });

            if(chooseProductGui.getGuiItem(product.getSlot()) == null)
                chooseProductGui.setItem(product.getSlot(), productGuiItem);
            else {
                chooseProductGui.addItem(productGuiItem);
            }
        }
        chooseProductGui.open(player);
    }

//    private void deleteCategory(ItemShopCategoryEntity itemShopCategoryEntity) {
//        dataManager.deleteCategory(itemShopCategoryEntity);
//    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(cmd.getName().equalsIgnoreCase("itemshopconfig")) {
            List<String> completions = new ArrayList<>();
            if(args.length == 1) {
                for(ShopType shopType : ShopType.values()) {
                    completions.add(shopType.name());
                }
            }
            if(args.length == 2) {
                completions.add("create");
                completions.add("delete");
            }
            if(args.length == 3) {
                completions.add("category");
                completions.add("product");
            }
            if(args.length == 4) {
//                if("create".equalsIgnoreCase(args[1]) && "product".equalsIgnoreCase(args[2])) {
//                    String shopTypeString = args[0];
//                    ShopType shopType;
//                    try {
//                        shopType = ShopType.valueOf(shopTypeString);
//
//                    } catch (Exception ignored) {
//                        return new ArrayList<>();
//                    }
//
//                    for(ItemShopCategoryEntity category : dataManager.getItemShopCategoriesByShopType(shopType)) {
//                        completions.add(category.getName());
//                    }
//                }

            }
//            if(args.length == 3) {
//                if(args[0].equalsIgnoreCase("create") && args[1].equalsIgnoreCase("product")) {
//                    for(ItemShopCategoryEntity categoryEntity : dataManager.getAllItemShopCategories()) {
//                        completions.add(categoryEntity.getName());
//                    }
//                }
//            }
//            if(args.length == 3) {
//                if(args[0].equalsIgnoreCase("delete") && args[1].equalsIgnoreCase("category")) {
//                    for(ItemShopCategoryEntity categoryEntity : dataManager.getAllItemShopCategories()) {
//                        completions.add(categoryEntity.getName());
//                    }
//                }
//            }
            return completions;
        }
        return null;
    }
}

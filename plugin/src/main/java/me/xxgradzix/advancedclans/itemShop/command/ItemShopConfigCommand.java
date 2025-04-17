package me.xxgradzix.advancedclans.itemShop.command;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.RequiredArgsConstructor;
import me.xxgradzix.advancedclans.itemShop.ItemShop;
import me.xxgradzix.advancedclans.itemShop.data.ShopService;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.itemShop.guis.AdminShopGui;
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
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
public class ItemShopConfigCommand implements CommandExecutor, TabCompleter {

    private final ShopService shopService;

    private Gui mockShopGui() {
        Gui gui = Gui.gui()
                .title(Component.text("Wybierz slot w ktorym chcesz umiescic produkt!"))
                .rows(ItemShop.SHOP_SIZE)
                .disableAllInteractions()
                .create();

        GuiItem item = new GuiItem(new ItemStack(Material.BARRIER));
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        gui.getFiller().fillBetweenPoints(1, 1, 2, 9, item);
        gui.getFiller().fillBetweenPoints(6, 1, 6, 9, item);

        gui.getFiller().fillBetweenPoints(1, 1, 6, 1, item);
        gui.getFiller().fillBetweenPoints(1, 9, 6, 9, item);
//        ItemShopCategoryEntity categoryEntity
//        shopService.getProductsByCategory(categoryEntity).forEach(product -> {
//
//            ItemStack clone = product.getProduct().clone();
//
//            List<String> lore = clone.getLore();
//            lore.add(" ");
//            lore.add("PPM aby usunac");
//
//            GuiItem guiItem = new GuiItem();
//
//
//
//            gui.setItem(product.getSlot(), ));
//        });

        return gui;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        // create/delete
        // category/product/price

//        if(args.length == 0) {
//            AdminShopGui adminShopGui = new AdminShopGui(shopService);
//            adminShopGui.mainGui(player, shopService.getA);
//        }

        if(args.length < 1) {
            player.sendMessage("Musisz podac co chcesz zrobic");
            player.sendMessage("create/delete");
            return false;
        }

        String action = args[0].toLowerCase();
//
//        if(!action.equalsIgnoreCase("create") && !action.equalsIgnoreCase("delete")) {
//            player.sendMessage("Mozesz wykonac tylko akcje create albo delete");
//            return false;
//        }

        if(args.length < 2) {
            player.sendMessage("Musisz podac co chcesz zrobic");
            player.sendMessage("create/delete");
            return false;
        }

        String entityType = args[1].toLowerCase();

        if("create".equalsIgnoreCase(action)) {

            if("product".equalsIgnoreCase(entityType)) {
                if(args.length < 3) {
                    player.sendMessage("Musisz podac kategorie");
                    return false;
                }

                ItemShopCategoryEntity categoryEntity = shopService.getCategoryByName(args[2].toLowerCase()).orElseThrow();

                AdminShopGui adminShopGui = new AdminShopGui(shopService);
                adminShopGui.mainGui(player, categoryEntity);
//
//                setSlotForProduct(player, categoryEntity);
            }
            if("category".equalsIgnoreCase(entityType)) {
                if(args.length < 3) {
                    player.sendMessage("Musisz nazwe kategorii");
                    return false;
                }
                String categoryName = args[2].toLowerCase();

                if(args.length < 4) {
                    player.sendMessage("Musisz nazwe SLOT");
                    return false;
                }
                int slot;
                try {
                    slot = Integer.parseInt(args[3]);
                } catch (Exception e) {
                    player.sendMessage("Slot musi byc liczba");
                    return false;
                }
                ItemStack itemInMainHand = player.getInventory().getItemInMainHand();
                if(itemInMainHand == null || Material.AIR.equals(itemInMainHand.getType())) {
                    player.sendMessage("Musisz trzymac item w rece");
                    return false;
                }
                shopService.createCategory(categoryName, itemInMainHand, slot);
            }
            if("price".equalsIgnoreCase(entityType)) {

                if(args.length < 3) {
                    player.sendMessage("Musisz nazwe kategorii");
                    return false;
                }

                String categoryName = args[2].toLowerCase();
                if(args.length < 4) {
                    player.sendMessage("Musisz nazwe typu sklepu");
                    player.sendMessage(Arrays.toString(ShopType.values()));
                    return false;
                }

                ShopType shopType;

                try {
                    shopType = ShopType.valueOf(args[3].toUpperCase());
                } catch (Exception e) {
                    player.sendMessage("Nie ma takiego typu sklepu");
                    player.sendMessage(Arrays.toString(ShopType.values()));
                    return false;
                }

                if(args.length < 5) {
                    player.sendMessage("Musisz podac cene");
                    return false;
                }

                double price;

                try {
                    price = Double.parseDouble(args[4]);
                } catch (Exception e) {
                    player.sendMessage("Cena musi byc liczba");
                    return false;
                }

                setPriceForProduct(player, shopService.getCategoryByName(categoryName).orElseThrow(), shopType, (int) price);
            }
        }
        if("delete".equalsIgnoreCase(action)) {
            if("category".equalsIgnoreCase(entityType)) {
                if(args.length < 3) {
                    player.sendMessage("Musisz nazwe kategorii");
                    return false;
                }
                String categoryName = args[2].toLowerCase();

//                ItemShopCategoryEntity categoryEntity = shopService.getCategoryByName(categoryName).orElseThrow();
//
//                shopService.cate(categoryEntity);

            }
            if("product".equalsIgnoreCase(entityType)) {
                if(args.length < 3) {
                    player.sendMessage("Musisz nazwe kategorii");
                    return false;
                }
                String categoryName = args[2].toLowerCase();

                ItemShopCategoryEntity categoryEntity = shopService.getCategoryByName(categoryName).orElseThrow();

                chooseProductToRemoveFromShopType(player, categoryEntity);
            }
        }
        if("gui".equalsIgnoreCase(action)) {
            if("category".equalsIgnoreCase(entityType)) {
                categoryGui(player);
            }
            if("product".equalsIgnoreCase(entityType)) {
                if(args.length < 3) {
                    player.sendMessage("Musisz nazwe kategorii");
                    return false;
                }
                String categoryName = args[2].toLowerCase();

                ItemShopCategoryEntity categoryEntity = shopService.getCategoryByName(categoryName).orElseThrow();

                AdminShopGui adminShopGui = new AdminShopGui(shopService);
                adminShopGui.mainGui(player, categoryEntity);
            }
        }


        return false;
    }

//    private void selectSlotAndCreateProduct(Player player, ItemShopCategoryEntity categoryEntity, ShopType shopType, int price) {
//        Gui selectSlotGui = Gui.gui()
//                .title(Component.text("Wybierz slot w ktorym chcesz umiescic produkt!"))
//                .rows(ItemShop.SHOP_SIZE)
//                .disableAllInteractions()
//                .create();
//
//        shopService.getProductsByCategory(categoryEntity).forEach(product -> {
//            selectSlotGui.setItem(product.getSlot(), new GuiItem(product.getProduct().clone()));
//        });
//
//        selectSlotGui.setDefaultClickAction(event -> {
//
//            ItemStack item = event.getCurrentItem();
//
//            if(item != null && !item.getType().equals(Material.AIR)) {
//                player.sendMessage("Slot nie moze byc zajety");
//                return;
//            }
//
//            shopService.createProduct(categoryEntity, player.getInventory().getItemInMainHand(), price, event.getSlot());
//            selectSlotGui.close(player);
//        });
//        selectSlotGui.open(player);
//
//    }

    private void chooseProductToRemoveFromShopType(Player player, ItemShopCategoryEntity categoryEntity) {
        Gui chooseProductGui = Gui.gui()
                .title(Component.text("Wybierz produkt do usuniecia!"))
                .rows(ItemShop.SHOP_SIZE)
                .disableAllInteractions()
                .create();

        for(ItemShopProductEntity product : shopService.getProductsByCategory(categoryEntity)) {

            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());

            productGuiItem.setAction((productAction) -> {
                chooseProductGui.removeItem(productGuiItem);
                shopService.deleteProduct(product);
            });

            if(chooseProductGui.getGuiItem(product.getSlot()) == null)
                chooseProductGui.setItem(product.getSlot(), productGuiItem);
            else {
                chooseProductGui.addItem(productGuiItem);
            }
        }
        chooseProductGui.open(player);
    }

    private void deleteProductsFromCategory(Player player, ItemShopCategoryEntity categoryEntity) {

        Gui gui = mockShopGui();

        gui.updateTitle("Wybierz produkt do usuniecia!");

        for(ItemShopProductEntity product : shopService.getProductsByCategory(categoryEntity)) {
            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());

            productGuiItem.setAction((productAction) -> {
                player.sendMessage("Usunieto produkt");
                shopService.deleteProduct(product);
            });

            gui.addItem(productGuiItem);
        }
        gui.open(player);
    }

    private void setSlotForProduct(Player player, ItemShopCategoryEntity categoryEntity) {

        Gui gui = mockShopGui();

        gui.updateTitle("Wybierz slot w ktorym chcesz umiescic produkt!");

        gui.disableAllInteractions();

        for(ItemShopProductEntity product : shopService.getProductsByCategory(categoryEntity)) {
            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());
            gui.setItem(product.getSlot(), productGuiItem);
        }

        gui.setDefaultClickAction(event -> {

            ItemStack item = event.getCurrentItem();

            if(item == null
            || Material.AIR.equals(item.getType())) {


                shopService.createProduct(categoryEntity, player.getInventory().getItemInMainHand(), event.getSlot());

                gui.close(player);

            } else {

                player.sendMessage("Slot musi byc nullem");
                return;
            }


        });
        gui.open(player);
    }


    private void setPriceForProduct(Player player, ItemShopCategoryEntity categoryEntity, ShopType shopType, int price) {
        Gui gui = mockShopGui();

        gui.updateTitle("Wybierz Produkt ktoremu chcesz umiescic cene");


        for(ItemShopProductEntity product : shopService.getProductsByCategory(categoryEntity)) {

            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());

            productGuiItem.setAction((productAction) -> {

                shopService.setPriceToProduct(product, shopType, price);
            });

            gui.setItem(product.getSlot(), productGuiItem);
        }
        gui.open(player);
    }

    private void categoryGui(Player player) {
        Gui gui = Gui.gui().type(GuiType.CHEST)
                .title(Component.text("kategorie!"))
                .rows(1)
                .create();

        for(ItemShopCategoryEntity category : shopService.getAllCategories()) {
            GuiItem categoryGuiItem = new GuiItem(category.getCategoryIcon().clone());
            try {
                gui.setItem(category.getCategorySlot(), categoryGuiItem);
            } catch (Exception e) {
                player.sendMessage("Nieprawidolwy slot dla kategori z ID: " + category.getId());
            }
            categoryGuiItem.setAction((categoryAction) -> {

                if(categoryAction.isShiftClick()) {
                    categoryAction.setCancelled(true);
                }

                if(categoryAction.isRightClick()) {
                    categoryAction.setCancelled(true);
                    setSlotForCategory((Player) categoryAction.getWhoClicked(), category);
                } else if (categoryAction.isLeftClick()) {
                    categoryAction.setCancelled(true);

                    ItemStack cursor = categoryAction.getCursor();
                    if(!Material.AIR.equals(cursor.getType())) {
                        category.setCategoryIcon(cursor);
                        shopService.createOrUpdateCategory(category);
                        player.sendMessage("Ustawiono ikone dla kategorii");
                    }
                }


            });
        }
        gui.open(player);
    }


    private void setSlotForCategory(Player player, ItemShopCategoryEntity category) {
        Gui gui = Gui.gui().type(GuiType.CHEST)
                .title(Component.text("Wybierz slot na kategorie!"))
                .rows(1)
                .create();

        gui.setDefaultClickAction(event -> {
            ItemStack item = event.getCurrentItem();

            if(item != null && !Material.AIR.equals(item.getType())) {
                player.sendMessage("Slot musi byc nullem");
                return;
            }

            category.setCategorySlot(event.getSlot());

            shopService.createOrUpdateCategory(category);
            categoryGui(player);
        });

        gui.open(player);
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        if(cmd.getName().equalsIgnoreCase("itemshopconfig")) {
            List<String> completions = new ArrayList<>();
//            if(args.length == 1) {
//                for(ShopType shopType : ShopType.values()) {
//                    completions.add(shopType.name());
//                }
//            }
            if(args.length == 1) {
                completions.add("create");
                completions.add("delete");
                completions.add("gui");
            }
            if(args.length == 2) {
                completions.add("category");
                completions.add("product");
                completions.add("price");
            }
            if(args.length == 3) {
                if("create".equalsIgnoreCase(args[0]) && "product".equalsIgnoreCase(args[1])) {

                    for(ItemShopCategoryEntity category : shopService.getAllCategories()) {
                        completions.add(category.getName());
                    }
                }

            }
            if(args.length == 3) {
                if("create".equalsIgnoreCase(args[0]) && "price".equalsIgnoreCase(args[1])) {

                    for(ItemShopCategoryEntity category : shopService.getAllCategories()) {
                        completions.add(category.getName());
                    }
                }

            }

            if(args.length == 4) {
                if("create".equalsIgnoreCase(args[0]) && "price".equalsIgnoreCase(args[1])) {

                    for (ShopType shopType : ShopType.values()) {
                        completions.add(shopType.name());
                    }
                }

            }
            if(args.length == 5) {
                if("create".equalsIgnoreCase(args[0]) && "price".equalsIgnoreCase(args[1])) {
                    for (int i = 1; i < 10; i++) {
                        completions.add(String.valueOf(i));
                    }
                }

            }
            if(args.length == 3) {
                if(args[0].equalsIgnoreCase("delete") && args[1].equalsIgnoreCase("category")) {
                    for(ItemShopCategoryEntity categoryEntity : shopService.getAllCategories()) {
                        completions.add(categoryEntity.getName());
                    }
                }
            }
            return completions;
        }
        return null;
    }
}

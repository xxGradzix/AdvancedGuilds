package me.xxgradzix.advancedclans.itemShop.guis;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.xxgradzix.advancedclans.itemShop.ItemShop;
import me.xxgradzix.advancedclans.itemShop.data.ShopService;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.itemShop.items.ItemManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AdminShopGui {

    private final ShopService shopService;

    public AdminShopGui(ShopService shopService) {
        this.shopService = shopService;
    }

    private Gui mockShopGui(ItemShopCategoryEntity categoryEntity) {

        Gui gui = Gui.gui()
                .title(Component.text("Produkty"))
                .rows(ItemShop.SHOP_SIZE)
                .create();

        GuiItem item = new GuiItem(new ItemStack(Material.BARRIER));
        item.setAction(event -> event.setCancelled(true));

        gui.getFiller().fillBetweenPoints(1, 1, 2, 9, item);
        gui.getFiller().fillBetweenPoints(6, 1, 6, 9, item);

        gui.getFiller().fillBetweenPoints(1, 1, 6, 2, item);
        gui.getFiller().fillBetweenPoints(1, 8, 6, 9, item);

        GuiItem addNewItem = new GuiItem(ItemManager.addNewItem);
        addNewItem.setAction((event) -> {

            ItemStack currentItem = event.getCursor().clone();

            if(currentItem == null || Material.AIR.equals(currentItem.getType())) {
                event.getWhoClicked().sendMessage("Musisz trzymac item w rece");
                return;
            }

            ItemShopProductEntity product = new ItemShopProductEntity(currentItem, 0, categoryEntity);

            setSlotForProduct((Player) event.getWhoClicked(), categoryEntity, product);

        });


        gui.updateItem(2, 5, addNewItem);


        return gui;
    }


    public void mainGui(Player player, ItemShopCategoryEntity categoryEntity) {
        Gui gui = mockShopGui(categoryEntity);

        for(ItemShopProductEntity product : shopService.getProductsByCategory(categoryEntity)) {

            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());

            productGuiItem.setAction((productAction) -> {

                if(productAction.isRightClick()) {
//                    chooseProductToRemoveFromShopType((Player) productAction.getWhoClicked(), categoryEntity);
                    gui.removeItem(productGuiItem);
                    shopService.deleteProduct(product);
                } else {
                    setSlotForProduct((Player) productAction.getWhoClicked(), categoryEntity, product);
                }
            });

            if(gui.getGuiItem(product.getSlot()) == null)
                gui.setItem(product.getSlot(), productGuiItem);
            else {
                gui.addItem(productGuiItem);
            }
        }

        gui.open(player);


    }

//
//
//    private void chooseProductToRemoveFromShopType(Player player, ItemShopCategoryEntity categoryEntity) {
//        Gui chooseProductGui = mockShopGui();
//
//        for(ItemShopProductEntity product : shopService.getProductsByCategory(categoryEntity)) {
//
//            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());
//
//            productGuiItem.setAction((productAction) -> {
//
//
//
//                chooseProductGui.removeItem(productGuiItem);
//                shopService.deleteProduct(product);
//            });
//
//            if(chooseProductGui.getGuiItem(product.getSlot()) == null)
//                chooseProductGui.setItem(product.getSlot(), productGuiItem);
//            else {
//                chooseProductGui.addItem(productGuiItem);
//            }
//        }
//        chooseProductGui.open(player);
//    }
//
//    private void deleteProductsFromCategory(Player player, ItemShopCategoryEntity categoryEntity) {
//
//        Gui gui = mockShopGui();
//
//        gui.updateTitle("Wybierz produkt do usuniecia!");
//
//        for(ItemShopProductEntity product : shopService.getProductsByCategory(categoryEntity)) {
//            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());
//
//            productGuiItem.setAction((productAction) -> {
//                player.sendMessage("Usunieto produkt");
//                shopService.deleteProduct(product);
//            });
//
//            gui.addItem(productGuiItem);
//        }
//        gui.open(player);
//    }

    private void setSlotForProduct(Player player, ItemShopCategoryEntity categoryEntity, ItemShopProductEntity productToSet) {

        Gui gui = mockShopGui(categoryEntity);

        gui.updateTitle("Wybierz slot w ktorym chcesz umiescic produkt!");

        gui.disableAllInteractions();

        for(ItemShopProductEntity product : shopService.getProductsByCategory(categoryEntity)) {
            GuiItem productGuiItem = new GuiItem(product.getProduct().clone());
            gui.setItem(product.getSlot(), productGuiItem);
        }

        gui.setDefaultClickAction(event -> {

            ItemStack item = event.getCurrentItem();

            if(item == null || Material.AIR.equals(item.getType())) {

                productToSet.setSlot(event.getSlot());
                productToSet.setCategory(categoryEntity);

                shopService.createOrUpdateProduct(productToSet);

                mainGui(player, categoryEntity);

            } else {

                player.sendMessage("Slot musi byc nullem");
                return;
            }


        });
        gui.open(player);
    }


    private void setPriceForProduct(Player player, ItemShopCategoryEntity categoryEntity, ShopType shopType, int price) {
        Gui gui = mockShopGui(categoryEntity);

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



}

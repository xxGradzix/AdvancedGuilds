package me.xxgradzix.advancedclans.itemShop.guis;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import me.xxgradzix.advancedclans.itemShop.ItemShop;
import me.xxgradzix.advancedclans.itemShop.data.ShopService;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductPriceType;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.itemShop.items.ItemManager;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.messages.MessageType;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import me.xxgradzix.advancedclans.utils.transaction.TransactionUtils;
import me.xxgradzix.advancedclans.utils.transaction.buyProduct.TransactionItemsProduct;
import me.xxgradzix.advancedclans.utils.transaction.buyProduct.TransactionProduct;
import me.xxgradzix.advancedclans.utils.transaction.priceType.TransactionEconomyPriceType;
import me.xxgradzix.advancedclans.utils.transaction.priceType.TransactionPriceType;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

public class ItemShopGui {

    private static final ShopService shopService = ItemShop.getShopService();

    public ItemShopGui(Player player) {
        chooseProduct(player, null);
    }

    /** BUY GUI **/

    private void buyGui(Player player, ItemShopProductEntity productEntity) {

        Gui buyGui = Gui.gui()
                .title(Component.text(ColorFixer.addColors("§f七七七七七七七七≩")))
                .rows(ItemShop.SHOP_SIZE)
                .disableAllInteractions()
                .create();

        AtomicReference<ShopType> paymentMethod = new AtomicReference<>(ShopType.MONEY);

        AtomicInteger itemStackSize = new AtomicInteger(1);
        AtomicInteger stackMultiplierValue = new AtomicInteger(1);

        updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());

        GuiItem stackMultiplicator = new GuiItem(ItemManager.createStackMultiplicatorButton(stackMultiplierValue.get()));
        stackMultiplicator.setAction((stackMultiplicatorAction) -> {

            if(stackMultiplicatorAction.isLeftClick()) {
                stackMultiplierValue.addAndGet(1);
                if(stackMultiplierValue.get() > 9) {
                    stackMultiplierValue.set(9);
                }
            } else {
                stackMultiplierValue.addAndGet(-1);
                if(stackMultiplierValue.get() < 1) {
                    stackMultiplierValue.set(1);
                }
            }
            stackMultiplicator.setItemStack(ItemManager.createStackMultiplicatorButton(stackMultiplierValue.get()));
            buyGui.updateItem(3, 5, stackMultiplicator);
            updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());
        });
        buyGui.setItem(3, 5, stackMultiplicator);


        GuiItem equal1Button = new GuiItem(ItemManager.equal1Button);
        equal1Button.setAction((equal1Action) -> {
            itemStackSize.set(1);
            updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());
        });
        buyGui.setItem(2, 2, equal1Button);

        GuiItem minus16Button = new GuiItem(ItemManager.minus16Button);
        minus16Button.setAction((minus16Action) -> {
            itemStackSize.addAndGet(-16);
            if(itemStackSize.get() < 1) {
                itemStackSize.set(1);
            }
            updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());
        });
        buyGui.setItem(2, 3, minus16Button);

        GuiItem minus1Button = new GuiItem(ItemManager.minus1Button);
        minus1Button.setAction((minus1Action) -> {
            itemStackSize.addAndGet(-1);
            if(itemStackSize.get() < 1) {
                itemStackSize.set(1);
            }
            updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());
        });
        buyGui.setItem(2, 4, minus1Button);

        GuiItem plus1Button = new GuiItem(ItemManager.plus1Button);
        plus1Button.setAction((plus1Action) -> {
            itemStackSize.addAndGet(1);
            if(itemStackSize.get() > productEntity.getProduct().getMaxStackSize()) {
                itemStackSize.set(productEntity.getProduct().getMaxStackSize());
            }
            updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());
        });
        buyGui.setItem(2, 6, plus1Button);

        GuiItem plus16Button = new GuiItem(ItemManager.plus16Button);
        plus16Button.setAction((plus16Action) -> {
            itemStackSize.addAndGet(16);
            if(itemStackSize.get() > productEntity.getProduct().getMaxStackSize()) {
                itemStackSize.set(productEntity.getProduct().getMaxStackSize());
            }
            updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());
        });
        buyGui.setItem(2, 7, plus16Button);

        GuiItem equal64Button = new GuiItem(ItemManager.equal64Button);
        equal64Button.setAction((equal64Action) -> {
            itemStackSize.set(productEntity.getProduct().getMaxStackSize());
            updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());
        });
        buyGui.setItem(2, 8, equal64Button);

        GuiItem returnButton = new GuiItem(ItemManager.returnButton);
        returnButton.setAction((returnAction) -> {
//            chooseProduct(player, paymentMethod.get(), 0);
            chooseProduct(player, productEntity.getCategory());
        });
        buyGui.setItem(4, 2, returnButton);

        GuiItem choosePaymentOption = new GuiItem(ItemManager.createChoosePaymentOptionButton(paymentMethod.get(), shopService.getProductPrices(productEntity).keySet()));
        choosePaymentOption.setAction((choosePaymentOptionAction) -> {

            shopService.getProductPrices(productEntity).keySet().stream().filter(shopType -> shopType != paymentMethod.get()).findFirst().ifPresent(paymentMethod::set);

            choosePaymentOption.setItemStack(ItemManager.createChoosePaymentOptionButton(paymentMethod.get(), shopService.getProductPrices(productEntity).keySet()));

            buyGui.updateItem(4, 8, choosePaymentOption);
            updateBuyGui(player, buyGui, paymentMethod.get(), productEntity, itemStackSize.get(), stackMultiplierValue.get());
        });
        buyGui.setItem(4, 8, choosePaymentOption);

        buyGui.open(player);

    }

    // TODO cache products prices etc

    private void updateBuyGui(Player player, Gui buyGui, ShopType paymentMethod, ItemShopProductEntity productEntity, int itemStackSize, int stackMultiplierValue) {

        Optional<ItemShopProductPriceType> priceType = shopService.getPriceType(productEntity, paymentMethod);

        if (priceType.isEmpty()) {
            return;
        }

        int amount = itemStackSize * stackMultiplierValue;

        ItemShopProductPriceType price = priceType.get();

        double pricePerItem = price.getPrice();

        double totalPrice = pricePerItem * amount;

        ItemStack showcase = new ItemStack(productEntity.getProduct());
        showcase.setAmount(itemStackSize);
        GuiItem productShowcase = new GuiItem(showcase);

        buyGui.updateItem(2, 5, productShowcase);


        GuiItem buyButton = new GuiItem(ItemManager.createProductBuyButton(productEntity.getProduct(), totalPrice, stackMultiplierValue, itemStackSize, paymentMethod));
        buyButton.setAction((buyAction) -> {

            ItemStack reward = new ItemStack(productEntity.getProduct().clone());

            reward.setAmount(itemStackSize);

            List<ItemStack> itemsToCollect = new ArrayList<>();
            for (int i = 0; i < stackMultiplierValue; i++) {
                itemsToCollect.add(reward.clone());
            }

            TransactionProduct transactionProduct = new TransactionItemsProduct(itemsToCollect);
            TransactionPriceType transactionPriceType = new TransactionEconomyPriceType(totalPrice);

            TransactionUtils.TransactionResponse purchase = TransactionUtils.purchase(player, List.of(transactionProduct), List.of(transactionPriceType));

            MessageManager.sendMessageFormated(player, purchase.getTransactionMessage(), MessageType.CHAT);

            if (purchase == TransactionUtils.TransactionResponse.SUCCESS) {
                player.closeInventory();
            }

        });
        buyGui.updateItem(5, 4, buyButton);
        buyGui.updateItem(5, 5, buyButton);
        buyGui.updateItem(5, 6, buyButton);


    }

    /** CHOOSE PRODUCT GUI **/

    private void chooseProduct(Player player, ItemShopCategoryEntity currentCategory) {

        List<ItemShopCategoryEntity> categories = shopService.getAllCategories();

        List<ItemShopCategoryEntity> categoriesSortedBySlot = new ArrayList<>(categories);
        categoriesSortedBySlot.sort(Comparator.comparingInt(ItemShopCategoryEntity::getCategorySlot));

        StringBuilder title = new StringBuilder("§f七七七七七七七七≬");


        int firstSlot = categories.stream().min(Comparator.comparing(ItemShopCategoryEntity::getCategorySlot)).map(ItemShopCategoryEntity::getCategorySlot).orElse(4);


        title.append("七".repeat(6 + (18 * (9 - firstSlot))));


        Gui chooseProductGui = Gui.gui()
                .title(Component.text("Temp"))
                .rows(ItemShop.SHOP_SIZE)
                .disableAllInteractions()
                .create();

        int previousSlot = 0;
        for(ItemShopCategoryEntity category : categoriesSortedBySlot) {

            GuiItem categoryGuiItem = new GuiItem(category.getCategoryIcon());

            categoryGuiItem.setAction((categoryAction) -> {
                chooseProduct(player, category);
            });
            chooseProductGui.setItem(category.getCategorySlot(), categoryGuiItem);

            while(previousSlot !=0 && previousSlot < category.getCategorySlot() - 1) {
                title.append("     七七");
                previousSlot++;
            }

            if (currentCategory != null && category.getId() == (currentCategory.getId())) {
                title.append("七七⎠");
            } else {
                title.append("七七⎞");
            }

            previousSlot = category.getCategorySlot();

        }

        chooseProductGui.updateTitle(ColorFixer.addColors(title.toString()));

        if(currentCategory == null) {
            chooseProductGui.open(player);
            return;
        }

        GuiItem nextPage = new GuiItem(ItemManager.nextPageButton);

        nextPage.setAction((nextPageAction) -> {
            ItemShopCategoryEntity nextCategory = categoriesSortedBySlot.stream().filter(category -> category.getCategorySlot() > currentCategory.getCategorySlot()).min(Comparator.comparingInt(ItemShopCategoryEntity::getCategorySlot)).orElse(categoriesSortedBySlot.getFirst());
            chooseProduct(player, nextCategory);
        });

        chooseProductGui.setItem(4, 8, nextPage);

        GuiItem previousPage = new GuiItem(ItemManager.previousPageButton);
        previousPage.setAction((previousPageAction) -> {
            ItemShopCategoryEntity previousCategory = categoriesSortedBySlot.stream().filter(category -> category.getCategorySlot() < currentCategory.getCategorySlot()).max(Comparator.comparingInt(ItemShopCategoryEntity::getCategorySlot)).orElse(categoriesSortedBySlot.getLast());
            chooseProduct(player, previousCategory);
        });

        chooseProductGui.setItem(4, 2, previousPage);


        for(ItemShopProductEntity product : shopService.getProductsByCategory(currentCategory)) {

            Map<ShopType, ItemShopProductPriceType> productPrices = shopService.getProductPrices(product);
            GuiItem productGuiItem = new GuiItem(ItemManager.createProductDisplayButton(product.getProduct(), productPrices));

            productGuiItem.setAction((productAction) -> {
                if(productPrices.isEmpty()) {
                    MessageManager.sendMessageFormated(player, "§cᴀᴋᴛᴜᴀʟɴɪᴇ ɴɪᴇ ᴅᴏꜱᴛęᴘɴᴇ", MessageType.CHAT);
                    return;
                }
                buyGui(player, product);
            });
            chooseProductGui.setItem(product.getSlot(), productGuiItem);
        }

        chooseProductGui.open(player);
    }


}

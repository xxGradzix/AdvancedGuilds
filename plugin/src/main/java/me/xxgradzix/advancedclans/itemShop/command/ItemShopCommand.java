package me.xxgradzix.advancedclans.itemShop.command;

import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import lombok.RequiredArgsConstructor;
import me.xxgradzix.advancedclans.itemShop.ItemShop;
import me.xxgradzix.advancedclans.itemShop.data.ShopService;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductPriceType;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import me.xxgradzix.advancedclans.itemShop.guis.ItemShopGui;
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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public class ItemShopCommand implements CommandExecutor {

    private final ShopService shopService;

    // TODO NASK
    // TODO INTEL GDANSK LATo

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(!(sender instanceof Player)) return false;

        Player player = (Player) sender;

        new ItemShopGui(player);
        return false;
    }

}

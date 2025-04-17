package me.xxgradzix.advancedclans.itemShop.command;


import me.xxgradzix.advancedclans.itemShop.data.ShopService;
import me.xxgradzix.advancedclans.itemShop.data.database.enums.ShopType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ItemShopBalanceCommand implements CommandExecutor {

    private final ShopService shopService;

    public ItemShopBalanceCommand(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

        if(!(sender instanceof Player player)) return false;

        player.sendMessage("Twoj stan konta:");
        player.sendMessage("Monety czasu: " + shopService.getPlayerBalance(player, ShopType.TIME));
        player.sendMessage("Monety zabojstw: " + shopService.getPlayerBalance(player, ShopType.KILLS));
        player.sendMessage("Pieniądze: " + shopService.getPlayerBalance(player, ShopType.MONEY));

        return true;
    }
}

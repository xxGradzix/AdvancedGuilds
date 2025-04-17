package me.xxgradzix.advancedclans.itemShop;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import lombok.Getter;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.itemShop.command.ItemShopBalanceCommand;
import me.xxgradzix.advancedclans.itemShop.command.ItemShopCommand;
import me.xxgradzix.advancedclans.itemShop.command.ItemShopConfigCommand;
import me.xxgradzix.advancedclans.itemShop.data.ShopService;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductPriceType;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopPlayerBalanceEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopCategoryRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopPlayerBalanceEntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopProductEntityRepository;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopProductPriceTypeRepository;
import me.xxgradzix.advancedclans.itemShop.items.ItemManager;
import me.xxgradzix.advancedclans.itemShop.listeners.OnPlayerKillAddPoint;
import me.xxgradzix.advancedclans.itemShop.listeners.OnPlayerJoinCreateBalanceEntity;

import java.sql.SQLException;

public class ItemShop {


    public static final int SHOP_SIZE = 6;
    private final AdvancedGuilds plugin = AdvancedGuilds.instance;

    private final ConnectionSource connectionSource;

    @Getter
    private static ShopService shopService;

    private ItemShopCategoryRepository itemShopCategoryRepository;
    private ItemShopProductEntityRepository itemShopProductEntityRepository;
    private ItemShopPlayerBalanceEntityRepository itemShopPlayerBalanceEntityRepository;
    private ItemShopProductPriceTypeRepository itemShopProductPriceTypeRepository;

    public void configureDB() throws SQLException {

        TableUtils.createTableIfNotExists(connectionSource, ItemShopProductEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ItemShopCategoryEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ItemShopPlayerBalanceEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ItemShopProductPriceType.class);

        itemShopCategoryRepository = new ItemShopCategoryRepository(connectionSource);
        itemShopProductEntityRepository = new ItemShopProductEntityRepository(connectionSource);
        itemShopPlayerBalanceEntityRepository = new ItemShopPlayerBalanceEntityRepository(connectionSource);
        itemShopProductPriceTypeRepository = new ItemShopProductPriceTypeRepository(connectionSource);
    }

    public ItemShop(AdvancedGuilds plugin, ConnectionSource connectionSource) {
        this.connectionSource = connectionSource;
    }

    public void onEnable() {

        ItemManager.init();

        try {
            configureDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        shopService = new ShopService(itemShopProductEntityRepository, itemShopCategoryRepository, itemShopPlayerBalanceEntityRepository, itemShopProductPriceTypeRepository);

        plugin.getCommand("sklep").setExecutor(new ItemShopCommand(shopService));
        plugin.getCommand("itemShopConfig").setExecutor(new ItemShopConfigCommand(shopService));
        plugin.getCommand("stankonta").setExecutor(new ItemShopBalanceCommand(shopService));

        plugin.getServer().getPluginManager().registerEvents(new OnPlayerKillAddPoint(shopService), plugin);
        plugin.getServer().getPluginManager().registerEvents(new OnPlayerJoinCreateBalanceEntity(shopService, plugin), plugin);

    }



}

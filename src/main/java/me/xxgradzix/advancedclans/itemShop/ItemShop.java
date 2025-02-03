package me.xxgradzix.advancedclans.itemShop;

import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.itemShop.command.ItemShopBalanceCommand;
import me.xxgradzix.advancedclans.itemShop.command.ItemShopCommand;
import me.xxgradzix.advancedclans.itemShop.command.ItemShopConfigCommand;
import me.xxgradzix.advancedclans.itemShop.data.DataManager;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopCategoryEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopPlayerBalanceEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.entities.ItemShopProductEntity;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopCategoryEntityManager;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopPlayerBalanceEntityManager;
import me.xxgradzix.advancedclans.itemShop.data.database.managers.ItemShopProductEntityManager;
import me.xxgradzix.advancedclans.itemShop.items.ItemManager;
import me.xxgradzix.advancedclans.itemShop.listeners.OnPlayerKillAddPoint;
import me.xxgradzix.advancedclans.itemShop.listeners.OnPlayerJoinCreateBalanceEntity;

import java.sql.SQLException;

public class ItemShop {


    public static final int SHOP_SIZE = 3;
    private final AdvancedGuilds plugin = AdvancedGuilds.instance;

    private final ConnectionSource connectionSource;

    private DataManager dataManager;

    private ItemShopCategoryEntityManager itemShopCategoryEntityManager;
    private ItemShopProductEntityManager itemShopProductEntityManager;
    private ItemShopPlayerBalanceEntityManager itemShopPlayerBalanceEntityManager;

    public void configureDB() throws SQLException {

        TableUtils.createTableIfNotExists(connectionSource, ItemShopProductEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ItemShopCategoryEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, ItemShopPlayerBalanceEntity.class);

        itemShopCategoryEntityManager= new ItemShopCategoryEntityManager(connectionSource);
        itemShopProductEntityManager = new ItemShopProductEntityManager(connectionSource);
        itemShopPlayerBalanceEntityManager = new ItemShopPlayerBalanceEntityManager(connectionSource);
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
        dataManager = new DataManager(itemShopProductEntityManager, itemShopPlayerBalanceEntityManager);

        plugin.getCommand("sklep").setExecutor(new ItemShopCommand(dataManager));
        plugin.getCommand("itemShopConfig").setExecutor(new ItemShopConfigCommand(dataManager));
        plugin.getCommand("stankonta").setExecutor(new ItemShopBalanceCommand(dataManager));

        plugin.getServer().getPluginManager().registerEvents(new OnPlayerKillAddPoint(dataManager), plugin);
        plugin.getServer().getPluginManager().registerEvents(new OnPlayerJoinCreateBalanceEntity(dataManager, plugin), plugin);

    }



}

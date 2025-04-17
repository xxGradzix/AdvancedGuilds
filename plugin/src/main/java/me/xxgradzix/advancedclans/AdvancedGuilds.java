package me.xxgradzix.advancedclans;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import me.xxgradzix.advancedclans.commands.clan.ClanCommand;
import me.xxgradzix.advancedclans.commands.hideout.HideOutAdminCommands;
import me.xxgradzix.advancedclans.commands.clan.PlayerCommand;
import me.xxgradzix.advancedclans.commands.hideout.VentureRewardCommands;
import me.xxgradzix.advancedclans.config.Config;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.VentureRewardController;
import me.xxgradzix.advancedclans.data.database.entities.hideout.storage.GuildlStorageEntity;
import me.xxgradzix.advancedclans.data.database.entities.hideout.storage.PersonalStorageEntity;
import me.xxgradzix.advancedclans.data.database.entities.hideout.venture.VentureReward;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.storage.HideoutStorageRepository;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.storage.PersonalStorageRepository;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.venture.VentureRewardRepository;
import me.xxgradzix.advancedclans.data.database.services.hideout.GuildHideOutDataService;
import me.xxgradzix.advancedclans.data.database.entities.clan.Clan;
import me.xxgradzix.advancedclans.data.database.entities.hideout.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.clan.User;
import me.xxgradzix.advancedclans.data.database.repositories.clans.ClanEntityRepository;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.data.database.repositories.hideout.GuildHideoutEntityRepository;
import me.xxgradzix.advancedclans.data.database.repositories.clans.UserEntityRepository;
import me.xxgradzix.advancedclans.data.database.services.hideout.StorageEntityDataService;
import me.xxgradzix.advancedclans.data.database.services.hideout.VentureRewardDataService;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionDto;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.expedition.ExpeditionGui;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.storage.HideoutStorage;
import me.xxgradzix.advancedclans.itemShop.ItemShop;
import me.xxgradzix.advancedclans.items.ItemManager;
import me.xxgradzix.advancedclans.listener.TestListener;
import me.xxgradzix.advancedclans.listener.clan.*;
import me.xxgradzix.advancedclans.listener.hideout.*;
import me.xxgradzix.advancedclans.data.database.controllers.clansCore.ClanController;
import me.xxgradzix.advancedclans.manager.CooldownManager;
import me.xxgradzix.advancedclans.data.database.controllers.hideouts.GuildHideOutController;
import me.xxgradzix.advancedclans.data.database.controllers.clansCore.UserController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.placeholder.ClanPlaceholder;
import me.xxgradzix.advancedclans.placeholder.VenturePlaceholder;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.stream.Stream;

public final class AdvancedGuilds extends JavaPlugin {

    public static AdvancedGuilds instance;

    private static Economy econ;

    private CooldownManager cooldownManager;

    /** REPOSITORIES **/

    private ClanEntityRepository clanEntityRepository;
    private UserEntityRepository userEntityRepository;
    private VentureRewardRepository ventureRewardRepository;
    private PersonalStorageRepository personalStorageRepository;
    private HideoutStorageRepository hideoutStorageRepository;
    private GuildHideoutEntityRepository guildHideoutEntityRepository;

    /** SERVICES **/

    private ClanAndUserDataService clanAndUserDataService;
    private GuildHideOutDataService guildHideOutDataService;
    private StorageEntityDataService storageEntityDataService;
    private VentureRewardDataService ventureRewardDataService;

    /** CONTROLLERS **/

    private UserController userController;
    private ClanController clansController;
    private GuildHideOutController guildHideOutController;
    private VentureRewardController ventureRewardController;

    /** OTHERS **/

    private TopRankScheduler topRankScheduler;
    private MessageManager messages;
    private Config config;
    private ConnectionSource connectionSource;

    Properties loadConfig() {
        Properties prop = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties");

        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return prop;
    }

    public void configureDB() throws SQLException, IOException {
        Properties config = loadConfig();

        String databaseUrl = config.getProperty("db.url");
        String user = config.getProperty("db.username");
        String password = config.getProperty("db.password");

        this.connectionSource = new JdbcConnectionSource(databaseUrl, user, password);

        TableUtils.createTableIfNotExists(connectionSource, User.class);
        TableUtils.createTableIfNotExists(connectionSource, GuildHideout.class);
        TableUtils.createTableIfNotExists(connectionSource, Clan.class);
        TableUtils.createTableIfNotExists(connectionSource, VentureReward.class);
        TableUtils.createTableIfNotExists(connectionSource, PersonalStorageEntity.class);
        TableUtils.createTableIfNotExists(connectionSource, GuildlStorageEntity.class);

        clanEntityRepository = new ClanEntityRepository(connectionSource);
        userEntityRepository = new UserEntityRepository(connectionSource);
        guildHideoutEntityRepository = new GuildHideoutEntityRepository(connectionSource);
        ventureRewardRepository = new VentureRewardRepository(connectionSource);
        personalStorageRepository = new PersonalStorageRepository(connectionSource);
        hideoutStorageRepository = new HideoutStorageRepository(connectionSource);

    }

    private ClanPlaceholder clanPlaceholder;
    private VenturePlaceholder  venturePlaceholder;

    @Override
    public void onEnable() {

        /** INSTANCES **/

        if (!setupEconomy() ) {
            getLogger().severe("Vault not found! Disabling plugin...");
            return;
        }

        instance = this;

        ItemManager.init();

        try {
            configureDB();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        new ItemShop(this, connectionSource).onEnable();

        try {
            this.messages = ConfigManager.create(MessageManager.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(this.getDataFolder(), "messages.yml"));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception exception) {
            this.getLogger().log(Level.SEVERE, "Error loading messages.yml", exception);
            return;
        }
        try {
            this.config = ConfigManager.create(Config.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(this.getDataFolder(), "config.yml"));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception exception) {
            this.getLogger().log(Level.SEVERE, "Error loading config.yml", exception);
            return;
        }

        clanAndUserDataService = new ClanAndUserDataService(clanEntityRepository, userEntityRepository);
        guildHideOutDataService = new GuildHideOutDataService(guildHideoutEntityRepository);
        ventureRewardDataService = new VentureRewardDataService(ventureRewardRepository);
        storageEntityDataService = new StorageEntityDataService(personalStorageRepository, hideoutStorageRepository);

        /** CONTROLLERS **/

        userController = new UserController();

        clansController = new ClanController(this, clanAndUserDataService);

        guildHideOutController = new GuildHideOutController(userController, guildHideOutDataService);

        ventureRewardController = new VentureRewardController();



        new ExpeditionGui(guildHideOutController, clansController);
        for (ExpeditionDto.ExpeditionObjective objective : ExpeditionDto.ExpeditionObjective.values()) {
            for (int i = 1; i <= 3; i++) {
                ExpeditionGui.refreshExpeditionRewards(objective, i);
            }
        }
        new HideoutStorage(userController, guildHideOutController, clansController);


        topRankScheduler = new TopRankScheduler(userController, clansController);
        topRankScheduler.runTaskTimerAsynchronously(this, 0, 20 * 60 * 2);

        clansController.setTopRankScheduler(topRankScheduler);
        userController.setTopRankScheduler(topRankScheduler);

        // manager

        cooldownManager = new CooldownManager();

        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            clanPlaceholder = new ClanPlaceholder(this, userController, clansController, topRankScheduler);

            venturePlaceholder = new VenturePlaceholder();
        }

        clansController.loadAllClans();
        userController.loadAllUsers();
        GuildHideOutController.loadHideOuts();


        Stream.of(
                new PlayerConnectionListener(this, cooldownManager, userController),
                new PlayerDeathListener(this, userController),
                new EntityDamageListener(this, userController),
                new AsyncPlayerChatListener(this, userController),
                new PlayerInteractionEntityListener(userController, cooldownManager),
                new HideOutUpgrade(guildHideOutController),
                new HideoutTeleportBlockClick(guildHideOutController),
                new HideoutOccupyBlockPlace(guildHideOutController),
                new HideoutStorageChestClick(),
                new InteractWithNpcEvent(),
                new TestListener(), //TODO remove
                new DHAPIHologramClickEvent()
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        getCommand("klan").setExecutor(new ClanCommand(clansController));
        getCommand("gracz").setExecutor(new PlayerCommand(userController));
        getCommand("stworzkryjowke").setExecutor(new HideOutAdminCommands(guildHideOutController));
        getCommand("ekspedycja").setExecutor(new VentureRewardCommands(ventureRewardController));
        HideoutStorage.loadGuildGuis();
        HideoutStorage.loadPersonalGuis();

    }
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return true;
    }

    public static Economy getEconomy() {
        return econ;
    }

    @Override
    public void onDisable() {
        HideoutStorage.saveGuis();
    }
}

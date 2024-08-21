package me.xxgradzix.advancedclans;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import me.xxgradzix.advancedclans.commands.ClanCommand;
import me.xxgradzix.advancedclans.commands.HideOutAdminCommands;
import me.xxgradzix.advancedclans.commands.PlayerCommand;
import me.xxgradzix.advancedclans.data.database.services.GuildHideOutDataManager;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.repositories.ClanEntityRepository;
import me.xxgradzix.advancedclans.data.database.services.ClanAndUserDataManager;
import me.xxgradzix.advancedclans.data.database.repositories.GuildHideoutEntityRepository;
import me.xxgradzix.advancedclans.data.database.repositories.UserEntityRepository;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.ExpeditionGui;
import me.xxgradzix.advancedclans.listener.*;
import me.xxgradzix.advancedclans.listener.guildHideOut.HideOutUpgrade;
import me.xxgradzix.advancedclans.controllers.ClanController;
import me.xxgradzix.advancedclans.listener.guildHideOut.HideoutOccupyBlockPlace;
import me.xxgradzix.advancedclans.listener.guildHideOut.HideoutTeleportBlockClick;
import me.xxgradzix.advancedclans.manager.CooldownManager;
import me.xxgradzix.advancedclans.controllers.GuildHideOutController;
import me.xxgradzix.advancedclans.controllers.UserController;
import me.xxgradzix.advancedclans.messages.MessageManager;
import me.xxgradzix.advancedclans.placeholder.ClanPlaceholder;
import me.xxgradzix.advancedclans.scheduler.TopRankScheduler;
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

    // manager
    private UserController userController;
    private ClanController clansController;
    private CooldownManager cooldownManager;

    private TopRankScheduler topRankScheduler;

    private ClanAndUserDataManager clanAndUserDataManager;
    private GuildHideOutController guildHideOutController;
    private MessageManager messages;
    private ConnectionSource connectionSource;

    private ClanEntityRepository clanEntityRepository;
    private UserEntityRepository userEntityRepository;
    private GuildHideoutEntityRepository guildHideoutEntityRepository;
    private GuildHideOutDataManager guildHideOutDataManager;

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

        clanEntityRepository = new ClanEntityRepository(connectionSource);
        userEntityRepository = new UserEntityRepository(connectionSource);
        guildHideoutEntityRepository = new GuildHideoutEntityRepository(connectionSource);

    }

    private ClanPlaceholder clanPlaceholder;

    @Override
    public void onEnable() {

        instance = this;

        try {
            configureDB();
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }

        try {
            this.messages = ConfigManager.create(MessageManager.class, (it) -> {
                it.withConfigurer(new YamlBukkitConfigurer());
                it.withBindFile(new File(this.getDataFolder(), "messages.yml"));
                it.saveDefaults();
                it.load(true);
            });
        } catch (Exception exception) {
            this.getLogger().log(Level.SEVERE, "Error loading messages.yml", exception);
            this.getPluginLoader().disablePlugin(this);
            return;
        }

        clanAndUserDataManager = new ClanAndUserDataManager(clanEntityRepository, userEntityRepository);
        guildHideOutDataManager = new GuildHideOutDataManager(guildHideoutEntityRepository);
        clansController = new ClanController(this, clanAndUserDataManager);

        userController = new UserController();
        guildHideOutController = new GuildHideOutController(userController, this);

        new ExpeditionGui(guildHideOutController, clansController);

        topRankScheduler = new TopRankScheduler(userController, clansController);

        topRankScheduler.runTaskTimerAsynchronously(this, 0, 20 * 60 * 2);

        clansController.setTopRankScheduler(topRankScheduler);
        userController.setTopRankScheduler(topRankScheduler);

        // manager

        cooldownManager = new CooldownManager();

        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            clanPlaceholder = new ClanPlaceholder(this, userController, clansController, topRankScheduler);
        }

        guildHideOutController.loadHideOuts();
        clansController.loadAllClans();
        userController.loadAllUsers();


        Stream.of(
                new PlayerConnectionListener(this, cooldownManager, userController),
                new PlayerDeathListener(this, userController),
                new EntityDamageListener(this, userController),
                new AsyncPlayerChatListener(this, userController),
                new PlayerInteractionEntityListener(userController, cooldownManager),
                new HideOutUpgrade(guildHideOutController),
                new HideoutTeleportBlockClick(guildHideOutController),
                new HideoutOccupyBlockPlace(guildHideOutController)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        getCommand("klan").setExecutor(new ClanCommand(clansController));
        getCommand("gracz").setExecutor(new PlayerCommand(userController));
        getCommand("stworzkryjowke").setExecutor(new HideOutAdminCommands(guildHideOutController));

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

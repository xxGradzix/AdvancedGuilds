package me.xxgradzix.advancedclans;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import eu.okaeri.configs.ConfigManager;
import eu.okaeri.configs.yaml.bukkit.YamlBukkitConfigurer;
import me.xxgradzix.advancedclans.commands.ClanCommand;
import me.xxgradzix.advancedclans.commands.PlayerCommand;
import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.data.database.repositories.ClanEntityRepository;
import me.xxgradzix.advancedclans.data.DataManager;
import me.xxgradzix.advancedclans.data.database.repositories.UserEntityRepository;
import me.xxgradzix.advancedclans.listener.*;
import me.xxgradzix.advancedclans.manager.ClanManager;
import me.xxgradzix.advancedclans.manager.CooldownManager;
import me.xxgradzix.advancedclans.manager.UserManager;
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

    private static AdvancedGuilds instance;

    // manager
    private UserManager userManager;
    private ClanManager clansManager;
    private CooldownManager cooldownManager;

    private TopRankScheduler topRankScheduler;

    private DataManager dataManager;
    private MessageManager messages;


    private ConnectionSource connectionSource;

    private ClanEntityRepository clanEntityRepository;
    private UserEntityRepository userEntityRepository;
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
        TableUtils.createTableIfNotExists(connectionSource, Clan.class);
        clanEntityRepository = new ClanEntityRepository(connectionSource);
        userEntityRepository = new UserEntityRepository(connectionSource);
    }

    private ClanPlaceholder clanPlaceholder;

    @Override
    public void onEnable() {

        instance = this;

        try {
            configureDB();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
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

        dataManager = new DataManager(clanEntityRepository, userEntityRepository);

        userManager = new UserManager(dataManager);

        clansManager = new ClanManager(this, dataManager, userManager);

        topRankScheduler = new TopRankScheduler(userManager, clansManager);

        topRankScheduler.runTaskTimerAsynchronously(this, 0, 20 * 60 * 2);

        clansManager.setTopRankScheduler(topRankScheduler);
        userManager.setTopRankScheduler(topRankScheduler);

        // manager

        cooldownManager = new CooldownManager();

        if(getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            clanPlaceholder = new ClanPlaceholder(this, userManager, clansManager, topRankScheduler);
        }
        Stream.of(
                new PlayerConnectionListener(this, cooldownManager, userManager),
                new PlayerDeathListener(this, userManager),
                new EntityDamageListener(this, userManager),
                new AsyncPlayerChatListener(this, userManager),
                new PlayerInteractionEntityListener(userManager, cooldownManager)
        ).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

        getCommand("klan").setExecutor(new ClanCommand(clansManager));
        getCommand("gracz").setExecutor(new PlayerCommand(userManager));

        clansManager.loadAllClans();
        userManager.loadAllUsers();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}

package me.xxgradzix.advancedclans.scheduler;

import me.xxgradzix.advancedclans.data.database.entities.Clan;
import me.xxgradzix.advancedclans.data.database.entities.User;
import me.xxgradzix.advancedclans.entities.PlayerStat;
import me.xxgradzix.advancedclans.entities.RankType;
import me.xxgradzix.advancedclans.manager.ClanManager;
import me.xxgradzix.advancedclans.manager.UserManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TopRankScheduler extends BukkitRunnable {

    private final UserManager userManager;
    private final ClanManager clanManager;
    private final Comparator<PlayerStat> comparator = (a, b) -> Integer.compare(b.getInt(), a.getInt());

    private final HashMap<RankType, PriorityQueue<PlayerStat>> rankData = new HashMap<>();
    public TopRankScheduler(UserManager userManager, ClanManager clanManager) {
        this.userManager = userManager;
        this.clanManager = clanManager;
    }
    @Override
    public void run() {
        // initialize sort ranking - rank data
        prepareQueue();

        // implements user and clan
        implementsUser();
        implementsClan();
    }

    private void implementsClan() {
        Queue<Clan> clanQueue = new LinkedList<>(clanManager.getClansData().values());
        int size = clanQueue.size();
        for (int i = 0; i < size; i++) {
            Clan clan = clanQueue.poll();
            addClan(clan);
        }
    }

    private void prepareQueue() {
        rankData.put(RankType.KILLS, new PriorityQueue<>(comparator));
        rankData.put(RankType.DEATHS, new PriorityQueue<>(comparator));
        rankData.put(RankType.USER_POINTS, new PriorityQueue<>(comparator));
        rankData.put(RankType.CLAN_POINTS, new PriorityQueue<>(comparator));
    }
    private void implementsUser() {
        Queue<User> userQueue = new LinkedList<>(userManager.getUserData().values());
        int queueSize = userQueue.size();
        for (int i = 0; i < queueSize; i++) {
            User user = userQueue.poll();
            assert user != null;
            addUser(user);
        }
    }

    public void addUser(User user) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(user.getUuid());
        int death = user.getDeath();
        int kills = user.getKills();
        int points = user.getPoints();
        String name = offlinePlayer.getName();
        addStats(name, kills, death, points);
    }

    public void addClan(Clan clan) {
        // check size of members fulfill threshold to counting a ranking
        if(!clanManager.doesClanFulfillThreshold(clan)) {
            return;
        }
        String tag = clan.getTag();
        String averagePoint = clanManager.getAveragePoint(clan);

        PriorityQueue<PlayerStat> playerStats = rankData.get(RankType.CLAN_POINTS);
        playerStats.add(new PlayerStat(tag, Integer.parseInt(averagePoint)));
    }

    public void removeClan(Clan clan) {
        PriorityQueue<PlayerStat> playerStats = rankData.get(RankType.CLAN_POINTS);
        PriorityQueue<PlayerStat> newQueue = new PriorityQueue<>(playerStats.comparator());
        for (PlayerStat stat : playerStats) {
            if (!stat.getName().equals(clan.getTag())) {
                newQueue.add(stat);
            }
        }
        rankData.put(RankType.CLAN_POINTS, newQueue);
    }


    private void addStats(String name, int kills, int deaths, int points) {
        // kills
        addStatToRank(RankType.KILLS, name, kills);

        // deaths
        addStatToRank(RankType.DEATHS, name, deaths);

        // user points
        addStatToRank(RankType.USER_POINTS, name, points);
    }

    private void addStatToRank(RankType rankType, String name, int value) {
        PriorityQueue<PlayerStat> playerStats = rankData.get(rankType);
        playerStats.add(new PlayerStat(name, value));
    }

    public OptionalInt getClanRankIndexByTag(String tag) {
        PriorityQueue<PlayerStat> playerStats = rankData.get(RankType.CLAN_POINTS);
        PriorityQueue<PlayerStat> tempQueue = new PriorityQueue<>(playerStats);
        PlayerStat stat;
        int index = 0;
        while ((stat = tempQueue.poll()) != null) {
            if (stat.getName().equalsIgnoreCase(tag)) {
                return OptionalInt.of(index);
            }
            index++;
        }
        return OptionalInt.empty();
    }

    public OptionalInt getUserRankByName(String username) {
        PriorityQueue<PlayerStat> playerStats = rankData.get(RankType.USER_POINTS);
        PriorityQueue<PlayerStat> tempQueue = new PriorityQueue<>(playerStats);
        PlayerStat stat;
        int index = 0;
        while ((stat = tempQueue.poll()) != null) {
            if(stat.getName()==null) {
                continue;
            }
            if (stat.getName().equals(username)) {
                return OptionalInt.of(index);
            }
            index++;
        }
        return OptionalInt.of(index);
    }


    public Optional<PlayerStat> getRank(RankType rankType, int top) {
        PriorityQueue<PlayerStat> tempQueue = new PriorityQueue<>(rankData.get(rankType));
        PlayerStat playerStat = null;
        for (int i = 0; i < top; i++) {
            playerStat = tempQueue.poll();
        }
        return Optional.ofNullable(playerStat);
    }
}

package me.xxgradzix.advancedclans.scheduler;

import entities.Clan;
import entities.User;
import me.xxgradzix.advancedclans.data.database.entities.clan.ClanImpl;
import me.xxgradzix.advancedclans.data.database.entities.clan.UserImpl;
import me.xxgradzix.advancedclans.data.database.services.clansCore.ClanAndUserDataService;
import me.xxgradzix.advancedclans.data.database.entities.clans.PlayerStat;
import me.xxgradzix.advancedclans.data.database.entities.clans.RankType;
import me.xxgradzix.advancedclans.data.database.controllers.clansCore.ClanController;
import me.xxgradzix.advancedclans.data.database.controllers.clansCore.UserController;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class TopRankScheduler extends BukkitRunnable {

    private final UserController userController;
    private final ClanController clanController;
    private final Comparator<PlayerStat> comparator = (a, b) -> Integer.compare(b.getInt(), a.getInt());

    private final HashMap<RankType, PriorityQueue<PlayerStat>> rankData = new HashMap<>();
    public TopRankScheduler(UserController userController, ClanController clanController) {
        this.userController = userController;
        this.clanController = clanController;
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
        Queue<Clan> clanImplQueue = new LinkedList<>(ClanAndUserDataService.getAllCachedClans());
        int size = clanImplQueue.size();
        for (int i = 0; i < size; i++) {
            Clan clanImpl = clanImplQueue.poll();
            addClan(clanImpl);
        }
    }

    private void prepareQueue() {
        rankData.put(RankType.KILLS, new PriorityQueue<>(comparator));
        rankData.put(RankType.DEATHS, new PriorityQueue<>(comparator));
        rankData.put(RankType.USER_POINTS, new PriorityQueue<>(comparator));
        rankData.put(RankType.CLAN_POINTS, new PriorityQueue<>(comparator));
    }
    private void implementsUser() {
        Queue<User> userImplQueue = new LinkedList<>(ClanAndUserDataService.getAllCachedUsers());
        int queueSize = userImplQueue.size();
        for (int i = 0; i < queueSize; i++) {
            User userImpl = userImplQueue.poll();
            assert userImpl != null;
            addUser(userImpl);
        }
    }

    public void addUser(User userImpl) {
        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(userImpl.getUuid());
        int death = userImpl.getDeath();
        int kills = userImpl.getKills();
        int points = userImpl.getPoints();
        String name = offlinePlayer.getName();
        addStats(name, kills, death, points);
    }

    public void addClan(Clan clanImpl) {
        // check size of members fulfill threshold to counting a ranking
        if(!clanController.doesClanFulfillThreshold(clanImpl)) {
            return;
        }
        String tag = clanImpl.getTag();
        String averagePoint = clanController.getAveragePoint(clanImpl);

        PriorityQueue<PlayerStat> playerStats = rankData.get(RankType.CLAN_POINTS);
        playerStats.add(new PlayerStat(tag, Integer.parseInt(averagePoint)));
    }

    public void removeClan(Clan clanImpl) {
        PriorityQueue<PlayerStat> playerStats = rankData.get(RankType.CLAN_POINTS);
        PriorityQueue<PlayerStat> newQueue = new PriorityQueue<>(playerStats.comparator());
        for (PlayerStat stat : playerStats) {
            if (!stat.getName().equals(clanImpl.getTag())) {
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

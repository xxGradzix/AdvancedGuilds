package me.xxgradzix.advancedclans.listener;

import com.xxgradzix.advancedguildsapi.TestEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TestListener implements Listener {

    // TODO testing - delete later
    @EventHandler
    public void test(TestEvent event){
        System.out.println("Test event triggered! in advancedclans");
    }

}

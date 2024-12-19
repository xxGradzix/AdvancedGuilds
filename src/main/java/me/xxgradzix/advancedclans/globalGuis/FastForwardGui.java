package me.xxgradzix.advancedclans.globalGuis;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

public class FastForwardGui {

    private final Player player;

    private final Gui gui;

    private final ClickAction clickAction;

    public enum ForwardType {
        GUILD_UPGRADE,
        EXPEDITION

    }

    public FastForwardGui(Player player, ClickAction clickAction) {

        this.player = player;

        this.clickAction = clickAction;

        this.gui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text("test"))
                .create();

        gui.setDefaultClickAction(event -> {
            clickAction.onClick(event.getAction());
        });

        this.gui.disableAllInteractions();
    }

}

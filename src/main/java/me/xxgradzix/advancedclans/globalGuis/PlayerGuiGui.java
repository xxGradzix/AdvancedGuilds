package me.xxgradzix.advancedclans.globalGuis;

import dev.triumphteam.gui.components.GuiType;
import dev.triumphteam.gui.guis.Gui;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class PlayerGuiGui {

    private final Player player;

    private final Gui gui;

    private static final HashMap<String, String> charMap = new HashMap<>();
    
    static {
        charMap.put("a", "𐀀");
        charMap.put("b", "𐀁");
        charMap.put("c", "𐀂");
        charMap.put("d", "𐀃");
        charMap.put("e", "𐀄");
        charMap.put("f", "𐀅");
        charMap.put("g", "𐀆");
        charMap.put("h", "𐀇");
        charMap.put("i", "𐀈");
        charMap.put("j", "𐀉");
        charMap.put("k", "𐀊");
        charMap.put("l", "𐀋");
        charMap.put("m", "𐀌");
        charMap.put("n", "𐀍");
        charMap.put("o", "𐀎");
        charMap.put("p", "𐀏");
        charMap.put("q", "𐀐");
        charMap.put("r", "𐀑");
        charMap.put("s", "𐀒");
        charMap.put("t", "𐀓");
        charMap.put("u", "𐀔");
        charMap.put("v", "𐀕");
        charMap.put("w", "𐀖");
        charMap.put("x", "𐀗");
        charMap.put("y", "𐀘");
        charMap.put("z", "𐀙");
        charMap.put("A", "𐀚");
        charMap.put("B", "𐀛");
        charMap.put("C", "𐀜");
        charMap.put("D", "𐀝");
        charMap.put("E", "𐀞");
        charMap.put("F", "𐀟");
        charMap.put("G", "𐀠");
        charMap.put("H", "𐀡");
        charMap.put("I", "𐀢");
        charMap.put("J", "𐀣");
        charMap.put("K", "𐀤");
        charMap.put("L", "𐀥");
        charMap.put("M", "𐀦");
        charMap.put("N", "𐀧");
        charMap.put("O", "𐀨");
        charMap.put("P", "𐀩");
        charMap.put("Q", "𐀪");
        charMap.put("R", "𐀫");
        charMap.put("S", "𐀬");
        charMap.put("T", "𐀭");
        charMap.put("U", "𐀮");
        charMap.put("V", "𐀯");
        charMap.put("W", "𐀰");
        charMap.put("X", "𐀱");
        charMap.put("Y", "𐀲");
        charMap.put("Z", "𐀳");
        charMap.put("0", "𐀴");
        charMap.put("1", "𐀵");
        charMap.put("2", "𐀶");
        charMap.put("3", "𐀷");
        charMap.put("4", "⎔");
        charMap.put("5", "⎕");
        charMap.put("6", "⎖");
        charMap.put("7", "⎗");
        charMap.put("8", "⎘");
        charMap.put("9", "⎙");
        charMap.put("_", "⎚");



    }
    public PlayerGuiGui(Player player) {

        this.player = player;

        StringBuilder title = new StringBuilder("&f七七七七七七七七ㇰ".replace("&", "§"));

        title.append("七七七七七七七七".replace("&", "§").repeat(10));

        String name = player.getName();

        for (Character character : player.getName().toCharArray()) {
            name = name.replaceAll(String.valueOf(character), charMap.getOrDefault(String.valueOf(character), " "));
        }

        title.append(name);

        gui = Gui.gui()
                .type(GuiType.CHEST)
                .title(Component.text(title.toString()))
                .rows(3)
                .create();

        gui.setDefaultClickAction(event -> {

        });

        gui.open(player);

        this.gui.disableAllInteractions();
    }

}

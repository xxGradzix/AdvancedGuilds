package me.xxgradzix.advancedclans.data.database.entities.hideout.fields;

import lombok.Getter;
import me.xxgradzix.advancedclans.AdvancedGuilds;
import me.xxgradzix.advancedclans.utils.ColorFixer;
import org.bukkit.Location;
import org.bukkit.World;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public enum Upgrade {
    STATION_HALL(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/upgrade-station-hall.schem"),
            10 * 60 * 60,
            1000,
            13.5,
            99.5,
            -43.5,
            Arrays.asList(
                    ColorFixer.addColors("&cᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴊᴇꜱᴛ ᴡʏᴍᴀɢᴀɴᴇ ᴅᴏ ᴢᴀᴋᴜᴘᴜ ᴜʟᴇᴘꜱᴢᴇń:"),
                    ColorFixer.addColors("&c- ᴋᴏᴡᴀʟ ɢɪʟᴅʏᴊɴʏ"),
                    ColorFixer.addColors("&c- ᴢᴀᴋʟɪɴᴀᴄᴢ ɢɪʟᴅʏᴊɴʏ"),
                    ColorFixer.addColors("&c- ᴋᴀᴘɪᴛᴀɴ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ")
            ),
            "&#9084BF&lꜱ&#8D84BC&lᴛ&#8984B9&lʀ&#8684B5&lᴇ&#8384B2&lꜰ&#7F84AF&lᴀ &#7884A9&lᴡ&#7585A6&lᴀ&#7285A2&lʀ&#6E859F&lꜱ&#6B859C&lᴢ&#688599&lᴛ&#648596&lᴀ&#618592&lᴛ&#5D858F&ló&#5A858C&lᴡ"),
    BLACKSMITH(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/blacksmith-upgrade.schem"),
            5 * 60 * 60,
            1000,
            27.0,
            95.5,
            -50.0,
            Arrays.asList(
                    ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏꜱᴛęᴘ ᴅᴏ ᴋᴜźɴɪ ɢɪʟᴅʏᴊɴᴇᴊ")
            ),
            "Stacja kowala gildyjnego"),
    WHICH(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/upgrade-which.schem"),
            5 * 60 * 60,
            1000,
            40.5,
            95.5,
            -50.0,
            Arrays.asList(),
            "Wiedźma gildyjna"),
    RESET(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/guild.schem"),
            0,
            0,
            0,
            0,
            0,
            Arrays.asList(),
            "Reset"),
    SORCERER(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/sorcery-station.schem"),
            5 * 60 * 60,
            1000,
            26.0,
            95.5,
            -38.0,
            Arrays.asList(
                    ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏꜱᴛęᴘ ᴅᴏ ᴢᴀᴋʟɪɴᴀᴄᴢᴀ ɢɪʟᴅʏᴊɴᴇɢᴏ")
            ),
            "Zaklinacz Gildyjny"),
    VENTURE(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/venture_station.schem"),
            5 * 60 * 60,
            1000,
            35,
            95.5,
            -38,
            Arrays.asList(
                    ColorFixer.addColors("&7ᴏᴅʙʟᴏᴋᴏᴡᴜᴊᴇ ᴅᴏꜱᴛęᴘ ᴅᴏ ᴋᴀᴘɪᴛᴀɴᴀ ᴇᴋꜱᴘᴇᴅʏᴄᴊɪ")
            ),
            "Ekspedycje gildyjne"),
    OUTPOST_PODEST(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/outpost-podest.schem"),
            0,
            0,
            0,
            0,
            0,
            Arrays.asList(),

            "Podest"),
    OUTPOST_HUT(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/outpost-hut.schem"),
            0,
            0,
            0,
            0,
            0,
            Arrays.asList(),

            "Budka"),
    ILLEGAL_BUSINESS_HALL(new File(AdvancedGuilds.instance.getDataFolder().getAbsolutePath() + "/upgrade-arena.schem"),
            20 * 60 * 60,
            1000,
            0.5,
            99.5,
            -54.5,
            Arrays.asList(
                    ColorFixer.addColors("&cᴛᴏ ᴜʟᴇᴘꜱᴢᴇɴɪᴇ ᴘᴏᴢᴡᴀʟᴀ ɴᴀ:"),
                    ColorFixer.addColors("&c- ᴏʀɢᴀɴɪᴢᴏᴡᴀɴɪᴇ ᴛᴜʀɴɪᴇᴊɪ ɴᴀ ᴀʀᴇɴɪᴇ ɢɪʟᴅʏᴊɴᴇᴊ"),
                    ColorFixer.addColors("&c- ᴘʀᴏᴡᴀᴅᴢᴇɴɪᴇ ᴄᴢᴀʀɴᴇɢᴏ ʀʏɴᴋᴜ "),
                    ColorFixer.addColors("&c- ᴡʏꜱᴛᴀᴡɪᴀɴɪᴇ ᴘʀᴢᴇᴅᴍɪᴏᴛóᴡ ɴᴀ ᴀᴜᴋᴄᴊᴇ ꜱᴇʀᴡᴇʀᴏᴡą")
            ),

            "&4&l&#626D4C&lꜱ&#606C4B&lᴛ&#5D6C4A&lʀ&#5B6B48&lᴇ&#596A47&lꜰ&#576A46&lᴀ &#526843&lɴ&#506842&lɪ&#4E6741&lᴇ&#4B6640&lʟ&#49663F&lᴇ&#47653D&lɢ&#45643C&lᴀ&#42643B&lʟ&#40633A&lɴ&#3E6238&lʏ&#3C6237&lᴄ&#396136&lʜ &#356034&lʙ&#335F32&lɪ&#305E31&lᴢ&#2E5E30&lɴ&#2C5D2F&lᴇ&#2A5C2D&lꜱ&#275C2C&ló&#255B2B&lᴡ"),

    STORAGE(null,
            0,
            0,
            0,
            0,
            0,
            Arrays.asList(),
            "&#EFD643ᴍ&#EAD143ᴀ&#E5CC42ɢ&#DFC742ᴀ&#DAC241ᴢ&#D5BD41ʏ&#D0B841ɴ &#C5AE40ɢ&#C0A93Fɪ&#BBA43Fʟ&#B69F3Fᴅ&#B19A3Eʏ&#AB953Eᴊ&#A6903Dɴ&#A18B3Dʏ"),
    STORAGE_1(null,
            0,
            1000,
            0,
            0,
            0,
            Arrays.asList(),
            "&#EFD643ᴍ&#EAD143ᴀ&#E5CC42ɢ&#DFC742ᴀ&#DAC241ᴢ&#D5BD41ʏ&#D0B841ɴ &#C5AE40ɢ&#C0A93Fɪ&#BBA43Fʟ&#B69F3Fᴅ&#B19A3Eʏ&#AB953Eᴊ&#A6903Dɴ&#A18B3Dʏ"),
    STORAGE_2(null,
            0,
            2000,
            0,
            0,
            0,
            Arrays.asList(),
            "&#EFD643ᴍ&#EAD143ᴀ&#E5CC42ɢ&#DFC742ᴀ&#DAC241ᴢ&#D5BD41ʏ&#D0B841ɴ &#C5AE40ɢ&#C0A93Fɪ&#BBA43Fʟ&#B69F3Fᴅ&#B19A3Eʏ&#AB953Eᴊ&#A6903Dɴ&#A18B3Dʏ"),
    STORAGE_3(null,
            0,
            3000,
            0,
            0,
            0,
            Arrays.asList(),
            "&#EFD643ᴍ&#EAD143ᴀ&#E5CC42ɢ&#DFC742ᴀ&#DAC241ᴢ&#D5BD41ʏ&#D0B841ɴ &#C5AE40ɢ&#C0A93Fɪ&#BBA43Fʟ&#B69F3Fᴅ&#B19A3Eʏ&#AB953Eᴊ&#A6903Dɴ&#A18B3Dʏ")

    ;

    @Getter
    private final File schemFile;
    @Getter
    private final int coolDown;
    @Getter
    private final int price;
    @Getter
    private final double hologramLocationX;
    @Getter
    private final double hologramLocationY;
    @Getter
    private final double hologramLocationZ;

    @Getter
    private final List<String> loreReview;



    @Getter
    private final String upgradeName;

    Upgrade(File schemFile, int coolDown, int price, double hologramLocationX, double hologramLocationY, double hologramLocationZ, List<String> loreReview, String upgradeName) {
        this.schemFile = schemFile;
        this.coolDown = coolDown;
        this.price = price;
        this.hologramLocationX = hologramLocationX;
        this.hologramLocationY = hologramLocationY;
        this.hologramLocationZ = hologramLocationZ;
        this.loreReview = loreReview;
        this.upgradeName = upgradeName;
    }

    public Location getHologramLocation(World world){
        return new Location(world, getHologramLocationX(), getHologramLocationY(), getHologramLocationZ());
    }

}

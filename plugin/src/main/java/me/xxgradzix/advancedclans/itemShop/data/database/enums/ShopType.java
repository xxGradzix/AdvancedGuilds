package me.xxgradzix.advancedclans.itemShop.data.database.enums;

import lombok.Getter;
import me.xxgradzix.advancedclans.utils.ColorFixer;

public enum ShopType {

    TIME("ᴍᴏɴᴇᴛʏ ᴄᴢᴀꜱᴜ", ColorFixer.addColors("&#084CFBᴍ&#105BFBᴏ&#1869FAɴ&#2078FAᴇ&#2886F9ᴛ&#3095F9ʏ &#3FB2F8ᴄ&#47C1F8ᴢ&#4FCFF7ᴀ&#57DEF7ꜱ&#ADF3FDᴜ")),
    KILLS("ᴍᴏɴᴇᴛʏ ᴢᴀʙóᴊꜱᴛᴡ", ColorFixer.addColors("&#EA5252ᴍ&#E35151ᴏ&#DC4F4Fɴ&#D54E4Eᴇ&#CE4D4Dᴛ&#C74C4Cʏ &#B94949ᴢ&#B24848ᴀ&#AB4646ʙ&#A44545ó&#9D4444ᴊ&#964343ꜱ&#8F4141ᴛ&#884040ᴡ")),
    MONEY("ᴘɪᴇɴɪąᴅᴢᴇ", ColorFixer.addColors("&#EAE052ᴘ&#E0D94Eɪ&#D5D14Bᴇ&#CBCA47ɴ&#C0C344ɪ&#B6BB40ą&#ABB43Cᴅ&#A1AC39ᴢ&#96A535ᴇ"));

    @Getter
    private final String name;
    @Getter
    private final String nameFormated;

    ShopType(String name, String nameFormated) {
        this.name = name;
        this.nameFormated = nameFormated;
    }
}
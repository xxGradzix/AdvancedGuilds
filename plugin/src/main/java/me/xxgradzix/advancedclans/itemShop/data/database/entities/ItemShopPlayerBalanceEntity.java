package me.xxgradzix.advancedclans.itemShop.data.database.entities;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@DatabaseTable(tableName = "advancedguilds_itemshop_player_balance")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemShopPlayerBalanceEntity {

    @DatabaseField(id = true, unique = true)
    private UUID id;

    @DatabaseField
    private int timeCoins;

    @DatabaseField
    private int killCoins;

}



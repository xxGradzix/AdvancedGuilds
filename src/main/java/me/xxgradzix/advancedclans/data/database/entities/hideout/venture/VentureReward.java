package me.xxgradzix.advancedclans.data.database.entities.hideout.venture;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.xxgradzix.advancedclans.data.database.persister.ItemStackPersister;
import me.xxgradzix.advancedclans.guildshideoutsystem.managers.stations.guis.expedition.ExpeditionDto;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

@AllArgsConstructor
@NoArgsConstructor
@DatabaseTable(tableName = "venture_reward")
public class VentureReward {

    @Getter
    @DatabaseField(generatedId = true)
    private long id;

    @Getter
    @DatabaseField(columnName = "objective")
    private ExpeditionDto.ExpeditionObjective objective;

    @Getter
    @DatabaseField(persisterClass = ItemStackPersister.class, columnDefinition = "LONGBLOB")
    private ItemStack reward;

    @Getter
    @DatabaseField(columnName = "diffLevel")
    private int diffLevel;

    @Getter
    @DatabaseField
    private int minAmount;

    @Getter
    @DatabaseField
    private int maxAmount;

    public VentureReward(ExpeditionDto.ExpeditionObjective objective, ItemStack reward, int diffLevel, int minAmount, int maxAmount) {
        this.objective = objective;
        this.reward = reward;
        this.diffLevel = diffLevel;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    public int getRandomAmount() {
        return new Random().nextInt(minAmount, maxAmount + 1);
    }
}
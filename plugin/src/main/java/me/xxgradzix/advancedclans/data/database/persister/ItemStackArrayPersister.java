package me.xxgradzix.advancedclans.data.database.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.sql.SQLException;

public class ItemStackArrayPersister extends StringType {

    private static final ItemStackArrayPersister instance = new ItemStackArrayPersister();

    public static ItemStackArrayPersister getSingleton() {
        return instance;
    }


    protected ItemStackArrayPersister() {
        super(SqlType.STRING, new Class<?>[]{ItemStack[].class});
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) throws SQLException {
        if (javaObject != null && javaObject instanceof ItemStack[]) {
            ItemStack[] inventory = (ItemStack[]) javaObject;
            return InventoryUtils.itemStackArrayToBase64(inventory);
        }
        return null;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) throws SQLException {
        if (sqlArg != null && sqlArg instanceof String) {
            String jsonString = (String) sqlArg;
            try {
                return InventoryUtils.itemStackArrayFromBase64(jsonString);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}

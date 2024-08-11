package me.xxgradzix.advancedclans.data.database.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import me.xxgradzix.advancedclans.data.database.entities.GuildHideout;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class UpgradeBooleanMap extends StringType {

    private static final UpgradeBooleanMap INSTANCE = new UpgradeBooleanMap();

    private UpgradeBooleanMap() {
        super(SqlType.STRING, new Class<?>[] { HashMap.class });
    }

    public static UpgradeBooleanMap getSingleton() {
        return INSTANCE;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        HashMap<GuildHideout.Upgrade, Boolean> myFieldClass = (HashMap<GuildHideout.Upgrade, Boolean>) javaObject;
        return myFieldClass != null ? getJsonFromItemStackClass(myFieldClass) : null;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        try {
            return sqlArg != null ? getItemStackClassFromJson((String) sqlArg) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJsonFromItemStackClass(HashMap<GuildHideout.Upgrade, Boolean> map) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(map);
            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private HashMap<GuildHideout.Upgrade, Boolean>  getItemStackClassFromJson(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            HashMap<GuildHideout.Upgrade, Boolean>  items = (HashMap<GuildHideout.Upgrade, Boolean> ) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}

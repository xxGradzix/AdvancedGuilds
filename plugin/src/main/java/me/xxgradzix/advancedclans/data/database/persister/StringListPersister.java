package me.xxgradzix.advancedclans.data.database.persister;

import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class StringListPersister extends StringType {

    private static final StringListPersister INSTANCE = new StringListPersister();

    private StringListPersister() {
        super(SqlType.STRING, new Class<?>[] { List.class });
    }

    public static StringListPersister getSingleton() {
        return INSTANCE;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        List<String> myFieldClass = (List<String>) javaObject;
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

    private String getJsonFromItemStackClass(List<String> Strings) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(Strings);
            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<String> getItemStackClassFromJson(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            List<String> items = (List<String>) dataInput.readObject();
            dataInput.close();
            return items;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }

}

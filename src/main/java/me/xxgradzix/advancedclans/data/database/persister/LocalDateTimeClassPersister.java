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
import java.time.LocalDateTime;

public class LocalDateTimeClassPersister extends StringType {

    private static final LocalDateTimeClassPersister INSTANCE = new LocalDateTimeClassPersister();

    private LocalDateTimeClassPersister() {
        super(SqlType.STRING, new Class<?>[] { LocalDateTime.class });
    }

    public static LocalDateTimeClassPersister getSingleton() {
        return INSTANCE;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        LocalDateTime myFieldClass = (LocalDateTime) javaObject;
        return myFieldClass != null ? getJsonFromLocalDateTimeClass(myFieldClass) : null;
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        try {
            return sqlArg != null ? getLocalDateTimeClassFromJson((String) sqlArg) : null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getJsonFromLocalDateTimeClass(LocalDateTime localDate) {
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);

            dataOutput.writeObject(localDate);
            dataOutput.close();

            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private LocalDateTime getLocalDateTimeClassFromJson(String data) throws IOException {
        try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);

            LocalDateTime localDate = (LocalDateTime) dataInput.readObject();
            dataInput.close();
            return localDate;
        } catch (ClassNotFoundException e) {
            throw new IOException("Unable to decode class type.", e);
        }
    }
}
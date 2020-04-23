package pl.alpheratzteam.database.api.database;

import java.util.zip.DeflaterOutputStream;
import java.io.*;

/**
 * @author hp888 on 20.04.2020.
 */

public enum DatabaseWriter
{
    INSTANCE;

    public void write(final Database database) throws IOException {
        final File databaseFile = new File("Alpheratz-Database" + File.separator + "databases", database.getName() + ".adb");
        databaseFile.getParentFile().mkdirs();

        if (databaseFile.exists())
            databaseFile.delete();

        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        this.saveCollections(dataOutputStream, database);

        final DeflaterOutputStream deflaterOutputStream = new DeflaterOutputStream(new FileOutputStream(databaseFile));
        deflaterOutputStream.write(byteArrayOutputStream.toByteArray());
        deflaterOutputStream.close();
    }

    private void saveCollections(final DataOutputStream dataOutputStream, final Database database) throws IOException {
        dataOutputStream.writeShort(database.getCollections().size());

        for (final Collection collection : database.getCollections().values())
            this.writeCollection(dataOutputStream, collection);
    }

    private void writeCollection(final DataOutputStream dataOutputStream, final Collection collection) throws IOException {
        dataOutputStream.writeUTF(collection.getName());
        dataOutputStream.writeInt(collection.getRecords().size());

        for (final String record : collection.getRecords())
            dataOutputStream.writeUTF(record);
    }
}
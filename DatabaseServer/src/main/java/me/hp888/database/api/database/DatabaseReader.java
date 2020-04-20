package me.hp888.database.api.database;

import me.hp888.database.objects.Collection;
import me.hp888.database.objects.Database;
import me.hp888.database.utils.IOUtil;
import java.util.stream.Collectors;
import java.util.zip.Inflater;
import java.util.*;
import java.io.*;
import java.util.zip.InflaterInputStream;

/**
 * @author hp888 on 19.04.2020.
 */

public enum DatabaseReader
{
    INSTANCE;

    public void read(final Database database, final File file) throws IOException {
        if (!file.exists())
            return;

        final DataInputStream dataInputStream = this.decode(file);
        final int nCollections = dataInputStream.readShort();
        final Collection[] collections = new Collection[nCollections];
        this.loadCollections(dataInputStream, collections);

        database.setCollections(convertCollections(collections));
    }

    private Map<String, Collection> convertCollections(final Collection[] collections) {
        return Arrays.stream(collections)
                .collect(Collectors.toMap(Collection::getName, collection -> collection));
    }

    private DataInputStream decode(final File file) throws IOException {
        return new DataInputStream(new ByteArrayInputStream(IOUtil.toByteArray(new InflaterInputStream(new FileInputStream(file), new Inflater()))));
    }

    private void loadCollections(final DataInputStream dataInputStream, final Collection[] collections) throws IOException {
        for (int i = 0; i < collections.length; i++)
            collections[i] = this.readCollection(dataInputStream);
    }

    private List<String> loadRecords(final DataInputStream dataInputStream, final int nRecords) throws IOException {
        final List<String> values = new ArrayList<>();
        for (int i = 0; i < nRecords; i++)
            values.add(dataInputStream.readUTF());

        return values;
    }

    private Collection readCollection(final DataInputStream dataInputStream) throws IOException {
        return new Collection(dataInputStream.readUTF(), this.loadRecords(dataInputStream, dataInputStream.readInt()));
    }
}
package pl.alpheratzteam.database.objects;

import lombok.Data;
import pl.alpheratzteam.database.api.database.DatabaseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author hp888 on 20.04.2020.
 */

@Data
public final class Database
{
    private final String name;

    public Database(final String name) {
        this.name = name;
    }

    public Database(final String name, final Map<String, Collection> collections) {
        this.name = name;
        this.collections = collections;
    }

    private Map<String, Collection> collections;
    private boolean needUpdate;

    public void save() throws IOException {
        DatabaseWriter.INSTANCE.write(this);
    }

    public Collection getCollection(final String name) {
        return collections.computeIfAbsent(name, str -> new Collection(name, new ArrayList<>()));
    }

    public void removeCollection(final String name) {
        collections.remove(name);
    }
}
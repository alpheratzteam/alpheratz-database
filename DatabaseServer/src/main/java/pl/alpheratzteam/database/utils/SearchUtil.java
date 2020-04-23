package pl.alpheratzteam.database.utils;

import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.search.SearchResponse;
import pl.alpheratzteam.database.api.database.Collection;
import pl.alpheratzteam.database.api.database.KeyData;
import java.util.Arrays;
import java.util.List;

public final class SearchUtil
{
    private static final DatabaseInitializer databaseInitializer = DatabaseInitializer.INSTANCE;

    private SearchUtil() {}

    public static int getPosition(final Collection collection, final KeyData keyData) {
        try {
            final String[] strings = collection.getRecords().stream()
                    .map(record -> databaseInitializer.getJsonParser().parse(record).getAsJsonObject()
                            .get(keyData.getKey()).getAsString()
                    )
                    .toArray(String[]::new);

            Arrays.sort(strings);
            return Arrays.binarySearch(strings, keyData.getValue());
        } catch (final ArrayIndexOutOfBoundsException ex) {
            return -1;
        }
    }

    public static SearchResponse search(final Collection collection, final KeyData keyData) {
        try {
            final List<String> records = collection.getRecords();
            final long time = System.currentTimeMillis();
            final String[] strings = records.stream()
                    .map(record -> databaseInitializer.getJsonParser().parse(record).getAsJsonObject()
                            .get(keyData.getKey()).getAsString()
                    )
                    .toArray(String[]::new);

            Arrays.sort(strings);

            final int index = Arrays.binarySearch(strings, keyData.getValue());
            return new SearchResponse(records.get(index), index, System.currentTimeMillis() - time);
        } catch (final ArrayIndexOutOfBoundsException ex) {
            return new SearchResponse("not found", -1, 0);
        }
    }
}
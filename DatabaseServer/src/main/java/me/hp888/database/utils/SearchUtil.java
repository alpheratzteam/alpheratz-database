package me.hp888.database.utils;

import me.hp888.database.DatabaseInitializer;
import me.hp888.database.api.search.SearchResponse;
import me.hp888.database.objects.Collection;
import me.hp888.database.objects.KeyData;
import java.util.Arrays;
import java.util.List;

public final class SearchUtil
{
    private static final DatabaseInitializer databaseInitializer = DatabaseInitializer.getInstance();

    private SearchUtil() {}

    public static SearchResponse search(final Collection collection, final KeyData keyData) {
        final long time = System.currentTimeMillis();

        try {
            final List<String> records = collection.getRecords();
            final String[] strings = records.stream()
                    .map(record -> databaseInitializer.getJsonParser().parse(record).getAsJsonObject()
                            .get(keyData.getKey()).getAsString()
                    )
                    .toArray(String[]::new);

            Arrays.sort(strings);
            return new SearchResponse(records.get(Arrays.binarySearch(strings, keyData.getValue())), System.currentTimeMillis() - time);
        } catch (final ArrayIndexOutOfBoundsException ex) {
            return new SearchResponse("not found", System.currentTimeMillis() - time);
        }
    }
}
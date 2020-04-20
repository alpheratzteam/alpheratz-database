package pl.alpheratzteam.database.utils;

import pl.alpheratzteam.database.DatabaseInitializer;
import pl.alpheratzteam.database.api.search.SearchResponse;
import pl.alpheratzteam.database.objects.Collection;
import pl.alpheratzteam.database.objects.KeyData;
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
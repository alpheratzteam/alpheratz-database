package pl.alpheratzteam.database.api.search;

import lombok.Data;

@Data
public final class SearchResponse
{
    private final String value;
    private final long time;
}
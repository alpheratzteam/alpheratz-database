package pl.alpheratzteam.database.utils;

import com.google.gson.Gson;
import com.google.gson.JsonParser;
import lombok.Getter;

/**
 * @author hp888 on 23.04.2020.
 */

public final class JsonUtil
{
    @Getter
    private static final JsonParser jsonParser = new JsonParser();

    @Getter
    private static final Gson gson = new Gson();

    private JsonUtil() {}
}
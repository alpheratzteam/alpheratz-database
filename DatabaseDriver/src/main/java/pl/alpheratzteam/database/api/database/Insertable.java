package pl.alpheratzteam.database.api.database;

import pl.alpheratzteam.database.utils.JsonUtil;

/**
 * @author hp888 on 26.04.2020.
 */

public interface Insertable
{
    KeyData getKeyData();

    default String toJson() {
        return JsonUtil.getGson().toJson(this, getClass());
    }
}
package pl.alpheratzteam.database.api.database;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import pl.alpheratzteam.database.utils.JsonUtil;
import java.io.Serializable;
import java.util.*;
import java.util.Collection;

/**
 * @author hp888 on 23.04.2020.
 */

public final class Document implements Map<String, Object>, Serializable
{
    private final Map<String, Object> map = new LinkedHashMap<>();

    private Document(final String jsonString) {
        final JsonObject jsonObject = JsonUtil.getJsonParser().parse(jsonString)
                .getAsJsonObject();

        jsonObject.entrySet()
                .forEach(entry -> map.put(entry.getKey(), entry.getValue()));
    }

    public Document append(final String key, final Object value) {
        map.put(key, value);
        return this;
    }

    public String getString(final String key) {
        if (!map.containsKey(key))
            return null;

        final JsonElement jsonElement = (JsonElement) map.get(key);
        return jsonElement.isJsonPrimitive() && jsonElement.getAsJsonPrimitive().isString()
                ? jsonElement.getAsJsonPrimitive().getAsString()
                : null;
    }

    public int getInteger(final String key) {
        return (int) map.get(key);
    }

    public double getDouble(final String key) {
        return (double) map.get(key);
    }

    public float getFloat(final String key) {
        return (float) map.get(key);
    }

    public boolean getBoolean(final String key) {
        return (boolean) map.get(key);
    }

    @SuppressWarnings("unchecked")
    private <T> T get(final String key, final T defaultValue) {
        final Object value = map.get(key);
        return Objects.isNull(value)
                ? defaultValue
                : (T) value;
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public boolean remove(Object key, Object value) {
        return map.remove(key, value);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public String toString() {
        return JsonUtil.getGson().toJson(this);
    }

    public static Document parse(final String jsonString) {
        return new Document(jsonString);
    }
}
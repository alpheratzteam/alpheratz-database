package pl.alpheratzteam.database.api.future;

/**
 * @author hp888 on 23.04.2020.
 */

public interface FutureListener<T>
{
    void onComplete(final T value);
}
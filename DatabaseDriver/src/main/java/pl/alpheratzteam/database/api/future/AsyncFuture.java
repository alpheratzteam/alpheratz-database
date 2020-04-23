package pl.alpheratzteam.database.api.future;

import lombok.Setter;
import java.util.Objects;

/**
 * @author hp888 on 23.04.2020.
 */

public final class AsyncFuture<T>
{
    @Setter
    private FutureListener<T> futureListener;

    public void complete(final T value) {
        if (Objects.isNull(futureListener))
            return;

        futureListener.onComplete(value);
    }
}
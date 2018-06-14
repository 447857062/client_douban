/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.functional;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;
import java.util.function.Consumer;


public class IterableCompat {

    private IterableCompat() {}

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> void forEach(Iterable<T> iterable, Consumer<T> action) {
        Objects.requireNonNull(iterable);
        for (T t : iterable) {
            action.accept(t);
        }
    }
}

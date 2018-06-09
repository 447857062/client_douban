/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.functional;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import deplink.com.douya.functional.compat.Function;
import deplink.com.douya.functional.compat.Predicate;
import deplink.com.douya.functional.extension.QuadFunction;
import deplink.com.douya.functional.extension.TriConsumer;
import deplink.com.douya.functional.extension.TriFunction;
import deplink.com.douya.functional.extension.TriPredicate;


@SuppressWarnings("unused")
public class FunctionalIterator {

    private FunctionalIterator() {}

    public static <T> boolean everyRemaining(Iterator<T> iterator, Predicate<T> predicate) {
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (!predicate.test(t)) {
                return false;
            }
        }
        return true;
    }

    public static <T> boolean everyRemaining(Iterator<T> iterator,
                                             BiPredicate<T, Integer> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (!predicate.test(t, index)) {
                return false;
            }
            ++index;
        }
        return true;
    }

    public static <I extends Iterator<T>, T> boolean everyRemaining(
            I iterator, TriPredicate<T, Integer, I> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (!predicate.test(t, index, iterator)) {
                return false;
            }
            ++index;
        }
        return true;
    }

    public static <T, J extends Collection<? super T>> J filterRemaining(Iterator<T> iterator,
                                                                         Predicate<T> predicate,
                                                                         J collector) {
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t)) {
                collector.add(t);
            }
        }
        return collector;
    }

    public static <T, J extends Collection<? super T>> ArrayList<T> filterRemaining(
            Iterator<T> iterator, Predicate<T> predicate) {
        return filterRemaining(iterator, predicate, new ArrayList<>());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, J extends Collection<? super T>> J filterRemaining(
            Iterator<T> iterator, BiPredicate<T, Integer> predicate, J collector) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t, index)) {
                collector.add(t);
            }
            ++index;
        }
        return collector;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, J extends Collection<? super T>> ArrayList<T> filterRemaining(
            Iterator<T> iterator, BiPredicate<T, Integer> predicate) {
        return filterRemaining(iterator, predicate, new ArrayList<>());
    }

    public static <I extends Iterator<T>, T, J extends Collection<? super T>> J filterRemaining(
            I iterator, TriPredicate<T, Integer, I> predicate, J collector) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t, index, iterator)) {
                collector.add(t);
            }
            ++index;
        }
        return collector;
    }

    public static <I extends Iterator<T>, T, J extends Collection<? super T>> ArrayList<T>
    filterRemaining(I iterator, TriPredicate<T, Integer, I> predicate) {
        return filterRemaining(iterator, predicate, new ArrayList<>());
    }

    public static <T> T findRemaining(Iterator<T> iterator, Predicate<T> predicate) {
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t)) {
                return t;
            }
        }
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> T findRemaining(Iterator<T> iterator, BiPredicate<T, Integer> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t, index)) {
                return t;
            }
            ++index;
        }
        return null;
    }

    public static <I extends Iterator<T>, T> T findRemaining(
            I iterator, TriPredicate<T, Integer, I> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t, index, iterator)) {
                return t;
            }
            ++index;
        }
        return null;
    }

    public static <T> int findIndexRemaining(Iterator<T> iterator, Predicate<T> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t)) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> int findIndexRemaining(Iterator<T> iterator,
                                             BiPredicate<T, Integer> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t, index)) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    public static <I extends Iterator<T>, T> int findIndexRemaining(
            I iterator, TriPredicate<T, Integer, I> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t, index, iterator)) {
                return index;
            }
            ++index;
        }
        return -1;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> void forEachRemaining(Iterator<T> iterator, Consumer<T> consumer) {
        while (iterator.hasNext()) {
            T t = iterator.next();
            consumer.accept(t);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> void forEachRemaining(Iterator<T> iterator, BiConsumer<T, Integer> consumer) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            consumer.accept(t, index);
            ++index;
        }
    }

    public static <I extends Iterator<T>, T> void forEachRemaining(
            I iterator, TriConsumer<T, Integer, I> consumer) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            consumer.accept(t, index, iterator);
            ++index;
        }
    }

    public static <T, U, J extends Collection<? super U>> J mapRemaining(Iterator<T> iterator,
                                                                         Function<T, U> function,
                                                                         J collector) {
        while (iterator.hasNext()) {
            T t = iterator.next();
            collector.add(function.apply(t));
        }
        return collector;
    }

    public static <T, U, J extends Collection<? super U>> ArrayList<U> mapRemaining(
            Iterator<T> iterator, Function<T, U> function) {
        return mapRemaining(iterator, function, new ArrayList<>());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, U, J extends Collection<? super U>> J mapRemaining(
            Iterator<T> iterator, BiFunction<T, Integer, U> function, J collector) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            collector.add(function.apply(t, index));
            ++index;
        }
        return collector;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, U, J extends Collection<? super U>> ArrayList<U> mapRemaining(
            Iterator<T> iterator, BiFunction<T, Integer, U> function) {
        return mapRemaining(iterator, function, new ArrayList<>());
    }

    public static <I extends Iterator<T>, T, U, J extends Collection<? super U>> J mapRemaining(
            I iterator, TriFunction<T, Integer, I, U> function, J collector) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            collector.add(function.apply(t, index, iterator));
            ++index;
        }
        return collector;
    }

    public static <I extends Iterator<T>, T, U, J extends Collection<? super U>> ArrayList<U>
    mapRemaining(I iterator, TriFunction<T, Integer, I, U> function) {
        return mapRemaining(iterator, function, new ArrayList<>());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, U> U reduceRemaining(Iterator<T> iterator, BiFunction<U, T, U> function,
                                           U initialValue) {
        U accumulator = initialValue;
        while (iterator.hasNext()) {
            T t = iterator.next();
            accumulator = function.apply(accumulator, t);
        }
        return accumulator;
    }

    public static <T, U> U reduceRemaining(Iterator<T> iterator,
                                           TriFunction<U, T, Integer, U> function, U initialValue) {
        U accumulator = initialValue;
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            accumulator = function.apply(accumulator, t, index);
            ++index;
        }
        return accumulator;
    }

    public static <I extends Iterator<T>, T, U> U reduceRemaining(
            I iterator, QuadFunction<U, T, Integer, I, U> function, U initialValue) {
        U accumulator = initialValue;
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            accumulator = function.apply(accumulator, t, index, iterator);
            ++index;
        }
        return accumulator;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> T reduceRemaining(Iterator<T> iterator, BiFunction<T, T, T> function) {
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("Reduce of empty iterator with no initial value");
        }
        return reduceRemaining(iterator, function, iterator.next());
    }

    public static <T> T reduceRemaining(Iterator<T> iterator,
                                        TriFunction<T, T, Integer, T> function) {
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("Reduce of empty iterator with no initial value");
        }
        return reduceRemaining(iterator, (accumulator, t, index) ->
                function.apply(accumulator, t, index + 1), iterator.next());
    }

    public static <I extends Iterator<T>, T> T reduceRemaining(
            I iterator, QuadFunction<T, T, Integer, I, T> function) {
        if (!iterator.hasNext()) {
            throw new IllegalArgumentException("Reduce of empty iterator with no initial value");
        }
        return reduceRemaining(iterator, (accumulator, t, index, iterator2) ->
                function.apply(accumulator, t, index + 1, iterator2), iterator.next());
    }

    public static <T> boolean someRemaining(Iterator<T> iterator, Predicate<T> predicate) {
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t)) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> boolean someRemaining(Iterator<T> iterator,
                                            BiPredicate<T, Integer> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t, index)) {
                return true;
            }
            ++index;
        }
        return false;
    }

    public static <I extends Iterator<T>, T> boolean someRemaining(
            I iterator, TriPredicate<T, Integer, I> predicate) {
        int index = 0;
        while (iterator.hasNext()) {
            T t = iterator.next();
            if (predicate.test(t, index, iterator)) {
                return true;
            }
            ++index;
        }
        return false;
    }
}
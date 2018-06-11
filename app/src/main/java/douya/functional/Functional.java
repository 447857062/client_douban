/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.functional;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;

import douya.functional.compat.Function;
import douya.functional.compat.Predicate;
import douya.functional.extension.QuadFunction;
import douya.functional.extension.TriConsumer;
import douya.functional.extension.TriFunction;
import douya.functional.extension.TriPredicate;


@SuppressWarnings("unused")
public class Functional {

    private Functional() {}

    public static <T> boolean every(Iterable<T> iterable, Predicate<T> predicate) {
        return FunctionalIterator.everyRemaining(iterable.iterator(), predicate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> boolean every(Iterable<T> iterable, BiPredicate<T, Integer> predicate) {
        return FunctionalIterator.everyRemaining(iterable.iterator(), predicate);
    }

    public static <I extends Iterable<T>, T> boolean every(I iterable,
                                                           TriPredicate<T, Integer, I> predicate) {
        return FunctionalIterator.everyRemaining(iterable.iterator(), (t, index, iterator) ->
                predicate.test(t, index, iterable));
    }

    public static <T, J extends Collection<? super T>> J filter(Iterable<T> iterable,
                                                                Predicate<T> predicate,
                                                                J collector) {
        return FunctionalIterator.filterRemaining(iterable.iterator(), predicate, collector);
    }

    public static <T, J extends Collection<? super T>> ArrayList<T> filter(Iterable<T> iterable,
                                                                           Predicate<T> predicate) {
        return FunctionalIterator.filterRemaining(iterable.iterator(), predicate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, J extends Collection<? super T>> J filter(Iterable<T> iterable,
                                                                BiPredicate<T, Integer> predicate,
                                                                J collector) {
        return FunctionalIterator.filterRemaining(iterable.iterator(), predicate, collector);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, J extends Collection<? super T>> ArrayList<T> filter(
            Iterable<T> iterable, BiPredicate<T, Integer> predicate) {
        return FunctionalIterator.filterRemaining(iterable.iterator(), predicate);
    }

    public static <I extends Iterable<T>, T, J extends Collection<? super T>> J filter(
            I iterable, TriPredicate<T, Integer, I> predicate, J collector) {
        return FunctionalIterator.filterRemaining(iterable.iterator(), (t, index, iterator) ->
                predicate.test(t, index, iterable), collector);
    }

    public static <I extends Iterable<T>, T, J extends Collection<? super T>> ArrayList<T> filter(
            I iterable, TriPredicate<T, Integer, I> predicate) {
        return FunctionalIterator.filterRemaining(iterable.iterator(), (t, index, iterator) ->
                predicate.test(t, index, iterable));
    }

    public static <T> T find(Iterable<T> iterable, Predicate<T> predicate) {
        return FunctionalIterator.findRemaining(iterable.iterator(), predicate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> T find(Iterable<T> iterable, BiPredicate<T, Integer> predicate) {
        return FunctionalIterator.findRemaining(iterable.iterator(), predicate);
    }

    public static <I extends Iterable<T>, T> T find(I iterable,
                                                    TriPredicate<T, Integer, I> predicate) {
        return FunctionalIterator.findRemaining(iterable.iterator(), (t, index, iterator) ->
                predicate.test(t, index, iterable));
    }

    public static <T> int findIndex(Iterable<T> iterable, Predicate<T> predicate) {
        return FunctionalIterator.findIndexRemaining(iterable.iterator(), predicate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> int findIndex(Iterable<T> iterable, BiPredicate<T, Integer> predicate) {
        return FunctionalIterator.findIndexRemaining(iterable.iterator(), predicate);
    }

    public static <I extends Iterable<T>, T> int findIndex(I iterable,
                                                           TriPredicate<T, Integer, I> predicate) {
        return FunctionalIterator.findIndexRemaining(iterable.iterator(), (t, index, iterator) ->
                predicate.test(t, index, iterable));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> void forEach(Iterable<T> iterable, Consumer<T> consumer) {
        FunctionalIterator.forEachRemaining(iterable.iterator(), consumer);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> void forEach(Iterable<T> iterable, BiConsumer<T, Integer> consumer) {
        FunctionalIterator.forEachRemaining(iterable.iterator(), consumer);
    }

    public static <I extends Iterable<T>, T> void forEach(I iterable,
                                                          TriConsumer<T, Integer, I> consumer) {
        FunctionalIterator.forEachRemaining(iterable.iterator(), (t, index, iterator) ->
                consumer.accept(t, index, iterable));
    }

    public static <T, U, J extends Collection<? super U>> J map(Iterable<T> iterable,
                                                                Function<T, U> function,
                                                                J collector) {
        return FunctionalIterator.mapRemaining(iterable.iterator(), function, collector);
    }

    public static <T, U, J extends Collection<? super U>> ArrayList<U> map(
            Iterable<T> iterable, Function<T, U> function) {
        return FunctionalIterator.mapRemaining(iterable.iterator(), function);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, U, J extends Collection<? super U>> J map(Iterable<T> iterable,
                                                                BiFunction<T, Integer, U> function,
                                                                J collector) {
        return FunctionalIterator.mapRemaining(iterable.iterator(), function, collector);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, U, J extends Collection<? super U>> ArrayList<U> map(
            Iterable<T> iterable, BiFunction<T, Integer, U> function) {
        return FunctionalIterator.mapRemaining(iterable.iterator(), function);
    }

    public static <I extends Iterable<T>, T, U, J extends Collection<? super U>> J map(
            I iterable, TriFunction<T, Integer, I, U> function, J collector) {
        return FunctionalIterator.mapRemaining(iterable.iterator(), (t, index, iterator) ->
                function.apply(t, index, iterable), collector);
    }

    public static <I extends Iterable<T>, T, U, J extends Collection<? super U>> ArrayList<U> map(
            I iterable, TriFunction<T, Integer, I, U> function) {
        return FunctionalIterator.mapRemaining(iterable.iterator(), (t, index, iterator) ->
                function.apply(t, index, iterable));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T, U> U reduce(Iterable<T> iterable, BiFunction<U, T, U> function,
                                  U initialValue) {
        return FunctionalIterator.reduceRemaining(iterable.iterator(), function, initialValue);
    }

    public static <T, U> U reduce(Iterable<T> iterable, TriFunction<U, T, Integer, U> function,
                                  U initialValue) {
        return FunctionalIterator.reduceRemaining(iterable.iterator(), function, initialValue);
    }

    public static <I extends Iterable<T>, T, U> U reduce(I iterable,
                                                         QuadFunction<U, T, Integer, I, U> function,
                                                         U initialValue) {
        return FunctionalIterator.reduceRemaining(iterable.iterator(),
                (accumulator, t, index, iterator) -> function.apply(accumulator, t, index,
                        iterable), initialValue);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> T reduce(Iterable<T> iterable, BiFunction<T, T, T> function) {
        return FunctionalIterator.reduceRemaining(iterable.iterator(), function);
    }

    public static <T> T reduce(Iterable<T> iterable, TriFunction<T, T, Integer, T> function) {
        return FunctionalIterator.reduceRemaining(iterable.iterator(), function);
    }

    public static <I extends Iterable<T>, T> T reduce(I iterable,
                                                      QuadFunction<T, T, Integer, I, T> function) {
        return FunctionalIterator.reduceRemaining(iterable.iterator(),
                (accumulator, t, index, iterator) -> function.apply(accumulator, t, index,
                        iterable));
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> T reduceRight(List<T> iterable, BiFunction<T, T, T> function) {
        return FunctionalIterator.reduceRemaining(new ReverseIterator<>(iterable), function);
    }

    public static <T> T reduceRight(List<T> list, TriFunction<T, T, Integer, T> function) {
        return FunctionalIterator.reduceRemaining(new ReverseIterator<>(list),
                (previousValue, t, index, iterator) -> function.apply(previousValue, t,
                        list.size() - 1 - index));
    }

    public static <I extends List<T>, T> T reduceRight(I list,
                                                       QuadFunction<T, T, Integer, I, T> function) {
        return FunctionalIterator.reduceRemaining(new ReverseIterator<>(list),
                (previousValue, t, index, iterator) -> function.apply(previousValue, t,
                        list.size() - 1 - index, list));
    }

    private static class ReverseIterator<T> implements Iterator<T> {

        private ListIterator<T> mListIterator;

        public ReverseIterator(List<T> list) {
            mListIterator = list.listIterator(list.size());
        }

        @Override
        public boolean hasNext() {
            return mListIterator.hasPrevious();
        }

        @Override
        public T next() {
            return mListIterator.previous();
        }
    }

    public static <T> boolean some(Iterable<T> iterable, Predicate<T> predicate) {
        return FunctionalIterator.someRemaining(iterable.iterator(), predicate);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static <T> boolean some(Iterable<T> iterable, BiPredicate<T, Integer> predicate) {
        return FunctionalIterator.someRemaining(iterable.iterator(), predicate);
    }

    public static <I extends Iterable<T>, T> boolean some(I iterable,
                                                          TriPredicate<T, Integer, I> predicate) {
        return FunctionalIterator.someRemaining(iterable.iterator(), (t, index, iterator) ->
                predicate.test(t, index, iterable));
    }
}

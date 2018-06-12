/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.Movie;
import douya.network.api.info.frodo.SimpleMovie;
import douya.util.FragmentUtils;

public class MovieResource extends BaseItemResource<SimpleMovie, Movie> {

    private static final String FRAGMENT_TAG_DEFAULT = MovieResource.class.getName();

    private static MovieResource newInstance(long movieId, SimpleMovie simpleMovie, Movie movie) {
        //noinspection deprecation
        MovieResource instance = new MovieResource();
        instance.setArguments(movieId, simpleMovie, movie);
        return instance;
    }

    public static MovieResource attachTo(long movieId, SimpleMovie simpleMovie, Movie movie,
                                         Fragment fragment, String tag, int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        MovieResource instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            instance = newInstance(movieId, simpleMovie, movie);
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static MovieResource attachTo(long movieId, SimpleMovie simpleMovie, Movie movie,
                                         Fragment fragment) {
        return attachTo(movieId, simpleMovie, movie, fragment, FRAGMENT_TAG_DEFAULT,
                REQUEST_CODE_INVALID);
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public MovieResource() {}

    @Override
    protected CollectableItem.Type getDefaultItemType() {
        return CollectableItem.Type.MOVIE;
    }
}

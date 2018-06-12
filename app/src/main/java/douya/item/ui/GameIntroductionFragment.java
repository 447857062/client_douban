/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.ui;

import android.support.v4.util.Pair;

import com.douya.R;

import java.util.ArrayList;
import java.util.List;

import douya.network.api.info.frodo.Game;


public class GameIntroductionFragment extends BaseItemIntroductionFragment<Game> {

    public static GameIntroductionFragment newInstance(Game game) {
        //noinspection deprecation
        GameIntroductionFragment fragment = new GameIntroductionFragment();
        fragment.setArguments(game);
        return fragment;
    }

    /**
     * @deprecated Use {@link #newInstance(Game)} instead.
     */
    public GameIntroductionFragment() {}

    @Override
    protected List<Pair<String, String>> makeInformationData() {
        List<Pair<String, String>> data = new ArrayList<>();
        addTextListToData(R.string.item_introduction_game_genres, mItem.genres, data);
        addTextListToData(R.string.item_introduction_game_platforms, mItem.getPlatformNames(),
                data);
        addTextListToData(R.string.item_introduction_game_alternative_titles,
                mItem.alternativeTitles, data);
        addTextListToData(R.string.item_introduction_game_developers, mItem.developers, data);
        addTextListToData(R.string.item_introduction_game_publishers, mItem.publishers, data);
        addTextToData(R.string.item_introduction_game_release_date, mItem.releaseDate, data);
        return data;
    }
}

/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.item.content;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import douya.network.api.info.frodo.CollectableItem;
import douya.network.api.info.frodo.Game;
import douya.network.api.info.frodo.SimpleGame;
import douya.util.FragmentUtils;

public class GameResource extends BaseItemResource<SimpleGame, Game> {

    private static final String FRAGMENT_TAG_DEFAULT = GameResource.class.getName();

    private static GameResource newInstance(long gameId, SimpleGame simpleGame, Game game) {
        //noinspection deprecation
        GameResource instance = new GameResource();
        instance.setArguments(gameId, simpleGame, game);
        return instance;
    }

    public static GameResource attachTo(long gameId, SimpleGame simpleGame, Game game,
                                        Fragment fragment, String tag, int requestCode) {
        FragmentActivity activity = fragment.getActivity();
        GameResource instance = FragmentUtils.findByTag(activity, tag);
        if (instance == null) {
            instance = newInstance(gameId, simpleGame, game);
            FragmentUtils.add(instance, activity, tag);
        }
        instance.setTarget(fragment, requestCode);
        return instance;
    }

    public static GameResource attachTo(long gameId, SimpleGame simpleGame, Game game,
                                        Fragment fragment) {
        return attachTo(gameId, simpleGame, game, fragment, FRAGMENT_TAG_DEFAULT,
                REQUEST_CODE_INVALID);
    }

    /**
     * @deprecated Use {@code attachTo()} instead.
     */
    public GameResource() {}

    @Override
    protected CollectableItem.Type getDefaultItemType() {
        return CollectableItem.Type.GAME;
    }
}

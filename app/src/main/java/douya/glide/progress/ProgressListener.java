/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.glide.progress;

import android.support.v4.util.SimpleArrayMap;

import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.GlideUrl;

import douya.glide.progress.okhttp3.OkHttpUrlLoader;


public abstract class ProgressListener {

    public abstract void onProgress(long bytesRead, long contentLength, boolean done);

    /**
     * @see SimpleArrayMap#hashCode() Which is used by {@link Options#hashCode()} because we are
     * setting listener to null in
     * {@link OkHttpUrlLoader#buildLoadData(GlideUrl, int, int, Options)}.
     */
    @Override
    public int hashCode() {
        return 0;
    }
}

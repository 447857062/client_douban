/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package deplink.com.douya.network.api.info;

import android.content.Context;

public interface ClipboardCopyable {

    String getClipboardLabel(Context context);

    String getClipboardText(Context context);
}

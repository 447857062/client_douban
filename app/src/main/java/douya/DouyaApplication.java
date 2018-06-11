package douya;

import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

import com.bumptech.glide.request.target.ViewTarget;
import com.facebook.stetho.Stetho;
import com.jakewharton.threetenabp.AndroidThreeTen;

import deplink.com.douya.BuildConfig;
import deplink.com.douya.R;
import douya.fabric.FabricUtils;
import douya.util.NightModeHelper;

/**
 * Created by ${kelijun} on 2018/6/4.
 */

public class DouyaApplication extends Application {
    private static DouyaApplication sInstance;
    public DouyaApplication(){
        sInstance=this;
    }
    public static DouyaApplication getInstance(){
        return sInstance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        NightModeHelper.setup(this);
        AndroidThreeTen.init(this);
        FabricUtils.init(this);
        ViewTarget.setTagId(R.id.glide_view_target_tag);
        Stetho.initializeWithDefaults(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }
}

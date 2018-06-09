package deplink.com.douya;

import android.app.Application;
import android.os.Build;
import android.webkit.WebView;

import com.facebook.stetho.Stetho;

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

        Stetho.initializeWithDefaults(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (BuildConfig.DEBUG) {
                WebView.setWebContentsDebuggingEnabled(true);
            }
        }
    }
}

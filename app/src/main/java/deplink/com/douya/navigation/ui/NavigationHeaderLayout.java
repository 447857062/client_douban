package deplink.com.douya.navigation.ui;

import android.accounts.Account;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.ButterKnife;
import deplink.com.douya.R;
import deplink.com.douya.network.api.info.apiv2.SimpleUser;
import deplink.com.douya.network.api.info.frodo.User;
import deplink.com.douya.util.DrawableUtils;
import deplink.com.douya.util.ViewUtils;

/**
 * Created by ${kelijun} on 2018/6/5.
 */

public class NavigationHeaderLayout extends FrameLayout {
    @BindView(R.id.backdrop)
    ImageView mBackdropImage;
    @BindView(R.id.scrim)
    View mScrimView;
    @BindViews({R.id.avatar, R.id.fade_out_avatar,
            R.id.recent_one_avatar, R.id.fade_out_recent_one_avatar,
            R.id.recent_two_avatar, R.id.fade_out_recent_two_avatar})
    ImageView[] mAvatarImages;
    @BindView(R.id.avatar)
    ImageView mAvatarImage;
    @BindView(R.id.fade_out_avatar)
    ImageView mFadeOutAvatarImage;
    @BindView(R.id.recent_one_avatar)
    ImageView mRecentOneAvatarImage;
    @BindView(R.id.fade_out_recent_one_avatar)
    ImageView mFadeOutRecentOneAvatarImage;
    @BindView(R.id.recent_two_avatar)
    ImageView mRecentTwoAvatarImage;
    @BindView(R.id.fade_out_recent_two_avatar)
    ImageView mFadeOutRecentTwoAvatarImage;
    @BindView(R.id.info)
    LinearLayout mInfoLayout;
    @BindView(R.id.name)
    TextView mNameText;
    @BindView(R.id.description)
    TextView mDescriptionText;
    @BindView(R.id.dropDown)
    ImageView mDropDownImage;
    private Adapter mAdapter;
    private Listener mListener;
    public NavigationHeaderLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public NavigationHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NavigationHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public NavigationHeaderLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
    }
    private void init() {

        ViewUtils.inflateInto(R.layout.navigation_header_layout, this);
        ButterKnife.bind(this);

        mBackdropImage.setImageResource(R.drawable.profile_header_backdrop);
        ViewCompat.setBackground(mScrimView, DrawableUtils.makeScrimDrawable());
        mInfoLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
              //  showAccountList(!mShowingAccountList);
            }
        });
    }
    public void setListener(Listener listener) {
        mListener = listener;
    }

    public void bind() {

        if (mAdapter == null) {
            return;
        }

     //   bindActiveUser();
      //  bindRecentUsers();
    }
    public interface Adapter {
        SimpleUser getPartialUser(Account account);
        User getUser(Account account);
    }

    public interface Listener {
        void openProfile(Account account);
        void showAccountList(boolean show);
        void onAccountTransitionStart();
        void onAccountTransitionEnd();
    }
}

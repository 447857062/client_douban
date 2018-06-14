/*
 * Copyright (c) 2018 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.douya.R;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import douya.broadcast.content.SendBroadcastCommentManager;
import douya.eventbus.BroadcastCommentSendErrorEvent;
import douya.eventbus.BroadcastCommentSentEvent;
import douya.eventbus.EventBusUtils;
import douya.ui.ConfirmDiscardContentDialogFragment;
import douya.ui.FragmentFinishable;
import douya.util.FragmentUtils;
import douya.util.ToastUtils;

public class SendCommentFragment extends Fragment
        implements ConfirmDiscardContentDialogFragment.Listener {

    private static final String KEY_PREFIX = SendCommentFragment.class.getName() + '.';

    private static final String EXTRA_BROADCAST_ID = KEY_PREFIX + "broadcast_id";
    private static final String EXTRA_TEXT = KEY_PREFIX + "text";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.text)
    EditText mTextEdit;

    private MenuItem mSendCommentMenuItem;

    private long mBroadcastId;
    private CharSequence mText;

    private boolean mCommentSent;

    public static SendCommentFragment newInstance(long broadcastId, CharSequence text) {
        //noinspection deprecation
        SendCommentFragment fragment = new SendCommentFragment();
        FragmentUtils.getArgumentsBuilder(fragment)
                .putLong(EXTRA_BROADCAST_ID, broadcastId)
                .putCharSequence(EXTRA_TEXT, text);
        return fragment;
    }

    /**
     * @deprecated Use {@link #newInstance(long, CharSequence)} instead.
     */
    public SendCommentFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        mBroadcastId = arguments.getLong(EXTRA_BROADCAST_ID);
        mText = arguments.getCharSequence(EXTRA_TEXT);

        setHasOptionsMenu(true);

        EventBusUtils.register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.broadcast_send_comment_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.setSupportActionBar(mToolbar);

        if (savedInstanceState == null) {
            mTextEdit.setText(mText);
        }
        updateSendCommentStatus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        EventBusUtils.unregister(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.broadcast_send_comment, menu);
        mSendCommentMenuItem = menu.findItem(R.id.action_send_comment);
        updateSendCommentStatus();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onFinish();
                return true;
            case R.id.action_send_comment:
                onSendComment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSendComment() {
        String text = mTextEdit.getText().toString();
        if (TextUtils.isEmpty(text)) {
            ToastUtils.show(R.string.broadcast_send_comment_error_empty, getActivity());
            return;
        }
        sendComment(text);
    }

    private void sendComment(String comment) {
        SendBroadcastCommentManager.getInstance().write(mBroadcastId, comment, getActivity());
        updateSendCommentStatus();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBroadcastCommentSent(BroadcastCommentSentEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcastId == mBroadcastId) {
            mCommentSent = true;
            finish();
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onBroadcastCommentSendError(BroadcastCommentSendErrorEvent event) {

        if (event.isFromMyself(this)) {
            return;
        }

        if (event.broadcastId == mBroadcastId) {
            updateSendCommentStatus();
        }
    }

    private void updateSendCommentStatus() {
        if (mCommentSent) {
            return;
        }
        SendBroadcastCommentManager manager = SendBroadcastCommentManager.getInstance();
        boolean sendingComment = manager.isWriting(mBroadcastId);
        getActivity().setTitle(sendingComment ? R.string.broadcast_send_comment_title_sending
                : R.string.broadcast_send_comment_title);
        boolean enabled = !sendingComment;
        mTextEdit.setEnabled(enabled);
        if (mSendCommentMenuItem != null) {
            mSendCommentMenuItem.setEnabled(enabled);
        }
        if (sendingComment) {
            mTextEdit.setText(manager.getComment(mBroadcastId));
        }
    }

    public void onFinish() {
        CharSequence text = mTextEdit.getText();
        if (text.length() > 0 && !TextUtils.equals(text, mText)) {
            ConfirmDiscardContentDialogFragment.show(this);
        } else {
            finish();
        }
    }

    @Override
    public void discardContent() {
        finish();
    }

    private void finish() {
        FragmentFinishable.finish(getActivity());
    }
}

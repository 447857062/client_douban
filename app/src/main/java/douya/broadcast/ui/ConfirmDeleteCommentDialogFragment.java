/*
 * Copyright (c) 2015 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.broadcast.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.douya.R;

import douya.util.FragmentUtils;


public class ConfirmDeleteCommentDialogFragment extends AppCompatDialogFragment {

    private static final String KEY_PREFIX = ConfirmDeleteCommentDialogFragment.class.getName()
            + '.';

    private static final String EXTRA_COMMENT_ID = KEY_PREFIX + "comment_id";

    private long mCommentId;

    /**
     * @deprecated Use {@link #newInstance(long)} instead.
     */
    public ConfirmDeleteCommentDialogFragment() {}

    public static ConfirmDeleteCommentDialogFragment newInstance(long commentId) {
        //noinspection deprecation
        ConfirmDeleteCommentDialogFragment fragment = new ConfirmDeleteCommentDialogFragment();
        FragmentUtils.getArgumentsBuilder(fragment)
                .putLong(EXTRA_COMMENT_ID, commentId);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mCommentId = getArguments().getLong(EXTRA_COMMENT_ID);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setMessage(R.string.broadcast_comment_delete_confirm)
                .setPositiveButton(R.string.ok, (dialog, which) -> getListener().deleteComment(
                        mCommentId))
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    private Listener getListener() {
        return (Listener) getParentFragment();
    }

    public static void show(long commentId, Fragment fragment) {
        ConfirmDeleteCommentDialogFragment.newInstance(commentId)
                .show(fragment.getChildFragmentManager(), null);
    }

    public interface Listener {
        void deleteComment(long commentId);
    }
}

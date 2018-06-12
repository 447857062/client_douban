/*
 * Copyright (c) 2017 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package douya.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

import com.douya.R;


public class ConfirmDiscardContentDialogFragment extends AppCompatDialogFragment {

    /**
     * @deprecated Use {@link #newInstance()} instead.
     */
    public ConfirmDiscardContentDialogFragment() {}

    public static ConfirmDiscardContentDialogFragment newInstance() {
        //noinspection deprecation
        return new ConfirmDiscardContentDialogFragment();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity(), getTheme())
                .setMessage(R.string.confirm_close_discarding_content)
                .setPositiveButton(R.string.discard,
                        (dialog, which) -> getListener().discardContent())
                .setNegativeButton(R.string.keep_editing, null)
                .create();
    }

    private Listener getListener() {
        return (Listener) getParentFragment();
    }

    public static void show(Fragment fragment) {
        ConfirmDiscardContentDialogFragment.newInstance()
                .show(fragment.getChildFragmentManager(), null);
    }

    public interface Listener {
        void discardContent();
    }
}

package com.daniel.minesweeper;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Daniel on 1/10/2015.
 */
public class WinDialog extends AlertDialog {

    protected WinDialog(Context context) {
        super(context);
    }

    protected WinDialog(Context context, int theme) {
        super(context, theme);
    }

    protected WinDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}

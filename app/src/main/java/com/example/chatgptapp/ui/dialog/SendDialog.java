package com.example.chatgptapp.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

public class SendDialog extends Dialog implements DialogInterface.OnDismissListener {
    Context context;
    EditText et_reply_comment;

    public SendDialog(Context context, int themeResId, EditText ed) {
        super(context, themeResId);
        this.context = context;
        this.et_reply_comment = ed;
    }

    @Override
    public void cancel() {
        super.cancel();

        InputMethodManager inputMgr = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);

        hideKeyboard(context, et_reply_comment);
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {
        InputMethodManager inputMgr = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMgr.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);

        hideKeyboard(context, et_reply_comment);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isShowing() && shouldCloseOnTouch(getContext(), event)) {
            hideKeyboard(context, et_reply_comment);
        }
        return super.onTouchEvent(event);
    }

    public boolean shouldCloseOnTouch(Context context, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN
                && isOutOfBounds(context, event) && getWindow().peekDecorView() != null) {
            return true;
        }
        return false;
    }

    private boolean isOutOfBounds(Context context, MotionEvent event) {
        final int x = (int) event.getX();
        final int y = (int) event.getY();
        final int slop = ViewConfiguration.get(context).getScaledWindowTouchSlop();
        final View decorView = getWindow().getDecorView();
        return (x < -slop) || (y < -slop)
                || (x > (decorView.getWidth() + slop))
                || (y > (decorView.getHeight() + slop));
    }

    // 关闭键盘
    public static void hideKeyboard(Context context, EditText et) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }

}

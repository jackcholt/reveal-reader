package com.jackcholt.reveal;

import java.text.MessageFormat;

import android.content.Context;

import com.jackcholt.reveal.data.History;
import com.jackcholt.reveal.data.YbkDAO;

/**
 * For asking people to confirm the delete.
 * 
 * @author Shon Vella
 * @author Jack Holt
 */

public abstract class DeleteBookmarkDialog {

    public static void create(final Context _this, final History hist) {
        SafeRunnable action = new SafeRunnable() {
            @Override
            public void protectedRun() {
                YbkDAO ybkDao = YbkDAO.getInstance(_this);
                ybkDao.deleteBookmark(hist);

            }
        };

        String message = MessageFormat.format(_this.getResources().getString(R.string.confirm_delete_bookmark),
                hist.title);
        ConfirmActionDialog.confirmedAction(_this, R.string.really_delete_bookmark, message, R.string.delete, action);
    }
}
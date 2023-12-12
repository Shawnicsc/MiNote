package net.micode.notes.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.widget.SimpleCursorAdapter;

public class MyCursorAdapter extends SimpleCursorAdapter {

    public MyCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }
}

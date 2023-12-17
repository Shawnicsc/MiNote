/*
 * Copyright (c) 2010-2011, The MiCode Open Source Community (www.micode.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.micode.notes.model;

import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import net.micode.notes.data.Notes;
import net.micode.notes.data.Notes.CallNote;
import net.micode.notes.data.Notes.DataColumns;
import net.micode.notes.data.Notes.DataConstants;
import net.micode.notes.data.Notes.NoteColumns;
import net.micode.notes.data.Notes.TextNote;
import net.micode.notes.data.NotesDatabaseHelper;
import net.micode.notes.tool.ResourceParser.NoteBgResources;


public class WorkingNote {
    // Note for the working note
    private Note mNote;
    // Note Id
    private long mNoteId;
    // Note content
    public String mContent;
    // Note mode
    private int mMode;

    private long mAlertDate;

    private long mModifiedDate;

    private int mBgColorId;

    private int mWidgetId;

    private int mWidgetType;

    private long mFolderId;
    private String mTop;

    private Context mContext;

    private static final String TAG = "WorkingNote";

    private boolean mIsDeleted;

    private NoteSettingChangedListener mNoteSettingStatusListener;
    //新添加的密码变量
    private String mPassword;
    //添加密码标志
    public String mLocker;

    public static final String[] DATA_PROJECTION = new String[] {
            DataColumns.ID,
            DataColumns.CONTENT,
            DataColumns.MIME_TYPE,
            DataColumns.DATA1,
            DataColumns.DATA2,
            DataColumns.DATA3,
            DataColumns.DATA4,
    };

    public static final String[] NOTE_PROJECTION = new String[] {
            NoteColumns.PARENT_ID,
            NoteColumns.ALERTED_DATE,
            NoteColumns.BG_COLOR_ID,
            NoteColumns.WIDGET_ID,
            NoteColumns.WIDGET_TYPE,
            NoteColumns.MODIFIED_DATE,
            //增加一个密码项
            NoteColumns.PASSWORD,
            NoteColumns.TOP,
            NoteColumns.LOCKER,
    };
    public static final String[] UP_NOTE_PROJECTION= new String[] {
                    NoteColumns.ID,
                    NoteColumns.PARENT_ID,
                    NoteColumns.ALERTED_DATE,
                    NoteColumns.BG_COLOR_ID,
                    NoteColumns.CREATED_DATE ,
                    NoteColumns.HAS_ATTACHMENT,
                    NoteColumns.MODIFIED_DATE,
                    NoteColumns.NOTES_COUNT ,
                    NoteColumns.SNIPPET ,
                    NoteColumns.TYPE ,
                    NoteColumns.WIDGET_ID,
                    NoteColumns.WIDGET_TYPE ,
                    NoteColumns.SYNC_ID,
                    NoteColumns.LOCAL_MODIFIED ,
                    NoteColumns.ORIGIN_PARENT_ID,
                    NoteColumns.GTASK_ID ,
                    NoteColumns.VERSION ,
                    NoteColumns.PASSWORD,
                    NoteColumns.TOP,
                    NoteColumns.LOCKER,

    };
    public static final String[] UP_DATA_PROJECTION =new String[]{
                    DataColumns.ID ,
                    DataColumns.MIME_TYPE ,
                    DataColumns.NOTE_ID ,
                    NoteColumns.CREATED_DATE ,
                    NoteColumns.MODIFIED_DATE ,
                    DataColumns.CONTENT,
                    DataColumns.DATA1 ,
                    DataColumns.DATA2 ,
                    DataColumns.DATA3 ,
                    DataColumns.DATA4 ,
                    DataColumns.DATA5,
    };

    private static final int DATA_ID_COLUMN = 0;

    private static final int DATA_CONTENT_COLUMN = 1;

    private static final int DATA_MIME_TYPE_COLUMN = 2;

    private static final int DATA_MODE_COLUMN = 3;

    private static final int NOTE_PARENT_ID_COLUMN = 0;

    private static final int NOTE_ALERTED_DATE_COLUMN = 1;

    private static final int NOTE_BG_COLOR_ID_COLUMN = 2;

    private static final int NOTE_WIDGET_ID_COLUMN = 3;

    private static final int NOTE_WIDGET_TYPE_COLUMN = 4;

    private static final int NOTE_MODIFIED_DATE_COLUMN = 5;
    //密码
    private static final int NOTE_PASSWORD_COLUMN=6;
    //密码标志
    private static final int NOTE_LOCKER_COLUMN=8;


    /**
     * 设置便签的访问密码
     * @param  password 新的访问密码
     */
    public void setPassword(String password){
        //将该类的mPassword属性设置为新的访问密码
        mPassword = password;
        //将修改后的便签密码写入到便签数据库中
        mNote.setNoteValue(NoteColumns.PASSWORD,mPassword);
    }
    public boolean hasPassword()
    {
        if (mPassword!=null && mPassword.length()!=0)
        //if (!mPassword.equals(""))
        {
            Log.d("密码是：", String.valueOf(true));
            Log.d("密码是",mPassword);
            return true;
        }
        else{
            Log.d("密码是：", String.valueOf(false));
            return false;
        }


    }
    //获取密码
    public String getmPassword(){
        return mPassword;
    }
    //设置密码标志
    public void setLocker(String mlocker){
        mLocker=mlocker;//1标志为上锁了  0标志为没有上锁
        mNote.setNoteValue(NoteColumns.LOCKER,mLocker);
    }

    // New note construct
    private WorkingNote(Context context, long folderId) {
        mContext = context;
        mAlertDate = 0;
        mModifiedDate = System.currentTimeMillis();
        mFolderId = folderId;
        mNote = new Note();
        mNoteId = 0;
        mIsDeleted = false;
        mMode = 0;
        mTop = "0";
        mWidgetType = Notes.TYPE_WIDGET_INVALIDE;
        //导入便签
        loadNote();
    }

    // Existing note construct
    private WorkingNote(Context context, long noteId, long folderId) {
        mContext = context;
        mNoteId = noteId;
        mFolderId = folderId;
        mIsDeleted = false;
        mNote = new Note();
        mTop = "0";
        loadNote();
    }

    private void loadNote() {
        Cursor cursor = mContext.getContentResolver().query(
                ContentUris.withAppendedId(Notes.CONTENT_NOTE_URI, mNoteId), NOTE_PROJECTION, null,
                null, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                mFolderId = cursor.getLong(NOTE_PARENT_ID_COLUMN);
                mBgColorId = cursor.getInt(NOTE_BG_COLOR_ID_COLUMN);
                mWidgetId = cursor.getInt(NOTE_WIDGET_ID_COLUMN);
                mWidgetType = cursor.getInt(NOTE_WIDGET_TYPE_COLUMN);
                mAlertDate = cursor.getLong(NOTE_ALERTED_DATE_COLUMN);
                mModifiedDate = cursor.getLong(NOTE_MODIFIED_DATE_COLUMN);
                //密码
                mPassword= cursor.getString(NOTE_PASSWORD_COLUMN);
                //密码标志
                mLocker=cursor.getString(NOTE_LOCKER_COLUMN);
            }
            cursor.close();
        } else {
            Log.e(TAG, "No note with id:" + mNoteId);
            throw new IllegalArgumentException("Unable to find note with id " + mNoteId);
        }
        loadNoteData();
    }

    private void loadNoteData() {
        Cursor cursor = mContext.getContentResolver().query(Notes.CONTENT_DATA_URI, DATA_PROJECTION,
                DataColumns.NOTE_ID + "=?", new String[] {
                    String.valueOf(mNoteId)
                }, null);

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    String type = cursor.getString(DATA_MIME_TYPE_COLUMN);
                    if (DataConstants.NOTE.equals(type)) {
                        mContent = cursor.getString(DATA_CONTENT_COLUMN);
                        mMode = cursor.getInt(DATA_MODE_COLUMN);
                        mNote.setTextDataId(cursor.getLong(DATA_ID_COLUMN));
                    } else if (DataConstants.CALL_NOTE.equals(type)) {
                        mNote.setCallDataId(cursor.getLong(DATA_ID_COLUMN));
                    } else {
                        Log.d(TAG, "Wrong note type with type:" + type);
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            Log.e(TAG, "No data with id:" + mNoteId);
            throw new IllegalArgumentException("Unable to find note's data with id " + mNoteId);
        }
    }

    public static WorkingNote createEmptyNote(Context context, long folderId, int widgetId,
            int widgetType, int defaultBgColorId) {
        WorkingNote note = new WorkingNote(context, folderId);
        note.setBgColorId(defaultBgColorId);
        note.setWidgetId(widgetId);
        note.setWidgetType(widgetType);
        return note;
    }

    public static WorkingNote load(Context context, long id) {
        return new WorkingNote(context, id, 0);
    }

    public synchronized boolean saveNote() {
        if (isWorthSaving()) {
            if (!existInDatabase()) {
                if ((mNoteId = Note.getNewNoteId(mContext, mFolderId)) == 0) {
                    Log.e(TAG, "Create new note fail with id:" + mNoteId);
                    return false;
                }
            }

            mNote.syncNote(mContext, mNoteId);

            /**
             * Update widget content if there exist any widget of this note
             */
            if (mWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID
                    && mWidgetType != Notes.TYPE_WIDGET_INVALIDE
                    && mNoteSettingStatusListener != null) {
                mNoteSettingStatusListener.onWidgetChanged();
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean existInDatabase() {
        return mNoteId > 0;
    }

    private boolean isWorthSaving() {
        if (mIsDeleted || (!existInDatabase() && TextUtils.isEmpty(mContent))
                || (existInDatabase() && !mNote.isLocalModified())) {
            return false;
        } else {
            return true;
        }
    }

    public void setOnSettingStatusChangedListener(NoteSettingChangedListener l) {
        mNoteSettingStatusListener = l;
    }

    public void setAlertDate(long date, boolean set) {
        if (date != mAlertDate) {
            mAlertDate = date;
            mNote.setNoteValue(NoteColumns.ALERTED_DATE, String.valueOf(mAlertDate));
        }
        if (mNoteSettingStatusListener != null) {
            mNoteSettingStatusListener.onClockAlertChanged(date, set);
        }
    }

    public void setTop(String Top){
        if(Top != mTop){
            mTop = Top;
            mNote.setTopValue(NoteColumns.TOP,mTop);
        }
    }

    public void markDeleted(boolean mark) {
        mIsDeleted = mark;
        if (mWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID
                && mWidgetType != Notes.TYPE_WIDGET_INVALIDE && mNoteSettingStatusListener != null) {
                mNoteSettingStatusListener.onWidgetChanged();
        }
    }

    public void setBgColorId(int id) {
        if (id != mBgColorId) {
            mBgColorId = id;
            if (mNoteSettingStatusListener != null) {
                mNoteSettingStatusListener.onBackgroundColorChanged();
            }
            mNote.setNoteValue(NoteColumns.BG_COLOR_ID, String.valueOf(id));
        }
    }

    public void setCheckListMode(int mode) {
        if (mMode != mode) {
            if (mNoteSettingStatusListener != null) {
                mNoteSettingStatusListener.onCheckListModeChanged(mMode, mode);
            }
            mMode = mode;
            mNote.setTextData(TextNote.MODE, String.valueOf(mMode));
        }
    }

    public void setWidgetType(int type) {
        if (type != mWidgetType) {
            mWidgetType = type;
            mNote.setNoteValue(NoteColumns.WIDGET_TYPE, String.valueOf(mWidgetType));
        }
    }

    public void setWidgetId(int id) {
        if (id != mWidgetId) {
            mWidgetId = id;
            mNote.setNoteValue(NoteColumns.WIDGET_ID, String.valueOf(mWidgetId));
        }
    }

    public void setWorkingText(String text) {
        if (!TextUtils.equals(mContent, text)) {
            mContent = text;
            mNote.setTextData(DataColumns.CONTENT, mContent);
        }
    }

    public void convertToCallNote(String phoneNumber, long callDate) {
        mNote.setCallData(CallNote.CALL_DATE, String.valueOf(callDate));
        mNote.setCallData(CallNote.PHONE_NUMBER, phoneNumber);
        mNote.setNoteValue(NoteColumns.PARENT_ID, String.valueOf(Notes.ID_CALL_RECORD_FOLDER));
    }

    public boolean hasClockAlert() {
        return (mAlertDate > 0 ? true : false);
    }

    public String getContent() {
        return mContent;
    }

    public long getAlertDate() {
        return mAlertDate;
    }

    public long getModifiedDate() {
        return mModifiedDate;
    }

    public int getBgColorResId() {
        return NoteBgResources.getNoteBgResource(mBgColorId);
    }

    public int getBgColorId() {
        return mBgColorId;
    }

    public int getTopId(){
        if(mTop.equals("1")){
            return 1;
        }else{
            return 0 ;
        }
    }

    public int getTitleBgResId() {
        return NoteBgResources.getNoteTitleBgResource(mBgColorId);
    }

    public int getCheckListMode() {
        return mMode;
    }

    public long getNoteId() {
        return mNoteId;
    }

    public long getFolderId() {
        return mFolderId;
    }

    public int getWidgetId() {
        return mWidgetId;
    }

    public int getWidgetType() {
        return mWidgetType;
    }

    public interface NoteSettingChangedListener {
        /**
         * Called when the background color of current note has just changed
         */
        void onBackgroundColorChanged();

        /**
         * Called when user set clock
         */
        void onClockAlertChanged(long date, boolean set);

        /**
         * Call when user create note from widget
         */
        void onWidgetChanged();

        /**
         * Call when switch between check list mode and normal mode
         * @param oldMode is previous mode before change
         * @param newMode is new mode
         */
        void onCheckListModeChanged(int oldMode, int newMode);
    }
}

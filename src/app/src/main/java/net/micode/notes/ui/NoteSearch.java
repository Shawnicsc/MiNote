package net.micode.notes.ui;

import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;

import net.micode.notes.R;
import net.micode.notes.data.Notes;
import net.micode.notes.data.NotesDatabaseHelper;
import net.micode.notes.data.NotesProvider;

@SuppressLint("NewApi")
public class NoteSearch extends ListActivity implements SearchView.OnQueryTextListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_search_list);
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(Notes.CONTENT_NOTE_URI);
        }
        SearchView searchview = (SearchView)findViewById(R.id.search_view);
        //为查询文本框注册监听器
        searchview.setOnQueryTextListener(NoteSearch.this);
    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        // 获取用户在搜索框中输入的文本
        String searchText = "%" + s + "%";

        // 创建内容提供者 URI
        Uri contentUri = Notes.CONTENT_NOTE_URI;

        // 执行查询操作，小米便签中 在NoteProvider中已经封装好搜索的功能，使用ContentResolver 调用对应的方法即可
        ContentResolver resolver = getContentResolver();
        String[] projection = NoteItemData.PROJECTION;
        String selection = Notes.NoteColumns.SNIPPET + " Like ? ";
        String[] selectionArgs = { searchText };
        Cursor cursor = resolver.query(contentUri, projection, selection, selectionArgs, null);
        String[] dataColumns = { Notes.NoteColumns.SNIPPET , Notes.NoteColumns.MODIFIED_DATE};
        int[] viewIDs = { android.R.id.text1 , R.id.text1_time };

        MyCursorAdapter adapter = new MyCursorAdapter(
                this,
                R.layout.noteslist_item,
                cursor,
                dataColumns,
                viewIDs
        );
        setListAdapter(adapter);
        return true;
    }

}

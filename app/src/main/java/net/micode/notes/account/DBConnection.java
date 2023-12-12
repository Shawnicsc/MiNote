package net.micode.notes.account;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


import net.micode.notes.data.Notes;
import net.micode.notes.data.NotesDatabaseHelper;
import net.micode.notes.model.Note;
import net.micode.notes.model.WorkingNote;
import net.micode.notes.ui.PassWordEditActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnection {
    private static final String CREATE_MY_NOTE_TABLE_SQL =
            NotesDatabaseHelper.TABLE.NOTE + "(" +
                    Notes.NoteColumns.ID + " BIGINT PRIMARY KEY," +
                    Notes.NoteColumns.PARENT_ID + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.ALERTED_DATE + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.BG_COLOR_ID + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.CREATED_DATE + " BIGINT," +
                    Notes.NoteColumns.HAS_ATTACHMENT + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.MODIFIED_DATE + " BIGINT ," +
                    Notes.NoteColumns.NOTES_COUNT + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.SNIPPET + " TEXT," +
                    Notes.NoteColumns.TYPE + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.WIDGET_ID + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.WIDGET_TYPE + " BIGINT NOT NULL DEFAULT -1," +
                    Notes.NoteColumns.SYNC_ID + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.LOCAL_MODIFIED + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.ORIGIN_PARENT_ID + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.GTASK_ID + " TEXT," +
                    Notes.NoteColumns.VERSION + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.PASSWORD + " TEXT ," +
                    Notes.NoteColumns.TOP + " INTEGER NOT NULL DEFAULT 0," +
                    Notes.NoteColumns.LOCKER + " INTEGER NOT NULL DEFAULT 0" +

                    //NoteColumns.PASSWORD+ " INTEGER NOT NULL DEFAULT 0" +
                    ")";
    private static final String CREATE_MY_DATA_TABLE_SQL =
            NotesDatabaseHelper.TABLE.DATA + "(" +
                    Notes.DataColumns.ID + " BIGINT PRIMARY KEY," +
                    Notes.DataColumns.MIME_TYPE + " TEXT ," +
                    Notes.DataColumns.NOTE_ID + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.DataColumns.CREATED_DATE + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.DataColumns.MODIFIED_DATE + " BIGINT NOT NULL DEFAULT 0," +
                    Notes.DataColumns.CONTENT + " TEXT ," +
                    Notes.DataColumns.DATA1 + " BIGINT," +
                    Notes.DataColumns.DATA2 + " BIGINT," +
                    Notes.DataColumns.DATA3 + " TEXT ," +
                    Notes.DataColumns.DATA4 + " TEXT ," +
                    Notes.DataColumns.DATA5 + " TEXT" +
                    ")";
    private static final String INSERT_NOTE_TABLE_SQL =
            "(" +
                    Notes.NoteColumns.ID + "," +
                    Notes.NoteColumns.PARENT_ID + "," +
                    Notes.NoteColumns.ALERTED_DATE + "," +
                    Notes.NoteColumns.BG_COLOR_ID + "," +
                    Notes.NoteColumns.CREATED_DATE + "," +
                    Notes.NoteColumns.HAS_ATTACHMENT + "," +
                    Notes.NoteColumns.MODIFIED_DATE + "," +
                    Notes.NoteColumns.NOTES_COUNT + "," +
                    Notes.NoteColumns.SNIPPET + "," +
                    Notes.NoteColumns.TYPE + "," +
                    Notes.NoteColumns.WIDGET_ID + "," +
                    Notes.NoteColumns.WIDGET_TYPE + "," +
                    Notes.NoteColumns.SYNC_ID + "," +
                    Notes.NoteColumns.LOCAL_MODIFIED + "," +
                    Notes.NoteColumns.ORIGIN_PARENT_ID + "," +
                    Notes.NoteColumns.GTASK_ID + "," +
                    Notes.NoteColumns.VERSION + "," +
                    Notes.NoteColumns.PASSWORD + "," +
                    Notes.NoteColumns.TOP + "," +
                    Notes.NoteColumns.LOCKER  +
                    ")";
    private static final String  INSERT_DATA_TABLE_SQL =
            "(" +
                    Notes.DataColumns.ID + "," +
                    Notes.DataColumns.MIME_TYPE + "," +
                    Notes.DataColumns.NOTE_ID + "," +
                    Notes.DataColumns.CREATED_DATE + "," +
                    Notes.DataColumns.MODIFIED_DATE + "," +
                    Notes.DataColumns.CONTENT + "," +
                    Notes.DataColumns.DATA1 + "," +
                    Notes.DataColumns.DATA2 + "," +
                    Notes.DataColumns.DATA3 + "," +
                    Notes.DataColumns.DATA4 + "," +
                    Notes.DataColumns.DATA5 +
                    ")";
    public static boolean register(String user_account, String user_password, String user_password_asked) {
        System.out.println("运行到link了");
        //数据库的地址和账号密码
        final String url = "jdbc:mysql://rm-cn-zpr3i7lfm001tlpo.rwlb.rds.aliyuncs.com/xiaomi_serve?useSSL=false&ampserverTimezone=GMT";
        final String user = "shawnii";
        final String password = "ww@2003929";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sqlRegister;
        String sqlFind;
        try {
            //1、加载驱动
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("驱动加载成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //2、注册 传送数据到数据库
            if (!user_password.equals(user_password_asked)) {//判断两次密码输入是否一致
                System.out.println("两次输入密码不一致");
                return false;
            } else {
                System.out.println("两次输入密码一致");
            }
            /**
             * 查找用户是否存在
             */
            sqlFind = "SELECT * FROM xiaomi_serve.note ";
            preparedStatement = conn.prepareStatement(sqlFind);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String user_account_registered = resultSet.getString("account");
                if (user_account_registered.equals(user_account)) {
                    System.out.println("该账号已经注册");
                    return false;
                }
            }
            sqlRegister = "REPLACE INTO xiaomi_serve.note(account,password,note_id,data_id,is_pushsync) values (?,?,?,?,?)";
            preparedStatement = conn.prepareStatement(sqlRegister);
            preparedStatement.setString(1, user_account);
            preparedStatement.setString(2, user_password);
            preparedStatement.setString(3, user_account + "_note");
            preparedStatement.setString(4, user_account + "_data");
            preparedStatement.setString(5, "0");//0标志该账户还没有同步上传过，意味着还没有创建相应的表
            System.out.println("注册账号是：" + user_account);
            System.out.println("注册密码是：" + user_password);
            //注册的时候创建相应得表
            sqlFind="CREATE TABLE " + user_account +"_"+CREATE_MY_NOTE_TABLE_SQL;
            PreparedStatement preparedStatementTable = conn.prepareStatement(sqlFind);
            preparedStatementTable.execute();

            sqlFind="CREATE TABLE " + user_account +"_"+CREATE_MY_DATA_TABLE_SQL;
            preparedStatementTable = conn.prepareStatement(sqlFind);
            preparedStatementTable.execute();
            System.out.println("创建成功");
            //
            int result = preparedStatement.executeUpdate();
            if (result != 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    public static boolean login(String user_account, String user_password) {
        System.out.println("运行到login了");
        //数据库的地址和账号密码
        final String url = "jdbc:mysql://rm-cn-zpr3i7lfm001tlpo.rwlb.rds.aliyuncs.com/xiaomi_serve?useSSL=false&ampserverTimezone=GMT";
        final String user = "shawnii";
        final String password = "ww@2003929";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sqlFind;
        try {
            //1、加载驱动
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("驱动加载成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            //2、登录
            /**
             * 查找用户是否存在
             */
            sqlFind = "SELECT * FROM xiaomi_serve.note ";
            preparedStatement = conn.prepareStatement(sqlFind);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String user_account_registered = resultSet.getString("account");
                String user_password_registered = resultSet.getString("password");
                //判断账号密码正确性
                if (user_account_registered.equals(user_account)) {
                    System.out.println("账号正确");
                    if (user_password_registered.equals(user_password)) {
                        System.out.println("密码正确");
                        //
                        /**sqlFind="CREATE TABLE " + user_account +"_"+CREATE_MY_NOTE_TABLE_SQL;
                        preparedStatement=conn.prepareStatement(sqlFind);
                        preparedStatement.execute();*/
                        //
                        return true;
                    } else {
                        System.out.println("密码不正确");
                        return false;
                    }
                } else {
                    System.out.println("账号不正确");
                }
            }
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    //同步上传到云端数据库函数
    public static boolean pushSync(String user_account, String user_password, Context context) {
        System.out.println("运行到pushsync了");
        //数据库的地址和账号密码
        final String url = "jdbc:mysql://rm-cn-zpr3i7lfm001tlpo.rwlb.rds.aliyuncs.com/xiaomi_serve?useSSL=false&ampserverTimezone=GMT";
        final String user = "shawnii";
        final String password = "ww@2003929";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sqlFind;
        String sql;
        String user_is_pushsync = "0";//标志该用户是否第一次上传
        try {
            //1、加载驱动
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("驱动加载成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            /**
             * 查找用户是否存在
             */
            sqlFind = "SELECT * FROM xiaomi_serve.note ";
            conn = DriverManager.getConnection(url, user, password);
            preparedStatement = conn.prepareStatement(sqlFind);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String user_account_registered = resultSet.getString("account");
                if (user_account_registered.equals(user_account)) {
                    break;
                }
            }
                //conn=DriverManager.getConnection(url, user, password);
                    conn = DriverManager.getConnection(url, user, password);
                    Cursor cursor = context.getContentResolver().query(Notes.CONTENT_NOTE_URI, WorkingNote.UP_NOTE_PROJECTION,
                            null, null, null);
                    //上传先删除云端库对应的表
                    sql = "DELETE FROM " + user_account + "_note";
                    PreparedStatement psDelNote = conn.prepareStatement(sql);
                    psDelNote.execute();
                    //再同步
                    sql = "INSERT INTO " + user_account + "_note " + INSERT_NOTE_TABLE_SQL +
                            " VALUES (?,?,?,?,?,?," +
                            "?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                    if (cursor != null) {
                        if (cursor.moveToFirst()) {
                            do {
                                PreparedStatement psUpNPTE = conn.prepareStatement(sql);
                                psUpNPTE.setLong(1,cursor.getLong(0));
                                psUpNPTE.setLong(2,cursor.getLong(1));
                                psUpNPTE.setLong(3,cursor.getLong(2));
                                psUpNPTE.setLong(4,cursor.getLong(3));
                                psUpNPTE.setLong(5,cursor.getLong(4));
                                psUpNPTE.setLong(6,cursor.getLong(5));
                                psUpNPTE.setLong(7,cursor.getLong(6));
                                psUpNPTE.setLong(8,cursor.getLong(7));
                                psUpNPTE.setString(9,cursor.getString(8));
                                psUpNPTE.setLong(10,cursor.getLong(9));
                                psUpNPTE.setLong(11,cursor.getLong(10));
                                psUpNPTE.setLong(12,cursor.getLong(11));
                                psUpNPTE.setLong(13,cursor.getLong(12));
                                psUpNPTE.setLong(14,cursor.getLong(13));
                                psUpNPTE.setLong(15,cursor.getLong(14));
                                psUpNPTE.setString(16,cursor.getString(15));
                                psUpNPTE.setLong(17,cursor.getLong(16));
                                psUpNPTE.setString(18,cursor.getString(17));
                                psUpNPTE.setLong(19,cursor.getLong(18));
                                psUpNPTE.setLong(20,cursor.getLong(19));
                                psUpNPTE.execute();
                            } while (cursor.moveToNext());
                        cursor.close();

                    } else {
                        Log.e("upSync" , "failed");
                    }
                    //************上传DATA表原理同Note表**************
                sql = "DELETE FROM " + user_account + "_data";
                PreparedStatement psDelData = conn.prepareStatement(sql);
                psDelData.execute();
                cursor = context.getContentResolver().query(Notes.CONTENT_DATA_URI, WorkingNote.UP_DATA_PROJECTION,
                        null, null, null);
                sql = "INSERT INTO " + user_account + "_data " + INSERT_DATA_TABLE_SQL +
                        " VALUES (?,?,?,?,?,?,?,?,?,?,?)";

                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            PreparedStatement psUpData = conn.prepareStatement(sql);
                            psUpData.setLong(1,cursor.getLong(0));
                            psUpData.setString(2,cursor.getString(1));
                            psUpData.setLong(3,cursor.getLong(2));
                            psUpData.setLong(4,cursor.getLong(3));
                            psUpData.setLong(5,cursor.getLong(4));
                            psUpData.setString(6,cursor.getString(5));
                            psUpData.setLong(7,cursor.getLong(6));
                            psUpData.setLong(8,cursor.getLong(7));
                            psUpData.setString(9,cursor.getString(8));
                            psUpData.setString(10,cursor.getString(9));
                            psUpData.setString(11,cursor.getString(10));
                            psUpData.execute();
                        } while (cursor.moveToNext());
                    }
                    cursor.close();
                } else {
                    Log.e("upSync" , "failed");
                }
                        //将note表中是否第一次上传标志为1
                sql = "REPLACE INTO xiaomi_serve.note(account,is_pushsync) values (?,?)";
                preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, user_account);
                preparedStatement.setString(2, "1");//0标志该账户还没有同步上传过，1标志已经同步上传过
                        Looper.prepare();
                        Toast.makeText(context, "上传成功!", Toast.LENGTH_SHORT).show();
                        Looper.loop();
                return true;
            }
        } catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }
    public static boolean pullSync(String user_account, String user_password, Context context){
        System.out.println("运行到pullsync了");
        //数据库的地址和账号密码
        final String url = "jdbc:mysql://rm-cn-zpr3i7lfm001tlpo.rwlb.rds.aliyuncs.com/xiaomi_serve?useSSL=false&ampserverTimezone=GMT";
        final String user = "shawnii";
        final String password = "ww@2003929";
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sqlFind;
        String sql;
        String user_is_pushsync = "0";//标志该用户是否第一次上传
        try {
            //1、加载驱动
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("驱动加载成功！！！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            /**
             * 查找用户是否存在
             */
            sqlFind = "SELECT * FROM xiaomi_serve.note ";
            conn = DriverManager.getConnection(url, user, password);
            preparedStatement = conn.prepareStatement(sqlFind);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String user_account_registered = resultSet.getString("account");
                if (user_account_registered.equals(user_account)) {
                    break;
                }
            }
            ContentResolver cr = context.getContentResolver();
            conn = DriverManager.getConnection(url, user, password);
            //先删除本地数据库中两个表的内容
            String selectionNote= Notes.NoteColumns.TYPE+" >= 0";
            String selectionData=Notes.NoteColumns.ID+" >= 0";
            cr.delete(Notes.CONTENT_NOTE_URI, selectionNote, null);
            cr.delete(Notes.CONTENT_DATA_URI, selectionData, null);
            //
            sql = "SELECT * FROM " + user_account + "_note";
            PreparedStatement psDownNoteTable = conn.prepareStatement(sql);
            ResultSet rs = psDownNoteTable.executeQuery();
            //在一行行便利，每行逐列写入本地数据库
            while(rs.next()){
                ContentValues cv = new ContentValues();
                cv.put(Notes.NoteColumns.ID, rs.getLong(Notes.NoteColumns.ID));
                cv.put(Notes.NoteColumns.PARENT_ID, rs.getLong(Notes.NoteColumns.PARENT_ID));
                cv.put(Notes.NoteColumns.ALERTED_DATE, rs.getLong(Notes.NoteColumns.ALERTED_DATE));
                cv.put(Notes.NoteColumns.BG_COLOR_ID, rs.getLong(Notes.NoteColumns.BG_COLOR_ID));
                cv.put(Notes.NoteColumns.CREATED_DATE, rs.getLong(Notes.NoteColumns.CREATED_DATE));
                cv.put(Notes.NoteColumns.HAS_ATTACHMENT, rs.getLong(Notes.NoteColumns.HAS_ATTACHMENT));
                cv.put(Notes.NoteColumns.MODIFIED_DATE, rs.getLong(Notes.NoteColumns.MODIFIED_DATE));
                cv.put(Notes.NoteColumns.NOTES_COUNT, rs.getLong(Notes.NoteColumns.NOTES_COUNT));
                cv.put(Notes.NoteColumns.SNIPPET, rs.getString(Notes.NoteColumns.SNIPPET));
                cv.put(Notes.NoteColumns.TYPE, rs.getLong(Notes.NoteColumns.TYPE));
                cv.put(Notes.NoteColumns.WIDGET_ID, rs.getLong(Notes.NoteColumns.WIDGET_ID));
                cv.put(Notes.NoteColumns.WIDGET_TYPE, rs.getLong(Notes.NoteColumns.WIDGET_TYPE));
                cv.put(Notes.NoteColumns.SYNC_ID, rs.getLong(Notes.NoteColumns.SYNC_ID));
                cv.put(Notes.NoteColumns.LOCAL_MODIFIED, rs.getLong(Notes.NoteColumns.LOCAL_MODIFIED));
                cv.put(Notes.NoteColumns.ORIGIN_PARENT_ID, rs.getLong(Notes.NoteColumns.ORIGIN_PARENT_ID));
                cv.put(Notes.NoteColumns.GTASK_ID, rs.getString(Notes.NoteColumns.GTASK_ID));
                cv.put(Notes.NoteColumns.VERSION, rs.getLong(Notes.NoteColumns.VERSION));
                cv.put(Notes.NoteColumns.PASSWORD, rs.getString(Notes.NoteColumns.PASSWORD));
                cv.put(Notes.NoteColumns.TOP,rs.getLong(Notes.NoteColumns.TOP));
                cv.put(Notes.NoteColumns.LOCKER,rs.getLong(Notes.NoteColumns.LOCKER));
                cr.insert(Notes.CONTENT_NOTE_URI, cv);
            }
            Log.d("midd", "data");
            /****同步data和同步Note同理*******/
            sql = "SELECT * FROM " + user_account + "_data";
            PreparedStatement psDownDataTable = conn.prepareStatement(sql);
            rs= psDownDataTable.executeQuery();
            while(rs.next()){
                ContentValues cv = new ContentValues();
                cv.put(Notes.DataColumns.ID, rs.getLong(Notes.DataColumns.ID));
                cv.put(Notes.DataColumns.MIME_TYPE, rs.getString(Notes.DataColumns.MIME_TYPE));
                cv.put(Notes.DataColumns.NOTE_ID, rs.getLong(Notes.DataColumns.NOTE_ID));
                cv.put(Notes.DataColumns.CREATED_DATE, rs.getLong(Notes.DataColumns.CREATED_DATE));
                cv.put(Notes.DataColumns.MODIFIED_DATE, rs.getLong(Notes.DataColumns.MODIFIED_DATE));
                cv.put(Notes.DataColumns.CONTENT, rs.getString(Notes.DataColumns.CONTENT));
                cv.put(Notes.DataColumns.DATA1, rs.getLong(Notes.DataColumns.DATA1));
                cv.put(Notes.DataColumns.DATA2, rs.getLong(Notes.DataColumns.DATA2));
                cv.put(Notes.DataColumns.DATA3, rs.getString(Notes.DataColumns.DATA3));
                cv.put(Notes.DataColumns.DATA4, rs.getString(Notes.DataColumns.DATA4));
                cv.put(Notes.DataColumns.DATA5, rs.getString(Notes.DataColumns.DATA5));
                cr.insert(Notes.CONTENT_DATA_URI, cv);
            }
            Looper.prepare();
            Toast.makeText(context, "下载成功!", Toast.LENGTH_SHORT).show();
            Looper.loop();
        }
        catch (SQLException e) {
            System.out.println("MySQL操作错误");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        return false;
    }
}


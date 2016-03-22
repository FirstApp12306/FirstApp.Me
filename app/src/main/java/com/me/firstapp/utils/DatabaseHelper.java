package com.me.firstapp.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "FirstAppData";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "user";

    //构造函数，创建数据库
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME
                + " (id VARCHAR(30) PRIMARY KEY,"
                + " name VARCHAR(30)  NOT NULL,"
                + " phone VARCHAR(30) NOT NULL,"
                + " avatar VARCHAR(500),"
                + " password VARCHAR(30),"
                + " signature VARCHAR(100),"
                + " sex VARCHAR(2),"
                + " level NUMBER(50),"
                + " points NUMBER(50),"
                + " sts VARCHAR(2),"
                + " login_sts VARCHAR(2),"//01代表登陆，02代表未登陆
                + " city VARCHAR(30))";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(sql);
        onCreate(db);
    }
}

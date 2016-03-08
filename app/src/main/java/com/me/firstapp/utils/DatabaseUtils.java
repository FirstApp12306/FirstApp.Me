package com.me.firstapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class DatabaseUtils {
    Context context;
    DatabaseHelper dbhelper;
    public SQLiteDatabase sqlitedatabase;

    public DatabaseUtils(Context context) {
        super();
        this.context = context;
    }

    //打开数据库连接
    public void opendb(Context context) {
        dbhelper = new DatabaseHelper(context);
        sqlitedatabase = dbhelper.getWritableDatabase();
    }

    //关闭数据库连接
    public void closedb(Context context) {
        if(sqlitedatabase.isOpen()){
            sqlitedatabase.close();
        }
    }

    //插入表数据
    public void insert (String table_name,ContentValues values) {

        try{
            opendb(context);
            sqlitedatabase.insert(table_name, null, values);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closedb(context);
        }

    }

    //更新数据
    public int updateTable(String table_name,ContentValues values,String whereClause, String[] whereArgs) {
        int result = -1;
        try{
            opendb(context);
            result = sqlitedatabase.update(table_name, values, whereClause, whereArgs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closedb(context);
        }
        return result;
    }

    //删除某一条数据
    public void deleteWhere(String table_name,String whereClause, String[] whereArgs){
        try{
            opendb(context);
            sqlitedatabase.delete(table_name, whereClause, whereArgs);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            closedb(context);
        }

    }

    //删除表数据
    public void delete(String table_name) {
        deleteWhere(table_name, null, null);
    }
}

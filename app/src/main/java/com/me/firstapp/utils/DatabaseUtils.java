package com.me.firstapp.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.me.firstapp.entity.User;

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

    //查询登陆用户
    public User queryLoginUser(String userID, String loginSts){
        User user = new User();
        Cursor cursor = null;
        try{
            opendb(context);
            cursor = sqlitedatabase.rawQuery("select * from user where id = '"+userID+"' and login_sts = '"+loginSts+"'", null);
            while (cursor.moveToNext()) {
                user.user_id = userID;
                user.user_avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                user.user_city = cursor.getString(cursor.getColumnIndex("city"));
                user.user_level = cursor.getLong(cursor.getColumnIndex("level"));
                user.user_name = cursor.getString(cursor.getColumnIndex("name"));
                user.user_phone = cursor.getString(cursor.getColumnIndex("phone"));
                user.user_points = cursor.getLong(cursor.getColumnIndex("points"));
                user.user_sex = cursor.getString(cursor.getColumnIndex("sex"));
                user.user_signature = cursor.getString(cursor.getColumnIndex("signature"));
                user.sts = cursor.getString(cursor.getColumnIndex("sts"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
            closedb(context);
        }
        return user;
    }

    //查询用户
    public User queryUser(String userID){
        User user = new User();
        Cursor cursor = null;
        try{
            opendb(context);
            cursor = sqlitedatabase.rawQuery("select * from user where id = '"+userID+"'", null);
            while (cursor.moveToNext()) {
                user.user_id = userID;
                user.user_avatar = cursor.getString(cursor.getColumnIndex("avatar"));
                user.user_city = cursor.getString(cursor.getColumnIndex("city"));
                user.user_level = cursor.getLong(cursor.getColumnIndex("level"));
                user.user_name = cursor.getString(cursor.getColumnIndex("name"));
                user.user_phone = cursor.getString(cursor.getColumnIndex("phone"));
                user.user_points = cursor.getLong(cursor.getColumnIndex("points"));
                user.user_sex = cursor.getString(cursor.getColumnIndex("sex"));
                user.user_signature = cursor.getString(cursor.getColumnIndex("signature"));
                user.sts = cursor.getString(cursor.getColumnIndex("sts"));
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            cursor.close();
            closedb(context);
        }
        return user;
    }
}

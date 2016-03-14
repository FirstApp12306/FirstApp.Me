package com.me.firstapp.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.me.firstapp.application.MyApplication;
import com.me.firstapp.entity.images.ImageItem;

import java.io.File;
import java.util.ArrayList;

/**
 * 作者： FirstApp.Me.
 * 博客: WWW.FirstApp.Me
 * 微信: 1046566144
 * QQ: 1046566144
 * 描述:
 */
public class ImageUtils {
    public static final String NOTE_IMG_DIR = "sdcard/FirstApp/pictures/";
    public static final String AVATAR_IMG_DIR = "sdcard/FirstApp/avatars/";
    public static final int MAX_PIC_NUM = 1;// 发布话题时的选中的图片的最大数量
    public static int max = 0;
    public static ArrayList<ImageItem> tempSelectedImg = new ArrayList(); // 选择的图片的临时列表

    /**
     * 拍摄照片
     */
    public static File takePhoto(Activity activity, String dir) {
        File file = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {// 判断SD卡是否存在，并且是否具有读写权限
            File fileDir = new File(dir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();
            }
            file = new File(dir, System.currentTimeMillis() + ".jpg");

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
            try {
                activity.startActivityForResult(intent,MyApplication.REQUEST_CODE_TAKE_PHOTO);
            } catch (ActivityNotFoundException anf) {
                Toast.makeText(activity, "摄像头尚未准备好", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(activity, "暂无外部存储", Toast.LENGTH_SHORT).show();
        }
        return file;
    }
}

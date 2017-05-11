package com.viewhelperutil;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;

/**
 * 图片的加载类
 * Created by songlintao on 2016/12/8.
 */

public class ImageUtil {
    public static void setImage(Context context, Activity activity, Fragment fragment, ImageView iv, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) return;
        getGlide(context, activity, fragment)
                .load(imgUrl)
                .placeholder(R.drawable.viewhelperutils_place_img)
                .error(R.drawable.viewhelperutils_place_img)
                .fitCenter()
                .into(iv);
    }


    public static void setNoPHImage(Context context, Activity activity, Fragment fragment, ImageView iv, String imgUrl) {
        if (TextUtils.isEmpty(imgUrl)) return;
        getGlide(context, activity, fragment)
                .load(imgUrl)
                .fitCenter()
                .into(iv);
    }


    public static void setImageResize(Context context, Activity activity, Fragment fragment,int width,int height, ImageView iv, String uri) {
        if (uri == null) return;
        getGlide(context, activity, fragment)
                .load(uri).override(width,height)
                .fitCenter()
                .into(iv);
    }

    public static void setImage(Context context, Activity activity, Fragment fragment, ImageView iv, String imgUrl, int placeHolderId) {
        if (TextUtils.isEmpty(imgUrl)) return;
        getGlide(context, activity, fragment)
                .load(imgUrl)
                .placeholder(placeHolderId)
                .error(placeHolderId)
                .fitCenter()
                .into(iv);
    }

    public static void setImage(Context context, Activity activity, Fragment fragment, ImageView iv, String imgUrl, Drawable placeHolderDraw) {
        if (TextUtils.isEmpty(imgUrl)) return;
        getGlide(context, activity, fragment)
                .load(imgUrl)
                .placeholder(placeHolderDraw)
                .error(placeHolderDraw)
                .fitCenter()
                .into(iv);
    }

    public static void setImage(Context context, Activity activity, Fragment fragment, ImageView iv, String imgUrl, float corner_percent, Drawable placeHolderDraw) {
        if (TextUtils.isEmpty(imgUrl)) return;
        if (corner_percent <= 0f) {
            return;
        }
        getGlide(context, activity, fragment)
                .load(imgUrl)
                .placeholder(placeHolderDraw)
                .transform(new TransformUtils.RecWithRound(context, corner_percent))
                .error(placeHolderDraw)
                .into(iv);
    }

    public static void setImage(Context context, Activity activity, Fragment fragment, ImageView iv, String imgUrl, float corner_percent, @DrawableRes int placeHolderId) {
        if (TextUtils.isEmpty(imgUrl)) return;
        if (corner_percent <= 0f) {
            return;
        }
        getGlide(context, activity, fragment)
                .load(imgUrl)
                .placeholder(placeHolderId)
                .transform(new TransformUtils.RecWithRound(context, corner_percent))
                .error(placeHolderId)
                .into(iv);
    }

    public static void setImage(Context context, Activity activity, Fragment fragment, ImageView iv, String url, float corner_percent) {
        if (corner_percent <= 0f) {
            return;
        }
        if (TextUtils.isEmpty(url)) return;
        getGlide(context, activity, fragment)
                .load(url)
                .placeholder(R.drawable.viewhelperutils_place_img)
                .transform(new TransformUtils.RecWithRound(context, corner_percent))
                .error(R.drawable.viewhelperutils_place_img)
                .into(iv);
    }

    public static void setCropSquareImage(Context context, Activity activity, Fragment fragment, ImageView iv, String url) {
        if (TextUtils.isEmpty(url)) return;
        getGlide(context, activity, fragment)
                .load(url)
                .transform(new TransformUtils.CropSquareTransformation(context))
                .dontAnimate()
                .into(iv);
    }

    public static void setCropSquareImage(Context context, Activity activity, Fragment fragment, ImageView iv, String url, int placeHolderId) {
        if (TextUtils.isEmpty(url)) return;
        getGlide(context, activity, fragment)
                .load(url)
                .placeholder(placeHolderId)
                .transform(new TransformUtils.CropSquareTransformation(context)).dontAnimate()
                .into(iv);
    }

    public static void setCropSquareImage(Context context, Activity activity, Fragment fragment, ImageView iv, Uri uri) {
        if (uri == null) return;
        getGlide(context, activity, fragment)
                .load(uri)
                .centerCrop()
                .thumbnail(0.1f)
                .dontAnimate()
                .fitCenter()
                .into(iv);
    }

    public static void setImage(Context context, Activity activity, Fragment fragment, ImageView iv, File imageFile) {
        if (imageFile == null || !imageFile.exists() || imageFile.isDirectory()) return;
        getGlide(context, activity, fragment)
                .load(imageFile)
                .fitCenter()
                .into(iv);
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        //canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    private static RequestManager getGlide(Context context, Activity activity, Fragment fragment) {
        if (fragment != null) {
            return Glide.with(fragment);
        } else if (activity != null) {
            return Glide.with(activity);
        } else {
            return Glide.with(context);
        }
    }

}

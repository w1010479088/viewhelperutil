package com.viewhelperutil.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

/**
 * 图片圆角工具类
 */

public class TransformUtils {
    //圆角矩形图片
    public static class RecWithRound extends BitmapTransformation {
        private float percent;

        public RecWithRound(Context context) {
            super(context);
            percent = 1/8f;
        }

        public RecWithRound(Context context,float percent){
            super(context);
            this.percent = percent;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {

            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, source.getWidth()* percent, source.getHeight()* percent, paint);
            return result;
        }

        @Override
        public String getId() {
            return "roundcorner";
        }
    }

    public static class CropSquareTransformation extends BitmapTransformation {

        private int mWidth;
        private int mHeight;

        public CropSquareTransformation(Context context) {
            super(context);
        }

        public CropSquareTransformation(BitmapPool bitmapPool) {
            super(bitmapPool);
        }


        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap source, int outWidth, int outHeight) {
            if (source == null) return null;
            int size = Math.min(source.getWidth(), source.getHeight());
            mWidth = (source.getWidth() - size) / 2;
            mHeight = (source.getHeight() - size) / 2;
            Bitmap bitmap = Bitmap.createBitmap(source, mWidth, mHeight, size, size);
            if (bitmap != source) {
                source.recycle();
            }

            return bitmap;
        }

        @Override
        public String getId() {
            return "cropsquare";
        }
    }
}

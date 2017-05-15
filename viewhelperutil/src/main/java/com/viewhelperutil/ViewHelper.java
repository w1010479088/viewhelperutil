package com.viewhelperutil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.viewhelperutil.util.ImageUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * View属性设置的帮助类
 * Created by songlintao on 16/10/13.
 */

@SuppressWarnings("all")
public class ViewHelper {
    private View mContentView;
    private Context mContext;
    private ExecutorService workServices = Executors.newFixedThreadPool(2);
    private Handler mUIHandler;
    private SparseArray<View> mSubViews = new SparseArray<>();

    public Activity getActivity() {
        return mActivity;
    }

    public Fragment getFragment() {
        return mFragment;
    }

    private Activity mActivity;
    private Fragment mFragment;


    public ViewHelper(View view, Context context) {
        mContentView = view;
        mContext = context;
        mUIHandler = new Handler(this.mContext.getMainLooper());
    }

    public ViewHelper(Activity activity) {
        mContentView = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        mContext = activity;
        mActivity = activity;
        mUIHandler = new Handler(this.mContext.getMainLooper());
    }

    public ViewHelper(View view, Activity activity) {
        mContentView = view;
        mContext = activity;
        mActivity = activity;
        mUIHandler = new Handler(this.mContext.getMainLooper());
    }

    public ViewHelper(View view, Fragment fragment) {
        mContentView = view;
        mContext = fragment.getContext();
        mFragment = fragment;
        mUIHandler = new Handler(this.mContext.getMainLooper());
    }

    public Context getContext() {
        return mContext;
    }

    public <T extends View> T getView(int viewId) {
        if (viewId == 0) {
            return (T) mContentView;
        }
        View view = mSubViews.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mSubViews.put(viewId, view);
        }
        return (T) view;
    }

    public void clearClick(int... viewIds) {
        for (int i : viewIds) {
            setClick(i, null);
        }
    }

    public void setContentClick(View.OnClickListener clickListener) {
        mContentView.setOnClickListener(clickListener);
    }

    public void clearContentClick() {
        setContentClick(null);
    }

    public void clearColor(int... viewIds) {
        for (int i : viewIds) {
            getView(i).setSelected(false);
        }
    }

    public String setEditText(int viewId, String text) {
        EditText editText = getView(viewId);
        editText.setText(text == null ? "" : Html.fromHtml(text));
        return editText.getText().toString();
    }

    public String getEditText(int viewId) {
        EditText editText = getView(viewId);
        return editText.getText().toString();
    }

    public void setEditTextSelection(int viewId, int index){
        EditText editText = getView(viewId);
        editText.setSelection(index);
    }

    public void clearEditFocus(int viewId) {
        EditText editText = getView(viewId);
        editText.clearFocus();
    }

    public void setClick(int viewId, View.OnClickListener clickListener) {
        getView(viewId).setOnClickListener(clickListener);
    }

    public void setLongClick(int viewId, View.OnLongClickListener longClickListener) {
        getView(viewId).setOnLongClickListener(longClickListener);
    }

    public void setClick(View.OnClickListener clickListener, int... viewIds) {
        for (int id : viewIds) {
            getView(id).setOnClickListener(clickListener);
        }
    }

    public void setParentTouchDelegate(final int... viewIds) {
        postDelay(new Runnable() {
            @Override
            public void run() {
                for (int id : viewIds) {
                    View target = getView(id);
                    View parent = (View) target.getParent();
                    Rect rect = new Rect(0, 0, parent.getMeasuredWidth(), parent.getMeasuredHeight());
                    parent.setTouchDelegate(new TouchDelegate(rect, target));
                }
            }
        }, 20);
    }

    public View getContentView() {
        return mContentView;
    }

    public void setText(int viewId, String text) {
        View view = getView(viewId);
        TextView textView = (TextView) view;
        textView.setText(Html.fromHtml(text == null ? "" : text));
        textView.setVisibility(View.VISIBLE);
    }

    public void setSelect(boolean select, int viewId) {
        getView(viewId).setSelected(select);
    }

    public void clearText(int viewId) {
        View view = getView(viewId);
        TextView textView = (TextView) view;
        textView.setText(null);
    }

    public void setEnable(boolean enable, int... viewIds) {
        for (int i : viewIds) {
            getView(i).setEnabled(enable);
        }
    }

    public boolean enable(int viewId) {
        return getView(viewId).isEnabled();
    }

    public void setVisible(boolean isShow, int... viewIds) {
        for (int i : viewIds) {
            getView(i).setVisibility(isShow ? View.VISIBLE : View.GONE);
        }
    }

    public void setInVisible(boolean isShow, int... viewIds) {
        for (int i : viewIds) {
            getView(i).setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
        }
    }

    public void clearText(int... viewIds) {
        for (int i : viewIds) {
            clearText(i);
        }
    }

    public void setTextColor(int color, int... viewIds) {
        for (int id : viewIds) {
            TextView tv = getView(id);
            tv.setTextColor(color);
        }
    }

    public void setTextSize(int viewId, float dpValue) {
        TextView view = getView(viewId);
        view.setTextSize(dpValue);
    }

    public void setProgress(int viewId, int progress) {
        ProgressBar bar = getView(viewId);
        if (progress > 0) {
            bar.setProgress(progress);
        } else {
            bar.setProgress(0);
        }
    }

    public void clearProgress(int viewId) {
        ProgressBar bar = getView(viewId);
        bar.setProgress(0);
    }

    public void setTextResourceColor(@ColorRes int colorId, int... viewIds) {
        for (int id : viewIds) {
            TextView tv = getView(id);
            tv.setTextColor(mContext.getResources().getColor(colorId));
        }
    }

    public void setTextResourceColorStateList(@ColorRes int colorId, int... viewIds) {
        for (int id : viewIds) {
            TextView tv = getView(id);
            tv.setTextColor(mContext.getResources().getColorStateList(colorId));
        }
    }


    public void setBackGroundRes(@ColorRes int backGroundRes, int... viewIds) {
        for (int id : viewIds) {
            View view = getView(id);
            view.setBackgroundResource(backGroundRes);
        }
    }

    public void setBackGroundDrawable(Drawable backGroundDrawable, int... viewIds) {
        for (int id : viewIds) {
            View view = getView(id);
            view.setBackground(backGroundDrawable);
        }
    }

    public void setBackGroundColor(int color, int... viewIds) {
        for (int id : viewIds) {
            View view = getView(id);
            view.setBackgroundColor(color);
        }
    }

    public void clearImage(int viewId) {
        setImage(viewId, R.drawable.viewhelperutils_place_img);
    }

    public void clearImage(int... viewIds) {
        for (int i : viewIds) {
            clearImage(i);
        }
    }

    public ImageView setImage(int viewId, @DrawableRes int resource) {
        if (resource == -1) return null;
        ImageView imageView = getView(viewId);
        imageView.setImageResource(resource);
        return imageView;
    }

    public void setImage(int viewId, String url) {
        ImageView imageView = getView(viewId);
        ImageUtil.getSingleton().setImage(getContext(), mActivity, mFragment, imageView, url);
    }


    public void setImageResize(int viewId, int width, int height, String url) {
        ImageView imageView = getView(viewId);
        ImageUtil.getSingleton().setImageResize(getContext(), mActivity, mFragment, width, height, imageView, url);
    }

    public void setImage(int viewId, String url, @DrawableRes int placeHolderId) {
        ImageView imageView = getView(viewId);
        ImageUtil.getSingleton().setImage(getContext(), mActivity, mFragment, imageView, url, placeHolderId);
    }

    public void setImage(int viewId, String url, float corner_percent) {
        ImageView imageView = getView(viewId);
        ImageUtil.getSingleton().setImage(getContext(), mActivity, mFragment, imageView, url, corner_percent);
    }

    public void setImage(int viewId, String url, float corner_percent, Drawable plachHolder) {
        ImageView imageView = getView(viewId);
        ImageUtil.getSingleton().setImage(getContext(), mActivity, mFragment, imageView, url, corner_percent, plachHolder);
    }

    public void setImage(int viewId, File imageFile) {
        ImageView imageView = getView(viewId);
        ImageUtil.getSingleton().setImage(getContext(), mActivity, mFragment, imageView, imageFile);
    }

    public ImageView setImage(int viewId, Drawable drawable) {
        ImageView imageView = getView(viewId);
        imageView.setImageDrawable(drawable);
        return imageView;
    }

    public void setImage(int viewId, Bitmap bitmap) {
        ImageView imageView = getView(viewId);
        imageView.setImageBitmap(bitmap);
    }

    void setSelect(int viewId, boolean isSelected) {
        getView(viewId).setSelected(isSelected);
    }

    public String getTextViewText(int viewId) {
        return ((TextView) getView(viewId)).getText().toString();
    }

    public void setBackGroundRes(int[] backRes, int[] viewIds) {
        if (backRes.length != viewIds.length)
            throw new RuntimeException("backRes 和 viewIds 数量不一致");
        for (int i = 0; i < backRes.length; i++) {
            getView(viewIds[i]).setBackgroundResource(backRes[i]);
        }
    }

    public void setTextColor(int[] colorsId, int[] viewIds) {
        if (colorsId.length != viewIds.length)
            throw new RuntimeException("colorsId 和 viewIds 数量不一致");
        for (int i = 0; i < colorsId.length; i++) {
            ((TextView) getView(viewIds[i])).setTextColor(mContext.getResources().getColor
                    (colorsId[i]));
        }
    }

    @NonNull
    public String getString(int stringId) {
        return mContext.getResources().getString(stringId);
    }

    public <T extends View> T inflateView(ViewGroup container, int layoutId, boolean isAttach) {
        return (T) LayoutInflater.from(mContext).inflate(layoutId, container, isAttach);
    }

    public void clearWebViewResource(WebView webView) {
        if (webView != null) {
            webView.removeAllViews();
            ((ViewGroup) webView.getParent()).removeView(webView);
            webView.setTag(null);
            webView.clearHistory();
            webView.destroy();
        }
    }

    public void goBack(WebView webView) {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        }
    }

    public void postDelay(Runnable runnable, int delay) {
        getContentView().postDelayed(runnable, delay);
    }

    public void setTextChangeListener(int viewId, final BeforeTextChanged beforeTextChangedListener,
            final OnTextChanged onTextChangedListener, final AfterTextChanged
            afterTextChangedListener) {
        EditText editText = getView(viewId);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (beforeTextChangedListener != null)
                    beforeTextChangedListener.beforeTextChanged(s, start, count, after);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (onTextChangedListener != null)
                    onTextChangedListener.onTextChanged(s, start, before, count);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (afterTextChangedListener != null)
                    afterTextChangedListener.afterTextChanged(s);
            }
        });
    }

    public Uri openFileToUri(File file) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(mContext, mContext.getApplicationContext().getPackageName() + ".provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

    public Intent setFlags(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        return intent;
    }

    public void clearTextChangeListener(int viewId) {
        EditText editText = getView(viewId);

        try {
            Field mListeners = editText.getClass().getSuperclass().getSuperclass().getDeclaredField("mListeners");
            mListeners.setAccessible(true);
            Object obj = mListeners.get(editText);
            if (obj != null) {
                ArrayList<TextWatcher> listeners = (ArrayList<TextWatcher>) obj;
                listeners.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setOnEditorSearchListener(int viewId, final OnSearchActionListener listener) {
        EditText editText = getView(viewId);
        if (listener != null) {
            editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    if (EditorInfo.IME_ACTION_SEARCH == actionId) {
                        listener.search((EditText) v, v.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
        }

    }

    public void needFocus(int viewId){
        View view = getView(viewId);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
    }

    public void setWidth(int viewId, int widthPx) {
        getView(viewId).getLayoutParams().width = widthPx;
    }

    public void setHeight(int viewId, int heightPx) {
        getView(viewId).getLayoutParams().height = heightPx;
    }

    public void setNoPHImage(int viewId, String imageUrl) {
        ImageView imageView = getView(viewId);
        ImageUtil.getSingleton().setNoPHImage(getContext(), mActivity, mFragment, imageView, imageUrl);
    }

    public void setNoPHImage(ImageView imageView, String imageUrl) {
        ImageUtil.getSingleton().setNoPHImage(getContext(), mActivity, mFragment, imageView, imageUrl);
    }

    public interface BeforeTextChanged {
        void beforeTextChanged(CharSequence s, int start, int count, int after);
    }

    public interface OnTextChanged {
        void onTextChanged(CharSequence s, int start, int before, int count);
    }

    public interface AfterTextChanged {
        void afterTextChanged(Editable s);
    }

    public interface OnSearchActionListener {
        void search(EditText v, String word);
    }

    @SuppressWarnings("unused")
    public Rect getGlobalRect(View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);
        return rect;
    }

    public ViewHelper work(Runnable runOnWork) {
        workServices.submit(runOnWork);
        return this;
    }

    public ViewHelper ui(Runnable runOnUI) {
        mUIHandler.post(runOnUI);
        return this;
    }

    public void destroy() {
        workServices.shutdownNow();
        mUIHandler.removeCallbacksAndMessages(null);
    }
}

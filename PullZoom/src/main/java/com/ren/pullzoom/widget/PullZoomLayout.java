package com.ren.pullzoom.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.ren.pullzoom.R;

/**
 * Created by Ren on 2017/10/24.
 * TODO
 */

public class PullZoomLayout extends LinearLayout {

    private ImageView pullZoomImage;
    private ImageView refreshProgress;
    private ListView listView;
    private float pressY;
    private boolean canZoom;
    private boolean needRefresh;
    private int refrshSlop = 300;
    private ViewGroup.LayoutParams originalParams;
    private ValueAnimator rotationAnimator;

    private float damp;
    private int imageRes;
    private int imageHeight;
    private int scaleType;
    private boolean refreshEnable;

    public PullZoomLayout(Context context) {
        super(context);
        init(context);
    }

    public PullZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public PullZoomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.PullZoomLayout);
        damp = ta.getFloat(R.styleable.PullZoomLayout_damp, 3.0f);
        imageRes = ta.getResourceId(R.styleable.PullZoomLayout_image_res, 0);
        imageHeight = ta.getDimensionPixelSize(R.styleable.PullZoomLayout_image_height, dip2px(context, 210));
        scaleType = ta.getInt(R.styleable.PullZoomLayout_scale_type, -1);
        refreshEnable = ta.getBoolean(R.styleable.PullZoomLayout_refresh_enable, false);
        ta.recycle();
        refrshSlop = context.getResources().getDisplayMetrics().heightPixels / 5;
        Log.e("rq", "refrshSlop " + refrshSlop);
        init(context);
    }

    private void init(Context context) {
        RelativeLayout head = new RelativeLayout(context);
        ViewGroup.LayoutParams headParams = new ViewGroup.LayoutParams(-1, -2);
        head.setLayoutParams(headParams);

        pullZoomImage = new ImageView(context);
        switch (scaleType) {
            case 1:
                pullZoomImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                break;
            case 3:
                pullZoomImage.setScaleType(ImageView.ScaleType.FIT_CENTER);
                break;
            case 6:
                pullZoomImage.setScaleType(ImageView.ScaleType.FIT_XY);
                break;
        }
        pullZoomImage.setImageResource(imageRes);
        originalParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, imageHeight);
//        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(originalParams);
        pullZoomImage.setLayoutParams(originalParams);
        head.addView(pullZoomImage);

        refreshProgress = new ImageView(context);
        refreshProgress.setVisibility(GONE);
        refreshProgress.setImageResource(R.drawable.refresh);
        RelativeLayout.LayoutParams refreshParams = new RelativeLayout.LayoutParams(dip2px(context, 35), dip2px(context, 35));
        refreshParams.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        refreshProgress.setLayoutParams(refreshParams);
        head.addView(refreshProgress);

        addView(head, 0);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildAt(1) instanceof ListView) {
            listView = (ListView) getChildAt(1);
            listView.setOnScrollListener(scrollListener);
            listView.setOnTouchListener(touchListener);
        }
    }

    AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            int firstVisibleItem = listView.getFirstVisiblePosition();
            if (firstVisibleItem == 0 && scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                View firstView = getChildAt(0);
                canZoom = firstView != null && firstView.getTop() == 0;
            } else
                canZoom = false;
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        }
    };

    OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent ev) {
            if (pullZoomImage == null) return false;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressY = ev.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (canZoom)
                        return zoomView(ev);
                    break;
                case MotionEvent.ACTION_CANCEL:
                case MotionEvent.ACTION_UP:
                    if (canZoom)
                        restroe();
                    if (needRefresh && refreshListener != null) {
                        refreshListener.onRefresh();
                        rotationProgress();
                    } else
                        refreshProgress.setVisibility(GONE);
                    needRefresh = false;
                    canZoom = false;
                    break;

            }
            return false;
        }
    };

    private boolean zoomView(MotionEvent ev) {
        float offY = ev.getY() - pressY;
        if (offY <= 0 || offY < 16)
            return false;
        if (refreshEnable) {
            needRefresh = offY >= refrshSlop;
            if (needRefresh)
                refreshProgress.setVisibility(VISIBLE);
        }
        ViewGroup.LayoutParams params = pullZoomImage.getLayoutParams();
        float height = originalParams.height + offY / damp;
        params.height = (int) height;
        scaleImage(height);
        rotationProgress(offY);
        if (params.height >= originalParams.height)
            pullZoomImage.setLayoutParams(params);
        return true;
    }

    private void scaleImage(float height) {
//        if (pullZoomImage.getScaleType() == ImageView.ScaleType.CENTER_CROP)
//            return;
        float scale = (height - originalParams.height) / originalParams.height;
        pullZoomImage.setScaleX(1 + scale);
        pullZoomImage.setScaleY(1 + scale);
    }

    private void rotationProgress(float offy) {
        refreshProgress.setRotation(offy / 2);
    }

    private void restroe() {
        ValueAnimator animator = ValueAnimator.ofFloat(pullZoomImage.getLayoutParams().height, originalParams.height);// 动画更新的监听
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                float height = (float) arg0.getAnimatedValue();// 获取动画当前变化的值
                // 根据最新高度,更新布局高度
                ViewGroup.LayoutParams params = pullZoomImage.getLayoutParams();
                params.height = (int) height;
                scaleImage(height);
                pullZoomImage.setLayoutParams(params);
            }
        });
        animator.setDuration(200);// 动画时间
        animator.start();// 开启动画
    }


    private void rotationProgress() {
        rotationAnimator = ValueAnimator.ofFloat(0, 720);// 动画更新的监听
        rotationAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                float angel = (float) arg0.getAnimatedValue();// 获取动画当前变化的值
                // 根据最新高度,更新布局高度
                rotationProgress(angel);
            }
        });
        rotationAnimator.setDuration(1000);// 动画时间
        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.start();// 开启动画
    }

    private ImageView getImageView() {
        return pullZoomImage;
    }

    public void refreshComplete() {
        if (rotationAnimator.isRunning())
            rotationAnimator.end();
        refreshProgress.setVisibility(GONE);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public interface onRefreshListener {
        void onRefresh();
    }

    private onRefreshListener refreshListener;

    public void setOnRefreshListener(onRefreshListener listener) {
        this.refreshListener = listener;
    }
}

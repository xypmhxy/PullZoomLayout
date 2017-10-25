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

public class PullZoomLayout extends PullZoomBaseView {

    private ListView listView;

    public PullZoomLayout(Context context) {
        super(context);
    }

    public PullZoomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PullZoomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        /*获取listview*/
        if (getChildAt(1) instanceof ListView) {
            listView = (ListView) getChildAt(1);
            listView.setOnScrollListener(scrollListener);
            listView.setOnTouchListener(touchListener);
        }
    }

    /*listview滑动监听*/
    protected AbsListView.OnScrollListener scrollListener = new AbsListView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            /*判断是否滑动到顶部*/
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
    /*listview touchListener监听*/
    protected OnTouchListener touchListener = new OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent ev) {
            if (pullZoomImage == null) return false;
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    pressY = ev.getY();//获取按下的Y坐标
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (canZoom)//如果已经滑动到顶部并继续滑动则开始放大pullZoomImage
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
}
